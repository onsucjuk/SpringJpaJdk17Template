package kopo.poly.service.impl;

import kopo.poly.dto.SeoulSiMarketDTO;
import kopo.poly.persistance.mongodb.IDongMapper;
import kopo.poly.service.IDongMarketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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

            if (recStoreCo > 0) {

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

            // 매출액 상승률을 %로 계산하여 소수점 두 자리까지 표시
            double salesRatePercent = saleRate * 100;
            String salesRate = df.format(salesRatePercent);

            // 매출액 형식 포맷
            String fMonthSales = df.format(rSales);

            // 필요한 값 업종명, 업종코드, 매출액, 매출액 증가량, 증가율
            SeoulSiMarketDTO pDTO = SeoulSiMarketDTO.builder()
                    .seoulLocationNm(seoulLocationNm)
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
        if(rList.size()>10) {

            rList.subList(rank, rList.size()).clear();

        }

        log.info(this.getClass().getName() + ".getGuMarketRes End!");

        return rList;

    }

    @Override
    public List<SeoulSiMarketDTO> getDongStoreRes(int rank, String preYear, String recYear, String seoulLocationCd, String indutySort, String indutyName) throws Exception {
        return null;
    }

    @Override
    public List<SeoulSiMarketDTO> getDongCloseStoreRes(int rank, String preYear, String recYear, String seoulLocationCd, String indutySort, String indutyName) throws Exception {
        return null;
    }

    @Override
    public SeoulSiMarketDTO getDongLatLon(String seoulLocationCd) throws Exception {
        return null;
    }
}
