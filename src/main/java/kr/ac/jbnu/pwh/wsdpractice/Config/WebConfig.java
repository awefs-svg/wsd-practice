package kr.ac.jbnu.pwh.wsdpractice.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration // 해당 클래스가 Spring 설정을 담당하는 클래스라고 알려줌
// Interceptor를 실제 API 요청에 적용
public class WebConfig implements WebMvcConfigurer{ //Interceptor를 등록하는 설정
    // * Spring이 LoggingInterceptor 객체를 만들어서 관리해줌
    private final LoggingInterceptor loggingInterceptor;

    public WebConfig(LoggingInterceptor loggingInterceptor){
        this.loggingInterceptor = loggingInterceptor;//Spring이 만들어 놓은 LoggingInterceptor를 WebConfig가 받아서 사용
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(loggingInterceptor).addPathPatterns
                ("api/v1/**", // ** : 그 뒤에 어떤 경로가 오더라도 포함.
                "/api/v2/**",
                "/api/v3/**");
    }
}
