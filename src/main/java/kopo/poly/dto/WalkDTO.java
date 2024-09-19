package kopo.poly.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.util.List;
import java.util.Map;


@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@Builder
public record WalkDTO(

        // API GET Parameter
        String startIdx, // 시작인덱스
        String endIdx, // 마지막 인덱스

        /* 여기서부터 리턴 데이터 */
        Map<String, Object> IotVdata018, // json 리턴 값
        String modelNm, // 모델번호
        String serialNo, // 시리얼
        String sensingIme, // 측정시간
        String visitorCount // 방문자수

) {
}
