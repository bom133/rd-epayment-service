package th.go.rd.rdepaymentservice.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModelProperty;

public class ReqSubmitMasterPaymentLineModel {

	@ApiModelProperty(value="รหัสช่องทางชำระเงิน", required=true)
	@NotBlank(message = "{app.val-resp.notblank}")
	@Size(max=10,message="{app.val-resp.max}")
	private String payLineCode;
	@ApiModelProperty(value="ชื่อช่องทางชำระเงิน", required=true)
	@NotBlank(message = "{app.val-resp.notblank}")
	@Size(max=200,message="{app.val-resp.max}")
	private String payLineName;
	@ApiModelProperty(value="สถานะการใช้งาน", required=true)
	@NotBlank(message = "{app.val-resp.notblank}")
	@Size(max=20,message="{app.val-resp.max}")
	private String payLineStatus;


	public ReqSubmitMasterPaymentLineModel() {
		super();
	}
	public ReqSubmitMasterPaymentLineModel(String payLineCode, String payLineName, String payLineStatus) {
		super();
		this.payLineCode = payLineCode;
		this.payLineName = payLineName;
		this.payLineStatus = payLineStatus;
	}
	
	public String getPayLineCode() {
		return payLineCode;
	}
	public void setPayLineCode(String payLineCode) {
		this.payLineCode = payLineCode;
	}
	public String getPayLineName() {
		return payLineName;
	}
	public void setPayLineName(String payLineName) {
		this.payLineName = payLineName;
	}
	public String getPayLineStatus() {
		return payLineStatus;
	}
	public void setPayLineStatus(String payLineStatus) {
		this.payLineStatus = payLineStatus;
	}
	@Override
	public String toString() {
		return "ReqSubmitMasterPaymentLintModel [payLineCode=" + payLineCode + ", payLineName=" + payLineName
				+ ", payLineStatus=" + payLineStatus + "]";
	}
}
