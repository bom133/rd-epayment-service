package th.go.rd.rdepaymentservice.xml;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@XmlRootElement(name="EPayCSConfirmPayment")
@JsonRootName("EPayCSConfirmPayment")
public class CSConfirmPaymentModel {
	
	@JsonProperty("EPaymentInformation")
	private CSConfirmPaymentInfoModel epay;
	
	@JsonProperty("PaymentStatusDetail")
	private CSConfPaymentStatusModel paymentStatus;
	
	@JsonProperty("SendMethod")
	private String sendMethod;
	
	@XmlElement(name="EPaymentInformation")
	public CSConfirmPaymentInfoModel getEpay() {
		return epay;
	}

	public void setEpay(CSConfirmPaymentInfoModel epay) {
		this.epay = epay;
	}

	@XmlElement(name="PaymentStatusDetail")
	public CSConfPaymentStatusModel getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(CSConfPaymentStatusModel paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	@XmlElement(name="SendMethod")
	public String getSendMethod() {
		return sendMethod;
	}

	public void setSendMethod(String sendMethod) {
		this.sendMethod = sendMethod;
	}

	@Override
	public String toString() {
		return "CSConfirmPaymentModel [epay=" + epay + ", paymentStatus=" + paymentStatus + ", sendMethod=" + sendMethod
				+ "]";
	}
}
