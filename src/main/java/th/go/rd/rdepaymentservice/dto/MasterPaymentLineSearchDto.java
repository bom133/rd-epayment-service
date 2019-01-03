package th.go.rd.rdepaymentservice.dto;

import javax.persistence.Column;

public class MasterPaymentLineSearchDto {

	private long paymentLineId;
	private String payLineCode;
	private String payLineName;
	private String status;
	
	@Column(name="MASTER_PAYMENT_LINE_ID")
	public long getPaymentLineId() {
		return paymentLineId;
	}
	public void setPaymentLineId(long paymentLineId) {
		this.paymentLineId = paymentLineId;
	}
	
	@Column(name="PAY_LINE_CODE")
	public String getPayLineCode() {
		return payLineCode;
	}
	public void setPayLineCode(String payLineCode) {
		this.payLineCode = payLineCode;
	}
	
	@Column(name="PAYLINENAME")
	public String getPayLineName() {
		return payLineName;
	}
	public void setPayLineName(String payLineName) {
		this.payLineName = payLineName;
	}
	
	@Column(name="STATUS")
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
