package th.go.rd.rdepaymentservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import th.go.rd.rdepaymentservice.entity.EpayAuthorization;
import th.go.rd.rdepaymentservice.entity.EpayRolePermission;
import th.go.rd.rdepaymentservice.entity.MasterSystemInfo;

public interface EpayRolePermissionRepository extends JpaRepository<EpayRolePermission, Long>{

    Optional<EpayRolePermission> findByEpayAuthorizationAndMasterSystemInfo(EpayAuthorization epayAuthorization,MasterSystemInfo masterSystemInfo);
}
