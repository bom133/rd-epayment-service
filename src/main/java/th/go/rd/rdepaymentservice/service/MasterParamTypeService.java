package th.go.rd.rdepaymentservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import th.go.rd.rdepaymentservice.dto.MasterParamTypeDto;
import th.go.rd.rdepaymentservice.entity.MasterParamType;
import th.go.rd.rdepaymentservice.repository.MasterParamTypeRepository;

@Service
@Transactional
public class MasterParamTypeService {

    @Autowired
    MasterParamTypeRepository masterParamTypeRepository;

    public List<MasterParamType> findAll(){
        return masterParamTypeRepository.findAll();
    }

	public Optional<MasterParamType> findById(String paramType) {
         return masterParamTypeRepository.findById(paramType);
    }
    
    public List<MasterParamTypeDto> convertToDto(List<MasterParamType> masterParamTypes){
        ModelMapper modelMapper = new ModelMapper();
        List<MasterParamTypeDto> paramTypeDtos = new ArrayList<>();
        for (MasterParamType masterParamType : masterParamTypes) {
            MasterParamTypeDto paramTypeDto = modelMapper.map(masterParamType,MasterParamTypeDto.class);
            paramTypeDtos.add(paramTypeDto);

        }
        return paramTypeDtos;
    }

    public MasterParamTypeDto convertToDto(MasterParamType masterParamType){
        ModelMapper modelMapper = new ModelMapper();
        MasterParamTypeDto paramTypeDto = modelMapper.map(masterParamType,MasterParamTypeDto.class);
        return paramTypeDto;
    }
}
