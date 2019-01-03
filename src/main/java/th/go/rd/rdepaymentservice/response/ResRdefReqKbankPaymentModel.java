package th.go.rd.rdepaymentservice.response;

public class ResRdefReqKbankPaymentModel {
	
	private String header;
	private String detail;
	private String item;
	private String trailer;
	private String tokenId;
	private String result;
	private String redirectURL;
	
	
	public ResRdefReqKbankPaymentModel() {
		super();
	}


	public ResRdefReqKbankPaymentModel(String header, String detail, String item, String trailer, String tokenId,
			String result) {
		super();
		this.header = header;
		this.detail = detail;
		this.item = item;
		this.trailer = trailer;
		this.tokenId = tokenId;
		this.result = result;
	}


	public String getHeader() {
		return header;
	}


	public void setHeader(String header) {
		this.header = header;
	}


	public String getDetail() {
		return detail;
	}


	public void setDetail(String detail) {
		this.detail = detail;
	}


	public String getItem() {
		return item;
	}


	public void setItem(String item) {
		this.item = item;
	}


	public String getTrailer() {
		return trailer;
	}


	public void setTrailer(String trailer) {
		this.trailer = trailer;
	}


	public String getTokenId() {
		return tokenId;
	}


	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}


	public String getResult() {
		return result;
	}


	public void setResult(String result) {
		this.result = result;
	}

	

	public String getRedirectURL() {
		return redirectURL;
	}


	public void setRedirectURL(String redirectURL) {
		this.redirectURL = redirectURL;
	}


	@Override
	public String toString() {
		return "ResRdefReqKbankPaymentModel [header=" + header + ", detail=" + detail + ", item=" + item + ", trailer="
				+ trailer + ", tokenId=" + tokenId + ", result=" + result + "]";
	}
	
	

}
