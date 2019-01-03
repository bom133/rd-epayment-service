package th.go.rd.rdepaymentservice.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import th.go.rd.rdepaymentservice.entity.EpayReceiverPaymentLine;
import th.go.rd.rdepaymentservice.entity.EpayTaxPaymentInbound;
import th.go.rd.rdepaymentservice.entity.EpayTaxPaymentInboundRefOutbound;
import th.go.rd.rdepaymentservice.entity.EpayTaxPaymentInfo;
import th.go.rd.rdepaymentservice.entity.EpayTaxPaymentOutbound;
import th.go.rd.rdepaymentservice.entity.MasterPaymentLine;
import th.go.rd.rdepaymentservice.entity.MasterPaymentStatus;
import th.go.rd.rdepaymentservice.entity.MasterReceiverUnit;
import th.go.rd.rdepaymentservice.entity.MasterSendMethod;
import th.go.rd.rdepaymentservice.repository.EpayReceiverPaymentLineRepository;
import th.go.rd.rdepaymentservice.repository.EpayTaxPaymentInboundRefOutboundRepository;
import th.go.rd.rdepaymentservice.repository.EpayTaxPaymentInboundRepository;
import th.go.rd.rdepaymentservice.repository.EpayTaxPaymentInfoRepository;
import th.go.rd.rdepaymentservice.repository.EpayTaxPaymentOutboundRepository;
import th.go.rd.rdepaymentservice.repository.MasterPaymentLineRepository;
import th.go.rd.rdepaymentservice.repository.MasterPaymentStatusRepository;
import th.go.rd.rdepaymentservice.repository.MasterReceiverUnitRepository;
import th.go.rd.rdepaymentservice.repository.MasterSendMethodRepository;

@Service
@Transactional
public class PaymentInService {

	@Autowired
	EpayTaxPaymentOutboundRepository paymentOutboundRepo;

	 @Autowired
     MasterPaymentLineRepository masterPaymentLineRepository;

	@Autowired
	EpayTaxPaymentInfoRepository epayTaxPaymentInfoRepository;
	
	@Autowired
	MasterReceiverUnitRepository masReceiverUntiRepository;
	
	@Autowired
	EpayReceiverPaymentLineRepository epayReceiverPaymentLineRepository;	
	
	@Autowired
	MasterPaymentStatusRepository masterPaymentStatusRepository;
	
	@Autowired
	MasterSendMethodRepository masterSendMethodRepository;
	
	@Autowired
	EpayTaxPaymentInboundRepository epayTaxPaymentInboundRepository;
	
	@Autowired
	EpayTaxPaymentInboundRefOutboundRepository epayTaxPaymentInboundRefOutboundRepository;

	public Optional<EpayTaxPaymentOutbound> findByTranNo(String tranNo) {
		return paymentOutboundRepo.findByTranNo(tranNo);
	}

	public EpayTaxPaymentInfo findByEpayTaxPaymentInfoId(long infoId) {
		return epayTaxPaymentInfoRepository.findByEpayTaxPaymentInfoId(infoId);
	}
	
	public Optional<EpayTaxPaymentOutbound> findByCtlCode(String ctlCode)
	{
		return paymentOutboundRepo.findByCtlCode(ctlCode);
	}
	
	public Optional<MasterPaymentLine> findByPayLineCode(String paymentLine)
	{
		return masterPaymentLineRepository.findByPayLineCode(paymentLine);
	}
	
	public Optional<MasterReceiverUnit> findByRecCode(String bankCode)
	{
		return masReceiverUntiRepository.findByRecCode(bankCode);
	}

	public List<EpayReceiverPaymentLine> findByMasterPaymentLineAndMasterReceiverUnit(MasterPaymentLine masterPaymentLine,MasterReceiverUnit masterReceiverUnit)
	{
		return epayReceiverPaymentLineRepository.findByMasterPaymentLineAndMasterReceiverUnit(masterPaymentLine,masterReceiverUnit);
	}
	
	public Optional<EpayTaxPaymentInfo> findByEpayTaxPaymentInfoIdAndAgentIdAndTotalAmount(long ifoID, String agentID, BigDecimal totalAmount)
	{
		return epayTaxPaymentInfoRepository.findByEpayTaxPaymentInfoIdAndAgentIdAndTotalAmount(ifoID,agentID,totalAmount);
	}
	
	@Transactional
	public void save(EpayTaxPaymentInfo entity)
	{
		epayTaxPaymentInfoRepository.save(entity);
    }
	
	public Optional<MasterPaymentStatus> findById(int payStatus)
	{
		return masterPaymentStatusRepository.findById(payStatus);
	}
	
	public Optional<MasterSendMethod> findBySendMethod(String sendMethod)
	{
		return masterSendMethodRepository.findBySendMethod(sendMethod);
	}
	
	@Transactional
	public void save(EpayTaxPaymentInbound entity)
	{
		epayTaxPaymentInboundRepository.save(entity);
    }
	
	@Transactional
	public void save(EpayTaxPaymentInboundRefOutbound entity)
	{
		epayTaxPaymentInboundRefOutboundRepository.save(entity);
    }
}
