package th.go.rd.rdepaymentservice.xml;

import javax.xml.bind.annotation.XmlElement;

public class EpayStdReturnInfoModel {

	private String rdRedirectUrl;
	private String rdDirectUrl;

	
	
	@XmlElement(name="RedirectURL")
	public String getRdRedirectUrl() {
		return rdRedirectUrl;
	}


	public void setRdRedirectUrl(String rdRedirectUrl) {
		this.rdRedirectUrl = rdRedirectUrl;
	}


	@XmlElement(name="DirectURL")
	public String getRdDirectUrl() {
		return rdDirectUrl;
	}


	public void setRdDirectUrl(String rdDirectUrl) {
		this.rdDirectUrl = rdDirectUrl;
	}


	@Override
	public String toString() {
		return "EpayStdReturnInfoModel [rdRedirectUrl=" + rdRedirectUrl + ", rdDirectUrl=" + rdDirectUrl + "]";
	}


}
