package th.go.rd.rdepaymentservice.response;

public class ResponseModel {
	private String responseCode;
	private String responseDec;
	private Object data;
	public ResponseModel(String responseCode, String responseDec) {
		super();
		this.responseCode = responseCode;
		this.responseDec = responseDec;
	}
	public String getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}
	public String getResponseDec() {
		return responseDec;
	}
	public void setResponseDec(String responseDec) {
		this.responseDec = responseDec;
	}
	
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	@Override
	public String toString() {
		return "Responses [responseCode=" + responseCode + ", responseDec=" + responseDec + "]";
	}
}
