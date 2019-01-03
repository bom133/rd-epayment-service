package th.go.rd.rdepaymentservice.xml;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;

@XmlRootElement(name="BillPayRDEFRespBillStatus")
public class ResBillStatusModel {
	@JsonProperty("BillPaymentInfomation")
	private ResBillStatusInfoModel bill;
	
	public ResBillStatusModel() {
		super();
	}

	@XmlElement(name="BillPaymentInfomation")
	public ResBillStatusInfoModel getBill() {
		return bill;
	}

	public void setBill(ResBillStatusInfoModel bill) {
		this.bill = bill;
	}

	public ResBillStatusModel(ResBillStatusInfoModel bill) {
		super();
		this.bill = bill;
	}

	@Override
	public String toString() {
		return "ResBillStatusModel [bill=" + bill + "]";
	}
	
	

}
