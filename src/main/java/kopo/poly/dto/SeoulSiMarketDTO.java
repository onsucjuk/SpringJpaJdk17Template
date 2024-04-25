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
        String indutyNm, // 업종명
        double monthSales, // 당월매출금액
        String fMonthSales, // 당월매출금액 소숫점2째 자리까지 표기
        double maleSales, // 남성매출금액
        double femaleSales, // 여성매출금액
        double age10Sales, // 10대 매출금액
        double age20Sales, // 20대 매출금액
        double age30Sales, // 30대 매출금액
        double age40Sales, // 40대 매출금액
        double age50Sales, // 50대 매출금액
        double age60Sales, // 60대 이상 매출금액

        // 형변환된 매출금액
        String age10Sale, // 10대 매출금액
        String age20Sale, // 20대 매출금액
        String age30Sale, // 30대 매출금액
        String age40Sale, // 40대 매출금액
        String age50Sale, // 50대 매출금액
        String age60Sale, // 60대 이상 매출금액
        String age10SalesRate, // 10대 매출금액
        String age20SalesRate, // 20대 매출금액
        String age30SalesRate, // 30대 매출금액
        String age40SalesRate, // 40대 매출금액
        String age50SalesRate, // 50대 매출금액
        String age60SalesRate, // 60대 이상 매출금액
        double salesDiff, // 매출액 증가량
        String salesRate, // 매출액 증가율
        double storeCount, //점포수
        double storeDiff, //점포수 증가량
        String storeRate, // 점포수 증가율
        double closeStoreCount, // 폐업수
        double closeStoreDiff, // 폐업수 증가량
        String closeStoreRate, // 폐업수 증가률

        double lat, // 위도
        double lon // 경도


) {
}
