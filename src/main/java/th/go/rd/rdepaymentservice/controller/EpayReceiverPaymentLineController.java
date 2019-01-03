package th.go.rd.rdepaymentservice.controller;

import java.text.ParseException;
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
import th.go.rd.rdepaymentservice.dto.EpayRecPayLineDto;
import th.go.rd.rdepaymentservice.dto.EpayReceiverPaymentLineDto;
import th.go.rd.rdepaymentservice.entity.EpayReceiverPaymentLine;
import th.go.rd.rdepaymentservice.entity.MasterPaymentLine;
import th.go.rd.rdepaymentservice.entity.MasterReceiverUnit;
import th.go.rd.rdepaymentservice.entity.MasterStatus;
import th.go.rd.rdepaymentservice.manager.RestManager;
import th.go.rd.rdepaymentservice.model.DataTable;
import th.go.rd.rdepaymentservice.model.EpayLogModel;
import th.go.rd.rdepaymentservice.model.PagingModel;
import th.go.rd.rdepaymentservice.service.EpayLogService;
import th.go.rd.rdepaymentservice.service.EpayReceiverPaymentLineService;
import th.go.rd.rdepaymentservice.service.MasterPaymentLineService;
import th.go.rd.rdepaymentservice.service.MasterReceiverUnitService;
import th.go.rd.rdepaymentservice.service.MasterStatusService;

@RestController
@RequestMapping("/epay/receiver-unit-payment-line")
public class EpayReceiverPaymentLineController {

    private static final Logger logger = LoggerFactory.getLogger(EpayReceiverPaymentLineController.class);

    @Autowired
    private EpayReceiverPaymentLineService epayReceiverPaymentLineService;

    @Autowired
    private MasterPaymentLineService masterPaymentLineService;

    @Autowired
    private MasterReceiverUnitService masterReceiverUnitService;

    @Autowired
    private MasterStatusService masterStatusService;

    @Autowired
    private AuthenticationFacade authenticationFacade;

    @Autowired
    private EpayLogService epayLogService;

    
 // รอสเปค response ก่อน
    @ApiOperation(value = "ReceiverPaymentLine List", notes = "รายการข้อมูลช่องทางชำระเงิน")
    @GetMapping(value = "list", headers = { "Authorization", "Accept-Language" })
    public Object list() {
        logger.info("ReceiverPaymentLine List");
        RestManager restManager = RestManager.getInstance();
        List<EpayReceiverPaymentLine> epayReceiverPaymentLines = epayReceiverPaymentLineService.findAll();
        List<EpayReceiverPaymentLineDto> epayReceiverPaymentLineDtos = epayReceiverPaymentLineService
                .convertToDto(epayReceiverPaymentLines);
        restManager.throwsException();
        return restManager.addSuccess(epayReceiverPaymentLineDtos);
    }

    @ApiOperation(value = "ReceiverPaymentLine Detail", notes = "รายการข้อมูลช่องทางชำระเงิน")
    @GetMapping(value = "{id}")
    public Object get(@PathVariable(value = "id") long id) {
        logger.info("ReceiverPaymentLine Detail [{0}]", id);
        RestManager restManager = RestManager.getInstance();
        Optional<EpayReceiverPaymentLine> epayReceiverPaymentLineOpt = epayReceiverPaymentLineService.findById(id);
        EpayReceiverPaymentLine epayReceiverPaymentLine = null;
        if (epayReceiverPaymentLineOpt.isPresent()) {
        	epayReceiverPaymentLine = epayReceiverPaymentLineOpt.get();
        }
        EpayReceiverPaymentLineDto epayReceiverPaymentLineDto = epayReceiverPaymentLineService
                .convertToDto(epayReceiverPaymentLine);
        restManager.throwsException();
        return restManager.addSuccess(epayReceiverPaymentLineDto);
    }

    @ApiOperation(value = "Create ReceiverPaymentLine", notes = "สร้างข้อมูลช่องทางชำระเงิน")
    @PostMapping(value = "create", headers = { "Authorization", "Accept-Language" })
    public Object create(@Valid @RequestBody EpayReceiverPaymentLineDto epayReceiverPaymentLineDto, HttpServletRequest request) {
        String detail = String.format("Create Authorization [%s]",
                epayReceiverPaymentLineDto.getMasterPaymentLineDto().getPayLineCode());
        logger.info(detail);
        RestManager restManager = RestManager.getInstance();
        String username = authenticationFacade.getAuthentication().getName();
        EpayLogModel log = new EpayLogModel(LogLevel.INFO, LogType.INTERNAL_SERVICE, ApiCode.EPAY_RECEIVER_PAYMENT_LINE_CREATE,
                 username, detail);
        Date now = new Date();

        EpayReceiverPaymentLine receiverPaymentLine = new EpayReceiverPaymentLine();
        receiverPaymentLine.setRecRedirectUrl(epayReceiverPaymentLineDto.getRecRedirectUrl());
        receiverPaymentLine.setRecDirectUrl(epayReceiverPaymentLineDto.getRecDirectUrl());
        receiverPaymentLine.setRdDirectUrl(epayReceiverPaymentLineDto.getRdDirectUrl());
        receiverPaymentLine.setRdRedirectUrl(epayReceiverPaymentLineDto.getRdRedirectUrl());

        receiverPaymentLine.setStartDate(epayReceiverPaymentLineDto.getStartDate());
        receiverPaymentLine.setEndDate(epayReceiverPaymentLineDto.getEndDate());
        receiverPaymentLine.setRdCertCode(epayReceiverPaymentLineDto.getRdCertCode());
        receiverPaymentLine.setRecCertCode(epayReceiverPaymentLineDto.getRecCertCode());
        receiverPaymentLine.setTerminalId(epayReceiverPaymentLineDto.getTerminalId());
        receiverPaymentLine.setMerchantId(epayReceiverPaymentLineDto.getMerchantId());
        receiverPaymentLine.setDescription(epayReceiverPaymentLineDto.getDescription());

        receiverPaymentLine.setCreateBy(username);
        receiverPaymentLine.setCreateDate(now);
        receiverPaymentLine.setUpdateBy(username);
        receiverPaymentLine.setUpdateDate(now);

        Optional<MasterPaymentLine> paymentLine = masterPaymentLineService
                .findById(epayReceiverPaymentLineDto.getMasterPaymentLineDto().getMasterPaymentLineId());
        if (paymentLine.isPresent()) {
            receiverPaymentLine.setMasterPaymentLine(paymentLine.get());
        } else {
            Object[] obj = { "Master Payment Line" };
            restManager.addGlobalErrorbyProperty("app.man-resp.id_not_found", obj);
            epayLogService.insertLog(restManager, log);
            restManager.throwsException();
        }
        Optional<MasterReceiverUnit> receiverUnit = masterReceiverUnitService
                .findById(epayReceiverPaymentLineDto.getMasterReceiverUnitDto().getMasterReceiverUnitId());
        if (receiverUnit.isPresent()) {
            receiverPaymentLine.setMasterReceiverUnit(receiverUnit.get());
        } else {
            Object[] obj = { "Master Receiver Unit" };
            restManager.addGlobalErrorbyProperty("app.man-resp.id_not_found", obj);
            epayLogService.insertLog(restManager, log);
            restManager.throwsException();
        }
        // Payment Line and Receiver Unit already exist
        if (paymentLine.isPresent() && receiverUnit.isPresent()) {
            if (epayReceiverPaymentLineService
                    .findByMasterPaymentLineAndMasterReceiverUnit(paymentLine.get(), receiverUnit.get()).size() > 0) {
                restManager.addGlobalErrorbyProperty("app.man-resp.receiver_payment_line_exist");
                epayLogService.insertLog(restManager, log);
                restManager.throwsException();
            }
        }

        Optional<MasterStatus> status = masterStatusService
                .findById(epayReceiverPaymentLineDto.getMasterStatusDto().getStatus());
        if (status.isPresent()) {
            receiverPaymentLine.setMasterStatus(status.get());
        } else {
            Object[] obj = { "Master Status" };
            restManager.addGlobalErrorbyProperty("app.man-resp.id_not_found", obj);
            epayLogService.insertLog(restManager, log);
            restManager.throwsException();
        }

        try {
            epayReceiverPaymentLineService.save(receiverPaymentLine);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            restManager.addGlobalErrorbyProperty("app.sys-resp.database_error");
        } finally {
            epayLogService.insertLog(restManager, log);
        }

        restManager.throwsException();
        return restManager.addSuccess(receiverPaymentLine);
    }

    @ApiOperation(value = "Delete ReceiverPaymentLine", notes = "ลบข้อมูลช่องทางชำระเงิน")
    @DeleteMapping(value = "delete/{id}", headers = { "Authorization", "Accept-Language" })
    public Object delete(@PathVariable(value = "id") long id, HttpServletRequest request) {
        String detail = String.format("Delete ReceiverPaymentLine [%d]", id);
        logger.info(detail);
        RestManager restManager = RestManager.getInstance();
        String username = authenticationFacade.getAuthentication().getName();
        EpayLogModel log = new EpayLogModel(LogLevel.INFO, LogType.INTERNAL_SERVICE, ApiCode.EPAY_RECEIVER_PAYMENT_LINE_DELETE,
                 username, detail);
        try {
            epayReceiverPaymentLineService.delete(id);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            restManager.addGlobalErrorbyProperty("app.sys-resp.database_error");
        } finally {
            epayLogService.insertLog(restManager, log);
        }
        return restManager.addSuccess(null);
    }

    @ApiOperation(value = "Search ReceiverPaymentLine", notes = "ค้นหาข้อมูลช่องทางชำระเงิน")
    @GetMapping(value = "search", headers = { "Authorization", "Accept-Language" })
    public Object search(HttpServletRequest req, @ModelAttribute("paging") PagingModel page, String paymentLineId, String receiverUnitId, String statusId, HttpServletRequest request) {
        String detail = String.format("Search ReceiverPaymentLine [%s,%s,%s]", paymentLineId, receiverUnitId, statusId);
        logger.info(detail);
        RestManager restManager = RestManager.getInstance();
        DataTable<EpayRecPayLineDto> epayRecPayLineDto = epayReceiverPaymentLineService.search(req.getHeader("Accept-Language").toUpperCase(), page, paymentLineId, receiverUnitId, statusId);
        return restManager.addSuccess(epayRecPayLineDto);
    }
    
    @ApiOperation(value = "Update ReceiverPaymentLine", notes = "แก้ไขปรับปรุงข้อมูลช่องทางชำระเงิน")
    @PutMapping(value = "update", headers = { "Authorization", "Accept-Language" })
    public Object update(@Valid @RequestBody EpayReceiverPaymentLineDto epayReceiverPaymentLineDto,HttpServletRequest request) throws ParseException {
        String detail = String.format("Update ReceiverPaymentLine [%d]", epayReceiverPaymentLineDto.getEpayReceiverPaymentLineId());
		logger.info(detail);
		RestManager restManager = RestManager.getInstance();
		String username = authenticationFacade.getAuthentication().getName();
		EpayLogModel log = new EpayLogModel(LogLevel.INFO, LogType.INTERNAL_SERVICE, ApiCode.EPAY_RECEIVER_PAYMENT_LINE_UPDATE,
		username, detail);
        Date now = new Date();

        Optional<EpayReceiverPaymentLine> epayReceiverPaymentLine = epayReceiverPaymentLineService
                .findById(epayReceiverPaymentLineDto.getEpayReceiverPaymentLineId());
        EpayReceiverPaymentLine receiverPaymentLine = null;
        if (epayReceiverPaymentLine.isPresent()) {
            receiverPaymentLine = epayReceiverPaymentLine.get();
        } else {
            Object[] obj = { "EpayReceiverPaymentLine" };
            restManager.addGlobalErrorbyProperty("app.map-resp.id_not_found", obj);
            epayLogService.insertLog(restManager, log);
            restManager.throwsException();
        }

        Optional<MasterPaymentLine> paymentLine = masterPaymentLineService
                .findById(epayReceiverPaymentLineDto.getMasterPaymentLineDto().getMasterPaymentLineId());
        if (paymentLine.isPresent()) {
            receiverPaymentLine.setMasterPaymentLine(paymentLine.get());
        } else {
            Object[] obj = { "Master Payment Line" };
            restManager.addGlobalErrorbyProperty("app.man-resp.id_not_found", obj);
            epayLogService.insertLog(restManager,log);
            restManager.throwsException();
        }
        Optional<MasterReceiverUnit> receiverUnit = masterReceiverUnitService
                .findById(epayReceiverPaymentLineDto.getMasterReceiverUnitDto().getMasterReceiverUnitId());
        if (receiverUnit.isPresent()) {
            receiverPaymentLine.setMasterReceiverUnit(receiverUnit.get());
        } else {
            Object[] obj = { "Master Receiver Unit" };
            restManager.addGlobalErrorbyProperty("app.man-resp.id_not_found", obj);
            epayLogService.insertLog(restManager, log);
            restManager.throwsException();
        }

        Optional<MasterStatus> status = masterStatusService.findById(epayReceiverPaymentLineDto.getMasterStatusDto().getStatus());
        if (status.isPresent()) {
            receiverPaymentLine.setMasterStatus(status.get());
        } else {
            Object[] obj = { "Master Status" };
            restManager.addGlobalErrorbyProperty("app.man-resp.id_not_found", obj);
            epayLogService.insertLog(restManager, log);
            restManager.throwsException();
        }
        try{
	        receiverPaymentLine.setRecRedirectUrl(epayReceiverPaymentLineDto.getRecRedirectUrl());
	        receiverPaymentLine.setRecDirectUrl(epayReceiverPaymentLineDto.getRecDirectUrl());
	        receiverPaymentLine.setRdDirectUrl(epayReceiverPaymentLineDto.getRdDirectUrl());
	        receiverPaymentLine.setRdRedirectUrl(epayReceiverPaymentLineDto.getRdRedirectUrl());
	
	        receiverPaymentLine.setStartDate(epayReceiverPaymentLineDto.getStartDate());
	        receiverPaymentLine.setEndDate(epayReceiverPaymentLineDto.getEndDate());
	        receiverPaymentLine.setRdCertCode(epayReceiverPaymentLineDto.getRdCertCode());
	        receiverPaymentLine.setRecCertCode(epayReceiverPaymentLineDto.getRecCertCode());
	        receiverPaymentLine.setTerminalId(epayReceiverPaymentLineDto.getTerminalId());
	        receiverPaymentLine.setMerchantId(epayReceiverPaymentLineDto.getMerchantId());
	        receiverPaymentLine.setDescription(epayReceiverPaymentLineDto.getDescription());
	
	        receiverPaymentLine.setUpdateBy(username);
	        receiverPaymentLine.setUpdateDate(now);
            epayReceiverPaymentLineService.save(receiverPaymentLine);
        }catch(Exception ex){
            logger.error(ex.getMessage());
            restManager.addGlobalErrorbyProperty("app.sys-resp.database_error");
        }finally{
            epayLogService.insertLog(restManager,log);
        }

        restManager.throwsException();
        return restManager.addSuccess(epayReceiverPaymentLineService.convertToDto(receiverPaymentLine));
    }
}
