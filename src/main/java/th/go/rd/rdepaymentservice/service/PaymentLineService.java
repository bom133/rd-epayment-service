package th.go.rd.rdepaymentservice.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import th.go.rd.rdepaymentservice.entity.EpayTaxPaymentInfo;
import th.go.rd.rdepaymentservice.entity.EpayTaxPaymentOutbound;
import th.go.rd.rdepaymentservice.repository.EpayTaxPaymentInfoRepository;
import th.go.rd.rdepaymentservice.repository.EpayTaxPaymentOutboundRepository;
import th.go.rd.rdepaymentservice.repository.MasterPaymentLineRepository;

@Service
@Transactional
public class PaymentLineService {

	// @Autowired
	// EpayReceiverPaymentLineRepository paymentLineRepo;

	@Autowired
	EpayTaxPaymentOutboundRepository paymentOutboundRepo;

	// @Autowired
	// MasterReceiverUnitRepository masterReceiverUnitRepository;

	 @Autowired
     MasterPaymentLineRepository masterPaymentLineRepository;

	@Autowired
	EpayTaxPaymentInfoRepository epayTaxPaymentInfoRepository;

	public Optional<EpayTaxPaymentOutbound> findByTranNo(String tranNo) {
		return paymentOutboundRepo.findByTranNo(tranNo);
	}

	public EpayTaxPaymentInfo findByEpayTaxPaymentInfoId(long infoId) {
		return epayTaxPaymentInfoRepository.findByEpayTaxPaymentInfoId(infoId);
	}
}
