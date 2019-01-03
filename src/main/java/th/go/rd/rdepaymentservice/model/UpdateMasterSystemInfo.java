package th.go.rd.rdepaymentservice.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlElement;

import com.fasterxml.jackson.annotation.JsonProperty;


public class UpdateMasterSystemInfo {

	@JsonProperty("systemInfoId")
	private int systemInfoId;
	
	@NotBlank(message = "{app.val-resp.notblank}")
	@JsonProperty("systemCode")
	private String systemCode;
	
	@NotBlank(message = "{app.val-resp.notblank}")
	@JsonProperty("systemName")
	private String systemName;
	
	@JsonProperty("description")
	private String description;
	
	@NotBlank(message = "{app.val-resp.notblank}")
	@JsonProperty("clientId")
	private String clientId;
	
	@NotBlank(message = "{app.val-resp.notblank}")
	@JsonProperty("secretId")
	private String secretId;
	
	@Size(min = 1, max = 1, message = "{app.val-resp.size-fix}")
	@JsonProperty("status")
	private String status;

	@XmlElement(name="systemInfoId")
	public int getSystemInfoId() {
		return systemInfoId;
	}

	public void setSystemInfoId(int systemInfoId) {
		this.systemInfoId = systemInfoId;
	}

	@XmlElement(name="systemCode")
	public String getSystemCode() {
		return systemCode;
	}

	public void setSystemCode(String systemCode) {
		this.systemCode = systemCode;
	}

	@XmlElement(name="systemName")
	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	@XmlElement(name="description")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@XmlElement(name="clientId")
	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	@XmlElement(name="secretId")
	public String getSecretId() {
		return secretId;
	}

	public void setSecretId(String secretId) {
		this.secretId = secretId;
	}

	@XmlElement(name="status")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
