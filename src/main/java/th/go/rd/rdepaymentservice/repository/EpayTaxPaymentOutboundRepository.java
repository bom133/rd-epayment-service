package th.go.rd.rdepaymentservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import th.go.rd.rdepaymentservice.entity.EpayTaxPaymentInfo;
import th.go.rd.rdepaymentservice.entity.EpayTaxPaymentOutbound;


public interface EpayTaxPaymentOutboundRepository extends JpaRepository<EpayTaxPaymentOutbound, Long>{
	Optional<EpayTaxPaymentOutbound> findByUuid(String uuid);
	
	Optional<EpayTaxPaymentOutbound> findByCtlCode(String ctlCode);
	
	Optional<EpayTaxPaymentOutbound> findByTranNo(String obid);
	
	Optional<EpayTaxPaymentOutbound> findByEpayTaxPaymentInfo(EpayTaxPaymentInfo epayTaxPaymentInfo);
	
	@Query(value = "SELECT EFEPAY.TRANSACTION_NO_FUNCTION() FROM SYSIBM.SYSDUMMY1",nativeQuery = true)
	String getRunNoTransaction();
	
	@Query(value = "SELECT EFEPAY.XML_RD2BANK_RUNNING_NO_FUNCTION() FROM SYSIBM.SYSDUMMY1",nativeQuery = true)
	String getRunNoFileXml();
}


