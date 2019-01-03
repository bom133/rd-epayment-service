package th.go.rd.rdepaymentservice.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import th.go.rd.rdepaymentservice.entity.EpayTaxPaymentInbound;

public interface EpayTaxPaymentInboundRepository extends JpaRepository<EpayTaxPaymentInbound, Long>{

    List<EpayTaxPaymentInbound> findByRdTranNo(String rdTranNo);

    List<EpayTaxPaymentInbound> findByTransmitDateGreaterThanEqualAndResponseCodeIsNull(Date transmiDate);

    Optional<EpayTaxPaymentInbound> findByUuid(String uuid);
}
