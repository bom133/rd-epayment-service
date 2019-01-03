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

import th.go.rd.rdepaymentservice.dto.EpayParameterDto;
import th.go.rd.rdepaymentservice.dto.EpayParameterSearchDto;
import th.go.rd.rdepaymentservice.dto.MasterParamTypeDto;
import th.go.rd.rdepaymentservice.dto.MasterPaymentLineSearchDto;
import th.go.rd.rdepaymentservice.entity.EpayParameter;
import th.go.rd.rdepaymentservice.entity.MasterParamType;
import th.go.rd.rdepaymentservice.manager.OrmXmlManager;
import th.go.rd.rdepaymentservice.model.DataTable;
import th.go.rd.rdepaymentservice.model.PagingModel;
import th.go.rd.rdepaymentservice.repository.EpayParameterRepository;
import th.go.rd.rdepaymentservice.repository.MasterParamTypeRepository;
import th.go.rd.rdepaymentservice.repository.SimpleDataTableRepository;

@Service
@Transactional
public class EpayParameterService {

    @Autowired
    EpayParameterRepository epayParameterRepository;

    @Autowired
    MasterParamTypeRepository masterParamTypeRepository;
    
    @Autowired
   	private SimpleDataTableRepository simpleDataTableRepository;

    private final String langEn = "EN";
	private final String langTh = "TH";
    
    public List<EpayParameter> findAll() {
        return epayParameterRepository.findAll();
    }

    public Optional<EpayParameter> findById(long id) {
        return epayParameterRepository.findById(id);
    }

    public void save(EpayParameter entity) {
        epayParameterRepository.save(entity);
    }

    public void delete(long id) {
        epayParameterRepository.deleteById(id);
    }

    public List<EpayParameter> findByParamCode(String paramCode) {
        return epayParameterRepository.findByParamCodeContaining(paramCode);
    }

    public List<EpayParameter> findByParamType(String paramType) {
        Optional<MasterParamType> masterParamType = masterParamTypeRepository.findById(paramType);
        if (masterParamType.isPresent()) {
            return epayParameterRepository.findByMasterParamType(masterParamType.get());
        } else {
            return new ArrayList<>();
        }
    }

    public List<EpayParameter> findByParamTypeAndParamCode(String paramType, String paramCode) {
        Optional<MasterParamType> masterParamType = masterParamTypeRepository.findById(paramType);
        if (masterParamType.isPresent()) {
            return epayParameterRepository.findByMasterParamTypeAndParamCodeContaining(masterParamType.get(), paramCode);
        } else {
            return new ArrayList<>();
        }
    }

    public List<EpayParameterDto> convertToDto(List<EpayParameter> epayParameters) {
        ModelMapper modelMapper = new ModelMapper();
        List<EpayParameterDto> epayParameterDtos = new ArrayList<>();
        for (EpayParameter epayParameter : epayParameters) {
            EpayParameterDto epayParameterDto = modelMapper.map(epayParameter, EpayParameterDto.class);
            MasterParamTypeDto masterParamTypeDto = modelMapper.map(epayParameter.getMasterParamType(),
                    MasterParamTypeDto.class);
            epayParameterDto.setMasterParamTypeDto(masterParamTypeDto);
            epayParameterDtos.add(epayParameterDto);

        }
        return epayParameterDtos;
    }

    public EpayParameterDto convertToDto(EpayParameter epayParameter) {
        ModelMapper modelMapper = new ModelMapper();
        EpayParameterDto epayParameterDto = modelMapper.map(epayParameter, EpayParameterDto.class);
        MasterParamTypeDto masterParamTypeDto = modelMapper.map(epayParameter.getMasterParamType(),
                MasterParamTypeDto.class);
        epayParameterDto.setMasterParamTypeDto(masterParamTypeDto);
        return epayParameterDto;
    }

    public DataTable<EpayParameterSearchDto> SearchEpayParameterByCriteria(PagingModel page, String lang, String paramType, String paramCode) {
    	String query = OrmXmlManager.getQuery("findEpayParameterByCriteria");
		Map<String, Object> params = new HashMap<String, Object>();	
		if (!StringUtils.isEmpty(paramType)) {
			query += " AND PARAM_TYPE = :paramType ";
			params.put("paramType",paramType);
		}
		if (!StringUtils.isEmpty(paramCode)) {
			query += " AND PARAM_CODE like :paramCode ";
			params.put("paramCode","%"+paramCode+"%");
		}
		if ((page.getSortField().size()==0) &&
			(page.getSortOrder().size()==0)) { 
			query += " ORDER BY PARAM_CODE ";
		}
		DataTable<EpayParameterSearchDto> pagingData = simpleDataTableRepository.getPagingData(query, page, params,
				EpayParameterSearchDto.class);
		return pagingData;
    }
}
