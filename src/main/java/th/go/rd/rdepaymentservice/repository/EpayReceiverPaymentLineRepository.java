package th.go.rd.rdepaymentservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import th.go.rd.rdepaymentservice.entity.EpayReceiverPaymentLine;
import th.go.rd.rdepaymentservice.entity.MasterPaymentLine;
import th.go.rd.rdepaymentservice.entity.MasterReceiverUnit;
import th.go.rd.rdepaymentservice.entity.MasterStatus;

public interface EpayReceiverPaymentLineRepository extends JpaRepository<EpayReceiverPaymentLine, Long> {
	List<EpayReceiverPaymentLine> findByMasterPaymentLine(MasterPaymentLine masterPaymentLine);

	List<EpayReceiverPaymentLine> findByMasterReceiverUnit(MasterReceiverUnit masterReceiverUnit);

	List<EpayReceiverPaymentLine> findByMasterStatus(MasterStatus masterStatus);
	
	List<EpayReceiverPaymentLine> findByMasterPaymentLineAndMasterReceiverUnit(MasterPaymentLine masterPaymentLine,MasterReceiverUnit masterReceiverUnit);

	List<EpayReceiverPaymentLine> findByMasterPaymentLineAndMasterStatus(MasterPaymentLine masterPaymentLine,MasterStatus masterStatus);

	List<EpayReceiverPaymentLine> findByMasterReceiverUnitAndMasterStatus(MasterReceiverUnit masterReceiverUnit, MasterStatus masterStatus);
	
	List<EpayReceiverPaymentLine> findByMasterPaymentLineAndMasterReceiverUnitAndMasterStatus(MasterPaymentLine masterPaymentLine,MasterReceiverUnit masterReceiverUnit,MasterStatus masterStatus);
}
