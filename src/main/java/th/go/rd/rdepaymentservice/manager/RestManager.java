package th.go.rd.rdepaymentservice.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.BindingResult;

import th.go.rd.rdepaymentservice.config.AppConfig;
import th.go.rd.rdepaymentservice.exception.ApiErrorsView;
import th.go.rd.rdepaymentservice.exception.ApiFieldError;
import th.go.rd.rdepaymentservice.exception.ApiGlobalError;
import th.go.rd.rdepaymentservice.exception.RestException;
import th.go.rd.rdepaymentservice.model.ApiSuccessView;



public class RestManager {

	private static Logger logger = LoggerFactory.getLogger(RestManager.class);
	private List<ApiGlobalError> apiGlobalErrors = new ArrayList<>();
	private List<ApiFieldError> apiFieldErrors = new ArrayList<>();
	private ApiSuccessView apiSuccessView;
	private MessageSource messageSource = AppConfig.getMessageSource();

	public static RestManager getInstance() {
		return new RestManager();
	}

	public void addBindingResult(BindingResult bindingResult) {
		List<ApiGlobalError> apiGlobalErrors = bindingResult.getGlobalErrors().stream()
				.map(globalError -> new ApiGlobalError(globalError.getCode(), globalError.getDefaultMessage()))
				.collect(Collectors.toList());

		List<ApiFieldError> apiFieldErrors = bindingResult.getFieldErrors().stream()
				.map(fieldError -> new ApiFieldError(
						String.format("%1$s.%2$s", fieldError.getObjectName(), fieldError.getField()),
						fieldError.getCode(), fieldError.getDefaultMessage(), fieldError.getRejectedValue()))
				.collect(Collectors.toList());

		this.apiGlobalErrors.addAll(apiGlobalErrors);
		this.apiFieldErrors.addAll(apiFieldErrors);
	}

	public void addGlobalErrorbyProperty(String messageProperty) {
		addGlobalErrorbyProperty(messageProperty, null);
	}

	public void addGlobalErrorbyProperty(String messageProperty, Object[] obj) {
		try {
			String msg = messageSource.getMessage(messageProperty, obj, LocaleContextHolder.getLocale());
			String[] a = msg.split("::");
			
			if(obj != null) {
				for(Object ob : obj) {
					
					a[1] = a[1] + ob.toString();
					
				}
			}
			
			ApiGlobalError error = new ApiGlobalError(a[0], a[1]);
			apiGlobalErrors.add(error);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Ignore propertie not found [name : {}]", messageProperty);
			ApiGlobalError error = new ApiGlobalError(null, messageProperty);
			apiGlobalErrors.add(error);
		}
	}

	public void addFieldErrorbyProperty(String field, String messageProperty, String rejectedValue) {
		addFieldErrorbyProperty(field, messageProperty, null, rejectedValue);
	}

	// Note: messageProperty control language errorMessage
	public void addFieldErrorbyProperty(String field, String messageProperty, Object[] obj, String rejectedValue) {
		String msg = messageProperty;
		try {
			msg = messageSource.getMessage(messageProperty, obj, LocaleContextHolder.getLocale());
			String[] a = msg.split("::");
			ApiFieldError error = new ApiFieldError(field, a[0], a[1], rejectedValue);
			apiFieldErrors.add(error);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Ignore propertie not found [name : {}]", messageProperty);
			ApiFieldError error = new ApiFieldError(field, null, messageProperty, rejectedValue);
			apiFieldErrors.add(error);
		}
	}

	public boolean hasError() {
		return (!apiGlobalErrors.isEmpty() || !apiFieldErrors.isEmpty());
	}

	public void throwsException() {
		if (hasError()) {
			throw new RestException(apiGlobalErrors, apiFieldErrors);
		}
	}
	
	public List<ApiGlobalError> getApiGlobalErrors() {
		return apiGlobalErrors;
	}

	public List<ApiFieldError> getApiFieldErrors() {
		return apiFieldErrors;
	}

	public ApiErrorsView getError() {
		ApiErrorsView apiErrorsView = new ApiErrorsView(this.apiFieldErrors, this.apiGlobalErrors);
		return apiErrorsView;
	}
	
	public Object addSuccess(Object data) {
		return data;
	}
	
	public Object getSuccess() {
		return apiSuccessView;
	}
}
