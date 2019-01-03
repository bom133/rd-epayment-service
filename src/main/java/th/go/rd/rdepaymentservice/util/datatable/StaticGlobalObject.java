package th.go.rd.rdepaymentservice.util.datatable;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class StaticGlobalObject implements ApplicationContextAware {
	
	private static StaticGlobalObject th;

	private ApplicationContext appC;

	@Override
	public void setApplicationContext(ApplicationContext arg0)
			throws BeansException {
				this.appC = arg0;
				th = this;
	}
	
	public static <T> T getBean(Class<T> clz) {
		return th.appC.getBean(clz);
	}
}
