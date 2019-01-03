package th.go.rd.rdepaymentservice.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlElement;

import io.swagger.annotations.ApiModelProperty;

public class CreateMasterSystemInfo {

	@ApiModelProperty(value="รหัสระบบ", required=true)
	@NotBlank(message = "{app.val-resp.notblank}")
	private String systemCode;
	
	@ApiModelProperty(value="ชื่อระบบ", required=true)
	@NotBlank(message = "{app.val-resp.notblank}")
	private String systemName;
	
	@ApiModelProperty(value="คำอธิบาย", required=false)
	private String description;
	
	@ApiModelProperty(value="รหัสสำหรับยืนยันตัวตน", required=true)
	@NotBlank(message = "{app.val-resp.notblank}")
	private String clientId;
	
	@ApiModelProperty(value="รหัสสำหรับยืนยันตัวตน", required=true)
	@NotBlank(message = "{app.val-resp.notblank}")
	private String secretId;
	
	@ApiModelProperty(value="สถานะ", required=true)
	@Size(min = 1, max = 1, message = "{app.val-resp.size-fix}")
	private String status;
	
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
