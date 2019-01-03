package th.go.rd.rdepaymentservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import th.go.rd.rdepaymentservice.entity.EpayTaxPaymentInboundDetail;

public interface EpayTaxPaymentInboundDetailRepository extends JpaRepository<EpayTaxPaymentInboundDetail, String>{
	
}


