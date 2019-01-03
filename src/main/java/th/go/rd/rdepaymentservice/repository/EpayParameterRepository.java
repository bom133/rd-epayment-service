package th.go.rd.rdepaymentservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import th.go.rd.rdepaymentservice.entity.EpayParameter;
import th.go.rd.rdepaymentservice.entity.MasterParamType;

public interface EpayParameterRepository extends JpaRepository<EpayParameter, Long>{
	//Optional<EpayParameter> findByParamCode(String code);
	
	List<EpayParameter> findByMasterParamType(MasterParamType paramType);

	List<EpayParameter> findByParamCode(String code);
	
	List<EpayParameter> findByMasterParamTypeAndParamCode(MasterParamType paramType,String code);

	List<EpayParameter> findByParamCodeContaining(String code);

	List<EpayParameter> findByMasterParamTypeAndParamCodeContaining(MasterParamType paramType,String code);
}
