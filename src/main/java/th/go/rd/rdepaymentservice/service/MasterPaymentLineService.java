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

import th.go.rd.rdepaymentservice.dto.MasterPaymentLineDto;
import th.go.rd.rdepaymentservice.dto.MasterPaymentLineSearchDto;
import th.go.rd.rdepaymentservice.dto.MasterStatusDto;
import th.go.rd.rdepaymentservice.entity.MasterPaymentLine;
import th.go.rd.rdepaymentservice.manager.OrmXmlManager;
import th.go.rd.rdepaymentservice.manager.RestManager;
import th.go.rd.rdepaymentservice.model.DataTable;
import th.go.rd.rdepaymentservice.model.PagingModel;
import th.go.rd.rdepaymentservice.repository.MasterPaymentLineRepository;
import th.go.rd.rdepaymentservice.repository.MasterStatusRepository;
import th.go.rd.rdepaymentservice.repository.SimpleDataTableRepository;

@Service
@Transactional
public class MasterPaymentLineService {

    @Autowired
    MasterPaymentLineRepository masterPaymentLineRepository;

    @Autowired
    MasterStatusRepository masterStatusRepository;
    
    @Autowired
	private SimpleDataTableRepository simpleDataTableRepository;
    
    private final String langEn = "EN";
	private final String langTh = "TH";

    public List<MasterPaymentLineSearchDto> SearchPaymentLineList(String lang, RestManager exManager) {
    	List<MasterPaymentLine> masterPaymentLineLst = masterPaymentLineRepository.findAll();
    	if(masterPaymentLineLst.size() <= 0)
		{
			exManager.addGlobalErrorbyProperty("app.man-resp.data-not-found");
			exManager.throwsException();
		}
    	List<MasterPaymentLineSearchDto> masterPaymentLineSearchDtoLst = new ArrayList<>();
    	for(MasterPaymentLine masterPaymentLine : masterPaymentLineLst)
		{
    		MasterPaymentLineSearchDto masterPaymentLineSearchDto = new MasterPaymentLineSearchDto();
    		masterPaymentLineSearchDto.setPaymentLineId(masterPaymentLine.getMasterPaymentLineId());
    		masterPaymentLineSearchDto.setPayLineCode(masterPaymentLine.getPayLineCode());
			if(lang.equals(langEn)){
				masterPaymentLineSearchDto.setPayLineName(masterPaymentLine.getPayLineNameEn());
				masterPaymentLineSearchDto.setStatus(masterPaymentLine.getMasterStatus().getStatusNameEn());
			}
			else if (lang.equals(langTh)){
				masterPaymentLineSearchDto.setPayLineName(masterPaymentLine.getPayLineNameTh());
				masterPaymentLineSearchDto.setStatus(masterPaymentLine.getMasterStatus().getStatusNameTh());
			}
			masterPaymentLineSearchDtoLst.add(masterPaymentLineSearchDto);
		}
    	
        return masterPaymentLineSearchDtoLst;
    }

    public Optional<MasterPaymentLine> findById(long id) {
        return masterPaymentLineRepository.findById(id);
    }

    public List<MasterPaymentLineDto> convertToDto(List<MasterPaymentLine> masterPaymentLines) {
        ModelMapper modelMapper = new ModelMapper();
        List<MasterPaymentLineDto> masterPaymentLineDtos = new ArrayList<>();
        for (MasterPaymentLine masterPaymentLine : masterPaymentLines) {
            MasterPaymentLineDto masterPaymentLineDto = modelMapper.map(masterPaymentLine, MasterPaymentLineDto.class);
            MasterStatusDto masterStatusDto = modelMapper.map(masterPaymentLine.getMasterStatus(),
                    MasterStatusDto.class);
            masterPaymentLineDto.setMasterStatusDto(masterStatusDto);
            masterPaymentLineDtos.add(masterPaymentLineDto);
        }

        return masterPaymentLineDtos;
    }

    public MasterPaymentLineDto convertToDto(MasterPaymentLine masterPaymentLine) {
        ModelMapper modelMapper = new ModelMapper();
        MasterPaymentLineDto masterPaymentLineDto = modelMapper.map(masterPaymentLine, MasterPaymentLineDto.class);
        MasterStatusDto masterStatusDto = modelMapper.map(masterPaymentLine.getMasterStatus(), MasterStatusDto.class);
        masterPaymentLineDto.setMasterStatusDto(masterStatusDto);
        return masterPaymentLineDto;
    }

    public void delete(long id) {
        masterPaymentLineRepository.deleteById(id);
    }

    public DataTable<MasterPaymentLineSearchDto> SearchPaymentLineByCriteria(PagingModel page, String lang, String payLineCode, String status ) {
    	String query = OrmXmlManager.getQuery("findPaymentLineByCriteria");
		Map<String, Object> params = new HashMap<String, Object>();
		if(!StringUtils.isEmpty(lang)) {
			if(lang.equals(langEn)) {
				query += " CONCAT(p.PAY_LINE_NAME_EN,'') AS payLineName, CONCAT(m.STATUS_NAME_EN,'') AS status ";
			}
			else if(lang.equals(langTh)) {
				query += " CONCAT(p.PAY_LINE_NAME_TH,'') AS payLineName, CONCAT(m.STATUS_NAME_TH,'') AS status ";
			}
		}
		query += " FROM EFEPAY_DEV.MASTER_PAYMENT_LINE p, EFEPAY_DEV.MASTER_STATUS m WHERE p.STATUS = m.STATUS ";		
		if (!StringUtils.isEmpty(payLineCode)) {
			query += " AND p.PAY_LINE_CODE = :payLineCode ";
			params.put("payLineCode",payLineCode);
		}
		if (!StringUtils.isEmpty(status)) {
			query += " AND p.STATUS = :status";
			params.put("status",status.toUpperCase());
		}
		query += " ORDER BY p.PAY_LINE_CODE ";
		DataTable<MasterPaymentLineSearchDto> pagingData = simpleDataTableRepository.getPagingData(query, page, params,
				MasterPaymentLineSearchDto.class);
		return pagingData;
       
    }
}
