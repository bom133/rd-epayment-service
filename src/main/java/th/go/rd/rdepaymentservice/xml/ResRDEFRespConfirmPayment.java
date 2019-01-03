package th.go.rd.rdepaymentservice.xml;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@XmlRootElement(name="RDEFRespConfirmPayment")
@JsonRootName("RDEFRespConfirmPayment")
public class ResRDEFRespConfirmPayment {
	
	@JsonProperty("PaymentInformation")
	private ResPaymentInformation epay;

	@XmlElement(name="PaymentInformation")
	public ResPaymentInformation getEpay() {
		return epay;
	}

	public void setEpay(ResPaymentInformation epay) {
		this.epay = epay;
	}

	@Override
	public String toString() {
		return "RDEFRespConfirmPayment [epay=" + epay + "]";
	}
	
	
	
}
