# 백엔드 프레임워크 실습 과제
=> Spring Boot를 활용해 REST API를 구현하는 실습 과제입니다.

# ItemDto
=> Item의 데이터를 전달하기 위해 작성한 DTO(Data Transfer Object) 클래스.
    , REST API에서 Item 데이터를 조회하거나 생성 혹은 수정한 결과를 전달할 때
    "id", "name", "price"를 하나의 객체로 관리하기 위해 사용

# APIResponse
=> API의 응답 형식을 일관되게 통일하기 위해 작성한 클래스
    ( JSON 구조로 응답하도록 하는 조건 D에 충족 )

* Response Format(응답 형태)
{
"status" : "success",
"data" : { ... }
}


# Controller
=>ItemController를 통해 Item에 대한 REST API를 구현했습니다.__
* '@RestController'를 사용해 RSET API Controller를 구성했으며,
    '@RequestMapping'을 통해 API의 기본 경로를 설정했습니다.

* 각 API에서 '@GetMapping', '@PostMapping', '@PutMapping','@DeleteMapping'을 
    사용해 HTTP method에 따라 요청을 처리하도록 구현했습니다.

1. GET : 조회
    - 전체 Item을 조회
    - '@PathVariable'을 이용해 ID 기반 단건 조회
        => URL 경로의 ID 값을 '@PathVariable'을 통해 전달받도록 구현했습니다.
    - '@RequestParam'을 이용한 Item 검색 및 페이지네이션 구현
        => 검색 API에서는 '@RequestParam'을 사용해 'keyword','page','size 값을 전달받고,
            stream의 'filter()','skip()','limit()'을 이용해 검색 및 페이지네이션을 구현했습니다.

2. POST : Item 생성
    - 새로운 Item을 생성
        => '@RequestBody'를 사용해 HTTP Body의 JSON 데이터를 'ItemCreateREquest' 객체로 
            전달받아 새로운 Item을 생성하도록 구현했습니다.
    - Request Header를 포함한 Item 요청
        => 또한 '@RequestHeader'를 사용해 'X-USER-ID','Autorization', 'JWT-TOKEN'과 같은
            HTTP Header 값을 전달받을 수 있도록 구현했습니다.
* Item 생성에 성공한 경우 201 Created를 반환하도록 구현 
   
3. PUT : 수정
   => '@PathVAriable'을 통해 수정할 Item의 ID를 전달받고, '@RequestBody'를 통해
        수정할 데이터를 전달받로고 구현했습니다.
   - ID를 이용한 Item 정보 수정
   - Item 가격 수정
        => /{'id'}/price 경로를 사용해 해당 Item의 가격만 수정하도록 구현했습니다.
     (1)해당 ID의 Item이 존재 (X) -> 404 Not Found 반환
     (2)정상적으로 가격이 수정 -> 200 Ok 반환
     (3)가격 수정 처리 과정에서 예외 발생 (try-catch) -> 500 Internal Server Error 반환

4. DELETE : 삭제
   => '@PathVariable'을 사용해 URL 경로의 ID 값을 전달받아 
        해당 Item을 삭제하도록 구현했습니다.
   - ID를 이용한 Item 삭제
     => 'store.remove()'를 사용해 해당 ID의 저장된 Item을 삭제하도록 구현했습니다.
   - 전체 Item 삭제
     => 'store.clear()'를 사용해 저장된 Item을 삭제하도록 구현했습니다.
     (1)저장된 Item 없음 -> 404 Not Found 반환
     (2)정상적으로 전체 삭제가 완료 -> 204 No Content 반환
     (3)전체 삭제 처리 과정에서 예외 발생(try-catch) -> 503 Service Unavailable 반환

( GET/POST/PUT/DELETE를 각각 2개 이상(GET 메서드는 3개) 구현하는 조건 A에 충족 
   (ItemController3에 작성했습니다.) )

# Middleware(미들웨어)
=> LoggingInterceptor를 구현해 API 요청시 HTTP method, 요청 URI
    , 응답 상태 코드를 로그로 출력하도록 구현했습니다.
( 요청, 응답 로그 출력을 하는 기타 기능으로 조건 B에 충족 )

# HTTP Status Code(HTTP 상태코드)
- 200(OK) : 조회, 수정 성공
- 201(Created) : Item 생성 성공
- 204(NoContent) : Item 삭제 성공
- 400(BadRequest) : 입력값 또는 ID 형식 오류
- 404(NotFound) : 대상 Item 없음
- 500(InternalServerError) : 서버 내부 처리 중 오류 발생
- 503(Service Unavailable) : 일시적으로 서비스 이용 불가

=> 400, 404 등의 상태 코드는 편의 메서드를 사용했지만 201와 500, 503은 ResponseEntity.status()를 통해
    상태 코드를 지정했고, 숫자를 직접 작성하는 대신 코드의 의미를 명확하게 표현하는 방법으로 
    import org.springframework.http.HttpStatus;를 추가하여 201, 500, 503을 
    HttpStatus.CREATED, HttpStatus.INTERNAL_SERVER_ERROR와 HttpStatus.SERVICE_UNAVAILABLE을 사용해 구현했습니다.

( 2XX, 4XX, 5XX 응답 코드를 각 범주에서 최소 2개 이상 사용하는 조건 C에 충족 )

# API 실행 및 테스트
=> Postman을 사용해 구현한 REST API의 요청 및 응답을 테스트 했습니다.
1. POST를 이용해 Item 생성 및 201 Created
2. GET을 이용한 전체/단건 조회
3. 잘못된 ID 요청에 대한 400 Bad Request & 404 Not Found
4. @RequestParam을 이용한 검색 및 페이지네이션
5. @RequestHeader를 이용한 Header 값 전달
6. PUT을 이용한 Item 정보 수정
7. PUT을 이용한 Item 가격만 수정
8. DELETE를 이용한 전체/단건 삭제
9. LoggingInterceptor의 요청 및 응답 로그 

* API 실행 결과는 실습 과제 테스트 및 실행 결과 폴더에 첨부했습니다.
* 500과 503 상태 코드는 서버 내부 처리 과정에서 예외가 발생한 경우를 처리하도록 코드에 구현했으며,
  ,정상적인 API 테스트 과정에서는 의도적으로 서버 오류를 발생시키지 않았습니다. 