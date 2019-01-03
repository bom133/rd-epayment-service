package th.go.rd.rdepaymentservice.xml;

import java.math.BigDecimal;
import java.util.Date;

import javax.xml.bind.annotation.XmlElement;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResPaymentInformation {
	
	@JsonProperty("RDTransactionNo")
	private String rDTransactionNo;
	
	@JsonProperty("TransmitDate")
	private Date transmitDate;
	
	@JsonProperty("TotalAmount")
	private BigDecimal totalAmount;
	
	@JsonProperty("TerminalID")
	private String terminalID;
	
	@JsonProperty("MerchantID")
	private String merchantID;
	
	@JsonProperty("ObKeyID")
	private String obKeyID;
	
	@JsonProperty("Ref1")
	private String ref1;
	
	@JsonProperty("Ref2")
	private String ref2;
	
	@JsonProperty("Ref3")
	private String ref3;

	@XmlElement(name="RDTransactionNo")
	public String getrDTransactionNo() {
		return rDTransactionNo;
	}

	public void setrDTransactionNo(String rDTransactionNo) {
		this.rDTransactionNo = rDTransactionNo;
	}

	@XmlElement(name="TransmitDate")
	public Date getTransmitDate() {
		return transmitDate;
	}

	public void setTransmitDate(Date transmitDate) {
		this.transmitDate = transmitDate;
	}

	@XmlElement(name="TotalAmount")
	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
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

	@XmlElement(name="ObKeyID")
	public String getObKeyID() {
		return obKeyID;
	}

	public void setObKeyID(String obKeyID) {
		this.obKeyID = obKeyID;
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

	@Override
	public String toString() {
		return "ResPaymentInformation [rDTransactionNo=" + rDTransactionNo + ", transmitDate=" + transmitDate
				+ ", totalAmount=" + totalAmount + ", terminalID=" + terminalID + ", merchantID=" + merchantID
				+ ", obKeyID=" + obKeyID + ", ref1=" + ref1 + ", ref2=" + ref2 + ", ref3=" + ref3 + "]";
	}
}
