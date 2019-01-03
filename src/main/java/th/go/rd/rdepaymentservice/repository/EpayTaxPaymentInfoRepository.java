package th.go.rd.rdepaymentservice.repository;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import th.go.rd.rdepaymentservice.entity.EpayTaxPaymentInfo;
import th.go.rd.rdepaymentservice.entity.MasterStatus;

public interface EpayTaxPaymentInfoRepository extends JpaRepository<EpayTaxPaymentInfo, Long> {

	Optional<EpayTaxPaymentInfo> findByRefNo(String refNo);

	Optional<EpayTaxPaymentInfo> findByRefNoAndMasterStatus(String refNo,MasterStatus status);
	
	EpayTaxPaymentInfo findByEpayTaxPaymentInfoId(long infoId);
	
	Optional<EpayTaxPaymentInfo> findByEpayTaxPaymentInfoIdAndAgentIdAndTotalAmount(long ifoID, String agentID, BigDecimal totalAmount);
	
	@Query(value = "SELECT EFEPAY.CONTROL_CODE_FUNCTION() FROM SYSIBM.SYSDUMMY1",nativeQuery = true)
	String getCTLCode();

	Optional<EpayTaxPaymentInfo> findByRefNoAndMasterStatusAndIsRound(String refNo, MasterStatus masterStatus,
			Character isRound);
}
