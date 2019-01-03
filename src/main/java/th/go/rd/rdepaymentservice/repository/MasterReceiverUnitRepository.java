package th.go.rd.rdepaymentservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import th.go.rd.rdepaymentservice.entity.MasterReceiverType;
import th.go.rd.rdepaymentservice.entity.MasterReceiverUnit;
import th.go.rd.rdepaymentservice.entity.MasterStatus;

public interface MasterReceiverUnitRepository extends JpaRepository<MasterReceiverUnit, Long>{

	Optional<MasterReceiverUnit> findByRecCode(String bankCode);
	
	List<MasterReceiverUnit> findByRecCodeAndMasterReceiverType(String recCode, MasterReceiverType masterReceiverType);
	List<MasterReceiverUnit> findByRecShortNameEnAndMasterReceiverType(String recShortName, MasterReceiverType masterReceiverType);
	List<MasterReceiverUnit> findByMasterReceiverType(MasterReceiverType masterReceiverType);
	List<MasterReceiverUnit> findByMasterReceiverTypeAndMasterStatus(MasterReceiverType masterReceiverType, MasterStatus masterStatus);
	List<MasterReceiverUnit> findByRecCodeAndRecShortNameEnAndMasterReceiverType(String recCode, String recShortName, MasterReceiverType masterReceiverType);
	List<MasterReceiverUnit> findByRecCodeAndMasterStatusAndMasterReceiverType(String recCode, MasterStatus masterStatus, MasterReceiverType masterReceiverType);
	List<MasterReceiverUnit> findByRecShortNameEnAndMasterStatusAndMasterReceiverType(String recShortName, MasterStatus masterStatus, MasterReceiverType masterReceiverType);
	List<MasterReceiverUnit> findByRecCodeAndRecShortNameEnAndMasterReceiverTypeAndMasterStatus(String recCode, String recShortName, MasterReceiverType masterReceiverType, MasterStatus masterStatus);
}
