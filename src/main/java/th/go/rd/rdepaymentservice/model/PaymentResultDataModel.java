package th.go.rd.rdepaymentservice.model;

public class PaymentResultDataModel {

    private String transmitDate;
    private String recShortName;
    private String paymentLineCode;
    private int paymentStatus;
    private String paymentStatusMessage;
    
	public PaymentResultDataModel() {
		super();
	}
	public PaymentResultDataModel(String transmitDate, String recShortName, String paymentLineCode, int paymentStatus, String paymentStatusMessage) {
		super();
		this.transmitDate = transmitDate;
		this.recShortName = recShortName;
		this.paymentLineCode = paymentLineCode;
		this.paymentStatus = paymentStatus;
		this.paymentStatusMessage = paymentStatusMessage;
	}
	public String getTransmitDate() {
		return transmitDate;
	}
	public void setTransmitDate(String transmitDate) {
		this.transmitDate = transmitDate;
	}
	public String getRecShortName() {
		return recShortName;
	}
	public void setRecShortName(String recShortName) {
		this.recShortName = recShortName;
	}
	public String getPaymentLineCode() {
		return paymentLineCode;
	}
	public void setPaymentLineCode(String paymentLineCode) {
		this.paymentLineCode = paymentLineCode;
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

}
