package th.go.rd.rdepaymentservice.serviceproxy;

//import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import th.go.rd.rdepaymentservice.model.KeySignModel;


//@RibbonClient(name = "rd-pki-service")
public interface PkiCryptoServiceProxy {

//	@PostMapping("rd-pki-service/pki/cryptography/xml/encrypt")
//	public ResponseEntity<InputStreamResource> encryptXML(KeySignModel keySign,@RequestParam("xml") MultipartFile xmlFile);
//
//	@PostMapping("rd-pki-service/pki/cryptography/xml/decrypt")
//	public ResponseEntity<InputStreamResource> decryptXML(KeySignModel keySign,@RequestParam("xml") MultipartFile xmlFile);

}
