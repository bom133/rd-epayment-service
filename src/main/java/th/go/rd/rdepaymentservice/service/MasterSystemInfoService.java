package th.go.rd.rdepaymentservice.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import th.go.rd.rdepaymentservice.dto.MasterSystemInfoDto;
import th.go.rd.rdepaymentservice.entity.EpayAuthorization;
import th.go.rd.rdepaymentservice.entity.EpayRolePermission;
import th.go.rd.rdepaymentservice.entity.MasterStatus;
import th.go.rd.rdepaymentservice.entity.MasterSystemInfo;
import th.go.rd.rdepaymentservice.manager.RestManager;
import th.go.rd.rdepaymentservice.model.CreateMasterSystemInfo;
import th.go.rd.rdepaymentservice.model.DataTable;
import th.go.rd.rdepaymentservice.model.PagingModel;
import th.go.rd.rdepaymentservice.model.UpdateMasterSystemInfo;
import th.go.rd.rdepaymentservice.repository.EpayAuthorizationRepository;
import th.go.rd.rdepaymentservice.repository.EpayRolePermissionRepository;
import th.go.rd.rdepaymentservice.repository.MasterStatusRepository;
import th.go.rd.rdepaymentservice.repository.MasterSystemInfoRepository;
import th.go.rd.rdepaymentservice.repository.SimpleDataTableRepository;
import th.go.rd.rdepaymentservice.manager.OrmXmlManager;

@Service
@Transactional
public class MasterSystemInfoService {

    @Autowired
    MasterSystemInfoRepository masterSystemInfoRepository;

    @Autowired
    MasterStatusRepository masterStatusRepository;

    @Autowired
    EpayRolePermissionRepository epayRolePermissionRepository;

    @Autowired
    EpayAuthorizationRepository epayAuthorizationRepository;

    @Autowired
    MasterStatusService masterStatusService;
    
    @Autowired
    SimpleDataTableRepository simpleDataTableRepository;

    public List<MasterSystemInfo> findAll() {
        return masterSystemInfoRepository.findAll();
    }

    public Optional<MasterSystemInfo> findById(int id) {
        return masterSystemInfoRepository.findById(id);
    }

    public void save(MasterSystemInfo entity) {
        masterSystemInfoRepository.save(entity);
    }

    public void delete(int id) {
        masterSystemInfoRepository.deleteById(id);
    }

    public boolean authen(String clientId, String secretId, long authId) {
        Optional<MasterStatus> statusOptional = masterStatusRepository.findById("A");

        Optional<MasterSystemInfo> mOptional = masterSystemInfoRepository
                .findByClientIdAndSecretIdAndMasterStatus(clientId, secretId, statusOptional.orElse(new MasterStatus()));
        if (mOptional.isPresent()) {
            Optional<EpayAuthorization> eAuthOptional = epayAuthorizationRepository.findById(authId);
            if (eAuthOptional.isPresent()) {
                Optional<EpayRolePermission> eRoleOptional = epayRolePermissionRepository
                        .findByEpayAuthorizationAndMasterSystemInfo(eAuthOptional.orElse(new EpayAuthorization()), mOptional.orElse(new MasterSystemInfo()));
                if (eRoleOptional.isPresent()) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }


	public void createMasterSystemInfo(@Valid CreateMasterSystemInfo createMasterSystemInfo, RestManager exManager) {
		Optional<MasterSystemInfo> masterSystemInfoOpt = masterSystemInfoRepository.findBySystemCode(createMasterSystemInfo.getSystemCode());
		if (masterSystemInfoOpt.isPresent()) {
			exManager.addFieldErrorbyProperty("SystemCode","app.man-resp.duplicate-value",createMasterSystemInfo.getSystemCode());
			exManager.throwsException();
		}
		masterSystemInfoOpt = masterSystemInfoRepository.findByClientId(createMasterSystemInfo.getClientId());
		if (masterSystemInfoOpt.isPresent()) {
			exManager.addFieldErrorbyProperty("ClientId","app.man-resp.duplicate-value",createMasterSystemInfo.getClientId());
			exManager.throwsException();
		}
		MasterSystemInfo masterSystemInfo = new MasterSystemInfo();
		masterSystemInfo.setSystemCode(createMasterSystemInfo.getSystemCode());
		masterSystemInfo.setSystemName(createMasterSystemInfo.getSystemName());
		masterSystemInfo.setDescription(createMasterSystemInfo.getDescription());
		masterSystemInfo.setClientId(createMasterSystemInfo.getClientId());
		masterSystemInfo.setSecretId(createMasterSystemInfo.getSecretId());
		masterSystemInfo.setMasterStatus(new MasterStatus(createMasterSystemInfo.getStatus()));
		masterSystemInfo.setCreateBy("test");
		masterSystemInfo.setCreateDate(new Date());
		masterSystemInfo.setUpdateBy("test");
		masterSystemInfo.setUpdateDate(new Date());
		masterSystemInfoRepository.save(masterSystemInfo);
		exManager.addSuccess("success");
	}

	public DataTable<MasterSystemInfoDto> getSystemInfoAll(PagingModel page, RestManager exManager) {
		String query = OrmXmlManager.getQuery("findSystemInfoAll");
		Map<String, Object> params = new HashMap<String, Object>();
		DataTable<MasterSystemInfoDto> pagingData = simpleDataTableRepository.getPagingData(query, page, params,
		MasterSystemInfoDto.class);
		return pagingData;
	}

	public DataTable<MasterSystemInfoDto> getSystemInfoByCriteria(PagingModel page, String systemInfoCode,
			String systemInfoName, String status, RestManager exManager) {
		String query = OrmXmlManager.getQuery("findSystemInfoByCriteria");
		Map<String, Object> params = new HashMap<String, Object>();
		if (!StringUtils.isEmpty(systemInfoCode)) {
			query += " WHERE SYSTEM_CODE Like :systemInfoCode";
			params.put("systemInfoCode","%"+systemInfoCode+"%");
		}
		if (!StringUtils.isEmpty(systemInfoName)) {
			query += " AND SYSTEM_NAME like :systemInfoName";
			params.put("systemInfoName","%"+systemInfoName+"%");
		}
		if (!StringUtils.isEmpty(status)) {
			query += " AND STATUS like :status ORDER BY SYSTEM_CODE";
			params.put("status","%"+status+"%");
		}
		DataTable<MasterSystemInfoDto> pagingData = simpleDataTableRepository.getPagingData(query, page, params,
				MasterSystemInfoDto.class);
		return pagingData;
	}

	public DataTable<MasterSystemInfoDto> getSystemInfoByID(PagingModel page, int systemInfoID, RestManager exManager) {
		String query = OrmXmlManager.getQuery("findSystemInfoByID");
		Map<String, Object> params = new HashMap<String, Object>();
		query += " = :systemInfoID";
		params.put("systemInfoID",systemInfoID);
		DataTable<MasterSystemInfoDto> pagingData = simpleDataTableRepository.getPagingData(query, page, params,
				MasterSystemInfoDto.class);
		return pagingData;
	}

	public void updateSystemInfo(@Valid UpdateMasterSystemInfo updateMasterSystemInfo, RestManager restManager) {
		Optional<MasterSystemInfo> masterSystemInfoOpt = masterSystemInfoRepository.findById(updateMasterSystemInfo.getSystemInfoId());
		if (!masterSystemInfoOpt.isPresent()) {
			restManager.addGlobalErrorbyProperty("app.man-resp.data-not-found");
			restManager.throwsException();
		}
		MasterSystemInfo masterSystemInfo = masterSystemInfoOpt.get();	
		if(!masterSystemInfo.getSystemCode().equals(updateMasterSystemInfo.getSystemCode()))
		{
			// For check duplicate-SystemInfo
			masterSystemInfoOpt = masterSystemInfoRepository.findBySystemCode(updateMasterSystemInfo.getSystemCode());
			if(masterSystemInfoOpt.isPresent())
			{
				restManager.addFieldErrorbyProperty("SystemCode","app.man-resp.duplicate-value",updateMasterSystemInfo.getSystemCode());
				restManager.throwsException();
			}
			masterSystemInfoOpt = masterSystemInfoRepository.findByClientId(updateMasterSystemInfo.getClientId());
			if(masterSystemInfoOpt.isPresent())
			{
				restManager.addFieldErrorbyProperty("ClientId","app.man-resp.duplicate-value",updateMasterSystemInfo.getClientId());
				restManager.throwsException();
			}
		}
		masterSystemInfo.setSystemCode(updateMasterSystemInfo.getSystemCode());
		masterSystemInfo.setSystemName(updateMasterSystemInfo.getSystemName());
		masterSystemInfo.setDescription(updateMasterSystemInfo.getDescription());
		masterSystemInfo.setClientId(updateMasterSystemInfo.getClientId());
		masterSystemInfo.setSecretId(updateMasterSystemInfo.getSecretId());
		masterSystemInfo.setMasterStatus(new MasterStatus(updateMasterSystemInfo.getStatus()));
		masterSystemInfo.setUpdateBy("test");
		masterSystemInfo.setUpdateDate(new Date());
		masterSystemInfoRepository.save(masterSystemInfo);
		restManager.addSuccess("success");
	}

	public void deleteSystemInfo(int systemInfoID, RestManager restManager) {
		Optional<MasterSystemInfo> masterSystemInfoOpt = masterSystemInfoRepository.findById(systemInfoID);
		if (!masterSystemInfoOpt.isPresent()) {
			restManager.addGlobalErrorbyProperty("app.man-resp.data-not-found");
			restManager.throwsException();
		}
		masterSystemInfoRepository.deleteById(systemInfoID);
		restManager.addSuccess("success");
	}

}
