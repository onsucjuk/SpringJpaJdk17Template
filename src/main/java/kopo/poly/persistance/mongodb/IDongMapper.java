package kopo.poly.persistance.mongodb;

import kopo.poly.dto.SeoulSiMarketDTO;

import java.util.List;

public interface IDongMapper {

    /**
     *
     * 행정동 기반 매출액 DB 데이터 가져오기 (서울 전체 : 모든 데이터)
     *
     * @param  seoulDongYear 기준년도
     * @param  indutyName 업종 소분류(이름과 같은 값 가져오기)
     * @param  colNm 컬렉션명
     *
     */

    List<SeoulSiMarketDTO> getDongSalesAllByName(String seoulDongYear, String indutyName, String colNm) throws Exception;

    /**
     *
     * 행정동 기반 매출액 DB 데이터 가져오기 (서울 전체 : 모든 데이터)
     *
     * @param  seoulDongYear 기준년도
     * @param  indutySort 업종 대분류(대분류 코드로 시작하는 업종들 가져오기)
     * @param  colNm 컬렉션명
     *
     */
    List<SeoulSiMarketDTO> getDongSalesAllBySort(String seoulDongYear, String indutySort, String colNm) throws Exception;

    /**
     *
     * 행정동 기반 점포수 DB 데이터 가져오기
     *
     * @param  seoulDongYear 기준년도
     * @param  indutyName 업종 소분류(이름과 같은 값 가져오기)
     * @param  colNm 컬렉션명
     *
     */

    List<SeoulSiMarketDTO> getDongStoreAllByName(String seoulDongYear, String indutyName, String colNm) throws Exception;

    /**
     *
     * 행정동 기반 점포수 DB 데이터 가져오기 (서울 전체 : 모든 데이터)
     *
     * @param  seoulDongYear 기준년도
     * @param  indutySort 업종 대분류(대분류 코드로 시작하는 업종들 가져오기)
     * @param  colNm 컬렉션명
     *
     */
    List<SeoulSiMarketDTO> getDongStoreAllBySort(String seoulDongYear, String indutySort, String colNm) throws Exception;



    /**
     *
     * 행정동 기반 매출액 DB 데이터 가져오기 (서울 전체 : 모든 데이터)
     *
     * @param  seoulDongYear 기준년도
     * @param  locationCd 지역코드
     * @param  indutyName 업종 소분류(이름과 같은 값 가져오기)
     * @param  colNm 컬렉션명
     *
     */

    List<SeoulSiMarketDTO> getDongSalesByLocationCdAndName(String seoulDongYear, String locationCd, String indutyName, String colNm) throws Exception;

    /**
     *
     * 행정동 기반 매출액 DB 데이터 가져오기 (서울 전체 : 모든 데이터)
     *
     * @param  seoulDongYear 기준년도
     * @param  locationCd 지역코드
     * @param  indutySort 업종 대분류(대분류 코드로 시작하는 업종들 가져오기)
     * @param  colNm 컬렉션명
     *
     */
    List<SeoulSiMarketDTO> getDongSalesByLocationCdAndSort(String seoulDongYear, String locationCd, String indutySort, String colNm) throws Exception;

    /**
     *
     * 행정동 기반 점포수 DB 데이터 가져오기
     *
     * @param  seoulDongYear 기준년도
     * @param  locationCd 지역코드
     * @param  indutyName 업종 소분류(이름과 같은 값 가져오기)
     * @param  colNm 컬렉션명
     *
     */

    List<SeoulSiMarketDTO> getDongStoreByLocationCdAndName(String seoulDongYear, String locationCd, String indutyName, String colNm) throws Exception;

    /**
     *
     * 행정동 기반 점포수 DB 데이터 가져오기 (서울 전체 : 모든 데이터)
     *
     * @param  seoulDongYear 기준년도
     * @param  locationCd 지역코드
     * @param  indutySort 업종 대분류(대분류 코드로 시작하는 업종들 가져오기)
     * @param  colNm 컬렉션명
     *
     */
    List<SeoulSiMarketDTO> getDongStoreByLocationCdAndSort(String seoulDongYear, String locationCd, String indutySort, String colNm) throws Exception;

}
