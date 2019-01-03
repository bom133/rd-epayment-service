package th.go.rd.rdepaymentservice.controller;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import th.go.rd.rdepaymentservice.component.AuthenticationFacade;
import th.go.rd.rdepaymentservice.constant.ApiCode;
import th.go.rd.rdepaymentservice.constant.LogLevel;
import th.go.rd.rdepaymentservice.constant.LogType;
import th.go.rd.rdepaymentservice.dto.MasterPaymentLineDto;
import th.go.rd.rdepaymentservice.dto.MasterPaymentLineSearchDto;
import th.go.rd.rdepaymentservice.entity.MasterPaymentLine;
import th.go.rd.rdepaymentservice.entity.MasterStatus;
import th.go.rd.rdepaymentservice.exception.NotFoundException;
import th.go.rd.rdepaymentservice.manager.RestManager;
import th.go.rd.rdepaymentservice.model.DataTable;
import th.go.rd.rdepaymentservice.model.EpayLogModel;
import th.go.rd.rdepaymentservice.model.PagingModel;
import th.go.rd.rdepaymentservice.repository.MasterPaymentLineRepository;
import th.go.rd.rdepaymentservice.repository.MasterStatusRepository;
import th.go.rd.rdepaymentservice.request.ReqSubmitMasterPaymentLineModel;
import th.go.rd.rdepaymentservice.response.ResPaymentLineModel;
import th.go.rd.rdepaymentservice.service.EpayLogService;
import th.go.rd.rdepaymentservice.service.MasterPaymentLineService;
import th.go.rd.rdepaymentservice.util.StringHelper;

@RestController
@RequestMapping("/epay/master-payment-line")
public class MasterPaymentLineController {

	private static final Logger logger = LoggerFactory.getLogger(MasterPaymentLineController.class);

	@Autowired
	private MasterPaymentLineRepository masterPaymentLineRepository;

	@Autowired
	private MasterStatusRepository masterStatusRepository;

	@Autowired
	private AuthenticationFacade authenticationFacade;

	@Autowired
	private MasterPaymentLineService masterPaymentLineService;

	@Autowired
	private EpayLogService epayLogService;
	
	@ApiOperation(value = "MasterPaymentLine List", notes = "สำหรับดึงข้อมูล ช่องทางชำระภาษี")
	@GetMapping(value = "/list", headers = { "Authorization", "Accept-Language" })
	public Object SearchPaymentLineByCriteria(HttpServletRequest request) {
		logger.info("MasterPaymentLine List");
		RestManager restManager = RestManager.getInstance();
		List<MasterPaymentLineSearchDto> masterPaymentLineSearchDto = masterPaymentLineService.SearchPaymentLineList(request.getHeader("Accept-Language").toUpperCase(), restManager);
		restManager.throwsException();
		return restManager.addSuccess(masterPaymentLineSearchDto);
	}

	@ApiOperation(value = "MasterPaymentLine Detail", notes = "รายละเอียดข้อมูลหน่วยรับชำระ")
	@GetMapping(value = "{id}", headers = { "Authorization", "Accept-Language" })
	public Object getMasterReceiverUnit(@PathVariable("id") long id) {
		logger.info("MasterPaymentLine Detail [{0}]", id);
		RestManager restManager = RestManager.getInstance();
		Optional<MasterPaymentLine> masterPaymentLineOpt = masterPaymentLineService.findById(id);
		MasterPaymentLineDto masterPaymentLineDto = null;
		if (masterPaymentLineOpt.isPresent()) {
			masterPaymentLineDto = masterPaymentLineService.convertToDto(masterPaymentLineOpt.get());
		} else {
			Object[] obj = { "MasterPaymentLine" };
			restManager.addGlobalErrorbyProperty("app.map-resp.id_not_found", obj);
		}
		restManager.throwsException();
		return restManager.addSuccess(masterPaymentLineDto);
	}

	@ApiOperation(value = "Create payment-line", notes = "สำหรับจัดการข้อมูลช่องทางชำระเงิน")
	@PostMapping(value = "create", headers = { "Authorization", "Accept-Language" })
	public Object create(@Valid @RequestBody ReqSubmitMasterPaymentLineModel reqPaymentLine,
			HttpServletRequest request) {
		// TODO: REWORK NEED !!
		String detail = String.format("Create Payment Line [%s]", reqPaymentLine.getPayLineCode());
		logger.info(detail);
		RestManager restManager = RestManager.getInstance();
		String username = authenticationFacade.getAuthentication().getName();
		EpayLogModel log = new EpayLogModel(LogLevel.INFO, LogType.INTERNAL_SERVICE, ApiCode.MASTER_PAYMENT_LINE_CREATE,
				username, detail);
		StringHelper stringHelper = new StringHelper();
		Date today = new Date();
		ResPaymentLineModel data = new ResPaymentLineModel();
		if (stringHelper.IsEmpty(reqPaymentLine.getPayLineCode())
				|| stringHelper.IsEmpty(reqPaymentLine.getPayLineName())
				|| stringHelper.IsEmpty(reqPaymentLine.getPayLineStatus())) {
			restManager.addGlobalErrorbyProperty("app.val-resp.notblank");
			epayLogService.insertLog(restManager, log);
			restManager.throwsException();
		} else {
			Optional<MasterPaymentLine> optionalMasterPaymentLine = masterPaymentLineRepository
					.findByPayLineCode(reqPaymentLine.getPayLineCode());
			if (!optionalMasterPaymentLine.isPresent()) // Save
			{
				Optional<MasterStatus> optionalMasterStatus = masterStatusRepository
						.findById(reqPaymentLine.getPayLineStatus());
				if (!optionalMasterStatus.isPresent()) {
					Object[] obj = { "MasterStatus" };
					restManager.addGlobalErrorbyProperty("app.map-resp.id_not_found", obj);
					epayLogService.insertLog(restManager, log);
					restManager.throwsException();
				} else {
					MasterStatus masterStatus = optionalMasterStatus.get();
					MasterPaymentLine masterPaymentLine = new MasterPaymentLine();
					masterPaymentLine.setPayLineCode(reqPaymentLine.getPayLineCode());
					masterPaymentLine.setPayLineNameEn(reqPaymentLine.getPayLineName());
					masterPaymentLine.setPayLineNameTh(reqPaymentLine.getPayLineName());
					masterPaymentLine.setCreateBy(username);
					masterPaymentLine.setCreateDate(today);
					masterPaymentLine.setUpdateBy(username);
					masterPaymentLine.setUpdateDate(today);
					masterPaymentLine.setMasterStatus(masterStatus);
					masterPaymentLineRepository.save(masterPaymentLine);
					data.setMasterPaymentLine(masterPaymentLine);
				}
			} else // No Save
			{
				restManager.addGlobalErrorbyProperty("app.man-resp.parameter_exist");
				epayLogService.insertLog(restManager, log);
				restManager.throwsException();
			}
		}
		return restManager.addSuccess(data);
	}

	@ApiOperation(value = "Search payment-line", notes = "สำหรับดึงข้อมูล ช่องทางชำระภาษี")
	@GetMapping(value = "/search", headers = { "Authorization", "Accept-Language" })
	public Object SearchPaymentLineByCriteria(@ModelAttribute("paging") PagingModel page, String payLineCode, String status, HttpServletRequest request)
			throws NotFoundException {
		String detail = String.format("Search Payment Line [%s,%s]", payLineCode, status);
		logger.info(detail);
		RestManager restManager = RestManager.getInstance();
		String username = authenticationFacade.getAuthentication().getName();
		EpayLogModel log = new EpayLogModel(LogLevel.INFO, LogType.INTERNAL_SERVICE, ApiCode.MASTER_PAYMENT_LINE_SEARCH,
				username, detail);

		DataTable<MasterPaymentLineSearchDto> masterPaymentLineSearchDto = masterPaymentLineService.SearchPaymentLineByCriteria(page, request.getHeader("Accept-Language").toUpperCase(), payLineCode, status); 
		return restManager.addSuccess(masterPaymentLineSearchDto);
	}

	@ApiOperation(value = "Update payment-line", notes = "สำหรับค้นหาข้อมูลช่องทางชำระเงิน")
	@PutMapping(value = "update", headers = { "Authorization", "Accept-Language" })
	public Object update(@Valid @RequestBody ReqSubmitMasterPaymentLineModel reqPaymentLine, HttpServletRequest request)
			throws NotFoundException {
		// TODO: REWORK NEED !!
		String detail = String.format("Update MasterPaymentLine [%s]", reqPaymentLine.getPayLineCode());
		logger.info(detail);
		RestManager restManager = RestManager.getInstance();
		String username = authenticationFacade.getAuthentication().getName();
		EpayLogModel log = new EpayLogModel(LogLevel.INFO, LogType.INTERNAL_SERVICE, ApiCode.MASTER_PAYMENT_LINE_UPDATE,
				username, detail);
		ResPaymentLineModel data = new ResPaymentLineModel();
		Date today = new Date();
		Optional<MasterPaymentLine> optionalMasterPaymentLine = masterPaymentLineRepository
				.findByPayLineCode(reqPaymentLine.getPayLineCode());
		if (!optionalMasterPaymentLine.isPresent()) {
			Object[] obj = { "MasterPaymentLine" };
			restManager.addGlobalErrorbyProperty("app.map-resp.id_not_found", obj);
			epayLogService.insertLog(restManager, log);
			restManager.throwsException();
		} else {
			Optional<MasterStatus> optionalMasterStatus = masterStatusRepository
					.findById(reqPaymentLine.getPayLineStatus());
			if (!optionalMasterStatus.isPresent()) {
				Object[] obj = { "MasterStatus" };
			restManager.addGlobalErrorbyProperty("app.map-resp.id_not_found", obj);
				restManager.throwsException();
			} else {
				MasterStatus masterStatus = optionalMasterStatus.get();
				MasterPaymentLine masterPaymentLine = optionalMasterPaymentLine.get();
				masterPaymentLine.setPayLineCode(reqPaymentLine.getPayLineCode());
				masterPaymentLine.setPayLineNameEn(reqPaymentLine.getPayLineName());
				masterPaymentLine.setPayLineNameTh(reqPaymentLine.getPayLineName());
				masterPaymentLine.setUpdateBy(username);
				masterPaymentLine.setUpdateDate(today);
				masterPaymentLine.setMasterStatus(masterStatus);
				masterPaymentLineRepository.save(masterPaymentLine);
				data.setMasterPaymentLine(masterPaymentLine);
			}
		}
		return restManager.addSuccess(data);
	}

	@ApiOperation(value = "Delete payment-line", notes = "สำหรับค้นหาข้อมูลช่องทางชำระเงิน")
	@DeleteMapping(value = "delete/{id}", headers = { "Authorization", "Accept-Language" })
	public Object delete(@PathVariable(value = "id") long id, HttpServletRequest request) throws NotFoundException {
		String detail = String.format("Delete PaymentLine [%d]", id);
		logger.info(detail);
		RestManager restManager = RestManager.getInstance();
		String username = authenticationFacade.getAuthentication().getName();
		EpayLogModel log = new EpayLogModel(LogLevel.INFO, LogType.INTERNAL_SERVICE, ApiCode.MASTER_PAYMENT_LINE_DELETE,
				username, detail);
		try {
			masterPaymentLineService.delete(id);
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			restManager.addGlobalErrorbyProperty("app.sys-resp.database_error");
		} finally {
			epayLogService.insertLog(restManager, log);
		}
		restManager.throwsException();
		return restManager.addSuccess(null);
	}
}
