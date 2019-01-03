package th.go.rd.rdepaymentservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import th.go.rd.rdepaymentservice.entity.PaymentLog;

public interface PaymentLogRepository extends JpaRepository<PaymentLog,Long>{


}
