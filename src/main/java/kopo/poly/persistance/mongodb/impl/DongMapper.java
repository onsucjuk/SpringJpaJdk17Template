package kopo.poly.persistance.mongodb.impl;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import kopo.poly.dto.SeoulSiMarketDTO;
import kopo.poly.persistance.mongodb.IDongMapper;
import kopo.poly.util.CmmUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DongMapper implements IDongMapper {

    private final MongoTemplate mongodb;

    @Override
    public List<SeoulSiMarketDTO> getDongSalesAllByName(String seoulDongYear, String indutyName, String colNm) {

        log.info(this.getClass().getName() + ".getDongSalesAllByName Start!");

        // 가져와야하는 데이터
        // 이번분기 업종명 기준, 매출액, 지역명
        // 전분기 지역 기준, 매출액, 지역명

        List<SeoulSiMarketDTO> rList = new LinkedList<>();

        Document query = new Document();

        query.append("SEOUL_DONG_YEAR", seoulDongYear);
        query.append("INDUTY_NM", indutyName);

        Document projection = new Document();

        projection.append("SEOUL_LOCATION_CD", "$SEOUL_LOCATION_CD");
        projection.append("SEOUL_LOCATION_NM", "$SEOUL_LOCATION_NM");
        projection.append("MONTH_SALES", "$MONTH_SALES");
        projection.append("AGE_10_SALES", "$AGE_10_SALES");
        projection.append("AGE_20_SALES", "$AGE_20_SALES");
        projection.append("AGE_30_SALES", "$AGE_30_SALES");
        projection.append("AGE_40_SALES", "$AGE_40_SALES");
        projection.append("AGE_50_SALES", "$AGE_50_SALES");
        projection.append("AGE_60_SALES", "$AGE_60_SALES");
        projection.append("_id", 0);


        // 컬렉션 이름이랑 같은 db 데이터 가져오기
        MongoCollection<Document> col = mongodb.getCollection(colNm);
        FindIterable<Document> rs = col.find(query).projection(projection);

        for (Document doc : rs) {

            String seoulLocationCd = CmmUtil.nvl(doc.getString("SEOUL_LOCATION_CD"));
            String seoulLocationNm = CmmUtil.nvl(doc.getString("SEOUL_LOCATION_NM"));
            double monthSales = doc.getDouble("MONTH_SALES");
            double age10Sales = doc.getDouble("AGE_10_SALES");
            double age20Sales = doc.getDouble("AGE_20_SALES");
            double age30Sales = doc.getDouble("AGE_30_SALES");
            double age40Sales = doc.getDouble("AGE_40_SALES");
            double age50Sales = doc.getDouble("AGE_50_SALES");
            double age60Sales = doc.getDouble("AGE_60_SALES");

            log.info("seoulLocationCd : " + seoulLocationCd + "seoulLocationNm : " + seoulLocationNm + " / monthSales : " + monthSales);
            log.info("salesSort : " + age10Sales + "," + age20Sales + "," + age30Sales + "," + age40Sales + "," + age50Sales + "," + age60Sales + ",");

            SeoulSiMarketDTO pDTO = SeoulSiMarketDTO.builder()
                    .seoulLocationCd(seoulLocationCd)
                    .seoulLocationNm(seoulLocationNm)
                    .monthSales(monthSales)
                    .age10Sales(age10Sales)
                    .age20Sales(age20Sales)
                    .age30Sales(age30Sales)
                    .age40Sales(age40Sales)
                    .age50Sales(age50Sales)
                    .age60Sales(age60Sales)
                    .build();

            rList.add(pDTO);

        }


        log.info(this.getClass().getName() + ".getDongSalesAllByName End!");

        return rList;
    }

    @Override
    public List<SeoulSiMarketDTO> getDongSalesAllBySort(String seoulDongYear, String indutySort, String colNm) {

        log.info(this.getClass().getName() + ".getDongSalesAllBySort Start!");

        // 가져와야하는 데이터
        // 이번분기 업종명 기준, 매출액, 지역명
        // 전분기 지역 기준, 매출액, 지역명

        List<SeoulSiMarketDTO> rList = new LinkedList<>();

        Document query = new Document();

        query.append("SEOUL_DONG_YEAR", seoulDongYear);
        query.append("INDUTY_CD", new Document("$regex", "^" + indutySort));

        Document projection = new Document();

        projection.append("SEOUL_LOCATION_CD", "$SEOUL_LOCATION_CD");
        projection.append("SEOUL_LOCATION_NM", "$SEOUL_LOCATION_NM");
        projection.append("MONTH_SALES", "$MONTH_SALES");
        projection.append("AGE_10_SALES", "$AGE_10_SALES");
        projection.append("AGE_20_SALES", "$AGE_20_SALES");
        projection.append("AGE_30_SALES", "$AGE_30_SALES");
        projection.append("AGE_40_SALES", "$AGE_40_SALES");
        projection.append("AGE_50_SALES", "$AGE_50_SALES");
        projection.append("AGE_60_SALES", "$AGE_60_SALES");
        projection.append("_id", 0);


        // 컬렉션 이름이랑 같은 db 데이터 가져오기
        MongoCollection<Document> col = mongodb.getCollection(colNm);
        FindIterable<Document> rs = col.find(query).projection(projection);


        for (Document doc : rs) {

            String seoulLocationCd = CmmUtil.nvl(doc.getString("SEOUL_LOCATION_CD"));
            String seoulLocationNm = CmmUtil.nvl(doc.getString("SEOUL_LOCATION_NM"));
            double monthSales = doc.getDouble("MONTH_SALES");
            double age10Sales = doc.getDouble("AGE_10_SALES");
            double age20Sales = doc.getDouble("AGE_20_SALES");
            double age30Sales = doc.getDouble("AGE_30_SALES");
            double age40Sales = doc.getDouble("AGE_40_SALES");
            double age50Sales = doc.getDouble("AGE_50_SALES");
            double age60Sales = doc.getDouble("AGE_60_SALES");

            /*log.info("seoulLocationCd : " + seoulLocationCd + "seoulLocationNm : " + seoulLocationNm + " / monthSales : " + monthSales);
            log.info("salesSort : " + age10Sales + "," + age20Sales + "," + age30Sales + "," + age40Sales + "," + age50Sales + "," + age60Sales + ",");*/

            SeoulSiMarketDTO pDTO = SeoulSiMarketDTO.builder()
                    .seoulLocationCd(seoulLocationCd)
                    .seoulLocationNm(seoulLocationNm)
                    .monthSales(monthSales)
                    .age10Sales(age10Sales)
                    .age20Sales(age20Sales)
                    .age30Sales(age30Sales)
                    .age40Sales(age40Sales)
                    .age50Sales(age50Sales)
                    .age60Sales(age60Sales)
                    .build();

            rList.add(pDTO);

        }

        log.info(this.getClass().getName() + ".getDongSalesAllBySort End!");

        return rList;
    }

    @Override
    public List<SeoulSiMarketDTO> getDongStoreAllByName(String seoulDongYear, String indutyName, String colNm) {

        log.info(this.getClass().getName() + ".getDongStoreAllByName Start!");

        // 가져와야하는 데이터
        // 이번분기 업종명 기준, 지역명, 점포수
        // 전분기 업종명 기준, 지역명, 점포수

        List<SeoulSiMarketDTO> rList = new LinkedList<>();

        Document query = new Document();

        query.append("SEOUL_DONG_YEAR", seoulDongYear);
        query.append("INDUTY_NM", indutyName);

        Document projection = new Document();

        projection.append("SEOUL_LOCATION_CD", "$SEOUL_LOCATION_CD");
        projection.append("SEOUL_LOCATION_NM", "$SEOUL_LOCATION_NM");
        projection.append("STORE_COUNT", "$STORE_COUNT");
        projection.append("_id", 0);

        Document sort = new Document();

        sort.append("SEOUL_LOCATION_CD", 1);


        // 컬렉션 이름이랑 같은 db 데이터 가져오기
        MongoCollection<Document> col = mongodb.getCollection(colNm);
        FindIterable<Document> rs = col.find(query).projection(projection).sort(sort);

        for (Document doc : rs) {

            String seoulLocationCd = CmmUtil.nvl(doc.getString("SEOUL_LOCATION_CD"));
            String seoulLocationNm = CmmUtil.nvl(doc.getString("SEOUL_LOCATION_NM"));
            double storeCount = doc.getDouble("STORE_COUNT");

            SeoulSiMarketDTO pDTO = SeoulSiMarketDTO.builder()
                    .seoulLocationCd(seoulLocationCd)
                    .seoulLocationNm(seoulLocationNm)
                    .storeCount(storeCount)
                    .build();

            rList.add(pDTO);

        }

        log.info(seoulDongYear + "년도 점포수 size : " + rList.size());

        log.info(this.getClass().getName() + ".getDongStoreAllByName End!");

        return rList;
    }

    @Override
    public List<SeoulSiMarketDTO> getDongStoreAllBySort(String seoulDongYear, String indutySort, String colNm) {

        log.info(this.getClass().getName() + ".getDongStoreAllBySort Start!");

        // 가져와야하는 데이터
        // 이번분기 업종명 기준, 지역명, 점포수
        // 전분기 업종명 기준, 지역명, 점포수

        List<SeoulSiMarketDTO> rList = new LinkedList<>();

        Document query = new Document();

        query.append("SEOUL_DONG_YEAR", seoulDongYear);
        query.append("INDUTY_CD", new Document("$regex", "^" + indutySort));

        Document projection = new Document();

        projection.append("SEOUL_LOCATION_CD", "$SEOUL_LOCATION_CD");
        projection.append("SEOUL_LOCATION_NM", "$SEOUL_LOCATION_NM");
        projection.append("STORE_COUNT", "$STORE_COUNT");
        projection.append("_id", 0);


        // 컬렉션 이름이랑 같은 db 데이터 가져오기
        MongoCollection<Document> col = mongodb.getCollection(colNm);
        FindIterable<Document> rs = col.find(query).projection(projection);

        for (Document doc : rs) {

            String seoulLocationCd = CmmUtil.nvl(doc.getString("SEOUL_LOCATION_CD"));
            String seoulLocationNm = CmmUtil.nvl(doc.getString("SEOUL_LOCATION_NM"));
            double storeCount = doc.getDouble("STORE_COUNT");

            log.info("seoulLocationCd : " + seoulLocationCd + " / seoulLocationNm : " + seoulLocationNm + " / storeCount : " + storeCount);

            SeoulSiMarketDTO pDTO = SeoulSiMarketDTO.builder()
                    .seoulLocationCd(seoulLocationCd)
                    .seoulLocationNm(seoulLocationNm)
                    .storeCount(storeCount)
                    .build();

            rList.add(pDTO);

        }

        log.info(this.getClass().getName() + ".getDongStoreAllBySort End!");

        return rList;

    }

    @Override
    public List<SeoulSiMarketDTO> getDongSalesByLocationCdAndName(String seoulDongYear, String locationCd, String indutyName, String colNm) {

        log.info(this.getClass().getName() + ".getDongSalesByLocationCdAndName Start!");

        // 가져와야하는 데이터
        // 이번분기 업종명 기준, 매출액, 지역명
        // 전분기 지역 기준, 매출액, 지역명

        List<SeoulSiMarketDTO> rList = new LinkedList<>();

        Document query = new Document();

        query.append("SEOUL_DONG_YEAR", seoulDongYear);
        query.append("INDUTY_NM", indutyName);
        query.append("SEOUL_LOCATION_CD", new Document("$regex", "^" + locationCd));

        Document projection = new Document();

        projection.append("SEOUL_LOCATION_CD", "$SEOUL_LOCATION_CD");
        projection.append("SEOUL_LOCATION_NM", "$SEOUL_LOCATION_NM");
        projection.append("MONTH_SALES", "$MONTH_SALES");
        projection.append("AGE_10_SALES", "$AGE_10_SALES");
        projection.append("AGE_20_SALES", "$AGE_20_SALES");
        projection.append("AGE_30_SALES", "$AGE_30_SALES");
        projection.append("AGE_40_SALES", "$AGE_40_SALES");
        projection.append("AGE_50_SALES", "$AGE_50_SALES");
        projection.append("AGE_60_SALES", "$AGE_60_SALES");
        projection.append("_id", 0);


        // 컬렉션 이름이랑 같은 db 데이터 가져오기
        MongoCollection<Document> col = mongodb.getCollection(colNm);
        FindIterable<Document> rs = col.find(query).projection(projection);

        for (Document doc : rs) {

            String seoulLocationCd = CmmUtil.nvl(doc.getString("SEOUL_LOCATION_CD"));
            String seoulLocationNm = CmmUtil.nvl(doc.getString("SEOUL_LOCATION_NM"));
            double monthSales = doc.getDouble("MONTH_SALES");
            double age10Sales = doc.getDouble("AGE_10_SALES");
            double age20Sales = doc.getDouble("AGE_20_SALES");
            double age30Sales = doc.getDouble("AGE_30_SALES");
            double age40Sales = doc.getDouble("AGE_40_SALES");
            double age50Sales = doc.getDouble("AGE_50_SALES");
            double age60Sales = doc.getDouble("AGE_60_SALES");

            log.info("seoulLocationCd : " + seoulLocationCd + "seoulLocationNm : " + seoulLocationNm + " / monthSales : " + monthSales);

            SeoulSiMarketDTO pDTO = SeoulSiMarketDTO.builder()
                    .seoulLocationCd(seoulLocationCd)
                    .seoulLocationNm(seoulLocationNm)
                    .monthSales(monthSales)
                    .age10Sales(age10Sales)
                    .age20Sales(age20Sales)
                    .age30Sales(age30Sales)
                    .age40Sales(age40Sales)
                    .age50Sales(age50Sales)
                    .age60Sales(age60Sales)
                    .build();

            rList.add(pDTO);

        }


        log.info(this.getClass().getName() + ".getDongSalesByLocationCdAndName End!");

        return rList;

    }

    @Override
    public List<SeoulSiMarketDTO> getDongSalesByLocationCdAndSort(String seoulDongYear, String locationCd, String indutySort, String colNm) {

        log.info(this.getClass().getName() + ".getDongSalesByLocationCdAndSort Start!");

        // 가져와야하는 데이터
        // 이번분기 업종명 기준, 매출액, 지역명
        // 전분기 지역 기준, 매출액, 지역명

        List<SeoulSiMarketDTO> rList = new LinkedList<>();

        Document query = new Document();

        query.append("SEOUL_DONG_YEAR", seoulDongYear);
        query.append("INDUTY_CD", new Document("$regex", "^" + indutySort));
        query.append("SEOUL_LOCATION_CD", new Document("$regex", "^" + locationCd));

        Document projection = new Document();

        projection.append("SEOUL_LOCATION_CD", "$SEOUL_LOCATION_CD");
        projection.append("SEOUL_LOCATION_NM", "$SEOUL_LOCATION_NM");
        projection.append("MONTH_SALES", "$MONTH_SALES");
        projection.append("AGE_10_SALES", "$AGE_10_SALES");
        projection.append("AGE_20_SALES", "$AGE_20_SALES");
        projection.append("AGE_30_SALES", "$AGE_30_SALES");
        projection.append("AGE_40_SALES", "$AGE_40_SALES");
        projection.append("AGE_50_SALES", "$AGE_50_SALES");
        projection.append("AGE_60_SALES", "$AGE_60_SALES");
        projection.append("_id", 0);


        // 컬렉션 이름이랑 같은 db 데이터 가져오기
        MongoCollection<Document> col = mongodb.getCollection(colNm);
        FindIterable<Document> rs = col.find(query).projection(projection);


        for (Document doc : rs) {

            String seoulLocationCd = CmmUtil.nvl(doc.getString("SEOUL_LOCATION_CD"));
            String seoulLocationNm = CmmUtil.nvl(doc.getString("SEOUL_LOCATION_NM"));
            double monthSales = doc.getDouble("MONTH_SALES");
            double age10Sales = doc.getDouble("AGE_10_SALES");
            double age20Sales = doc.getDouble("AGE_20_SALES");
            double age30Sales = doc.getDouble("AGE_30_SALES");
            double age40Sales = doc.getDouble("AGE_40_SALES");
            double age50Sales = doc.getDouble("AGE_50_SALES");
            double age60Sales = doc.getDouble("AGE_60_SALES");

            log.info("seoulLocationCd : " + seoulLocationCd + "seoulLocationNm : " + seoulLocationNm + " / monthSales : " + monthSales);

            SeoulSiMarketDTO pDTO = SeoulSiMarketDTO.builder()
                    .seoulLocationCd(seoulLocationCd)
                    .seoulLocationNm(seoulLocationNm)
                    .monthSales(monthSales)
                    .age10Sales(age10Sales)
                    .age20Sales(age20Sales)
                    .age30Sales(age30Sales)
                    .age40Sales(age40Sales)
                    .age50Sales(age50Sales)
                    .age60Sales(age60Sales)
                    .build();

            rList.add(pDTO);

        }

        log.info(this.getClass().getName() + ".getDongSalesByLocationCdAndSort End!");

        return rList;
    }

    @Override
    public List<SeoulSiMarketDTO> getDongStoreByLocationCdAndName(String seoulDongYear, String locationCd, String indutyName, String colNm) {

        log.info(this.getClass().getName() + ".getDongStoreByLocationCdAndName Start!");

        // 가져와야하는 데이터
        // 이번분기 업종명 기준, 지역명, 점포수
        // 전분기 업종명 기준, 지역명, 점포수

        List<SeoulSiMarketDTO> rList = new LinkedList<>();

        Document query = new Document();

        log.info("seoulDongYear : " + seoulDongYear);
        log.info("locationCd : " + locationCd);
        log.info("indutyName : " + indutyName);
        log.info("colNm : " + colNm);

        query.append("SEOUL_DONG_YEAR", seoulDongYear);
        query.append("INDUTY_NM", indutyName);
        query.append("SEOUL_LOCATION_CD", new Document("$regex", "^" + locationCd));

        Document projection = new Document();

        projection.append("SEOUL_LOCATION_CD", "$SEOUL_LOCATION_CD");
        projection.append("SEOUL_LOCATION_NM", "$SEOUL_LOCATION_NM");
        projection.append("STORE_COUNT", "$STORE_COUNT");
        projection.append("_id", 0);


        // 컬렉션 이름이랑 같은 db 데이터 가져오기
        MongoCollection<Document> col = mongodb.getCollection(colNm);
        FindIterable<Document> rs = col.find(query).projection(projection);

        for (Document doc : rs) {

            String seoulLocationCd = CmmUtil.nvl(doc.getString("SEOUL_LOCATION_CD"));
            String seoulLocationNm = CmmUtil.nvl(doc.getString("SEOUL_LOCATION_NM"));
            double storeCount = doc.getDouble("STORE_COUNT");

            log.info("seoulLocationCd : " + seoulLocationCd + " / seoulLocationNm : " + seoulLocationNm + " / storeCount : " + storeCount);

            SeoulSiMarketDTO pDTO = SeoulSiMarketDTO.builder()
                    .seoulLocationCd(seoulLocationCd)
                    .seoulLocationNm(seoulLocationNm)
                    .storeCount(storeCount)
                    .build();

            rList.add(pDTO);

        }

        log.info(this.getClass().getName() + ".getDongStoreByLocationCdAndName End!");

        return rList;
    }

    @Override
    public List<SeoulSiMarketDTO> getDongStoreByLocationCdAndSort(String seoulDongYear, String locationCd, String indutySort, String colNm) {

        log.info(this.getClass().getName() + ".getDongStoreByLocationCdAndSort Start!");

        // 가져와야하는 데이터
        // 이번분기 업종명 기준, 지역명, 점포수
        // 전분기 업종명 기준, 지역명, 점포수

        List<SeoulSiMarketDTO> rList = new LinkedList<>();

        Document query = new Document();

        query.append("SEOUL_DONG_YEAR", seoulDongYear);
        query.append("INDUTY_CD", new Document("$regex", "^" + indutySort));
        query.append("SEOUL_LOCATION_CD", new Document("$regex", "^" + locationCd));

        Document projection = new Document();

        projection.append("SEOUL_LOCATION_CD", "$SEOUL_LOCATION_CD");
        projection.append("SEOUL_LOCATION_NM", "$SEOUL_LOCATION_NM");
        projection.append("STORE_COUNT", "$STORE_COUNT");
        projection.append("_id", 0);


        // 컬렉션 이름이랑 같은 db 데이터 가져오기
        MongoCollection<Document> col = mongodb.getCollection(colNm);
        FindIterable<Document> rs = col.find(query).projection(projection);

        for (Document doc : rs) {

            String seoulLocationCd = CmmUtil.nvl(doc.getString("SEOUL_LOCATION_CD"));
            String seoulLocationNm = CmmUtil.nvl(doc.getString("SEOUL_LOCATION_NM"));
            double storeCount = doc.getDouble("STORE_COUNT");

            log.info("seoulLocationCd : " + seoulLocationCd + " / seoulLocationNm : " + seoulLocationNm + " / storeCount : " + storeCount);

            SeoulSiMarketDTO pDTO = SeoulSiMarketDTO.builder()
                    .seoulLocationCd(seoulLocationCd)
                    .seoulLocationNm(seoulLocationNm)
                    .storeCount(storeCount)
                    .build();

            rList.add(pDTO);

        }

        log.info(this.getClass().getName() + ".getDongStoreByLocationCdAndSort End!");

        return rList;

    }

    /**
     *
     *  창업 지역 추천
     *
     */

    @Override
    public List<SeoulSiMarketDTO> getLocationMarketByIndutyNm(String year, String indutyNm, String colNm) {

        log.info(this.getClass().getName() + ".getLocationMarketByIndutyNm Start!");

        List<SeoulSiMarketDTO> rList = new LinkedList<>();

        Document query = new Document();

        log.info("year : " + year);
        log.info("indutyNm : " + indutyNm);

        query.append("SEOUL_DONG_YEAR", year);
        query.append("INDUTY_NM", indutyNm);

        Document projection = new Document();

        projection.append("SEOUL_LOCATION_CD", "$SEOUL_LOCATION_CD");
        projection.append("SEOUL_LOCATION_NM", "$SEOUL_LOCATION_NM");
        projection.append("MONTH_SALES", "$MONTH_SALES");
        projection.append("AGE_10_SALES", "$AGE_10_SALES");
        projection.append("AGE_20_SALES", "$AGE_20_SALES");
        projection.append("AGE_30_SALES", "$AGE_30_SALES");
        projection.append("AGE_40_SALES", "$AGE_40_SALES");
        projection.append("AGE_50_SALES", "$AGE_50_SALES");
        projection.append("AGE_60_SALES", "$AGE_60_SALES");
        projection.append("TIME_0006_SALES", "$TIME_0006_SALES");
        projection.append("TIME_0611_SALES", "$TIME_0611_SALES");
        projection.append("TIME_1114_SALES", "$TIME_1114_SALES");
        projection.append("TIME_1417_SALES", "$TIME_1417_SALES");
        projection.append("TIME_1721_SALES", "$TIME_1721_SALES");
        projection.append("TIME_2124_SALES", "$TIME_2124_SALES");
        projection.append("FEMALE_SALES", "$FEMALE_SALES");
        projection.append("MALE_SALES", "$MALE_SALES");
        projection.append("_id", 0);

        Document sort = new Document();

        sort.append("SEOUL_LOCATION_CD", 1);

        // 컬렉션 이름이랑 같은 db 데이터 가져오기
        MongoCollection<Document> col = mongodb.getCollection(colNm);
        FindIterable<Document> rs = col.find(query).projection(projection).sort(sort);

        for (Document doc : rs) {

            String locationCd = CmmUtil.nvl(doc.getString("SEOUL_LOCATION_CD"));
            String locationNm = CmmUtil.nvl(doc.getString("SEOUL_LOCATION_NM"));
            double monthSales = doc.getDouble("MONTH_SALES");
            double age10Sales = doc.getDouble("AGE_10_SALES");
            double age20Sales = doc.getDouble("AGE_20_SALES");
            double age30Sales = doc.getDouble("AGE_30_SALES");
            double age40Sales = doc.getDouble("AGE_40_SALES");
            double age50Sales = doc.getDouble("AGE_50_SALES");
            double age60Sales = doc.getDouble("AGE_60_SALES");
            double time0006Sales = doc.getDouble("TIME_0006_SALES");
            double time0611Sales = doc.getDouble("TIME_0611_SALES");
            double time1114Sales = doc.getDouble("TIME_1114_SALES");
            double time1417Sales = doc.getDouble("TIME_1417_SALES");
            double time1721Sales = doc.getDouble("TIME_1721_SALES");
            double time2124Sales = doc.getDouble("TIME_2124_SALES");
            double maleSales = doc.getDouble("FEMALE_SALES");
            double femaleSales = doc.getDouble("MALE_SALES");

            /*log.info("locationCd : " + locationCd + " / locationNm : " + locationNm + " / monthSales : " + monthSales);*/

            SeoulSiMarketDTO pDTO = SeoulSiMarketDTO.builder()
                    .seoulLocationCd(locationCd)
                    .seoulLocationNm(locationNm)
                    .monthSales(monthSales)
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

            rList.add(pDTO);

        }

        log.info(year + "년도 매출액 데이터들 사이즈 : " + rList.size());


        log.info(this.getClass().getName() + ".getLocationMarketByIndutyNm End!");

        return rList;

    }

    @Override
    public SeoulSiMarketDTO getDongGuLatLon(String seoulLocationCd, String colNm) {

        log.info(this.getClass().getName() + ".getDongGuLatLon Start!");
        log.info(seoulLocationCd + "지역 위도 경도 찾기!!");

        SeoulSiMarketDTO rDTO = SeoulSiMarketDTO.builder().build();

        Document query = new Document();

        query.append("SEOUL_LOCATION_CD", seoulLocationCd);

        Document projection = new Document();

        projection.append("LAT", "$LAT");
        projection.append("LON", "$LON");
        projection.append("_id", 0);


        // 컬렉션 이름이랑 같은 db 데이터 가져오기
        MongoCollection<Document> col = mongodb.getCollection(colNm);
        FindIterable<Document> rs = col.find(query).projection(projection);

        for (Document doc : rs) {

            double lat = doc.getDouble("LAT");
            double lon = doc.getDouble("LON");

            log.info("lat : " + lat + " / lon : " + lon);

            rDTO = SeoulSiMarketDTO.builder()
                    .lat(lat)
                    .lon(lon)
                    .build();

        }

        log.info(this.getClass().getName() + ".getDongGuLatLon End!");

        return rDTO;
    }


}
