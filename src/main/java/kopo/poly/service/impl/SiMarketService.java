package kopo.poly.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import kopo.poly.dto.SeoulSiMarketDTO;
import kopo.poly.service.ISiMarketService;
import kopo.poly.util.NetworkUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SiMarketService implements ISiMarketService {

    @Value("${siMarket.api.key}")
    private String apiKey;

    private Map<String, String> setSiMarketInfo() {
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("accept", "application/json");
        requestHeader.put("Content-Type", "application/json");
        requestHeader.put("appkey", apiKey);

        return requestHeader;
    }

    // data 변수 매출액데이터API, 점포수데이터API
    String marketData  = ISiMarketService.marketData;
    String storeData  = ISiMarketService.storeData;

    //
    @Override
    public List<Map<String, Object>> getSiDataByYearAndData(String year, String data) throws Exception {

        log.info(this.getClass().getName() + ".getSiInfoByYearAndData Start!");

        // API Index Number(요청인자) 1000회까지 호출 가능
        int start = 1;
        int end = start + 999;

        // API 호출 반복 횟수
        int i = 1;

        List<Map<String, Object>> rContents = new ArrayList<>();


        while(true) {

            String apiParam = "/" + apiKey + "/" + "json" + "/" + data + "/" + start + "/" + end;

            String json = NetworkUtil.get(ISiMarketService.apiURL + apiParam, this.setSiMarketInfo());

            /*System.out.print("json : " + json);*/

            Map<String, Object> tMap = new ObjectMapper().readValue(json, LinkedHashMap.class);
            Map<String, Object> rData = tMap.containsKey(data) ?
                    (Map<String, Object>) tMap.get(data) : new HashMap<>();

            if(rData.isEmpty()) { // 호출 데이터가 없으면 변수값 초기화 후 종료

                i = 1;
                start = 1;
                end = start + 999;

                break;
            } else {

                Map<String, Object> tData = (Map<String, Object>) tMap.get(data);
                List<Map<String, Object>> tContents = (List<Map<String, Object>>) tData.get("row");

                rContents.addAll(tContents);

                log.info(data + "API " + i + "Times Data Count : " +tContents.size());

                start = end+1;
                end = start + 999;
                i++;

            }
        }

        log.info(data + "API Count : " + rContents.size());

        // 가져온 데이터에서 분기 데이터 필터
        List<Map<String, Object>> fData = rContents.stream()
                .filter(item -> year.equals(item.get("STDR_YYQU_CD")))
                .collect(Collectors.toList());

        log.info(this.getClass().getName() + ".getSiInfoByYearAndData End!");

        return fData;

    }

    /**
     *
     * API로 받아온 데이터 가공해서 넘겨주기 (매출액)
     *
     */
    @Override
    public List<SeoulSiMarketDTO> getSiMarketRes(int rank, String preYear, String recYear) throws Exception {

        log.info(this.getClass().getName() + ".SeoulSiMarketDTO Start!");

        // 넘겨줄 Data
        List<Map<String, Object>> tData = new ArrayList<>();

        // 가져온 매출 데이터에서 이번분기 데이터 필터
        List<Map<String, Object>> fMarketRec = getSiDataByYearAndData(recYear, marketData);

        // 가져온 매출 데이터에서 이전분기 데이터 필터
        List<Map<String, Object>> fMarketPre = getSiDataByYearAndData(preYear, marketData);

        // 가져온 점포 데이터에서 이번분기 데이터 필터
        List<Map<String, Object>> fStoreRec = getSiDataByYearAndData(recYear, storeData);

        // 가져온 점포 데이터에서 이전분기 데이터 필터
        List<Map<String, Object>> fStorePre = getSiDataByYearAndData(preYear, storeData);

        for ( int j = 0; j < fMarketRec.size(); j++) {

            // 2023년 기준년도 매출액 데이터
            // j 횟수 돌리는 데이터 jData
            Map<String, Object> jData = fMarketRec.get(j);
            String jIndutyCdNm = (String) jData.get("SVC_INDUTY_CD_NM");
            String jIndutyCd = (String) jData.get("SVC_INDUTY_CD");
            double jSelng = (double) jData.get("THSMON_SELNG_AMT");

            log.info("기준년도 업종명 : " + jIndutyCdNm);
            log.info("기준년도 매출액 : " + jSelng);

            // 가져온 데이터에서 비교년도에서 기준년도 산업 이름 기준으로 데이터 가져오기
            List<Map<String, Object>> kDataPre = fMarketPre.stream()
                    .filter(item -> jIndutyCdNm.equals(item.get("SVC_INDUTY_CD_NM")))
                    .collect(Collectors.toList());

            // 가져온 점포 데이터에서 기준년도에서 기준년도 산업 이름 기준으로 데이터 가져오기
            List<Map<String, Object>> kStoreRec = fStoreRec.stream()
                    .filter(item -> jIndutyCdNm.equals(item.get("SVC_INDUTY_CD_NM")))
                    .collect(Collectors.toList());

            // 가져온 점포 데이터에서 비교년도에서 기준년도 산업 이름 기준으로 데이터 가져오기
            List<Map<String, Object>> kStorePre = fStorePre.stream()
                    .filter(item -> jIndutyCdNm.equals(item.get("SVC_INDUTY_CD_NM")))
                    .collect(Collectors.toList());

            // 업종명으로 분류된 비교년도 데이터 매출액
            double kSelng = 0;

            for (Map<String, Object> item : kDataPre) {

                kSelng= (double) item.get("THSMON_SELNG_AMT");

            }

            /*log.info("비교년도 매출액 :" + kSelng);*/

            // 기준년도 데이터 점포수, 폐업수, 폐업률
            double jSimilrIndutyStorCo = 0;

            for (Map<String, Object> item : kStoreRec) {

                jSimilrIndutyStorCo = (double) item.get("SIMILR_INDUTY_STOR_CO");

            }

            /*log.info("기준년도 점포수 :" + jSimilrIndutyStorCo);*/

            // 비교년도 데이터 점포수, 폐업수, 폐업률
            double kSimilrIndutyStorCo = 0;

            for (Map<String, Object> item : kStorePre) {

                kSimilrIndutyStorCo = (double) item.get("SIMILR_INDUTY_STOR_CO");

            }

            /*log.info("비교년도 점포수 :" + kSimilrIndutyStorCo);*/

            // 점포당 매출액 기준년도 jSales, 비교년도 kSales 1만 단위
            double jSales = (jSelng / jSimilrIndutyStorCo) / 10000;
            double kSales = (kSelng / kSimilrIndutyStorCo) / 10000;

            // 점포당 매출액 증가량 1만 단위
            long salesDiff = Math.round(jSales - kSales);

            // 점포당 매출액 상승률
            double salesRate = salesDiff / kSales;

            /*log.info(jIndutyCdNm + "매출액 차이 : " + salesDiff);
            log.info("비교년도 점포당 매출액 : " + kSales);
            log.info(jIndutyCdNm + "매출액 증가률 : " + salesRate);*/

            // 필요한 값 업종명, 업종코드, 매출액, 매출액 증가량, 증가율
            Map<String, Object> inputData = new HashMap<>();
            inputData.put("indusyNm", jIndutyCdNm);
            inputData.put("indutyCd", jIndutyCd);
            inputData.put("monthSales", jSales);
            inputData.put("salesDiff", salesDiff);
            inputData.put("salesRate", salesRate);

            // 리스트에 추가
            tData.add(inputData);
        }

        // tData 매출액 증가률 기준으로 높은 순서로 정렬
        Collections.sort(tData, Comparator.comparingDouble((Map<String, Object> map) -> (double) map.get("salesRate")).reversed());

        // 상위 n개 요소 선택 (parameter rank)
        List<Map<String, Object>> topSalesRate = tData.subList(0, Math.min(rank, tData.size()));

        List<SeoulSiMarketDTO> rList = new ArrayList<>();

        // DecimalFormat 객체 생성 데이터 포맷
        DecimalFormat df = new DecimalFormat("#.##");

        for (Map<String, Object> data : topSalesRate) {

            double monthSalesDouble = (double)data.get("monthSales");
            String fMonthSales = df.format(monthSalesDouble);
            String indutyCd = (String)data.get("indutyCd");
            String indutyNm = (String)data.get("indusyNm");

            // 매출액 상승률을 %로 계산하여 소수점 두 자리까지 표시
            double salesRatePercent = (double) data.get("salesRate") * 100;
            String salesRate = df.format(salesRatePercent);

            long salesDiff = (long)data.get("salesDiff");

            log.info("Top"+ rank + " Data 데이터");
            log.info("fMonthSales : " + fMonthSales);
            log.info("indutyCd : " + indutyCd);
            log.info("indutyNm : " +indutyNm);
            log.info("salesRate : " + salesRate);
            log.info("salesDiff : " + salesDiff);
            SeoulSiMarketDTO seoulMarketDTO = SeoulSiMarketDTO.builder()
                    .fMonthSales(fMonthSales)
                    .indutyCd(indutyCd)
                    .indutyNm(indutyNm)
                    .salesRate(salesRate)
                    .salesDiff(salesDiff)
                    .build();
            rList.add(seoulMarketDTO);

        }

        log.info(this.getClass().getName() + ".SeoulSiMarketDTO End!");

        return rList;
    }


    /**
     *
     * API로 받아온 데이터 가공해서 넘겨주기 (점포수)
     *
     */
    @Override
    public List<SeoulSiMarketDTO> getSiStoreRes(int rank, String preYear, String recYear) throws Exception {

        log.info(this.getClass().getName() + ".getSiStoreRes Start!");

        // 넘겨줄 Data
        List<Map<String, Object>> tData = new ArrayList<>();


        // 가져온 점포 데이터에서 이번분기 데이터 필터
        List<Map<String, Object>> fStoreRec = getSiDataByYearAndData(recYear, storeData);

        // 가져온 점포 데이터에서 이전분기 데이터 필터
        List<Map<String, Object>> fStorePre = getSiDataByYearAndData(preYear, storeData);

        for ( int j = 0; j < fStoreRec.size(); j++) {

            // 2023년 기준년도 점포수 데이터
            // j 횟수 돌리는 데이터 jData
            Map<String, Object> jData = fStoreRec.get(j);
            String jIndutyCdNm = (String) jData.get("SVC_INDUTY_CD_NM");
            String jIndutyCd = (String) jData.get("SVC_INDUTY_CD");
            double jSimilrIndutyStorCo = (double) jData.get("SIMILR_INDUTY_STOR_CO");

            log.info("기준년도 업종명 : " + jIndutyCdNm);
            log.info("기준년도 점포수 : " + jSimilrIndutyStorCo);

            // 가져온 점포 데이터에서 비교년도에서 기준년도 산업 이름 기준으로 데이터 가져오기
            List<Map<String, Object>> kStorePre = fStorePre.stream()
                    .filter(item -> jIndutyCdNm.equals(item.get("SVC_INDUTY_CD_NM")))
                    .collect(Collectors.toList());

            /*log.info("기준년도 점포수 :" + jSimilrIndutyStorCo);*/

            // 비교년도 데이터 점포수, 폐업수, 폐업률
            double kSimilrIndutyStorCo = 0;

            for (Map<String, Object> item : kStorePre) {

                kSimilrIndutyStorCo = (double) item.get("SIMILR_INDUTY_STOR_CO");

            }

            /*log.info("비교년도 점포수 :" + kSimilrIndutyStorCo);*/

            // 점포수 증가량 : 기준년도 점포수 - 비교년도 점포수
            double storeDiff = (jSimilrIndutyStorCo - kSimilrIndutyStorCo);

            // 점포 수 상승률
            double storeRate = storeDiff / kSimilrIndutyStorCo;

            /*log.info(jIndutyCdNm + "매출액 차이 : " + salesDiff);
            log.info("비교년도 점포당 매출액 : " + kSales);
            log.info(jIndutyCdNm + "매출액 증가률 : " + salesRate);*/

            // 기준년도 기준 데이터
            // 필요한 값 업종명, 업종코드, 점포수, 점포수 증가량, 증가율
            Map<String, Object> inputData = new HashMap<>();
            inputData.put("indutyCd", jIndutyCd);
            inputData.put("indusyNm", jIndutyCdNm);
            inputData.put("storeCount", jSimilrIndutyStorCo);
            inputData.put("storeDiff", storeDiff);
            inputData.put("storeRate", storeRate);

            // 리스트에 추가
            tData.add(inputData);
        }

        // tData 점포수 증가률 기준으로 높은 순서로 정렬
        Collections.sort(tData, Comparator.comparingDouble((Map<String, Object> map) -> (double) map.get("storeRate")).reversed());

        // 상위 n개 요소 선택 (parameter rank)
        List<Map<String, Object>> topSalesRate = tData.subList(0, Math.min(rank, tData.size()));

        List<SeoulSiMarketDTO> rList = new ArrayList<>();

        // DecimalFormat 객체 생성 데이터 포맷
        DecimalFormat df = new DecimalFormat("#.##");

        for (Map<String, Object> data : topSalesRate) {

            String indutyCd = (String)data.get("indutyCd");
            String indutyNm = (String)data.get("indusyNm");
            double storeCount = (double)data.get("storeCount");
            double storeDiff = (double)data.get("storeDiff");

            // 매출액 상승률을 %로 계산하여 소수점 두 자리까지 표시
            double storeRateDouble = (double) data.get("storeRate") * 100;
            String storeRate = df.format(storeRateDouble);

            log.info("Top"+ rank + " Data 데이터");
            log.info("indutyCd : " + indutyCd);
            log.info("indutyNm : " +indutyNm);
            log.info("storeCount : " + storeCount);
            log.info("storeDiff : " + storeDiff);
            log.info("storeRate : " + storeRate);

            SeoulSiMarketDTO seoulMarketDTO = SeoulSiMarketDTO.builder()
                    .indutyCd(indutyCd)
                    .indutyNm(indutyNm)
                    .storeCount(storeCount)
                    .storeDiff(storeDiff)
                    .storeRate(storeRate)
                    .build();
            rList.add(seoulMarketDTO);

        }

        log.info(this.getClass().getName() + ".getSiStoreRes End!");

        return rList;
    }

    @Override
    public List<SeoulSiMarketDTO> getSiStoreCloseRes(int rank, String preYear, String recYear) throws Exception {

        log.info(this.getClass().getName() + ".getSiStoreCloseRes Start!");

        // 넘겨줄 Data
        List<Map<String, Object>> tData = new ArrayList<>();


        // 가져온 점포 데이터에서 이번분기 데이터 필터
        List<Map<String, Object>> fStoreRec = getSiDataByYearAndData(recYear, storeData);

        // 가져온 점포 데이터에서 이전분기 데이터 필터
        List<Map<String, Object>> fStorePre = getSiDataByYearAndData(preYear, storeData);

        for ( int j = 0; j < fStoreRec.size(); j++) {

            // 2023년 기준년도 폐업수 데이터
            // j 횟수 돌리는 데이터 jData
            Map<String, Object> jData = fStoreRec.get(j);
            String jIndutyCdNm = (String) jData.get("SVC_INDUTY_CD_NM");
            String jIndutyCd = (String) jData.get("SVC_INDUTY_CD");
            double jClsStoreCo = (double) jData.get("CLSBIZ_STOR_CO");

            log.info("기준년도 업종명 : " + jIndutyCdNm);
            log.info("기준년도 폐업수 : " + jClsStoreCo);

            // 가져온 점포 데이터에서 비교년도에서 기준년도 산업 이름 기준으로 데이터 가져오기
            List<Map<String, Object>> kStorePre = fStorePre.stream()
                    .filter(item -> jIndutyCdNm.equals(item.get("SVC_INDUTY_CD_NM")))
                    .collect(Collectors.toList());

            /*log.info("기준년도 점포수 :" + jSimilrIndutyStorCo);*/

            // 비교년도 데이터 점포수, 폐업수, 폐업률
            double kClsStoreCo = 0;

            for (Map<String, Object> item : kStorePre) {

                kClsStoreCo = (double) item.get("CLSBIZ_STOR_CO");

            }

            /*log.info("비교년도 점포수 :" + kSimilrIndutyStorCo);*/

            // 점포수 증가량 : 기준년도 점포수 - 비교년도 점포수
            double closeStoreDiff = (jClsStoreCo - kClsStoreCo);

            // 점포 수 상승률
            double closeStoreRate = closeStoreDiff / kClsStoreCo;

            /*log.info(jIndutyCdNm + "매출액 차이 : " + salesDiff);
            log.info("비교년도 점포당 매출액 : " + kSales);
            log.info(jIndutyCdNm + "매출액 증가률 : " + salesRate);*/

            // 기준년도 기준 데이터
            // 필요한 값 업종명, 업종코드, 점포수, 점포수 증가량, 증가율
            Map<String, Object> inputData = new HashMap<>();
            inputData.put("indutyCd", jIndutyCd);
            inputData.put("indusyNm", jIndutyCdNm);
            inputData.put("closeStoreCount", jClsStoreCo);
            inputData.put("closeStoreDiff", closeStoreDiff);
            inputData.put("closeStoreRate", closeStoreRate);

            // 리스트에 추가
            tData.add(inputData);
        }

        // tData 점포수 증가률 기준으로 높은 순서로 정렬
        Collections.sort(tData, Comparator.comparingDouble((Map<String, Object> map) -> (double) map.get("closeStoreRate")).reversed());

        // 상위 n개 요소 선택 (parameter rank)
        List<Map<String, Object>> topSalesRate = tData.subList(0, Math.min(rank, tData.size()));

        List<SeoulSiMarketDTO> rList = new ArrayList<>();

        // DecimalFormat 객체 생성 데이터 포맷
        DecimalFormat df = new DecimalFormat("#.##");

        for (Map<String, Object> data : topSalesRate) {

            String indutyCd = (String)data.get("indutyCd");
            String indutyNm = (String)data.get("indusyNm");
            double closeStoreCount = (double)data.get("closeStoreCount");
            double closeStoreDiff = (double)data.get("closeStoreDiff");

            // 매출액 상승률을 %로 계산하여 소수점 두 자리까지 표시
            double closeStoreRateDouble = (double) data.get("closeStoreRate") * 100;
            String closeStoreRate = df.format(closeStoreRateDouble);

            log.info("Top"+ rank + " Data 데이터");
            log.info("indutyCd : " + indutyCd);
            log.info("indutyNm : " +indutyNm);
            log.info("closeStoreCount : " + closeStoreCount);
            log.info("closeStoreDiff : " + closeStoreDiff);
            log.info("closeStoreRate : " + closeStoreRate);

            SeoulSiMarketDTO seoulMarketDTO = SeoulSiMarketDTO.builder()
                    .indutyCd(indutyCd)
                    .indutyNm(indutyNm)
                    .closeStoreCount(closeStoreCount)
                    .closeStoreDiff(closeStoreDiff)
                    .closeStoreRate(closeStoreRate)
                    .build();
            rList.add(seoulMarketDTO);

        }

        log.info(this.getClass().getName() + ".getSiStoreCloseRes End!");

        return rList;
    }
}