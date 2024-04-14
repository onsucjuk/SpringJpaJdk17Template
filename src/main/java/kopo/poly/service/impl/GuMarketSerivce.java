package kopo.poly.service.impl;

import kopo.poly.dto.SeoulSiMarketDTO;
import kopo.poly.persistance.mongodb.IGuMapper;
import kopo.poly.service.IGuMarketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class GuMarketSerivce implements IGuMarketService {

    private final IGuMapper guMapper;

    /**
     *
     * MongoDB 에서 받아온 데이터 가공해서 넘겨주기 (매출액)
     *
     * @param rank 최종 정제 후 순위
     * @param preYear 컨트롤러로 보내온 비교년도 데이터
     * @param recYear 컨트롤러로 보내온 기준년도 데이터
     * @param seoulLocationNm 컨트롤러로 보내온 지역명
     *
     */
    @Override
    public List<SeoulSiMarketDTO> getGuMarketRes(int rank, String preYear, String recYear, String seoulLocationNm) throws Exception {


            log.info(this.getClass().getName() + ".getGuMarketRes Start!");

            // 구 기준 매출액 MongoDB 컬렉션
            String colNm = "SEOUL_GU_MARKET";

            // 넘겨줄 rList
            List<SeoulSiMarketDTO> rList = new ArrayList<>();

            log.info("여기서부터는 '" + colNm + "' 데이터 가져오기");
            // 매출 데이터에서 가져온 이번분기 데이터 리스트
            List<SeoulSiMarketDTO> recSalesList = guMapper.getGuSalesList(recYear, seoulLocationNm, colNm);

            // 매출 데이터에서 가져온 전분기 데이터 리스트
            List<SeoulSiMarketDTO> preSalesList = guMapper.getGuSalesList(preYear, seoulLocationNm, colNm);


            //매출액쪽 데이터를 다 가져왔고 점포수 데이터를 가져와야하므로 컬렉션 네임 변경
            colNm = "SEOUL_GU_STORE";
            log.info("여기서부터는 '" + colNm + "' 데이터 가져오기");
            // 점포 데이터에서 가져온 이번분기 데이터 리스트
            List<SeoulSiMarketDTO> recStoreList = guMapper.getGuStoreList(recYear, seoulLocationNm, colNm);

            // 점포 데이터에서 가져온 이전분기 데이터 필터
            List<SeoulSiMarketDTO> preStoreList = guMapper.getGuStoreList(preYear, seoulLocationNm, colNm);

            // DecimalFormat 객체 생성 데이터 포맷
            DecimalFormat df = new DecimalFormat("#.##");

            for ( int j = 0; j < recSalesList.size(); j++) {

                // 기준년도 매출액 데이터
                // rec : recent의 약자(최신)
                SeoulSiMarketDTO recDTO = recSalesList.get(j);
                String recIndutyNm = recDTO.indutyNm();
                double recSales = recDTO.monthSales();


                // 가져온 데이터에서 비교년도에서 기준년도 산업 이름 기준으로 매출액 데이터 가져오기
                // 비교년도 매출액 초기화
                double preSales = 0;
                for (SeoulSiMarketDTO dto : preSalesList) {
                    if (dto.indutyNm().equals(recIndutyNm)) {
                        preSales = dto.monthSales();

                        break;
                    }
                }

                // 가져온 점포 데이터에서 기준년도에서 기준년도 산업 이름 기준으로 데이터 가져오기
                double recStoreCo = 0;
                for (SeoulSiMarketDTO dto : recStoreList) {
                    if (dto.indutyNm().equals(recIndutyNm)) {

                        recStoreCo = dto.storeCount();

                        break;
                    }
                }

                // 가져온 점포 데이터에서 비교년도에서 기준년도 산업 이름 기준으로 데이터 가져오기
                double preStoreCo = 0;
                for (SeoulSiMarketDTO dto : preStoreList) {
                    if (dto.indutyNm().equals(recIndutyNm)) {

                        preStoreCo = dto.storeCount();

                        break;
                    }
                }

                // 점포당 매출액 기준년도 rSales, 비교년도 pSales 1만 단위
                double rSales = (recSales / recStoreCo) / 10000;
                double pSales = (preSales / preStoreCo) / 10000;

                // 점포당 매출액 증가량 1만 단위
                long salesDiff = Math.round(rSales - pSales);

                // 점포당 매출액 상승률
                double saleRate = salesDiff / pSales;

                // 매출액 상승률을 %로 계산하여 소수점 두 자리까지 표시
                double salesRatePercent = saleRate * 100;
                String salesRate = df.format(salesRatePercent);

                // 매출액 형식 포맷
                String fMonthSales = df.format(rSales);

                // 필요한 값 업종명, 업종코드, 매출액, 매출액 증가량, 증가율
                SeoulSiMarketDTO pDTO = SeoulSiMarketDTO.builder()
                        .indutyNm(recIndutyNm)
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
            rList.subList(rank, rList.size()).clear();

            log.info(this.getClass().getName() + ".getGuMarketRes End!");

            return rList;
        }

    /**
     *
     * MongoDB 에서 받아온 데이터 가공해서 넘겨주기 (점포수)
     *
     * @param rank 최종 정제 후 순위
     * @param preYear 컨트롤러로 보내온 비교년도 데이터
     * @param recYear 컨트롤러로 보내온 기준년도 데이터
     * @param seoulLocationNm 컨트롤러로 보내온 지역명
     *
     */
    @Override
    public List<SeoulSiMarketDTO> getGuStoreRes(int rank, String preYear, String recYear, String seoulLocationNm) throws Exception {

        log.info(this.getClass().getName() + ".getGuStoreRes Start!");

        // 구 기준 점포수 관련 MongoDB 컬렉션
        String colNm = "SEOUL_GU_STORE";

        // 넘겨줄 rList
        List<SeoulSiMarketDTO> rList = new ArrayList<>();

        log.info("여기서부터는 '" + colNm + "' 데이터 가져오기");

        // 점포 데이터에서 가져온 이번분기 데이터 리스트
        List<SeoulSiMarketDTO> recStoreList = guMapper.getGuStoreList(recYear, seoulLocationNm, colNm);

        // 점포 데이터에서 가져온 이전분기 데이터 리스트
        List<SeoulSiMarketDTO> preStoreList = guMapper.getGuStoreList(preYear, seoulLocationNm, colNm);

        // DecimalFormat 객체 생성 데이터 포맷
        DecimalFormat df = new DecimalFormat("#.##");


        for ( int j = 0; j < recStoreList.size(); j++) {

            // 기준년도 점포수 데이터
            // rec : recent의 약자(최신)
            SeoulSiMarketDTO recDTO = recStoreList.get(j);
            String recIndutyNm = recDTO.indutyNm();
            double recStoreCo = recDTO.storeCount();

                // 가져온 데이터에서 비교년도에서 기준년도 산업 이름 기준으로 점포수 데이터 가져오기
                // 비교년도 점포수 초기화
            double preStoreCo = 0;
                for (SeoulSiMarketDTO dto : preStoreList) {
                    if (dto.indutyNm().equals(recIndutyNm)) {
                        preStoreCo = dto.storeCount();

                        break;
                    }
                }

            // 점포수 증가량
            double storeDiff = recStoreCo - preStoreCo;

            // 점포수 상승률
            double tStoreRate = 0;
            if(preStoreCo > 0) {

                tStoreRate = storeDiff / preStoreCo;

            }

            // 점포수 상승률을 %로 계산하여 소수점 두 자리까지 표시
            double storeRatePercent = tStoreRate * 100;
            String storeRate = df.format(storeRatePercent);

            // 필요한 값 업종명, 점포수, 점포수 증가량, 증가율
            SeoulSiMarketDTO pDTO = SeoulSiMarketDTO.builder()
                    .indutyNm(recIndutyNm)
                    .storeCount(recStoreCo)
                    .storeDiff(storeDiff)
                    .storeRate(storeRate)
                    .build();

            // 리스트에 추가
            rList.add(pDTO);
        }

        // rList 점포수 증가률 기준으로 높은 순서로 정렬
        Comparator<SeoulSiMarketDTO> storeRateComparator = (dto1, dto2) -> {
            double storeRate1 = Double.parseDouble(dto1.storeRate());
            double storeRate2 = Double.parseDouble(dto2.storeRate());
            return Double.compare(storeRate2, storeRate1); // 내림차순 정렬
        };

        // rList를 storeRate 기준으로 정렬
        Collections.sort(rList, storeRateComparator);

        // rList에서 rank 이외의 요소를 모두 삭제
        rList.subList(rank, rList.size()).clear();

        log.info(this.getClass().getName() + ".getGuStoreRes End!");

        return rList;

    }

    @Override
    public List<SeoulSiMarketDTO> getGuCloseStoreRes(int rank, String preYear, String recYear, String seoulLocationNm) throws Exception {

        log.info(this.getClass().getName() + ".getGuStoreCloseRes Start!");

        // 구 기준 폐업수 관련 MongoDB 컬렉션
        String colNm = "SEOUL_GU_STORE";

        // 넘겨줄 rList
        List<SeoulSiMarketDTO> rList = new ArrayList<>();

        log.info("여기서부터는 '" + colNm + "' 데이터 가져오기");

        // 점포 데이터에서 가져온 이번분기 데이터 리스트
        List<SeoulSiMarketDTO> recCloseStoreList = guMapper.getGuCloseStoreList(recYear, seoulLocationNm, colNm);

        // 점포 데이터에서 가져온 이전분기 데이터 리스트
        List<SeoulSiMarketDTO> preCloseStoreList = guMapper.getGuCloseStoreList(preYear, seoulLocationNm, colNm);

        // DecimalFormat 객체 생성 데이터 포맷
        DecimalFormat df = new DecimalFormat("#.##");


        for ( int j = 0; j < recCloseStoreList.size(); j++) {

            // 기준년도 폐업수 데이터
            // rec : recent의 약자(최신)
            SeoulSiMarketDTO recDTO = recCloseStoreList.get(j);
            String recIndutyNm = recDTO.indutyNm();
            double recCloseStoreCo = recDTO.closeStoreCount();

            // 가져온 데이터에서 비교년도에서 기준년도 산업 이름 기준으로 폐업수 데이터 가져오기
            // 비교년도 폐업수 초기화
            double preCloseStoreCo = 0;
            for (SeoulSiMarketDTO dto : preCloseStoreList) {
                if (dto.indutyNm().equals(recIndutyNm)) {
                    preCloseStoreCo = dto.closeStoreCount();

                    log.info("preStoreCount : " + preCloseStoreCo);
                    break;
                }
            }

            // 폐업수 증가량
            double closeStoreDiff = recCloseStoreCo - preCloseStoreCo;

            // 폐업 상승률
            double tCloseStoreRate = 0;
            if(preCloseStoreCo > 0) {

                tCloseStoreRate = closeStoreDiff / preCloseStoreCo;

            }

            // 폐업수 상승률을 %로 계산하여 소수점 두 자리까지 표시
            double closeStoreRatePercent = tCloseStoreRate * 100;
            String closeStoreRate = df.format(closeStoreRatePercent);

            // 필요한 값 업종명, 폐업수, 폐업수 증가량, 증가율
            SeoulSiMarketDTO pDTO = SeoulSiMarketDTO.builder()
                    .indutyNm(recIndutyNm)
                    .closeStoreCount(recCloseStoreCo)
                    .closeStoreDiff(closeStoreDiff)
                    .closeStoreRate(closeStoreRate)
                    .build();

            // 리스트에 추가
            rList.add(pDTO);

        }

        // rList 폐업수 증가률 기준으로 높은 순서로 정렬
        Comparator<SeoulSiMarketDTO> closeStoreRateComparator = (dto1, dto2) -> {
            double closeStoreRate1 = Double.parseDouble(dto1.closeStoreRate());
            double closeStoreRate2 = Double.parseDouble(dto2.closeStoreRate());
            return Double.compare(closeStoreRate2, closeStoreRate1); // 내림차순 정렬
        };

        // rList를 closeStoreRate 기준으로 정렬
        Collections.sort(rList, closeStoreRateComparator);

        // rList에서 rank 이외의 요소를 모두 삭제
        rList.subList(rank, rList.size()).clear();

        log.info(this.getClass().getName() + ".getGuStoreCloseRes End!");

        return rList;
    }

}