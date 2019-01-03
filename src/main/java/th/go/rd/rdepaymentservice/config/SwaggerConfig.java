package th.go.rd.rdepaymentservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import th.go.rd.rdepaymentservice.controller.AppController;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Value("${application.name}")
    private String applicationName;

    @Value("${build.version}")
    private String buildVersion;
	
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(new ApiInfoBuilder()
						.version(buildVersion)
						.title(applicationName)
						.description("").build())
				.select()
				.apis(RequestHandlerSelectors.basePackage(AppController.class.getPackage().getName()))
				.paths(PathSelectors.any()).build();
	}
}
