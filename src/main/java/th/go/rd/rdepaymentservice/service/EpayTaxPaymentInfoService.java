package th.go.rd.rdepaymentservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import th.go.rd.rdepaymentservice.dto.EpayTaxPaymentInboundDto;
import th.go.rd.rdepaymentservice.dto.EpayTaxPaymentInfoDto;
import th.go.rd.rdepaymentservice.dto.MasterStatusDto;
import th.go.rd.rdepaymentservice.entity.EpayTaxPaymentInfo;
import th.go.rd.rdepaymentservice.entity.MasterStatus;
import th.go.rd.rdepaymentservice.repository.EpayTaxPaymentInfoRepository;

@Service
@Transactional
public class EpayTaxPaymentInfoService {

	@Autowired
	EpayTaxPaymentInfoRepository epayTaxPaymentInfoRepository;

	public List<EpayTaxPaymentInfo> findAll(){
		return epayTaxPaymentInfoRepository.findAll();
	}

	public Optional<EpayTaxPaymentInfo> findById(long id){
		return epayTaxPaymentInfoRepository.findById(id);
	}

	public void save(EpayTaxPaymentInfo entity){
		epayTaxPaymentInfoRepository.save(entity);
	}

	public void delete(long id){
		epayTaxPaymentInfoRepository.deleteById(id);
	}

	public List<EpayTaxPaymentInfoDto> convertToDto(List<EpayTaxPaymentInfo> epayTaxPaymentInfos){
		ModelMapper modelMapper = new ModelMapper();
		List<EpayTaxPaymentInfoDto> epayTaxPaymentInfoDtos = new ArrayList<>();
		for (EpayTaxPaymentInfo epayTaxPaymentInfo : epayTaxPaymentInfos) {
			EpayTaxPaymentInfoDto epayTaxPaymentInfoDto = modelMapper.map(epayTaxPaymentInfo,EpayTaxPaymentInfoDto.class);
			epayTaxPaymentInfoDto.setMasterStatusDto(modelMapper.map(epayTaxPaymentInfo.getMasterStatus(),MasterStatusDto.class));
			epayTaxPaymentInfoDto.setEpayTaxPaymentInboundDto(modelMapper.map(epayTaxPaymentInfo.getEpayTaxPaymentInbound(), EpayTaxPaymentInboundDto.class));
			epayTaxPaymentInfoDtos.add(epayTaxPaymentInfoDto);
		}
		return epayTaxPaymentInfoDtos;
	}

	public EpayTaxPaymentInfoDto convertToDto(EpayTaxPaymentInfo epayTaxPaymentInfo){
		ModelMapper modelMapper = new ModelMapper();
		EpayTaxPaymentInfoDto epayTaxPaymentInfoDto = modelMapper.map(epayTaxPaymentInfo,EpayTaxPaymentInfoDto.class);
			epayTaxPaymentInfoDto.setMasterStatusDto(modelMapper.map(epayTaxPaymentInfo.getMasterStatus(),MasterStatusDto.class));
			epayTaxPaymentInfoDto.setEpayTaxPaymentInboundDto(modelMapper.map(epayTaxPaymentInfo.getEpayTaxPaymentInbound(), EpayTaxPaymentInboundDto.class));
		return epayTaxPaymentInfoDto;
	}
	
	public Optional<EpayTaxPaymentInfo> findByRefNoAndMasterStatusAndIsRound(String refNo, MasterStatus masterStatus, String isRound) {
		Optional<EpayTaxPaymentInfo> taxInfo = epayTaxPaymentInfoRepository.findByRefNoAndMasterStatusAndIsRound(refNo, masterStatus, isRound.charAt(0));
		return taxInfo;
	}

	public Optional<EpayTaxPaymentInfo> findByRefNo(String refNo) {
		Optional<EpayTaxPaymentInfo> taxInfoOpt = epayTaxPaymentInfoRepository.findByRefNo(refNo);
		return taxInfoOpt;
	}

	public Optional<EpayTaxPaymentInfo> findByRefNoAndMasterStatus(String refNo, MasterStatus masterStatus) {
		Optional<EpayTaxPaymentInfo> taxInfoOpt = epayTaxPaymentInfoRepository.findByRefNoAndMasterStatus(refNo, masterStatus);
		return taxInfoOpt;
	}
}
