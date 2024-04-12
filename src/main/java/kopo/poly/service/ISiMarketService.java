package kopo.poly.service;

import kopo.poly.dto.SeoulSiMarketDTO;

import java.util.List;
import java.util.Map;

public interface ISiMarketService {

    String apiURL = "http://openapi.seoul.go.kr:8088";
    String marketData = "VwsmMegaSelngW";
    String storeData = "VwsmMegaStorW";

    List<SeoulSiMarketDTO> getSiMarketRes(int rank) throws Exception;

    List<Map<String, Object>> getSiInfoByYearAndData(String year, String data) throws Exception;


}
