package kopo.poly.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public record SeoulSiMarketDTO(

        String seoulSiYear,	// 기준_년분기_코드
        String seoulGuYear,	// 기준_년분기_코드
        String seoulDongYear,	// 기준_년분기_코드
        String seoulLocationCd, // 지역코드
        String seoulLocationNm, // 지역이름
        String indutyCd, // 업종코드
        String indusyNm, // 업종명
        Long monthSales, // 당월매출금액
        Long maleSales, // 남성매출금액
        Long femaleSales, // 여성매출금액
        Long age10Sales, // 10대 매출금액
        Long age20Sales, // 20대 매출금액
        Long age30Sales, // 30대 매출금액
        Long age40Sales, // 40대 매출금액
        Long age50Sales, // 50대 매출금액
        Long age60Sales // 60대 이상 매출금액

) {
}
