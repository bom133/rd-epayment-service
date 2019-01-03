package th.go.rd.rdepaymentservice.dto;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class EpayAuthorizationDto {
	
	@ApiModelProperty(value = "หมายเลขสิทธิ์การใช้งาน")
	private long epayAuthorizationId;

	@ApiModelProperty(value = "รหัสสิทธิ์การใช้งาน", required=true)
	@NotBlank(message = "{app.val-resp.notblank}")
	@Size(max = 30,message="{app.val-resp.size}")
	private String authCode;

	@ApiModelProperty(value = "ชื่อสิทธิ์การใช้งาน")
	@Size(max= 200,message="{app.val-resp.size}")
	private String description;
	
	@ApiModelProperty(value = "สถานะการใช้งาน", required=true)
	@JsonProperty("masterStatus")
	private MasterStatusDto masterStatusDto;
	
	
	@ApiModelProperty(value = "ระบบที่มีสิทธิ์ใช้งาน")
	private List<EpayRolePermissionDto> epayRolePermissions;
	
	
	public long getEpayAuthorizationId() {
		return epayAuthorizationId;
	}


	public void setEpayAuthorizationId(long epayAuthorizationId) {
		this.epayAuthorizationId = epayAuthorizationId;
	}


	public EpayAuthorizationDto() {
		super();
	}

	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	public MasterStatusDto getMasterStatusDto() {
		return masterStatusDto;
	}


	public void setMasterStatusDto(MasterStatusDto masterStatusDto) {
		this.masterStatusDto = masterStatusDto;
	}


	public List<EpayRolePermissionDto> getEpayRolePermissions() {
		return epayRolePermissions;
	}


	public void setEpayRolePermissions(List<EpayRolePermissionDto> epayRolePermissions) {
		this.epayRolePermissions = epayRolePermissions;
	}


	public EpayAuthorizationDto(long epayAuthorizationId, String authCode, String description,
			MasterStatusDto masterStatusDto, List<EpayRolePermissionDto> epayRolePermissions) {
		super();
		this.epayAuthorizationId = epayAuthorizationId;
		this.authCode = authCode;
		this.description = description;
		this.masterStatusDto = masterStatusDto;
		this.epayRolePermissions = epayRolePermissions;
	}


	@Override
	public String toString() {
		return "EpayAuthorizationDto [epayAuthorizationId=" + epayAuthorizationId + ", authCode=" + authCode
				+ ", description=" + description + ", masterStatusDto=" + masterStatusDto + ", epayRolePermissions="
				+ epayRolePermissions + "]";
	}
}
