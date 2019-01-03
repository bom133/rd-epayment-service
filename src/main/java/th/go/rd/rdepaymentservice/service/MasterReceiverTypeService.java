package th.go.rd.rdepaymentservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import th.go.rd.rdepaymentservice.dto.MasterReceiverTypeDto;
import th.go.rd.rdepaymentservice.dto.MasterStatusDto;
import th.go.rd.rdepaymentservice.entity.MasterReceiverType;
import th.go.rd.rdepaymentservice.entity.MasterStatus;
import th.go.rd.rdepaymentservice.repository.MasterReceiverTypeRepository;

@Service
@Transactional
public class MasterReceiverTypeService {
	@Autowired
	MasterReceiverTypeRepository masterReceiverTypeRepository;

	public Optional<MasterReceiverType> findByReceiverType(int receiverType) {
		return masterReceiverTypeRepository.findByReceiverType(receiverType);
	}

	public List<MasterReceiverType> findAll() {
		return masterReceiverTypeRepository.findAll();
	}
	
	public List<MasterReceiverTypeDto> convertToDto(List<MasterReceiverType> masterReceiverTypes){
        ModelMapper modelMapper = new ModelMapper();
        List<MasterReceiverTypeDto> masterReceiverTypeDtos = new ArrayList<>();
        for (MasterReceiverType masterReceiverType : masterReceiverTypes) {
        	MasterReceiverTypeDto masterReceiverTypeDto = modelMapper.map(masterReceiverType, MasterReceiverTypeDto.class);
        	 masterReceiverTypeDtos.add(masterReceiverTypeDto);
            
        }
        return masterReceiverTypeDtos;
	}
	
	public MasterReceiverTypeDto convertToDto(MasterReceiverType masterReceiverType){
		ModelMapper modelMapper = new ModelMapper();
		MasterReceiverTypeDto masterReceiverTypeDto = modelMapper.map(masterReceiverType, MasterReceiverTypeDto.class);
		return masterReceiverTypeDto;
	}
}
