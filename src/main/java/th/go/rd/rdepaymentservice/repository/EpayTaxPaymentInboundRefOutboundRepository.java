package th.go.rd.rdepaymentservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import th.go.rd.rdepaymentservice.entity.EpayTaxPaymentInbound;
import th.go.rd.rdepaymentservice.entity.EpayTaxPaymentInboundRefOutbound;
import th.go.rd.rdepaymentservice.entity.EpayTaxPaymentOutbound;


public interface EpayTaxPaymentInboundRefOutboundRepository extends JpaRepository<EpayTaxPaymentInboundRefOutbound, Long>{
   
    Optional<EpayTaxPaymentInboundRefOutbound> findByEpayTaxPaymentInbound(EpayTaxPaymentInbound inbound);
    Optional<EpayTaxPaymentInboundRefOutbound> findByEpayTaxPaymentOutbound(EpayTaxPaymentOutbound outbound);
}
