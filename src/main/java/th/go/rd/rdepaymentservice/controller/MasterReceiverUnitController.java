package th.go.rd.rdepaymentservice.controller;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.sql.rowset.serial.SerialException;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.ApiOperation;
import th.go.rd.rdepaymentservice.component.AuthenticationFacade;
import th.go.rd.rdepaymentservice.constant.ApiCode;
import th.go.rd.rdepaymentservice.constant.LogLevel;
import th.go.rd.rdepaymentservice.constant.LogType;
import th.go.rd.rdepaymentservice.dto.MasterReceiverUnitDto;
import th.go.rd.rdepaymentservice.dto.MasterReceiverUnitListDto;
import th.go.rd.rdepaymentservice.entity.MasterReceiverType;
import th.go.rd.rdepaymentservice.entity.MasterReceiverUnit;
import th.go.rd.rdepaymentservice.entity.MasterStatus;
import th.go.rd.rdepaymentservice.manager.RestManager;
import th.go.rd.rdepaymentservice.model.EpayLogModel;
import th.go.rd.rdepaymentservice.service.EpayLogService;
import th.go.rd.rdepaymentservice.service.MasterReceiverTypeService;
import th.go.rd.rdepaymentservice.service.MasterReceiverUnitService;
import th.go.rd.rdepaymentservice.service.MasterStatusService;
import th.go.rd.rdepaymentservice.util.BlobHelper;

@RestController
@RequestMapping("/epay/receiver-unit")
public class MasterReceiverUnitController {

	private static final Logger logger = LoggerFactory.getLogger(MasterReceiverUnitController.class);

	@Autowired
	private MasterReceiverUnitService masterReceiverUnitService;

	@Autowired
	private MasterReceiverTypeService masterReceiverTypeService;

	@Autowired
	private MasterStatusService masterStatusService;

	@Autowired
	private AuthenticationFacade authenticationFacade;

	@Autowired
	private EpayLogService epayLogService;

	// get list
	@ApiOperation(value = "MasterReceiverUnit List", notes = "สำหรับดึงข้อมูล หน่วยรับชำระภาษี")
	@GetMapping(value = "/list", headers = { "Authorization", "Accept-Language" })
	public Object SearchReceiverUnitAll(HttpServletRequest request) {
		logger.info("MasterReceiverUnit List");
		RestManager restManager = RestManager.getInstance();
		List<MasterReceiverUnitListDto> masterReceiverUnitListDto = masterReceiverUnitService.SearchReceiverUnitAll(request.getHeader("Accept-Language").toUpperCase(),restManager);
		restManager.throwsException();
		return restManager.addSuccess(masterReceiverUnitListDto);
	}

	// get detail
	@ApiOperation(value = "MasterReceiverUnit Detail", notes = "รายละเอียดข้อมูลหน่วยรับชำระ")
	@GetMapping(value = "{id}", headers = { "Authorization", "Accept-Language" })
	public Object getMasterReceiverUnit(@PathVariable("id") long id) {
		logger.info("MasterReceiverUnit Detail [{0}]", id);
		RestManager restManager = RestManager.getInstance();
		Optional<MasterReceiverUnit> masterReceiverUnitOpt = masterReceiverUnitService.findById(id);
		MasterReceiverUnitDto masterReceiverUnitDto = null;
		if (masterReceiverUnitOpt.isPresent()) {
			masterReceiverUnitDto = masterReceiverUnitService.convertToDto(masterReceiverUnitOpt.get());
		} else {
			restManager.addGlobalErrorbyProperty("app.map-resp.id_not_found");
		}
		restManager.throwsException();
		return restManager.addSuccess(masterReceiverUnitDto);
	}

	// create
	@ApiOperation(value = "Create MasterReceiverUnit", notes = "สร้างข้อมูลหน่วยรับชำระ")
	@PostMapping(value = "create", headers = { "Authorization", "Accept-Language" })
	public Object createReceiverUnit(@Valid MasterReceiverUnitDto masterReceiverUnitDto,
			MultipartFile imgFile,
			HttpServletRequest request) throws SerialException, IOException, SQLException {
		String detail = String.format("Create MasterReceiverUnit [%s]",
				masterReceiverUnitDto.getMasterReceiverTypeDto().getDescriptionEn());
		logger.info(detail);
		RestManager restManager = RestManager.getInstance();
		String username = authenticationFacade.getAuthentication().getName();
		EpayLogModel log = new EpayLogModel(LogLevel.INFO, LogType.INTERNAL_SERVICE,
				ApiCode.MASTER_RECEIVER_UNIT_CREATE, username, detail);
		Date now = new Date();

		Optional<MasterReceiverUnit> optionalMasterReceiverUnit = masterReceiverUnitService
				.findByRecCode(masterReceiverUnitDto.getRecCode());
		MasterReceiverUnit masterReceiverUnit = new MasterReceiverUnit();
		if (!optionalMasterReceiverUnit.isPresent()) {
			masterReceiverUnit.setRecCode(masterReceiverUnitDto.getRecCode());
			masterReceiverUnit.setRecShortNameTh(masterReceiverUnitDto.getRecShortNameTh());
			masterReceiverUnit.setRecShortNameEn(masterReceiverUnitDto.getRecShortNameEn());
			masterReceiverUnit.setRecNameTh(masterReceiverUnitDto.getRecNameTh());
			masterReceiverUnit.setRecNameEn(masterReceiverUnitDto.getRecNameEn());
			masterReceiverUnit.setCreateBy(username);
			masterReceiverUnit.setCreateDate(now);
			masterReceiverUnit.setUpdateBy(username);
			masterReceiverUnit.setUpdateDate(now);
			masterReceiverUnit.setRecFileNo(masterReceiverUnitDto.getRecFileNo());
			
			if (imgFile != null) {
				BlobHelper blobHelper = new BlobHelper();
				Blob imageBlob = blobHelper.multipartFileToBlob(imgFile);
				masterReceiverUnit.setImagePath(imageBlob);
			}

			MasterReceiverType masterReceiverType = new MasterReceiverType();
			Optional<MasterReceiverType> optionalMasterReceiverType = masterReceiverTypeService
					.findByReceiverType(masterReceiverUnitDto.getMasterReceiverTypeDto().getReceiverType());
			if (optionalMasterReceiverType.isPresent()) {
				masterReceiverType = optionalMasterReceiverType.get();
				masterReceiverUnit.setMasterReceiverType(masterReceiverType);
			}

			MasterStatus masterStatus = new MasterStatus();
			Optional<MasterStatus> optionalMasterStatus = masterStatusService
					.findByStatus(masterReceiverUnitDto.getMasterStatusDto().getStatus());
			if (optionalMasterStatus.isPresent()) {
				masterStatus = optionalMasterStatus.get();
				masterReceiverUnit.setMasterStatus(masterStatus);
			}
			restManager.throwsException();
			try {
				masterReceiverUnitService.save(masterReceiverUnit);
			} catch (Exception ex) {
				logger.error(ex.getMessage());
				restManager.addGlobalErrorbyProperty("app.sys-resp.database_error");
			} finally {
				epayLogService.insertLog(restManager, log);
			}

			masterReceiverUnitDto.setMasterReceiverUnitId(masterReceiverUnit.getMasterReceiverUnitId());
			masterReceiverUnitDto.setImagePath("/epay/receiver-unit/fetch-img/"+masterReceiverUnit.getMasterReceiverUnitId());
			return restManager.addSuccess(masterReceiverUnitDto);
		} else {
			logger.info("Error Code : E07107 ");
			restManager.addGlobalErrorbyProperty("app.man-resp.master_receiver_unit_exist");
			epayLogService.insertLog(restManager, log);
			restManager.throwsException();
		}
		return restManager.addSuccess(masterReceiverUnitDto);
	}
	
	@GetMapping("fetch-img/{masterReceiverUnitId}")
	public ResponseEntity fetchImage(@PathVariable("masterReceiverUnitId") long masterReceiverUnitId) throws SQLException, IOException {
		Optional<MasterReceiverUnit> masterReceiverUnitOpt = masterReceiverUnitService.findById(masterReceiverUnitId);
		MasterReceiverUnit masterReceiverUnit = null;
		byte[] blobAsBytes = null;
		try {
			if (masterReceiverUnitOpt.isPresent()) {
				masterReceiverUnit = masterReceiverUnitOpt.get();
				Blob imgBlob = masterReceiverUnit.getImagePath();
				blobAsBytes = imgBlob.getBytes(1, (int)imgBlob.length());
			}
		}
		catch (Exception ex) {
			logger.error(ex.getMessage());
		}
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("Content-Type", "image/jpeg");
		httpHeaders.add("Cache-Control", "max-age=259200");
		return new ResponseEntity(blobAsBytes, httpHeaders, HttpStatus.OK);
	}

	// update
	@ApiOperation(value = "Update MasterReceiverUnit", notes = "สำหรับหน้าจอเจ้าหน้าที่จัดการหนว่ยรับชำระ")
	@PutMapping(value = "update", headers = "Accept-Language")
	public Object updateReceiverUnit(@Valid MasterReceiverUnitDto masterReceiverUnitDto,
			MultipartFile imgFile, HttpServletRequest request) throws SerialException, IOException, SQLException {
		String detail = String.format("Update MasterReceiverUnit [%s]",
				masterReceiverUnitDto.getMasterReceiverTypeDto().getDescriptionEn());
		logger.info(detail);
		RestManager restManager = RestManager.getInstance();
		String username = authenticationFacade.getAuthentication().getName();
		EpayLogModel log = new EpayLogModel(LogLevel.INFO, LogType.INTERNAL_SERVICE,
				ApiCode.MASTER_RECEIVER_UNIT_CREATE, username, detail);
		Date now = new Date();

		Optional<MasterReceiverUnit> optionalMasterReceiverUnit = masterReceiverUnitService
				.findById(masterReceiverUnitDto.getMasterReceiverUnitId());
		if (!optionalMasterReceiverUnit.isPresent()) {
			Object[] obj = {"MasterReceiverUnit"};
			restManager.addGlobalErrorbyProperty("app.man-resp.rec_unit_id_not_found",obj);
			restManager.throwsException();
		}

		MasterReceiverUnit masterReceiverUnit = optionalMasterReceiverUnit.isPresent()? optionalMasterReceiverUnit.get() : new MasterReceiverUnit();
		// masterReceiverUnit.setRecShortName(request.getRecShortName());
		masterReceiverUnit.setRecNameTh(masterReceiverUnitDto.getRecNameTh());
		masterReceiverUnit.setRecNameEn(masterReceiverUnitDto.getRecNameEn());
		masterReceiverUnit.setUpdateBy(username);
		masterReceiverUnit.setUpdateDate(now);
		masterReceiverUnit.setRecFileNo(masterReceiverUnitDto.getRecFileNo());
		if (imgFile != null) {
			BlobHelper blobHelper = new BlobHelper();
			Blob imageBlob = blobHelper.multipartFileToBlob(imgFile);
			masterReceiverUnit.setImagePath(imageBlob);
		}

		MasterReceiverType masterReceiverType = new MasterReceiverType();
		Optional<MasterReceiverType> optionalMasterReceiverType = masterReceiverTypeService
				.findByReceiverType(masterReceiverUnitDto.getMasterReceiverTypeDto().getReceiverType());
		if (optionalMasterReceiverType.isPresent()) {
			masterReceiverType = optionalMasterReceiverType.get();
			masterReceiverUnit.setMasterReceiverType(masterReceiverType);
		}

		MasterStatus masterStatus = new MasterStatus();
		Optional<MasterStatus> optionalMasterStatus = masterStatusService
				.findByStatus(masterReceiverUnitDto.getMasterStatusDto().getStatus());
		if (optionalMasterStatus.isPresent()) {
			masterStatus = optionalMasterStatus.get();
			masterReceiverUnit.setMasterStatus(masterStatus);
		}
		try {
			masterReceiverUnitService.save(masterReceiverUnit);
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			restManager.addGlobalErrorbyProperty("app.sys-resp.database_error");
		} finally {
			epayLogService.insertLog(restManager, log);
		}
		masterReceiverUnitDto = masterReceiverUnitService.convertToDto(masterReceiverUnit);
		masterReceiverUnitDto.setImagePath("/epay/receiver-unit/fetch-img/"+masterReceiverUnitDto.getMasterReceiverUnitId());
		return restManager.addSuccess(masterReceiverUnitDto);
	}

	// delete
	@ApiOperation(value = "Delete MasterReceiverUnit", notes = "ลบข้อมูลหน่วยรับชำระ")
	@DeleteMapping(value = "delete/{id}", headers = { "Authorization", "Accept-Language" })
	public Object deleteReceiverUnit(@PathVariable("id") long id, HttpServletRequest request) {
		String detail = String.format("Delete MasterReceiverUnit [%d]", id);
		logger.info(detail);
		RestManager restManager = RestManager.getInstance();
		String username = authenticationFacade.getAuthentication().getName();
		EpayLogModel log = new EpayLogModel(LogLevel.INFO, LogType.INTERNAL_SERVICE,
				ApiCode.MASTER_RECEIVER_UNIT_DELETE, username, detail);
		try {
			masterReceiverUnitService.delete(id);
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			restManager.addGlobalErrorbyProperty("app.sys-resp.database_error");
		} finally {
			epayLogService.insertLog(restManager, log);
		}
		restManager.throwsException();
		return restManager.addSuccess(null);
	}

	// search
	@ApiOperation(value = "Search MasterReceiverUnit", notes = "สำหรับหน้าจอค้นหาข้อมูลหน่วยรับชำระ")
	@PostMapping(value = "search", headers = { "Authorization", "Accept-Language" })
	public Object searchReceiverUnit(String recCode, String recShortNameTh, String recShortNameEn, int receiverType, String status,
			HttpServletRequest request) {
		String detail = String.format("Search MasterReceiverUnit [%s,%s,%s,%d,%s]", recCode, recShortNameTh, recShortNameEn, receiverType,
				status);
		logger.info(detail);
		RestManager restManager = RestManager.getInstance();
		String username = authenticationFacade.getAuthentication().getName();
		EpayLogModel log = new EpayLogModel(LogLevel.INFO, LogType.INTERNAL_SERVICE,
				ApiCode.MASTER_RECEIVER_UNIT_DELETE, username, detail);
		List<MasterReceiverUnit> masterReceiverUnits = null;
		try {
			masterReceiverUnits = masterReceiverUnitService.search(recCode, recShortNameTh, recShortNameEn, receiverType, status);
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			restManager.addGlobalErrorbyProperty("app.sys-resp.database_error");
		} finally {
			epayLogService.insertLog(restManager, log);
		}
		restManager.throwsException();
		return restManager.addSuccess(masterReceiverUnitService.convertToDto(masterReceiverUnits));

	}
}
