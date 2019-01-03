package th.go.rd.rdepaymentservice.response;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import th.go.rd.rdepaymentservice.component.JsonDecimalSerializer;

public class ResPaymentInfoProcessing {

    private String RefNo;
    private String agentId;
    private String TransactionNo;
    private String controlCode;
	@JsonSerialize(using=JsonDecimalSerializer.class)
    private BigDecimal totalAmount;
    private String infoStatus;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC+07:00")
    private Date transferDate;
    private long recPaymentLineId;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC+07:00")
    private Date transmitDate;
    private int paymentStatus;
    private String paymentStatusMessage;
    private String outboundStatus;
	public ResPaymentInfoProcessing() {
		super();
	}
	public ResPaymentInfoProcessing(String refNo, String agentId, String transactionNo, String controlCode, BigDecimal totalAmount, String infoStatus, Date transferDate, long recPaymentLineId,
			Date transmitDate, int paymentStatus, String paymentStatusMessage, String outboundStatus) {
		super();
		RefNo = refNo;
		this.agentId = agentId;
		TransactionNo = transactionNo;
		this.controlCode = controlCode;
		this.totalAmount = totalAmount;
		this.infoStatus = infoStatus;
		this.transferDate = transferDate;
		this.recPaymentLineId = recPaymentLineId;
		this.transmitDate = transmitDate;
		this.paymentStatus = paymentStatus;
		this.paymentStatusMessage = paymentStatusMessage;
		this.outboundStatus = outboundStatus;
	}
	public String getRefNo() {
		return RefNo;
	}
	public void setRefNo(String refNo) {
		RefNo = refNo;
	}
	public String getAgentId() {
		return agentId;
	}
	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}
	public String getTransactionNo() {
		return TransactionNo;
	}
	public void setTransactionNo(String transactionNo) {
		TransactionNo = transactionNo;
	}
	public String getControlCode() {
		return controlCode;
	}
	public void setControlCode(String controlCode) {
		this.controlCode = controlCode;
	}
	public BigDecimal getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}
	public String getInfoStatus() {
		return infoStatus;
	}
	public void setInfoStatus(String infoStatus) {
		this.infoStatus = infoStatus;
	}
	public Date getTransferDate() {
		return transferDate;
	}
	public void setTransferDate(Date transferDate) {
		this.transferDate = transferDate;
	}
	public long getRecPaymentLineId() {
		return recPaymentLineId;
	}
	public void setRecPaymentLineId(long recPaymentLineId) {
		this.recPaymentLineId = recPaymentLineId;
	}
	public Date getTransmitDate() {
		return transmitDate;
	}
	public void setTransmitDate(Date transmitDate) {
		this.transmitDate = transmitDate;
	}
	public int getPaymentStatus() {
		return paymentStatus;
	}
	public void setPaymentStatus(int paymentStatus) {
		this.paymentStatus = paymentStatus;
	}
	public String getPaymentStatusMessage() {
		return paymentStatusMessage;
	}
	public void setPaymentStatusMessage(String paymentStatusMessage) {
		this.paymentStatusMessage = paymentStatusMessage;
	}
	public String getOutboundStatus() {
		return outboundStatus;
	}
	public void setOutboundStatus(String outboundStatus) {
		this.outboundStatus = outboundStatus;
	}
	@Override
	public String toString() {
		return "ResPaymentInfo [RefNo=" + RefNo + ", agentId=" + agentId + ", TransactionNo=" + TransactionNo + ", controlCode=" + controlCode + ", totalAmount=" + totalAmount + ", infoStatus="
				+ infoStatus + ", transferDate=" + transferDate + ", recPaymentLineId=" + recPaymentLineId + ", transmitDate=" + transmitDate + ", paymentStatus=" + paymentStatus
				+ ", paymentStatusMessage=" + paymentStatusMessage + ", outboundStatus=" + outboundStatus + "]";
	}
    
    
}
