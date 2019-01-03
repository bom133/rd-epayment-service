package th.go.rd.rdepaymentservice.controller;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import io.swagger.annotations.ApiOperation;
import th.go.rd.rdepaymentservice.component.AuthenticationFacade;
import th.go.rd.rdepaymentservice.constant.ApiCode;
import th.go.rd.rdepaymentservice.constant.LogLevel;
import th.go.rd.rdepaymentservice.constant.LogType;
import th.go.rd.rdepaymentservice.entity.EpayKbankToken;
import th.go.rd.rdepaymentservice.entity.EpayParameter;
import th.go.rd.rdepaymentservice.entity.EpayReceiverPaymentLine;
import th.go.rd.rdepaymentservice.entity.EpayTaxPaymentInfo;
import th.go.rd.rdepaymentservice.entity.EpayTaxPaymentInfoDetail;
import th.go.rd.rdepaymentservice.entity.EpayTaxPaymentOutbound;
import th.go.rd.rdepaymentservice.manager.RestManager;
import th.go.rd.rdepaymentservice.model.EpayLogModel;
import th.go.rd.rdepaymentservice.response.ResRdefReqCitiPaymentModel;
import th.go.rd.rdepaymentservice.response.ResRdefReqKbankPaymentModel;
import th.go.rd.rdepaymentservice.response.ResRdefReqPaymentDataModel;
import th.go.rd.rdepaymentservice.service.EpayKbankTokenService;
import th.go.rd.rdepaymentservice.service.EpayLogService;
import th.go.rd.rdepaymentservice.service.EpayParameterService;
import th.go.rd.rdepaymentservice.service.EpayReceiverPaymentLineService;
import th.go.rd.rdepaymentservice.service.EpayTaxPaymentOutBoundService;
import th.go.rd.rdepaymentservice.service.PkiService;
import th.go.rd.rdepaymentservice.util.DateHelper;
import th.go.rd.rdepaymentservice.util.GenXmlHelper;
import th.go.rd.rdepaymentservice.xml.Rd2BankEpaymentXmlModel;

@RestController
@RequestMapping("/epay/payment-out")
public class PaymentOutController {

	private static final Logger logger = LoggerFactory.getLogger(PaymentOutController.class);

	@Autowired
	private EpayKbankTokenService epayKbankTokenService;

	@Autowired
	private EpayParameterService epayParameterService;

	@Autowired
	private EpayTaxPaymentOutBoundService epayTaxPaymentOutBoundService;

	@Autowired
	private EpayReceiverPaymentLineService epayReceiverPaymentLineService;

	@Autowired
	private AuthenticationFacade authenticationFacade;

	@Autowired
	private EpayLogService epayLogService;
	
	@Autowired
	private PkiService pkiService;
	

	@ApiOperation(value = "Payment", notes = "สำหรับระบบบริการผู้เสียภาษีส่งข้อมูลการชำระไปยังช่องทางชำระที่เลือก")
	@PostMapping(value = "std/{uuid}", produces = "application/json", headers = "Accept-Language")
	public Object ePayRd2BankPaymentStd(@PathVariable String uuid, @RequestParam long recPaymentLineCode,
			HttpServletRequest request) throws JAXBException, IOException {
		String detail = String.format("ePayRd2BankPaymentStd [%s]", uuid);
		logger.info(detail);
		RestManager restManager = RestManager.getInstance();
		String username = authenticationFacade.getAuthentication().getName();
		EpayLogModel log = new EpayLogModel(LogLevel.INFO, LogType.PAYMENT, ApiCode.RD_2_BANK_PAYMENT_STD,
				 username, detail);

		Date today = new Date();

		// ----- Validate Payment Information
		restManager = this.paymentValidate(uuid, recPaymentLineCode);

		restManager.throwsException();

		GenXmlHelper genXmlHelper = new GenXmlHelper();

		/*------ Read E-Payment Information ----*/
		Optional<EpayTaxPaymentOutbound> epayTaxPaymentOutboundOpt = epayTaxPaymentOutBoundService.findByUuid(uuid);
		EpayTaxPaymentOutbound epayTaxPaymentOutbound = epayTaxPaymentOutboundOpt.isPresent()? 
				epayTaxPaymentOutboundOpt.get() : new EpayTaxPaymentOutbound();
		EpayTaxPaymentInfo epayTaxPaymentInfo = epayTaxPaymentOutbound.getEpayTaxPaymentInfo();
		Optional<EpayReceiverPaymentLine> epayReceiverPaymentLineOpt = epayReceiverPaymentLineService.findById(recPaymentLineCode);
		EpayReceiverPaymentLine epayReceiverPaymentLine = epayReceiverPaymentLineOpt.isPresent() ?
				epayReceiverPaymentLineOpt.get() : new EpayReceiverPaymentLine();
		/*------ End Read E-Payment Information ----*/

		/*------ Read Payment Std Parameter ------*/
		List<EpayParameter> lEpayParameter = epayParameterService.findByParamType("RD2BANK_STD");
		if (lEpayParameter.size() < 1) {
			restManager.addGlobalErrorbyProperty("app.man-resp.parameter_not_found");
			restManager.throwsException();
		}
		/*------ End Read Payment Std Parameter ------*/

		/*------ Gen Transaction no ------*/
		String transactionNo = epayTaxPaymentOutbound.getTranNo();
		String attachFile = epayTaxPaymentOutbound.getAttachFile();
		if (epayTaxPaymentOutbound.getTranNo() == null) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			String strDate = dateFormat.format(today);
			String runTransStr = epayTaxPaymentOutBoundService.getRunNoTransaction();
			transactionNo = String.format("%14.14s%s%s", strDate, epayTaxPaymentInfo.getRefNo(), runTransStr);
			// --- Gen AttachFile
			attachFile = String.format("RD2BANK-%s-%s-%14.14s.xml",
					epayReceiverPaymentLine.getMasterReceiverUnit().getRecShortNameEn(), transactionNo, strDate);
		}
		/*------ End Gen Transaction no ------*/

		String xmlString = new String();
		xmlString = genXmlHelper.ePayRdefReqPaymentXmlStd(transactionNo, epayTaxPaymentOutbound,
				epayReceiverPaymentLine, lEpayParameter);

		/*-------- Send to encrypt and add dsig ---------*/
		if(epayReceiverPaymentLine.getRecCertCode() == null || epayReceiverPaymentLine.getRdCertCode() == null) {
			restManager.addGlobalErrorbyProperty("app.man-resp.data_not_found");
			epayLogService.insertLog(restManager, log);
			restManager.throwsException();
		}
		
		int encCode = Integer.parseInt(epayReceiverPaymentLine.getRecCertCode());
		int signCode = Integer.parseInt(epayReceiverPaymentLine.getRdCertCode());
		String encryptData = null;
		try {
		
			encryptData = pkiService.dSigWithEncrypt(signCode, encCode, attachFile, xmlString);
			
		}catch(Exception e){
			restManager.addGlobalErrorbyProperty("app.man-resp.pki_sign_fail",  new Object[]{ e.getMessage() });
			epayLogService.insertLog(restManager, log);
			restManager.throwsException();
		}
		
		/*-------- Send to encrypt and add dsig ---------*/

		Rd2BankEpaymentXmlModel rd2BankEpaymentXmlModel = new Rd2BankEpaymentXmlModel();
		rd2BankEpaymentXmlModel.setAttachFile(attachFile);
		rd2BankEpaymentXmlModel.setMerchantId(epayReceiverPaymentLine.getMerchantId());
		rd2BankEpaymentXmlModel.setTerminalId(epayReceiverPaymentLine.getTerminalId());
		rd2BankEpaymentXmlModel.setEncryptData(encryptData);

		xmlString = genXmlHelper.convertToXmlString(rd2BankEpaymentXmlModel, true);
		
		ResRdefReqPaymentDataModel resRdefReqPaymentDataModel = new ResRdefReqPaymentDataModel();
		resRdefReqPaymentDataModel.setTerminalId(epayReceiverPaymentLine.getTerminalId());
		resRdefReqPaymentDataModel.setMerchantId(epayReceiverPaymentLine.getMerchantId());
		resRdefReqPaymentDataModel.setDataFile(xmlString);
		resRdefReqPaymentDataModel.setRedirectURL(epayReceiverPaymentLine.getRecRedirectUrl());
		System.err.println(resRdefReqPaymentDataModel.getDataFile());

		epayTaxPaymentOutbound.setTranNo(transactionNo);
		epayTaxPaymentOutbound.setTransmitDate(today);
		epayTaxPaymentOutbound.setEpayReceiverPaymentLine(epayReceiverPaymentLine);
		epayTaxPaymentOutbound.setAttachFile(attachFile);
		epayTaxPaymentOutbound.setUpdateBy("epay_user");
		epayTaxPaymentOutbound.setUpdateDate(today);

		try {
			epayTaxPaymentOutBoundService.save(epayTaxPaymentOutbound);
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			restManager.addGlobalErrorbyProperty("app.sys-resp.database_error");
		} finally {
			epayLogService.insertLog(restManager, log);
		}

		restManager.throwsException();
		epayLogService.insertLog(restManager, log);
		return restManager.addSuccess(resRdefReqPaymentDataModel);
	}

	@ApiOperation(value = "Payment", notes = "สำหรับระบบบริการผู้เสียภาษีส่งข้อมูลการชำระไป JP Morgan epayment")
	@PostMapping(value = "jp/{uuid}", produces = "application/json", headers = "Accept-Language")
	public Object ePayRd2JpPayment(@PathVariable String uuid, @RequestParam long recPaymentLineCode,
			HttpServletRequest request) throws JAXBException {
		String detail = String.format("ePayRd2JpPayment [%s]", uuid);
		logger.info(detail);
		RestManager restManager = RestManager.getInstance();
		String username = authenticationFacade.getAuthentication().getName();
		EpayLogModel log = new EpayLogModel(LogLevel.INFO, LogType.PAYMENT, ApiCode.RD_2_BANK_PAYMENT_STD,
				 username, detail);
		Date today = new Date();

		// ----- Validate Payment Information
		restManager = this.paymentValidate(uuid, recPaymentLineCode);

		restManager.throwsException();

		GenXmlHelper genXmlHelper = new GenXmlHelper();

		/*------ Read E-Payment Information ----*/
		Optional<EpayTaxPaymentOutbound> epayTaxPaymentOutboundOpt = epayTaxPaymentOutBoundService.findByUuid(uuid);
		EpayTaxPaymentOutbound epayTaxPaymentOutbound = epayTaxPaymentOutboundOpt.isPresent()? 
				epayTaxPaymentOutboundOpt.get() : new EpayTaxPaymentOutbound();
		EpayTaxPaymentInfo epayTaxPaymentInfo = epayTaxPaymentOutbound.getEpayTaxPaymentInfo();
		Optional<EpayReceiverPaymentLine> epayReceiverPaymentLineOpt = epayReceiverPaymentLineService.findById(recPaymentLineCode);
		EpayReceiverPaymentLine epayReceiverPaymentLine = epayReceiverPaymentLineOpt.isPresent() ?
				epayReceiverPaymentLineOpt.get() : new EpayReceiverPaymentLine();
		/*------ End Read E-Payment Information ----*/

		/*------ Read Payment Std Parameter ------*/
		List<EpayParameter> lEpayParameter = epayParameterService.findByParamType("RD2BANK_STD");
		if (lEpayParameter.size() < 1) {
			restManager.addGlobalErrorbyProperty("app.man-resp.parameter_not_found");
		}
		/*------ End Read Payment Std Parameter ------*/

		// Gen transaction no
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String strDate = dateFormat.format(today);
		String runTransStr = epayTaxPaymentOutBoundService.getRunNoTransaction();
		String transactionNo = String.format("%14.14s%s%s", strDate, epayTaxPaymentInfo.getRefNo(), runTransStr);
		System.out.println(transactionNo);
		// ===================

		String xmlString = new String();
		try {
			xmlString = genXmlHelper.ePayRdefReqPaymentXml(transactionNo, epayTaxPaymentOutbound,
					epayReceiverPaymentLine, lEpayParameter);
		} catch (Exception ex) {
			logger.error("context", ex);
		}

		String runFileStr = epayTaxPaymentOutBoundService.getRunNoFileXml();
		String attachFile = String.format("RD2BANK-%s%s%14.14s.xml",
				epayReceiverPaymentLine.getMasterReceiverUnit().getRecShortNameEn(), runFileStr, strDate);
		String converId = String.format("%s%s", epayReceiverPaymentLine.getMasterReceiverUnit().getRecShortNameEn(),
				runFileStr);

		RestTemplate restTemplate = new RestTemplate();
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("dataFile", xmlString);
		map.add("attachFile", attachFile);
		map.add("converId", converId);
		map.add("RDTrans_no", transactionNo);

		String result = restTemplate.postForObject(epayReceiverPaymentLine.getRecDirectUrl(), map, String.class);

		epayTaxPaymentOutbound.setTranNo(transactionNo);
		epayTaxPaymentOutbound.setTransmitDate(today);
		epayTaxPaymentOutbound.setEpayReceiverPaymentLine(epayReceiverPaymentLine);
		epayTaxPaymentOutbound.setAttachFile(attachFile);
		epayTaxPaymentOutbound.setUpdateBy("epay_user");
		epayTaxPaymentOutbound.setUpdateDate(today);

		try {
			epayTaxPaymentOutBoundService.save(epayTaxPaymentOutbound);
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			restManager.addGlobalErrorbyProperty("app.sys-resp.database_error");
		} finally {
			epayLogService.insertLog(restManager, log);
		}

		restManager.throwsException();
		return restManager.addSuccess(result);
	}

	@ApiOperation(value = "Payment", notes = "สำหรับระบบบริการผู้เสียภาษีส่งข้อมูลการชำระไป Deutsche epayment")
	@PostMapping(value = "deutche/{uuid}", produces = "application/json", headers = "Accept-Language")
	public Object ePayRd2DeutchePayment(@PathVariable String uuid, @RequestParam long recPaymentLineCode,
			HttpServletRequest request) throws JAXBException, IOException {
		String detail = String.format("ePayRd2DeutchePayment [%s]", uuid);
		logger.info(detail);
		RestManager restManager = RestManager.getInstance();
		String username = authenticationFacade.getAuthentication().getName();
		EpayLogModel log = new EpayLogModel(LogLevel.INFO, LogType.PAYMENT, ApiCode.RD_2_BANK_PAYMENT_STD,
				 username, detail);
		Date today = new Date();

		// ----- Validate Payment Information
		restManager = this.paymentValidate(uuid, recPaymentLineCode);

		restManager.throwsException();

		GenXmlHelper genXmlHelper = new GenXmlHelper();

		/*------ Read E-Payment Information ----*/
		Optional<EpayTaxPaymentOutbound> epayTaxPaymentOutboundOpt = epayTaxPaymentOutBoundService.findByUuid(uuid);
		EpayTaxPaymentOutbound epayTaxPaymentOutbound = epayTaxPaymentOutboundOpt.isPresent()? 
				epayTaxPaymentOutboundOpt.get() : new EpayTaxPaymentOutbound();
		EpayTaxPaymentInfo epayTaxPaymentInfo = epayTaxPaymentOutbound.getEpayTaxPaymentInfo();
		Optional<EpayReceiverPaymentLine> epayReceiverPaymentLineOpt = epayReceiverPaymentLineService.findById(recPaymentLineCode);
		EpayReceiverPaymentLine epayReceiverPaymentLine = epayReceiverPaymentLineOpt.isPresent() ?
				epayReceiverPaymentLineOpt.get() : new EpayReceiverPaymentLine();
		/*------ End Read E-Payment Information ----*/

		/*------ Read Payment Std Parameter ------*/
		List<EpayParameter> lEpayParameter = epayParameterService.findByParamType("RD2BANK_STD");
		if (lEpayParameter.size() < 1) {
			restManager.addGlobalErrorbyProperty("app.man-resp.parameter_not_found");
		}
		/*------ End Read Payment Std Parameter ------*/

		// Gen transaction no
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String strDate = dateFormat.format(today);
		String runTransStr = epayTaxPaymentOutBoundService.getRunNoTransaction();
		String transactionNo = String.format("%14.14s%s%s", strDate, epayTaxPaymentInfo.getRefNo(), runTransStr);
		System.out.println(transactionNo);
		// ===================

		String xmlString = new String();
		try {
			xmlString = genXmlHelper.ePayRdefReqPaymentXml(transactionNo, epayTaxPaymentOutbound,
					epayReceiverPaymentLine, lEpayParameter);
		} catch (Exception ex) {
			logger.error("context", ex);
		}

		String runFileStr = epayTaxPaymentOutBoundService.getRunNoFileXml();
		String attachFile = String.format("RD2BANK-%s%s%14.14s.xml",
				epayReceiverPaymentLine.getMasterReceiverUnit().getRecShortNameEn(), runFileStr, strDate);

		String deutschePath = new String("D:\\deutschexml\\");
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(deutschePath + attachFile);
			fileWriter.write(xmlString);
		} catch (Exception e) {
			logger.error("context", e);
		}
		finally {
			fileWriter.close();
		}

		epayTaxPaymentOutbound.setTranNo(transactionNo);
		epayTaxPaymentOutbound.setTransmitDate(today);
		epayTaxPaymentOutbound.setEpayReceiverPaymentLine(epayReceiverPaymentLine);
		epayTaxPaymentOutbound.setAttachFile(attachFile);
		epayTaxPaymentOutbound.setUpdateBy("epay_user");
		epayTaxPaymentOutbound.setUpdateDate(today);

		try {
			epayTaxPaymentOutBoundService.save(epayTaxPaymentOutbound);
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			restManager.addGlobalErrorbyProperty("app.sys-resp.database_error");
		} finally {
			epayLogService.insertLog(restManager, log);
		}

		restManager.throwsException();
		return restManager.addSuccess(attachFile);
	}

	@ApiOperation(value = "Payment", notes = "สำหรับระบบบริการผู้เสียภาษีส่งข้อมูลการชำระไป Citi epayment")
	@PostMapping(value = "citi/{uuid}", produces = "application/json", headers = "Accept-Language")
	public Object ePayRd2CitiPayment(@PathVariable String uuid, @RequestParam long recPaymentLineCode,HttpServletRequest request)
			throws JAXBException {
		String detail = String.format("ePayRd2CitiPayment [%s]", uuid);
		logger.info(detail);
		RestManager restManager = RestManager.getInstance();
		String username = authenticationFacade.getAuthentication().getName();
		EpayLogModel log = new EpayLogModel(LogLevel.INFO, LogType.PAYMENT, ApiCode.RD_2_BANK_PAYMENT_STD,
				 username, detail);
		Date today = new Date();

		// ----- Validate Payment Information
		restManager = this.paymentValidate(uuid, recPaymentLineCode);

		restManager.throwsException();

		GenXmlHelper genXmlHelper = new GenXmlHelper();

		/*------ Read E-Payment Information ----*/
		Optional<EpayTaxPaymentOutbound> epayTaxPaymentOutboundOpt = epayTaxPaymentOutBoundService.findByUuid(uuid);
		EpayTaxPaymentOutbound epayTaxPaymentOutbound = epayTaxPaymentOutboundOpt.isPresent()? 
				epayTaxPaymentOutboundOpt.get() : new EpayTaxPaymentOutbound();
		EpayTaxPaymentInfo epayTaxPaymentInfo = epayTaxPaymentOutbound.getEpayTaxPaymentInfo();
		Optional<EpayReceiverPaymentLine> epayReceiverPaymentLineOpt = epayReceiverPaymentLineService.findById(recPaymentLineCode);
		EpayReceiverPaymentLine epayReceiverPaymentLine = epayReceiverPaymentLineOpt.isPresent() ?
				epayReceiverPaymentLineOpt.get() : new EpayReceiverPaymentLine();
		/*------ End Read E-Payment Information ----*/

		/*------ Read Payment Std Parameter ------*/
		List<EpayParameter> lEpayParameter = epayParameterService.findByParamType("RD2BANK_STD");
		if (lEpayParameter.size() < 1) {
			restManager.addGlobalErrorbyProperty("app.man-resp.parameter_not_found");
		}
		/*------ End Read Payment Std Parameter ------*/

		// Gen transaction no
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String strDate = dateFormat.format(today);
		String runTransStr = epayTaxPaymentOutBoundService.getRunNoTransaction();
		String transactionNo = String.format("%14.14s%s%s", strDate, epayTaxPaymentInfo.getRefNo(), runTransStr);
		System.out.println(transactionNo);
		// ===================

		String xmlString = new String();
		try {
			xmlString = genXmlHelper.ePayRdefReqPaymentXml(transactionNo, epayTaxPaymentOutbound,
					epayReceiverPaymentLine, lEpayParameter);
		} catch (Exception ex) {
			logger.error("context", ex);
		}

		String runFileStr = epayTaxPaymentOutBoundService.getRunNoFileXml();
		String attachFile = String.format("RD2BANK-%s%s%14.14s.xml",
				epayReceiverPaymentLine.getMasterReceiverUnit().getRecShortNameEn(), runFileStr, strDate);

		ResRdefReqCitiPaymentModel resRdefReqCitiPaymentModel = new ResRdefReqCitiPaymentModel();
		resRdefReqCitiPaymentModel.setMenuId(
				epayParameterService.findByParamTypeAndParamCode("RD2BANK_CITI", "MenuId").get(0).getParamValue());
		resRdefReqCitiPaymentModel.setMerchartId(epayReceiverPaymentLine.getMerchantId());
		resRdefReqCitiPaymentModel.setAppData(xmlString);
		resRdefReqCitiPaymentModel.setRedirectURL(epayReceiverPaymentLine.getRecRedirectUrl());

		epayTaxPaymentOutbound.setTranNo(transactionNo);
		epayTaxPaymentOutbound.setTransmitDate(today);
		epayTaxPaymentOutbound.setEpayReceiverPaymentLine(epayReceiverPaymentLine);
		epayTaxPaymentOutbound.setAttachFile(attachFile);
		epayTaxPaymentOutbound.setUpdateBy("epay_user");
		epayTaxPaymentOutbound.setUpdateDate(today);

		try {
			epayTaxPaymentOutBoundService.save(epayTaxPaymentOutbound);
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			restManager.addGlobalErrorbyProperty("app.sys-resp.database_error");
		} finally {
			epayLogService.insertLog(restManager, log);
		}

		return restManager.addSuccess(resRdefReqCitiPaymentModel);
	}

	@ApiOperation(value = "Payment", notes = "สำหรับระบบบริการผู้เสียภาษีส่งข้อมูลการชำระไป Kbank epayment")
	@PostMapping(value = "kbank/{uuid}", produces = "application/json", headers = "Accept-Language")
	public Object epayRd2KbankPayment(@PathVariable String uuid, @RequestParam long recPaymentLineCode,HttpServletRequest request) {
		String logDetail = String.format("epayRd2KbankPayment [%s]", uuid);
		logger.info(logDetail);
		RestManager restManager = RestManager.getInstance();
		String username = authenticationFacade.getAuthentication().getName();
		EpayLogModel log = new EpayLogModel(LogLevel.INFO, LogType.PAYMENT, ApiCode.RD_2_BANK_PAYMENT_STD,
				 username, logDetail);
		Date today = new Date();

		// ----- Validate Payment Information
		restManager = this.paymentValidate(uuid, recPaymentLineCode);

		restManager.throwsException();

		/*------ Read E-Payment Information ----*/
		Optional<EpayTaxPaymentOutbound> epayTaxPaymentOutboundOpt = epayTaxPaymentOutBoundService.findByUuid(uuid);
		EpayTaxPaymentOutbound epayTaxPaymentOutbound = epayTaxPaymentOutboundOpt.isPresent()? 
				epayTaxPaymentOutboundOpt.get() : new EpayTaxPaymentOutbound();
		EpayTaxPaymentInfo epayTaxPaymentInfo = epayTaxPaymentOutbound.getEpayTaxPaymentInfo();
		Optional<EpayReceiverPaymentLine> epayReceiverPaymentLineOpt = epayReceiverPaymentLineService.findById(recPaymentLineCode);
		EpayReceiverPaymentLine epayReceiverPaymentLine = epayReceiverPaymentLineOpt.isPresent() ?
				epayReceiverPaymentLineOpt.get() : new EpayReceiverPaymentLine();
		/*------ End Read E-Payment Information ----*/

		List<EpayParameter> lEpayParameter = epayParameterService.findByParamType("RD2BANK_KBANK");
		;

		String transactionType = lEpayParameter.stream().filter(o -> o.getParamCode().equals("TransactionType"))
				.findFirst().orElse(new EpayParameter()).getParamValue();
		String extShortNo = lEpayParameter.stream().filter(o -> o.getParamCode().equals("ExtShortNo")).findFirst().orElse(new EpayParameter())
				.getParamValue();
		String submiterLoginName = lEpayParameter.stream().filter(o -> o.getParamCode().equals("SubmitterLoginName"))
				.findFirst().orElse(new EpayParameter()).getParamValue();
		String language = lEpayParameter.stream().filter(o -> o.getParamCode().equals("Language")).findFirst().orElse(new EpayParameter())
				.getParamValue();
		String paymentMode = lEpayParameter.stream().filter(o -> o.getParamCode().equals("PaymentMode")).findFirst()
				.orElse(new EpayParameter()).getParamValue();

		String header = String.format("0%-4.4s%-12.12s%50.50s%-15.15s%-4.4s", transactionType, extShortNo,
				submiterLoginName, language, paymentMode);
		System.out.println("header=" + header);

		String payeeCode = lEpayParameter.stream().filter(o -> o.getParamCode().equals("PayeeCode")).findFirst().orElse(new EpayParameter())
				.getParamValue();
		String payerCode = lEpayParameter.stream().filter(o -> o.getParamCode().equals("PayerCode")).findFirst().orElse(new EpayParameter())
				.getParamValue();
		String payerBankAccount = new String("");
		SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String timeStamp = timeStampFormat.format(today);
		SimpleDateFormat effectDateFormat = new SimpleDateFormat("yyyyMMdd");
		String effectDate = effectDateFormat.format(today);

		String detail = String.format("1%-20.20s%-12.12s%-30.30s %-20.20s%015.2f%-14.14s%-8.8s",
				epayTaxPaymentInfo.getRefNo(), payeeCode, payerCode, payerBankAccount,
				epayTaxPaymentInfo.getTotalAmount(), timeStamp, effectDate);
		System.out.println("detail=" + detail);

		String runTransStr = epayTaxPaymentOutBoundService.getRunNoTransaction();
		String transactionNo = String.format("%14.14s%s", timeStamp, runTransStr);
		String item = String.format("2%-100.100s%-100.100s%-100.100s%-100.100s", transactionNo,
				epayTaxPaymentInfo.getRefNo(),
				epayTaxPaymentInfo.getEpayTaxPaymentInfoDetails().stream().findFirst().orElse(new EpayTaxPaymentInfoDetail()).getPaymentMessage(),
				epayTaxPaymentInfo.getTaxMonthDisplay());
		System.out.println("item=" + item);

		String trailer = new String("91");

		List<EpayKbankToken> lEpayKbankToken = epayTaxPaymentInfo.getEpayKbankTokens();

		String result = new String();
		String tokenId = new String();

		if (lEpayKbankToken.isEmpty()) {
			System.out.println("get new token");

			RestTemplate restTemplate = new RestTemplate();
			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
			map.add("header", header);
			map.add("detail", detail);
			map.add("item", item);
			map.add("trailer", trailer);

			result = restTemplate.postForObject(epayReceiverPaymentLine.getRecDirectUrl(), map, String.class);

			int returnStatus = Integer.parseInt(result.substring(0, 1));

			if (returnStatus == 0) { // This process is successful
				tokenId = result.substring(1, 21); // get token
				EpayKbankToken epayKbankToken = new EpayKbankToken();
				epayKbankToken.setTokenId(tokenId);
				epayKbankToken.setCreateBy("epay_user");
				epayKbankToken.setCreateDate(today);
				epayKbankToken.setUpdateBy("epay_user");
				epayKbankToken.setUpdateDate(today);
				epayKbankToken.setEpayTaxPaymentInfo(epayTaxPaymentInfo);

				epayKbankTokenService.save(epayKbankToken);
			} else {
				restManager.addGlobalErrorbyProperty(result);
				restManager.throwsException();
			}
		} else {
			System.out.println("use exist token");

			EpayKbankToken epayKbankTokenExist = lEpayKbankToken.get(lEpayKbankToken.size() - 1); // Get last record
			tokenId = epayKbankTokenExist.getTokenId();
		}

		epayTaxPaymentOutbound.setTranNo(transactionNo);
		epayTaxPaymentOutbound.setTransmitDate(today);
		epayTaxPaymentOutbound.setEpayReceiverPaymentLine(epayReceiverPaymentLine);
		epayTaxPaymentOutbound.setAttachFile("");
		epayTaxPaymentOutbound.setUpdateBy("epay_user");
		epayTaxPaymentOutbound.setUpdateDate(today);

		try {
			epayTaxPaymentOutBoundService.save(epayTaxPaymentOutbound);
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			restManager.addGlobalErrorbyProperty("app.sys-resp.database_error");
		} finally {
			epayLogService.insertLog(restManager, log);
		}

		ResRdefReqKbankPaymentModel resRdefReqKbankPaymentModel = new ResRdefReqKbankPaymentModel();
		resRdefReqKbankPaymentModel.setHeader(header);
		resRdefReqKbankPaymentModel.setDetail(detail);
		resRdefReqKbankPaymentModel.setItem(item);
		resRdefReqKbankPaymentModel.setTrailer(trailer);
		resRdefReqKbankPaymentModel.setTokenId(tokenId);
		resRdefReqKbankPaymentModel.setResult(result);
		resRdefReqKbankPaymentModel.setRedirectURL(epayReceiverPaymentLine.getRecRedirectUrl());

		return restManager.addSuccess(resRdefReqKbankPaymentModel);
	}

	private RestManager paymentValidate(String uuid, long recPaymentLineCode) {
		RestManager restManager = RestManager.getInstance();
		Date today = new Date();
		DateHelper dateHelper = new DateHelper();

		Optional<EpayTaxPaymentOutbound> optionalPaymentOutbound = epayTaxPaymentOutBoundService.findByUuid(uuid);
		EpayTaxPaymentOutbound epayTaxPaymentOutbound = null;
		if (optionalPaymentOutbound.isPresent()) {
			epayTaxPaymentOutbound = optionalPaymentOutbound.get();
		}
		else {
			// throw new NotFoundException("Error not found uuid");
			restManager.addFieldErrorbyProperty("uuid", "app.man-resp.uuid_not_found", uuid);
			restManager.throwsException();
			return null;
		}
		
		if (!epayTaxPaymentOutbound.getMasterStatus().getStatus().equals("A")) {
			restManager.addGlobalErrorbyProperty("app.man-resp.payinslip_cancel");
			restManager.throwsException();
		}
		
		EpayTaxPaymentInfo epayTaxPaymentInfo = epayTaxPaymentOutbound.getEpayTaxPaymentInfo();
		if (dateHelper.isAfterDate(today, epayTaxPaymentInfo.getExpDate())) {
			// is expire date
			restManager.addGlobalErrorbyProperty("app.man-resp.expire");
			restManager.throwsException();
		}

		if (!epayTaxPaymentInfo.getMasterStatus().getStatus().equals("A")) {
			// refno has been cancel
			restManager.addGlobalErrorbyProperty("app.man-resp.cancel");
			restManager.throwsException();
		}

		if (epayTaxPaymentInfo.getEpayTaxPaymentInbound() != null) {
			restManager.addGlobalErrorbyProperty("app.man-resp.already_paid");
			restManager.throwsException();
		}

		Optional<EpayReceiverPaymentLine> optionalReceiverPaymentLine = epayReceiverPaymentLineService
				.findById(recPaymentLineCode);
		EpayReceiverPaymentLine epayReceiverPaymentLine = null;
		if (optionalReceiverPaymentLine.isPresent()) {
			epayReceiverPaymentLine = optionalReceiverPaymentLine.get();
		}
		else {
			restManager.addFieldErrorbyProperty("recPaymentLineCode", "app.man-resp.receiver_payment_line_not_found",
					Long.toString(recPaymentLineCode));
			restManager.throwsException();
			return null;
		}
		Date startDate = epayReceiverPaymentLine.getStartDate();
		Date endDate = epayReceiverPaymentLine.getEndDate();
		if ((startDate != null) && (dateHelper.isBeforeDate(today, startDate))) {
			restManager.addGlobalErrorbyProperty("app.man-resp.receiver_payment_line_not_enable");
			restManager.throwsException();
		}

		if ((endDate != null) && (dateHelper.isAfterDate(today, endDate))) {
			restManager.addGlobalErrorbyProperty("app.man-resp.receiver_payment_line_not_enable");
			restManager.throwsException();
		}

		if (!epayReceiverPaymentLine.getMasterStatus().getStatus().equals("A")) {
			restManager.addGlobalErrorbyProperty("app.man-resp.receiver_payment_line_invalid_status");
			restManager.throwsException();
		}

		if (epayTaxPaymentOutbound.getTranNo() != null) {
			restManager.addGlobalErrorbyProperty("app.man-resp.already_active");
			restManager.throwsException();
		}
		
		return restManager;
	}
}
