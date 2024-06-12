package kopo.poly.service.impl;

import kopo.poly.dto.SeoulSiMarketDTO;
import kopo.poly.persistance.mongodb.IDongMapper;
import kopo.poly.service.IDongMarketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class DongMarketService implements IDongMarketService {

    private final IDongMapper dongMapper;

    /**
     *
     * MongoDB 에서 받아온 데이터 가공해서 넘겨주기 (매출액)
     *
     * @param rank 최종 정제 후 순위
     * @param preYear 컨트롤러로 보내온 비교년도 데이터
     * @param recYear 컨트롤러로 보내온 기준년도 데이터
     * @param seoulLocationCd 컨트롤러로 보내온 지역명
     * @param indutySort 컨트롤러로 보내온 업종 대분류
     * @param indutyName 컨트롤러로 보내온 업종 소분류
     *
     */
    @Override
    public List<SeoulSiMarketDTO> getDongMarketRes(int rank, String preYear, String recYear, String seoulLocationCd, String indutySort, String indutyName) throws Exception {

        log.info(this.getClass().getName() + ".getDongMarketRes Start!");

        int length = seoulLocationCd.length();
        String colNm = "SEOUL_DONG_MARKET";
        // 넘겨줄 rList
        List<SeoulSiMarketDTO> rList = new ArrayList<>();
        // 매출액 결과값 받아올 List
        List<SeoulSiMarketDTO> recSalesList = new ArrayList<>();
        List<SeoulSiMarketDTO> preSalesList = new ArrayList<>();
        // 점포수 결과값 받아올 List
        List<SeoulSiMarketDTO> recStoreList = new ArrayList<>();
        List<SeoulSiMarketDTO> preStoreList = new ArrayList<>();


        log.info("Dong Service seoulLocationCd length : " + length);
        log.info("Dong indutySort : " + indutySort);
        log.info("Dong indutyName : " + indutyName);

        if(length==2) { // 11(서울 전체)

            log.info("여기서부터는 '" + colNm + "' 데이터 가져오기");

            if(indutyName.length() > 0) { // 소분류가 있다면 indutyName(소분류)와 일치하는 데이터 가져오기

                recSalesList = dongMapper.getDongSalesAllByName(recYear, indutyName, colNm);
                preSalesList = dongMapper.getDongSalesAllByName(preYear, indutyName, colNm);

            } else { // 소분류가 없다면 indutySort(대분류)에 속하는 데이터 가져오기

                recSalesList = dongMapper.getDongSalesAllBySort(recYear, indutySort, colNm);
                preSalesList = dongMapper.getDongSalesAllBySort(preYear, indutySort, colNm);

            }

            //매출액쪽 데이터를 다 가져왔고 점포수 데이터를 가져와야하므로 컬렉션 네임 변경
            colNm = "SEOUL_DONG_STORE";
            log.info("여기서부터는 '" + colNm + "' 데이터 가져오기");

            if(indutyName.length() > 0) { // 소분류가 있다면 indutyName(소분류)와 일치하는 데이터 가져오기

                // 점포 데이터에서 가져온 이번분기 데이터 리스트
                recStoreList = dongMapper.getDongStoreAllByName(recYear, indutyName, colNm);

                // 점포 데이터에서 가져온 이전분기 데이터 필터
                preStoreList = dongMapper.getDongStoreAllByName(preYear, indutyName, colNm);


            } else { // 소분류가 없다면 indutySort(대분류)에 속하는 데이터 가져오기

                recStoreList = dongMapper.getDongStoreAllBySort(recYear, indutySort, colNm);
                preStoreList = dongMapper.getDongStoreAllBySort(preYear, indutySort, colNm);

            }

        } else { // 구 기준 데이터

            log.info("여기서부터는 '" + colNm + "' 데이터 가져오기");

            if(indutyName.length() > 0) { // 소분류가 있다면 indutyName(소분류)와 일치하는 데이터 가져오기

                recSalesList = dongMapper.getDongSalesByLocationCdAndName(recYear, seoulLocationCd, indutyName, colNm);
                preSalesList = dongMapper.getDongSalesByLocationCdAndName(preYear, seoulLocationCd, indutyName, colNm);

            } else { // 소분류가 없다면 indutySort(대분류)에 속하는 데이터 가져오기

                recSalesList = dongMapper.getDongSalesByLocationCdAndSort(recYear, seoulLocationCd, indutySort, colNm);
                preSalesList = dongMapper.getDongSalesByLocationCdAndSort(preYear, seoulLocationCd, indutySort, colNm);

            }

            //매출액쪽 데이터를 다 가져왔고 점포수 데이터를 가져와야하므로 컬렉션 네임 변경
            colNm = "SEOUL_DONG_STORE";
            log.info("여기서부터는 '" + colNm + "' 데이터 가져오기");

            if(indutyName.length() > 0) { // 소분류가 있다면 indutyName(소분류)와 일치하는 데이터 가져오기

                // 점포 데이터에서 가져온 이번분기 데이터 리스트
                recStoreList = dongMapper.getDongStoreByLocationCdAndName(recYear, seoulLocationCd, indutyName, colNm);

                // 점포 데이터에서 가져온 이전분기 데이터 필터
                preStoreList = dongMapper.getDongStoreByLocationCdAndName(preYear, seoulLocationCd, indutyName, colNm);


            } else { // 소분류가 없다면 indutySort(대분류)에 속하는 데이터 가져오기

                recStoreList = dongMapper.getDongStoreByLocationCdAndSort(recYear, seoulLocationCd, indutySort, colNm);
                preStoreList = dongMapper.getDongStoreByLocationCdAndSort(preYear, seoulLocationCd, indutySort, colNm);

            }

        }

        // DecimalFormat 객체 생성 데이터 포맷
        DecimalFormat df = new DecimalFormat("#.##");

        for ( int j = 0; j < recSalesList.size(); j++) {

            // 기준년도 매출액 데이터
            // rec : recent의 약자(최신)
            SeoulSiMarketDTO recDTO = recSalesList.get(j);
            String seoulLocationNm = recDTO.seoulLocationNm();
            String locationCd = recDTO.seoulLocationCd();
            double recSales = recDTO.monthSales();


            // 가져온 데이터에서 비교년도에서 기준년도 지역명 기준으로 매출액 데이터 가져오기
            // 비교년도 매출액 초기화
            double preSales = 0;
            for (SeoulSiMarketDTO dto : preSalesList) {
                if (dto.seoulLocationNm().equals(seoulLocationNm)) {
                    preSales = dto.monthSales();

                    break;
                }
            }

            // 가져온 점포 데이터에서 기준년도에서 기준년도 지역명 기준으로 데이터 가져오기
            double recStoreCo = 0;
            for (SeoulSiMarketDTO dto : recStoreList) {
                if (dto.seoulLocationNm().equals(seoulLocationNm)) {

                    recStoreCo = dto.storeCount();

                    break;
                }
            }

            // 가져온 점포 데이터에서 비교년도에서 기준년도 지역명 기준으로 데이터 가져오기
            double preStoreCo = 0;
            for (SeoulSiMarketDTO dto : preStoreList) {
                if (dto.seoulLocationNm().equals(seoulLocationNm)) {

                    preStoreCo = dto.storeCount();

                    break;
                }
            }

            double pSales = 0;

            if (preStoreCo > 0) {

                pSales = (preSales / preStoreCo) / 10000;

            }

            // 점포당 매출액 기준년도 rSales, 비교년도 pSales 1만 단위
            double rSales = 0;
            if (recStoreCo > 0 || pSales > 0 ) {

                rSales = (recSales / recStoreCo) / 10000;

            }

            // 점포당 매출액 증가량 1만 단위
            long salesDiff = Math.round(rSales - pSales);

            // 점포당 매출액 상승률
            double saleRate = 0;
            if (pSales > 0) {

                saleRate = salesDiff / pSales;

            }

            if (saleRate == 0) {

                salesDiff = 0;

            }

            // 매출액 상승률을 %로 계산하여 소수점 두 자리까지 표시
            double salesRatePercent = saleRate * 100;

            // 너무 편차가 크면 패스(표본 오류)
            if(salesRatePercent > 3000) {

                continue;

            }

            String salesRate = df.format(salesRatePercent);

            // 매출액 형식 포맷
            String fMonthSales = df.format(rSales);
            // 지역명 표시 형식 변경
            // CSV파일 ?로 구분
            // ? -> . 으로 변경
            seoulLocationNm = seoulLocationNm.replace("?", ".");

            // 필요한 값 업종명, 업종코드, 매출액, 매출액 증가량, 증가율
            SeoulSiMarketDTO pDTO = SeoulSiMarketDTO.builder()
                    .seoulLocationNm(seoulLocationNm)
                    .seoulLocationCd(locationCd)
                    .fMonthSales(fMonthSales)
                    .salesDiff(salesDiff)
                    .salesRate(salesRate)
                    .build();

            // 리스트에 추가
            rList.add(pDTO);
        }

        // rList 매출액 증가률 기준으로 높은 순서로 정렬
        Comparator<SeoulSiMarketDTO> salesRateComparator = (dto1, dto2) -> {
            double salesRate1 = Double.parseDouble(dto1.salesRate());
            double salesRate2 = Double.parseDouble(dto2.salesRate());
            return Double.compare(salesRate2, salesRate1); // 내림차순 정렬
        };

        // rList를 salesRate 기준으로 정렬
        Collections.sort(rList, salesRateComparator);

        // rList에서 rank 이외의 요소를 모두 삭제
        if(rList.size()>rank) {

            rList.subList(rank, rList.size()).clear();

        }

        log.info(this.getClass().getName() + ".getDongMarketRes End!");

        return rList;

    }

    @Override
    public List<SeoulSiMarketDTO> getDongStoreRes(int rank, String preYear, String recYear, String seoulLocationCd, String indutySort, String indutyName) throws Exception {

        log.info(this.getClass().getName() + ".getDongStoreRes Start!");

        int length = seoulLocationCd.length();
        String colNm = "SEOUL_DONG_STORE";
        // 넘겨줄 rList
        List<SeoulSiMarketDTO> rList = new ArrayList<>();

        // 점포수 결과값 받아올 List
        List<SeoulSiMarketDTO> recStoreList = new ArrayList<>();
        List<SeoulSiMarketDTO> preStoreList = new ArrayList<>();

        log.info("Dong Service seoulLocationCd length : " + length);
        log.info("Dong indutySort : " + indutySort);
        log.info("Dong indutyName : " + indutyName);
        log.info(colNm + "' 데이터 가져오기");

        if(length==2) { // 11(서울 전체)

            if(indutyName.length() > 0) { // 소분류가 있다면 indutyName(소분류)와 일치하는 데이터 가져오기

                // 점포 데이터에서 가져온 이번분기 데이터 리스트
                recStoreList = dongMapper.getDongStoreAllByName(recYear, indutyName, colNm);

                // 점포 데이터에서 가져온 이전분기 데이터 필터
                preStoreList = dongMapper.getDongStoreAllByName(preYear, indutyName, colNm);


            } else { // 소분류가 없다면 indutySort(대분류)에 속하는 데이터 가져오기

                recStoreList = dongMapper.getDongStoreAllBySort(recYear, indutySort, colNm);
                preStoreList = dongMapper.getDongStoreAllBySort(preYear, indutySort, colNm);

            }

        } else { // 구 기준 데이터

            if(indutyName.length() > 0) { // 소분류가 있다면 indutyName(소분류)와 일치하는 데이터 가져오기

                // 점포 데이터에서 가져온 이번분기 데이터 리스트
                recStoreList = dongMapper.getDongStoreByLocationCdAndName(recYear, seoulLocationCd, indutyName, colNm);

                // 점포 데이터에서 가져온 이전분기 데이터 필터
                preStoreList = dongMapper.getDongStoreByLocationCdAndName(preYear, seoulLocationCd, indutyName, colNm);


            } else { // 소분류가 없다면 indutySort(대분류)에 속하는 데이터 가져오기

                recStoreList = dongMapper.getDongStoreByLocationCdAndSort(recYear, seoulLocationCd, indutySort, colNm);
                preStoreList = dongMapper.getDongStoreByLocationCdAndSort(preYear, seoulLocationCd, indutySort, colNm);

            }

        }

        // DecimalFormat 객체 생성 데이터 포맷
        DecimalFormat df = new DecimalFormat("#.##");

        for ( int j = 0; j < recStoreList.size(); j++) {

            SeoulSiMarketDTO recDTO = recStoreList.get(j);
            double recStoreCo = recDTO.storeCount();
            String seoulLocationNm = recDTO.seoulLocationNm();

            // 가져온 점포 데이터에서 비교년도에서 기준년도 지역명 기준으로 데이터 가져오기
            double preStoreCo = 0;
            for (SeoulSiMarketDTO dto : preStoreList) {
                if (dto.seoulLocationNm().equals(seoulLocationNm)) {

                        preStoreCo = dto.storeCount();
                        break;

                }
            }

            // 점포수 상승량
            double storeDiff = recStoreCo - preStoreCo;

            // 점포 상승률
            double tStoreRate = 0;
            if (preStoreCo > 0) {

                tStoreRate = storeDiff / preStoreCo;

            }

            if (tStoreRate == 0) {

                storeDiff = 0;

            }


            // 매출액 상승률을 %로 계산하여 소수점 두 자리까지 표시
            double storeRatePercent = tStoreRate * 100;
            String storeRate = df.format(storeRatePercent);
            // 지역명 표시 형식 변경
            // CSV파일 ?로 구분
            // ? -> . 으로 변경
            seoulLocationNm = seoulLocationNm.replace("?", ".");

            // 필요한 값 업종명, 업종코드, 점포수, 점포수 증가량, 증가율
            SeoulSiMarketDTO pDTO = SeoulSiMarketDTO.builder()
                    .seoulLocationNm(seoulLocationNm)
                    .storeCount(recStoreCo)
                    .storeDiff(storeDiff)
                    .storeRate(storeRate)
                    .build();

            // 리스트에 추가
            rList.add(pDTO);
        }

        // rList 매출액 증가률 기준으로 높은 순서로 정렬
        Comparator<SeoulSiMarketDTO> storeRateComparator = (dto1, dto2) -> {
            double storeRate1 = Double.parseDouble(dto1.storeRate());
            double storeRate2 = Double.parseDouble(dto2.storeRate());
            return Double.compare(storeRate2, storeRate1); // 내림차순 정렬
        };

        // rList를 salesRate 기준으로 정렬
        Collections.sort(rList, storeRateComparator);

        // rList에서 rank 이외의 요소를 모두 삭제
        if(rList.size()>10) {

            rList.subList(rank, rList.size()).clear();

        }

        log.info(this.getClass().getName() + ".getDongStoreRes End!");

        return rList;

    }

    @Override
    public List<SeoulSiMarketDTO> getDongCustomerRes(String recYear, String seoulLocationCd, String indutySort, String indutyName) throws Exception {

        log.info(this.getClass().getName() + ".getDongCustomerRes Start!");

        int length = seoulLocationCd.length();
        String colNm = "SEOUL_DONG_MARKET";
        // 넘겨줄 rList
        List<SeoulSiMarketDTO> rList = new ArrayList<>();
        // 나이대 별 데이터 가공전 임시 리스트
        List<SeoulSiMarketDTO> tList = new ArrayList<>();
        // 매출액 결과값 받아올 List
        List<SeoulSiMarketDTO> recSalesList = new ArrayList<>();

        // 점포수 결과값 받아올 List
        List<SeoulSiMarketDTO> recStoreList = new ArrayList<>();


        log.info("Dong Service seoulLocationCd length : " + length);
        log.info("Dong indutySort : " + indutySort);
        log.info("Dong indutyName : " + indutyName);

        if(length==2) { // 11(서울 전체)

            log.info("여기서부터는 '" + colNm + "' 데이터 가져오기");

            if(indutyName.length() > 0) { // 소분류가 있다면 indutyName(소분류)와 일치하는 데이터 가져오기

                recSalesList = dongMapper.getDongSalesAllByName(recYear, indutyName, colNm);

            } else { // 소분류가 없다면 indutySort(대분류)에 속하는 데이터 가져오기

                recSalesList = dongMapper.getDongSalesAllBySort(recYear, indutySort, colNm);
            }

            //매출액쪽 데이터를 다 가져왔고 점포수 데이터를 가져와야하므로 컬렉션 네임 변경
            colNm = "SEOUL_DONG_STORE";
            log.info("여기서부터는 '" + colNm + "' 데이터 가져오기");

            if(indutyName.length() > 0) { // 소분류가 있다면 indutyName(소분류)와 일치하는 데이터 가져오기

                // 점포 데이터에서 가져온 이번분기 데이터 리스트
                recStoreList = dongMapper.getDongStoreAllByName(recYear, indutyName, colNm);

            } else { // 소분류가 없다면 indutySort(대분류)에 속하는 데이터 가져오기

                recStoreList = dongMapper.getDongStoreAllBySort(recYear, indutySort, colNm);

            }

        } else { // 구 기준 데이터

            log.info("여기서부터는 '" + colNm + "' 데이터 가져오기");

            if(indutyName.length() > 0) { // 소분류가 있다면 indutyName(소분류)와 일치하는 데이터 가져오기

                recSalesList = dongMapper.getDongSalesByLocationCdAndName(recYear, seoulLocationCd, indutyName, colNm);

            } else { // 소분류가 없다면 indutySort(대분류)에 속하는 데이터 가져오기

                recSalesList = dongMapper.getDongSalesByLocationCdAndSort(recYear, seoulLocationCd, indutySort, colNm);

            }

            //매출액쪽 데이터를 다 가져왔고 점포수 데이터를 가져와야하므로 컬렉션 네임 변경
            colNm = "SEOUL_DONG_STORE";
            log.info("여기서부터는 '" + colNm + "' 데이터 가져오기");

            if(indutyName.length() > 0) { // 소분류가 있다면 indutyName(소분류)와 일치하는 데이터 가져오기

                // 점포 데이터에서 가져온 이번분기 데이터 리스트
                recStoreList = dongMapper.getDongStoreByLocationCdAndName(recYear, seoulLocationCd, indutyName, colNm);

            } else { // 소분류가 없다면 indutySort(대분류)에 속하는 데이터 가져오기

                recStoreList = dongMapper.getDongStoreByLocationCdAndSort(recYear, seoulLocationCd, indutySort, colNm);

            }

        }

        // DecimalFormat 객체 생성 데이터 포맷
        DecimalFormat df = new DecimalFormat("#.##");

        for ( int j = 0; j < recSalesList.size(); j++) {

            // 기준년도 매출액 데이터
            // rec : recent의 약자(최신)
            SeoulSiMarketDTO recDTO = recSalesList.get(j);
            String seoulLocationNm = recDTO.seoulLocationNm();
            double recSales = recDTO.monthSales();
            double age10Sales = recDTO.age10Sales();
            double age20Sales = recDTO.age20Sales();
            double age30Sales = recDTO.age30Sales();
            double age40Sales = recDTO.age40Sales();
            double age50Sales = recDTO.age50Sales();
            double age60Sales = recDTO.age60Sales();


            // 가져온 점포 데이터에서 기준년도에서 기준년도 지역명 기준으로 데이터 가져오기
            double recStoreCo = 0;
            for (SeoulSiMarketDTO dto : recStoreList) {
                if (dto.seoulLocationNm().equals(seoulLocationNm)) {

                    recStoreCo = dto.storeCount();

                    break;
                }
            }



            // 점포당 매출액 기준년도 rSales, 비교년도 pSales 1만 단위
            double rSales = 0;
            if (recStoreCo > 0) {

                rSales = (recSales / recStoreCo) / 10000;

            }

            // 60대까지 있으니 배열 6개
            double rAge10Sales = 0;
            double rAge20Sales = 0;
            double rAge30Sales = 0;
            double rAge40Sales = 0;
            double rAge50Sales = 0;
            double rAge60Sales = 0;

            // 나이대 별 점포당 매출액

            if (recStoreCo > 0) {

                rAge10Sales = (age10Sales / recStoreCo) / 10000; // 10대 매출액
                rAge20Sales = (age20Sales / recStoreCo) / 10000; // 20대 매출액
                rAge30Sales = (age30Sales / recStoreCo) / 10000; // 30대 매출액
                rAge40Sales = (age40Sales / recStoreCo) / 10000; // 40대 매출액
                rAge50Sales = (age50Sales / recStoreCo) / 10000; // 50대 매출액
                rAge60Sales = (age60Sales / recStoreCo) / 10000; // 60대 매출액

            }

            // 점포당 매출액 상승률
            double tSale10Rate = 0;
            double tSale20Rate = 0;
            double tSale30Rate = 0;
            double tSale40Rate = 0;
            double tSale50Rate = 0;
            double tSale60Rate = 0;

            if (rSales > 0) {

                tSale10Rate = rAge10Sales / rSales;
                tSale20Rate = rAge20Sales / rSales;
                tSale30Rate = rAge30Sales / rSales;
                tSale40Rate = rAge40Sales / rSales;
                tSale50Rate = rAge50Sales / rSales;
                tSale60Rate = rAge60Sales / rSales;

            }

            // 매출액 상승률을 %로 계산하여 소수점 두 자리까지 표시
            double tSales10RatePercent = tSale10Rate * 100;
            double tSales20RatePercent = tSale20Rate * 100;
            double tSales30RatePercent = tSale30Rate * 100;
            double tSales40RatePercent = tSale40Rate * 100;
            double tSales50RatePercent = tSale50Rate * 100;
            double tSales60RatePercent = tSale60Rate * 100;


            String sales10Rate = df.format(tSales10RatePercent);
            String sales20Rate = df.format(tSales20RatePercent);
            String sales30Rate = df.format(tSales30RatePercent);
            String sales40Rate = df.format(tSales40RatePercent);
            String sales50Rate = df.format(tSales50RatePercent);
            String sales60Rate = df.format(tSales60RatePercent);

            // 매출액 형식 포맷
            String fMonthSales = df.format(rSales);
            String fAge10Sales = df.format(rAge10Sales);
            String fAge20Sales = df.format(rAge20Sales);
            String fAge30Sales = df.format(rAge30Sales);
            String fAge40Sales = df.format(rAge40Sales);
            String fAge50Sales = df.format(rAge50Sales);
            String fAge60Sales = df.format(rAge60Sales);
            // 지역명 표시 형식 변경
            // CSV파일 ?로 구분
            // ? -> . 으로 변경
            seoulLocationNm = seoulLocationNm.replace("?", ".");

            // 필요한 값 업종명, 업종코드, 매출액, 매출액 증가량, 증가율
            SeoulSiMarketDTO pDTO = SeoulSiMarketDTO.builder()
                    .seoulLocationNm(seoulLocationNm)
                    .fMonthSales(fMonthSales)
                    .age10Sale(fAge10Sales)
                    .age20Sale(fAge20Sales)
                    .age30Sale(fAge30Sales)
                    .age40Sale(fAge40Sales)
                    .age50Sale(fAge50Sales)
                    .age60Sale(fAge60Sales)
                    .age10SalesRate(sales10Rate)
                    .age20SalesRate(sales20Rate)
                    .age30SalesRate(sales30Rate)
                    .age40SalesRate(sales40Rate)
                    .age50SalesRate(sales50Rate)
                    .age60SalesRate(sales60Rate)
                    .build();

            // 리스트에 추가
            tList.add(pDTO);
        }

        // rList 매출액 나이대 별 증가률 기준으로 높은 순서로 정렬 10 ~ 60
        Comparator<SeoulSiMarketDTO> sales10RateComparator = (dto1, dto2) -> {
            double SalesRate1 = Double.parseDouble(dto1.age10SalesRate());
            double SalesRate2 = Double.parseDouble(dto2.age10SalesRate());
            return Double.compare(SalesRate2, SalesRate1); // 내림차순 정렬
        };

        Comparator<SeoulSiMarketDTO> sales20RateComparator = (dto1, dto2) -> {
            double SalesRate1 = Double.parseDouble(dto1.age20SalesRate());
            double SalesRate2 = Double.parseDouble(dto2.age20SalesRate());
            return Double.compare(SalesRate2, SalesRate1); // 내림차순 정렬
        };
        Comparator<SeoulSiMarketDTO> sales30RateComparator = (dto1, dto2) -> {
            double SalesRate1 = Double.parseDouble(dto1.age30SalesRate());
            double SalesRate2 = Double.parseDouble(dto2.age30SalesRate());
            return Double.compare(SalesRate2, SalesRate1); // 내림차순 정렬
        };
        Comparator<SeoulSiMarketDTO> sales40RateComparator = (dto1, dto2) -> {
            double SalesRate1 = Double.parseDouble(dto1.age40SalesRate());
            double SalesRate2 = Double.parseDouble(dto2.age40SalesRate());
            return Double.compare(SalesRate2, SalesRate1); // 내림차순 정렬
        };
        Comparator<SeoulSiMarketDTO> sales50RateComparator = (dto1, dto2) -> {
            double SalesRate1 = Double.parseDouble(dto1.age50SalesRate());
            double SalesRate2 = Double.parseDouble(dto2.age50SalesRate());
            return Double.compare(SalesRate2, SalesRate1); // 내림차순 정렬
        };
        Comparator<SeoulSiMarketDTO> sales60RateComparator = (dto1, dto2) -> {
            double SalesRate1 = Double.parseDouble(dto1.age60SalesRate());
            double SalesRate2 = Double.parseDouble(dto2.age60SalesRate());
            return Double.compare(SalesRate2, SalesRate1); // 내림차순 정렬
        };

        // 나이대 별 정렬
        // tList를 sales10Rate~sales60Rate 기준으로 정렬
        Collections.sort(tList, sales10RateComparator);
        rList.add(tList.get(0));
        Collections.sort(tList, sales20RateComparator);
        rList.add(tList.get(0));
        Collections.sort(tList, sales30RateComparator);
        rList.add(tList.get(0));
        Collections.sort(tList, sales40RateComparator);
        rList.add(tList.get(0));
        Collections.sort(tList, sales50RateComparator);
        rList.add(tList.get(0));
        Collections.sort(tList, sales60RateComparator);
        rList.add(tList.get(0));


        log.info(this.getClass().getName() + ".getDongCustomerRes End!");

        return rList;

    }

    /**
     *
     * 창업 지역 추천
     *
     **/

    @Override
    public List<SeoulSiMarketDTO> getDongLatLon(List<SeoulSiMarketDTO> pList) {

        List<SeoulSiMarketDTO> rList = new ArrayList<>();

        for(int i = 0; i < pList.size(); i++) {

            String colNm = "SEOUL_DONG_LATLON";

            SeoulSiMarketDTO pDTO = pList.get(i);
            String locationCd = pDTO.seoulLocationCd();

            SeoulSiMarketDTO rDTO = Optional.ofNullable(dongMapper.getDongGuLatLon(locationCd, colNm))
                    .orElseGet(() -> SeoulSiMarketDTO.builder().build());

            rList.add(rDTO);

        }

        return rList;
    }

    @Override
    public List<List<SeoulSiMarketDTO>> getLocationMarketRes(SeoulSiMarketDTO pDTO) {

        log.info(this.getClass().getName() + ".getLocationMarketRes Start!");

        String recentYear = "20233";
        String preYear = "20232";
        String colNm = "SEOUL_DONG_MARKET";
        String indutyNm = pDTO.indutyNm();
        String maxAge = pDTO.maxAge();
        String maxGender = pDTO.maxGender();
        String maxTime = pDTO.maxTime();

        int rank = 3;

        // 최종 리스트들을 넘겨줄 List
        List<List<SeoulSiMarketDTO>> rList = new ArrayList<>();
        // 각 종 매출액 정보를 담을 List
        List<SeoulSiMarketDTO> saleList = new ArrayList<>();
        // 매출액 증가율을 담을 List
        List<SeoulSiMarketDTO> rateList = new ArrayList<>();

        List<SeoulSiMarketDTO> recSalesList = Optional.ofNullable(dongMapper.getLocationMarketByIndutyNm(recentYear, indutyNm, colNm))
                .orElseGet(LinkedList::new);
        List<SeoulSiMarketDTO> preSalesList = Optional.ofNullable(dongMapper.getLocationMarketByIndutyNm(preYear, indutyNm, colNm))
                .orElseGet(LinkedList::new);

        colNm = "SEOUL_DONG_STORE";

        List<SeoulSiMarketDTO> recStoreList = Optional.ofNullable(dongMapper.getDongStoreAllByName(recentYear, indutyNm, colNm))
                .orElseGet(LinkedList::new);


        for ( int j = 0; j < recSalesList.size(); j++) {

            // 기준년도 매출액 데이터
            // rec : recent의 약자(최신)
            SeoulSiMarketDTO recDTO = recSalesList.get(j);
            String seoulLocationNm = recDTO.seoulLocationNm();
            String seoulLocationCd = recDTO.seoulLocationCd();
            double recSales = recDTO.monthSales();
            double age10Sales = recDTO.age10Sales();
            double age20Sales = recDTO.age20Sales();
            double age30Sales = recDTO.age30Sales();
            double age40Sales = recDTO.age40Sales();
            double age50Sales = recDTO.age50Sales();
            double age60Sales = recDTO.age60Sales();
            double time0006Sales = recDTO.time0006Sales();
            double time0611Sales = recDTO.time0611Sales();
            double time1114Sales = recDTO.time1114Sales();
            double time1417Sales = recDTO.time1417Sales();
            double time1721Sales = recDTO.time1721Sales();
            double time2124Sales = recDTO.time2124Sales();
            double femaleSales = recDTO.femaleSales();
            double maleSales = recDTO.maleSales();

            // 가져온 점포 데이터에서 기준년도에서 기준년도 지역명 기준으로 데이터 가져오기
            double recStoreCo = 0;
            for (SeoulSiMarketDTO dto : recStoreList) {
                if (dto.seoulLocationNm().equals(seoulLocationNm)) {

                    recStoreCo = dto.storeCount();

                    break;
                }
            }

            if(recStoreCo > 0) {

                recSales = (recSales / 10000) / recStoreCo;
                age10Sales = (age10Sales / 10000) / recStoreCo;
                age20Sales = (age20Sales / 10000) / recStoreCo;
                age30Sales = (age30Sales / 10000) / recStoreCo;
                age40Sales = (age40Sales / 10000) / recStoreCo;
                age50Sales = (age50Sales / 10000) / recStoreCo;
                age60Sales = (age60Sales / 10000) / recStoreCo;
                time0006Sales = (time0006Sales / 10000) / recStoreCo;
                time0611Sales = (time0611Sales / 10000) / recStoreCo;
                time1114Sales = (time1114Sales / 10000) / recStoreCo;
                time1417Sales = (time1417Sales / 10000) / recStoreCo;
                time1721Sales = (time1721Sales / 10000) / recStoreCo;
                time2124Sales = (time2124Sales / 10000) / recStoreCo;
                maleSales = (maleSales / 10000) / recStoreCo;
                femaleSales = (femaleSales / 10000) / recStoreCo;

                SeoulSiMarketDTO tDTO = SeoulSiMarketDTO.builder()
                        .seoulLocationNm(seoulLocationNm)
                        .seoulLocationCd(seoulLocationCd)
                        .monthSales(recSales)
                        .age10Sales(age10Sales)
                        .age20Sales(age20Sales)
                        .age30Sales(age30Sales)
                        .age40Sales(age40Sales)
                        .age50Sales(age50Sales)
                        .age60Sales(age60Sales)
                        .time0006Sales(time0006Sales)
                        .time0611Sales(time0611Sales)
                        .time1114Sales(time1114Sales)
                        .time1417Sales(time1417Sales)
                        .time1721Sales(time1721Sales)
                        .time2124Sales(time2124Sales)
                        .maleSales(maleSales)
                        .femaleSales(femaleSales)
                        .build();

                saleList.add(tDTO);

            } else { // 점포수가 없으면 0으로 세팅(산출 값에서 계산값 제외)

                SeoulSiMarketDTO tDTO = SeoulSiMarketDTO.builder()
                        .seoulLocationNm(seoulLocationNm)
                        .seoulLocationCd(seoulLocationCd)
                        .monthSales(0)
                        .age10Sales(0)
                        .age20Sales(0)
                        .age30Sales(0)
                        .age40Sales(0)
                        .age50Sales(0)
                        .age60Sales(0)
                        .time0006Sales(0)
                        .time0611Sales(0)
                        .time1114Sales(0)
                        .time1417Sales(0)
                        .time1721Sales(0)
                        .time2124Sales(0)
                        .maleSales(0)
                        .femaleSales(0)
                        .build();

                saleList.add(tDTO);

            }

        }

        /* 리스트들 sort*/

        // 매출액 기준 정렬
        Comparator<SeoulSiMarketDTO> salesComparator = (dto1, dto2) -> {
            double sales1 = dto1.monthSales();
            double sales2 = dto2.monthSales();
            return Double.compare(sales2, sales1); // 내림차순 정렬
        };

        // 점포수 기준 정렬
        Comparator<SeoulSiMarketDTO> storeComparator = (dto1, dto2) -> {
            double storeCount1 = dto1.storeCount();
            double storeCount2 = dto2.storeCount();
            return Double.compare(storeCount2, storeCount1); // 내림차순 정렬
        };


        // 매출액 기준 Top3 rList[0]
        List<SeoulSiMarketDTO> tempSaleList = new ArrayList<>(saleList);
        tempSaleList.sort(salesComparator);
        if(tempSaleList.size() > rank) {
            tempSaleList.subList(rank, tempSaleList.size()).clear();
        }
        rList.add(tempSaleList);

        // recStoreList를 storeCount 기준으로 정렬 rList[1]
        recStoreList.sort(storeComparator);
        // recStoreList에서 rank 이외의 요소를 모두 삭제
        if(recStoreList.size()>rank) {
            recStoreList.subList(rank, recStoreList.size()).clear();
        }
        rList.add(recStoreList);

        List<SeoulSiMarketDTO> tempAgeList = new ArrayList<>(saleList);
        // 주고객층 나이대 Top3 rList[2]
        tempAgeList = listSortByAge(tempAgeList, maxAge, rank);
        rList.add(tempAgeList);

        // 주고객층 성별 Top3 rList[3]
        List<SeoulSiMarketDTO> tempGenderList = new ArrayList<>(saleList);
        tempGenderList = listSortByGender(tempGenderList, maxGender, rank);
        rList.add(tempGenderList);

        // 피크타임 Top3 rList[4]
        List<SeoulSiMarketDTO> tempTimeList = new ArrayList<>(saleList);
        tempTimeList = listSortByTime(tempTimeList, maxTime, rank);
        rList.add(tempTimeList);

        log.info("매출액 지역 : " + rList.get(0).get(0).monthSales() + ", " + rList.get(0).get(1).monthSales() + ", " + rList.get(0).get(2).monthSales());
        log.info("매출액 지역명 : " + rList.get(0).get(0).seoulLocationNm() + ", " + rList.get(0).get(1).seoulLocationNm() + ", " + rList.get(0).get(2).seoulLocationNm());
        log.info("점포수 지역 : " + rList.get(1).get(0).storeCount() + ", " + rList.get(1).get(1).storeCount() + ", " + rList.get(1).get(2).storeCount());
        log.info("점포수 지역명 : " + rList.get(1).get(0).seoulLocationNm() + ", " + rList.get(1).get(1).seoulLocationNm() + ", " + rList.get(1).get(2).seoulLocationNm());

        log.info(this.getClass().getName() + ".getLocationMarketRes End!");

        return rList;
    }

    List<SeoulSiMarketDTO> listSortByAge(List<SeoulSiMarketDTO> pList, String maxAge, int rank) {

        // 나이대 별 정렬
        // 10~60대
        Comparator<SeoulSiMarketDTO> age10Comparator = (dto1, dto2) -> {
            double sales1 = dto1.age10Sales();
            double sales2 = dto2.age10Sales();
            return Double.compare(sales2, sales1); // 내림차순 정렬
        };
        Comparator<SeoulSiMarketDTO> age20Comparator = (dto1, dto2) -> {
            double sales1 = dto1.age20Sales();
            double sales2 = dto2.age20Sales();
            return Double.compare(sales2, sales1); // 내림차순 정렬
        };
        Comparator<SeoulSiMarketDTO> age30Comparator = (dto1, dto2) -> {
            double sales1 = dto1.age30Sales();
            double sales2 = dto2.age30Sales();
            return Double.compare(sales2, sales1); // 내림차순 정렬
        };
        Comparator<SeoulSiMarketDTO> age40Comparator = (dto1, dto2) -> {
            double sales1 = dto1.age40Sales();
            double sales2 = dto2.age40Sales();
            return Double.compare(sales2, sales1); // 내림차순 정렬
        };
        Comparator<SeoulSiMarketDTO> age50Comparator = (dto1, dto2) -> {
            double sales1 = dto1.age50Sales();
            double sales2 = dto2.age50Sales();
            return Double.compare(sales2, sales1); // 내림차순 정렬
        };
        Comparator<SeoulSiMarketDTO> age60Comparator = (dto1, dto2) -> {
            double sales1 = dto1.age60Sales();
            double sales2 = dto2.age60Sales();
            return Double.compare(sales2, sales1); // 내림차순 정렬
        };

        // 나이대 별 매출액 Top3
        // salesRate에서 maxAge 기준으로 정렬
        if(maxAge.equals("10")) {
            pList.sort(age10Comparator);
        } else if(maxAge.equals("20")) {
            pList.sort(age20Comparator);
        } else if(maxAge.equals("30")) {
            pList.sort(age30Comparator);
        } else if(maxAge.equals("40")) {
            pList.sort(age40Comparator);
        } else if(maxAge.equals("50")) {
            pList.sort(age50Comparator);
        } else {
            pList.sort(age60Comparator);
        }

        if (pList.size() > rank) {
            pList.subList(rank, pList.size()).clear();
        }

        return pList;
    }

    List<SeoulSiMarketDTO> listSortByGender(List<SeoulSiMarketDTO> pList, String maxGender, int rank) {

        boolean temp = maxGender.equals("male");

        log.info("분류되는 주고객층 성별 : " + maxGender);
        log.info("남성 여부 : " + temp );

        // 성별 정렬
        // 남, 여
        Comparator<SeoulSiMarketDTO> maleComparator = (dto1, dto2) -> {
            double sales1 = dto1.maleSales();
            double sales2 = dto2.maleSales();
            return Double.compare(sales2, sales1); // 내림차순 정렬
        };
        Comparator<SeoulSiMarketDTO> femaleComparator = (dto1, dto2) -> {
            double sales1 = dto1.femaleSales();
            double sales2 = dto2.femaleSales();
            return Double.compare(sales2, sales1); // 내림차순 정렬
        };

        // 성별 매출액 Top3
        // maxGender 기준으로 정렬
        if(maxGender.equals("male")) {
            pList.sort(maleComparator);
        } else {
            pList.sort(femaleComparator);
        }

        if (pList.size() > rank) {
            pList.subList(rank, pList.size()).clear();
        }

        return pList;
    }

    List<SeoulSiMarketDTO> listSortByTime(List<SeoulSiMarketDTO> pList, String maxTime, int rank) {

        // 시간대 별 정렬
        Comparator<SeoulSiMarketDTO> time0006Comparator = (dto1, dto2) -> {
            double sales1 = dto1.time0006Sales();
            double sales2 = dto2.time0006Sales();
            return Double.compare(sales2, sales1); // 내림차순 정렬
        };
        Comparator<SeoulSiMarketDTO> time0611Comparator = (dto1, dto2) -> {
            double sales1 = dto1.time0611Sales();
            double sales2 = dto2.time0611Sales();
            return Double.compare(sales2, sales1); // 내림차순 정렬
        };
        Comparator<SeoulSiMarketDTO> time1114Comparator = (dto1, dto2) -> {
            double sales1 = dto1.time1114Sales();
            double sales2 = dto2.time1114Sales();
            return Double.compare(sales2, sales1); // 내림차순 정렬
        };
        Comparator<SeoulSiMarketDTO> time1417Comparator = (dto1, dto2) -> {
            double sales1 = dto1.time1417Sales();
            double sales2 = dto2.time1417Sales();
            return Double.compare(sales2, sales1); // 내림차순 정렬
        };
        Comparator<SeoulSiMarketDTO> time1721Comparator = (dto1, dto2) -> {
            double sales1 = dto1.time1721Sales();
            double sales2 = dto2.time1721Sales();
            return Double.compare(sales2, sales1); // 내림차순 정렬
        };
        Comparator<SeoulSiMarketDTO> time2124Comparator = (dto1, dto2) -> {
            double sales1 = dto1.time2124Sales();
            double sales2 = dto2.time2124Sales();
            return Double.compare(sales2, sales1); // 내림차순 정렬
        };

        // 나이대 별 매출액 Top3
        // salesRate에서 maxAge 기준으로 정렬
        if(maxTime.equals("0006")) {
            pList.sort(time0006Comparator);
        } else if(maxTime.equals("0611")) {
            pList.sort(time0611Comparator);
        } else if(maxTime.equals("1114")) {
            pList.sort(time1114Comparator);
        } else if(maxTime.equals("1417")) {
            pList.sort(time1417Comparator);
        } else if(maxTime.equals("1721")) {
            pList.sort(time1721Comparator);
        } else {
            pList.sort(time2124Comparator);
        }

        if (pList.size() > rank) {
            pList.subList(rank, pList.size()).clear();
        }

        return pList;
    }
}
