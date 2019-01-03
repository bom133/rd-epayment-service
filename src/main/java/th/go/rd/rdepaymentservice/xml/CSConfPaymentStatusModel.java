package th.go.rd.rdepaymentservice.xml;

import java.util.Date;

import javax.xml.bind.annotation.XmlElement;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CSConfPaymentStatusModel {
	@JsonProperty("PaymentStatus")
	private String paymentStatus;
	
	@JsonProperty("TransferDate")
	private String transferDate;
	
	@JsonProperty("AuthorizeCode")
	private String authorizeCode;
	
	@JsonProperty("StatusMessage")
	private String statusMessage;

	@XmlElement(name="PaymentStatus")
	public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	@XmlElement(name="TransferDate")
	public String getTransferDate() {
		return transferDate;
	}

	public void setTransferDate(String transferDate) {
		this.transferDate = transferDate;
	}

	@XmlElement(name="AuthorizeCode")
	public String getAuthorizeCode() {
		return authorizeCode;
	}

	public void setAuthorizeCode(String authorizeCode) {
		this.authorizeCode = authorizeCode;
	}

	@XmlElement(name="StatusMessage")
	public String getStatusMessage() {
		return statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}

	@Override
	public String toString() {
		return "EPYConfPaymentStatus [paymentStatus=" + paymentStatus + ", transferDate=" + transferDate
				+ ", authorizeCode=" + authorizeCode + ", statusMessage=" + statusMessage + "]";
	}
	
}
