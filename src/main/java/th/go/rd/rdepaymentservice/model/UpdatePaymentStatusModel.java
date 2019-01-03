package th.go.rd.rdepaymentservice.model;

public class UpdatePaymentStatusModel {

    private String ResponseCode;
    private String ResponseDesc;
    
	public UpdatePaymentStatusModel() {
		super();
	}
	public UpdatePaymentStatusModel(String ResponseCode, String ResponseDesc) {
		super();
		this.ResponseCode = ResponseCode;
		this.ResponseDesc = ResponseDesc;
	}
	public String getResponseCode() {
		return ResponseCode;
	}
	public void setResponseCode(String responseCode) {
		ResponseCode = responseCode;
	}
	public String getResponseDesc() {
		return ResponseDesc;
	}
	public void setResponseDesc(String responseDesc) {
		ResponseDesc = responseDesc;
	}
	
	@Override
	public String toString() {
		return "UpdatePaymentStatusModel [ResponseCode=" + ResponseCode + ", ResponseDesc=" + ResponseDesc + "]";
	}
	
}
