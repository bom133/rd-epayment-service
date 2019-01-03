package th.go.rd.rdepaymentservice.service;

import java.util.Date;
import java.util.Set;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import th.go.rd.rdepaymentservice.constant.LogLevel;
import th.go.rd.rdepaymentservice.constant.LogType;
import th.go.rd.rdepaymentservice.manager.RestManager;
import th.go.rd.rdepaymentservice.model.EpayLogModel;

@Service
@Transactional
public class EpayLogService {

    private static final Logger logger = LoggerFactory.getLogger(EpayLogService.class);

    @Value("${application.loglevel}")
    private String logLevel;

    @Autowired
    PaymentLogService paymentLogService;

    @Autowired
    InternalServiceLogService internalServiceLogService;

    public void insertLog(RestManager restManager, EpayLogModel log) {
        if(restManager.hasError()){
			log.setError(restManager);
		}

        if (logLevel.equals("info")) {
            if (log.getLogLevel() == LogLevel.ERROR) {
                return;
            }
        }

        log.setResponseTime(new Date());
        if (log.getLogType() == LogType.PAYMENT) {
            paymentLogService.save(log);
        } else if (log.getLogType() == LogType.INTERNAL_SERVICE) {
            internalServiceLogService.save(log);
        }
    }

    public void insertLog(EpayLogModel log) {
        if (logLevel.equals("info")) {
            if (log.getLogLevel() == LogLevel.ERROR) {
                return;
            }
        }

        log.setResponseTime(new Date());
        if (log.getLogType() == LogType.PAYMENT) {
            paymentLogService.save(log);
        } else if (log.getLogType() == LogType.INTERNAL_SERVICE) {
            internalServiceLogService.save(log);
        }
    }

    public Set<ConstraintViolation<EpayLogModel>> validateLog(EpayLogModel logModel) {

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<EpayLogModel>> violations = validator.validate(logModel);
        for (ConstraintViolation<EpayLogModel> violation : violations) {
            logger.info(violation.getMessage());
        }
        return violations;
    }

    public Set<ConstraintViolation<EpayLogModel>> validateLog(RestManager restManager,EpayLogModel log) {

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<EpayLogModel>> violations = validator.validate(log);
        for (ConstraintViolation<EpayLogModel> violation : violations) {
            logger.info(violation.getMessage());
            logger.info(violation.getPropertyPath().toString());
            restManager.addFieldErrorbyProperty(violation.getPropertyPath().toString(), "app.man-resp.invalid_data",
                    null);
        }
        return violations;
    }

}
