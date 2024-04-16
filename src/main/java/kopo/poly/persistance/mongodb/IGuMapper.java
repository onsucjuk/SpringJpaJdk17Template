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

    List<SeoulSiMarketDTO> getGuSalesList(String seoulGuYear, String seoulLocationCd, String colNm) throws Exception;

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
}
