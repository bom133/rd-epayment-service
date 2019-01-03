package th.go.rd.rdepaymentservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class EpayRolePermissionDto {

	@ApiModelProperty(value = "หมายเลขสิทธิ์การใช้งาน")
	private long epayRolePermissionId;

	@ApiModelProperty(value = "สิทธิ์การใช้งาน")
	@JsonProperty("epayAuthorization")
	private EpayAuthorizationDto epayAuthorizationDto;

	@ApiModelProperty(value = "สิทธิ์การใช้งานระบบ")
	@JsonProperty("masterSystemInfo")
	private MasterSystemInfoDto masterSystemInfoDto;

	public EpayRolePermissionDto() {
		super();
	}

	public EpayRolePermissionDto(long epayRolePermissionId, EpayAuthorizationDto epayAuthorizationDto, MasterSystemInfoDto masterSystemInfoDto) {
		super();
		this.epayRolePermissionId = epayRolePermissionId;
		this.epayAuthorizationDto = epayAuthorizationDto;
		this.masterSystemInfoDto = masterSystemInfoDto;
	}

	public long getEpayRolePermissionId() {
		return epayRolePermissionId;
	}

	public void setEpayRolePermissionId(long epayRolePermissionId) {
		this.epayRolePermissionId = epayRolePermissionId;
	}

	public EpayAuthorizationDto getEpayAuthorizationDto() {
		return epayAuthorizationDto;
	}

	public void setEpayAuthorizationDto(EpayAuthorizationDto epayAuthorizationDto) {
		this.epayAuthorizationDto = epayAuthorizationDto;
	}

	public MasterSystemInfoDto getMasterSystemInfoDto() {
		return masterSystemInfoDto;
	}

	public void setMasterSystemInfoDto(MasterSystemInfoDto masterSystemInfoDto) {
		this.masterSystemInfoDto = masterSystemInfoDto;
	}

	@Override
	public String toString() {
		return "EpayRolePermissionDto [epayRolePermissionId=" + epayRolePermissionId + ", epayAuthorizationDto=" + epayAuthorizationDto + ", masterSystemInfoDto=" + masterSystemInfoDto + "]";
	}	
	
}
