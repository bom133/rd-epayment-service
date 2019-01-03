package th.go.rd.rdepaymentservice.model;

import java.io.Serializable;

public class ApiView implements Serializable {

	private static final long serialVersionUID = -3459649314460725723L;
	private int code=0;// 0:SUCCESS , 1:ERROR

	public ApiView(int code) {
		this.code = code;
	}
	
	public int getCode() {
		return code;
	}
}
