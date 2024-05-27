package kopo.poly.service;

import kopo.poly.dto.SeoulSiMarketDTO;

import java.util.List;

public interface IGuMarketService {

    /**
     *
     * mongoDB에서 받아온 데이터 가공해서 넘겨주기 (매출액)
     *
     */
    List<SeoulSiMarketDTO> getGuMarketRes(int rank, String preYear, String recYear, String seoulLocationCd) ;
    List<SeoulSiMarketDTO> getGuStoreRes(int rank, String preYear, String recYear, String seoulLocationCd) ;
    List<SeoulSiMarketDTO> getGuCloseStoreRes(int rank, String preYear, String recYear, String seoulLocationCd) ;

    SeoulSiMarketDTO getGuLatLon(String seoulLocationCd) ;

    List<SeoulSiMarketDTO> getGuMarketLikeIndutyCd(String induty, String guSelect);
    List<SeoulSiMarketDTO> getGuMarketIndutyNm(String induty, String guSelect);

    List<SeoulSiMarketDTO> getIndutyMarket();

    List<SeoulSiMarketDTO> getSortedMarketByIndutyNm(String indutyNm);
    List<SeoulSiMarketDTO> getSortedMarketByIndutyCd(String indutyNm);

}
