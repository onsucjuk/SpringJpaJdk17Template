package kopo.poly.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import kopo.poly.dto.SeoulSiMarketDTO;
import kopo.poly.service.ISiMarketService;
import kopo.poly.util.NetworkUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SiMarketService implements ISiMarketService {

    @Value("${SiMarket.api.key}")
    private String apiKey;

    private Map<String, String> setSiMarketInfo() {
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("accept", "application/json");
        requestHeader.put("Content-Type", "application/json");
        requestHeader.put("appkey", apiKey);

        log.info("appKey : " + apiKey);

        return requestHeader;
    }

    @Override
    public SeoulSiMarketDTO getSiMarketRes() throws Exception {

        log.info(this.getClass().getName() + ".SeoulSiMarketDTO Start!");


        String apiParam = "/" + apiKey + "/" + "json" + "/" + "VwsmMegaSelngW" + "/" + "1135" + "/" + "1197";
        String apiParam2 = "/" + apiKey + "/" + "json" + "/" + "VwsmMegaSelngW" + "/" + "1058" + "/" + "1134";

        String json = NetworkUtil.get(ISiMarketService.apiURL + apiParam, this.setSiMarketInfo());
        String json2 = NetworkUtil.get(ISiMarketService.apiURL + apiParam2, this.setSiMarketInfo());
        /* log.info("json : " + json);*/
        /* log.info("json2 : " + json2);*/

        // JSON 구조를 Map 데이터 구조로 변경하기
        // 20233분기 데이터
        Map<String, Object> rMap = new ObjectMapper().readValue(json, LinkedHashMap.class);
        Map<String, Object> rVwsmMegaSelngW = (Map<String, Object>) rMap.get("VwsmMegaSelngW");
        List<Map<String, Object>> rContents = (List<Map<String, Object>>) rVwsmMegaSelngW.get("row");

        // 20232분기 데이터
        Map<String, Object> rMap2 = new ObjectMapper().readValue(json2, LinkedHashMap.class);
        Map<String, Object> rVwsmMegaSelngW2 = (Map<String, Object>) rMap2.get("VwsmMegaSelngW");
        List<Map<String, Object>> rContents2 = (List<Map<String, Object>>) rVwsmMegaSelngW2.get("row");

        Map<String, Object> firstItem = rContents.get(0);
        String indutyCdNm = (String) firstItem.get("SVC_INDUTY_CD_NM");
        System.out.println("indutyCdNm의 값: " + indutyCdNm);

        List<Map<String, Object>> filteredDataList = rContents2.stream()
                .filter(item -> "20232".equals(item.get("STDR_YYQU_CD")))
                .collect(Collectors.toList());

        int count = rContents2.size();
        int count2 = filteredDataList.size();
        System.out.println("rContents2의 갯수: " + count);
        System.out.println("filteredDataList의 갯수: " + count2);


        SeoulSiMarketDTO rDTO = SeoulSiMarketDTO
                .builder()
                .indusyNm(indutyCdNm)
                .build();

        log.info(this.getClass().getName() + ".SeoulSiMarketDTO End!");

        return rDTO;
    }
}
