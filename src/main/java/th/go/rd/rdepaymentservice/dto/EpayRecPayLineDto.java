package th.go.rd.rdepaymentservice.dto;

import java.util.Date;

import javax.persistence.Column;

import com.fasterxml.jackson.annotation.JsonFormat;

public class EpayRecPayLineDto {

	private long epayReceiverPaymentLineId;
	private String payLineCode;
	private String recShortName;
	private String description;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC+07:00")
	private Date startDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC+07:00")
	private Date endDate;
	private String status;
	
	@Column(name="EPAY_RECEIVER_PAYMENT_LINE_ID")
	public long getEpayReceiverPaymentLineId() {
		return epayReceiverPaymentLineId;
	}
	public void setEpayReceiverPaymentLineId(long epayReceiverPaymentLineId) {
		this.epayReceiverPaymentLineId = epayReceiverPaymentLineId;
	}
	
	@Column(name="PAY_LINE_CODE")
	public String getPayLineCode() {
		return payLineCode;
	}
	public void setPayLineCode(String payLineCode) {
		this.payLineCode = payLineCode;
	}
	
	@Column(name="RECNAME")
	public String getRecShortName() {
		return recShortName;
	}
	public void setRecShortName(String recShortName) {
		this.recShortName = recShortName;
	}
	
	@Column(name="DESCRIPTION")
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Column(name="START_DATE")
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	@Column(name="END_DATE")
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	@Column(name="STATUS")
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
