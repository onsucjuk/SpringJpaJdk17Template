package kopo.poly.persistance.mongodb;

import kopo.poly.dto.SeoulSiMarketDTO;

import java.util.List;

public interface IGuMapper {

    /**
     *
     * 행정구 기반 매출액 DB 데이터 가져오기
     *
     * @param  seoulGuYear 기준년도
     * @param  seoulLocationCd 지역코드
     * @param  colNm 컬렉션명
     *
     */

    List<SeoulSiMarketDTO> getGuSalesList(String seoulGuYear, String seoulLocationCd,  String colNm) ;

    /**
     *
     * 행정구 기반 점포수 DB 데이터 가져오기
     *
     * @param  seoulGuYear 기준년도
     * @param  seoulLocationCd 지역코드
     * @param  colNm 컬렉션명
     *
     */

    List<SeoulSiMarketDTO> getGuStoreList(String seoulGuYear, String seoulLocationCd, String colNm) ;

    /**
     *
     * 행정구 기반 폐업수 DB 데이터 가져오기
     *
     * @param  seoulGuYear 기준년도
     * @param  seoulLocationCd 지역코드
     * @param  colNm 컬렉션명
     *
     */

    List<SeoulSiMarketDTO> getGuCloseStoreList(String seoulGuYear, String seoulLocationCd, String colNm) ;

    /**
     *
     * 행정구 코드 기반 좌표 데이터 가져오기
     *
     * @param  seoulLocationCd 지역코드
     * @param  colNm 컬렉션명
     *
     */

    SeoulSiMarketDTO getGuLatLon(String seoulLocationCd, String colNm) ;

    /**
     *
     * 연도, 행정구 코드, 업종명 기반 점포수 매출액 조회하기
     *
     * @param year 연도
     * @param induty 업종코드
     * @param  guSelect 지역코드
     * @param  colNm 컬렉션명
     *
     * @return 조회 결과
     *
     */

    SeoulSiMarketDTO getGuSalesGraphLikeInduty(String year, String induty, String guSelect, String colNm);

    /**
     *
     * 연도, 행정구 코드, 업종명 기반 점포수 조회하기
     *
     * @param year 연도
     * @param induty 업종코드
     * @param  guSelect 지역코드
     * @param  colNm 컬렉션명
     *
     * @return 조회 결과
     *
     */
    SeoulSiMarketDTO getGuStoreGraphLikeInduty(String year, String induty, String guSelect, String colNm);

    /**
     *
     * 연도, 행정구 코드, 업종명 기반 점포수 매출액 조회하기
     *
     * @param year 연도
     * @param induty 업종명
     * @param  guSelect 지역코드
     * @param  colNm 컬렉션명
     *
     * @return 조회 결과
     *
     */

    SeoulSiMarketDTO getGuSalesGraphByIndutyNm(String year, String induty, String guSelect, String colNm);

    /**
     *
     * 연도, 행정구 코드, 업종명 기반 점포수 조회하기
     *
     * @param year 연도
     * @param induty 업종명
     * @param  guSelect 지역코드
     * @param  colNm 컬렉션명
     *
     * @return 조회 결과
     *
     */
    SeoulSiMarketDTO getGuStoreGraphByIndutyNm(String year, String induty, String guSelect, String colNm);

    /**
     *
     * 연도 기준 매출액 합계 조회하기
     *
     * @param year 연도
     * @param  colNm 컬렉션명
     *
     * @return 조회 결과
     *
     */

    SeoulSiMarketDTO getAllSalesGraph(String year, String colNm);

    /**
     *
     * 연도 기준 점포수 합계 조회하기
     *
     * @param year 연도
     * @param  colNm 컬렉션명
     *
     * @return 조회 결과
     *
     */
    SeoulSiMarketDTO getAllStoreGraph(String year, String colNm);

    /**
     *
     * 업종 대분류로 연도별 매출액 지역 비중 조회하기
     *
     * @param year 연도
     * @param indutyNm 업종명
     * @param colNm 컬렉션명
     *
     * @return 조회 결과
     *
     */
    List<SeoulSiMarketDTO> getSortedMarketByIndutyNm(String year, String indutyNm, String colNm);



    /**
     *
     * 업종 소분류로 연도별 매출액 지역 비중 조회하기
     *
     * @param year 연도
     * @param indutCd 업종코드
     * @param colNm 컬렉션명
     *
     * @return 조회 결과
     *
     */
    List<SeoulSiMarketDTO> getSortedMarketByIndutyCd(String year, String indutCd, String colNm);

    /**
     *
     * 업종 대분류로 연도별 점포수 지역 비중 조회하기
     *
     * @param year 연도
     * @param indutyNm 업종명
     * @param colNm 컬렉션명
     *
     * @return 조회 결과
     *
     */
    List<SeoulSiMarketDTO> getSortedStoreByIndutyNm(String year, String indutyNm, String colNm);

    /**
     *
     * 업종 소분류로 연도별 점포수 지역 비중 조회하기
     *
     * @param year 연도
     * @param indutCd 업종코드
     * @param colNm 컬렉션명
     *
     * @return 조회 결과
     *
     */
    List<SeoulSiMarketDTO> getSortedStoreByIndutyCd(String year, String indutCd, String colNm);


}
