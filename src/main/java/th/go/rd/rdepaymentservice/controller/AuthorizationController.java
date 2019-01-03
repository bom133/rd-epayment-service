package th.go.rd.rdepaymentservice.controller;

import java.util.ArrayList;
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
import th.go.rd.rdepaymentservice.dto.CertificateApiAuthDto;
import th.go.rd.rdepaymentservice.dto.EpayAuthorizationDto;
import th.go.rd.rdepaymentservice.dto.EpayRolePermissionDto;
import th.go.rd.rdepaymentservice.entity.EpayAuthorization;
import th.go.rd.rdepaymentservice.entity.EpayRolePermission;
import th.go.rd.rdepaymentservice.entity.MasterStatus;
import th.go.rd.rdepaymentservice.entity.MasterSystemInfo;
import th.go.rd.rdepaymentservice.manager.RestManager;
import th.go.rd.rdepaymentservice.model.DataTable;
import th.go.rd.rdepaymentservice.model.EpayLogModel;
import th.go.rd.rdepaymentservice.model.PagingModel;
import th.go.rd.rdepaymentservice.service.EpayAuthorizationService;
import th.go.rd.rdepaymentservice.service.EpayLogService;
import th.go.rd.rdepaymentservice.service.MasterStatusService;
import th.go.rd.rdepaymentservice.service.MasterSystemInfoService;

@RestController
@RequestMapping("/epay/api-auth")
public class AuthorizationController {

	private static final Logger logger = LoggerFactory.getLogger(AuthorizationController.class);

	@Autowired
	private EpayAuthorizationService epayAuthorizationService;

	@Autowired
	private MasterStatusService masterStatusService; 

	@Autowired
	private MasterSystemInfoService masterSystemInfoService;

	@Autowired
	private EpayLogService epayLogService;

	@Autowired
	private AuthenticationFacade authenticationFacade;

	@ApiOperation(value = "Authorization", notes = "สำหรับขอข้อมูลสิทธิ์การใช้งานทั้งหมด")
	@GetMapping(value = "list", headers = { "Authorization", "Accept-Language" })
	public Object list() {
		logger.info("Authorization List");
		RestManager restManager = RestManager.getInstance();
		List<EpayAuthorization> epayAuthorizations = epayAuthorizationService.findAll();
		List<EpayAuthorizationDto> authorizationDtos = epayAuthorizationService.convertToDto(epayAuthorizations);
		return restManager.addSuccess(authorizationDtos);
	}

	@ApiOperation(value = "Authorization", notes = "สำหรับขอข้อมูลสิทธิ์การใช้งานตามรหัสสิทธิ์ที่กำหนด")
	@GetMapping(value = "{id}", headers = { "Authorization", "Accept-Language" })
	public Object get(@PathVariable long id) {
		logger.info("Authorization Detail [{0}]", id);
		RestManager restManager = RestManager.getInstance();

		Optional<EpayAuthorization> oEpayAuthorization = epayAuthorizationService.findById(id);
		EpayAuthorization epayAuthorization = null;
		if (oEpayAuthorization.isPresent()) {
			epayAuthorization = oEpayAuthorization.get();
		}
		else {
			Object[] obj = {"Authorization"};
			restManager.addGlobalErrorbyProperty("app.man-resp.id_not_found",obj);
			restManager.throwsException();
		}
		EpayAuthorizationDto authorizationDto = epayAuthorizationService.convertToDto(epayAuthorization);
		return restManager.addSuccess(authorizationDto);
	}
	
	@ApiOperation(value = "Authorization", notes = "สำหรับค้นหาข้อมูลสิทธิ์การใช้งาน")
	@GetMapping(value = "/search", headers = { "Authorization", "Accept-Language" })
	public Object searchAuthByCriteria(@ModelAttribute("paging") PagingModel page, HttpServletRequest request, String authCode, String description, String status) {
		String detail = String.format("Search Authorization [%s,%s,%s]", authCode, description, status);
		logger.info(detail);
		RestManager restManager = RestManager.getInstance();
		String username = authenticationFacade.getAuthentication().getName();
		EpayLogModel log = new EpayLogModel(LogLevel.INFO, LogType.INTERNAL_SERVICE, ApiCode.AUTHORIZTION_SEARCH,
				 username, detail);
		DataTable<CertificateApiAuthDto> certificateApiAuthDto = epayAuthorizationService.searchAuthByCriteria(page, request.getHeader("Accept-Language").toUpperCase(), authCode, description, status); 
		return restManager.addSuccess(certificateApiAuthDto);
	}

	@ApiOperation(value = "Authorization", notes = "สำหรับลบข้อมูลสิทธิ์การใช้งานตามรหัสสิทธิ์ที่กำหนด")
	@DeleteMapping(value = "delete/{id}", headers = { "Authorization", "Accept-Language" })
	public Object delete(@PathVariable long id, HttpServletRequest request) {
		String detail = String.format("Delete Authorization [%d]", id);
		logger.info(detail);
		RestManager restManager = RestManager.getInstance();
		String username = authenticationFacade.getAuthentication().getName();
		EpayLogModel log = new EpayLogModel(LogLevel.INFO, LogType.INTERNAL_SERVICE, ApiCode.AUTHORIZTION_DELETE,
				 username, detail);

		try {
			epayAuthorizationService.delete(id);
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			restManager.addGlobalErrorbyProperty("app.sys-resp.database_error");
		} finally {
			epayLogService.insertLog(restManager, log);
		}
		restManager.throwsException();
		return restManager.addSuccess(null);
	}

	@ApiOperation(value = "Authorization", notes = "สำหรับสร้างข้อมูลสิทธิ์การใช้งาน")
	@PostMapping(value = "create", headers = { "Authorization", "Accept-Language" })
	public Object create(@Valid @RequestBody EpayAuthorizationDto epayAuthorizationDto, HttpServletRequest request) {
		String detail = String.format("Create Authorization [%s]", epayAuthorizationDto.getAuthCode());
		logger.info(detail);
		RestManager restManager = RestManager.getInstance();
		String username = authenticationFacade.getAuthentication().getName();
		EpayLogModel log = new EpayLogModel(LogLevel.INFO, LogType.INTERNAL_SERVICE, ApiCode.AUTHORIZTION_CREATE,
				 username, detail);
		Date now = new Date();

		List<EpayAuthorization> oEpayAuthorization = epayAuthorizationService
				.findByAuthCode(epayAuthorizationDto.getAuthCode());
		if (oEpayAuthorization.size() > 0) {
			restManager.addGlobalErrorbyProperty("app.man-resp.auth_code_exist");
			epayLogService.insertLog(restManager, log);
			restManager.throwsException();
		}

		EpayAuthorization epayAuthorization = new EpayAuthorization();
		MasterStatus masterStatus = new MasterStatus();
		Optional<MasterStatus> oMasterStatus = masterStatusService
				.findByStatus(epayAuthorizationDto.getMasterStatusDto().getStatus());
		if (oMasterStatus.isPresent()) {
			masterStatus = oMasterStatus.get();
			epayAuthorization.setMasterStatus(masterStatus);
		}
		epayAuthorization.setAuthCode(epayAuthorizationDto.getAuthCode());
		epayAuthorization.setDescription(epayAuthorizationDto.getDescription());

		epayAuthorization.setCreateBy(username);
		epayAuthorization.setCreateDate(now);
		epayAuthorization.setUpdateBy(username);
		epayAuthorization.setUpdateDate(now);

		List<EpayRolePermissionDto> reqSubmitRolePermission = epayAuthorizationDto.getEpayRolePermissions();
		for (EpayRolePermissionDto e : reqSubmitRolePermission) {
			EpayRolePermission epayRolePermission = new EpayRolePermission();

			Optional<MasterSystemInfo> masterSystemInfo = masterSystemInfoService
					.findById(e.getMasterSystemInfoDto().getSystemInfoId());
			if (masterSystemInfo.isPresent()) {
				epayRolePermission.setMasterSystemInfo(masterSystemInfo.get());
			}
			epayRolePermission.setCreateBy(username);
			epayRolePermission.setCreateDate(now);
			epayRolePermission.setUpdateBy(username);
			epayRolePermission.setUpdateDate(now);
			epayRolePermission.setEpayAuthorization(epayAuthorization);

			epayAuthorization.getEpayRolePermissions().add(epayRolePermission);
		}

		try {
			epayAuthorizationService.save(epayAuthorization);
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			restManager.addGlobalErrorbyProperty("app.sys-resp.database_error");
		} finally {
			epayLogService.insertLog(restManager, log);
		}

		epayAuthorizationDto.setEpayAuthorizationId(epayAuthorization.getEpayAuthorizationId());

		return restManager.addSuccess(epayAuthorizationDto);
	}

	@ApiOperation(value = "Authorization", notes = "สำหรับแก้ไขข้อมูลสิทธิ์การใช้งาน")
	@PutMapping(value = "update", headers = { "Authorization", "Accept-Language" })
	public Object update(@Valid @RequestBody EpayAuthorizationDto epayAuthorizationDto, HttpServletRequest request) {
		String detail = String.format("Update Authorization [%d]", epayAuthorizationDto.getEpayAuthorizationId());
		logger.info(detail);
		RestManager restManager = RestManager.getInstance();
		String username = authenticationFacade.getAuthentication().getName();
		EpayLogModel log = new EpayLogModel(LogLevel.INFO, LogType.INTERNAL_SERVICE, ApiCode.AUTHORIZTION_UPDATE,
				 username, detail);
		Date now = new Date();

		Optional<EpayAuthorization> oEpayAuthorization = epayAuthorizationService
				.findById(epayAuthorizationDto.getEpayAuthorizationId());
		EpayAuthorization epayAuthorization = null;
		if (oEpayAuthorization.isPresent()) {
			epayAuthorization = oEpayAuthorization.get();
		}
		else {
			Object[] obj = {"Authorization"};
			restManager.addGlobalErrorbyProperty("app.man-resp.id_not_found",obj);
			restManager.throwsException();
		}

		MasterStatus masterStatus = new MasterStatus();
		Optional<MasterStatus> oMasterStatus = masterStatusService
				.findByStatus(epayAuthorizationDto.getMasterStatusDto().getStatus());
		if (oMasterStatus.isPresent()) {
			masterStatus = oMasterStatus.get();
			epayAuthorization.setMasterStatus(masterStatus);
		}
		epayAuthorization.setAuthCode(epayAuthorizationDto.getAuthCode());
		epayAuthorization.setDescription(epayAuthorizationDto.getDescription());

		epayAuthorization.setUpdateBy(username);
		epayAuthorization.setUpdateDate(now);

		epayAuthorization.getEpayRolePermissions().clear();

		List<EpayRolePermissionDto> reqSubmitRolePermission = epayAuthorizationDto.getEpayRolePermissions();
		List<EpayRolePermission> epayRolePermissionsUpdate = new ArrayList<>();
		for (EpayRolePermissionDto e : reqSubmitRolePermission) {
			EpayRolePermission epayRolePermission = new EpayRolePermission();
			epayRolePermission.setEpayRolePermissionId(e.getEpayRolePermissionId());

			Optional<MasterSystemInfo> masterSystemInfo = masterSystemInfoService
					.findById(e.getMasterSystemInfoDto().getSystemInfoId());
			if (masterSystemInfo.isPresent()) {
				epayRolePermission.setMasterSystemInfo(masterSystemInfo.get());
			}
			epayRolePermission.setCreateBy(username);
			epayRolePermission.setCreateDate(now);
			epayRolePermission.setUpdateBy(username);
			epayRolePermission.setUpdateDate(now);
			epayRolePermission.setEpayAuthorization(epayAuthorization);

			epayRolePermissionsUpdate.add(epayRolePermission);
		}

		epayAuthorization.getEpayRolePermissions().addAll(epayRolePermissionsUpdate);

		try {
			epayAuthorizationService.save(epayAuthorization);
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			restManager.addGlobalErrorbyProperty("app.sys-resp.database_error");
		} finally {
			epayLogService.insertLog(restManager, log);
		}

		EpayAuthorizationDto authorizationDto = epayAuthorizationService.convertToDto(oEpayAuthorization.get());

		return restManager.addSuccess(authorizationDto);
	}
}
