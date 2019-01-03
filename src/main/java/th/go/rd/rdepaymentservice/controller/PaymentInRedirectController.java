package th.go.rd.rdepaymentservice.controller;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
import th.go.rd.rdepaymentservice.model.EpayLogModel;
import th.go.rd.rdepaymentservice.service.EpayLogService;
import th.go.rd.rdepaymentservice.service.EpayReceiverPaymentLineService;
import th.go.rd.rdepaymentservice.service.MasterSystemInfoService;
import th.go.rd.rdepaymentservice.service.PaymentInService;
import th.go.rd.rdepaymentservice.service.PkiService;
import th.go.rd.rdepaymentservice.util.GenXmlHelper;
import th.go.rd.rdepaymentservice.xml.CSConfPaymentDetailModel;
import th.go.rd.rdepaymentservice.xml.CSConfPaymentStatusModel;
import th.go.rd.rdepaymentservice.xml.CSConfirmPaymentInfoModel;
import th.go.rd.rdepaymentservice.xml.CSConfirmPaymentModel;
import th.go.rd.rdepaymentservice.xml.ReqEpaymentXmlModel;

@Controller
@RequestMapping("/epay/payment-in-redirect")
public class PaymentInRedirectController {
	private static Logger logger = LoggerFactory.getLogger(PaymentInRedirectController.class);
	
	@Autowired
	private EpayReceiverPaymentLineService epayReceiverPaymentLineService;
	
	@Autowired
	PaymentInService paymentInService;

	@Autowired
	MasterSystemInfoService masterSystemInfoService;
	
	@Autowired
	private AuthenticationFacade authenticationFacade;
	
	@Autowired
	private EpayLogService epayLogService;
	
	@Autowired
	private MessageSource messageSource;
	
	@Value("${spring.payment-std.redirect-url}")
	private String epayRedirectUri;
	
	@Value("${payment-std.xml-path}")
	private String writeXmlPath;
	
	@Autowired
	private PkiService pkiService;
	
	private String bCode = "";
	private String pLineCode = "";
	
	@ApiOperation(value = "EPayUpdatePaymentStatus",
			notes="สำหรับหน่วยรับชำระภาษีส่งข้อความแจ้งผลการชำระภาษีผ่าน HTTP Post ให้กับกรมสรรพากร และทำการ Redirect มายังหน้าจอของกรมสรรพากร ข้อความจะถูกส่งหลังจากผู้เสียภาษียืนยันการชำระเงิน")
	@PostMapping(value="adjust-status-epay-redirect", headers = "Accept-Language")
	public String  updatePaymentStatusRedirect(String dataFile ,Model model, HttpServletRequest request) 
	{
		String logDetail = String.format("Adjust Status Epay Direct [%s]", "dataFile");
		logger.info(logDetail);
		EpayLogModel log = new EpayLogModel(LogLevel.INFO, LogType.PAYMENT, ApiCode.UPDATE_STATUS_PAYMENT_REDIRECT,
				 "dataFile", logDetail);
		// Variables Declaration
		GenXmlHelper genXmlHelper = new GenXmlHelper();
		Authentication authentication = authenticationFacade.getAuthentication();
		String USERNAME = authentication.getName();	
		String url = "";
		String responseCode = "";
		String responseMessage = "";
		String errCode = "";
		String errMsg = "";
		try 
		{
			// Variables Declaration
			String INBOUND_UUID = UUID.randomUUID().toString();
			String stringXml = "";
			String bankCode = "";
			String paymentLine = "";
			String attachFile = "";
			
			/*----------- Convert String to XmlObject--------------*/
			ReqEpaymentXmlModel reqXml = new ReqEpaymentXmlModel();
			reqXml = (ReqEpaymentXmlModel) genXmlHelper.convertToXmlObject(dataFile,reqXml);
			/*----------- Convert String to XmlObject--------------*/
			
			bankCode = reqXml.getBankCode();
			paymentLine = reqXml.getPaymentLine();
			bCode = bankCode;
			pLineCode = paymentLine;
			attachFile = reqXml.getAttachFile();
			/*-------- Validate Field ---------*/
			if(bankCode.equals("") || bankCode.length() > 3 || bankCode.length() < 3) 
			{
				model = dispError("E07113   ข้อมูลไม่ถูกต้อง [BankCode]",model);
				errCode = "E07113";
				errMsg = messageSource.getMessage("app.man-resp.invalid_field", new Object[] { "BankCode" },
						LocaleContextHolder.getLocale());
			}
			else if(paymentLine.equals("") || paymentLine.length() > 10 || paymentLine.length() <= 0 ) 
			{
				model = dispError("E07113   ข้อมูลไม่ถูกต้อง [PaymentLine]",model);
				errCode = "E07113";
				errMsg = messageSource.getMessage("app.man-resp.invalid_field", new Object[] { "PaymentLine" },
						LocaleContextHolder.getLocale());
			}
			else if(attachFile.equals("") || attachFile.length() > 100 || attachFile.length() <= 0) 
			{
				model = dispError("E07113   ข้อมูลไม่ถูกต้อง [AttachFile]",model);
				errCode = "E07113";
				errMsg = messageSource.getMessage("app.man-resp.invalid_field", new Object[] { "AttachFile" },
						LocaleContextHolder.getLocale());
			}
			else if(reqXml.getEncryptData() == null) 
			{
				model = dispError("E07113   ข้อมูลไม่ถูกต้อง [EncryptData]",model);
				errCode = "E07113";
				errMsg = messageSource.getMessage("app.man-resp.invalid_field", new Object[] { "EncryptData" },
						LocaleContextHolder.getLocale());
			}
			if(!errCode.equals(""))
			{
				return "epayRedirect";
			}
			
			//---- Check bank & payment line
			Optional<MasterReceiverUnit> recEnt = paymentInService.findByRecCode(bankCode);
			if (!recEnt.isPresent()) {
				 errCode = "E07100";
				 errMsg = messageSource.getMessage("app.man-resp.data_not_found_with_arg",
						new Object[] { "BankCode" }, LocaleContextHolder.getLocale());
				log.setError(errCode, errMsg);
				epayLogService.insertLog(log);
				return "epayRedirect";
			}
			MasterReceiverUnit masterReceiverUnit = recEnt.get();
			Optional<MasterPaymentLine> lineEnt = paymentInService.findByPayLineCode(paymentLine);
			if (!lineEnt.isPresent()) {
				 errCode = "E07100";
				 errMsg = messageSource.getMessage("app.man-resp.data_not_found_with_arg",
						new Object[] { "paymentLine" }, LocaleContextHolder.getLocale());
				log.setError(errCode, errMsg);
				epayLogService.insertLog(log);
				return "epayRedirect";
			}
			MasterPaymentLine masPaymentLine = lineEnt.get();
			
			List<EpayReceiverPaymentLine> epayReceiverPaymentLineOpt = epayReceiverPaymentLineService.findByMasterPaymentLineAndMasterReceiverUnit(masPaymentLine, masterReceiverUnit);
			
				
			if(epayReceiverPaymentLineOpt.size() == 0) {
				 errCode = "E07100";
				 errMsg = messageSource.getMessage("app.man-resp.data_not_found_with_arg",
						new Object[] { "BankCode , Payment Line" }, LocaleContextHolder.getLocale());
				log.setError(errCode, errMsg);
				epayLogService.insertLog(log);
				return "epayRedirect";
			}
			//---- end Check bank & payment line	
			
			
			/*-------- Send to decrypt and add dsig ---------*/
			EpayReceiverPaymentLine epayReceiverPaymentLine = epayReceiverPaymentLineOpt.get(0);
			
			int recCode = Integer.parseInt(epayReceiverPaymentLine.getRecCertCode());
			int rdCode = Integer.parseInt(epayReceiverPaymentLine.getRdCertCode());

			String decrptXmlEscap = StringEscapeUtils.unescapeXml(reqXml.getEncryptData());
			String decryptData = pkiService.verifyWithDecrypt(recCode, rdCode, attachFile, decrptXmlEscap);
			/*-------- Send to decrypt and add dsig ---------*/
			
			if(decryptData.substring(0,5).equals("ERROR"))
			{
				errCode = "E07003";
				errMsg = "ไม่สามารถถอดรหัส / ตรวจสอบลายมือชื่อข้อมูล XML";
				model = dispError(errCode+"    "+errMsg,model);
				log.setError(errCode, errMsg);
				epayLogService.insertLog(log);
				return "epayRedirect";
			}
			
			CSConfirmPaymentModel cSConfirmPaymentModel = new CSConfirmPaymentModel();
			cSConfirmPaymentModel = (CSConfirmPaymentModel) genXmlHelper.convertToXmlObject(decryptData, cSConfirmPaymentModel);
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
			if(payModel.getTotalAmount() == null)
			{
				model = dispError("E07113   ข้อมูลไม่ถูกต้อง [TotalAmount]",model);
				errCode = "E07113";
				errMsg = messageSource.getMessage("app.man-resp.invalid_field", new Object[] { "TotalAmount" },
						LocaleContextHolder.getLocale());
				log.setError(errCode, errMsg);
				epayLogService.insertLog(log);
				return "epayRedirect";
			}
			BigDecimal total = new BigDecimal(payModel.getTotalAmount());
			for(int i = 0; i < count; i++)
			{
				if(detail[i].getSystemRefNo().length() > 20 || detail[i].getSystemRefNo().length() <= 0)
				{
					model = dispError("E07113   ข้อมูลไม่ถูกต้อง [SystemRefNo]",model);
					errCode = "E07113";
					errMsg = messageSource.getMessage("app.man-resp.invalid_field", new Object[] { "SystemRefNo" },
							LocaleContextHolder.getLocale());
				}
				if(detail[i].getAmount() == null)
				{
					model = dispError("E07113   ข้อมูลไม่ถูกต้อง [Amount]",model);
					errCode = "E07113";
					errMsg = messageSource.getMessage("app.man-resp.invalid_field", new Object[] { "Amount" },
							LocaleContextHolder.getLocale());
				}
				amount = new BigDecimal(detail[i].getAmount());
				if(amount.compareTo(BigDecimal.ZERO) == 0) 
				{
					model = dispError("E07113   ข้อมูลไม่ถูกต้อง [Amount]",model);
					errCode = "E07113";
					errMsg = messageSource.getMessage("app.man-resp.invalid_field", new Object[] { "Amount" },
							LocaleContextHolder.getLocale());
				}
				if(detail[i].getNid().length() > 13 || detail[i].getNid().length() <= 0) 
				{
					model = dispError("E07113   ข้อมูลไม่ถูกต้อง [Nid]",model);
					errCode = "E07113";
					errMsg = messageSource.getMessage("app.man-resp.invalid_field", new Object[] { "Nid" },
							LocaleContextHolder.getLocale());
				}
			}
			if(!errCode.equals("")) 
			{
				log.setError(errCode, errMsg);
				epayLogService.insertLog(log);
				return "epayRedirect";
			}
			else if(payModel.getBankCode().length() > 3  || payModel.getBankCode().length() < 3) 
			{
				model = dispError("E07113   ข้อมูลไม่ถูกต้อง [BankCode]",model);
				errCode = "E07113";
				errMsg = messageSource.getMessage("app.man-resp.invalid_field", new Object[] { "BankCode" },
						LocaleContextHolder.getLocale());
			}
			else if(payModel.getPaymentLine().length() > 10  || payModel.getPaymentLine().length() <= 0) 
			{
				model = dispError("E07113   ข้อมูลไม่ถูกต้อง [PaymentLine]",model);
				errCode = "E07113";
				errMsg = messageSource.getMessage("app.man-resp.invalid_field", new Object[] { "PaymentLine" },
						LocaleContextHolder.getLocale());
			}
			else if(!bankCode.equals(payModel.getBankCode()))
			{
				model = dispError("E07113   ข้อมูลไม่ถูกต้อง [BankCode]",model);
				errCode = "E07113";
				errMsg = messageSource.getMessage("app.man-resp.invalid_field", new Object[] { "BankCode" },
						LocaleContextHolder.getLocale());
			}
			else if(!paymentLine.equals(payModel.getPaymentLine())) 
			{
				model = dispError("E07113   ข้อมูลไม่ถูกต้อง [PaymentLine]",model);
				errCode = "E07113";
				errMsg = messageSource.getMessage("app.man-resp.invalid_field", new Object[] { "PaymentLine" },
						LocaleContextHolder.getLocale());
			}
			else if(payModel.getRdTransactionNo().length() > 35 || payModel.getRdTransactionNo().length() <= 0) 
			{
				model = dispError("E07113   ข้อมูลไม่ถูกต้อง [RdTransactionNo]",model);
				errCode = "E07113";
				errMsg = messageSource.getMessage("app.man-resp.invalid_field", new Object[] { "RdTransactionNo" },
						LocaleContextHolder.getLocale());
			}
			else if(payModel.getPaymentID().length() > 20 || payModel.getPaymentID().length() <= 0) 
			{
				model = dispError("E07113   ข้อมูลไม่ถูกต้อง [PaymentID]",model);
				errCode = "E07113";
				errMsg = messageSource.getMessage("app.man-resp.invalid_field", new Object[] { "PaymentID" },
						LocaleContextHolder.getLocale());
			}
			else if(payModel.getAgentID().length() > 20 || payModel.getAgentID().length() <= 0) 
			{
				model = dispError("E07113   ข้อมูลไม่ถูกต้อง [AgentID]",model);
				errCode = "E07113";
				errMsg = messageSource.getMessage("app.man-resp.invalid_field", new Object[] { "AgentID" },
						LocaleContextHolder.getLocale());
			}
			else if(payModel.getAgentBrano().length() > 6 || payModel.getAgentBrano().length() <= 0) 
			{
				model = dispError("E07113   ข้อมูลไม่ถูกต้อง [AgentBrano]",model);
				errCode = "E07113";
				errMsg = messageSource.getMessage("app.man-resp.invalid_field", new Object[] { "AgentBrano" },
						LocaleContextHolder.getLocale());
			}
			else if(payModel.getTransmitDate().length() > 14 || payModel.getTransmitDate().length() <= 0) 
			{
				model = dispError("E07113   ข้อมูลไม่ถูกต้อง [TransmitDate]",model);
				errCode = "E07113";
				errMsg = messageSource.getMessage("app.man-resp.invalid_field", new Object[] { "TransmitDate" },
						LocaleContextHolder.getLocale());;
			}
			else if(total.compareTo(BigDecimal.ZERO) == 0)
			{
				model = dispError("E07113   ข้อมูลไม่ถูกต้อง [TotalAmount]",model);
				errCode = "E07113";
				errMsg = messageSource.getMessage("app.man-resp.invalid_field", new Object[] { "TotalAmount" },
						LocaleContextHolder.getLocale());
			}
			else if(status.getPaymentStatus().length() > 1 || status.getPaymentStatus().length() <= 0) 
			{
				model = dispError("E07113   ข้อมูลไม่ถูกต้อง [PaymentStatus]",model);
				errCode = "E07113";
				errMsg = messageSource.getMessage("app.man-resp.invalid_field", new Object[] { "PaymentStatus" },
						LocaleContextHolder.getLocale());
			}
			else if(!isNumeric(status.getPaymentStatus()))
			{
				model = dispError("E07113   ข้อมูลไม่ถูกต้อง [PaymentStatus]",model);
				errCode = "E07113";
				errMsg = messageSource.getMessage("app.man-resp.invalid_field", new Object[] { "PaymentStatus" },
						LocaleContextHolder.getLocale());
		    }
			else if(cSConfirmPaymentModel.getSendMethod().length() > 10 || cSConfirmPaymentModel.getSendMethod().length() <= 0) 
			{
				model = dispError("E07113   ข้อมูลไม่ถูกต้อง [SendMethod]",model);
				errCode = "E07113";
				errMsg = messageSource.getMessage("app.man-resp.invalid_field", new Object[] { "SendMethod" },
						LocaleContextHolder.getLocale());
			}	
			if(!errCode.equals("")) 
			{
				log.setError(errCode, errMsg);
				epayLogService.insertLog(log);
				return "epayRedirect";
			}
			/*-------- Validate Field ---------*/
			int payStatus = Integer.valueOf(status.getPaymentStatus());
			String paymentID = payModel.getPaymentID();
			EpayTaxPaymentOutbound epayTaxPaymentOutbound = new EpayTaxPaymentOutbound();
			EpayTaxPaymentInfo epayTaxPaymentInfo = new EpayTaxPaymentInfo();
			EpayTaxPaymentInboundDetail epayTaxPaymentInboundDetail = new EpayTaxPaymentInboundDetail();
			EpayTaxPaymentInbound epayTaxPaymentInbound = new EpayTaxPaymentInbound();
			EpayTaxPaymentInboundRefOutbound epayTaxPaymentInboundRefOutbound = new EpayTaxPaymentInboundRefOutbound();	
//			Optional<MasterReceiverUnit> recEnt = paymentInService.findByRecCode(bankCode);
//			if (!recEnt.isPresent()) 
//			{
//				model = dispError("E07100   ไม่พบข้อมูลในระบบ [bankCode]",model);
//				errCode = "E07100";
//				errMsg = messageSource.getMessage("app.man-resp.data_not_found_with_arg", new Object[] { "bankCode" },
//						LocaleContextHolder.getLocale());
//				log.setError(errCode, errMsg);
//				epayLogService.insertLog(log);
//				return "epayRedirect";
//				
//			}
//			MasterReceiverUnit masterReceiverUnit = recEnt.get();	
//			Optional<MasterPaymentLine> lineEnt  = paymentInService.findByPayLineCode(paymentLine);
//			if (!lineEnt.isPresent()) 
//			{
//				model = dispError("E07100   ไม่พบข้อมูลในระบบ [paymentLine]",model);
//				errCode = "E07100";
//				errMsg = messageSource.getMessage("app.man-resp.data_not_found_with_arg", new Object[] { "paymentLine" },
//						LocaleContextHolder.getLocale());
//				log.setError(errCode, errMsg);
//				epayLogService.insertLog(log);
//				return "epayRedirect";
//			}
//			MasterPaymentLine masPaymentLine = lineEnt.get();
			List<EpayReceiverPaymentLine> reciverPayLineEnt = paymentInService.findByMasterPaymentLineAndMasterReceiverUnit(masPaymentLine,masterReceiverUnit);
			if (reciverPayLineEnt.size() < 1)
			{
				model = dispError("E07100   ไม่พบข้อมูลในระบบ [lineEnt,masterReceiverUnit]",model);
				errCode = "E07100";
				errMsg = messageSource.getMessage("app.man-resp.data_not_found_with_arg", new Object[] { "lineEnt,masterReceiverUnit" },
						LocaleContextHolder.getLocale());
				log.setError(errCode, errMsg);
				epayLogService.insertLog(log);
				return "epayRedirect";
			}			
			EpayReceiverPaymentLine recPayLineEnt = reciverPayLineEnt.get(0);		
			Optional<MasterPaymentStatus> mPaymentStatus = paymentInService.findById(payStatus);
			if (!mPaymentStatus.isPresent()) 
			{
				model = dispError("E07100   ไม่พบข้อมูลในระบบ [PaymentStatus]",model);
				errCode = "E07100";
				errMsg = messageSource.getMessage("app.man-resp.data_not_found_with_arg", new Object[] { "PaymentStatus" },
						LocaleContextHolder.getLocale());
				log.setError(errCode, errMsg);
				epayLogService.insertLog(log);
				return "epayRedirect";
			}			
			MasterPaymentStatus masterPaymentStatus = mPaymentStatus.get();
			
			Optional<MasterSendMethod> OptionalMasterSendMethod = paymentInService.findBySendMethod(cSConfirmPaymentModel.getSendMethod());
			if (!OptionalMasterSendMethod.isPresent()) 
			{
				model = dispError("E07100   ไม่พบข้อมูลในระบบ [SendMethod]",model);
				errCode = "E07100";
				errMsg = messageSource.getMessage("app.man-resp.data_not_found_with_arg", new Object[] { "SendMethod" },
						LocaleContextHolder.getLocale());
				log.setError(errCode, errMsg);
				epayLogService.insertLog(log);
				return "epayRedirect";
			}
			MasterSendMethod masterSendMethod = OptionalMasterSendMethod.get();
			
			// ประกาศไว้รอ function cert
			String recCertCode = ""; // REC_CERT_CODE 
			String recPayLineCode = ""; // REC_PAY_LINE_CODE คือ primary key ของ table EPAY_RECEIVER_PAYMENT_LINE 	
			Date currentDate = new Date();
			Date tranmitDate = new SimpleDateFormat("yyyyMMddHHmmss").parse(payModel.getTransmitDate());
			Date tranferDate = new SimpleDateFormat("yyyyMMddHHmmss").parse(status.getTransferDate());			
			if(recPayLineEnt.getStartDate() == null && recPayLineEnt.getEndDate() == null) 
			{ 
				logger.info("Not found Start Date and End Date");
			}
			else if(currentDate.after(recPayLineEnt.getStartDate()) && currentDate.before(recPayLineEnt.getEndDate())) 
			{
				logger.info("In range");
			}
			else if(currentDate.equals(recPayLineEnt.getStartDate()) || currentDate.equals(recPayLineEnt.getEndDate())) 
			{
				logger.info("In range");	
			}
			else
			{
//				if(recPayLineEnt.getMasterStatus().getStatus() != null && recPayLineEnt.getMasterStatus().getStatus().equals("A"))
//				{
//					responseCode = "E07106";
//				}
//				else if(masterReceiverUnit.getMasterStatus().getStatus() != null && masterReceiverUnit.getMasterStatus().getStatus().equals("A"))
//				{
//					responseCode = "E07106";
//				}
//				else
//				{
					model = dispError("E07105  ไม่สามารถทำรายการได้ เนื่องจากข้อมูลที่ส่งเข้ามาอยู่นอกวันที่ให้บริการ",model);
					errCode = "E07105";
					errMsg = "ไม่สามารถทำรายการได้ เนื่องจากข้อมูลที่ส่งเข้ามาอยู่นอกวันที่ให้บริการ";
					log.setError(errCode, errMsg);
					epayLogService.insertLog(log);
					return "epayRedirect";
		//		}
			}
			tranNo = payModel.getRdTransactionNo();
			Optional<EpayTaxPaymentOutbound> optionalEpayTaxPaymentOutbound = paymentInService.findByTranNo(tranNo);
			if (!optionalEpayTaxPaymentOutbound.isPresent()) 
			{
				model = dispError("E07100   ไม่พบข้อมูลในระบบ [TransactionNo]",model);
				errCode = "E07100";
				errMsg = messageSource.getMessage("app.man-resp.data_not_found_with_arg", new Object[] { "TransactionNo" },
						LocaleContextHolder.getLocale());
				log.setError(errCode, errMsg);
				epayLogService.insertLog(log);
				return "epayRedirect";
			}
			epayTaxPaymentOutbound = optionalEpayTaxPaymentOutbound.get();
			Optional<EpayTaxPaymentInfo> optionalEpayTaxPaymentInfo = paymentInService.findByEpayTaxPaymentInfoIdAndAgentIdAndTotalAmount(epayTaxPaymentOutbound.getEpayTaxPaymentInfo().getEpayTaxPaymentInfoId(),payModel.getAgentID(),total);
			if (!optionalEpayTaxPaymentInfo.isPresent()) 
			{
				model = dispError("E07100   ไม่พบข้อมูลในระบบ [infoID,agentID,totalamount]",model);
				errCode = "E07100";
				errMsg = messageSource.getMessage("app.man-resp.data_not_found_with_arg", new Object[] { "infoID,agentID,totalamount" },
						LocaleContextHolder.getLocale());
				log.setError(errCode, errMsg);
				epayLogService.insertLog(log);
				return "epayRedirect";
			}
			epayTaxPaymentInfo = optionalEpayTaxPaymentInfo.get();
			Date expDate = new SimpleDateFormat("yyyy-MM-dd").parse(epayTaxPaymentInfo.getExpDate().toString().substring(0,10));
		    for(int i = 0; i < count; i++)
			{
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
			epayTaxPaymentInbound.setResponseCode(responseCode);
			epayTaxPaymentInbound.setReponseMessage(responseMessage);
			paymentInService.save(epayTaxPaymentInbound);
			logger.info("Insert Inbound Success!!");
			
			epayTaxPaymentInboundRefOutbound.setEpayTaxPaymentOutbound(epayTaxPaymentOutbound);
			epayTaxPaymentInboundRefOutbound.setEpayTaxPaymentInbound(epayTaxPaymentInbound);
			paymentInService.save(epayTaxPaymentInboundRefOutbound);	
			logger.info("Insert InboundRefOutbound Success!!");
			/*------- Write Text File -------*/
			stringXml = genXmlHelper.convertToXmlString(reqXml,true);
			Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(writeXmlPath+epayTaxPaymentInbound.getAttachFile()), "utf-8")); 
			{
				writer.write(stringXml);
			}
			if(writer != null)
			{
				writer.close();
			}
			/*------- Write Text File -------*/
			
			/*------- Send to Response -------*/
			url = epayRedirectUri+"/"+paymentID+"/"+INBOUND_UUID;
			model.addAttribute("page",url);
			/*------- Send to Response -------*/
			
			// Check condition
			infoId =  epayTaxPaymentOutbound.getEpayTaxPaymentInfo().getEpayTaxPaymentInfoId();
			epayTaxPaymentInfo = paymentInService.findByEpayTaxPaymentInfoId(infoId);	
			tranmitDate = new SimpleDateFormat("yyyyMMdd").parse(payModel.getTransmitDate().substring(0,8));
			if(payStatus == 1)
			{
				if(tranmitDate.after(expDate)) 
				{
					epayTaxPaymentInbound.setResponseCode("E07108");
					paymentInService.save(epayTaxPaymentInbound);
					log.setResponseCode("I07000");
					epayLogService.insertLog(log);
					return "epayRedirect";

				}
				if(epayTaxPaymentInfo.getMasterStatus().getStatus().equals("I"))
				{
					epayTaxPaymentInbound.setResponseCode("E07110");
					paymentInService.save(epayTaxPaymentInbound);
					log.setResponseCode("I07000");
					epayLogService.insertLog(log);
					return "epayRedirect";
				}
				if(epayTaxPaymentInfo.getEpayTaxPaymentInbound() != null) 
				{
					epayTaxPaymentInbound.setResponseCode("E07109");
					paymentInService.save(epayTaxPaymentInbound);
					log.setResponseCode("I07000");
					epayLogService.insertLog(log);
					return "epayRedirect";
				}
				if(epayTaxPaymentOutbound.getMasterStatus().getStatus().equals("I")) 
				{
					epayTaxPaymentInbound.setResponseCode("E07112");
					paymentInService.save(epayTaxPaymentInbound);
					log.setResponseCode("I07000");
					epayLogService.insertLog(log);
					return "epayRedirect";
				}
				epayTaxPaymentInfo.setEpayTaxPaymentInbound(epayTaxPaymentInbound);
				epayTaxPaymentInfo.setUpdateBy(USERNAME);
				epayTaxPaymentInfo.setUpdateDate(now);
				paymentInService.save(epayTaxPaymentInfo);
				logger.info("Update info Success!!");
				log.setResponseCode("I07000");
				epayLogService.insertLog(log);
				return "epayRedirect";
			}
		} 
		catch (Exception ex) {
			logger.error("context", ex);
			errCode = "E07113";
			errMsg = "ไม่สำเร็จ ["+ex.getMessage()+"]";
			model = dispError(errCode+"    "+errMsg,model);
			log.setError(errCode, errMsg);
			epayLogService.insertLog(log);
			return "epayRedirect";
		}
		return "epayRedirect";
	}
	public Model dispError(String Desc, Model model ) 
	{
		String bankCodeText = "หน่วยรับชำระ             ";
		String paymentLineText = "ช่องทางการรับชำระ     ";
		String descText = "คำอธิบาย                  ";
		model.addAttribute("bankCode",bCode);
		model.addAttribute("errBankCode",bankCodeText+bCode);
		model.addAttribute("errPaymentLine",paymentLineText+pLineCode);
		model.addAttribute("errDesc",descText+Desc);
		return model;
	}
	public static boolean isNumeric(String str)
	{
	    for (char c : str.toCharArray())
	    {
	        if (!Character.isDigit(c)) return false;
	    }
	    return true;
	}
}
