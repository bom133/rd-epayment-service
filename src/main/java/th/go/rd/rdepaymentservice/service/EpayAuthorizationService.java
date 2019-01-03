package th.go.rd.rdepaymentservice.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import th.go.rd.rdepaymentservice.dto.CertificateApiAuthDto;
import th.go.rd.rdepaymentservice.dto.EpayAuthorizationDto;
import th.go.rd.rdepaymentservice.dto.EpayRolePermissionDto;
import th.go.rd.rdepaymentservice.dto.MasterStatusDto;
import th.go.rd.rdepaymentservice.dto.MasterSystemInfoDto;
import th.go.rd.rdepaymentservice.entity.EpayAuthorization;
import th.go.rd.rdepaymentservice.entity.EpayRolePermission;
import th.go.rd.rdepaymentservice.manager.OrmXmlManager;
import th.go.rd.rdepaymentservice.model.DataTable;
import th.go.rd.rdepaymentservice.model.PagingModel;
import th.go.rd.rdepaymentservice.repository.EpayAuthorizationRepository;
import th.go.rd.rdepaymentservice.repository.MasterStatusRepository;
import th.go.rd.rdepaymentservice.repository.SimpleDataTableRepository;

@Service
@Transactional
public class EpayAuthorizationService {

	@Autowired
	EpayAuthorizationRepository epayAuthorizationRepository;

	@Autowired
	MasterStatusRepository masterStatusRepository;
	
	@Autowired
	private SimpleDataTableRepository simpleDataTableRepository;
	
	private final String langEn = "EN";
	private final String langTh = "TH";

	public void save(EpayAuthorization epayAuthorization) {
		epayAuthorizationRepository.save(epayAuthorization);
	}

	public List<EpayAuthorization> findAll() {
		return epayAuthorizationRepository.findAll();
	}

	public List<EpayAuthorization> findByAuthCode(String authCode) {
		return epayAuthorizationRepository.findByAuthCode(authCode);
	}

	public Optional<EpayAuthorization> findById(long id) {
		return epayAuthorizationRepository.findById(id);
	}

	public void delete(long id) {
		epayAuthorizationRepository.deleteById(id);
	}

	public void deleteInBatch(List<Long> authorizationIds) {
		for (long e : authorizationIds) {
			this.delete(e);
		}
	}

	public DataTable<CertificateApiAuthDto> searchAuthByCriteria(PagingModel page, String lang, String authCode, String description, String status) {
		String query = OrmXmlManager.getQuery("findAuthByCriteria");
		Map<String, Object> params = new HashMap<String, Object>();
		if(!StringUtils.isEmpty(lang)) {
			if(lang.equals(langEn)) {
				query += " CONCAT(m.STATUS_NAME_EN,'') AS status";
			}
			else if(lang.equals(langTh)) {
				query += " CONCAT(m.STATUS_NAME_TH,'') AS status";
			}
		}
		query += " FROM EFEPAY_DEV.EPAY_AUTHORIZATION a, EFEPAY_DEV.MASTER_STATUS m WHERE a.STATUS = m.STATUS ";
		if (!StringUtils.isEmpty(authCode)) {
			query += " AND a.AUTH_CODE like :authCode";
			params.put("authCode","%"+authCode+"%");
		}
		if (!StringUtils.isEmpty(description)) {
			query += " AND a.DESCRIPTION like :description ";
			params.put("description","%"+description+"%");
		}
		if (!StringUtils.isEmpty(status)) {
			query += " AND a.STATUS = :status";
			params.put("status",status.toUpperCase());
		}
		
		DataTable<CertificateApiAuthDto> pagingData = simpleDataTableRepository.getPagingData(query, page, params,
				CertificateApiAuthDto.class);
		return pagingData;
	}

	public EpayAuthorizationDto convertToDto(EpayAuthorization epayAuthorization) {
		ModelMapper modelMapper = new ModelMapper();
		EpayAuthorizationDto authorizationDto = modelMapper.map(epayAuthorization, EpayAuthorizationDto.class);
		MasterStatusDto masterStatusDto = modelMapper.map(epayAuthorization.getMasterStatus(), MasterStatusDto.class);
		authorizationDto.setMasterStatusDto(masterStatusDto);
		List<EpayRolePermissionDto> epayRolePermissionDtos = new ArrayList<>();
		for (EpayRolePermission epayRolePermission : epayAuthorization.getEpayRolePermissions()) {
			EpayRolePermissionDto epayRolePermissionDto = new EpayRolePermissionDto();
			epayRolePermissionDto.setEpayRolePermissionId(epayRolePermission.getEpayRolePermissionId());
			MasterSystemInfoDto masterSystemInfo = modelMapper.map(epayRolePermission.getMasterSystemInfo(),
					MasterSystemInfoDto.class);
			epayRolePermissionDto.setMasterSystemInfoDto(masterSystemInfo);
			epayRolePermissionDtos.add(epayRolePermissionDto);
		}
		authorizationDto.setEpayRolePermissions(epayRolePermissionDtos);
		return authorizationDto;
	}

	public List<EpayAuthorizationDto> convertToDto(List<EpayAuthorization> epayAuthorizations) {
		ModelMapper modelMapper = new ModelMapper();
		List<EpayAuthorizationDto> authorizationDtos = new ArrayList<>();
		for (EpayAuthorization e : epayAuthorizations) {
			EpayAuthorizationDto authorizationDto = modelMapper.map(e, EpayAuthorizationDto.class);
			MasterStatusDto masterStatusDto = modelMapper.map(e.getMasterStatus(), MasterStatusDto.class);
			authorizationDto.setMasterStatusDto(masterStatusDto);
			List<EpayRolePermissionDto> epayRolePermissionDtos = new ArrayList<>();
			for (EpayRolePermission epayRolePermission : e.getEpayRolePermissions()) {
				EpayRolePermissionDto epayRolePermissionDto = new EpayRolePermissionDto();
				epayRolePermissionDto.setEpayRolePermissionId(epayRolePermission.getEpayRolePermissionId());
				MasterSystemInfoDto masterSystemInfo = modelMapper.map(epayRolePermission.getMasterSystemInfo(),
						MasterSystemInfoDto.class);
				epayRolePermissionDto.setMasterSystemInfoDto(masterSystemInfo);
				epayRolePermissionDtos.add(epayRolePermissionDto);
			}
			authorizationDto.setEpayRolePermissions(epayRolePermissionDtos);
			authorizationDtos.add(authorizationDto);
		}
		return authorizationDtos;
	}

}
