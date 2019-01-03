package th.go.rd.rdepaymentservice.dto;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;

public class CertificateApiAuthDto implements Serializable {
	private int certAuthorizationId;
	private String description;
	private String status;
	private String authCode;
	private String[] systemInfoId;
	
	@Column(name="EPAY_AUTHORIZATION_ID")
	public int getCertAuthorizationId() {
		return certAuthorizationId;
	}
	public void setCertAuthorizationId(int certAuthorizationId) {
		this.certAuthorizationId = certAuthorizationId;
	}
	@Column(name="DESCRIPTION")
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Column(name="STATUS")
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	@Column(name="AUTH_CODE")
	public String getAuthCode() {
		return authCode;
	}
	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}
	
	public String[] getSystemInfoId() {
		return systemInfoId;
	}
	public void setSystemInfoId(String[] systemInfoId) {
		this.systemInfoId = systemInfoId;
	}
}
