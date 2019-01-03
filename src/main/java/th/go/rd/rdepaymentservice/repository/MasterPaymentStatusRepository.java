package th.go.rd.rdepaymentservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import th.go.rd.rdepaymentservice.entity.MasterPaymentStatus;

public interface MasterPaymentStatusRepository extends JpaRepository<MasterPaymentStatus, Integer>{

}
