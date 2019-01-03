package th.go.rd.rdepaymentservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import th.go.rd.rdepaymentservice.entity.MasterSendMethod;

public interface MasterSendMethodRepository extends JpaRepository<MasterSendMethod, String> {
	Optional<MasterSendMethod> findBySendMethod(String sendMethod);
}
