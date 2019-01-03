package th.go.rd.rdepaymentservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import th.go.rd.rdepaymentservice.entity.MasterStatus;
import th.go.rd.rdepaymentservice.entity.MasterSystemInfo;

public interface MasterSystemInfoRepository extends JpaRepository<MasterSystemInfo,Integer>{

	Optional<MasterSystemInfo> findByClientIdAndSecretIdAndMasterStatus(String clientId, String secretId,MasterStatus MasterStatus);

	Optional<MasterSystemInfo> findBySystemCode(String systemCode);

	Optional<MasterSystemInfo> findByClientId(String clientId);

}
