package th.go.rd.rdepaymentservice.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import th.go.rd.rdepaymentservice.entity.EpayTaxPaymentInfo;
import th.go.rd.rdepaymentservice.entity.EpayTaxPaymentOutbound;
import th.go.rd.rdepaymentservice.repository.EpayTaxPaymentOutboundRepository;

@Service
public class EpayTaxPaymentOutBoundService {

	@Autowired
	EpayTaxPaymentOutboundRepository epayTaxPaymentOutboundRepository;
	
	public Optional<EpayTaxPaymentOutbound> findByUuid(String uuid) {
		return epayTaxPaymentOutboundRepository.findByUuid(uuid);
	}
	
	public Optional<EpayTaxPaymentOutbound> findByCtrlCode(String ctlCode) {
		return epayTaxPaymentOutboundRepository.findByCtlCode(ctlCode);
	}
	
	public Optional<EpayTaxPaymentOutbound> findByEpayTaxPaymentInfo(EpayTaxPaymentInfo epayTaxPaymentInfo){
		return epayTaxPaymentOutboundRepository.findByEpayTaxPaymentInfo(epayTaxPaymentInfo);
	}
	
	public String getRunNoTransaction() {
		return epayTaxPaymentOutboundRepository.getRunNoTransaction();
	}
	
	public String getRunNoFileXml() {
		return epayTaxPaymentOutboundRepository.getRunNoFileXml();
	}
	
	public void save(EpayTaxPaymentOutbound epayTaxPaymentOutbound) {
		epayTaxPaymentOutboundRepository.save(epayTaxPaymentOutbound);
	}
}
