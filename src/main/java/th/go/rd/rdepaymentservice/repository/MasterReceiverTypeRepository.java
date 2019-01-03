package th.go.rd.rdepaymentservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import th.go.rd.rdepaymentservice.entity.MasterReceiverType;

public interface MasterReceiverTypeRepository extends JpaRepository<MasterReceiverType, Integer>{
	Optional<MasterReceiverType> findByReceiverType(int receiverType);
} 
