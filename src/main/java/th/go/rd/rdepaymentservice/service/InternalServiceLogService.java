package th.go.rd.rdepaymentservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import th.go.rd.rdepaymentservice.dto.InternalServiceLogDto;
import th.go.rd.rdepaymentservice.entity.InternalServiceLog;
import th.go.rd.rdepaymentservice.model.EpayLogModel;
import th.go.rd.rdepaymentservice.repository.InternalServiceLogRepository;

@Service
@Transactional
public class InternalServiceLogService {

    @Autowired
    InternalServiceLogRepository internalServiceLogRepository;

    public List<InternalServiceLog> findAll(){
        return internalServiceLogRepository.findAll();
    }

    public Optional<InternalServiceLog> findById(long id){
        return internalServiceLogRepository.findById(id);
    }

    public void save(EpayLogModel log){
        ModelMapper modelMapper = new ModelMapper();
        InternalServiceLog entity = modelMapper.map(log,InternalServiceLog.class);
        this.save(entity);
    }

    public void save(InternalServiceLog entity){
        internalServiceLogRepository.save(entity);
    }

    public void delete(long id){
        internalServiceLogRepository.deleteById(id);
    }

    public List<InternalServiceLogDto> convertToDto(List<InternalServiceLog> internalServiceLogs){
        ModelMapper modelMapper = new ModelMapper();
        List<InternalServiceLogDto> internalServiceLogDtos = new ArrayList<>();
        for (InternalServiceLog internalServiceLog : internalServiceLogs) {
            InternalServiceLogDto internalServiceLogDto = modelMapper.map(internalServiceLog, InternalServiceLogDto.class);
            internalServiceLogDtos.add(internalServiceLogDto);
        }
        return internalServiceLogDtos;
    }

    public InternalServiceLogDto convertToDto(InternalServiceLog internalServiceLog){
        ModelMapper modelMapper = new ModelMapper();
        InternalServiceLogDto internalServiceLogDto = modelMapper.map(internalServiceLog, InternalServiceLogDto.class);
        return internalServiceLogDto;
    }
}
