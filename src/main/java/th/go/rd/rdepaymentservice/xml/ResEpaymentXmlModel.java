package th.go.rd.rdepaymentservice.xml;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@XmlRootElement(name="Epayment")
@JsonRootName("Epayment")
public class ResEpaymentXmlModel {
	
	@JsonProperty("ResponseCode")
	String responseCode;
	
	@JsonProperty("ResponseMesage")
	String responseMessage;
	
	@JsonProperty("EncryptData")
	String encryptData;
	
	@JsonProperty("OutboundKeyID")
	@JsonInclude(Include.NON_NULL)
	String outboundKeyID;


	public ResEpaymentXmlModel() {
		super();
	}


	public ResEpaymentXmlModel(String responseCode, String responseMessage, String encryptData) {
		super();
		this.responseCode = responseCode;
		this.responseMessage = responseMessage;
		this.encryptData = encryptData;
	}

	@XmlElement(name="ResponseCode")
	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	@XmlElement(name="ResponseMesage")
	public String getResponseMessage() {
		return responseMessage;
	}


	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}

	@XmlElement(name="EncryptData")
	public String getEncryptData() {
		return encryptData;
	}


	public void setEncryptData(String encryptData) {
		this.encryptData = encryptData;
	}

	@XmlElement(name="OutboundKeyID")
	public String getOutboundKeyID() {
		return outboundKeyID;
	}

	public void setOutboundKeyID(String outboundKeyID) {
		this.outboundKeyID = outboundKeyID;
	}


	@Override
	public String toString() {
		return "ResEpaymentXmlModel [responseCode=" + responseCode + ", responseMessage=" + responseMessage
				+ ", encryptData=" + encryptData + ", outboundKeyID=" + outboundKeyID + "]";
	}


}
