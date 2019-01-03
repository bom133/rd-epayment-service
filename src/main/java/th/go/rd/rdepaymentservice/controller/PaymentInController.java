package th.go.rd.rdepaymentservice.controller;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBException;

import org.apache.commons.lang.StringEscapeUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import th.go.rd.rdepaymentservice.component.AuthenticationFacade;
import th.go.rd.rdepaymentservice.constant.ApiCode;
import th.go.rd.rdepaymentservice.constant.LogLevel;
import th.go.rd.rdepaymentservice.constant.LogType;
import th.go.rd.rdepaymentservice.entity.EpayReceiverPaymentLine;
import th.go.rd.rdepaymentservice.entity.EpayTaxPaymentInbound;
import th.go.rd.rdepaymentservice.entity.EpayTaxPaymentInboundDetail;
import th.go.rd.rdepaymentservice.entity.EpayTaxPaymentInboundRefOutbound;
import th.go.rd.rdepaymentservice.entity.EpayTaxPaymentInfo;
import th.go.rd.rdepaymentservice.entity.EpayTaxPaymentOutbound;
import th.go.rd.rdepaymentservice.entity.MasterPaymentLine;
import th.go.rd.rdepaymentservice.entity.MasterPaymentStatus;
import th.go.rd.rdepaymentservice.entity.MasterReceiverUnit;
import th.go.rd.rdepaymentservice.entity.MasterSendMethod;
import th.go.rd.rdepaymentservice.manager.RestManager;
import th.go.rd.rdepaymentservice.model.EpayLogModel;
import th.go.rd.rdepaymentservice.service.EpayLogService;
import th.go.rd.rdepaymentservice.service.EpayReceiverPaymentLineService;
import th.go.rd.rdepaymentservice.service.PaymentInService;
import th.go.rd.rdepaymentservice.service.PkiService;
import th.go.rd.rdepaymentservice.util.DateHelper;
import th.go.rd.rdepaymentservice.util.GenXmlHelper;
import th.go.rd.rdepaymentservice.xml.CSConfPaymentDetailModel;
import th.go.rd.rdepaymentservice.xml.CSConfPaymentStatusModel;
import th.go.rd.rdepaymentservice.xml.CSConfirmPaymentInfoModel;
import th.go.rd.rdepaymentservice.xml.CSConfirmPaymentModel;
import th.go.rd.rdepaymentservice.xml.Rd2BankEpaymentXmlModel;
import th.go.rd.rdepaymentservice.xml.ReqBillPayCSConfirmPayment;
import th.go.rd.rdepaymentservice.xml.ReqBillPaymentInformation;
import th.go.rd.rdepaymentservice.xml.ReqBillStatusModel;
import th.go.rd.rdepaymentservice.xml.ReqEpaymentXmlModel;
import th.go.rd.rdepaymentservice.xml.ResBillStatusInfoModel;
import th.go.rd.rdepaymentservice.xml.ResBillStatusModel;
import th.go.rd.rdepaymentservice.xml.ResEpaymentXmlModel;
import th.go.rd.rdepaymentservice.xml.ResPaymentInformation;
import th.go.rd.rdepaymentservice.xml.ResRDEFRespConfirmPayment;

@RestController
@RequestMapping("/epay/payment-in")
public class PaymentInController {

	private static Logger logger = LoggerFactory.getLogger(PaymentInController.class);

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private PaymentInService paymentInService;

	@Autowired
	private AuthenticationFacade authenticationFacade;
	
	@Autowired
	private EpayReceiverPaymentLineService epayReceiverPaymentLineService;

	@Autowired
	private EpayLogService epayLogService;
	
	@Autowired
	private PkiService pkiService;

	@Value("${spring.payment-std.xml-path}")
	private String writeXmlPath;

	@ApiOperation(value = "Payment", notes = "สำหรับหน่วยรับชำระสอบถามข้อมูลสถานะของชุดชำระภาษี")
	@PostMapping(value = "check-status-payin", consumes = "application/xml", produces = "application/xml", headers = {
			"Accept-Language" })
	public ResEpaymentXmlModel checkPaymentStatus(@RequestBody ReqEpaymentXmlModel xml, HttpServletRequest request)
			throws JAXBException, ParseException, IOException {
		String detail = String.format("Check Status Payin [%s]", xml.getBankCode());
		logger.info(detail);
		RestManager restManager = RestManager.getInstance();
		DateHelper dateHelper = new DateHelper();
		
		EpayLogModel log = new EpayLogModel(LogLevel.INFO, LogType.PAYMENT, ApiCode.CHECK_STATUS_PAYMENT,
				 xml.getBankCode(), detail);
		Date today = new Date();
		ResEpaymentXmlModel resEpaymentXmlModel = new ResEpaymentXmlModel();

		Optional<MasterReceiverUnit> oMasReceiverUnit = paymentInService.findByRecCode(xml.getBankCode());
		if (!oMasReceiverUnit.isPresent()) {
			String errMsg = "ไม่สำเร็จ  [E07100[receiver_unit]]";
			resEpaymentXmlModel.setResponseCode("E07000");
			resEpaymentXmlModel.setResponseMessage(errMsg);
			log.setError("E07000", errMsg);
			epayLogService.insertLog(log);
			return resEpaymentXmlModel;
		}
		MasterReceiverUnit masReceiverUnit = oMasReceiverUnit.get();

		Optional<MasterPaymentLine> oMasPaymentLine = paymentInService.findByPayLineCode(xml.getPaymentLine());
		if (!oMasPaymentLine.isPresent()) {
			String errMsg = "ไม่สำเร็จ [E07100[payment_line]]";
			resEpaymentXmlModel.setResponseCode("E07000");
			resEpaymentXmlModel.setResponseMessage(errMsg);
			log.setError("E07000", errMsg);
			epayLogService.insertLog(log);
			return resEpaymentXmlModel;
		}
		MasterPaymentLine masPaymentLine = oMasPaymentLine.get();

		List<EpayReceiverPaymentLine> oEpayReceiverPaymentLine = paymentInService
				.findByMasterPaymentLineAndMasterReceiverUnit(masPaymentLine, masReceiverUnit);
		if (oEpayReceiverPaymentLine.size() < 1) {
			String errMsg = "ไม่สำเร็จ [E07110[rec_pay_line]]";
			resEpaymentXmlModel.setResponseCode("E07000");
			resEpaymentXmlModel.setResponseMessage(errMsg);
			log.setError("E07000", errMsg);
			epayLogService.insertLog(log);
			return resEpaymentXmlModel;
		}
		EpayReceiverPaymentLine epayReceiverPaymentLine = oEpayReceiverPaymentLine.get(0);
		Date startDate = epayReceiverPaymentLine.getStartDate();
		Date endDate = epayReceiverPaymentLine.getEndDate();
		if ((startDate != null) && (dateHelper.isBeforeDate(today, startDate))) {
			String errMsg = "ไม่สำเร็จ [E07105]";
			resEpaymentXmlModel.setResponseCode("E07000");
			resEpaymentXmlModel.setResponseMessage(errMsg);
			log.setError("E07000", errMsg);
			epayLogService.insertLog(log);
			return resEpaymentXmlModel;
		}

		if ((endDate != null) && (dateHelper.isAfterDate(today, endDate))) {
			String errMsg = "ไม่สำเร็จ [E07105]";
			resEpaymentXmlModel.setResponseCode("E07000");
			resEpaymentXmlModel.setResponseMessage(errMsg);
			log.setError("E07000", errMsg);
			epayLogService.insertLog(log);
			return resEpaymentXmlModel;
		}

		if (!epayReceiverPaymentLine.getMasterStatus().getStatus().equals("A")) {
			String errMsg = "ไม่สำเร็จ [E07106]";
			resEpaymentXmlModel.setResponseCode("E07000");
			resEpaymentXmlModel.setResponseMessage(errMsg);
			log.setError("E07000", errMsg);
			epayLogService.insertLog(log);
			return resEpaymentXmlModel;
		}
		
		/*-------- Send to decrypt and add dsig ---------*/
		if(epayReceiverPaymentLine.getRecCertCode() == null || epayReceiverPaymentLine.getRdCertCode() == null) {
			restManager.addGlobalErrorbyProperty("app.man-resp.data_not_found");
			restManager.throwsException();
		}
		
		int recCode = Integer.parseInt(epayReceiverPaymentLine.getRecCertCode());
		int rdCode = Integer.parseInt(epayReceiverPaymentLine.getRdCertCode());

		String decrptSrc = StringEscapeUtils.unescapeXml(xml.getEncryptData());
		String decrptXml =  null;
		
		try {
			decrptXml = pkiService.verifyWithDecrypt(recCode, rdCode, xml.getAttachFile(), decrptSrc);		
		}catch(Exception e){
			restManager.addGlobalErrorbyProperty("app.man-resp.pki_verify_fail",  new Object[]{ e.getMessage() });
			restManager.throwsException();
		}
		/*-------- Send to decrypt and add dsig ---------*/


		
		GenXmlHelper genXmlHelper = new GenXmlHelper();

		ReqBillStatusModel reqBillStatusModel = new ReqBillStatusModel();

		reqBillStatusModel = (ReqBillStatusModel) genXmlHelper.convertToXmlObject(decrptXml, reqBillStatusModel);
		/*------- Send to decrypt -------*/

		Optional<EpayTaxPaymentOutbound> oEpayTaxPaymentOutbound = paymentInService
				.findByCtlCode(reqBillStatusModel.getBill().getRef2());
		if (!oEpayTaxPaymentOutbound.isPresent()) {
			String errMsg = "ไม่สำเร็จ [E07113[REF2]]";
			resEpaymentXmlModel.setResponseCode("E07000");
			resEpaymentXmlModel.setResponseMessage(errMsg);
			log.setError("E07000", errMsg);
			epayLogService.insertLog(log);
			return resEpaymentXmlModel;
		}

		EpayTaxPaymentOutbound epayTaxPaymentOutbound = oEpayTaxPaymentOutbound.get();

		EpayTaxPaymentInfo epayTaxPaymentInfo = epayTaxPaymentOutbound.getEpayTaxPaymentInfo();

		ModelMapper modelMapper = new ModelMapper();
		ResBillStatusInfoModel resBillStatusInfoModel = modelMapper.map(reqBillStatusModel.getBill(),
				ResBillStatusInfoModel.class);

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String transmitDate = dateFormat.format(today);
		resBillStatusInfoModel.setRdTransactionNo(epayTaxPaymentOutbound.getTranNo());
		resBillStatusInfoModel.setTransmitDate(transmitDate);
		dateFormat = new SimpleDateFormat("yyyyMMdd");
		String expDate = dateFormat.format(epayTaxPaymentInfo.getExpDate());
		resBillStatusInfoModel.setExpDate(expDate);

		// String sClientTransmitDate =
		// reqBillStatusModel.getBill().getTransmitDate()==null ?
		// "": reqBillStatusModel.getBill().getTransmitDate();
		// Date clientTransmitDate = new
		// SimpleDateFormat("yyyyMMddHHmmss").parse(sClientTransmitDate);

		if (epayTaxPaymentOutbound.getMasterStatus().getStatus().equals("I")) {
			String errMsg = "ไม่สำเร็จ [E07112]";
			resEpaymentXmlModel.setResponseCode("E07000");
			resEpaymentXmlModel.setResponseMessage(errMsg);
			log.setError("E07000", errMsg);
		} else if (!xml.getBankCode().equals(reqBillStatusModel.getBill().getBankCode())) {
			String errMsg = "ไม่สำเร็จ [E07113[bank_code miss match]]";
			resEpaymentXmlModel.setResponseCode("E07000");
			resEpaymentXmlModel.setResponseMessage(errMsg);
			log.setError("E07000", errMsg);
		} else if (!xml.getPaymentLine().equals(reqBillStatusModel.getBill().getPaymentLine())) {
			String errMsg = "ไม่สำเร็จ [E07113[payment line miss match]]";
			resEpaymentXmlModel.setResponseCode("E07000");
			resEpaymentXmlModel.setResponseMessage(errMsg);
			log.setError("E07000", errMsg);
		}
		/*
		 * else if (!DateUtils.isSameDay(clientTransmitDate, today)) { String errMsg =
		 * "ไม่สำเร็จ [E07113]"; resEpaymentXmlModel.setResponseCode("E07000");
		 * resEpaymentXmlModel.setResponseMessage(errMsg); logger.info("case5"); }
		 */
		else if (epayTaxPaymentInfo.getTotalAmount().compareTo(reqBillStatusModel.getBill().getTotalAmout()) != 0) {
			String errMsg = "ไม่สำเร็จ [E07100[total_amount]]";
			resEpaymentXmlModel.setResponseCode("E07000");
			resEpaymentXmlModel.setResponseMessage(errMsg);
			log.setError("E07000", errMsg);
		} else if (!epayTaxPaymentInfo.getAgentId().equals(reqBillStatusModel.getBill().getRef1())) {
			String errMsg = "ไม่สำเร็จ [E07100[REF1]]";
			resEpaymentXmlModel.setResponseCode("E07000");
			resEpaymentXmlModel.setResponseMessage(errMsg);
			log.setError("E07000", errMsg);
		} else if (dateHelper.isAfterDate(today, epayTaxPaymentInfo.getExpDate())) {
			// is expire date
			String errMsg = "ไม่สำเร็จ [E07108]";
			resEpaymentXmlModel.setResponseCode("E07000");
			resEpaymentXmlModel.setResponseMessage(errMsg);
			log.setError("E07000", errMsg);
		} else if (epayTaxPaymentInfo.getMasterStatus().getStatus().equals("I")) {
			// refno has been cancel
			String errMsg = "ไม่สำเร็จ [E07110]";
			resEpaymentXmlModel.setResponseCode("E07000");
			resEpaymentXmlModel.setResponseMessage(errMsg);
			log.setError("E07000", errMsg);
		} else if (epayTaxPaymentInfo.getEpayTaxPaymentInbound() != null) {
			String errMsg = "ไม่สำเร็จ [E07109]";
			resEpaymentXmlModel.setResponseCode("E07000");
			resEpaymentXmlModel.setResponseMessage(errMsg);
			log.setError("E07000", errMsg);
		} else {
			String errMsg = "สำเร็จ สามารถชำระภาษีได้";
			resEpaymentXmlModel.setResponseCode("I07000");
			resEpaymentXmlModel.setResponseMessage(errMsg);
			log.setError("I07000", errMsg);
			ResBillStatusModel resBillStatusModel = new ResBillStatusModel(resBillStatusInfoModel);
			String xmlEncrypt = genXmlHelper.convertToXmlString(resBillStatusModel, true);
			
			/*-------- Send to decrypt and add dsig ---------*/
			if(epayReceiverPaymentLine.getRecCertCode() == null || epayReceiverPaymentLine.getRdCertCode() == null) {
				restManager.addGlobalErrorbyProperty("app.man-resp.data_not_found");
				epayLogService.insertLog(log);
				restManager.throwsException();
			}
			
			recCode = Integer.parseInt(epayReceiverPaymentLine.getRecCertCode());
			rdCode = Integer.parseInt(epayReceiverPaymentLine.getRdCertCode());
			String encryptData = null;
			
			try {
				encryptData = pkiService.dSigWithEncrypt(rdCode, recCode, "tmp", xmlEncrypt);		
			}catch(Exception e){
				restManager.addGlobalErrorbyProperty("app.man-resp.pki_sign_fail",  new Object[]{ e.getMessage() });
				epayLogService.insertLog(log);
				restManager.throwsException();
			}
			
			
			/*-------- Send to decrypt and add dsig ---------*/
			
			resEpaymentXmlModel.setEncryptData(encryptData);
		}
		epayLogService.insertLog(restManager, log);
		restManager.throwsException();
		return resEpaymentXmlModel;
	}

	@ApiOperation(value = "EPayUpdatePaymentStatus", notes = "หน่วยรับชำระภาษีส่งข้อความแจ้งผลการชำระภาษีผ่าน Web Service ให้กับกรมสรรพากร ข้อความจะถูกส่งหลังจากผู้เสียภาษียืนยันการชำระเงิน จากนั้นสามารถทำการ Redirect กลับมายังหน้าจอของกรมสรรพากรตามที่กำหนดได้")
	@PostMapping(value = "adjust-status-epay-direct", consumes = "application/xml", produces = "application/xml", headers = "Accept-Language")
	public Object updatePaymentStatusDirect(@RequestBody ReqEpaymentXmlModel reqXml, HttpServletRequest request)
			throws JAXBException, IOException {
		String logDetail = String.format("Adjust Status Epay Direct [%s]", reqXml.getBankCode());
		logger.info(logDetail);
		EpayLogModel log = new EpayLogModel(LogLevel.INFO, LogType.PAYMENT, ApiCode.CHECK_STATUS_PAYMENT,
				reqXml.getBankCode(), logDetail);
		GenXmlHelper genXmlHelper = new GenXmlHelper();
		Authentication authentication = authenticationFacade.getAuthentication();
		String USERNAME = authentication.getName();
		String INBOUND_UUID = UUID.randomUUID().toString();
		String resEncryptXml = "";
		String ResponseCode = "";
		String responseMessage = "";
		String errCode = "";
		String errMsg = "";
//		try {
			// Variables Declaration
			String stringXml = "";
			String bankCode = "";
			String paymentLine = "";
			String attachFile = "";
			bankCode = reqXml.getBankCode();
			paymentLine = reqXml.getPaymentLine();
			attachFile = reqXml.getAttachFile();
			/*-------- Validate Field ---------*/
			if (bankCode.equals("") || bankCode.length() > 3 || bankCode.length() <= 0) {
				errCode = "E07113";
				errMsg = messageSource.getMessage("app.man-resp.invalid_field", new Object[] { "BankCode" },
						LocaleContextHolder.getLocale());
			}
			else if (paymentLine.equals("") || paymentLine.length() > 10 || paymentLine.length() <= 0) {
				 errCode = "E07113";
				 errMsg = messageSource.getMessage("app.man-resp.invalid_field", new Object[] { "PaymentLine" },
						LocaleContextHolder.getLocale());
			}
			else if (attachFile.equals("") || attachFile.length() > 100 || attachFile.length() <= 0) {
				 errCode = "E07113";
				 errMsg = messageSource.getMessage("app.man-resp.invalid_field", new Object[] { "AttachFile" },
						LocaleContextHolder.getLocale());
			}
			else if (reqXml.getEncryptData() == null) {
				 errCode = "E07113";
				 errMsg = messageSource.getMessage("app.man-resp.invalid_field",
						new Object[] { "EncryptData not found" }, LocaleContextHolder.getLocale());
			}
			if(!errCode.equals("")) 
			{
				log.setError(errCode, errMsg);
				epayLogService.insertLog(log);
				return setXml(errCode, errMsg, "", "");
			}
			
			
			//---- Check bank & payment line
			Optional<MasterReceiverUnit> recEnt = paymentInService.findByRecCode(bankCode);
			if (!recEnt.isPresent()) {
				 errCode = "E07100";
				 errMsg = messageSource.getMessage("app.man-resp.data_not_found_with_arg",
						new Object[] { "BankCode" }, LocaleContextHolder.getLocale());
				log.setError(errCode, errMsg);
				epayLogService.insertLog(log);
				return setXml(errCode, errMsg, "", "");
			}
			MasterReceiverUnit masterReceiverUnit = recEnt.get();
			Optional<MasterPaymentLine> lineEnt = paymentInService.findByPayLineCode(paymentLine);
			if (!lineEnt.isPresent()) {
				 errCode = "E07100";
				 errMsg = messageSource.getMessage("app.man-resp.data_not_found_with_arg",
						new Object[] { "paymentLine" }, LocaleContextHolder.getLocale());
				log.setError(errCode, errMsg);
				epayLogService.insertLog(log);
				return setXml(errCode, errMsg, "", "");
			}
			MasterPaymentLine masPaymentLine = lineEnt.get();
			
			List<EpayReceiverPaymentLine> epayReceiverPaymentLineOpt = epayReceiverPaymentLineService.findByMasterPaymentLineAndMasterReceiverUnit(masPaymentLine, masterReceiverUnit);
			
				
			if(epayReceiverPaymentLineOpt.size() == 0) {
				 errCode = "E07100";
				 errMsg = messageSource.getMessage("app.man-resp.data_not_found_with_arg",
						new Object[] { "BankCode , Payment Line" }, LocaleContextHolder.getLocale());
				log.setError(errCode, errMsg);
				epayLogService.insertLog(log);
				return setXml(errCode, errMsg, "", "");
			}
			//---- end Check bank & payment line	
			
			
			/*-------- Send to decrypt and add dsig ---------*/
			EpayReceiverPaymentLine epayReceiverPaymentLine = epayReceiverPaymentLineOpt.get(0);
			
			int recCode = Integer.parseInt(epayReceiverPaymentLine.getRecCertCode());
			int rdCode = Integer.parseInt(epayReceiverPaymentLine.getRdCertCode());

			String decrptXmlEscap = StringEscapeUtils.unescapeXml(reqXml.getEncryptData());
			String decryptData = null;
			
			try {
				decryptData = pkiService.verifyWithDecrypt(recCode, rdCode, attachFile, decrptXmlEscap);		
			}catch(Exception e){
				
				errCode = "E07100";
				errMsg = messageSource.getMessage("app.man-resp.pki_verify_fail",
						new Object[] { e.getMessage() }, LocaleContextHolder.getLocale());
				 
				log.setError(errCode, errMsg);
				epayLogService.insertLog(log);
				return setXml(errCode, errMsg, "", "");
			}
			/*-------- Send to decrypt and add dsig ---------*/
			
			
			
			if(decryptData.substring(0,5).equals("ERROR"))
			{
				errCode = "E07003";
				errMsg = "ไม่สามารถถอดรหัส / ตรวจสอบลายมือชื่อข้อมูล XML";
				log.setError(errCode, errMsg);
				epayLogService.insertLog(log);
				return setXml(errCode, errMsg, "", "");
			}
			CSConfirmPaymentModel cSConfirmPaymentModel = new CSConfirmPaymentModel();
			cSConfirmPaymentModel = (CSConfirmPaymentModel) genXmlHelper.convertToXmlObject(decryptData,
					cSConfirmPaymentModel);
			
			
			/*------- Send to decrypt -------*/

			String tranNo = "";
			long infoId = 0;
			Date now = new Date();
			int count = 0;
			CSConfirmPaymentInfoModel payModel = cSConfirmPaymentModel.getEpay();
			CSConfPaymentStatusModel status = cSConfirmPaymentModel.getPaymentStatus();
			CSConfPaymentDetailModel[] detail = payModel.getPaymentDetail();
			count = detail.length;
			BigDecimal amount;

			/*-------- Validate Field ---------*/
			if (payModel.getTotalAmount() == null) {
				 errCode = "E07113";
				 errMsg = messageSource.getMessage("app.man-resp.invalid_field", new Object[] { "TotalAmount" },
						LocaleContextHolder.getLocale());
				 log.setError(errCode, errMsg);
				 epayLogService.insertLog(log);
				 return setXml(errCode, errMsg, "", "");
			}
			BigDecimal total = new BigDecimal(payModel.getTotalAmount());
			for (int i = 0; i < count; i++) {
				if (detail[i].getSystemRefNo().length() > 20 || detail[i].getSystemRefNo().length() <= 0) {
					 errCode = "E07113";
					 errMsg = messageSource.getMessage("app.man-resp.invalid_field",
							new Object[] { "SystemRefNo" }, LocaleContextHolder.getLocale());
				}
				if (detail[i].getAmount() == null) {
					 errCode = "E07113";
					 errMsg = messageSource.getMessage("app.man-resp.invalid_field", new Object[] { "Amount" },
							LocaleContextHolder.getLocale());
				}
				amount = new BigDecimal(detail[i].getAmount());
				if (amount.compareTo(BigDecimal.ZERO) == 0) {
					 errCode = "E07113";
					 errMsg = messageSource.getMessage("app.man-resp.invalid_field", new Object[] { "Amount" },
							LocaleContextHolder.getLocale());
				}
				if (detail[i].getNid().length() > 13 || detail[i].getNid().length() <= 0) {
					 errCode = "E07113";
					 errMsg = messageSource.getMessage("app.man-resp.invalid_field", new Object[] { "Nid" },
							LocaleContextHolder.getLocale());
				}
			}
			if(!errCode.equals("")) 
			{
				log.setError(errCode, errMsg);
				epayLogService.insertLog(log);
				return setXml(errCode, errMsg, "", "");
			}
			else if (total.compareTo(BigDecimal.ZERO) == 0) {
				 errCode = "E07113";
				 errMsg = messageSource.getMessage("app.man-resp.invalid_field", new Object[] { "TotalAmount" },
						LocaleContextHolder.getLocale());
			}
			else if(!bankCode.equals(payModel.getBankCode()))
			{
				errCode = "E07113";
				 errMsg = messageSource.getMessage("app.man-resp.invalid_field", new Object[] { "BankCode" },
						LocaleContextHolder.getLocale());
			}
			else if(!paymentLine.equals(payModel.getPaymentLine()))
			{
				errCode = "E07113";
				 errMsg = messageSource.getMessage("app.man-resp.invalid_field", new Object[] { "PaymentLine" },
						LocaleContextHolder.getLocale());
			}
			else if (payModel.getBankCode().length() > 3 || payModel.getBankCode().length() <= 0) {
				 errCode = "E07113";
				 errMsg = messageSource.getMessage("app.man-resp.invalid_field", new Object[] { "BankCode" },
						LocaleContextHolder.getLocale());
			}
			else if (payModel.getPaymentLine().length() > 10 || payModel.getPaymentLine().length() <= 0) {
				 errCode = "E07113";
				 errMsg = messageSource.getMessage("app.man-resp.invalid_field", new Object[] { "PaymentLine" },
						LocaleContextHolder.getLocale());
			}
			else if (payModel.getRdTransactionNo().length() > 35 || payModel.getRdTransactionNo().length() <= 0) {
				 errCode = "E07113";
				 errMsg = messageSource.getMessage("app.man-resp.invalid_field",
						new Object[] { "RdTransactionNo" }, LocaleContextHolder.getLocale());
			}
			else if (payModel.getPaymentID().length() > 20 || payModel.getPaymentID().length() <= 0) {
				 errCode = "E07113";
				 errMsg = messageSource.getMessage("app.man-resp.invalid_field", new Object[] { "PaymentID" },
						LocaleContextHolder.getLocale());
			}
			else if (payModel.getAgentID().length() > 20 || payModel.getAgentID().length() <= 0) {
				 errCode = "E07113";
				 errMsg = messageSource.getMessage("app.man-resp.invalid_field", new Object[] { "AgentID" },
						LocaleContextHolder.getLocale());
			}
			else if (payModel.getAgentBrano().length() > 6 || payModel.getAgentBrano().length() <= 0) {
				 errCode = "E07113";
				 errMsg = messageSource.getMessage("app.man-resp.invalid_field", new Object[] { "AgentBrano" },
						LocaleContextHolder.getLocale());
			}
			else if (payModel.getTransmitDate().length() > 14 || payModel.getTransmitDate().length() <= 0) {
				 errCode = "E07113";
				 errMsg = messageSource.getMessage("app.man-resp.invalid_field", new Object[] { "TransmitDate" },
						LocaleContextHolder.getLocale());
			}
			else if (status.getTransferDate().length() > 14 || status.getTransferDate().length() <= 0) {
				 errCode = "E07113";
				 errMsg = messageSource.getMessage("app.man-resp.invalid_field", new Object[] { "TransferDate" },
						LocaleContextHolder.getLocale());
			}
			else if (status.getPaymentStatus().length() > 1 || status.getPaymentStatus().length() <= 0) {
				 errCode = "E07113";
				 errMsg = messageSource.getMessage("app.man-resp.invalid_field", new Object[] { "PaymentStatus" },
						LocaleContextHolder.getLocale());
			}
			else if (!isNumeric(status.getPaymentStatus())) {
				 errCode = "E07113";
				 errMsg = messageSource.getMessage("app.man-resp.invalid_field", new Object[] { "PaymentStatus" },
						LocaleContextHolder.getLocale());
			}
			else if (cSConfirmPaymentModel.getSendMethod().length() > 10
					|| cSConfirmPaymentModel.getSendMethod().length() <= 0) {
				 errCode = "E07113";
				 errMsg = messageSource.getMessage("app.man-resp.invalid_field", new Object[] { "SendMethod" },
						LocaleContextHolder.getLocale());
			}
			if(!errCode.equals("")) 
			{
				log.setError(errCode, errMsg);
				epayLogService.insertLog(log);
				return setXml(errCode, errMsg, "", "");
			}
			/*-------- Validate Field ---------*/
			int payStatus = Integer.valueOf(status.getPaymentStatus());
			EpayTaxPaymentOutbound epayTaxPaymentOutbound = new EpayTaxPaymentOutbound();
			EpayTaxPaymentInfo epayTaxPaymentInfo = new EpayTaxPaymentInfo();
			EpayTaxPaymentInboundDetail epayTaxPaymentInboundDetail = new EpayTaxPaymentInboundDetail();
			EpayTaxPaymentInbound epayTaxPaymentInbound = new EpayTaxPaymentInbound();
			EpayTaxPaymentInboundRefOutbound epayTaxPaymentInboundRefOutbound = new EpayTaxPaymentInboundRefOutbound();
//			Optional<MasterReceiverUnit> recEnt = paymentInService.findByRecCode(bankCode);
//			if (!recEnt.isPresent()) {
//				 errCode = "E07100";
//				 errMsg = messageSource.getMessage("app.man-resp.data_not_found_with_arg",
//						new Object[] { "BankCode" }, LocaleContextHolder.getLocale());
//				log.setError(errCode, errMsg);
//				epayLogService.insertLog(log);
//				return setXml(errCode, errMsg, "", "");
//			}
//			MasterReceiverUnit masterReceiverUnit = recEnt.get();
//			Optional<MasterPaymentLine> lineEnt = paymentInService.findByPayLineCode(paymentLine);
//			if (!lineEnt.isPresent()) {
//				 errCode = "E07100";
//				 errMsg = messageSource.getMessage("app.man-resp.data_not_found_with_arg",
//						new Object[] { "paymentLine" }, LocaleContextHolder.getLocale());
//				log.setError(errCode, errMsg);
//				epayLogService.insertLog(log);
//				return setXml(errCode, errMsg, "", "");
//			}
//			MasterPaymentLine masPaymentLine = lineEnt.get();
			List<EpayReceiverPaymentLine> reciverPayLineEnt = paymentInService
					.findByMasterPaymentLineAndMasterReceiverUnit(masPaymentLine, masterReceiverUnit);
			if (reciverPayLineEnt.size() < 1) {
				 errCode = "E07100";
				 errMsg = messageSource.getMessage("app.man-resp.data_not_found_with_arg",
						new Object[] { "EpayReceiverPaymentLine,MasterReceiverUnit" }, LocaleContextHolder.getLocale());
				log.setError(errCode, errMsg);
				epayLogService.insertLog(log);
				return setXml(errCode, errMsg, "", "");
				// return setXml("E07111","ไม่สามารถทำรายการได้
				// เนื่องจากไม่พบช่องทางชำระภาษีที่เปิดให้บริการ
				// [lineEnt,masterReceiverUnit]","","");
			}
			EpayReceiverPaymentLine recPayLineEnt = reciverPayLineEnt.get(0);
			Optional<MasterPaymentStatus> mPaymentStatus = paymentInService.findById(payStatus);
			if (!mPaymentStatus.isPresent()) {
				 errCode = "E07100";
				 errMsg = messageSource.getMessage("app.man-resp.data_not_found_with_arg",
						new Object[] { "PaymentStatus" }, LocaleContextHolder.getLocale());
				log.setError(errCode, errMsg);
				epayLogService.insertLog(log);
				return setXml(errCode, errMsg, "", "");
				// return setXml("E07100","ไม่พบข้อมูลในระบบ [PaymentStatus]","","");
			}
			MasterPaymentStatus masterPaymentStatus = mPaymentStatus.get();
			Optional<MasterSendMethod> OptionalMasterSendMethod = paymentInService
					.findBySendMethod(cSConfirmPaymentModel.getSendMethod());
			if (!OptionalMasterSendMethod.isPresent()) {
				 errCode = "E07100";
				 errMsg = messageSource.getMessage("app.man-resp.data_not_found_with_arg",
						new Object[] { "SendMethod" }, LocaleContextHolder.getLocale());
				log.setError(errCode, errMsg);
				epayLogService.insertLog(log);
				return setXml(errCode, errMsg, "", "");
			}
			MasterSendMethod masterSendMethod = OptionalMasterSendMethod.get();

			// ประกาศไว้รอ function cert
			String recCertCode = ""; // REC_CERT_CODE
			Date currentDate = new Date();
			Date tranmitDate = new Date();
			Date tranferDate = new Date();
			
			try {
				tranmitDate = new SimpleDateFormat("yyyyMMddHHmmss").parse(payModel.getTransmitDate());
				tranferDate = new SimpleDateFormat("yyyyMMddHHmmss").parse(status.getTransferDate());
			}catch(Exception e) {
				errCode = "E07100";
				 errMsg = messageSource.getMessage("app.man-resp.invalid_data",
						new Object[] { "TransmitDate / TransferDate" }, LocaleContextHolder.getLocale());
				log.setError(errCode, errMsg);
				epayLogService.insertLog(log);
				return setXml(errCode, errMsg, "", "");
			}
			
			if (recPayLineEnt.getStartDate() == null && recPayLineEnt.getEndDate() == null) {
				logger.info("Not found Start Date and End Date");
			} else if (currentDate.after(recPayLineEnt.getStartDate())
					&& currentDate.before(recPayLineEnt.getEndDate())) {
				logger.info("In range");
			} else if (currentDate.equals(recPayLineEnt.getStartDate())
					|| currentDate.equals(recPayLineEnt.getEndDate())) {
				logger.info("In range");
			} else 
			{
////				if (recPayLineEnt.getMasterStatus().getStatus() != null
////						&& recPayLineEnt.getMasterStatus().getStatus().equals("A")) {
////					ResponseCode = "E07106";
////					responseMessage = "ไม่สามารถทำรายการได้ เนื่องจากช่องทางชำระภาษีปิดให้บริการ";
////				} else if (masterReceiverUnit.getMasterStatus().getStatus() != null
////						&& masterReceiverUnit.getMasterStatus().getStatus().equals("A")) {
////					ResponseCode = "E07106";
////					responseMessage = "ไม่สามารถทำรายการได้ เนื่องจากช่องทางชำระภาษีปิดให้บริการ";
////				} 
////				else 
////				{
//				errCode = "E07105";
//				errMsg = "ไม่สามารถทำรายการได้ เนื่องจากข้อมูลที่ส่งเข้ามาอยู่นอกวันที่ให้บริการ";
//				log.setError(errCode, errMsg);
//				epayLogService.insertLog(log);
//				return setXml(errCode, errMsg, "", "");
//				}
				
			//**************
				if (recPayLineEnt.getMasterStatus().getStatus() != null
				&& recPayLineEnt.getMasterStatus().getStatus().equals("A")) {
					errCode = "E07106";
					errMsg = "ไม่สามารถทำรายการได้ เนื่องจากช่องทางชำระภาษีปิดให้บริการ";
					log.setError(errCode, errMsg);
					epayLogService.insertLog(log);
					return setXml(errCode, errMsg, "", "");
				}else{
					errCode = "E07105";
					errMsg = "ไม่สามารถทำรายการได้ เนื่องจากข้อมูลที่ส่งเข้ามาอยู่นอกวันที่ให้บริการ";
					log.setError(errCode, errMsg);
					epayLogService.insertLog(log);
					return setXml(errCode, errMsg, "", "");
				}

			}
			
			tranNo = payModel.getRdTransactionNo();
			Optional<EpayTaxPaymentOutbound> optionalEpayTaxPaymentOutbound = paymentInService.findByTranNo(tranNo);
			if (!optionalEpayTaxPaymentOutbound.isPresent()) {
				 errCode = "E07100";
				 errMsg = messageSource.getMessage("app.man-resp.data_not_found_with_arg",
						new Object[] { "TransactionNo" }, LocaleContextHolder.getLocale());
				log.setError(errCode, errMsg);
				epayLogService.insertLog(log);
				return setXml(errCode, errMsg, "", "");
				// return setXml("E07100","ไม่พบข้อมูลในระบบ [TransactionNo]","","");
			}
			epayTaxPaymentOutbound = optionalEpayTaxPaymentOutbound.get();
			Optional<EpayTaxPaymentInfo> optionalEpayTaxPaymentInfo = paymentInService
					.findByEpayTaxPaymentInfoIdAndAgentIdAndTotalAmount(
							epayTaxPaymentOutbound.getEpayTaxPaymentInfo().getEpayTaxPaymentInfoId(),
							payModel.getAgentID(), total);
			if (!optionalEpayTaxPaymentInfo.isPresent()) {
				 errCode = "E07100";
				 errMsg = messageSource.getMessage("app.man-resp.data_not_found_with_arg",
						new Object[] { "infoID,agentID,totalamount" }, LocaleContextHolder.getLocale());
				log.setError(errCode, errMsg);
				epayLogService.insertLog(log);
				return setXml(errCode, errMsg, "", "");
				// return setXml("E07100","ไม่พบข้อมูลในระบบ
				// [infoID,agentID,totalamount]","","");
			}
			epayTaxPaymentInfo = optionalEpayTaxPaymentInfo.get();
			Date expDate = new Date();
			
			try {
				expDate = new SimpleDateFormat("yyyy-MM-dd")
						.parse(epayTaxPaymentInfo.getExpDate().toString().substring(0, 10));
			}catch(Exception e) {
				errCode = "E07100";
				 errMsg = messageSource.getMessage("app.man-resp.invalid_data",
						new Object[] { "Expire Date" }, LocaleContextHolder.getLocale());
				log.setError(errCode, errMsg);
				epayLogService.insertLog(log);
				return setXml(errCode, errMsg, "", "");
			}
			
			for (int i = 0; i < count; i++) {
				epayTaxPaymentInboundDetail.setEpayTaxPaymentInbound(epayTaxPaymentInbound);
				epayTaxPaymentInboundDetail.setSysRefNo(detail[i].getSystemRefNo());
				amount = new BigDecimal(detail[i].getAmount());
				epayTaxPaymentInboundDetail.setAmount(amount);
				epayTaxPaymentInboundDetail.setNid(detail[i].getNid());
				epayTaxPaymentInboundDetail.setNidBraNo(detail[i].getNidBrano());
				epayTaxPaymentInboundDetail.setCreateBy(USERNAME);
				epayTaxPaymentInboundDetail.setCreateDate(now);
				epayTaxPaymentInboundDetail.setUpdateBy(USERNAME);
				epayTaxPaymentInboundDetail.setUpdateDate(now);
				epayTaxPaymentInbound.getEpayTaxPaymentInboundDetails().add(epayTaxPaymentInboundDetail);
			}
			logger.info("Add Inbound Success!!");
			epayTaxPaymentInbound.setMasterPaymentStatus(masterPaymentStatus);
			epayTaxPaymentInbound.setRdTranNo(payModel.getRdTransactionNo());
			epayTaxPaymentInbound.setTransmitDate(tranmitDate);
			epayTaxPaymentInbound.setTransferDate(tranferDate);
			epayTaxPaymentInbound.setEpayReceiverPaymentLine(recPayLineEnt);
			epayTaxPaymentInbound.setAgentId(payModel.getAgentID());
			epayTaxPaymentInbound.setAgentBraNo(payModel.getAgentBrano());
			epayTaxPaymentInbound.setTotalAmount(total);
			epayTaxPaymentInbound.setCreateDate(now);
			epayTaxPaymentInbound.setCreateBy(USERNAME);
			epayTaxPaymentInbound.setUpdateBy(USERNAME);
			epayTaxPaymentInbound.setUpdateDate(now);
			epayTaxPaymentInbound.setAttachFile(attachFile);
			epayTaxPaymentInbound.setAuthorizeCode(status.getAuthorizeCode());
			epayTaxPaymentInbound.setPayStatusMsg(status.getStatusMessage());
			epayTaxPaymentInbound.setMasterSendMethod(masterSendMethod);
			epayTaxPaymentInbound.setUuid(INBOUND_UUID);
			epayTaxPaymentInbound.setResponseCode(ResponseCode);
			epayTaxPaymentInbound.setReponseMessage(responseMessage);
			paymentInService.save(epayTaxPaymentInbound);
			logger.info("Insert Inbound Success!!");

			epayTaxPaymentInboundRefOutbound.setEpayTaxPaymentOutbound(epayTaxPaymentOutbound);
			epayTaxPaymentInboundRefOutbound.setEpayTaxPaymentInbound(epayTaxPaymentInbound);
			paymentInService.save(epayTaxPaymentInboundRefOutbound);
			logger.info("Insert InboundRefOutbound Success!!");
			/*------- Write Text File -------*/
			stringXml = genXmlHelper.convertToXmlString(reqXml, true);
			Writer writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(writeXmlPath + epayTaxPaymentInbound.getAttachFile()), "utf-8"));
			{
				writer.write(stringXml);
			}
			if (writer != null) {
				writer.close();
			}
			/*------- Write Text File -------*/
			/*------- Send to Response -------*/
			ResRDEFRespConfirmPayment resRDEFRespConfirmPayment = new ResRDEFRespConfirmPayment();
			ResPaymentInformation resPaymentInformation = new ResPaymentInformation();
			resPaymentInformation.setrDTransactionNo(epayTaxPaymentInbound.getRdTranNo());
			resPaymentInformation.setTransmitDate(epayTaxPaymentInbound.getTransmitDate());
			resPaymentInformation.setTotalAmount(epayTaxPaymentInbound.getTotalAmount());
			resPaymentInformation.setTerminalID(epayTaxPaymentInbound.getTerminalId());
			resPaymentInformation.setMerchantID(epayTaxPaymentInbound.getMerchantId());
			resPaymentInformation.setObKeyID(INBOUND_UUID);
			resPaymentInformation.setRef1(epayTaxPaymentInfo.getAgentId());
			resPaymentInformation.setRef2(epayTaxPaymentInfo.getRefNo());
			resPaymentInformation.setRef3(epayTaxPaymentInbound.getRef3());
			resRDEFRespConfirmPayment.setEpay(resPaymentInformation);
			resEncryptXml = genXmlHelper.convertToXmlString(resRDEFRespConfirmPayment, true);
			/*-------- Send to encrypt and add dsig ---------*/
			
			try {
				resEncryptXml = pkiService.dSigWithEncrypt(rdCode, recCode, attachFile, resEncryptXml);	
			}catch(Exception e){
				
				errCode = "E07100";
				errMsg = messageSource.getMessage("app.man-resp.pki_verify_fail",
						new Object[] { e.getMessage() }, LocaleContextHolder.getLocale());
				 
				log.setError(errCode, errMsg);
				epayLogService.insertLog(log);
				return setXml(errCode, errMsg, "", "");
			}
			
			/*-------- Send to encrypt and add dsig ---------*/
			/*------- Send to Response -------*/
			

			// Check condition
			infoId = epayTaxPaymentOutbound.getEpayTaxPaymentInfo().getEpayTaxPaymentInfoId();
			epayTaxPaymentInfo = paymentInService.findByEpayTaxPaymentInfoId(infoId);
			
			
			try {
				tranmitDate = new SimpleDateFormat("yyyyMMdd").parse(payModel.getTransmitDate().substring(0, 8));
			}catch(Exception e) {
				errCode = "E07100";
				 errMsg = messageSource.getMessage("app.man-resp.invalid_data",
						new Object[] { "Transmit Date" }, LocaleContextHolder.getLocale());
				log.setError(errCode, errMsg);
				epayLogService.insertLog(log);
				return setXml(errCode, errMsg, "", "");
			}
			
			if (payStatus == 1) {
				if (tranmitDate.after(expDate)) {
					epayTaxPaymentInbound.setResponseCode("E07108");
					paymentInService.save(epayTaxPaymentInbound);
					log.setResponseCode("I07000");
					epayLogService.insertLog(log);
					return setXml("I07000", "สำเร็จ", INBOUND_UUID, resEncryptXml);

				}
				if (epayTaxPaymentInfo.getMasterStatus().getStatus().equals("I")) {
					epayTaxPaymentInbound.setResponseCode("E07110");
					paymentInService.save(epayTaxPaymentInbound);
					log.setResponseCode("I07000");
					epayLogService.insertLog(log);
					return setXml("I07000", "สำเร็จ", INBOUND_UUID, resEncryptXml);
				}
				if (epayTaxPaymentInfo.getEpayTaxPaymentInbound() != null) {
					epayTaxPaymentInbound.setResponseCode("E07109");
					paymentInService.save(epayTaxPaymentInbound);
					log.setResponseCode("I07000");
					epayLogService.insertLog(log);
					return setXml("I07000", "สำเร็จ", INBOUND_UUID, resEncryptXml);
				}
				if (epayTaxPaymentOutbound.getMasterStatus().getStatus().equals("I")) {
					epayTaxPaymentInbound.setResponseCode("E07112");
					paymentInService.save(epayTaxPaymentInbound);
					log.setResponseCode("I07000");
					epayLogService.insertLog(log);
					return setXml("I07000", "สำเร็จ", INBOUND_UUID, resEncryptXml);
				}
				epayTaxPaymentInfo.setEpayTaxPaymentInbound(epayTaxPaymentInbound);
				epayTaxPaymentInfo.setUpdateBy(USERNAME);
				epayTaxPaymentInfo.setUpdateDate(now);
				paymentInService.save(epayTaxPaymentInfo);
				logger.info("Update info Success!!");
				log.setResponseCode("I07000");
				epayLogService.insertLog(log);
				return setXml("I07000", "สำเร็จ", INBOUND_UUID, resEncryptXml);
			}
//		} catch (Exception ex) {
//			logger.error("context", ex);
//			errCode = "E07113";
//			errMsg = "ไม่สำเร็จ ["+ex.getMessage()+"]";
//			log.setError(errCode, errMsg);
//			epayLogService.insertLog(log);
//			return setXml(errCode, errMsg, "", "");
//		}
			
		epayLogService.insertLog(log);
		return setXml("I07000", "สำเร็จ", INBOUND_UUID, resEncryptXml);
	}

	public static boolean isNumeric(String str) {
		for (char c : str.toCharArray()) {
			if (!Character.isDigit(c))
				return false;
		}
		return true;
	}

	public String setXml(String errCode, String errMsg, String INBOUND_UUID, String resEncryptXml)
			throws JAXBException {
		String resultXml = "";
		GenXmlHelper genXmlHelper = new GenXmlHelper();
		ResEpaymentXmlModel resEpaymentXmlModel = new ResEpaymentXmlModel();
		if (INBOUND_UUID.equals("") && resEncryptXml.equals("")) {
			resEpaymentXmlModel.setResponseCode(errCode);
			resEpaymentXmlModel.setResponseMessage(errMsg);
		} else {
			resEpaymentXmlModel.setResponseCode(errCode);
			resEpaymentXmlModel.setResponseMessage(errMsg);
			resEpaymentXmlModel.setOutboundKeyID(INBOUND_UUID);
			resEpaymentXmlModel.setEncryptData(resEncryptXml);
		}
		resultXml = genXmlHelper.convertToXmlString(resEpaymentXmlModel, true);
		return resultXml;
	}

	@ApiOperation(value = "PayInUpdatePaymentStatus", notes = "สำหรับให้หน่วยรับชำระส่งผลการชำระด้วย Bill Payment ให้กับกรมสรรพากร")
	@PostMapping(value = "adjust-status-payin", consumes = "application/xml", produces = "application/xml", headers = "Accept-Language")
	public Object updatePaymentStatusPayIn(@RequestBody ReqEpaymentXmlModel reqXml, HttpServletRequest request) throws JAXBException {
		String logDetail = String.format("Adjust Status Epay Direct [%s]", reqXml.getBankCode());
		logger.info(logDetail);
		EpayLogModel log = new EpayLogModel(LogLevel.INFO, LogType.PAYMENT, ApiCode.CHECK_STATUS_PAYMENT,
				 reqXml.getBankCode(), logDetail);
		GenXmlHelper genXmlHelper = new GenXmlHelper();
		Authentication authentication = authenticationFacade.getAuthentication();
		String USERNAME = authentication.getName();
		String INBOUND_UUID = UUID.randomUUID().toString();
		String resEncryptXml = "";
		String ResponseCode = "";
		String responseMessage = "";
		String errCode = "";
		String errMsg = "";
		//try {
			String stringXml = "";
			String strDate = "";
			String bankCode = "";
			String paymentLine = "";
			String attachFile = "";
			bankCode = reqXml.getBankCode();
			paymentLine = reqXml.getPaymentLine();
			attachFile = reqXml.getAttachFile();
			String CTL_CODE = "";
			long infoId = 0;
			Date now = new Date();
			int payStatus = 1;
			/*-------- Validate Field ---------*/
			if (bankCode.equals("") || bankCode.length() > 3 || bankCode.length() <= 0) {
				errCode = "E07113";
				errMsg = messageSource.getMessage("app.man-resp.invalid_field", new Object[] { "BankCode" },
						LocaleContextHolder.getLocale());
			}
			else if (paymentLine.equals("") || paymentLine.length() > 10 || paymentLine.length() <= 0) {
				errCode = "E07113";
				errMsg = messageSource.getMessage("app.man-resp.invalid_field", new Object[] { "PaymentLine" },
						LocaleContextHolder.getLocale());
			}
			else if (attachFile.equals("") || attachFile.length() > 100 || attachFile.length() <= 0) {
				errCode = "E07113";
				errMsg = messageSource.getMessage("app.man-resp.invalid_field", new Object[] { "AttachFile" },
						LocaleContextHolder.getLocale());
			}
			else if (reqXml.getEncryptData() == null) {
				errCode = "E07113";
				errMsg = messageSource.getMessage("app.man-resp.invalid_field", new Object[] { "EncryptData" },
						LocaleContextHolder.getLocale());
			}
			if(!errCode.equals("")) 
			{
				log.setError(errCode, errMsg);
				epayLogService.insertLog(log);
				return setXml(errCode, errMsg, "", "");
			}
			/*-------- Validate Field ---------*/
			
			
			
			//---- Check bank & payment line
			Optional<MasterReceiverUnit> recEnt = paymentInService.findByRecCode(bankCode);
			if (!recEnt.isPresent()) {
				 errCode = "E07100";
				 errMsg = messageSource.getMessage("app.man-resp.data_not_found_with_arg",
						new Object[] { "BankCode" }, LocaleContextHolder.getLocale());
				log.setError(errCode, errMsg);
				epayLogService.insertLog(log);
				return setXml(errCode, errMsg, "", "");
			}
			MasterReceiverUnit masterReceiverUnit = recEnt.get();
			Optional<MasterPaymentLine> lineEnt = paymentInService.findByPayLineCode(paymentLine);
			if (!lineEnt.isPresent()) {
				 errCode = "E07100";
				 errMsg = messageSource.getMessage("app.man-resp.data_not_found_with_arg",
						new Object[] { "paymentLine" }, LocaleContextHolder.getLocale());
				log.setError(errCode, errMsg);
				epayLogService.insertLog(log);
				return setXml(errCode, errMsg, "", "");
			}
			MasterPaymentLine masPaymentLine = lineEnt.get();
			
			List<EpayReceiverPaymentLine> epayReceiverPaymentLineOpt = epayReceiverPaymentLineService.findByMasterPaymentLineAndMasterReceiverUnit(masPaymentLine, masterReceiverUnit);
			
				
			if(epayReceiverPaymentLineOpt.size() == 0) {
				 errCode = "E07100";
				 errMsg = messageSource.getMessage("app.man-resp.data_not_found_with_arg",
						new Object[] { "BankCode , Payment Line" }, LocaleContextHolder.getLocale());
				log.setError(errCode, errMsg);
				epayLogService.insertLog(log);
				return setXml(errCode, errMsg, "", "");
			}
			//---- end Check bank & payment line	
			
			
			/*-------- Send to decrypt and add dsig ---------*/
			EpayReceiverPaymentLine epayReceiverPaymentLine = epayReceiverPaymentLineOpt.get(0);
			
			int recCode = Integer.parseInt(epayReceiverPaymentLine.getRecCertCode());
			int rdCode = Integer.parseInt(epayReceiverPaymentLine.getRdCertCode());

			String decrptXmlEscap = StringEscapeUtils.unescapeXml(reqXml.getEncryptData());
			
			String decryptData = null;
			
			try {
				decryptData = pkiService.verifyWithDecrypt(recCode, rdCode, attachFile, decrptXmlEscap);		
			}catch(Exception e){
				
				errCode = "E07100";
				errMsg = messageSource.getMessage("app.man-resp.pki_verify_fail",
						new Object[] { e.getMessage() }, LocaleContextHolder.getLocale());
				 
				log.setError(errCode, errMsg);
				epayLogService.insertLog(log);
				return setXml(errCode, errMsg, "", "");
			}
			
			

			if(decryptData.substring(0,5).equals("ERROR"))
			{
				errCode = "E07003";
				errMsg = "ไม่สามารถถอดรหัส / ตรวจสอบลายมือชื่อข้อมูล XML";
				log.setError(errCode, errMsg);
				epayLogService.insertLog(log);
				return setXml(errCode, errMsg, "", "");
			}
			
			ReqBillPayCSConfirmPayment reqBillPayCSConfirmPayment = new ReqBillPayCSConfirmPayment();
			reqBillPayCSConfirmPayment = (ReqBillPayCSConfirmPayment) genXmlHelper.convertToXmlObject(decryptData,
					reqBillPayCSConfirmPayment);
			/*------- Send to decrypt -------*/			
			ReqBillPaymentInformation billPaymentInfo = reqBillPayCSConfirmPayment.getPayin();			
			/*-------- Validate Field ---------*/
			if (billPaymentInfo.getTotalAmount() == null || billPaymentInfo.getTotalAmount().length() <= 0) {
				errCode = "E07113";
				errMsg = messageSource.getMessage("app.man-resp.invalid_field", new Object[] { "TotalAmount" },
						LocaleContextHolder.getLocale());
				log.setError(errCode, errMsg);
				epayLogService.insertLog(log);
				return setXml(errCode, errMsg, "", "");
			}
			BigDecimal total = new BigDecimal(billPaymentInfo.getTotalAmount());
			if (total.compareTo(BigDecimal.ZERO) == 0) {
				errCode = "E07113";
				errMsg = messageSource.getMessage("app.man-resp.invalid_field", new Object[] { "TotalAmount" },
						LocaleContextHolder.getLocale());
			}
			else if (billPaymentInfo.getBankCode().equals("") || billPaymentInfo.getBankCode().length() > 3
					|| billPaymentInfo.getBankCode().length() <= 0) {
				errCode = "E07113";
				errMsg = messageSource.getMessage("app.man-resp.invalid_field", new Object[] { "BankCode" },
						LocaleContextHolder.getLocale());
			}
			else if (billPaymentInfo.getPaymentLine().equals("") || billPaymentInfo.getPaymentLine().length() > 10
					|| billPaymentInfo.getPaymentLine().length() <= 0) {
				errCode = "E07113";
				errMsg = messageSource.getMessage("app.man-resp.invalid_field", new Object[] { "PaymentLine" },
						LocaleContextHolder.getLocale());
			}
			else if (!bankCode.equals(billPaymentInfo.getBankCode())) {
				errCode = "E07113";
				errMsg = messageSource.getMessage("app.man-resp.invalid_field", new Object[] { "BankCode" },
						LocaleContextHolder.getLocale());
			}
			else if (!paymentLine.equals(billPaymentInfo.getPaymentLine())) {
				errCode = "E07113";
				errMsg = messageSource.getMessage("app.man-resp.invalid_field", new Object[] { "PaymentLine" },
						LocaleContextHolder.getLocale());
			}
			else if (billPaymentInfo.getCSTransactionNo().length() > 35
					|| billPaymentInfo.getCSTransactionNo().length() <= 0) {
				errCode = "E07113";
				errMsg = messageSource.getMessage("app.man-resp.invalid_field", new Object[] { "CSTransactionNo" },
						LocaleContextHolder.getLocale());
			}
			else if (billPaymentInfo.getRef1().length() > 13 || billPaymentInfo.getRef1().length() <= 0) {
				errCode = "E07113";
				errMsg = messageSource.getMessage("app.man-resp.invalid_field", new Object[] { "Ref1" },
						LocaleContextHolder.getLocale());
			}
			else if (billPaymentInfo.getRef2().length() > 15 || billPaymentInfo.getRef2().length() <= 0) {
				errCode = "E07113";
				errMsg = messageSource.getMessage("app.man-resp.invalid_field", new Object[] { "Ref2" },
						LocaleContextHolder.getLocale());
			}
			else if (billPaymentInfo.getTransmitDate().length() > 14 || billPaymentInfo.getTransmitDate().length() <= 0) {
				errCode = "E07113";
				errMsg = messageSource.getMessage("app.man-resp.invalid_field", new Object[] { "TransmitDate" },
						LocaleContextHolder.getLocale());
			}
			else if (billPaymentInfo.getTransferDate().length() > 14 || billPaymentInfo.getTransferDate().length() <= 0) {
				errCode = "E07113";
				errMsg = messageSource.getMessage("app.man-resp.invalid_field", new Object[] { "TransferDate" },
						LocaleContextHolder.getLocale());
			}
			if(!errCode.equals("")) 
			{
				log.setError(errCode, errMsg);
				epayLogService.insertLog(log);
				return setXml(errCode, errMsg, "", "");
			}
			/*-------- Validate Field ---------*/
			EpayTaxPaymentOutbound epayTaxPaymentOutbound = new EpayTaxPaymentOutbound();
			EpayTaxPaymentInfo epayTaxPaymentInfo = new EpayTaxPaymentInfo();
			EpayTaxPaymentInbound epayTaxPaymentInbound = new EpayTaxPaymentInbound();
			EpayTaxPaymentInboundRefOutbound epayTaxPaymentInboundRefOutbound = new EpayTaxPaymentInboundRefOutbound();
//			Optional<MasterReceiverUnit> recEnt = paymentInService.findByRecCode(bankCode);
//			if (!recEnt.isPresent()) {
//				errCode = "E07100";
//				 errMsg = messageSource.getMessage("app.man-resp.data_not_found_with_arg",
//						new Object[] { "bankCode" }, LocaleContextHolder.getLocale());
//				log.setError(errCode, errMsg);
//				epayLogService.insertLog(log);
//				return setXml("E07100", "ไม่พบข้อมูลในระบบ [bankCode", "", "");
//			}
//			MasterReceiverUnit masterReceiverUnit = recEnt.get();
//			Optional<MasterPaymentLine> lineEnt = paymentInService.findByPayLineCode(paymentLine);
//			if (!lineEnt.isPresent()) {
//				errCode = "E07100";
//				 errMsg = messageSource.getMessage("app.man-resp.data_not_found_with_arg",
//						new Object[] { "paymentLine" }, LocaleContextHolder.getLocale());
//				log.setError(errCode, errMsg);
//				epayLogService.insertLog(log);
//				return setXml("E07100", "ไม่พบข้อมูลในระบบ [paymentLine]", "", "");
//			}
//			MasterPaymentLine masPaymentLine = lineEnt.get();

			List<EpayReceiverPaymentLine> reciverPayLineEnt = paymentInService
					.findByMasterPaymentLineAndMasterReceiverUnit(masPaymentLine, masterReceiverUnit);
			if (reciverPayLineEnt.size() < 1) {
				errCode = "E07100";
				 errMsg = messageSource.getMessage("app.man-resp.data_not_found_with_arg",
						new Object[] { "lineEnt,masterReceiverUnit" }, LocaleContextHolder.getLocale());
				log.setError(errCode, errMsg);
				epayLogService.insertLog(log);
				return setXml("E07100", "ไม่พบข้อมูลในระบบ [lineEnt,masterReceiverUnit]", "", "");
			}
			EpayReceiverPaymentLine recPayLineEnt = reciverPayLineEnt.get(0);
			Optional<MasterPaymentStatus> mPaymentStatus = paymentInService.findById(payStatus);
			if (!mPaymentStatus.isPresent()) {
				errCode = "E07100";
				 errMsg = messageSource.getMessage("app.man-resp.data_not_found_with_arg",
						new Object[] { "PaymentStatus" }, LocaleContextHolder.getLocale());
				log.setError(errCode, errMsg);
				epayLogService.insertLog(log);
				return setXml("E07100", "ไม่พบข้อมูลในระบบ [PaymentStatus]", "", "");
			}
			MasterPaymentStatus masterPaymentStatus = mPaymentStatus.get();

			// ประกาศไว้รอ function cert
			String recCertCode = ""; // REC_CERT_CODE
			String recPayLineCode = ""; // REC_PAY_LINE_CODE คือ primary key ของ table EPAY_RECEIVER_PAYMENT_LINE
			Date currentDate = new Date();
//			Date tranmitDate = new SimpleDateFormat("yyyyMMddHHmmss").parse(billPaymentInfo.getTransmitDate());
//			Date tranferDate = new SimpleDateFormat("yyyyMMddHHmmss").parse(billPaymentInfo.getTransferDate());
			
			Date tranmitDate = new Date();
			Date tranferDate = new Date();
			
			try {
				tranmitDate = new SimpleDateFormat("yyyyMMddHHmmss").parse(billPaymentInfo.getTransmitDate());
				tranferDate = new SimpleDateFormat("yyyyMMddHHmmss").parse(billPaymentInfo.getTransferDate());
			}catch(Exception e) {
				errCode = "E07100";
				 errMsg = messageSource.getMessage("app.man-resp.invalid_data",
						new Object[] { "TransmitDate / TransferDate" }, LocaleContextHolder.getLocale());
				log.setError(errCode, errMsg);
				epayLogService.insertLog(log);
				return setXml(errCode, errMsg, "", "");
			}
			
			
			if (recPayLineEnt.getStartDate() == null && recPayLineEnt.getEndDate() == null) {
				logger.info("Not found Start Date and End Date");
			} else if (currentDate.after(recPayLineEnt.getStartDate())
					&& currentDate.before(recPayLineEnt.getEndDate())) {
				logger.info("In range");
			} else if (currentDate.equals(recPayLineEnt.getStartDate())
					|| currentDate.equals(recPayLineEnt.getEndDate())) {
				logger.info("In range");
			} else 
			{
//				if (recPayLineEnt.getMasterStatus().getStatus() != null
//						&& recPayLineEnt.getMasterStatus().getStatus().equals("A")) {
//					ResponseCode = "E07106";
//					responseMessage = "ไม่สามารถทำรายการได้ เนื่องจากช่องทางชำระภาษีปิดให้บริการ";
//				} else if (masterReceiverUnit.getMasterStatus().getStatus() != null
//						&& masterReceiverUnit.getMasterStatus().getStatus().equals("A")) {
//					ResponseCode = "E07106";
//					responseMessage = "ไม่สามารถทำรายการได้ เนื่องจากช่องทางชำระภาษีปิดให้บริการ";
//				} else {
//					errCode = "E07105";
//					errMsg = "ไม่สามารถทำรายการได้ เนื่องจากข้อมูลที่ส่งเข้ามาอยู่นอกวันที่ให้บริการ";
//					log.setError(errCode, errMsg);
//					epayLogService.insertLog(log);
//					return setXml(errCode, errMsg, "", "");
			//	}
					
			//*******
				if (recPayLineEnt.getMasterStatus().getStatus() != null
						&& recPayLineEnt.getMasterStatus().getStatus().equals("A")) {
					ResponseCode = "E07106";
					responseMessage = "ไม่สามารถทำรายการได้ เนื่องจากช่องทางชำระภาษีปิดให้บริการ";
				}  else {
					errCode = "E07105";
					errMsg = "ไม่สามารถทำรายการได้ เนื่องจากข้อมูลที่ส่งเข้ามาอยู่นอกวันที่ให้บริการ";
					log.setError(errCode, errMsg);
					epayLogService.insertLog(log);
					return setXml(errCode, errMsg, "", "");
				}
				
			}
			CTL_CODE = billPaymentInfo.getRef2();
			Optional<EpayTaxPaymentOutbound> optionalEpayTaxPaymentOutbound = paymentInService.findByCtlCode(CTL_CODE);
			if (!optionalEpayTaxPaymentOutbound.isPresent()) {
				errCode = "E07100";
				 errMsg = messageSource.getMessage("app.man-resp.data_not_found_with_arg",
						new Object[] { "CSTransactionNo" }, LocaleContextHolder.getLocale());
				log.setError(errCode, errMsg);
				epayLogService.insertLog(log);
				return setXml("E07100", "ไม่พบข้อมูลในระบบ [CSTransactionNo]", "", "");
			}
			epayTaxPaymentOutbound = optionalEpayTaxPaymentOutbound.get();
			Optional<EpayTaxPaymentInfo> optionalEpayTaxPaymentInfo = paymentInService
					.findByEpayTaxPaymentInfoIdAndAgentIdAndTotalAmount(
							epayTaxPaymentOutbound.getEpayTaxPaymentInfo().getEpayTaxPaymentInfoId(),
							billPaymentInfo.getRef1(), total);
			if (!optionalEpayTaxPaymentInfo.isPresent()) {
				errCode = "E07100";
				 errMsg = messageSource.getMessage("app.man-resp.data_not_found_with_arg",
						new Object[] { "Ref1,Ref2,Amount" }, LocaleContextHolder.getLocale());
				log.setError(errCode, errMsg);
				epayLogService.insertLog(log);
				return setXml("E07100", "ไม่พบข้อมูลในระบบ [Ref1,Ref2,Amount]", "", "");
			}
			epayTaxPaymentInfo = optionalEpayTaxPaymentInfo.get();
			Date expDate = new Date();
			
			try {
				expDate = new SimpleDateFormat("yyyy-MM-dd")
						.parse(epayTaxPaymentInfo.getExpDate().toString().substring(0, 10));
			}catch(Exception e) {
				errCode = "E07100";
				 errMsg = messageSource.getMessage("app.man-resp.invalid_data",
						new Object[] { "Expire Date" }, LocaleContextHolder.getLocale());
				log.setError(errCode, errMsg);
				epayLogService.insertLog(log);
				return setXml(errCode, errMsg, "", "");
			}
			
			/*------------ Save -------------------*/
			epayTaxPaymentInbound.setMasterPaymentStatus(masterPaymentStatus);
			if (billPaymentInfo.getRDTransactionNo() != null) {
				epayTaxPaymentInbound.setRdTranNo(billPaymentInfo.getRDTransactionNo());
			}
			if (billPaymentInfo.getRef3() != null) {
				epayTaxPaymentInbound.setRef3(billPaymentInfo.getRef3());
			}
			if (reqBillPayCSConfirmPayment.getPayin().getTerminalID() != null) {
				epayTaxPaymentInbound.setTerminalId(reqBillPayCSConfirmPayment.getPayin().getTerminalID());
			}
			if (reqBillPayCSConfirmPayment.getPayin().getMerchantID() != null) {
				epayTaxPaymentInbound.setMerchantId(reqBillPayCSConfirmPayment.getPayin().getMerchantID());
			}
			epayTaxPaymentInbound.setCsTranNo(billPaymentInfo.getCSTransactionNo());
			epayTaxPaymentInbound.setAgentId(billPaymentInfo.getRef1());
			epayTaxPaymentInbound.setRef2(billPaymentInfo.getRef2());
			epayTaxPaymentInbound.setTransmitDate(tranmitDate);
			epayTaxPaymentInbound.setTransferDate(tranferDate);
			epayTaxPaymentInbound.setEpayReceiverPaymentLine(recPayLineEnt);
			epayTaxPaymentInbound.setTotalAmount(total);
			epayTaxPaymentInbound.setAttachFile(attachFile);
			epayTaxPaymentInbound.setCreateDate(now);
			epayTaxPaymentInbound.setUpdateDate(now);
			epayTaxPaymentInbound.setCreateBy(USERNAME);
			epayTaxPaymentInbound.setUpdateBy(USERNAME);
			epayTaxPaymentInbound.setUuid(INBOUND_UUID);
			epayTaxPaymentInbound.setResponseCode(ResponseCode);
			epayTaxPaymentInbound.setReponseMessage(responseMessage);
			paymentInService.save(epayTaxPaymentInbound);
			logger.info("Insert Inbound Success!!");

			epayTaxPaymentInboundRefOutbound.setEpayTaxPaymentOutbound(epayTaxPaymentOutbound);
			epayTaxPaymentInboundRefOutbound.setEpayTaxPaymentInbound(epayTaxPaymentInbound);
			paymentInService.save(epayTaxPaymentInboundRefOutbound);
			logger.info("Insert InboundRefOutbound Success!!");
			/*------------ Save -------------------*/
			/*------- Write Text File -------*/
			DateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
			strDate = dateFormat.format(epayTaxPaymentInbound.getTransmitDate());
			stringXml = genXmlHelper.convertToXmlString(reqXml, true);
			
			Writer writer = null;
			try {
				writer = new BufferedWriter(new OutputStreamWriter(
						new FileOutputStream(
								writeXmlPath + "PAYIN-" + epayTaxPaymentInbound.getRef2() + "-" + strDate + ".xml"),
						"utf-8"));
				{
					writer.write(stringXml);
				}
			}catch(Exception e) {
				errCode = "E07100";
				errMsg = "ไม่สำเร็จ [Create File XML Fail : " + e.getMessage() + "]";
				log.setError(errCode, errMsg);
				epayLogService.insertLog(log);
				return setXml(errCode, errMsg, "", "");
			}			
			finally {
				
				try {
					writer.close();
				}catch(Exception e) {
					errCode = "E07100";
					errMsg = "ไม่สำเร็จ [Create File XML Fail : " + e.getMessage() + "]";
					log.setError(errCode, errMsg);
					epayLogService.insertLog(log);
					return setXml(errCode, errMsg, "", "");
				}

			}
			
			/*------- Write Text File -------*/

			/*------- Send to Response -------*/
			ResRDEFRespConfirmPayment resRDEFRespConfirmPayment = new ResRDEFRespConfirmPayment();
			ResPaymentInformation resPaymentInformation = new ResPaymentInformation();
			if (epayTaxPaymentInbound.getRdTranNo() != null) {
				resPaymentInformation.setrDTransactionNo(epayTaxPaymentInbound.getRdTranNo());
			}
			resPaymentInformation.setRef1(epayTaxPaymentInbound.getAgentId());
			resPaymentInformation.setRef2(epayTaxPaymentInbound.getRef2());
			resPaymentInformation.setRef3(epayTaxPaymentInbound.getRef3());
			resPaymentInformation.setTransmitDate(epayTaxPaymentInbound.getTransmitDate());
			resPaymentInformation.setTotalAmount(epayTaxPaymentInbound.getTotalAmount());
			resPaymentInformation.setTerminalID(epayTaxPaymentInbound.getTerminalId());
			resPaymentInformation.setMerchantID(epayTaxPaymentInbound.getMerchantId());
			resRDEFRespConfirmPayment.setEpay(resPaymentInformation);
			resEncryptXml = genXmlHelper.convertToXmlString(resRDEFRespConfirmPayment, true);
			/*-------- Send to encrypt and add dsig ---------*/
			
			try {
				resEncryptXml = pkiService.dSigWithEncrypt(rdCode, recCode, attachFile, resEncryptXml);
			}catch(Exception e){
				
				errCode = "E07100";
				errMsg = messageSource.getMessage("app.man-resp.pki_verify_fail",
						new Object[] { e.getMessage() }, LocaleContextHolder.getLocale());
				 
				log.setError(errCode, errMsg);
				epayLogService.insertLog(log);
				return setXml(errCode, errMsg, "", "");
			}
			
			/*-------- Send to encrypt and add dsig ---------*/
			/*------- Send to Response -------*/

			// Check condition
			infoId = epayTaxPaymentOutbound.getEpayTaxPaymentInfo().getEpayTaxPaymentInfoId();
			epayTaxPaymentInfo = paymentInService.findByEpayTaxPaymentInfoId(infoId);
			
			
			try {
				tranmitDate = new SimpleDateFormat("yyyyMMdd").parse(billPaymentInfo.getTransmitDate().substring(0, 8));
			}catch(Exception e) {
				errCode = "E07100";
				 errMsg = messageSource.getMessage("app.man-resp.invalid_data",
						new Object[] { "Transmit Date" }, LocaleContextHolder.getLocale());
				log.setError(errCode, errMsg);
				epayLogService.insertLog(log);
				return setXml(errCode, errMsg, "", "");
			}
			
			if (payStatus == 1) {
				if (tranmitDate.after(expDate)) {
					epayTaxPaymentInbound.setResponseCode("E07108");
					paymentInService.save(epayTaxPaymentInbound);
					log.setResponseCode("I07000");
					epayLogService.insertLog(log);
					return setXml("I07000", "สำเร็จ", INBOUND_UUID, resEncryptXml);
				}
				if (epayTaxPaymentInfo.getMasterStatus().getStatus().equals("I")) {
					epayTaxPaymentInbound.setResponseCode("E07110");
					paymentInService.save(epayTaxPaymentInbound);
					log.setResponseCode("I07000");
					epayLogService.insertLog(log);
					return setXml("I07000", "สำเร็จ", INBOUND_UUID, resEncryptXml);
				}
				if (epayTaxPaymentInfo.getEpayTaxPaymentInbound() != null) {
					epayTaxPaymentInbound.setResponseCode("E07109");
					paymentInService.save(epayTaxPaymentInbound);
					log.setResponseCode("I07000");
					epayLogService.insertLog(log);
					return setXml("I07000", "สำเร็จ", INBOUND_UUID, resEncryptXml);
				}
				if (epayTaxPaymentOutbound.getMasterStatus().getStatus().equals("I")) {
					epayTaxPaymentInbound.setResponseCode("E07112");
					paymentInService.save(epayTaxPaymentInbound);
					log.setResponseCode("I07000");
					epayLogService.insertLog(log);
					return setXml("I07000", "สำเร็จ", INBOUND_UUID, resEncryptXml);
				}
				epayTaxPaymentInfo.setEpayTaxPaymentInbound(epayTaxPaymentInbound);
				epayTaxPaymentInfo.setUpdateBy("User_01");
				epayTaxPaymentInfo.setUpdateDate(now);
				paymentInService.save(epayTaxPaymentInfo);
				log.setResponseCode("I07000");
				epayLogService.insertLog(log);
				logger.info("Update info Success!!");
				return setXml("I07000", "สำเร็จ", INBOUND_UUID, resEncryptXml);
			}
//		} catch (Exception ex) {
//			logger.error("context", ex);
//			errCode = "E07113";
//			errMsg = "ไม่สำเร็จ ["+ex.getMessage()+"]";
//			log.setError(errCode, errMsg);
//			epayLogService.insertLog(log);
//			return setXml(errCode, errMsg, "", "");
//		}
		return setXml("I07000", "สำเร็จ", INBOUND_UUID, resEncryptXml);
	}
	
	@ApiOperation(value = "ForTest", notes = "เตรียมข้อมูลเพื่อทดสอบ epay,payin")
	@PostMapping(value = "prepare-data-for-test", consumes = "application/xml", produces = "application/xml", headers = "Accept-Language")
	public Object prepareData(@RequestBody ReqEpaymentXmlModel reqXml) throws JAXBException {
		String res = "";
		try {
			String unescapeXmlEscap = StringEscapeUtils.unescapeXml(reqXml.getEncryptData());
			String encryptData = pkiService.dSigWithEncrypt(166, 164, reqXml.getAttachFile(), unescapeXmlEscap);
			String encryptDataEscape = StringEscapeUtils.escapeXml(encryptData);
			res = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+"<Epayment>"+"<BankCode>"+reqXml.getBankCode()+"</BankCode>"+"<PaymentLine>"
			+reqXml.getPaymentLine()+"</PaymentLine>"+"<AttachFile>"+reqXml.getAttachFile()+"</AttachFile>"+
			"<MerchartId>"+reqXml.getMerchartId()+"</MerchartId>"+"<TerminalId>"+reqXml.getTerminalId()+"</TerminalId>"+
			"<EncryptData>"+encryptDataEscape+"</EncryptData>"+"</Epayment>";
		}
		catch (Exception ex) {
			logger.error("context", ex);
		}
		return res;
	}
	
	@ApiOperation(value = "ForTest", notes = "decrypt ข้อมูลเพื่อทดสอบ epay,payin")
	 @PostMapping(value = "verify-data-Inbound-for-test", consumes = "application/xml", produces = "application/xml", headers = "Accept-Language")
	 public Object verifyDataInbound(@RequestBody ReqEpaymentXmlModel reqXml) throws JAXBException {
	  String res = "";
	  try {
	   

	   res = pkiService.verifyWithDecrypt(63, 67, reqXml.getAttachFile(), reqXml.getEncryptData());

	   System.out.println("decryptData : " + reqXml.toString());
	     
	  }
	  catch (Exception ex) {
		logger.error("context", ex);
	  }
	  return res;
	 }
	
	
	@ApiOperation(value = "ForTest", notes = "decrypt ข้อมูลเพื่อทดสอบ epay,payin")
	 @PostMapping(value = "verify-data-outbound-for-test", consumes = "application/xml", produces = "application/xml", headers = "Accept-Language")
	 public Object verifyDataOutbound(@RequestBody Rd2BankEpaymentXmlModel reqXml) throws JAXBException {
	  String res = "";
	  try {
	   
	   System.out.println("xxx : " + reqXml.getEncryptData());
	   res = pkiService.verifyWithDecrypt(63, 67, reqXml.getAttachFile(), reqXml.getEncryptData());
	     
	  }
	  catch (Exception ex) {
	   ex.printStackTrace();
	  }
	  return res;
	 }
}
