package th.go.rd.rdepaymentservice.xml;

import java.util.Arrays;
import java.util.Date;

import javax.xml.bind.annotation.XmlElement;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class CSConfirmPaymentInfoModel {
	
	@JsonProperty("AgentBrano")
	private String agentBrano;
	
	@JsonProperty("AgentID")
	private String agentID;
	
	@JsonProperty("BankCode")
	private String bankCode;
	
	@JsonProperty("RDTransactionNo")
	private String rdTransactionNo;
	
	@JsonProperty("TotalAmount")
	private Double totalAmount;
	
	@JsonProperty("TransmitDate")
	private String transmitDate;
	
	@JsonProperty("PaymentID")
	private String paymentID;
	
	@JsonProperty("PaymentLine")
	private String paymentLine;
		
	@JsonProperty("PaymentDetail")
	@JacksonXmlProperty(localName = "PaymentDetail")
    @JacksonXmlElementWrapper(useWrapping = false)
	private CSConfPaymentDetailModel[] paymentDetail;

	@XmlElement(name="BankCode")
	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	@XmlElement(name="RDTransactionNo")
	public String getRdTransactionNo() {
		return rdTransactionNo;
	}
	
	public void setRdTransactionNo(String rdTransactionNo) {
		this.rdTransactionNo = rdTransactionNo;
	}

	@XmlElement(name="PaymentID")
	public String getPaymentID() {
		return paymentID;
	}

	public void setPaymentID(String paymentID) {
		this.paymentID = paymentID;
	}

	@XmlElement(name="AgentID")
	public String getAgentID() {
		return agentID;
	}

	public void setAgentID(String agentID) {
		this.agentID = agentID;
	}

	@XmlElement(name="AgentBrano")
	public String getAgentBrano() {
		return agentBrano;
	}

	public void setAgentBrano(String agentBrano) {
		this.agentBrano = agentBrano;
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
	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}


	@XmlElement(name="PaymentDetail")
	public CSConfPaymentDetailModel[] getPaymentDetail() {
		return paymentDetail;
	}

	public void setPaymentDetail(CSConfPaymentDetailModel[] paymentDetail) {
		this.paymentDetail = paymentDetail;
	}

	@Override
	public String toString() {
		return "CSConfirmPaymentInfoModel [agentBrano=" + agentBrano + ", agentID=" + agentID + ", bankCode=" + bankCode
				+ ", rdTransactionNo=" + rdTransactionNo + ", totalAmount=" + totalAmount + ", transmitDate="
				+ transmitDate + ", paymentID=" + paymentID + ", paymentLine=" + paymentLine + ", paymentDetail="
				+ Arrays.toString(paymentDetail) + "]";
	}
}
