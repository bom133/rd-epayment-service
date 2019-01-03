package th.go.rd.rdepaymentservice.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class MasterParamTypeDto {

	@NotBlank(message = "{app.val-resp.notblank}")
	@Size(max = 20,message = "{app.val-resp.size}")
	private String paramType;
	@Size(max = 100,message = "{app.val-resp.size}")
	private String paramDescription;
	public MasterParamTypeDto() {
		super();
	}
	public MasterParamTypeDto(String paramType, String paramDescription) {
		super();
		this.paramType = paramType;
		this.paramDescription = paramDescription;
	}
	public String getParamType() {
		return paramType;
	}
	public void setParamType(String paramType) {
		this.paramType = paramType;
	}
	public String getParamDescription() {
		return paramDescription;
	}
	public void setParamDescription(String paramDescription) {
		this.paramDescription = paramDescription;
	}
	
	
}
