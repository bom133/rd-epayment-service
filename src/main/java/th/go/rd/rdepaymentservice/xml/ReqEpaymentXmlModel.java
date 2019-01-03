package th.go.rd.rdepaymentservice.xml;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@XmlRootElement(name="Epayment")
@JsonRootName("Epayment")
public class ReqEpaymentXmlModel {

	
	@JsonProperty("EncryptData")
	private String encryptData;
	
	@JsonProperty("MerchartId")
	private String merchartId;
	
	@JsonProperty("TerminalId")
	private String terminalId;
	
	@JsonProperty("BankCode")
	private String bankCode;
	
	@JsonProperty("PaymentLine")
	private String paymentLine;
	
	@JsonProperty("AttachFile")
	@JsonInclude(Include.NON_NULL)
	private String attachFile;
	
	public ReqEpaymentXmlModel() {
		super();
	}

	public ReqEpaymentXmlModel(String encryptData, String bankCode, String paymentLine, String attachFile, String merchartId, String TerminalId) {
		super();
		this.encryptData = encryptData;
		this.bankCode = bankCode;
		this.paymentLine = paymentLine;
		this.attachFile = attachFile;
		this.merchartId = merchartId;
		this.terminalId = TerminalId;
	}

	@XmlElement(name="BankCode")
	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	@XmlElement(name="PaymentLine")
	public String getPaymentLine() {
		return paymentLine;
	}

	public void setPaymentLine(String paymentLine) {
		this.paymentLine = paymentLine;
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

	@XmlElement(name="MerchartId")
	public String getMerchartId() {
		return merchartId;
	}

	public void setMerchartId(String merchartId) {
		this.merchartId = merchartId;
	}

	@XmlElement(name="TerminalId")
	public String getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}

	@Override
	public String toString() {
		return "ReqEpaymentXmlModel [encryptData=" + encryptData + ", merchartId=" + merchartId + ", terminalId="
				+ terminalId + ", bankCode=" + bankCode + ", paymentLine=" + paymentLine + ", attachFile=" + attachFile
				+ "]";
	}

}
