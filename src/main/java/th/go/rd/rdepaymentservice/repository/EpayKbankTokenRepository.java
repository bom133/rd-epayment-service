package th.go.rd.rdepaymentservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import th.go.rd.rdepaymentservice.entity.EpayKbankToken;

public interface EpayKbankTokenRepository extends JpaRepository<EpayKbankToken, Long> {

}
