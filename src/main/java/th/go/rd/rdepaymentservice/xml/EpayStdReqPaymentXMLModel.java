package th.go.rd.rdepaymentservice.xml;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name="EPayRDEFReqPayment")
public class EpayStdReqPaymentXMLModel {
	private EpayStdPaymentInfoModel epayInfo;
	 
	private EpayStdReturnInfoModel retInfo;
	
	
	
	public EpayStdReqPaymentXMLModel() {
		super();
	}

	public EpayStdReqPaymentXMLModel(EpayStdPaymentInfoModel epayInfo, EpayStdReturnInfoModel retInfo) {
		super();
		this.epayInfo = epayInfo;
		this.retInfo = retInfo;
	}

	@XmlElement(name="EPaymentInformation")
	public EpayStdPaymentInfoModel getEpayInfo() {
		return epayInfo;
	}

	public void setEpayInfo(EpayStdPaymentInfoModel epayInfo) {
		this.epayInfo = epayInfo;
	}

	@XmlElement(name="ReturnInformation")
	public EpayStdReturnInfoModel getRetInfo() {
		return retInfo;
	}

	public void setRetInfo(EpayStdReturnInfoModel retInfo) {
		this.retInfo = retInfo;
	}

	@Override
	public String toString() {
		return "EpayStdReqPaymentXMLModel [epayInfo=" + epayInfo + ", retInfo=" + retInfo + "]";
	}
}
