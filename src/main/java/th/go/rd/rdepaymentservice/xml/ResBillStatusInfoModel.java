package th.go.rd.rdepaymentservice.xml;

import javax.xml.bind.annotation.XmlElement;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResBillStatusInfoModel {
	
	@JsonProperty("RDTransactionNo")
	private String rdTransactionNo;
	
	@JsonProperty("BankCode")
	private String bankCode;
	
	
	@JsonProperty("Ref1")
	private String ref1;
	
	@JsonProperty("Ref2")
	private String ref2;
	
	@JsonProperty("Ref3")
	private String ref3;
	
	@JsonProperty("TransmitDate")
	private String transmitDate;

	@JsonProperty("ExpDate")
	private String expDate;

	@JsonProperty("TotalAmout")
	private Double totalAmout;
	
	@JsonProperty("TerminalID")
	private String terminalID;
	
	
	@JsonProperty("MerchantID")
	private String merchantID;


	public ResBillStatusInfoModel() {
		super();
	}


	public ResBillStatusInfoModel(String rdTransactionNo, String bankCode, String ref1, String ref2, String ref3,
			String transmitDate, String expDate, Double totalAmout, String terminalID, String merchantID) {
		super();
		this.rdTransactionNo = rdTransactionNo;
		this.bankCode = bankCode;
		this.ref1 = ref1;
		this.ref2 = ref2;
		this.ref3 = ref3;
		this.transmitDate = transmitDate;
		this.expDate = expDate;
		this.totalAmout = totalAmout;
		this.terminalID = terminalID;
		this.merchantID = merchantID;
	}


	@XmlElement(name="RDTransactionNo")
	public String getRdTransactionNo() {
		return rdTransactionNo;
	}


	public void setRdTransactionNo(String rdTransactionNo) {
		this.rdTransactionNo = rdTransactionNo;
	}

	@XmlElement(name="BankCode")
	public String getBankCode() {
		return bankCode;
	}


	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}


	@XmlElement(name="Ref1")
	public String getRef1() {
		return ref1;
	}


	public void setRef1(String ref1) {
		this.ref1 = ref1;
	}


	@XmlElement(name="Ref2")
	public String getRef2() {
		return ref2;
	}


	public void setRef2(String ref2) {
		this.ref2 = ref2;
	}


	@XmlElement(name="Ref3")
	public String getRef3() {
		return ref3;
	}


	public void setRef3(String ref3) {
		this.ref3 = ref3;
	}


	@XmlElement(name="TransmitDate")
	public String getTransmitDate() {
		return transmitDate;
	}


	public void setTransmitDate(String transmitDate) {
		this.transmitDate = transmitDate;
	}


	@XmlElement(name="ExpDate")
	public String getExpDate() {
		return expDate;
	}


	public void setExpDate(String expDate) {
		this.expDate = expDate;
	}


	@XmlElement(name="TotalAmount")
	public Double getTotalAmout() {
		return totalAmout;
	}


	public void setTotalAmout(Double totalAmout) {
		this.totalAmout = totalAmout;
	}


	@XmlElement(name="TerminalID")
	public String getTerminalID() {
		return terminalID;
	}


	public void setTerminalID(String terminalID) {
		this.terminalID = terminalID;
	}


	@XmlElement(name="MerchantID")
	public String getMerchantID() {
		return merchantID;
	}


	public void setMerchantID(String merchantID) {
		this.merchantID = merchantID;
	}


	@Override
	public String toString() {
		return "ResBillStatusInfoModel [rdTransactionNo=" + rdTransactionNo + ", bankCode=" + bankCode + ", ref1="
				+ ref1 + ", ref2=" + ref2 + ", ref3=" + ref3 + ", transmitDate=" + transmitDate + ", expDate=" + expDate
				+ ", totalAmout=" + totalAmout + ", terminalID=" + terminalID + ", merchantID=" + merchantID + "]";
	}
}
