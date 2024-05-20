package kopo.poly.service;

import kopo.poly.dto.SeoulSiMarketDTO;

import java.util.List;

public interface IGuMarketService {

    /**
     *
     * mongoDB에서 받아온 데이터 가공해서 넘겨주기 (매출액)
     *
     */
    List<SeoulSiMarketDTO> getGuMarketRes(int rank, String preYear, String recYear, String seoulLocationCd) throws Exception;
    List<SeoulSiMarketDTO> getGuStoreRes(int rank, String preYear, String recYear, String seoulLocationCd) throws Exception;
    List<SeoulSiMarketDTO> getGuCloseStoreRes(int rank, String preYear, String recYear, String seoulLocationCd) throws Exception;

    SeoulSiMarketDTO getGuLatLon(String seoulLocationCd) throws Exception;

    List<SeoulSiMarketDTO> getGuMarketLikeIndutyCd(String induty, String guSelect) throws Exception;

}
