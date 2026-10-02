package kr.ac.jbnu.pwh.wsdpractice.api.v3;

import kr.ac.jbnu.pwh.wsdpractice.api.dto.ItemDto;
import kr.ac.jbnu.pwh.wsdpractice.api.request.ItemCreateRequest;
import kr.ac.jbnu.pwh.wsdpractice.api.request.ItemUpdateRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v3/items")

public class ItemController3 {

    // 메모리용 임시 DB (예제용 )
    private final Map<Long, ItemDto> store = new HashMap<>();
    // -> Map : 데이터를 Key(Long타입의 ID)와 Value(ItemDto 객체)의 쌍으로 저장하는 자료구조
    //            & 이런 방식으로 데이터를 관리해야 한다는 인터페이스
    // -> HashMap : 그 Map을 실제로 구현한 클래스
    private long sequence = 1L;
    // 새로운 상품을 만들 때 ID를 자동으로 부여하기 위한 숫자

    // 1) GET : 전체 조회
    @GetMapping
    public List<ItemDto> getItems() { // getItems()를 실행하면 ItemDto 객체 여러 개를 담는 List를 반환
        return new ArrayList<>(store.values());
        // --> store에 저장되어 있는 모든 ItemDto를 꺼내서 List로 만들어 반환.
        // store.values() : store에서 value들만 가져옴
    }

    // 2) GET : 단건 조회 (PathVariable 사용)
    @GetMapping("/{id}")
    public ResponseEntity<ItemDto> getItem(@PathVariable Long id) {
        // @pathvariable = URL 경로에 들어있는 값을 변수로 받아서 사용하는 것
        ItemDto item = store.get(id); // Map에서 요첨받은 ID를 이용해 해당하는 상품(value)를 가져와 item 변수에 저장.

        if (id<=0){
            return ResponseEntity.badRequest().build(); // 400
        }

        if (item == null){
            return ResponseEntity.notFound().build();
            // ResponseEntity : Springboot에서 반환 형태를 지정해주는 역할로 REST API에서의 응답 형태를 자동으로 만들어주는 클래스
            // ResponseEntity<ItemDto> : Spring에서 HTTP 응답 전체를 직접 제어할 수 있게 해주는 객체
            // notFound().build() : 404와 Not Found 메시지를 반환. & 빌드 패턴
        }
        return ResponseEntity.ok(item); // 200
    }

    // 3) POST : 생성
    @PostMapping
    public ResponseEntity<ItemDto> CreateItem(@RequestBody ItemCreateRequest request){
        // (@RequestBody ItemCreateRequest request) << 파라미터
        ItemDto item = new ItemDto();
        item.setId(sequence++);
        item.setName(request.getName());
        item.setPrice(request.getPrice());

        store.put(item.getId(), item);

        return ResponseEntity.ok(item);
    }

    // 4) PUT : 수정
    @PutMapping("/{id}")
    public ResponseEntity<ItemDto> updateItem
    (@PathVariable Long id, @RequestBody ItemUpdateRequest request) {
        ItemDto item = store.get(id);
        if (item == null) {
            return ResponseEntity.notFound().build(); // 404
        }
        if (request.getName() != null) {
            item.setName(request.getName());
        }
        if (request.getPrice() != null) {
            item.setPrice(request.getPrice());
        }
        return ResponseEntity.ok(item);
    }

    // 5) DELETE : 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id){
        ItemDto removed = store.remove(id);
        // remove() : 삭제만 하는 것이 아닌 삭제된 Value를 반환
        if(removed == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
        // noContent : 204 응답
    }

    // /api/items/searh?keyword=abc&page=1 & size=10 ( 설치 경로 추가! ㅍ)
    @GetMapping("/search")
    public List<ItemDto> searchItems(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        // 예제 : keyword 포함된 name만 필터링
        return store.values().stream()
                .filter(item -> keyword == null || item.getName().contains(keyword))
                .skip((long) page * size)
                .limit(size)
                .toList();
    }

    @PostMapping("/with-header")
    public ResponseEntity<String> createItemWithHeader(
            @RequestBody ItemCreateRequest request,
            @RequestHeader(value = "X-USER-ID", required = false) String userId,
            @RequestHeader(value="Authorization", required = false) String authorization
            ,@RequestHeader(value = "JWT-TOKEN",required = true) String jwtToken
    ){
        StringBuilder sb = new StringBuilder();
        sb.append("요청한 아이템: ").append(request.getName())
                .append(", price=").append(request.getPrice()).append("\n")
                .append("X-USER-ID: ").append(userId).append("\n")
                .append("Authorization: ").append(authorization).append("\n")
                .append("JWT-TOKEN: ").append(jwtToken);

        return ResponseEntity.ok(sb.toString());
    }

    // PUT - 가격만 수정
    @PutMapping("/{id}/price")
    public ResponseEntity<ItemDto> updateItemPrice(
            @PathVariable Long id,
            @RequestBody ItemUpdateRequest request) {
        ItemDto item = store.get(id);
        if (item == null){ //해당 ID의 아이템이 없다면 404 error
            return ResponseEntity.notFound().build();
        }
        if (request.getPrice() != null){ // 요청으로 받은 가격으로 수정
            item.setPrice(request.getPrice());
        }
        return ResponseEntity.ok(item);
    }

    //500 : 서버 내부 오류
    @GetMapping("/error/500")
    public ResponseEntity<String> internalServerError() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("서버 내부 오류 발생");
    }
    // 503 : 서비스 이용 불가
    @GetMapping("/error/503")
    public ResponseEntity<String> serviceUnavailable() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("현재 서비스 이용 불가");
    }

    // DELETE - 전체 아이템 삭제하기
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAllItems(){
        if (store.isEmpty()){ // 저장된 아이템이 하나도 없으면 404 error
            return ResponseEntity.notFound().build();
        }
        store.clear(); // 모든 아이템을 삭제

        return ResponseEntity.noContent().build(); // 204
    }


}
