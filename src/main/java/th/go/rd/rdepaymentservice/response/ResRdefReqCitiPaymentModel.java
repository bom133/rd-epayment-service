package th.go.rd.rdepaymentservice.response;

public class ResRdefReqCitiPaymentModel {
	private String menuId;
	private String merchartId;
	private String appData;
	private String redirectURL;
	
	public ResRdefReqCitiPaymentModel() {
		super();
	}
	
	public ResRdefReqCitiPaymentModel(String menuId, String merchartId, String appData) {
		super();
		this.menuId = menuId;
		this.merchartId = merchartId;
		this.appData = appData;
	}
	public String getMenuId() {
		return menuId;
	}
	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}
	public String getMerchartId() {
		return merchartId;
	}
	public void setMerchartId(String merchartId) {
		this.merchartId = merchartId;
	}
	public String getAppData() {
		return appData;
	}
	public void setAppData(String appData) {
		this.appData = appData;
	}
	
	public String getRedirectURL() {
		return redirectURL;
	}

	public void setRedirectURL(String redirectURL) {
		this.redirectURL = redirectURL;
	}

	@Override
	public String toString() {
		return "ResRdefReqCitiPaymentModel [menuId=" + menuId + ", merchartId=" + merchartId + ", appData=" + appData
				+ "]";
	}
	
	
}
