package th.go.rd.rdepaymentservice.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class PkiService {
    private static final Logger logger = LoggerFactory.getLogger(PkiService.class);
	
	@Value("${payment-std.pki-service}")
	private String pkiServiceUrl;
	
	@Value("${payment-std.api-pki-sign-xml}")
	private String apiSignXML;
	
	@Value("${payment-std.api-pki-verify-xml}")
	private String apiVerifyXML;

	@Value("${payment-std.client_id}")
	private String clientId;
	
	@Value("${payment-std.secret_id}")
	private String secretId;
	
	@Value("${payment-std.api-pki-certificate-list}")
	private String apiCertificateList;
	
	@Value("${payment-std.xml-path}")
	private String xmlPath;
	
	public String dSigWithEncrypt(long keySignId, long keyEncId, String fileName, String src) throws Exception {
		String encryptData = PkiService.trim(src);
		MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<>();

		Path tempFile = Files.createTempFile(fileName, ".xml");
		
		Files.write(tempFile, encryptData.getBytes());
		File file = tempFile.toFile();
		
		ResponseEntity<String> response = null;
		try {
			
			bodyMap.add("xmlFile", new FileSystemResource(file));
			
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.MULTIPART_FORM_DATA);
			headers.set("client_id", clientId);
			headers.set("secret_id", secretId);
			
//			System.out.println("clientId : " + clientId); 
//			System.out.println("secretId : " + secretId); 
//			
//			headers.add("client_id", "07");
//	        headers.add("secret_id", "123");
			
			HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(bodyMap, headers);

//			System.out.println("PKI URL : " + this.pkiServiceUrl + apiSignXML + "?keySignId=" + keySignId + "&keyEncId=" + keyEncId);
			RestTemplate restTemplate = new RestTemplate();
			
//			response = restTemplate.exchange(this.pkiServiceUrl + apiSignXML + "?keySignId=" + keySignId + "&keyEncId=" + keyEncId,
//					HttpMethod.POST, requestEntity, String.class);
			
			response = restTemplate.exchange("http://10.11.2.25:8769/rd-pki-service/pki/digital-signature/sign-encrypt-xml?keySignId=163&keyEncId=169",
					HttpMethod.POST, requestEntity, String.class);
			
			
			System.out.println("Http status : " + response.getStatusCode()); 
			if (response.getStatusCode() == HttpStatus.OK) {
				encryptData = PkiService.trim(response.getBody());
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("context", e);
			throw new Exception("Sign XML Fail [" + e.getMessage() + "]");
		} finally {
			if (!file.delete()) {
				logger.error("cann't delete file!");
			}
		}
		
		return encryptData;
	}
	
	public String verifyWithDecrypt(long keyVerifyId, long keyDecId, String fileName, String src) throws Exception {
		String decryptData = PkiService.trim(src);
		
		MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<>();

		Path tempFile = Files.createTempFile(fileName, ".xml");
		Files.write(tempFile, decryptData.getBytes());
		File file = tempFile.toFile();
		
		ResponseEntity<String> response = null;
		try {
			bodyMap.add("keyVerifyId", keyVerifyId);
			bodyMap.add("keyDecId", keyDecId);
			bodyMap.add("xmlFile", new FileSystemResource(file));
			
			HttpHeaders headers = new HttpHeaders();
			headers.set("client_id", clientId);
			headers.set("secret_id", secretId);
			
			headers.setContentType(MediaType.MULTIPART_FORM_DATA);
			HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(bodyMap, headers);

			//System.out.println("PKI URL : " + this.pkiServiceUrl+"/pki/digital-signature/verify-decrypt-xml");
			RestTemplate restTemplate = new RestTemplate();
			response = restTemplate.exchange(this.pkiServiceUrl + apiVerifyXML,
					HttpMethod.POST, requestEntity, String.class);
			if (response.getStatusCode() == HttpStatus.OK) {
				decryptData = PkiService.trim(response.getBody());
			}
		} catch (Exception e) {
			logger.error("context", e);
			throw new Exception("Verify XML Fail [" + e.getMessage() + "]");
		} finally {
			if (!file.delete()) {
				logger.error("cann't delete file!");
			}
		}
		
		return decryptData;
	}
	
	public Object getKeyStoreKey(long keyStoreId) {
		RestTemplate restTemplate = new RestTemplate();
		
		MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<>();
		
		HttpHeaders headers = new HttpHeaders();
		headers.set("client_id", clientId);
		headers.set("secret_id", secretId);
		
		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(bodyMap, headers);
		ResponseEntity response = restTemplate.exchange(
                this.pkiServiceUrl+ apiCertificateList + "?keyStoreID="+keyStoreId,
                HttpMethod.GET,requestEntity, Object.class);

		return response.getBody();
	}
	
	public static String trim(String input) {
	    BufferedReader reader = new BufferedReader(new StringReader(input));
	    StringBuffer result = new StringBuffer();
	    try {
	        String line;
	        while ( (line = reader.readLine() ) != null)
	            result.append(line.trim());
	        return result.toString();
	    } catch (IOException e) {
	        throw new RuntimeException(e);
	    }
	}
}
