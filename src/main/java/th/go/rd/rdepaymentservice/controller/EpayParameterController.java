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
import th.go.rd.rdepaymentservice.dto.EpayParameterDto;
import th.go.rd.rdepaymentservice.dto.EpayParameterSearchDto;
import th.go.rd.rdepaymentservice.entity.EpayParameter;
import th.go.rd.rdepaymentservice.entity.MasterParamType;
import th.go.rd.rdepaymentservice.manager.RestManager;
import th.go.rd.rdepaymentservice.model.DataTable;
import th.go.rd.rdepaymentservice.model.EpayLogModel;
import th.go.rd.rdepaymentservice.model.PagingModel;
import th.go.rd.rdepaymentservice.request.ReqSubmitParameterModel;
import th.go.rd.rdepaymentservice.service.EpayLogService;
import th.go.rd.rdepaymentservice.service.EpayParameterService;
import th.go.rd.rdepaymentservice.service.MasterParamTypeService;

@RestController
@RequestMapping("/epay/parameter")
public class EpayParameterController {

    private static final Logger logger = LoggerFactory.getLogger(EpayParameterController.class);

    @Autowired
    private EpayParameterService epayParameterService;

    @Autowired
    private MasterParamTypeService masterParamTypeService;

    @Autowired
    private AuthenticationFacade authenticationFacade;

    @Autowired
    private EpayLogService epayLogService;

    @ApiOperation(value = "Parameter List", notes = "รายการข้อมูลค่าคงที่สำหรับหน่วยรับชำระ")
    @GetMapping(value = "list", headers = { "Authorization" })
    public Object list() {
        logger.info("Parameter List");
        RestManager restManager = RestManager.getInstance();
        List<EpayParameter> epayParameters = epayParameterService.findAll();
        List<EpayParameterDto> epayParameterDtos = epayParameterService.convertToDto(epayParameters);
        restManager.throwsException();
        return restManager.addSuccess(epayParameterDtos);
    }

    @ApiOperation(value = "Parameter Detail", notes = "รายละเอียดข้อมูลค่าคงที่สำหรับหน่วยรับชำระ")
    @GetMapping(value = "{id}", headers = { "Authorization", "Accept-Language" })
    public Object get(@PathVariable(value = "id") long id) {
        logger.info("Parameter Detail [{0}]", id);
        RestManager restManager = RestManager.getInstance();
        Optional<EpayParameter> epayParameterOptional = epayParameterService.findById(id);
        EpayParameter epayParameter = null;
        if(epayParameterOptional.isPresent()){
            epayParameter = epayParameterOptional.get();
        }else{
            Object[] obj = { "Parameter" };
            restManager.addGlobalErrorbyProperty("app.map-resp.id_not_found", obj);
        }
        restManager.throwsException();
        return restManager.addSuccess(epayParameterService.convertToDto(epayParameter));
    }

    @ApiOperation(value = "Create Parameter", notes = "สร้างข้อมูลค่าคงที่สำหรับหน่วยรับชำระ")
    @PostMapping(value = "create", headers = { "Authorization", "Accept-Language" })
    public Object create(@Valid @RequestBody ReqSubmitParameterModel reqSubmitParameter, HttpServletRequest request) {
        String detail = String.format("Create Paremeter [%s]", reqSubmitParameter.getParamCode());
        logger.info(detail);
        RestManager restManager = RestManager.getInstance();
        String username = authenticationFacade.getAuthentication().getName();
        EpayLogModel log = new EpayLogModel(LogLevel.INFO, LogType.INTERNAL_SERVICE, ApiCode.EPAY_PARAMETER_CREATE,
                 username, detail);
        Date now = new Date();

        EpayParameter param = new EpayParameter();
        if(reqSubmitParameter.getMasterParamTypeDto().getParamType().equals("")) {
        	restManager.addGlobalErrorbyProperty("app.man-resp.invalid_data");
            epayLogService.insertLog(restManager, log);
            restManager.throwsException();
        }
        Optional<MasterParamType> masterParamType = masterParamTypeService
                .findById(reqSubmitParameter.getMasterParamTypeDto().getParamType());

        if (masterParamType.isPresent()) {
            List<EpayParameter> eparam = epayParameterService.findByParamTypeAndParamCode(
                    reqSubmitParameter.getMasterParamTypeDto().getParamType(), reqSubmitParameter.getParamCode());
            if (eparam.size() > 0) {
                // ParamCode and ParamType already exist
                restManager.addGlobalErrorbyProperty("app.man-resp.parameter_exist");
                epayLogService.insertLog(restManager, log);
                restManager.throwsException();
            }
            param.setMasterParamType(masterParamType.get());
        }
        param.setParamCode(reqSubmitParameter.getParamCode());
        param.setParamValue(reqSubmitParameter.getParamValue());
        param.setCreateBy(username);
        param.setCreateDate(now);
        param.setUpdateBy(username);
        param.setUpdateDate(now);

        try {
            epayParameterService.save(param);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            restManager.addGlobalErrorbyProperty("app.sys-resp.database_error");
        } finally {
            epayLogService.insertLog(restManager, log);
        }

        restManager.throwsException();
        return restManager.addSuccess(epayParameterService.convertToDto(param));
    }

    @ApiOperation(value = "Search Paremeter", notes = "สำหรับดึงข้อมูล ค่าคงที่")
    @GetMapping(value = "/search", headers = { "Authorization", "Accept-Language" })
    public Object SearchEpayParameterByCriteria(@ModelAttribute("paging") PagingModel page, HttpServletRequest request, String paramType, String paramCode) {
        String detail = String.format("Search Paremeter [%s,%s]", paramType, paramCode);
        logger.info(detail);
        RestManager restManager = RestManager.getInstance();
        String username = authenticationFacade.getAuthentication().getName();
        EpayLogModel log = new EpayLogModel(LogLevel.INFO, LogType.INTERNAL_SERVICE, ApiCode.EPAY_PARAMETER_SEARCH,
                 username, detail);

        DataTable<EpayParameterSearchDto> epayParameterSearchDto = epayParameterService.SearchEpayParameterByCriteria(page, request.getHeader("Accept-Language").toUpperCase(), paramType, paramCode); 
		return restManager.addSuccess(epayParameterSearchDto);
    }

    @ApiOperation(value = "Delete Paremater", notes = "ลบข้อมูลค่าคงที่สำหรับหน่วยรับชำระ")
    @DeleteMapping(value = "delete/{id}", headers = { "Authorization", "Accept-Language" })
    public Object delete(@PathVariable(value = "id") long id, HttpServletRequest request) {
        String detail = String.format("Delete Paremeter [%d]", id);
        logger.info(detail);
        RestManager restManager = RestManager.getInstance();
        String username = authenticationFacade.getAuthentication().getName();
        EpayLogModel log = new EpayLogModel(LogLevel.INFO, LogType.INTERNAL_SERVICE, ApiCode.EPAY_PARAMETER_DELETE,
                 username, detail);
        try {
            epayParameterService.delete(id);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            restManager.addGlobalErrorbyProperty("app.sys-resp.database_error");
        } finally {
            epayLogService.insertLog(restManager, log);
        }
        restManager.throwsException();
        return restManager.addSuccess(null);
    }

    @ApiOperation(value = "Update Paremeter", notes = "แก้ไขปรับปรุงข้อมูลค่าคงที่สำหรับหน่วยรับชำระ")
    @PutMapping(value = "update", headers = { "Authorization", "Accept-Language" })
    public Object update(@Valid @RequestBody ReqSubmitParameterModel reqSubmitParameter,HttpServletRequest request) {
        String detail = String.format("Update Paremeter [%d]", reqSubmitParameter.getId());
        logger.info(detail);
        RestManager restManager = RestManager.getInstance();
        String username = authenticationFacade.getAuthentication().getName();
		EpayLogModel log = new EpayLogModel(LogLevel.INFO, LogType.INTERNAL_SERVICE, ApiCode.EPAY_PARAMETER_UPDATE,
				 username, detail);
        Date now = new Date();

        Optional<EpayParameter> paramOp = epayParameterService.findById(reqSubmitParameter.getId());
        EpayParameter param = null;
        if (paramOp.isPresent()) {
            param = paramOp.get();
        } else {
            Object[] obj = { "Parameter" };
            restManager.addGlobalErrorbyProperty("app.map-resp.id_not_found", obj);
            epayLogService.insertLog(restManager, log);
            restManager.throwsException();
        }

        try {
        	param.setParamValue(reqSubmitParameter.getParamValue());
        	param.setUpdateBy(username);
        	param.setUpdateDate(now);
            epayParameterService.save(param);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            restManager.addGlobalErrorbyProperty("app.sys-resp.database_error");
        } finally {
            epayLogService.insertLog(restManager, log);
        }

        restManager.throwsException();
        return restManager.addSuccess(epayParameterService.convertToDto(param));
    }
}
