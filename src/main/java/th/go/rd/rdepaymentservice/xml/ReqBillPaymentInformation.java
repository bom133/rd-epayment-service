package th.go.rd.rdepaymentservice.xml;

import java.util.Arrays;

import javax.xml.bind.annotation.XmlElement;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import io.swagger.annotations.ApiModelProperty;

public class ReqBillPaymentInformation {
	
	@JsonProperty("BankCode")
	private String bankCode;
	
	@JsonProperty("CSTransactionNo")
	private String cSTransactionNo;
	
	@JsonProperty("RDTransactionNo")
	private String rDTransactionNo;
	
	@JsonProperty("Ref1")
	private String ref1;
	
	@JsonProperty("Ref2")
	private String ref2;
	
	@JsonProperty("Ref3")
	private String ref3;
	
	@JsonProperty("TransmitDate")
	private String transmitDate;
	
	@JsonProperty("PaymentLine")
	private String paymentLine;
	
	@JsonProperty("TotalAmount")
	private String totalAmount;
	
	@JsonProperty("TerminalID")
	private String terminalID;
	
	@JsonProperty("MerchantID")
	private String merchantID;
		
	@JsonProperty("TransferDate")
	private String transferDate;

	
	@XmlElement(name="BankCode")
	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	@XmlElement(name="CSTransactionNo")
	public String getCSTransactionNo() {
		return cSTransactionNo;
	}

	public void setCSTransactionNo(String cSTransactionNo) {
		this.cSTransactionNo = cSTransactionNo;
	}

	@XmlElement(name="RDTransactionNo")
	public String getRDTransactionNo() {
		return rDTransactionNo;
	}

	public void setRDTransactionNo(String rDTransactionNo) {
		this.rDTransactionNo = rDTransactionNo;
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

	@XmlElement(name="PaymentLine")
	public String getPaymentLine() {
		return paymentLine;
	}

	public void setPaymentLine(String paymentLine) {
		this.paymentLine = paymentLine;
	}

	@XmlElement(name="TotalAmount")
	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
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

	@XmlElement(name="TransferDate")
	public String getTransferDate() {
		return transferDate;
	}

	public void setTransferDate(String transferDate) {
		this.transferDate = transferDate;
	}

	@Override
	public String toString() {
		return "ReqBillPaymentInformation [bankCode=" + bankCode + ", cSTransactionNo=" + cSTransactionNo
				+ ", rDTransactionNo=" + rDTransactionNo + ", ref1=" + ref1 + ", ref2=" + ref2 + ", ref3=" + ref3
				+ ", transmitDate=" + transmitDate + ", paymentLine=" + paymentLine + ", totalAmount=" + totalAmount
				+ ", terminalID=" + terminalID + ", merchantID=" + merchantID + ", transferDate=" + transferDate + "]";
	}
}
