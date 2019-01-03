package th.go.rd.rdepaymentservice.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import com.ibm.icu.text.DecimalFormat;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import th.go.rd.rdepaymentservice.component.AuthenticationFacade;
import th.go.rd.rdepaymentservice.constant.ApiCode;
import th.go.rd.rdepaymentservice.constant.LogLevel;
import th.go.rd.rdepaymentservice.constant.LogType;
import th.go.rd.rdepaymentservice.entity.EpayReceiverPaymentLine;
import th.go.rd.rdepaymentservice.entity.EpayReceiverPaymentLineApiUrl;
import th.go.rd.rdepaymentservice.entity.EpayTaxPaymentInbound;
import th.go.rd.rdepaymentservice.entity.EpayTaxPaymentInboundRefOutbound;
import th.go.rd.rdepaymentservice.entity.EpayTaxPaymentInfo;
import th.go.rd.rdepaymentservice.entity.EpayTaxPaymentOutbound;
import th.go.rd.rdepaymentservice.entity.MasterPaymentLine;
import th.go.rd.rdepaymentservice.manager.RestManager;
import th.go.rd.rdepaymentservice.model.EpayLogModel;
import th.go.rd.rdepaymentservice.model.PaymentResultDataModel;
import th.go.rd.rdepaymentservice.model.ReceiverUnitModel;
import th.go.rd.rdepaymentservice.model.TaxInfoModel;
import th.go.rd.rdepaymentservice.repository.EpayReceiverPaymentLineApiUrlRepository;
import th.go.rd.rdepaymentservice.repository.EpayTaxPaymentInboundRefOutboundRepository;
import th.go.rd.rdepaymentservice.repository.EpayTaxPaymentInboundRepository;
import th.go.rd.rdepaymentservice.repository.EpayTaxPaymentOutboundRepository;
import th.go.rd.rdepaymentservice.repository.MasterPaymentLineRepository;
import th.go.rd.rdepaymentservice.response.ResDisplayPaymentLineChannelDataModel;
import th.go.rd.rdepaymentservice.response.ResPaymentResultModel;
import th.go.rd.rdepaymentservice.service.EpayLogService;
import th.go.rd.rdepaymentservice.util.DateHelper;

@RestController
@RequestMapping("/epay/taxpayer")
public class TaxPayerController {
	private static Logger logger = LoggerFactory.getLogger(TaxPayerController.class);

	@Autowired
	private MasterPaymentLineRepository masterPayRepository;

	@Autowired
	private EpayTaxPaymentOutboundRepository epayTaxPaymentOutboundRepository;

	@Autowired
	private EpayTaxPaymentInboundRepository epayTaxPaymentInboundRepository;

	@Autowired
	private EpayTaxPaymentInboundRefOutboundRepository epayTaxPaymentInboundRefOutboundRepository;

	@Autowired
	private EpayReceiverPaymentLineApiUrlRepository epayReceiverPaymentLineApiUrlRepository;

	@Autowired
	private AuthenticationFacade authenticationFacade;

	@Autowired
	private EpayLogService epayLogService;

	@Value("${payment-std.epay-service}")
	private String baseUrl;

	@Value("${payment-std.uri}")
	private String paymentStrUri;

	@ApiOperation(value = "DisplayPayment", notes = "สำหรับแสดงข้อมูลช่องทางชำระภาษี และข้อมูลการชำระภาษี ที่หน้าจอ เลือกช่องทางการชำระเงิน")
	@GetMapping(value = "display-payment-line-channel/{payLineCode}/{uuid}", produces = "application/json", headers = {
			"Authorization", "Accept-Language" })
	public Object displaypaymentlinechannel(@PathVariable String uuid, @PathVariable String payLineCode,
			HttpServletRequest request) {
		String detail = String.format("Display Payment Line Channel [%s,%s]", uuid, payLineCode);
		RestManager restManager = RestManager.getInstance();
		String username = authenticationFacade.getAuthentication().getName();
		EpayLogModel log = new EpayLogModel(LogLevel.INFO, LogType.INTERNAL_SERVICE, ApiCode.DISPLAY_PAYMENT_RESULT,
				 username, detail);

		MasterPaymentLine masPaymentLine = new MasterPaymentLine();
		Optional<MasterPaymentLine> optionalMasPaymentLine = masterPayRepository.findByPayLineCode(payLineCode);
		if (optionalMasPaymentLine.isPresent()) {
			masPaymentLine = optionalMasPaymentLine.get();
		}
		else {
			// error not found payment line code
			restManager.addFieldErrorbyProperty("payLineCode", "Error not found payLineCode", payLineCode);
			epayLogService.insertLog(restManager, log);
			restManager.throwsException();
		}

		Date today = new Date();
		List<EpayReceiverPaymentLine> epayReceiverPaymentLines = masPaymentLine.getEpayReceiverPaymentLines();
		List<ReceiverUnitModel> listRec = new ArrayList<ReceiverUnitModel>();
		DateHelper dateHelper = new DateHelper();
		for (EpayReceiverPaymentLine e : epayReceiverPaymentLines) {
			if (e.getMasterStatus().getStatus().equals("A")) {
				if (e.getStartDate() != null && e.getEndDate() != null) {
					if (dateHelper.isBetweenDate(today, e.getStartDate(), e.getEndDate())) {
						ModelMapper modelMapper = new ModelMapper();
						ReceiverUnitModel receiverUnitModel = modelMapper.map(e.getMasterReceiverUnit(),
								ReceiverUnitModel.class);
						listRec.add(receiverUnitModel);
						receiverUnitModel.setPayLineCode(payLineCode);
						receiverUnitModel.setRecPaymentLineId(e.getEpayReceiverPaymentLineId());
						String url = null;
						Optional<EpayReceiverPaymentLineApiUrl> optionalEpayReceiverPaymentLineApiUrl = epayReceiverPaymentLineApiUrlRepository
								.findByEpayReceiverPaymentLine(e);
						if (optionalEpayReceiverPaymentLineApiUrl.isPresent()) {
							EpayReceiverPaymentLineApiUrl urls = optionalEpayReceiverPaymentLineApiUrl.get();

							if (urls.getApiUrl() != null && urls.getMasterStatus().getStatus().equals("A")) {
								url = this.baseUrl + urls.getApiUrl();
							} else {
								url = this.baseUrl + this.paymentStrUri + uuid + "?recPaymentLineCode="
										+ e.getEpayReceiverPaymentLineId();
							}
						} else {
							url = this.baseUrl + this.paymentStrUri + uuid + "?recPaymentLineCode="
									+ e.getEpayReceiverPaymentLineId();
						}

						receiverUnitModel.setUrl(url);
					} else {
						System.err.println("Date is not between " + e.getStartDate() + " to " + e.getEndDate());
					}
				} else {
					ModelMapper modelMapper = new ModelMapper();
					ReceiverUnitModel receiverUnitModel = modelMapper.map(e.getMasterReceiverUnit(),
							ReceiverUnitModel.class);
					listRec.add(receiverUnitModel);
					receiverUnitModel.setPayLineCode(payLineCode);
					receiverUnitModel.setRecPaymentLineId(e.getEpayReceiverPaymentLineId());
					String url = null;
					Optional<EpayReceiverPaymentLineApiUrl> optionalEpayReceiverPaymentLineApiUrl = epayReceiverPaymentLineApiUrlRepository
							.findByEpayReceiverPaymentLine(e);
					if (optionalEpayReceiverPaymentLineApiUrl.isPresent()) {
						EpayReceiverPaymentLineApiUrl urls = optionalEpayReceiverPaymentLineApiUrl.get();

						if (urls.getApiUrl() != null && urls.getMasterStatus().getStatus().equals("A")) {
							url = this.baseUrl + urls.getApiUrl();
						} else {
							url = this.baseUrl + this.paymentStrUri + uuid + "?recPaymentLineCode="
									+ e.getEpayReceiverPaymentLineId();
						}
					} else {
						url = this.baseUrl + this.paymentStrUri + uuid + "?recPaymentLineCode="
								+ e.getEpayReceiverPaymentLineId();
					}

					receiverUnitModel.setUrl(url);
				}
			}
		}

		Optional<EpayTaxPaymentOutbound> optionalPaymentOutbound = epayTaxPaymentOutboundRepository.findByUuid(uuid);
		EpayTaxPaymentOutbound epayTaxPaymentOutbound = new EpayTaxPaymentOutbound();
		if (optionalPaymentOutbound.isPresent()) {
			epayTaxPaymentOutbound = optionalPaymentOutbound.get();
		}
		else {
			restManager.addFieldErrorbyProperty("uuid", "app.man-resp.uuid_not_found", uuid);
			epayLogService.insertLog(restManager, log);
			restManager.throwsException();
		}
		EpayTaxPaymentInfo epayTaxPaymentInfo = epayTaxPaymentOutbound.getEpayTaxPaymentInfo();

		if (!epayTaxPaymentInfo.getMasterStatus().getStatus().equals("A")) {
			restManager.addGlobalErrorbyProperty("app.man-resp.cancel");
			epayLogService.insertLog(restManager, log);
			restManager.throwsException();
		}

		if (!epayTaxPaymentOutbound.getMasterStatus().getStatus().equals("A")) {
			restManager.addGlobalErrorbyProperty("app.man-resp.payinslip_cancel");
			epayLogService.insertLog(restManager, log);
			restManager.throwsException();
		}

		String getFormCode = epayTaxPaymentInfo.getFormCodeDisplay();
		String getTaxMonth = epayTaxPaymentInfo.getTaxMonthDisplay();
		String getAgentId = epayTaxPaymentInfo.getAgentId();
		String getIsRound = String.valueOf(epayTaxPaymentInfo.getIsRound());

		DecimalFormat formatNum = new DecimalFormat("#,##0.00");
		String getTotalAmount = formatNum.format(epayTaxPaymentInfo.getTotalAmount());

		TaxInfoModel taxInfo = new TaxInfoModel();
		ModelMapper modelMapper = new ModelMapper();
		taxInfo = modelMapper.map(epayTaxPaymentInfo, TaxInfoModel.class);
		if (getIsRound.equals("Y")) {
			taxInfo.setIsRound("Y");
			taxInfo.setIsRoundMessage("ท่านได้รับยกเว้นไม่ต้องชำระเศษของบาท");
			taxInfo.setRedirectPageMessage("หากประสงค์ชำระเต็มจำนวน กด");
		}else if (getIsRound.equals("N")) {
			taxInfo.setIsRound("N");
			taxInfo.setRedirectPageMessage("หากไม่ประสงค์ยกเว้นเศษสตางค์ กด ");
		} else {
			taxInfo.setIsRound("R");
		}
		taxInfo.setTotalAmount(getTotalAmount);
		taxInfo.setFormCode(getFormCode);
		taxInfo.setTaxMonth(getTaxMonth);
		taxInfo.setAgentId(getAgentId);

		ResDisplayPaymentLineChannelDataModel data = new ResDisplayPaymentLineChannelDataModel(payLineCode, listRec,
				taxInfo);
		epayLogService.insertLog(restManager, log);
		restManager.throwsException();
		return restManager.addSuccess(data);
	}

	@ApiOperation(value = "Display Payment Result", notes = "สำหรับแสดงผลการชำระภาษี จากการชำระภาษีผ่านช่องทางที่เป็น E-Payment Standard")
	@GetMapping(value = "display-payment-result/{paymentId}/{uuid}", headers = { "Authorization", "Accept-Language" })
	public Object displayPaymentResult(@PathVariable String paymentId, @PathVariable String uuid,
			HttpServletRequest request) {
		String detail = String.format("Display Payment Result [%s]", uuid);
		logger.info(detail);
		RestManager restManager = RestManager.getInstance();
		String username = authenticationFacade.getAuthentication().getName();
		EpayLogModel log = new EpayLogModel(LogLevel.INFO, LogType.INTERNAL_SERVICE, ApiCode.DISPLAY_PAYMENT_RESULT,
				username, detail);

		ResPaymentResultModel data = new ResPaymentResultModel();

		if (uuid.length() > 36) {
			logger.info("uuid length > 36");
			Object[] obj = { "uuid" };
			restManager.addGlobalErrorbyProperty("app.man-resp.invalid_field", obj);
			epayLogService.insertLog(restManager, log);
			restManager.throwsException();
		}

		Optional<EpayTaxPaymentInbound> inboundOpt = epayTaxPaymentInboundRepository.findByUuid(uuid);
		EpayTaxPaymentInbound inbound = new EpayTaxPaymentInbound();
		if (inboundOpt.isPresent()) {
			inbound = inboundOpt.get();
		}
		else {
			logger.info("EpayTaxPaymentInbound not found by uuid [{}]", uuid);
			Object[] obj = { "uuid" };
			restManager.addGlobalErrorbyProperty("app.man-resp.invalid_field", obj);
			epayLogService.insertLog(restManager, log);
			restManager.throwsException();
		}
		Optional<EpayTaxPaymentInboundRefOutbound> EpayTaxPaymentInboundRefOutboundOpt = epayTaxPaymentInboundRefOutboundRepository
				.findByEpayTaxPaymentInbound(inbound);

		if (!EpayTaxPaymentInboundRefOutboundOpt.isPresent()) {
			restManager.addGlobalErrorbyProperty("app.man-resp.data_not_found");
			epayLogService.insertLog(restManager, log);
			restManager.throwsException();
		}

		EpayTaxPaymentOutbound outbound = EpayTaxPaymentInboundRefOutboundOpt.orElse(new EpayTaxPaymentInboundRefOutbound()).getEpayTaxPaymentOutbound();
		logger.info("Outbound ID : " + String.valueOf(outbound.getEpayTaxPaymentOutboundId()));

		EpayTaxPaymentInfo info = outbound.getEpayTaxPaymentInfo();
		logger.info("RefNO : " + info.getRefNo());

		if (!paymentId.equals(info.getRefNo())) {
			logger.info("Refno not equal to paymentId [{}]", paymentId);
			Object[] obj = { "paymentId" };
			restManager.addFieldErrorbyProperty("paymentId", "app.man-resp.invalid_field", obj, paymentId);
			epayLogService.insertLog(restManager, log);
			restManager.throwsException();
		}

		PaymentResultDataModel paymentResultDataModel = new PaymentResultDataModel();
		paymentResultDataModel.setTransmitDate(inbound.getTransmitDate().toString());
		paymentResultDataModel
				.setRecShortName(inbound.getEpayReceiverPaymentLine().getMasterReceiverUnit().getRecShortNameEn());
		paymentResultDataModel
				.setPaymentLineCode(inbound.getEpayReceiverPaymentLine().getMasterPaymentLine().getPayLineCode());

		paymentResultDataModel.setPaymentStatus(inbound.getMasterPaymentStatus().getMasterPaymentStatusId());
		paymentResultDataModel.setPaymentStatusMessage(inbound.getMasterPaymentStatus().getDescriptionTh());
		TaxInfoModel taxInfo = new TaxInfoModel();
		taxInfo.setAgentId(info.getAgentId());
		taxInfo.setFormCode(info.getEpayTaxPaymentInfoDetails().get(0).getFormCode());
		taxInfo.setTaxMonth(info.getTaxMonthDisplay());
		taxInfo.setTotalAmount(info.getTotalAmount().toString());
		taxInfo.setRefNo(info.getRefNo());
		taxInfo.setExpDate(info.getExpDate().toString());
		taxInfo.setIsRound(String.valueOf(info.getIsRound()));

		data.setPaymentResult(paymentResultDataModel);
		data.setTaxInfo(taxInfo);

		epayLogService.insertLog(restManager, log);
		restManager.throwsException();
		return restManager.addSuccess(data);
	}
}
