package kopo.poly.dto;

import lombok.Builder;

@Builder
public record InterestDTO(

        String interestSeq,

        String userId,

        String seoulLocationCd,

        String seoulLocationNm,

        String indutyNm,

        String indutyCd,

        String existsYn

) {
}
