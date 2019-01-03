package th.go.rd.rdepaymentservice.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;
import th.go.rd.rdepaymentservice.dto.MasterParamTypeDto;

public class ReqSubmitParameterModel {

	@ApiModelProperty(value = "เลขค่าค่งที่")
	private long id;
	
	@JsonProperty("masterParamType")
	@ApiModelProperty(value = "ประเภทค่าคงที่",required=true)
	private MasterParamTypeDto masterParamTypeDto;

	@NotBlank(message = "{app.val-resp.notblank}")
	@Size(min= 1 ,max = 30,message="{app.val-resp.size}")
	@ApiModelProperty(value = "รหัสค่าคงที่",required = true)
	private String paramCode;

	@NotBlank(message = "{app.val-resp.notblank}")
	@Size(min = 1,max = 100,message="{app.val-resp.size}")
	@ApiModelProperty(value = "ค่าคงที่",required=true)
	private String paramValue;

	public ReqSubmitParameterModel() {
		super();
	}
	
	

	public MasterParamTypeDto getMasterParamTypeDto() {
		return masterParamTypeDto;
	}



	public void setMasterParamTypeDto(MasterParamTypeDto masterParamTypeDto) {
		this.masterParamTypeDto = masterParamTypeDto;
	}



	public void setId(long id) {
		this.id = id;
	}



	public ReqSubmitParameterModel(long id, @NotNull MasterParamTypeDto masterParamTypeDto,
			@NotNull @Size(min = 1, max = 60) String paramCode, @NotNull @Size(min = 1, max = 200) String paramValue) {
		super();
		this.id = id;
		this.masterParamTypeDto = masterParamTypeDto;
		this.paramCode = paramCode;
		this.paramValue = paramValue;
	}



	public long getId(){
		return id;
	}

	public String getParamCode() {
		return paramCode;
	}
	public void setParamCode(String paramCode) {
		this.paramCode = paramCode;
	}
	public String getParamValue() {
		return paramValue;
	}
	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}

	
}
