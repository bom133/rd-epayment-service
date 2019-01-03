package th.go.rd.rdepaymentservice.xml;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@XmlRootElement(name="BillPayCSConfirmPayment")
@JsonRootName("BillPayCSConfirmPayment")
public class ReqBillPayCSConfirmPayment {
	
	@JsonProperty("BillPaymentInformation")
	private ReqBillPaymentInformation payin;

	@XmlElement(name="BillPaymentInformation")
	public ReqBillPaymentInformation getPayin() {
		return payin;
	}

	public void setPayin(ReqBillPaymentInformation payin) {
		this.payin = payin;
	}

	@Override
	public String toString() {
		return "ReqBillPayCSConfirmPayment [payin=" + payin + "]";
	}

}
