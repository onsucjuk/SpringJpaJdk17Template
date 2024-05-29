package kopo.poly.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@Builder
public record RedisDTO(
        String text // 텍스트 문구

) {

}
