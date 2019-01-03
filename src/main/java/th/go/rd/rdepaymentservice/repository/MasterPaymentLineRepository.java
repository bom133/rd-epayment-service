package th.go.rd.rdepaymentservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import th.go.rd.rdepaymentservice.entity.MasterPaymentLine;
import th.go.rd.rdepaymentservice.entity.MasterStatus;

public interface MasterPaymentLineRepository extends JpaRepository<MasterPaymentLine, Long>{

	Optional<MasterPaymentLine> findByPayLineCode(String paymentLine);

	List<MasterPaymentLine> findAll();
	
	List<MasterPaymentLine> findByPayLineCodeContainingAndPayLineNameThContaining(String paymentLine, String paymentLineNameThai);
	
	List<MasterPaymentLine> findByPayLineCodeContainingAndPayLineNameThContainingAndMasterStatus(String paymentLine, String paymentLineNameThai, MasterStatus masterStatus);
	
	Optional<MasterPaymentLine> findByPayLineCodeAndPayLineNameThAndMasterStatus(String paymentLine, String paymentLineNameThai, MasterStatus masterStatus);

}
