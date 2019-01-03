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

import th.go.rd.rdepaymentservice.dto.MasterReceiverTypeDto;
import th.go.rd.rdepaymentservice.dto.MasterReceiverUnitDto;
import th.go.rd.rdepaymentservice.dto.MasterReceiverUnitListDto;
import th.go.rd.rdepaymentservice.dto.MasterStatusDto;
import th.go.rd.rdepaymentservice.entity.MasterReceiverType;
import th.go.rd.rdepaymentservice.entity.MasterReceiverUnit;
import th.go.rd.rdepaymentservice.entity.MasterStatus;
import th.go.rd.rdepaymentservice.manager.OrmXmlManager;
import th.go.rd.rdepaymentservice.manager.RestManager;
import th.go.rd.rdepaymentservice.repository.MasterReceiverTypeRepository;
import th.go.rd.rdepaymentservice.repository.MasterReceiverUnitRepository;
import th.go.rd.rdepaymentservice.repository.MasterStatusRepository;
import th.go.rd.rdepaymentservice.repository.SimpleDataTableRepository;
import th.go.rd.rdepaymentservice.util.StringHelper;

@Service
@Transactional
public class MasterReceiverUnitService {

	@Autowired
	MasterReceiverUnitRepository masterReceiverUnitRepository;

	@Autowired
	MasterReceiverTypeRepository masterReceiverTypeRepository;

	@Autowired
	MasterStatusRepository masterStatusRepository;
	
	@Autowired
	SimpleDataTableRepository simpleDataTableRepository;
	
	private final String langEn = "EN";
	private final String langTh = "TH";

	public List<MasterReceiverUnit> findAll() {
		return masterReceiverUnitRepository.findAll();
	}

	public Optional<MasterReceiverUnit> findById(long id) {
		return masterReceiverUnitRepository.findById(id);
	}

	public Optional<MasterReceiverUnit> findByRecCode(String recCode) {
		return masterReceiverUnitRepository.findByRecCode(recCode);
	}

	public List<MasterReceiverUnit> findByMasterReceiverType(int receiverType) {
		Optional<MasterReceiverType> optionalMasterReceiverType = masterReceiverTypeRepository
				.findByReceiverType(receiverType);
		if (optionalMasterReceiverType.isPresent()) {
			return masterReceiverUnitRepository.findByMasterReceiverType(optionalMasterReceiverType.get());
		} else {
			return new ArrayList<>();
		}
	}

	public List<MasterReceiverUnitDto> convertToDto(List<MasterReceiverUnit> masterReceiverUnits) {
		ModelMapper modelMapper = new ModelMapper();
		List<MasterReceiverUnitDto> masterReceiverUnitDtos = new ArrayList<>();
		for (MasterReceiverUnit masterReceiverUnit : masterReceiverUnits) {
			MasterReceiverUnitDto masterReceiverUnitDto = modelMapper.map(masterReceiverUnit,
					MasterReceiverUnitDto.class);
			MasterReceiverTypeDto masterReceiverTypeDto = modelMapper.map(masterReceiverUnit.getMasterReceiverType(),
					MasterReceiverTypeDto.class);
			masterReceiverUnitDto.setMasterReceiverTypeDto(masterReceiverTypeDto);
			MasterStatusDto masterStatusDto = modelMapper.map(masterReceiverUnit.getMasterStatus(),
					MasterStatusDto.class);
			masterReceiverUnitDto.setMasterStatusDto(masterStatusDto);
			masterReceiverUnitDto.setImagePath("/epay/receiver-unit/fetch-img/"+masterReceiverUnit.getMasterReceiverUnitId());
			masterReceiverUnitDtos.add(masterReceiverUnitDto);

		}
		return masterReceiverUnitDtos;
	}

	public MasterReceiverUnitDto convertToDto(MasterReceiverUnit masterReceiverUnit) {
		ModelMapper modelMapper = new ModelMapper();
		MasterReceiverUnitDto masterReceiverUnitDto = modelMapper.map(masterReceiverUnit, MasterReceiverUnitDto.class);
		MasterReceiverTypeDto masterReceiverTypeDto = modelMapper.map(masterReceiverUnit.getMasterReceiverType(),
				MasterReceiverTypeDto.class);
		masterReceiverUnitDto.setMasterReceiverTypeDto(masterReceiverTypeDto);
		MasterStatusDto masterStatusDto = modelMapper.map(masterReceiverUnit.getMasterStatus(), MasterStatusDto.class);
		masterReceiverUnitDto.setImagePath("/epay/receiver-unit/fetch-img/"+masterReceiverUnit.getMasterReceiverUnitId());
		masterReceiverUnitDto.setMasterStatusDto(masterStatusDto);
		return masterReceiverUnitDto;
	}

	public void save(MasterReceiverUnit entity) {
		masterReceiverUnitRepository.save(entity);
	}

	public void delete(long id) {
		masterReceiverUnitRepository.deleteById(id);
	}

	public List<MasterReceiverUnit> findBy(String recCode, String recShortName, int receiverType, String status) {
		Optional<MasterReceiverType> optionalMasterReceiverType = masterReceiverTypeRepository.findById(receiverType);
		Optional<MasterStatus> optionalMasterStatus = masterStatusRepository.findByStatus(status);
		if (optionalMasterReceiverType.isPresent() && optionalMasterStatus.isPresent()) {
			return masterReceiverUnitRepository.findByRecCodeAndRecShortNameEnAndMasterReceiverTypeAndMasterStatus(
					recCode, recShortName, optionalMasterReceiverType.get(), optionalMasterStatus.get());
		} else {
			return new ArrayList<>();
		}
	}

	//Deprecate
	public List<MasterReceiverUnit> searchDeprecate(String recCode, String recShortName, int receiverType, String status) {
		StringHelper stringHelper = new StringHelper();
		List<MasterReceiverUnit> masterReceiverUnits = null;
		MasterReceiverUnit masterReceiverUnit = null;

		Optional<MasterReceiverType> optionalMasterReceiverType = masterReceiverTypeRepository
				.findByReceiverType(receiverType);
		MasterReceiverType masterReceiverType = new MasterReceiverType();
		if (optionalMasterReceiverType.isPresent()) {
			masterReceiverType = optionalMasterReceiverType.get();
		}
		
		Optional<MasterStatus> optionalMasterStatus = masterStatusRepository.findByStatus(status);
		MasterStatus masterStatus = new MasterStatus();
		if (optionalMasterStatus.isPresent()) {
			masterStatus = optionalMasterStatus.get();
		}

		if (!stringHelper.IsEmpty(recCode) && !stringHelper.IsEmpty(recShortName)
				&& !stringHelper.IsEmpty(String.valueOf(receiverType)) && !stringHelper.IsEmpty(status)) {
			masterReceiverUnits = masterReceiverUnitRepository
					.findByRecCodeAndRecShortNameEnAndMasterReceiverTypeAndMasterStatus(recCode, recShortName,
							masterReceiverType, masterStatus);
		} else if (!stringHelper.IsEmpty(recCode) && !stringHelper.IsEmpty(recShortName)) {
			// find recCode and recShortName
			masterReceiverUnits = masterReceiverUnitRepository.findByRecCodeAndRecShortNameEnAndMasterReceiverType(
					recCode, recShortName, masterReceiverType);
		} else if (!stringHelper.IsEmpty(recCode) && !stringHelper.IsEmpty(status)) {
			// find recCode and status
			masterReceiverUnits = masterReceiverUnitRepository.findByRecCodeAndMasterStatusAndMasterReceiverType(
					recCode, masterStatus, masterReceiverType);
		} else if (!stringHelper.IsEmpty(recShortName) && !stringHelper.IsEmpty(status)) {
			// find recShortName and status
			masterReceiverUnits = masterReceiverUnitRepository.findByRecShortNameEnAndMasterStatusAndMasterReceiverType(
					recShortName, masterStatus, masterReceiverType);
		} else if (!stringHelper.IsEmpty(recCode)) {
			// find recCode
			masterReceiverUnits = masterReceiverUnitRepository.findByRecCodeAndMasterReceiverType(recCode,
					masterReceiverType);
		} else if (!stringHelper.IsEmpty(recShortName)) {
			// find recShortName
			masterReceiverUnits = masterReceiverUnitRepository.findByRecShortNameEnAndMasterReceiverType(recShortName,
					masterReceiverType);
		} else if (!stringHelper.IsEmpty(status)) {
			// find status
			masterReceiverUnits = masterReceiverUnitRepository.findByMasterReceiverTypeAndMasterStatus(
					masterReceiverType, masterStatus);
		} else if (!stringHelper.IsEmpty(String.valueOf(receiverType))) {
			// find recCeiverType
			masterReceiverUnits = masterReceiverUnitRepository
					.findByMasterReceiverType(masterReceiverType);
		} else {
			masterReceiverUnits = masterReceiverUnitRepository.findAll();
		}
		return masterReceiverUnits;
	}

	public List<MasterReceiverUnit> search(String recCode, String recShortNameTh, String recShortNameEn,
			int receiverType, String status) {
		String query = OrmXmlManager.getQuery("searchMasterReceiverUnit");
		Map<String, Object> params = new HashMap<String, Object>();
		if (!StringUtils.isEmpty(recCode)) {
			query += " AND m.REC_CODE = :recCode";
			params.put("recCode", recCode);
		}
		if (!StringUtils.isEmpty(recShortNameTh)) {
			query += " AND m.REC_SHORT_NAME_TH = :recShortNameTh";
			params.put("recShortNameTh", recShortNameTh);
		}
		if (!StringUtils.isEmpty(recShortNameEn)) {
			query += " AND m.REC_SHORT_NAME_EN = :recShortNameEn";
			params.put("recShortNameEn", recShortNameEn);
		}
		if (receiverType >= 0) {
			String receiverTypeStr = "" + receiverType; 
			query += " AND m.REC_TYPE = :receiverType";
			params.put("receiverType", receiverTypeStr);
		}
		if (!StringUtils.isEmpty(status)) {
			query += " AND m.STATUS = :status";
			params.put("status", status);
		}
		List<MasterReceiverUnit> searchResults = simpleDataTableRepository.getList(query, params, MasterReceiverUnit.class);
		List<MasterReceiverUnit> masterReceiverUnits = new ArrayList<>();
		for (MasterReceiverUnit e : searchResults) {
			Optional<MasterReceiverUnit> masterReceiverUnitOpt = masterReceiverUnitRepository.findById(e.getMasterReceiverUnitId());
			if (masterReceiverUnitOpt.isPresent()) {
				masterReceiverUnits.add(masterReceiverUnitOpt.get());
			}
		}
		return masterReceiverUnits;
	}
	
	public List<MasterReceiverUnitListDto> SearchReceiverUnitAll(String lang, RestManager exManager){
		List<MasterReceiverUnit> MasterReceiverUnitLst = masterReceiverUnitRepository.findAll();
		if(MasterReceiverUnitLst.size() <= 0) {
			exManager.addGlobalErrorbyProperty("app.man-resp.data-not-found");
			exManager.throwsException();
		}
		List<MasterReceiverUnitListDto> masterReceiverUnitListDtoLst = new ArrayList<>();
		for(MasterReceiverUnit masterReceiverUnit : MasterReceiverUnitLst) {
			MasterReceiverUnitListDto masterReceiverUnitListDto = new MasterReceiverUnitListDto();
			masterReceiverUnitListDto.setMasterReceiverUnitId(masterReceiverUnit.getMasterReceiverUnitId());
			masterReceiverUnitListDto.setRecCode(masterReceiverUnit.getRecCode());
			if(lang.equals(langEn)){
				masterReceiverUnitListDto.setRecName(masterReceiverUnit.getRecNameEn());
				masterReceiverUnitListDto.setRecShortName(masterReceiverUnit.getRecShortNameEn());
				masterReceiverUnitListDto.setStatus(masterReceiverUnit.getMasterStatus().getStatusNameEn());
			}
			else if (lang.equals(langTh)){
				masterReceiverUnitListDto.setRecName(masterReceiverUnit.getRecNameTh());
				masterReceiverUnitListDto.setRecShortName(masterReceiverUnit.getRecShortNameTh());
				masterReceiverUnitListDto.setStatus(masterReceiverUnit.getMasterStatus().getStatusNameTh());
			}
			masterReceiverUnitListDtoLst.add(masterReceiverUnitListDto);
		}
		return masterReceiverUnitListDtoLst;
	}
}
