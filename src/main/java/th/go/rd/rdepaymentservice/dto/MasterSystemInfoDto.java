package th.go.rd.rdepaymentservice.dto;

import java.util.Date;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MasterSystemInfoDto {

	private int systemInfoId;
	
	@JsonProperty("masterStatus")
	private String masterStatus;
	
	@NotBlank(message = "{app.val-resp.notblank}")
	@Size(max = 50,message = "{app.val-resp.size}")
	private String systemCode;
	
	@NotBlank(message = "{app.val-resp.notblank}")
	@Size(max = 200,message = "{app.val-resp.size}")
	private String systemName;
	
	@Size(max = 500,message = "{app.val-resp.size}")
	private String description;
	
	@Size(max = 50,message = "{app.val-resp.size}")
	private String clientId;
	
	@Size(max = 50,message = "{app.val-resp.size}")
	private String secretId;

	public MasterSystemInfoDto() {
		super();
	}
	public MasterSystemInfoDto(int masterSystemInfoId, String masterStatusDto, String systemCode, String systemName, String description, String clientId, String secretId) {
		super();
		this.systemInfoId = systemInfoId;
		this.masterStatus = masterStatus;
		this.systemCode = systemCode;
		this.systemName = systemName;
		this.description = description;
		this.clientId = clientId;
		this.secretId = secretId;
	}
	@Column(name="MASTER_SYSTEM_INFO_ID")
	public int getSystemInfoId() {
		return systemInfoId;
	}
	public void setSystemInfoId(int systemInfoId) {
		this.systemInfoId = systemInfoId;
	}
	
	@Column(name="STATUS")
	public String getMasterStatus() {
		return masterStatus;
	}
	public void setMasterStatus(String masterStatus) {
		this.masterStatus = masterStatus;
	}
	
	@Column(name="SYSTEM_CODE")
	public String getSystemCode() {
		return systemCode;
	}
	public void setSystemCode(String systemCode) {
		this.systemCode = systemCode;
	}
	
	@Column(name="SYSTEM_NAME")
	public String getSystemName() {
		return systemName;
	}
	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}
	
	@Column(name="DESCRIPTION")
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Column(name="CLIENT_ID")
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	
	@Column(name="SECRET_ID")
	public String getSecretId() {
		return secretId;
	}
	public void setSecretId(String secretId) {
		this.secretId = secretId;
	}
}
