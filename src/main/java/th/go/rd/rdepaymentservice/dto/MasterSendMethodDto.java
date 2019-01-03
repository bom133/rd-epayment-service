package th.go.rd.rdepaymentservice.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class MasterSendMethodDto {

	@NotBlank(message = "{app.val-resp.notblank}")
	@Size(max = 10,message="{app.val-resp.size}")
	private String sendMethod;
	@Size(max = 200,message = "{app.val-resp.size}")
	private String description;
	public MasterSendMethodDto() {
		super();
	}
	public MasterSendMethodDto(String sendMethod, String description) {
		super();
		this.sendMethod = sendMethod;
		this.description = description;
	}
	public String getSendMethod() {
		return sendMethod;
	}
	public void setSendMethod(String sendMethod) {
		this.sendMethod = sendMethod;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Override
	public String toString() {
		return "MasterSendMethodDto [sendMethod=" + sendMethod + ", description=" + description + "]";
	}
	
	
}
