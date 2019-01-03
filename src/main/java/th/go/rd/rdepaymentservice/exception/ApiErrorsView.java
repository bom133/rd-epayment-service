package th.go.rd.rdepaymentservice.exception;

import java.util.List;

import th.go.rd.rdepaymentservice.model.ApiView;

public class ApiErrorsView extends ApiView {

	private static final long serialVersionUID = 8309912861269565591L;
	
	private List<ApiFieldError> fieldErrors;
	private List<ApiGlobalError> globalErrors;

	public ApiErrorsView(List<ApiFieldError> fieldErrors, List<ApiGlobalError> globalErrors) {
		super(1); //FAIL
		this.fieldErrors = fieldErrors;
		this.globalErrors = globalErrors;
	}

	public List<ApiFieldError> getFieldErrors() {
		return fieldErrors;
	}

	public List<ApiGlobalError> getGlobalErrors() {
		return globalErrors;
	}
}
