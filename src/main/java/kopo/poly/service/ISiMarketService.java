package kopo.poly.service;

import kopo.poly.dto.SeoulSiMarketDTO;

import java.util.List;
import java.util.Map;

public interface ISiMarketService {

    String apiURL = "http://openapi.seoul.go.kr:8088";
    String marketData = "VwsmMegaSelngW";
    String storeData = "VwsmMegaStorW";

    List<SeoulSiMarketDTO> getSiMarketRes(int rank, String preYear, String recYear) throws Exception;
    List<SeoulSiMarketDTO> getSiStoreRes(int rank, String preYear, String recYear) throws Exception;
    List<SeoulSiMarketDTO> getSiStoreCloseRes(int rank, String preYear, String recYear) throws Exception;
    List<SeoulSiMarketDTO> getSeoulMarketLikeIndutyCd(String indutyCd) throws Exception;




}
