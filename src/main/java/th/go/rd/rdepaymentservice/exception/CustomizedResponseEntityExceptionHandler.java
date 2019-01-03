package th.go.rd.rdepaymentservice.exception;

import static java.util.stream.Collectors.toList;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	@Value("${app.exception-handler.stacktrace.enable ?: false}")
	private boolean stacktraceEnable;

	@ExceptionHandler(Exception.class)
	public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {

		List<ApiGlobalError> apiGlobalErrors = new ArrayList<ApiGlobalError>();
		ApiGlobalError apiGlobalError = null;
		if (stacktraceEnable) {
			String stackTrace = ExceptionUtils.getStackTrace(ex);
			apiGlobalError = new ApiGlobalError(HttpStatus.INTERNAL_SERVER_ERROR.toString(), ex.getMessage(),
					stackTrace);
		} else {
			apiGlobalError = new ApiGlobalError(HttpStatus.INTERNAL_SERVER_ERROR.toString(), ex.getMessage());
		}
		apiGlobalErrors.add(apiGlobalError);
		ApiErrorsView apiErrorsView = new ApiErrorsView(null, apiGlobalErrors);
		return new ResponseEntity<>(apiErrorsView, HttpStatus.INTERNAL_SERVER_ERROR);

	}

	@ExceptionHandler(NotFoundException.class)
	public final ResponseEntity<Object> handleNotFoundException(NotFoundException ex, WebRequest request) {
		List<ApiGlobalError> apiGlobalErrors = new ArrayList<ApiGlobalError>();
		ApiGlobalError apiGlobalError = null;
		if (stacktraceEnable) {
			String stackTrace = ExceptionUtils.getStackTrace(ex);
			apiGlobalError = new ApiGlobalError(HttpStatus.NOT_FOUND.toString(), ex.getMessage(), stackTrace);
		} else {
			apiGlobalError = new ApiGlobalError(HttpStatus.NOT_FOUND.toString(), ex.getMessage());
		}
		apiGlobalErrors.add(apiGlobalError);
		ApiErrorsView apiErrorsView = new ApiErrorsView(null, apiGlobalErrors);
		return new ResponseEntity<>(apiErrorsView, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler({ SQLException.class, DataAccessException.class })
	public final ResponseEntity<Object> handleDBExceptions(Exception ex, WebRequest request) {
		List<ApiGlobalError> apiGlobalErrors = new ArrayList<ApiGlobalError>();
		ApiGlobalError apiGlobalError = null;
		if (stacktraceEnable) {
			String stackTrace = ExceptionUtils.getStackTrace(ex);
			apiGlobalError = new ApiGlobalError(HttpStatus.PRECONDITION_FAILED.toString(), ex.getMessage(), stackTrace);
		} else {
			apiGlobalError = new ApiGlobalError(HttpStatus.PRECONDITION_FAILED.toString(), ex.getMessage());
		}
		apiGlobalErrors.add(apiGlobalError);
		ApiErrorsView apiErrorsView = new ApiErrorsView(null, apiGlobalErrors);
		return new ResponseEntity<>(apiErrorsView, HttpStatus.PRECONDITION_FAILED);// 412 Pre condition fail
	}

	@ExceptionHandler({ RestException.class })
	public final ResponseEntity<Object> handleRestExceptions(RestException ex, WebRequest request) {
		System.out.println("handleRestExceptions");
		List<ApiGlobalError> apiGlobalErrors = ex.getApiGlobalErrors();
		List<ApiFieldError> apiFieldErrors = ex.getApiFieldErrors();
		ApiErrorsView apiErrorsView = new ApiErrorsView(apiFieldErrors, apiGlobalErrors);
		return new ResponseEntity<>(apiErrorsView, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(NumberFormatException.class)
	public final ResponseEntity<Object> handleMessageNotReadableExceptions(NumberFormatException ex, WebRequest request) {
		List<ApiGlobalError> apiGlobalErrors = new ArrayList<ApiGlobalError>();
		ApiGlobalError apiGlobalError = null;
		if (stacktraceEnable) {
			String stackTrace = ExceptionUtils.getStackTrace(ex);
			apiGlobalError = new ApiGlobalError(HttpStatus.BAD_REQUEST.toString(), ex.getMessage(), stackTrace);
		} else {
			apiGlobalError = new ApiGlobalError(HttpStatus.BAD_REQUEST.toString(), ex.getMessage());
		}
		apiGlobalErrors.add(apiGlobalError);
		ApiErrorsView apiErrorsView = new ApiErrorsView(null, apiGlobalErrors);
		return new ResponseEntity<>(apiErrorsView, HttpStatus.BAD_REQUEST);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
				System.out.println("handleMethodArgumentNotValid");
		BindingResult bindingResult = ex.getBindingResult();

		List<ApiFieldError> apiFieldErrors = bindingResult.getFieldErrors().stream()
				.map(fieldError -> new ApiFieldError(
						String.format("%1$s.%2$s", fieldError.getObjectName(), fieldError.getField()),
						fieldError.getCode(), fieldError.getDefaultMessage(), fieldError.getRejectedValue()))
				.collect(toList());

		List<ApiGlobalError> apiGlobalErrors = bindingResult.getGlobalErrors().stream()
				.map(globalError -> new ApiGlobalError(globalError.getCode(), globalError.getDefaultMessage()))
				.collect(toList());

		ApiErrorsView apiErrorsView = new ApiErrorsView(apiFieldErrors, apiGlobalErrors);
		return new ResponseEntity<>(apiErrorsView, HttpStatus.BAD_REQUEST);
	}
}
