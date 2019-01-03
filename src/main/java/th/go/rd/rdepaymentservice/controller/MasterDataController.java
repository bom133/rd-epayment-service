package th.go.rd.rdepaymentservice.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import th.go.rd.rdepaymentservice.dto.MasterParamTypeDto;
import th.go.rd.rdepaymentservice.dto.MasterReceiverTypeDto;
import th.go.rd.rdepaymentservice.dto.MasterStatusDto;
import th.go.rd.rdepaymentservice.entity.MasterParamType;
import th.go.rd.rdepaymentservice.entity.MasterReceiverType;
import th.go.rd.rdepaymentservice.entity.MasterStatus;
import th.go.rd.rdepaymentservice.manager.RestManager;
import th.go.rd.rdepaymentservice.service.MasterParamTypeService;
import th.go.rd.rdepaymentservice.service.MasterPaymentLineService;
import th.go.rd.rdepaymentservice.service.MasterReceiverTypeService;
import th.go.rd.rdepaymentservice.service.MasterReceiverUnitService;
import th.go.rd.rdepaymentservice.service.MasterStatusService;

@RestController
@RequestMapping("/epay/masterdata")
public class MasterDataController {

    private static final Logger logger = LoggerFactory.getLogger(MasterDataController.class);

    @Autowired
    MasterParamTypeService masterParamTypeService;

    @Autowired
    MasterReceiverUnitService masterReceiverUnitService;

    @Autowired
    MasterPaymentLineService masterPaymentLineService;

    @Autowired
    MasterStatusService masterStatusService;
    
    @Autowired
    MasterReceiverTypeService masterReceiverTypeService;

    @ApiOperation(value = "MasterParamTypes List", notes = "สำหรับดึงข้อมูลรายการ Param Type")
    @GetMapping(value = "MasterParamType/list")
    public Object getMasterParamType() {
        logger.info("MasterParamTypes List");
        RestManager restManager = RestManager.getInstance();
        List<MasterParamType> masterParamTypes = masterParamTypeService.findAll();
        List<MasterParamTypeDto> masterParamTypeDtos = masterParamTypeService.convertToDto(masterParamTypes);
        restManager.throwsException();
        return restManager.addSuccess(masterParamTypeDtos);
    }

    @ApiOperation(value = "MasterStatus List", notes = "สำหรับดึงข้อมูลรายการ MasterStatus")
    @GetMapping(value = "MasterStatus/list")
    public Object getMasterStatus() {
        logger.info("MasterStatus List");
        RestManager restManager = RestManager.getInstance();
        List<MasterStatus> masterStatuss = masterStatusService.findAll();
        List<MasterStatusDto> masterStatusDtos = masterStatusService.convertToDto(masterStatuss);
        restManager.throwsException();
        return restManager.addSuccess(masterStatusDtos);
    }
    
    @ApiOperation(value = "MasterReceiverType List", notes = "สำหรับดึงข้อมูลรายการ MasterReceiverType")
    @GetMapping(value = "MasterReceiverType/list")
    public Object getMasterReceiverType() {
        logger.info("MasterReceiverType List");
    	RestManager restManager = RestManager.getInstance();
    	List<MasterReceiverType> masterReceiverTypes = masterReceiverTypeService.findAll();
    	List<MasterReceiverTypeDto> masterReeiverTypeDtos = masterReceiverTypeService.convertToDto(masterReceiverTypes);
    	restManager.throwsException();
    	return restManager.addSuccess(masterReeiverTypeDtos);
    }
    
    @ApiOperation(value = "Certificate List", notes = "สำหรับดึงข้อมูลรายการ Certificate")
    @GetMapping(value = "Certificate/list")
    public Object getCertificate() {
        logger.info("Certificate List");
        RestManager restManager = RestManager.getInstance();
        return restManager.addSuccess(null);
    }
}
