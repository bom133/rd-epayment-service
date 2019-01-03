package th.go.rd.rdepaymentservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import th.go.rd.rdepaymentservice.entity.MasterStatus;

public interface MasterStatusRepository extends JpaRepository<MasterStatus, String> {
	Optional<MasterStatus> findByStatus(String status);
}
