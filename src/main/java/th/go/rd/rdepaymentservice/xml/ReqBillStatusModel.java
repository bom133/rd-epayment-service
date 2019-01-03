package th.go.rd.rdepaymentservice.xml;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import th.go.rd.rdepaymentservice.xml.ReqBillStatusInfoModel;

@XmlRootElement(name="BillPayCSReqBillStatus")
@JsonRootName("BillPayCSReqBillStatus")
public class ReqBillStatusModel {
	@JsonProperty("BillPaymentInfomation")
	private ReqBillStatusInfoModel bill;

	@XmlElement(name="BillPaymentInfomation")
	public ReqBillStatusInfoModel getBill() {
		return bill;
	}

	public void setBill(ReqBillStatusInfoModel bill) {
		this.bill = bill;
	}

	@Override
	public String toString() {
		return "BillPayCSReqBillStatus [bill=" + bill + "]";
	}
	
}
