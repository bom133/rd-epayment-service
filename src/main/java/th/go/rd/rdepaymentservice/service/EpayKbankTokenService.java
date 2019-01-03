package th.go.rd.rdepaymentservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import th.go.rd.rdepaymentservice.entity.EpayKbankToken;
import th.go.rd.rdepaymentservice.repository.EpayKbankTokenRepository;

@Service
public class EpayKbankTokenService {
	
	@Autowired
	EpayKbankTokenRepository epayKbankTokenRepository; 

	public void save(EpayKbankToken epayKbankToken) {
		epayKbankTokenRepository.save(epayKbankToken);
	}
}
