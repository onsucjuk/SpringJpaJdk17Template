package kopo.poly.service;

import kopo.poly.dto.SeoulSiMarketDTO;

import java.util.List;

public interface IDongMarketService {

    /**
     *
     * mongoDB에서 받아온 데이터 가공해서 넘겨주기 (매출액)
     *
     */
    List<SeoulSiMarketDTO> getDongMarketRes(int rank, String preYear, String recYear, String seoulLocationCd, String indutySort, String indutyName) throws Exception;
    List<SeoulSiMarketDTO> getDongStoreRes(int rank, String preYear, String recYear, String seoulLocationCd, String indutySort, String indutyName) throws Exception;
    List<SeoulSiMarketDTO> getDongCloseStoreRes(int rank, String preYear, String recYear, String seoulLocationCd, String indutySort, String indutyName) throws Exception;

    SeoulSiMarketDTO getDongLatLon(String seoulLocationCd) throws Exception;

}
