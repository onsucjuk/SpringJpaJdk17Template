package kopo.poly.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;


@Builder
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public record WeatherDTO (

        String pageNo, // 요청 페이지 번호
        String numOfRows, // 결과 페이지 번호
        String dataType, // 결과 리턴 타입 JSON, XML ...
        String regId, // 예보구역(서울 11B10101)
        String authKey, // 인증키


        /* 여기서부터 리턴 데이터 */
        Map<String, Object> response, // json 리턴 값
        String announceTime, // 날짜
        String ta, // 온도
        String wf, // 날씨
        String rnSt // 강수 확률

){
}
