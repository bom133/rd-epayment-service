package th.go.rd.rdepaymentservice.request;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;

public class ReqEpayGetPaymentInfoProcessing {

	@ApiModelProperty(value = "รายการชำระภาษีที่เกิดขึ้นในระบบชำระภาษี ตั้งแต่ วัน เวลาที่ระบบ")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC+07:00")
	private Date transmitDate;

	public ReqEpayGetPaymentInfoProcessing() {
		super();
	}

	public ReqEpayGetPaymentInfoProcessing(Date transmitDate) {
		super();
		this.transmitDate = transmitDate;
	}

	public Date getTransmitDate() {
		return transmitDate;
	}

	public void setTransmitDate(Date transmitDate) {
		this.transmitDate = transmitDate;
	}

	@Override
	public String toString() {
		return "PaymentInfoModel [transmitDate=" + transmitDate + "]";
	} 
	
	
}
