package th.go.rd.rdepaymentservice.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import th.go.rd.rdepaymentservice.component.AuthenticationFacade;
import th.go.rd.rdepaymentservice.constant.ApiCode;
import th.go.rd.rdepaymentservice.constant.LogLevel;
import th.go.rd.rdepaymentservice.constant.LogType;
import th.go.rd.rdepaymentservice.entity.EpayReceiverPaymentLine;
import th.go.rd.rdepaymentservice.entity.EpayTaxPaymentInbound;
import th.go.rd.rdepaymentservice.entity.EpayTaxPaymentInboundRefOutbound;
import th.go.rd.rdepaymentservice.entity.EpayTaxPaymentInfo;
import th.go.rd.rdepaymentservice.entity.EpayTaxPaymentInfoDetail;
import th.go.rd.rdepaymentservice.entity.EpayTaxPaymentOutbound;
import th.go.rd.rdepaymentservice.entity.MasterStatus;
import th.go.rd.rdepaymentservice.manager.RestManager;
import th.go.rd.rdepaymentservice.model.EpayLogModel;
import th.go.rd.rdepaymentservice.model.UpdatePaymentStatusModel;
import th.go.rd.rdepaymentservice.repository.EpayTaxPaymentInboundRefOutboundRepository;
import th.go.rd.rdepaymentservice.repository.EpayTaxPaymentInboundRepository;
import th.go.rd.rdepaymentservice.repository.EpayTaxPaymentInfoRepository;
import th.go.rd.rdepaymentservice.repository.MasterStatusRepository;
import th.go.rd.rdepaymentservice.request.ReqCheckPayinSlipModel;
import th.go.rd.rdepaymentservice.request.ReqEpayGetPaymentInfoProcessing;
import th.go.rd.rdepaymentservice.request.ReqSubmitPayTaxModel;
import th.go.rd.rdepaymentservice.request.ReqSubmitPaymentDetailModel;
import th.go.rd.rdepaymentservice.response.ResCheckPayinSlipModel;
import th.go.rd.rdepaymentservice.response.ResPaymentInfoH2h;
import th.go.rd.rdepaymentservice.response.ResPaymentInfoProcessing;
import th.go.rd.rdepaymentservice.response.ResSubmitPayTaxDataModel;
import th.go.rd.rdepaymentservice.response.ResponseModel;
import th.go.rd.rdepaymentservice.service.EpayLogService;
import th.go.rd.rdepaymentservice.service.EpayReceiverPaymentLineService;
import th.go.rd.rdepaymentservice.service.EpayTaxPaymentInfoService;
import th.go.rd.rdepaymentservice.service.EpayTaxPaymentOutBoundService;
import th.go.rd.rdepaymentservice.service.GeneratePDFForPayInSlipService;
import th.go.rd.rdepaymentservice.service.MasterSystemInfoService;
import th.go.rd.rdepaymentservice.util.ConvertThaiBahtHelper;
import th.go.rd.rdepaymentservice.util.CtlCodeHelper;
import th.go.rd.rdepaymentservice.util.DateHelper;
import th.go.rd.rdepaymentservice.util.IPAddressUtil;

@RestController
@RequestMapping("/epay/internal-service")
public class InternalServiceController {

	private static final Logger logger = LoggerFactory.getLogger(InternalServiceController.class);

	@Autowired
	private EpayTaxPaymentInfoRepository epayTaxPaymentInfoRepository;

	@Autowired
	private EpayTaxPaymentOutBoundService epayTaxPaymentOutboundService;

	@Autowired
	private EpayReceiverPaymentLineService epayReceiverPaymentLineService;

	@Autowired
	private MasterStatusRepository masterStatusRepository;

	@Autowired
	private GeneratePDFForPayInSlipService generatePDFForPayInSlipService;

	@Autowired
	private EpayTaxPaymentInboundRepository epayTaxPaymentInboundDetailRepository;

	@Autowired
	private EpayTaxPaymentInboundRefOutboundRepository epayTaxPaymentInboundRefOutboundRepository;

	@Autowired
	private EpayTaxPaymentInboundRepository epayTaxPaymentInboundRepository;

	@Autowired
	private MasterSystemInfoService masterSystemInfoService;

	@Autowired
	private AuthenticationFacade authenticationFacade;

	@Autowired
	private EpayLogService epayLogService;
	
	@Autowired
	private EpayTaxPaymentInfoService epayTaxPaymentInfoService;

	@ApiOperation(value = "Payment", notes = "สำหรับระบบบริการผู้เสียภาษีส่งข้อมูลการชำระเงินเพื่อขอรหัสชำระ")
	@PostMapping(value = "submit-tax", produces = "application/json", headers = "Accept-Language")
	public Object ePaySubmitPayTax(@Valid @RequestBody ReqSubmitPayTaxModel reqSubmitPayTaxModel,
			@RequestHeader(name = "client_id") String clientId, @RequestHeader(name = "secret_id") String secretId,
			HttpServletRequest request) {
		String detail = String.format("Submit Tax[%s]", reqSubmitPayTaxModel.getRefNo());
		logger.info("start API /internal-service/submit-tax");
		logger.info(detail);
		RestManager restManager = RestManager.getInstance();
		String username = authenticationFacade.getAuthentication().getName();
		EpayLogModel log = new EpayLogModel(LogLevel.INFO, LogType.INTERNAL_SERVICE, ApiCode.SUBMIT_PAY_TAX,
				username, detail);
		Date now = new Date();
		DateHelper dateHelper = new DateHelper();
		String OUTBOUND_UUID = UUID.randomUUID().toString();
		Optional<MasterStatus> STATUS = masterStatusRepository.findById("A");

		long AUTH_ID = 1002;
		if (!masterSystemInfoService.authen(clientId, secretId, AUTH_ID)) {
			logger.error("unauthorize");
			restManager.addGlobalErrorbyProperty("app.sys-resp.unauthorize");
			epayLogService.insertLog(restManager, log);
			restManager.throwsException();
		}
		
		logger.info("auten & authorize done");
		
		Optional<EpayTaxPaymentInfo> epayInfo = epayTaxPaymentInfoService.findByRefNoAndMasterStatus(reqSubmitPayTaxModel.getRefNo(), STATUS.isPresent()? STATUS.get(): new MasterStatus());
		EpayTaxPaymentInfo info = null;
		if (epayInfo.isPresent()) {
			info = epayInfo.get();
			if (info.getEpayTaxPaymentInbound() != null) {
				logger.error("this refNo is alreader paid");
				restManager.addGlobalErrorbyProperty("app.man-resp.ref_already_paid");
				epayLogService.insertLog(restManager, log);
				restManager.throwsException();
			}
		}

		ResSubmitPayTaxDataModel data = new ResSubmitPayTaxDataModel(OUTBOUND_UUID);
		epayInfo = epayTaxPaymentInfoService
				.findByRefNoAndMasterStatusAndIsRound(reqSubmitPayTaxModel.getRefNo(), STATUS.isPresent()? STATUS.get(): new MasterStatus(),
						reqSubmitPayTaxModel.getIsRound());
		if (epayInfo.isPresent()) {
			info = epayInfo.get();
			
			if (dateHelper.isAfterDate(now, info.getExpDate())) {
				restManager.addGlobalErrorbyProperty("app.man-resp.expire");
				logger.error("this refNo is expired");
				epayLogService.insertLog(restManager, log);
				restManager.throwsException();
			}
			EpayTaxPaymentOutbound outbound = new EpayTaxPaymentOutbound();
			outbound.setEpayTaxPaymentInfo(info);
			outbound.setUuid(OUTBOUND_UUID);
			outbound.setCreateDate(now);
			outbound.setUpdateDate(now);
			outbound.setMasterStatus(STATUS.get());
			// outbound.setCtlCode(CTL_CODE);
			outbound.setCreateBy(clientId);
			outbound.setUpdateBy(clientId);

			List<EpayReceiverPaymentLine> paymentLines = epayReceiverPaymentLineService
					.findEpayReceiver(reqSubmitPayTaxModel.getPaymentLine(), reqSubmitPayTaxModel.getBankCode());
			if (paymentLines.size() > 0) {
				outbound.setEpayReceiverPaymentLine(paymentLines.get(0));
			}

			info.getEpayTaxPaymentOutbounds().add(outbound);
			epayTaxPaymentInfoService.save(info);

		} else {
			// Payment Info not exist
			
			Optional<EpayTaxPaymentInfo> epayTaxPaymentInfoOpt = epayTaxPaymentInfoService
					.findByRefNoAndMasterStatus(reqSubmitPayTaxModel.getRefNo(),STATUS.isPresent()? STATUS.get(): new MasterStatus());
			if (epayTaxPaymentInfoOpt.isPresent()) {
				EpayTaxPaymentInfo epayTaxPaymentInfo = epayTaxPaymentInfoOpt.get();
				Optional<MasterStatus> masterStatusOpt = masterStatusRepository.findByStatus("I");
				epayTaxPaymentInfo.setMasterStatus(masterStatusOpt.orElse(new MasterStatus()));
				List<EpayTaxPaymentOutbound> epayTaxPaymentOutbounds = epayTaxPaymentInfo.getEpayTaxPaymentOutbounds();
				masterStatusOpt = masterStatusRepository.findByStatus("I");
				for (EpayTaxPaymentOutbound e : epayTaxPaymentOutbounds) {
					e.setMasterStatus(masterStatusOpt.orElse(new MasterStatus()));
				}
				epayTaxPaymentInfo.setEpayTaxPaymentOutbounds(epayTaxPaymentOutbounds);
				
				try {
					epayTaxPaymentInfoService.save(epayTaxPaymentInfo);
				} catch (Exception ex) {
					logger.error("update outbound fail : " + ex.getMessage());
					restManager.addGlobalErrorbyProperty("app.sys-resp.database_error");
					epayLogService.insertLog(restManager, log);
					restManager.throwsException();
				}
			}
			
			logger.info("Create new EpayTaxPaymentInfo");
			info = new EpayTaxPaymentInfo();
			info.setRefNo(reqSubmitPayTaxModel.getRefNo());
			info.setExpDate(reqSubmitPayTaxModel.getExpDate());
			info.setPayLineCode(reqSubmitPayTaxModel.getPaymentLine());
			info.setPaymentMethod(reqSubmitPayTaxModel.getPaymentMethod());
			info.setAccountNo(reqSubmitPayTaxModel.getAccountNo());
			info.setRecCode(reqSubmitPayTaxModel.getBankCode());
			info.setCreateDate(now);
			info.setUpdateDate(now);
			info.setOrderTranDate(reqSubmitPayTaxModel.getOrderTransferDate());
			info.setAgentId(reqSubmitPayTaxModel.getAgentId());
			info.setAgentBraNo(reqSubmitPayTaxModel.getAgentBrano());
			info.setAgentName(reqSubmitPayTaxModel.getAgentName());
			info.setIsRound(reqSubmitPayTaxModel.getIsRound().charAt(0));
			info.setMasterStatus(STATUS.get());
			info.setTotalAmount(reqSubmitPayTaxModel.getTotalAmount());
			info.setTaxAmount(reqSubmitPayTaxModel.getTaxAmount());
			info.setSurchargeAmount(reqSubmitPayTaxModel.getSurchargeAmount());
			info.setTaxMonthDisplay(reqSubmitPayTaxModel.getTaxMonthDisplay());
			info.setFormCodeDisplay(reqSubmitPayTaxModel.getFormCodeDisplay());
			info.setCriminalFinesAmount(reqSubmitPayTaxModel.getCriminalFinesAmount());
			info.setNetTaxAmount(reqSubmitPayTaxModel.getNetTaxAmount());
			info.setPenaltyAmount(reqSubmitPayTaxModel.getPenaltyAmount());
			info.setTotalTaxAmount(reqSubmitPayTaxModel.getTotalTaxAmount());
			info.setCreateBy(clientId);
			info.setUpdateBy(clientId);

			EpayTaxPaymentOutbound outbound = new EpayTaxPaymentOutbound();
			outbound.setEpayTaxPaymentInfo(info);
			outbound.setUuid(OUTBOUND_UUID);
			outbound.setMasterStatus(STATUS.get());
			// outbound.setCtlCode(CTL_CODE);
			outbound.setCreateDate(now);
			outbound.setCreateBy(clientId);
			outbound.setUpdateDate(now);
			outbound.setUpdateBy(clientId);

			List<EpayReceiverPaymentLine> paymentLines = epayReceiverPaymentLineService
					.findEpayReceiver(reqSubmitPayTaxModel.getPaymentLine(), reqSubmitPayTaxModel.getBankCode());
			if (paymentLines.size() > 0) {
				outbound.setEpayReceiverPaymentLine(paymentLines.get(0));
			}

			List<EpayTaxPaymentOutbound> outbounds = new ArrayList<>();
			outbounds.add(outbound);
			info.setEpayTaxPaymentOutbounds(outbounds);
			List<EpayTaxPaymentInfoDetail> epayDetails = new ArrayList<>();

			List<ReqSubmitPaymentDetailModel> details = reqSubmitPayTaxModel.getPaymentDetail();
			for (ReqSubmitPaymentDetailModel reqSubmitPaymentDetailModel : details) {

				EpayTaxPaymentInfoDetail epayDetail = new EpayTaxPaymentInfoDetail();
				epayDetail.setSysRefNo(reqSubmitPaymentDetailModel.getSystemRefNo());
				epayDetail.setFormCode(reqSubmitPaymentDetailModel.getFormCode());
				epayDetail.setTotalAmount(reqSubmitPaymentDetailModel.getTotalAmount());
				epayDetail.setNid(reqSubmitPaymentDetailModel.getNid());
				epayDetail.setNidBraNo(reqSubmitPaymentDetailModel.getNidBrano());
				epayDetail.setPaymentMessage(reqSubmitPaymentDetailModel.getTaxMonth());
				epayDetail.setCriminalFinesAmount(reqSubmitPaymentDetailModel.getCriminalFinesAmount());
				epayDetail.setNetTaxAmount(reqSubmitPaymentDetailModel.getNetTaxAmount());
				epayDetail.setPenaltyAmount(reqSubmitPaymentDetailModel.getPenaltyAmount());
				epayDetail.setTotalTaxAmount(reqSubmitPaymentDetailModel.getTotalTaxAmount());
				epayDetail.setSurchargeAmount(reqSubmitPaymentDetailModel.getSurchargeAmount());
				epayDetail.setTaxAmount(reqSubmitPaymentDetailModel.getTaxAmount());
				epayDetail.setIsRound(reqSubmitPaymentDetailModel.getIsRound().charAt(0));
				epayDetail.setEpayTaxPaymentInfo(info);
				epayDetail.setCreateBy(clientId);
				epayDetail.setCreateDate(now);
				epayDetail.setUpdateBy(clientId);
				epayDetail.setUpdateDate(now);

				epayDetails.add(epayDetail);
				info.setEpayTaxPaymentInfoDetails(epayDetails);
			}
		}

		try {
			epayTaxPaymentInfoService.save(info);
			logger.info("insert complete");
		} catch (Exception ex) {
			logger.error("insert info fail : " + ex.getMessage());
			restManager.addGlobalErrorbyProperty("app.sys-resp.database_error");
			epayLogService.insertLog(restManager, log);
			restManager.throwsException();
		} 

		restManager.throwsException();

		log.setDescription("success");
		epayLogService.insertLog(restManager, log);
		 
		logger.info("finish /internal-service/submit-tax");
		return restManager.addSuccess(data);
	}

	@ApiOperation(value = "DowloadPayinSlip", notes = "สำหรับ download ใบ pay in-slip")
	@GetMapping(value = "payin-slip", produces = "application/json", headers = "Accept-Language")
	public ResponseEntity<byte[]> downloadPayInSlip(@RequestParam("uuid") String uuid,
			@RequestHeader(name = "client_id") String clientId, @RequestHeader(name = "secret_id") String secretId)
			throws Exception {
		RestManager restManager = RestManager.getInstance();
		logger.info("start api /internal-service/payin-slip");
		
		String username = authenticationFacade.getAuthentication().getName();
		
		EpayLogModel log = new EpayLogModel(LogLevel.INFO, LogType.INTERNAL_SERVICE, ApiCode.DOWNLOAD_PAYIN_SLIP,
				username, "");

		Date today = new Date();
		CtlCodeHelper ctlCodeHelper = new CtlCodeHelper();
		String runningNo = epayTaxPaymentInfoRepository.getCTLCode();
		String OUTBOUND_UUID = UUID.randomUUID().toString();

		long AUTH_ID = 2;
		if (!masterSystemInfoService.authen(clientId, secretId, AUTH_ID)) {
			restManager.addGlobalErrorbyProperty("app.sys-resp.unauthorize");
			logger.error("unauthorize");
			epayLogService.insertLog(restManager, log);
			restManager.throwsException();
		}
		
		logger.info("auten & authorize done");

		Optional<EpayTaxPaymentOutbound> optionalPaymentOutbound = epayTaxPaymentOutboundService.findByUuid(uuid);
		EpayTaxPaymentOutbound epayTaxPaymentOutbound = null;
		if (optionalPaymentOutbound.isPresent()) {
			epayTaxPaymentOutbound = optionalPaymentOutbound.get();
		}
		else {
			logger.error("uuid not found " + uuid);
			restManager.addFieldErrorbyProperty("uuid", "app.man-resp.uuid_not_found", uuid);
			epayLogService.insertLog(restManager, log);
			restManager.throwsException();
		}

		EpayTaxPaymentInfo epayTaxPaymentInfo = epayTaxPaymentOutbound.getEpayTaxPaymentInfo();
		MasterStatus masterStatus = epayTaxPaymentOutbound.getMasterStatus();
		EpayReceiverPaymentLine epayReceiverPaymentLine = epayTaxPaymentOutbound.getEpayReceiverPaymentLine();

		// read totalAmount to thai
		ConvertThaiBahtHelper convertThaiBahtHelper = new ConvertThaiBahtHelper();
		String text = convertThaiBahtHelper.getThaiBaht(epayTaxPaymentInfo.getTotalAmount());
		logger.info(text);
		DateHelper dateHelper = new DateHelper();
		if (dateHelper.isAfterDate(today, epayTaxPaymentInfo.getExpDate())) {
			// is expire date
			logger.error("Error Code : E007108 [refno is expired]");
			restManager.addGlobalErrorbyProperty("app.man-resp.expire");
			epayLogService.insertLog(restManager, log);
			restManager.throwsException();
		}

		else if (!epayTaxPaymentInfo.getMasterStatus().getStatus().equals("A")
				&& epayTaxPaymentOutbound.getMasterStatus().getStatus().equals("I")) {
			// refno has been cancel
			logger.error("Error Code : E007108 [refno is cancelled]");
			restManager.addGlobalErrorbyProperty("app.man-resp.cancel");
			epayLogService.insertLog(restManager, log);
			restManager.throwsException();
		}

		else if (epayTaxPaymentInfo.getEpayTaxPaymentInbound() != null) {
			// has been paid
			logger.error("Error Code : E007109 [refno is already paid]");
			restManager.addGlobalErrorbyProperty("app.man-resp.already_paid");
			epayLogService.insertLog(restManager, log);
			restManager.throwsException();
		}

		boolean genCtlCode = true;
		String tranNo = null;
		List<EpayTaxPaymentOutbound> listOutbound = epayTaxPaymentInfo.getEpayTaxPaymentOutbounds();
		for (EpayTaxPaymentOutbound e : listOutbound) {
			if (e.getCtlCode() != null && e.getMasterStatus().getStatus().equals("A")) {
				tranNo = e.getTranNo();
				genCtlCode = false;
				System.err.println("have ctlCode");
				logger.error("this uuid is already use");
				break;
			}
		}
		
		if (genCtlCode) {
			if (epayTaxPaymentOutbound.getTranNo() != null) {
				System.err.println("have tran no");
				
				// control code
				LocalDate localDateExpDate = epayTaxPaymentInfo.getExpDate().toInstant().atZone(ZoneId.systemDefault())
						.toLocalDate();
				String strTotalAmount = String.valueOf(epayTaxPaymentInfo.getTotalAmount());
				String ctlCode = ctlCodeHelper.generateCtlCode(runningNo, localDateExpDate,
						epayTaxPaymentInfo.getAgentId(), strTotalAmount);
				EpayTaxPaymentOutbound outbound = new EpayTaxPaymentOutbound();
				outbound.setEpayTaxPaymentInfo(epayTaxPaymentInfo);
				outbound.setUuid(OUTBOUND_UUID);
				outbound.setTranNo(epayTaxPaymentOutbound.getTranNo());
				outbound.setCtlCode(ctlCode);
				outbound.setTransmitDate(today);
				outbound.setAttachFile(epayTaxPaymentOutbound.getAttachFile());
				// outbound.setTerminalId(terminalId);
				// outbound.setMerchantId(merchantId);
				outbound.setCreateBy(clientId);
				outbound.setCreateDate(today);
				outbound.setUpdateBy(clientId);
				outbound.setUpdateDate(today);
				outbound.setEpayReceiverPaymentLine(epayReceiverPaymentLine);
				outbound.setMasterStatus(masterStatus);
				epayTaxPaymentOutboundService.save(outbound);
				tranNo = epayTaxPaymentOutbound.getTranNo();
				
				logger.info("Create new outbound uuid : " + OUTBOUND_UUID + " is done");
			} else {
				
				// Gen transaction no
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
				String strDate = dateFormat.format(today);
				String runTransStr = epayTaxPaymentOutboundService.getRunNoTransaction();
				tranNo = String.format("%14.14s%s%s", strDate, epayTaxPaymentInfo.getRefNo(), runTransStr);

				// control code
				LocalDate localDateExpDate = epayTaxPaymentInfo.getExpDate().toInstant().atZone(ZoneId.systemDefault())
						.toLocalDate();
				String strTotalAmount = String.valueOf(epayTaxPaymentInfo.getTotalAmount());
				String ctlCode = ctlCodeHelper.generateCtlCode(runningNo, localDateExpDate,
						epayTaxPaymentInfo.getAgentId(), strTotalAmount);

				// update table
				epayTaxPaymentOutbound.setTranNo(tranNo);
				epayTaxPaymentOutbound.setCtlCode(ctlCode);
				epayTaxPaymentOutbound.setUpdateBy(clientId);
				epayTaxPaymentOutbound.setUpdateDate(today);
				epayTaxPaymentOutboundService.save(epayTaxPaymentOutbound);
				
				logger.info("update outbound tranNo : " + tranNo + " is done");
			}
		}
		byte[] contents = generatePDFForPayInSlipService.getBillPdf(uuid);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_PDF);
		headers.add("Content-Disposition", "attachment; filename=" + tranNo + ".pdf");
		ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(contents, headers, HttpStatus.OK);
		
		logger.info("finish api /internal-service/payin-slip");
		epayLogService.insertLog(restManager, log);
		return response;
	}// close method

	@ApiOperation(value = "EPayGetPaymentInfo", notes = "สำหรับรับข้อมูลแบบฯจากระบบบริการผู้เสียภาษีฯ ในรูปแบบ XML ตามที่กรมสรรพากรกำหนด เพื่อสร้างรายการชำระเงินในระบบชำระภาษีฯ")
	@GetMapping(value = "payment-info-processing", produces = "application/json", headers = "Accept-Language")
	public Object processing(@Valid @RequestBody ReqEpayGetPaymentInfoProcessing reqEpayGetPaymentInfoProcessing,
			@RequestHeader(name = "client_id") String clientId, @RequestHeader(name = "secret_id") String secretId,
			HttpServletRequest request) {
		RestManager restManager = RestManager.getInstance();
		logger.info("Payment Info Processing");
		EpayLogModel log = new EpayLogModel(LogLevel.INFO, LogType.PAYMENT, ApiCode.GET_PAYMENT_INFO,
				clientId, "Payment Info Processing");

		Date transmitDate = reqEpayGetPaymentInfoProcessing.getTransmitDate();
		Calendar cal = Calendar.getInstance();
		cal.setTime(transmitDate);
		cal.add(Calendar.DATE, -1);
		Date beforeDate = cal.getTime();

		long AUTH_ID = 3;
		if (!masterSystemInfoService.authen(clientId, secretId, AUTH_ID)) {
			restManager.addGlobalErrorbyProperty("app.sys-resp.unauthorize");
			epayLogService.insertLog(restManager, log);
			restManager.throwsException();
		}

		List<EpayTaxPaymentInbound> inbounds = epayTaxPaymentInboundDetailRepository
				.findByTransmitDateGreaterThanEqualAndResponseCodeIsNull(beforeDate);
		List<ResPaymentInfoProcessing> resPaymentInfos = new ArrayList<>();
		for (EpayTaxPaymentInbound inbound : inbounds) {
			// if inbound dont have any paymentinfo then skip
			if (inbound.getEpayTaxPaymentInfos().size() < 1) {
				continue;
			}
			EpayTaxPaymentInfo info = inbound.getEpayTaxPaymentInfos().get(0);
			Optional<EpayTaxPaymentInboundRefOutbound> refOptioanal = epayTaxPaymentInboundRefOutboundRepository
					.findByEpayTaxPaymentInbound(inbound);
			EpayTaxPaymentOutbound outbound = null;
			if (refOptioanal.isPresent()) {
				outbound = refOptioanal.get().getEpayTaxPaymentOutbound();
			} else {
				continue;
			}

			ResPaymentInfoProcessing resPaymentInfo = new ResPaymentInfoProcessing();
			resPaymentInfo.setRefNo(info.getRefNo());
			resPaymentInfo.setAgentId(info.getAgentId());
			resPaymentInfo.setTransactionNo(outbound.getTranNo());
			resPaymentInfo.setControlCode(outbound.getCtlCode());
			resPaymentInfo.setTotalAmount(info.getTotalAmount());
			resPaymentInfo.setInfoStatus(info.getMasterStatus().getStatus());
			resPaymentInfo.setTransferDate(inbound.getTransferDate());
			resPaymentInfo.setRecPaymentLineId(inbound.getEpayReceiverPaymentLine().getEpayReceiverPaymentLineId());
			resPaymentInfo.setTransmitDate(inbound.getTransmitDate());
			resPaymentInfo.setTransferDate(inbound.getTransferDate());
			resPaymentInfo.setPaymentStatus(inbound.getMasterPaymentStatus().getMasterPaymentStatusId());
			resPaymentInfo.setPaymentStatusMessage(inbound.getMasterPaymentStatus().getDescriptionEn());
			resPaymentInfo.setOutboundStatus(outbound.getMasterStatus().getStatusNameEn());
			resPaymentInfos.add(resPaymentInfo);
		}

		epayLogService.insertLog(restManager, log);
		restManager.throwsException();
		return restManager.addSuccess(resPaymentInfos);
	}

	@ApiOperation(value = "EPayGetPaymentInfo", notes = "สำหรับรับข้อมูลแบบฯจากระบบบริการผู้เสียภาษีฯ ในรูปแบบ XML ตามที่กรมสรรพากรกำหนด เพื่อสร้างรายการชำระเงินในระบบชำระภาษีฯ")
	@GetMapping(value = "payment-info-h2h/{uuid}", produces = "application/json", headers = "Accept-Language")
	public Object h2h(@PathVariable(value = "uuid") String uuid, @RequestHeader(name = "client_id") String clientId,
			@RequestHeader(name = "secret_id") String secretId, HttpServletRequest request) {
		logger.info("Payment Info H2H");
		RestManager restManager = RestManager.getInstance();
		EpayLogModel log = new EpayLogModel(LogLevel.INFO, LogType.INTERNAL_SERVICE, ApiCode.GET_PAYMENT_INFO,
				clientId, "Payment Info H2H");

		long AUTH_ID = 4;
		if (!masterSystemInfoService.authen(clientId, secretId, AUTH_ID)) {
			restManager.addGlobalErrorbyProperty("app.sys-resp.unauthorize");
			epayLogService.insertLog(restManager, log);
			restManager.throwsException();
		}
		EpayTaxPaymentOutbound outbound = null;

		Optional<EpayTaxPaymentOutbound> outboundOptional = epayTaxPaymentOutboundService.findByUuid(uuid);
		EpayTaxPaymentInfo info = null;
		if (outboundOptional.isPresent()) {
			outbound = outboundOptional.get();
			info = outbound.getEpayTaxPaymentInfo();
		} else {
			restManager.addGlobalErrorbyProperty("app.man-resp.data_not_found");
			epayLogService.insertLog(restManager, log);
			restManager.throwsException();
		}

		ResPaymentInfoH2h paymentInfoH2h = new ResPaymentInfoH2h();
		paymentInfoH2h.setAgentId(info.getAgentId());
		paymentInfoH2h.setControlCode(outbound.getCtlCode());
		paymentInfoH2h.setTotalAmount(info.getTotalAmount());

		epayLogService.insertLog(restManager, log);
		restManager.throwsException();
		return restManager.addSuccess(paymentInfoH2h);
	}

	@ApiOperation(value = "EPayUpdatePaymentStatus", notes = "สำหรับการปรับปรุงสถานะรายการชำระภาษีในระบบชำระภาษีฯ ที่มาจากระบบบริการผู้เสียภาษี ส่งสถานะการยกเลิกแบบ หรือยกเลิกชุดชำระภาษี")
	@PutMapping(value = "adjust-status-tax", headers = "Accept-Language")
	public Object updatePaymentStatusInternal(String refNo, String updateType, String updateValue,
			@RequestHeader(name = "client_id") String clientId, @RequestHeader(name = "secret_id") String secretId,
			HttpServletRequest request) {
		logger.info("Adjust Status Tax");
		EpayLogModel log = new EpayLogModel(LogLevel.INFO, LogType.INTERNAL_SERVICE, ApiCode.GET_PAYMENT_INFO,
				clientId, "Payment Info H2H");
		RestManager restManager = RestManager.getInstance();
		long AUTH_ID = 5;
		if (!masterSystemInfoService.authen(clientId, secretId, AUTH_ID)) {
			restManager.addGlobalErrorbyProperty("app.sys-resp.unauthorize");
			epayLogService.insertLog(restManager, log);
			restManager.throwsException();
		}
		if(refNo.length() != 13)
		{
			Object[] obj = { "RefNo" };
			restManager.addGlobalErrorbyProperty("app.man-resp.invalid_field", obj);
			epayLogService.insertLog(restManager, log);
			restManager.throwsException();
		}
		long id0 = 0; // dummy data in EpayTaxPaymentInbound
		UpdatePaymentStatusModel updatePaymentStatusModel = new UpdatePaymentStatusModel();
		try {
			EpayTaxPaymentOutbound epayTaxPaymentOutbound = new EpayTaxPaymentOutbound();
			EpayTaxPaymentInfo epayTaxPaymentInfo = new EpayTaxPaymentInfo();
			MasterStatus masterStatus = new MasterStatus();
			EpayTaxPaymentInbound epayTaxPaymentInbound = new EpayTaxPaymentInbound();

			Optional<EpayTaxPaymentInfo> optionalEpayTaxPaymentInfo = epayTaxPaymentInfoRepository.findByRefNo(refNo);
			if (!optionalEpayTaxPaymentInfo.isPresent()) {
				System.out.println("Error not found refNo in EpayTaxPaymentInfo");
				Object[] obj = { "EpayTaxPaymentInfo" };
				restManager.addGlobalErrorbyProperty("app.map-resp.id_not_found", obj);
				epayLogService.insertLog(restManager, log);
				restManager.throwsException();
			}
			epayTaxPaymentInfo = optionalEpayTaxPaymentInfo.isPresent() ? optionalEpayTaxPaymentInfo.get() : new EpayTaxPaymentInfo();
			Optional<EpayTaxPaymentOutbound> optionalEpayTaxPaymentOutbound = epayTaxPaymentOutboundService
					.findByEpayTaxPaymentInfo(epayTaxPaymentInfo);
			if (!optionalEpayTaxPaymentOutbound.isPresent()) {
				System.out.println("Error not found epayTaxPaymentInfo in EpayTaxPaymentOutbound");
				Object[] obj = { "epayTaxPaymentInfo" };
				restManager.addGlobalErrorbyProperty("app.map-resp.data_not_found_with_arg", obj);
				epayLogService.insertLog(restManager, log);
				restManager.throwsException();
			}
			epayTaxPaymentOutbound = optionalEpayTaxPaymentOutbound.isPresent()? optionalEpayTaxPaymentOutbound.get() : new EpayTaxPaymentOutbound();
			/*--------- Master Status = I -----------*/
			Optional<MasterStatus> optionalMasterStatus = masterStatusRepository.findByStatus("I");
			if (!optionalMasterStatus.isPresent()) {
				System.out.println("Error not found Status in MasterStatus");
				Object[] obj = { "MasterStatus" };
				restManager.addGlobalErrorbyProperty("app.map-resp.id_not_found", obj);
				epayLogService.insertLog(restManager, log);
				restManager.throwsException();
			}
			masterStatus = optionalMasterStatus.isPresent()? optionalMasterStatus.get() : new MasterStatus();
			/*--------- Master Status = I-----------*/
			if (updateType.equals("0")) {
				if (epayTaxPaymentInfo.getEpayTaxPaymentInbound() != null) {
					updatePaymentStatusModel.setResponseCode("E07109");
					updatePaymentStatusModel.setResponseDesc("Error RefNo = " + refNo);
					epayLogService.insertLog(restManager, log);
					return updatePaymentStatusModel;
				}
				if (epayTaxPaymentOutbound.getCtlCode() != null) {
					epayTaxPaymentOutbound.setMasterStatus(masterStatus);
					try {
						epayTaxPaymentOutboundService.save(epayTaxPaymentOutbound);
					} catch (Exception ex) {
						logger.error(ex.getMessage());
						restManager.addGlobalErrorbyProperty("app.sys-resp.database_error");
					} finally {
						epayLogService.insertLog(restManager, log);
					}
					System.out.println("Update masterStatus in epayTaxPaymentOutbound success");
				}
			} else if (updateType.equals("1")) {
				if (epayTaxPaymentInfo.getEpayTaxPaymentInbound() != null) {
					updatePaymentStatusModel.setResponseCode("E07109");
					updatePaymentStatusModel.setResponseDesc("Error RefNo = " + refNo);
					return updatePaymentStatusModel;
				}
				epayTaxPaymentInfo.setMasterStatus(masterStatus);
				epayTaxPaymentInfoRepository.save(epayTaxPaymentInfo);
				System.out.println("Update masterStatus in epayTaxPaymentInfo success");
			} else if (updateType.equals("2")) {
				if (epayTaxPaymentInfo.getEpayTaxPaymentInbound() != null) {
					updatePaymentStatusModel.setResponseCode("E07109");
					updatePaymentStatusModel.setResponseDesc("Error RefNo = " + refNo);
					return updatePaymentStatusModel;
				}
				Optional<EpayTaxPaymentInbound> optionalEpayTaxPaymentInbound = epayTaxPaymentInboundRepository
						.findById(id0);
				if (!optionalEpayTaxPaymentInbound.isPresent()) {
					System.out.println("Error not found inbound_ID in EpayTaxPaymentInbound");
					Object[] obj = { "EpayTaxPaymentInbound" };
					restManager.addGlobalErrorbyProperty("app.map-resp.id_not_found", obj);
					restManager.throwsException();
				}
				epayTaxPaymentInbound = optionalEpayTaxPaymentInbound.isPresent() ? optionalEpayTaxPaymentInbound.get() : new EpayTaxPaymentInbound();
				epayTaxPaymentInfo.setEpayTaxPaymentInbound(epayTaxPaymentInbound);
				epayTaxPaymentInfoRepository.save(epayTaxPaymentInfo);
				System.out.println("Update EpayTaxPaymentInboundId in epayTaxPaymentInfo success");
			} else if (updateType.equals("3")) {
				if (epayTaxPaymentInfo.getEpayTaxPaymentInbound() != null) {
					updatePaymentStatusModel.setResponseCode("E07109");
					updatePaymentStatusModel.setResponseDesc("Error RefNo = " + refNo);
					return updatePaymentStatusModel;
				}
				// รอสเปคจากพี่ x
			}
		} catch (Exception ex) {
			logger.error("context", ex);
		}
		return restManager.addSuccess("Success");
	}
	
	@GetMapping(value = "/check-payin-slip" , headers = {"Authorization"})
	public Object checkPayinSlip(@Valid ReqCheckPayinSlipModel reqCheckPayinSlipModel, BindingResult bindingResult) {
		RestManager restManager = RestManager.getInstance();
		restManager.addBindingResult(bindingResult);
		restManager.throwsException();
		
		String username = authenticationFacade.getAuthentication().getName();
		EpayLogModel log = new EpayLogModel(LogLevel.INFO, LogType.INTERNAL_SERVICE, ApiCode.CHECK_PAYIN_SLIP,
				username, "");
		
		log.setDescription("Agent : " + reqCheckPayinSlipModel.getAgentId() + ","
				+ "Total Amount : " + reqCheckPayinSlipModel.getTotalAmount() + ","
				+ "Control Code : " + reqCheckPayinSlipModel.getControlCode());
		
		ResponseModel responseModel = new ResponseModel(null, null);
		ResCheckPayinSlipModel response = new ResCheckPayinSlipModel();
		Optional<EpayTaxPaymentOutbound> epayTaxPaymentOutboundOpt = epayTaxPaymentOutboundService.findByCtrlCode(reqCheckPayinSlipModel.getControlCode());
		
		if (epayTaxPaymentOutboundOpt.isPresent()) {
			EpayTaxPaymentOutbound epayTaxPaymentOutbound = epayTaxPaymentOutboundOpt.get();
			EpayTaxPaymentInfo epayTaxPaymentInfo = epayTaxPaymentOutbound.getEpayTaxPaymentInfo();
			
			if (!epayTaxPaymentInfo.getAgentId().equals(reqCheckPayinSlipModel.getAgentId()) ||
				(epayTaxPaymentInfo.getTotalAmount().compareTo(reqCheckPayinSlipModel.getTotalAmount())!=0)) {
				//restManager.addGlobalErrorbyProperty("app.man-resp.data_not_found");
				
				responseModel.setResponseCode("E07000");
				responseModel.setResponseDec("สำเร็จ");
				response.setCheckPayinStatus("ไม่ถูกต้อง");
				response.setAgentId(reqCheckPayinSlipModel.getAgentId());
				response.setControlCode(reqCheckPayinSlipModel.getControlCode());
				response.setTotalAmount(reqCheckPayinSlipModel.getTotalAmount());
				
			}
			else {
				
				response.setCheckPayinStatus("ถูกต้อง");
				
				if (epayTaxPaymentInfo.getMasterStatus().getStatus().equals("A") &&
					epayTaxPaymentOutbound.getMasterStatus().getStatus().equals("A")) {
					if (epayTaxPaymentInfo.getEpayTaxPaymentInbound() == null) {
						responseModel.setResponseCode("I07000");
						response.setPayinSlipStatus("รอชำระ");
						log.setResponseMessage("รอชำระ");
					}
					else {
						responseModel.setResponseCode("I07000");
						response.setPayinSlipStatus("ชำระแล้ว");
						log.setResponseMessage("ชำระแล้ว");
					}
				}
				else if (!epayTaxPaymentInfo.getMasterStatus().getStatus().equals("A") ||
						!epayTaxPaymentOutbound.getMasterStatus().getStatus().equals("A")) {
					responseModel.setResponseCode("I07000");
					response.setPayinSlipStatus("ยกเลิก");
					log.setResponseMessage("ยกเลิก");
				}
				else {
					responseModel.setResponseCode("E07000");
					responseModel.setResponseDec("ไม่สำเร็จ");
					log.setResponseMessage("ไม่สำเร็จ");
				}
			
			
				response.setAgentId(epayTaxPaymentInfo.getAgentId());
				response.setControlCode(epayTaxPaymentOutbound.getCtlCode());
				response.setExpireDate(epayTaxPaymentInfo.getExpDate());
				response.setTotalAmount(epayTaxPaymentInfo.getTotalAmount());
			}
			
		}
		else {
			responseModel.setResponseCode("E07000");
			responseModel.setResponseDec("ไม่สำเร็จ");
			response.setCheckPayinStatus("ไม่ถูกต้อง");
			log.setResponseMessage("ไม่ถูกต้อง");
			response.setAgentId(reqCheckPayinSlipModel.getAgentId());
			response.setControlCode(reqCheckPayinSlipModel.getControlCode());
			response.setTotalAmount(reqCheckPayinSlipModel.getTotalAmount());
		}
		
		epayLogService.insertLog(restManager, log);
		restManager.throwsException();
		
		restManager.throwsException();
		
		responseModel.setData(response);
		return restManager.addSuccess(responseModel);
	}
}
