package th.go.rd.rdepaymentservice.xml;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@XmlRootElement(name="Epayment")
@JsonRootName("Epayment")
public class Rd2BankEpaymentXmlModel {

	
	@JsonProperty("EncryptData")
	private String encryptData;
	
	@JsonProperty("TerminalId")
	private String terminalId;
	
	@JsonProperty("MerchantId")
	private String merchantId;
	
	@JsonProperty("AttachFile")
	private String attachFile;
	
	public Rd2BankEpaymentXmlModel() {
		super();
	}



	public Rd2BankEpaymentXmlModel(String encryptData, String terminalId, String merchantId, String attachFile) {
		super();
		this.encryptData = encryptData;
		this.terminalId = terminalId;
		this.merchantId = merchantId;
		this.attachFile = attachFile;
	}



	@XmlElement(name="TerminalId")
	public String getTerminalId() {
		return terminalId;
	}



	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId == null ? "" : terminalId;
	}



	@XmlElement(name="MerchantId")
	public String getMerchantId() {
		return merchantId;
	}



	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId == null? "" : merchantId;
	}



	@XmlElement(name="AttachFile")
	public String getAttachFile() {
		return attachFile;
	}

	public void setAttachFile(String attachFile) {
		this.attachFile = attachFile;
	}

	@XmlElement(name="EncryptData")
	public String getEncryptData() {
		return encryptData;
	}

	public void setEncryptData(String encryptData) {
		this.encryptData = encryptData;
	}



	@Override
	public String toString() {
		return "Rd2BankEpaymentXmlModel [encryptData=" + encryptData + ", terminalId=" + terminalId + ", merchantId="
				+ merchantId + ", attachFile=" + attachFile + "]";
	}

	
}
