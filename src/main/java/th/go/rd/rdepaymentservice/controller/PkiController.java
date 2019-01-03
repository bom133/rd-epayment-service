package th.go.rd.rdepaymentservice.controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import th.go.rd.rdepaymentservice.service.PkiService;

@RestController
@RequestMapping("/pki/")
public class PkiController {

	private static final Logger logger = LoggerFactory.getLogger(PkiController.class);
	
	@Autowired 
	PkiService pkiService;
	
	@Value("${payment-std.rd-keystore}")
	private long rdKeyStore;
	
	@Value("${payment-std.rec-keystore}")
	private long recKeyStore;
	
	@GetMapping(value="rd-certificate/list", headers = { "Authorization" })
	public Object getRdCertifiate() {
		logger.info("RD Certificate List");
		return pkiService.getKeyStoreKey(rdKeyStore);
	}
	
	@GetMapping(value="rec-certificate/list", headers = { "Authorization" })
	public Object getRecCertifiate() {
		logger.info("REC Certificate List");
		return pkiService.getKeyStoreKey(recKeyStore);
	}
	
	@PostMapping("test-encryptXml")
	public String testEncryptXml(long keySignId, long keyEncId, @RequestBody String xml) throws Exception {
		logger.info("Test Encrypt XML");
		String encryptXml = pkiService.dSigWithEncrypt(keySignId, keyEncId, "tmp", xml);
		return encryptXml;
	}

	@PostMapping("test-decryptXml")
	public String testDecryptXml(long keyVerifyId, long keyDecId, @RequestBody String xml) throws Exception {
		logger.info("Test Decrypt XML");
		String decryptXml = pkiService.verifyWithDecrypt(keyVerifyId, keyDecId, "tmp", xml);
		return decryptXml;
	}
}
