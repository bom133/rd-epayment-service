package th.go.rd.rdepaymentservice.config;

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import th.go.rd.rdepaymentservice.manager.OrmXmlManager;

@Configuration
public class OrmInitConfig {
	
	 private ResourceLoader resourceLoader;

	    @Autowired
	    public OrmInitConfig(ResourceLoader resourceLoader) {
	        this.resourceLoader = resourceLoader;
	    }
	
	 @PostConstruct
		public void appInit() {
		 	Resource resource = resourceLoader.getResource("classpath:orm.xml");
		 	try {
		        	InputStream stream = resource.getInputStream(); 
					OrmXmlManager.init(stream);
			} catch (IOException e) {
					e.printStackTrace();
			}		
		}
}
