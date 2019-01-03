package th.go.rd.rdepaymentservice.model;

public class ApiSuccessView extends ApiView {

	private static final long serialVersionUID = 2080798628377525336L;

	private Object resp;

	public ApiSuccessView(Object resp) {
		super(0); //SUCCESS
		this.resp = resp;
	}

	public Object getResp() {
		return resp;
	}

}
