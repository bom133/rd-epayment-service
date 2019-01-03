package th.go.rd.rdepaymentservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import th.go.rd.rdepaymentservice.dto.PaymentLogDto;
import th.go.rd.rdepaymentservice.entity.PaymentLog;
import th.go.rd.rdepaymentservice.model.EpayLogModel;
import th.go.rd.rdepaymentservice.repository.PaymentLogRepository;

@Service
@Transactional
public class PaymentLogService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentLogService.class);

    @Autowired
    PaymentLogRepository paymentLogRepository;

    public List<PaymentLog> findAll() {
        return paymentLogRepository.findAll();
    }

    public Optional<PaymentLog> findById(long id) {
        return paymentLogRepository.findById(id);
    }

    public void save(EpayLogModel log) {
        ModelMapper modelMapper = new ModelMapper();
        PaymentLog entity = modelMapper.map(log, PaymentLog.class);
        this.save(entity);
    }

    public void save(PaymentLog entity) {
        paymentLogRepository.save(entity);
    }

    public void delete(long id) {
        paymentLogRepository.deleteById(id);
    }

    public List<PaymentLogDto> convertToDto(List<PaymentLog> paymentLogs) {
        ModelMapper modelMapper = new ModelMapper();
        List<PaymentLogDto> paymentLogDtos = new ArrayList<>();
        for (PaymentLog paymentLog : paymentLogs) {
            PaymentLogDto paymentLogDto = modelMapper.map(paymentLog, PaymentLogDto.class);
            paymentLogDtos.add(paymentLogDto);
        }
        return paymentLogDtos;
    }

    public PaymentLogDto convertToDto(PaymentLog paymentLog) {
        ModelMapper modelMapper = new ModelMapper();
        PaymentLogDto paymentLogDto = modelMapper.map(paymentLog, PaymentLogDto.class);
        return paymentLogDto;
    }
}
