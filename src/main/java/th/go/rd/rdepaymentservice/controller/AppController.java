package th.go.rd.rdepaymentservice.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import th.go.rd.rdepaymentservice.component.AuthenticationFacade;
import th.go.rd.rdepaymentservice.constant.ApiCode;
import th.go.rd.rdepaymentservice.constant.LogLevel;
import th.go.rd.rdepaymentservice.constant.LogType;
import th.go.rd.rdepaymentservice.dto.MasterStatusDto;
import th.go.rd.rdepaymentservice.manager.RestManager;
import th.go.rd.rdepaymentservice.model.AppValidatorModel;
import th.go.rd.rdepaymentservice.model.EpayLogModel;
import th.go.rd.rdepaymentservice.service.EpayLogService;

@Configuration
@RestController
@RequestMapping("/epay")
public class AppController {

	private static final Logger logger = LoggerFactory.getLogger(AppController.class);

	@Value("${application.name}")
	private String applicationName;

	@Value("${build.version}")
	private String buildVersion;

	@Value("${build.timestamp}")
	private String buildTimestamp;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private AuthenticationFacade authenticationFacade;

	@Autowired
	private EpayLogService epayLogService;
	
	private static final String APP_SYS_MAINTENANCE = "app.sys-resp.maintenance-app";
	private static final String APP_MAN_NOTENABLE = "app.man-resp.not-enable";

	/*
	 * POST {URL: localhost:8080/epay/appinfo, Method:GET}
	 */
	@ApiOperation(value = "Display Application Informaition")
	@GetMapping(value = "/appinfo")
	public Object appInfo() {
		Object[] obj = { applicationName, buildVersion, buildTimestamp };
		String msg = messageSource.getMessage("msg.app.info", obj, LocaleContextHolder.getLocale());
		logger.info("API:appinfo,value:{} ", msg);

		RestManager exManager = RestManager.getInstance();
		return exManager.addSuccess(msg);
	}

	@ApiOperation(value = "Service validate field and throws exception")
	@PostMapping(value = "/app/exception/validate-throws", headers = "Accept-Language")
	public void appValidateFail(@Valid @RequestBody AppValidatorModel model) {
		logger.info("API:validate-throws");
	}

	@ApiOperation(value = "Service validate field and binding exception")
	@PostMapping(value = "/app/exception/validate-binding", headers = "Accept-Language")
	public void appValidateFail(@Valid @RequestBody AppValidatorModel model, BindingResult bindingResult) {
		RestManager exManager = RestManager.getInstance();
		exManager.addBindingResult(bindingResult);
		exManager.addGlobalErrorbyProperty(APP_SYS_MAINTENANCE);
		exManager.addGlobalErrorbyProperty(APP_MAN_NOTENABLE);
		exManager.addFieldErrorbyProperty("appValidatorModel.test", APP_MAN_NOTENABLE, null, "test");
		exManager.hasError();
		logger.info("API:validate-binding hasError:{}", exManager.hasError());
		exManager.throwsException();
	}

	public enum ExceptionCatalog {
		ADDEXCEPTION, ADDEXCEPTION_THROWS, THROWS, VIEW_ERROR;
	}

	@GetMapping(value = "/app/exception/pathvariable{exception-catalog}", headers = { "Accept-Language" })
	public Object appManageException(@PathVariable(value = "exception-catalog") ExceptionCatalog catalog) {
		logger.info("API:manage-exception exceptionNumber={} ", catalog);
		Map<String, Object> map = new HashMap<>();
		RestManager exManager = RestManager.getInstance();
		switch (catalog) {
		case ADDEXCEPTION:
			exManager.addGlobalErrorbyProperty(APP_SYS_MAINTENANCE);
			break;
		case ADDEXCEPTION_THROWS:
			exManager.addGlobalErrorbyProperty(APP_SYS_MAINTENANCE);
			exManager.throwsException();
			break;
		case THROWS:
			exManager.throwsException();
			break;
		default:// VIEW_ERROR
			map.put("appInfo", appInfo());
			exManager.addGlobalErrorbyProperty(APP_SYS_MAINTENANCE);
			exManager.addFieldErrorbyProperty("appValidatorModel.test", APP_MAN_NOTENABLE, null, "test");
			map.put("apiname", "VIEW_ERROR");
			map.put("viewerrors", exManager.getError());
		}

		return exManager.addSuccess(map);
	}

	@PostMapping(value = "/app/hasauthority/profileinfo", headers = {
			"Authorization=Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsibXMvYWRtaW4iLCJtcy91c2VyIiwibXcvYWRtaW5hcHAiXSwidXNlcl9uYW1lIjoiYWRtaW4iLCJzY29wZSI6WyJyb2xlX2FkbWluIl0sImV4cCI6MTUzNDM4Njg4MiwiYXV0aG9yaXRpZXMiOlsicm9sZV9hZG1pbiIsInVwZGF0ZV91c2VyIiwiY3JlYXRlX3VzZXIiLCJ2aWV3X3VzZXIiLCJkZWxldGVfdXNlciJdLCJqdGkiOiJkNmQ4MjRiYy1hNjQ4LTRkMGUtYjJlOS0wZjZlNmIzMGJkYjEiLCJlbWFpbCI6ImFkbWluQHdpc2Vzb2Z0LmNvbSIsImNsaWVudF9pZCI6ImFkbWluYXBwIn0.nNQbBQkPWcVCVwlTueJaTCyb2pmzyYTYSPADU1LWWTSn66QFyrMLTeqMnZ1aapHzNuEwwfmzVdSN4FiWoeojAufp3uhb8ytOAB425SVqWhm-XGFOn3K6CZG9IM0GRt03lpnFc7NW6MdUnDZKj_J91ZztLw4IwhAcuotYLdm4FunQafKuhQGibbcT-Fk3vvRu_Q_oAbwS6J10vJc34RKu5daTBrYiw9A8S6eKiJOiVRMa6X1PngasQj4d6yEBo3k53B71d6-UAADdve1AaNwClg-EcTDxu7fyk2PL3-ErELXt3m3128h1RLGAtNUgznf4VMyNtuKdCgVS5PiyIuJEPw" })
	@PreAuthorize("hasAuthority('role_admin')")
	public Object hasAuthorityMyProfile() {
		RestManager exManager = RestManager.getInstance();
		Authentication authentication = authenticationFacade.getAuthentication();
		return exManager.addSuccess(authentication);
	}

	@PostMapping(value = "/app/hasauthority/fail", headers = { "Authorization" })
	@PreAuthorize("hasAuthority('1')")
	public Object hasAuthorityFail() {
		RestManager exManager = RestManager.getInstance();
		Authentication authentication = authenticationFacade.getAuthentication();
		return exManager.addSuccess(authentication);
	}

	@GetMapping(value = "/app/basicAuth")
	public Object testBasicAuth(@RequestHeader(name = "client_id") String clientId,
			@RequestHeader(name = "secret_id") String secretId) {
		RestManager restManager = RestManager.getInstance();
		Object[] object = { clientId, secretId };
		return restManager.addSuccess(object);
	}

	@GetMapping(value = "/app/testLog", headers = { "Authorization", "Accept-Language" })
	public Object testLog(HttpServletRequest request) {
		RestManager restManager = RestManager.getInstance();
		logger.info(" ========== Test Log ========== ");
		
		String username = authenticationFacade.getAuthentication().getName();
		EpayLogModel log = new EpayLogModel(LogLevel.INFO, LogType.PAYMENT, ApiCode.SUBMIT_PAY_TAX,
				 username, "Test put log");

		epayLogService.validateLog(restManager, log);
		epayLogService.insertLog(restManager, log);
		restManager.throwsException();
		logger.info(" ========== End Test Log ========== ");
		return restManager.addSuccess("data");
	}

	@GetMapping(value= "/app/search")
	public Object testSearch(MasterStatusDto masterStatusDto){
		RestManager restManager = RestManager.getInstance();

		return restManager.addSuccess(masterStatusDto);
	}
}
