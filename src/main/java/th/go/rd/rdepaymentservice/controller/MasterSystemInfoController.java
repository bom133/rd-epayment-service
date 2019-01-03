package th.go.rd.rdepaymentservice.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import th.go.rd.rdepaymentservice.dto.MasterSystemInfoDto;
import th.go.rd.rdepaymentservice.manager.RestManager;
import th.go.rd.rdepaymentservice.model.CreateMasterSystemInfo;
import th.go.rd.rdepaymentservice.model.DataTable;
import th.go.rd.rdepaymentservice.model.PagingModel;
import th.go.rd.rdepaymentservice.model.UpdateMasterSystemInfo;
import th.go.rd.rdepaymentservice.service.MasterSystemInfoService;

@RestController
@RequestMapping("/epay/system-info")
public class MasterSystemInfoController {
	private static final Logger logger = LoggerFactory.getLogger(MasterSystemInfoController.class);
	
	@Autowired
	MasterSystemInfoService masterSystemInfoService;
	
	// Security เป็น OAuth ยังไม่ต้องส่ง header มาตรวจสอบสิทธิ์การใช้งาน รอสเปคก่อน
	@PostMapping("/")
	@ResponseBody
	@ApiResponses({ @ApiResponse(code = 201, message = "Success") })
	@ApiOperation(value = "Create System Info", notes = "สำหรับเพิ่มข้อมูลใน Table : MASTER_SYSTEM_INFO")
	public Object createSystemInfo(@Valid @RequestBody CreateMasterSystemInfo createMasterSystemInfo, BindingResult bindingResult) {
		RestManager exManager = RestManager.getInstance();
		exManager.addBindingResult(bindingResult);
		masterSystemInfoService.createMasterSystemInfo(createMasterSystemInfo, exManager);
		exManager.throwsException();
		return new ResponseEntity<>(exManager.getSuccess(), HttpStatus.CREATED);
	}
	
	@GetMapping(value = "/all", headers = { "Authorization", "Accept-Language" })
	@ResponseBody
	@ApiResponses({ @ApiResponse(code = 200, message = "Success") })
	@ApiOperation(value = "Get All System Info", notes = "สำหรับดึงข้อมูล MASTER_SYSTEM_INFO (ทั้งหมด) มาแสดงในหน้าจอจัดการ SYSTEM_INFO")
	public Object searchSystemInfoAll(@ModelAttribute("paging") PagingModel page) {
		RestManager exManager = RestManager.getInstance();
		DataTable<MasterSystemInfoDto> masterSystemInfoDto = masterSystemInfoService.getSystemInfoAll(page,exManager);
		exManager.throwsException();
		return exManager.addSuccess(masterSystemInfoDto);
	}
	
	@GetMapping("/")
	@ResponseBody
	@ApiResponses({ @ApiResponse(code = 200, message = "Success") })
	@ApiOperation(value = "Get System Info by Criteria", notes = "สำหรับดึงข้อมูล System Info มาแสดงในหน้าจอจัดการ System Info (ตามเงื่อนไข)")
	public Object searchSystemInfoByCriteria(@ModelAttribute("paging") PagingModel page, String systemInfoCode,String systemInfoName, String status ) {
		RestManager exManager = RestManager.getInstance();
		DataTable<MasterSystemInfoDto> masterSystemInfoDto = masterSystemInfoService.getSystemInfoByCriteria(page, systemInfoCode, systemInfoName, status, exManager); 
		return exManager.addSuccess(masterSystemInfoDto);
	}
	
	@GetMapping("/{systemInfoID}")
	@ResponseBody
	@ApiResponses({ @ApiResponse(code = 200, message = "Success") })
	@ApiOperation(value = "Get System Info by Id", notes = "สำหรับดึงข้อมูลมาแสดงในหน้าจอเพื่อปรับปรุงข้อมูล System Info")
	public Object searchSystemInfoByID(@ModelAttribute("paging") PagingModel page, @RequestParam int systemInfoID) {
		RestManager exManager = RestManager.getInstance();
		DataTable<MasterSystemInfoDto> masterSystemInfoDto = masterSystemInfoService.getSystemInfoByID(page, systemInfoID, exManager); 
		return exManager.addSuccess(masterSystemInfoDto);
	}
	

	@PostMapping("/{systemInfoID}")
	@ResponseBody
	@ApiResponses({ @ApiResponse(code = 200, message = "Success") })
	@ApiOperation(value = "Update System Info", notes = "สำหรับปรับปรุงข้อมูล System Info")
	public Object updateSystemInfo(@Valid @RequestBody UpdateMasterSystemInfo updateMasterSystemInfo, BindingResult bindingResult) {
		RestManager restManager = RestManager.getInstance();
		restManager.addBindingResult(bindingResult);
		restManager.throwsException();
		masterSystemInfoService.updateSystemInfo(updateMasterSystemInfo, restManager);
		restManager.throwsException();
		return new ResponseEntity<>(restManager.getSuccess(), HttpStatus.OK);
	}
	
	@DeleteMapping("/{systemInfoID}")
	@ResponseBody
	@ApiResponses({ @ApiResponse(code = 200, message = "Success") })
	@ApiOperation(value = "Delete System Info", notes = "สำหรับลบ System Info ")
	public Object deleteSystemInfo(@PathVariable(value = "systemInfoID") int systemInfoID) {
		RestManager restManager = RestManager.getInstance();
		masterSystemInfoService.deleteSystemInfo(systemInfoID, restManager);
		restManager.throwsException();
		return new ResponseEntity<>(restManager.getSuccess(), HttpStatus.OK);
	}
}
