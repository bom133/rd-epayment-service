package th.go.rd.rdepaymentservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import th.go.rd.rdepaymentservice.entity.EpayAuthorization;
import th.go.rd.rdepaymentservice.entity.MasterStatus;

public interface EpayAuthorizationRepository extends JpaRepository<EpayAuthorization, Long> {

	List<EpayAuthorization> findByAuthCode(String authCode);
	
	List<EpayAuthorization> findByAuthCodeContaining(String authCode);
	
	List<EpayAuthorization> findByDescription(String description);
	
	List<EpayAuthorization> findByDescriptionContaining(String description);
	
	List<EpayAuthorization> findByMasterStatus(MasterStatus masterStatus);
	
	List<EpayAuthorization> findByAuthCodeAndDescription(String authCode, String description);
	
	List<EpayAuthorization> findByAuthCodeContainingAndDescriptionContaining(String authCode, String description);
	
	List<EpayAuthorization> findByAuthCodeAndMasterStatus(String authCode, MasterStatus masterStatus);
	
	List<EpayAuthorization> findByAuthCodeContainingAndMasterStatus(String authCode, MasterStatus masterStatus);
	
	List<EpayAuthorization> findByDescriptionAndMasterStatus(String description, MasterStatus masterStatus);
	
	List<EpayAuthorization> findByDescriptionContainingAndMasterStatus(String description, MasterStatus masterStatus);
	
	List<EpayAuthorization> findByAuthCodeAndDescriptionAndMasterStatus(String authCode, String description, MasterStatus masterStatus);
	
	List<EpayAuthorization> findByAuthCodeContainingAndDescriptionContainingAndMasterStatus(String authCode, String description, MasterStatus masterStatus);

}
