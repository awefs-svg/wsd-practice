package kr.ac.jbnu.pwh.wsdpractice.api.dto;

public class ApiResponse<T> {

    private String status;
    private T data;

    public ApiResponse(String status, T data){
        this.status = status;
        this.data = data;
    }
    public String getStatus() {
        return status;
    }
    public T getData(){
        return data;
    }
}
// Java 객체를 만든 다음 Spring에게 반환
// controller에서 return ResponseEntity.ok(item);을 반환시 Spring이 알아서 JSON으로 바꿔줌