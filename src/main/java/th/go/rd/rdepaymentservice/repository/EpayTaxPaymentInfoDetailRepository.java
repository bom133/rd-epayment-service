package th.go.rd.rdepaymentservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import th.go.rd.rdepaymentservice.entity.EpayTaxPaymentInfo;
import th.go.rd.rdepaymentservice.entity.EpayTaxPaymentInfoDetail;

public interface EpayTaxPaymentInfoDetailRepository extends JpaRepository<EpayTaxPaymentInfoDetail,Long>  {
	EpayTaxPaymentInfoDetail findByEpayTaxPaymentInfo(EpayTaxPaymentInfo epayTaxPaymentInfoId);
}
