package th.go.rd.rdepaymentservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import th.go.rd.rdepaymentservice.entity.EpayReceiverPaymentLine;
import th.go.rd.rdepaymentservice.entity.EpayReceiverPaymentLineApiUrl;


public interface EpayReceiverPaymentLineApiUrlRepository extends JpaRepository<EpayReceiverPaymentLineApiUrl, Integer>{
	Optional<EpayReceiverPaymentLineApiUrl> findByEpayReceiverPaymentLine(EpayReceiverPaymentLine epayReceiverPaymentLine);
}
