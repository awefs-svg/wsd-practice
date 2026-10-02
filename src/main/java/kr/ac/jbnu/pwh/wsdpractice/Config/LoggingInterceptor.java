package kr.ac.jbnu.pwh.wsdpractice.Config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component // LoggingInterceptor를 Spring이 관리하는 객체로 등록
// HandlerInterceptor : contoller가 요청을 처리하기 전이나 처리한 후에 중간에서 작업할 수 있게 해주는 기능
public class LoggingInterceptor implements HandlerInterceptor{
    @Override // 이미 정해져 있는 method를 다시 정의해서 사용

    // Controller가 실행되기 전 ( 요청이 들어왔을 때 )
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) throws Exception{
        System.out.println("[LoggingInterceptor] 요청 시작 :"
        + request.getMethod() + " " + request.getRequestURI());
        return true;
    }
    // 요청 처리가 모두 끝난 후 실행
    @Override
    public void afterCompletion(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            Exception ex) throws Exception {
        System.out.println(
                "[LoggingInterceptor] 요청 완료 : status = "
                        + response.getStatus()); // 최종 HTTP 응답 코드를 가져옴
    }
}
