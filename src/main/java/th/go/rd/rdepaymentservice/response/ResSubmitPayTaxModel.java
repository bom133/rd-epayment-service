package th.go.rd.rdepaymentservice.response;


public class ResSubmitPayTaxModel {
	private ResponseModel response;
	private ResSubmitPayTaxDataModel data;
	
	public ResSubmitPayTaxModel(ResponseModel response, ResSubmitPayTaxDataModel data) {
		super();
		this.response = response;
		this.data = data;
	}
	public ResponseModel getResponse() {
		return response;
	}
	public void setResponse(ResponseModel response) {
		this.response = response;
	}
	public ResSubmitPayTaxDataModel getData() {
		return data;
	}
	public void setData(ResSubmitPayTaxDataModel data) {
		this.data = data;
	}
	@Override
	public String toString() {
		return "ResSubmitPayTaxModel [response=" + response + ", data=" + data + "]";
	}
	


	
	
}
