package th.go.rd.rdepaymentservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import th.go.rd.rdepaymentservice.dto.MasterStatusDto;
import th.go.rd.rdepaymentservice.entity.MasterStatus;
import th.go.rd.rdepaymentservice.repository.MasterStatusRepository;

@Service
@Transactional
public class MasterStatusService {

    @Autowired
    MasterStatusRepository masterStatusRepository;

    public List<MasterStatus> findAll(){
        return masterStatusRepository.findAll();
    }

    public Optional<MasterStatus> findById(String id){
        return masterStatusRepository.findById(id);
    }
    
    public Optional<MasterStatus> findByStatus(String status){
    	return masterStatusRepository.findByStatus(status);
    }

    public List<MasterStatusDto> convertToDto(List<MasterStatus> masterStatuss){
        ModelMapper modelMapper = new ModelMapper();
        List<MasterStatusDto> masterStatusDtos = new ArrayList<>();
        for (MasterStatus masterStatus : masterStatuss) {
            MasterStatusDto masterStatusDto = modelMapper.map(masterStatus, MasterStatusDto.class);
            masterStatusDtos.add(masterStatusDto);
            
        }
        return masterStatusDtos;
    }

    public MasterStatusDto convertToDto(MasterStatus masterStatus){
        ModelMapper modelMapper = new ModelMapper();
        MasterStatusDto masterStatusDto = modelMapper.map(masterStatus, MasterStatusDto.class);
        return masterStatusDto;
    }
}
