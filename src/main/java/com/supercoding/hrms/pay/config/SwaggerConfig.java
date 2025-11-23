package com.supercoding.hrms.pay.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI hrmsAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("HRMS Payroll API")
                        .description("급여관리 API 명세서")
                        .version("1.0.0"));
    }
}
