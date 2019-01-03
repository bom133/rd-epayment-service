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

import th.go.rd.rdepaymentservice.dto.EpayRecPayLineDto;
import th.go.rd.rdepaymentservice.dto.EpayReceiverPaymentLineDto;
import th.go.rd.rdepaymentservice.dto.MasterPaymentLineDto;
import th.go.rd.rdepaymentservice.dto.MasterReceiverUnitDto;
import th.go.rd.rdepaymentservice.dto.MasterStatusDto;
import th.go.rd.rdepaymentservice.entity.EpayReceiverPaymentLine;
import th.go.rd.rdepaymentservice.entity.MasterPaymentLine;
import th.go.rd.rdepaymentservice.entity.MasterReceiverUnit;
import th.go.rd.rdepaymentservice.manager.OrmXmlManager;
import th.go.rd.rdepaymentservice.model.DataTable;
import th.go.rd.rdepaymentservice.model.PagingModel;
import th.go.rd.rdepaymentservice.repository.EpayReceiverPaymentLineRepository;
import th.go.rd.rdepaymentservice.repository.MasterPaymentLineRepository;
import th.go.rd.rdepaymentservice.repository.MasterReceiverUnitRepository;
import th.go.rd.rdepaymentservice.repository.MasterStatusRepository;
import th.go.rd.rdepaymentservice.repository.SimpleDataTableRepository;

@Service
@Transactional
public class EpayReceiverPaymentLineService {

	@Autowired
	EpayReceiverPaymentLineRepository epayReceiverPaymentLineRepository;

	@Autowired
	MasterPaymentLineRepository masterPaymentLineRepository;

	@Autowired
	MasterReceiverUnitRepository masterReceiverUntiRepository;

	@Autowired
	MasterStatusRepository masterStatusRepository;
	
	@Autowired
	private SimpleDataTableRepository simpleDataTableRepository;

	private final String langEn = "EN";
	private final String langTh = "TH";
	public List<EpayReceiverPaymentLine> findEpayReceiver(String paymentLine, String bankCode) {

		Optional<MasterPaymentLine> masterPaymentLine = masterPaymentLineRepository.findByPayLineCode(paymentLine);

		Optional<MasterReceiverUnit> masterReceiverUnit = masterReceiverUntiRepository.findByRecCode(bankCode);
		if (masterPaymentLine.isPresent() && masterReceiverUnit.isPresent()) {

			List<EpayReceiverPaymentLine> epayReceiverPaymentLine = epayReceiverPaymentLineRepository
					.findByMasterPaymentLineAndMasterReceiverUnit(masterPaymentLine.get(), masterReceiverUnit.get());
			return epayReceiverPaymentLine;
		} else {
			return new ArrayList<>();
		}
	}

	public void save(EpayReceiverPaymentLine entity) {
		epayReceiverPaymentLineRepository.save(entity);
	}

	public void delete(long id) {
		epayReceiverPaymentLineRepository.deleteById(id);
	}

	public Optional<EpayReceiverPaymentLine> findById(long id) {
		return epayReceiverPaymentLineRepository.findById(id);
	}

	public List<EpayReceiverPaymentLine> findAll() {
		return epayReceiverPaymentLineRepository.findAll();
	}

	public DataTable<EpayRecPayLineDto> search(String lang, PagingModel page, String paymentLineId, String receiverUnitId, String statusId) {		
		String query = OrmXmlManager.getQuery("findEpayRecPayLineByCriteria");
		Map<String, Object> params = new HashMap<String, Object>();	
		if(!StringUtils.isEmpty(lang)) {
			if(lang.equals(langEn)) {
				query += " CONCAT(u.REC_SHORT_NAME_EN,'') AS recName, CONCAT(m.STATUS_NAME_EN,'') AS status ";
			}
			else if(lang.equals(langTh)) {
				query += " CONCAT(u.REC_SHORT_NAME_TH,'') AS recName, CONCAT(m.STATUS_NAME_TH,'') AS status";
			}
		}
		query += " FROM EFEPAY_DEV.EPAY_RECEIVER_PAYMENT_LINE e, EFEPAY_DEV.MASTER_PAYMENT_LINE p, EFEPAY_DEV.MASTER_RECEIVER_UNIT u, EFEPAY_DEV.MASTER_STATUS m" + 
				" WHERE e.MASTER_PAYMENT_LINE_ID = p.MASTER_PAYMENT_LINE_ID AND e.MASTER_RECEIVER_UNIT_ID = u.MASTER_RECEIVER_UNIT_ID AND e.STATUS = m.STATUS ";
		if (!StringUtils.isEmpty(paymentLineId)) {
			query += " AND p.MASTER_PAYMENT_LINE_ID = :paymentLineId ";
			params.put("paymentLineId",paymentLineId);
		}
		if (!StringUtils.isEmpty(receiverUnitId)) {
			query += " AND e.MASTER_RECEIVER_UNIT_ID = :receiverUnitId ";
			params.put("receiverUnitId",receiverUnitId);
		}
		if (!StringUtils.isEmpty(statusId)) {
			query += " AND e.STATUS = :statusId ";
			params.put("statusId",statusId.toUpperCase());
		}
		DataTable<EpayRecPayLineDto> pagingData = simpleDataTableRepository.getPagingData(query, page, params,
				EpayRecPayLineDto.class);
		return pagingData;

	}

	public List<EpayReceiverPaymentLine> findByMasterPaymentLineAndMasterReceiverUnit(MasterPaymentLine masterPaymentLine, MasterReceiverUnit masterReceiverUnit){
		return epayReceiverPaymentLineRepository.findByMasterPaymentLineAndMasterReceiverUnit(masterPaymentLine, masterReceiverUnit);
	}

	public List<EpayReceiverPaymentLineDto> convertToDto(List<EpayReceiverPaymentLine> epayReceiverPaymentLines) {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setAmbiguityIgnored(true);
		List<EpayReceiverPaymentLineDto> epayReceiverPaymentLineDtos = new ArrayList<>();
		for (EpayReceiverPaymentLine epayReceiverPaymentLine : epayReceiverPaymentLines) {
			EpayReceiverPaymentLineDto epayReceiverPaymentLineDto = modelMapper.map(epayReceiverPaymentLine,EpayReceiverPaymentLineDto.class);
			epayReceiverPaymentLineDto.setMasterStatusDto(modelMapper.map(epayReceiverPaymentLine.getMasterStatus(),MasterStatusDto.class));
			epayReceiverPaymentLineDto.setMasterPaymentLineDto(modelMapper.map(epayReceiverPaymentLine.getMasterPaymentLine(),MasterPaymentLineDto.class));
			epayReceiverPaymentLineDto.setMasterReceiverUnitDto(modelMapper.map(epayReceiverPaymentLine.getMasterReceiverUnit(),MasterReceiverUnitDto.class));
			epayReceiverPaymentLineDtos.add(epayReceiverPaymentLineDto);
		}
		
		return epayReceiverPaymentLineDtos;
	}
	
	public EpayReceiverPaymentLineDto convertToDto(EpayReceiverPaymentLine epayReceiverPaymentLine) {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setAmbiguityIgnored(true);
		EpayReceiverPaymentLineDto epayReceiverPaymentLineDto = modelMapper.map(epayReceiverPaymentLine,EpayReceiverPaymentLineDto.class);
		epayReceiverPaymentLineDto.setMasterStatusDto(modelMapper.map(epayReceiverPaymentLine.getMasterStatus(),MasterStatusDto.class));
		epayReceiverPaymentLineDto.setMasterPaymentLineDto(modelMapper.map(epayReceiverPaymentLine.getMasterPaymentLine(),MasterPaymentLineDto.class));
		epayReceiverPaymentLineDto.setMasterReceiverUnitDto(modelMapper.map(epayReceiverPaymentLine.getMasterReceiverUnit(),MasterReceiverUnitDto.class));
		return epayReceiverPaymentLineDto;
	}

}
