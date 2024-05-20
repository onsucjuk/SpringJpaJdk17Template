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

    List<SeoulSiMarketDTO> getGuSalesList(String seoulGuYear, String seoulLocationCd,  String colNm) throws Exception;

    /**
     *
     * 행정구 기반 점포수 DB 데이터 가져오기
     *
     * @param  seoulGuYear 기준년도
     * @param  seoulLocationCd 지역코드
     * @param  colNm 컬렉션명
     *
     */

    List<SeoulSiMarketDTO> getGuStoreList(String seoulGuYear, String seoulLocationCd, String colNm) throws Exception;

    /**
     *
     * 행정구 기반 폐업수 DB 데이터 가져오기
     *
     * @param  seoulGuYear 기준년도
     * @param  seoulLocationCd 지역코드
     * @param  colNm 컬렉션명
     *
     */

    List<SeoulSiMarketDTO> getGuCloseStoreList(String seoulGuYear, String seoulLocationCd, String colNm) throws Exception;

    /**
     *
     * 행정구 코드 기반 좌표 데이터 가져오기
     *
     * @param  seoulLocationCd 지역코드
     * @param  colNm 컬렉션명
     *
     */

    SeoulSiMarketDTO getGuLatLon(String seoulLocationCd, String colNm) throws Exception;

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

    SeoulSiMarketDTO getGuSalesGraph(String year, String induty, String guSelect, String colNm) throws Exception;

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
    SeoulSiMarketDTO getGuStoreGraph(String year, String induty, String guSelect, String colNm) throws Exception;
}
