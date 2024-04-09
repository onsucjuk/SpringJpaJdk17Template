package kopo.poly.service;

import kopo.poly.dto.SeoulSiMarketDTO;

public interface ISiMarketService {

    String apiURL = "http://openapi.seoul.go.kr:8088";

    SeoulSiMarketDTO getSiMarketRes() throws Exception;

}
