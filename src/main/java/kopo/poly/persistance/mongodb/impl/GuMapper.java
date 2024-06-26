package kopo.poly.persistance.mongodb.impl;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import kopo.poly.dto.SeoulSiMarketDTO;
import kopo.poly.persistance.mongodb.IGuMapper;
import kopo.poly.util.CmmUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.BsonRegularExpression;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class GuMapper implements IGuMapper {

    private final MongoTemplate mongodb;

    @Override
    public List<SeoulSiMarketDTO> getGuSalesList(String seoulGuYear, String seoulLocationCd, String colNm) {

        log.info(this.getClass().getName() + ".getGuSalesList Start!");

        List<SeoulSiMarketDTO> rList = new LinkedList<>();

        Document query = new Document();

        query.append("SEOUL_GU_YEAR", seoulGuYear);
        query.append("SEOUL_LOCATION_CD", seoulLocationCd);

        Document projection = new Document();

        projection.append("INDUTY_NM", "$INDUTY_NM");
        projection.append("MONTH_SALES", "$MONTH_SALES");
        projection.append("_id", 0);


        // 컬렉션 이름이랑 같은 db 데이터 가져오기
        MongoCollection<Document> col = mongodb.getCollection(colNm);
        FindIterable<Document> rs = col.find(query).projection(projection);

        for (Document doc : rs) {

            String indutyNm = CmmUtil.nvl(doc.getString("INDUTY_NM"));
            double monthSales = doc.getDouble("MONTH_SALES");

            log.info("indutyNm : " + indutyNm + " / monthSales : " + monthSales);

            SeoulSiMarketDTO pDTO = SeoulSiMarketDTO.builder()
                    .indutyNm(indutyNm)
                    .monthSales(monthSales)
                    .build();

            rList.add(pDTO);

        }


        log.info(this.getClass().getName() + ".getGuSalesList End!");

        return rList;
    }

    @Override
    public List<SeoulSiMarketDTO> getGuStoreList(String seoulGuYear, String seoulLocationCd, String colNm) {

        log.info(this.getClass().getName() + ".getGuStoreList Start!");

        List<SeoulSiMarketDTO> rList = new LinkedList<>();

        Document query = new Document();

        query.append("SEOUL_GU_YEAR", seoulGuYear);
        query.append("SEOUL_LOCATION_CD", seoulLocationCd);

        Document projection = new Document();

        projection.append("INDUTY_NM", "$INDUTY_NM");
        projection.append("STORE_COUNT", "$STORE_COUNT");
        projection.append("_id", 0);


        // 컬렉션 이름이랑 같은 db 데이터 가져오기
        MongoCollection<Document> col = mongodb.getCollection(colNm);
        FindIterable<Document> rs = col.find(query).projection(projection);

        for (Document doc : rs) {

            String indutyNm = CmmUtil.nvl(doc.getString("INDUTY_NM"));
            double storeCount = doc.getDouble("STORE_COUNT");

            log.info("indutyNm : " + indutyNm + " / storeCount : " + storeCount);

            SeoulSiMarketDTO pDTO = SeoulSiMarketDTO.builder()
                    .indutyNm(indutyNm)
                    .storeCount(storeCount)
                    .build();

            rList.add(pDTO);

        }

        log.info(this.getClass().getName() + ".getGuStoreList End!");

        return rList;

    }

    @Override
    public List<SeoulSiMarketDTO> getGuCloseStoreList(String seoulGuYear, String seoulLocationCd, String colNm) {

        log.info(this.getClass().getName() + ".getGuCloseStoreList Start!");

        List<SeoulSiMarketDTO> rList = new LinkedList<>();

        Document query = new Document();

        query.append("SEOUL_GU_YEAR", seoulGuYear);
        query.append("SEOUL_LOCATION_CD", seoulLocationCd);

        Document projection = new Document();

        projection.append("INDUTY_NM", "$INDUTY_NM");
        projection.append("CLOSE_STORE_COUNT", "$CLOSE_STORE_COUNT");
        projection.append("_id", 0);


        // 컬렉션 이름이랑 같은 db 데이터 가져오기
        MongoCollection<Document> col = mongodb.getCollection(colNm);
        FindIterable<Document> rs = col.find(query).projection(projection);

        for (Document doc : rs) {

            String indutyNm = CmmUtil.nvl(doc.getString("INDUTY_NM"));
            double closeStoreCount = doc.getDouble("CLOSE_STORE_COUNT");

            log.info("indutyNm : " + indutyNm + " / closeStoreCount : " + closeStoreCount);

            SeoulSiMarketDTO pDTO = SeoulSiMarketDTO.builder()
                    .indutyNm(indutyNm)
                    .closeStoreCount(closeStoreCount)
                    .build();

            rList.add(pDTO);

        }

        log.info(this.getClass().getName() + ".getGuCloseStoreList End!");

        return rList;

    }

    @Override
    public SeoulSiMarketDTO getGuLatLon(String seoulLocationCd, String colNm) {

        log.info(this.getClass().getName() + ".getGuLatLon Start!");

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

        log.info(this.getClass().getName() + ".getGuLatLon End!");

        return rDTO;
    }

    /**
     *
     *  그래프 기능
     *
     */

    @Override
    public SeoulSiMarketDTO getGuSalesGraphLikeInduty(String year, String induty, String guSelect, String colNm) {

        log.info(this.getClass().getName() + ".getGuSalesGraphLikeInduty Start!");

        Document query = new Document();

        query.append("SEOUL_GU_YEAR", year);
        query.append("INDUTY_CD", new Document("$regex", "^" + induty));
        query.append("SEOUL_LOCATION_CD", guSelect);

        Document projection = new Document();

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

        double sumMonthSales = 0;
        double sumAge10Sales = 0;
        double sumAge20Sales = 0;
        double sumAge30Sales = 0;
        double sumAge40Sales = 0;
        double sumAge50Sales = 0;
        double sumAge60Sales = 0;

        for (Document doc : rs) {

            String seoulLocationNm = CmmUtil.nvl(doc.getString("SEOUL_LOCATION_NM"));
            double monthSales = doc.getDouble("MONTH_SALES");
            double age10Sales = doc.getDouble("AGE_10_SALES");
            double age20Sales = doc.getDouble("AGE_20_SALES");
            double age30Sales = doc.getDouble("AGE_30_SALES");
            double age40Sales = doc.getDouble("AGE_40_SALES");
            double age50Sales = doc.getDouble("AGE_50_SALES");
            double age60Sales = doc.getDouble("AGE_60_SALES");

            log.info("seoulLocationNm : " + seoulLocationNm + " / monthSales : " + monthSales);
            log.info("age10Sales : " + age10Sales);

            sumMonthSales += monthSales;
            sumAge10Sales += age10Sales;
            sumAge20Sales += age20Sales;
            sumAge30Sales += age30Sales;
            sumAge40Sales += age40Sales;
            sumAge50Sales += age50Sales;
            sumAge60Sales += age60Sales;

        }

        SeoulSiMarketDTO rDTO = SeoulSiMarketDTO.builder()
                .monthSales(sumMonthSales)
                .age10Sales(sumAge10Sales)
                .age20Sales(sumAge20Sales)
                .age30Sales(sumAge30Sales)
                .age40Sales(sumAge40Sales)
                .age50Sales(sumAge50Sales)
                .age60Sales(sumAge60Sales)
                .build();

        log.info(this.getClass().getName() + ".getGuSalesGraphLikeInduty End!");

        return  rDTO;

    }

    @Override
    public SeoulSiMarketDTO getGuStoreGraphLikeInduty(String year, String induty, String guSelect, String colNm) {

        log.info(this.getClass().getName() + ".getGuStoreGraphLikeInduty Start!");

        Document query = new Document();

        // MongoDB 조회 쿼리
        query.append("SEOUL_GU_YEAR", year);
        query.append("INDUTY_CD", new Document("$regex", "^" + induty));
        query.append("SEOUL_LOCATION_CD", guSelect);

        Document projection = new Document();

        projection.append("SEOUL_LOCATION_NM", "$SEOUL_LOCATION_NM");
        projection.append("STORE_COUNT", "$STORE_COUNT");
        projection.append("CLOSE_STORE_COUNT", "$CLOSE_STORE_COUNT");
        projection.append("_id", 0);

        // 컬렉션 이름이랑 같은 db 데이터 가져오기
        MongoCollection<Document> col = mongodb.getCollection(colNm);
        FindIterable<Document> rs = col.find(query).projection(projection);

        double sumStoreCount = 0;
        double sumCloseCount = 0;

        for (Document doc : rs) {

            String seoulLocationNm = CmmUtil.nvl(doc.getString("SEOUL_LOCATION_NM"));
            double storeCount = doc.getDouble("STORE_COUNT");
            double closeStoreCount = doc.getDouble("CLOSE_STORE_COUNT");

            log.info("seoulLocationNm : " + seoulLocationNm + " / storeCount : " + storeCount + " / closeStoreCount : " + closeStoreCount);

            sumStoreCount += storeCount;
            sumCloseCount += closeStoreCount;

        }

        SeoulSiMarketDTO rDTO = SeoulSiMarketDTO.builder()
                .storeCount(sumStoreCount)
                .closeStoreCount(sumCloseCount)
                .build();

        log.info(this.getClass().getName() + ".getGuStoreGraphLikeInduty End!");

        return  rDTO;
    }

    @Override
    public SeoulSiMarketDTO getGuSalesGraphByIndutyNm(String year, String induty, String guSelect, String colNm) {

        log.info(this.getClass().getName() + ".getGuSalesGraphByIndutyNm Start!");

        Document query = new Document();

        query.append("SEOUL_GU_YEAR", year);
        query.append("INDUTY_NM", induty);
        query.append("SEOUL_LOCATION_CD", guSelect);

        Document projection = new Document();

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


        // 컬렉션 이름이랑 같은 db 데이터 가져오기
        MongoCollection<Document> col = mongodb.getCollection(colNm);
        FindIterable<Document> rs = col.find(query).projection(projection);

        double sumMonthSales = 0;
        double sumAge10Sales = 0;
        double sumAge20Sales = 0;
        double sumAge30Sales = 0;
        double sumAge40Sales = 0;
        double sumAge50Sales = 0;
        double sumAge60Sales = 0;
        double sumTime0006Sales = 0;
        double sumTime0611Sales = 0;
        double sumTime1114Sales = 0;
        double sumTime1417Sales = 0;
        double sumTime1721Sales = 0;
        double sumTime2124Sales = 0;
        double sumFemaleSales = 0;
        double sumMaleSales = 0;

        for (Document doc : rs) {

            String seoulLocationNm = CmmUtil.nvl(doc.getString("SEOUL_LOCATION_NM"));
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

            log.info("seoulLocationNm : " + seoulLocationNm + " / monthSales : " + monthSales);

            sumMonthSales += monthSales;
            sumAge10Sales += age10Sales;
            sumAge20Sales += age20Sales;
            sumAge30Sales += age30Sales;
            sumAge40Sales += age40Sales;
            sumAge50Sales += age50Sales;
            sumAge60Sales += age60Sales;
            sumTime0006Sales += time0006Sales;
            sumTime0611Sales += time0611Sales;
            sumTime1114Sales += time1114Sales;
            sumTime1417Sales += time1417Sales;
            sumTime1721Sales += time1721Sales;
            sumTime2124Sales += time2124Sales;
            sumMaleSales += maleSales;
            sumFemaleSales += femaleSales;

        }

        SeoulSiMarketDTO rDTO = SeoulSiMarketDTO.builder()
                .monthSales(sumMonthSales)
                .age10Sales(sumAge10Sales)
                .age20Sales(sumAge20Sales)
                .age30Sales(sumAge30Sales)
                .age40Sales(sumAge40Sales)
                .age50Sales(sumAge50Sales)
                .age60Sales(sumAge60Sales)
                .time0006Sales(sumTime0006Sales)
                .time0611Sales(sumTime0611Sales)
                .time1114Sales(sumTime1114Sales)
                .time1417Sales(sumTime1417Sales)
                .time1721Sales(sumTime1721Sales)
                .time2124Sales(sumTime2124Sales)
                .maleSales(sumMaleSales)
                .femaleSales(sumFemaleSales)
                .build();

        log.info(this.getClass().getName() + ".getGuSalesGraphByIndutyNm End!");

        return  rDTO;

    }

    @Override
    public SeoulSiMarketDTO getGuStoreGraphByIndutyNm(String year, String induty, String guSelect, String colNm) {

        log.info(this.getClass().getName() + ".getGuStoreGraphByIndutyNm Start!");

        Document query = new Document();

        // MongoDB 조회 쿼리
        query.append("SEOUL_GU_YEAR", year);
        query.append("INDUTY_NM", induty);
        query.append("SEOUL_LOCATION_CD", guSelect);

        Document projection = new Document();

        projection.append("SEOUL_LOCATION_NM", "$SEOUL_LOCATION_NM");
        projection.append("STORE_COUNT", "$STORE_COUNT");
        projection.append("CLOSE_STORE_COUNT", "$CLOSE_STORE_COUNT");
        projection.append("_id", 0);

        // 컬렉션 이름이랑 같은 db 데이터 가져오기
        MongoCollection<Document> col = mongodb.getCollection(colNm);
        FindIterable<Document> rs = col.find(query).projection(projection);

        double sumStoreCount = 0;
        double sumCloseCount = 0;

        for (Document doc : rs) {

            String seoulLocationNm = CmmUtil.nvl(doc.getString("SEOUL_LOCATION_NM"));
            double storeCount = doc.getDouble("STORE_COUNT");
            double closeStoreCount = doc.getDouble("CLOSE_STORE_COUNT");

            log.info("seoulLocationNm : " + seoulLocationNm + " / storeCount : " + storeCount + " / closeStoreCount : " + closeStoreCount);


            sumStoreCount += storeCount;
            sumCloseCount += closeStoreCount;

        }

        SeoulSiMarketDTO rDTO = SeoulSiMarketDTO.builder()
                .storeCount(sumStoreCount)
                .closeStoreCount(sumCloseCount)
                .build();

        log.info(this.getClass().getName() + ".getGuStoreGraphByIndutyNm End!");

        return  rDTO;
    }

    /* 전체 업종 기준 점포당 매출액 */

    @Override
    public SeoulSiMarketDTO getAllSalesGraph(String year, String colNm) {

        log.info(this.getClass().getName() + ".getAllSalesGraph Start!");

        SeoulSiMarketDTO rDTO = SeoulSiMarketDTO.builder().build();
        double monthSales = 0;

        List<? extends Bson> pipeline = Arrays.asList(
                new Document()
                        .append("$match", new Document()
                                .append("SEOUL_GU_YEAR", year)
                        ),
                new Document()
                        .append("$group", new Document()
                                .append("_id", new Document()
                                        .append("SEOUL_GU_YEAR", "$SEOUL_DONG_YEAR")
                                )
                                .append("SUM(MONTH_SALES)", new Document()
                                        .append("$sum", "$MONTH_SALES")
                                )
                        ),
                new Document()
                        .append("$project", new Document()
                                .append("MONTH_SALES", "$SUM(MONTH_SALES)")
                                .append("_id", 0)
                        )
        );

        MongoCollection<Document> col = mongodb.getCollection(colNm);
        Document res = col.aggregate(pipeline)
                .allowDiskUse(true)
                .first();



        if(res != null) {

            monthSales = res.getDouble("MONTH_SALES");

            log.info("sum(monthSales) : " + monthSales);

                rDTO = SeoulSiMarketDTO.builder()
                    .monthSales(monthSales)
                    .build();

        }

        log.info(this.getClass().getName() + ".getAllSalesGraph End!");

        return rDTO;

    }

    @Override
    public SeoulSiMarketDTO getAllStoreGraph(String year, String colNm) {


        log.info(this.getClass().getName() + ".getAllStoreGraph Start!");

        SeoulSiMarketDTO rDTO = SeoulSiMarketDTO.builder().build();
        double storeCount = 0;

        List<? extends Bson> pipeline = Arrays.asList(
                new Document()
                        .append("$match", new Document()
                                .append("SEOUL_GU_YEAR", year)
                        ),
                new Document()
                        .append("$group", new Document()
                                .append("_id", new Document()
                                        .append("SEOUL_GU_YEAR", "$SEOUL_DONG_YEAR")
                                )
                                .append("SUM(STORE_COUNT)", new Document()
                                        .append("$sum", "$STORE_COUNT")
                                )
                        ),
                new Document()
                        .append("$project", new Document()
                                .append("STORE_COUNT", "$SUM(STORE_COUNT)")
                                .append("_id", 0)
                        )
        );

        MongoCollection<Document> col = mongodb.getCollection(colNm);
        Document res = col.aggregate(pipeline)
                .allowDiskUse(true)
                .first();

        if(res != null) {

            storeCount = res.getDouble("STORE_COUNT");

            log.info("sum(storeCount) : " + storeCount);

            rDTO = SeoulSiMarketDTO.builder()
                    .storeCount(storeCount)
                    .build();

        }

        log.info(this.getClass().getName() + ".getAllStoreGraph End!");

        return rDTO;

    }

    /* 지역별 매출 비중 */

    @Override
    public List<SeoulSiMarketDTO> getSortedMarketByIndutyNm(String year, String indutyNm, String colNm) {

        log.info(this.getClass().getName() + ".getSortedMarketByIndutyNm Start!");

        List<SeoulSiMarketDTO> rList = new LinkedList<>();

        Document query = new Document();

        log.info("year : " + year);
        log.info("indutyNm : " + indutyNm);

        query.append("SEOUL_GU_YEAR", year);
        query.append("INDUTY_NM", indutyNm);

        Document projection = new Document();

        projection.append("SEOUL_LOCATION_CD", "$SEOUL_LOCATION_CD");
        projection.append("SEOUL_LOCATION_NM", "$SEOUL_LOCATION_NM");
        projection.append("MONTH_SALES", "$MONTH_SALES");
        projection.append("_id", 0);


        // 컬렉션 이름이랑 같은 db 데이터 가져오기
        MongoCollection<Document> col = mongodb.getCollection(colNm);
        FindIterable<Document> rs = col.find(query).projection(projection);

        for (Document doc : rs) {

            String locationCd = CmmUtil.nvl(doc.getString("SEOUL_LOCATION_CD"));
            String locationNm = CmmUtil.nvl(doc.getString("SEOUL_LOCATION_NM"));
            double monthSales = doc.getDouble("MONTH_SALES");

            log.info("locationCd : " + locationCd + " / locationNm : " + locationNm + " / monthSales : " + monthSales);

            SeoulSiMarketDTO pDTO = SeoulSiMarketDTO.builder()
                    .seoulLocationCd(locationCd)
                    .seoulLocationNm(locationNm)
                    .monthSales(monthSales)
                    .build();

            rList.add(pDTO);

        }


        log.info(this.getClass().getName() + ".getSortedMarketByIndutyNm End!");

        return rList;

    }

    @Override
    public List<SeoulSiMarketDTO> getSortedMarketByIndutyCd(String year, String indutyCd, String colNm) {

        log.info(this.getClass().getName() + ".getSortedMarketByIndutyCd Start!");

        List<SeoulSiMarketDTO> rList = new LinkedList<>();

        Document query = new Document();

        log.info("indutycd : " + indutyCd);

        query.append("SEOUL_GU_YEAR", year);
        query.append("INDUTY_CD", new BsonRegularExpression("^" +indutyCd+".*$", "i"));

        Document projection = new Document();

        projection.append("SEOUL_LOCATION_CD", "$SEOUL_LOCATION_CD");
        projection.append("SEOUL_LOCATION_NM", "$SEOUL_LOCATION_NM");
        projection.append("MONTH_SALES", "$MONTH_SALES");
        projection.append("_id", 0);


        // 컬렉션 이름이랑 같은 db 데이터 가져오기
        MongoCollection<Document> col = mongodb.getCollection(colNm);
        FindIterable<Document> rs = col.find(query).projection(projection);

        for (Document doc : rs) {

            String locationCd = CmmUtil.nvl(doc.getString("SEOUL_LOCATION_CD"));
            String locationNm = CmmUtil.nvl(doc.getString("SEOUL_LOCATION_NM"));
            double monthSales = doc.getDouble("MONTH_SALES");

            log.info("locationCd : " + locationCd + " / locationNm : " + locationNm + " / monthSales : " + monthSales);

            SeoulSiMarketDTO pDTO = SeoulSiMarketDTO.builder()
                    .seoulLocationCd(locationCd)
                    .seoulLocationNm(locationNm)
                    .monthSales(monthSales)
                    .build();

            rList.add(pDTO);

        }


        log.info(this.getClass().getName() + ".getSortedMarketByIndutyCd End!");

        return rList;

    }

    @Override
    public List<SeoulSiMarketDTO> getSortedStoreByIndutyNm(String year, String indutyNm, String colNm) {

        log.info(this.getClass().getName() + ".getSortedStoreByIndutyNm Start!");

        List<SeoulSiMarketDTO> rList = new LinkedList<>();

        Document query = new Document();

        query.append("SEOUL_GU_YEAR", year);
        query.append("INDUTY_NM", indutyNm);

        Document projection = new Document();

        projection.append("SEOUL_LOCATION_CD", "$SEOUL_LOCATION_CD");
        projection.append("SEOUL_LOCATION_NM", "$SEOUL_LOCATION_NM");
        projection.append("STORE_COUNT", "$STORE_COUNT");
        projection.append("_id", 0);


        // 컬렉션 이름이랑 같은 db 데이터 가져오기
        MongoCollection<Document> col = mongodb.getCollection(colNm);
        FindIterable<Document> rs = col.find(query).projection(projection);

        for (Document doc : rs) {

            String locationCd = CmmUtil.nvl(doc.getString("SEOUL_LOCATION_CD"));
            String locationNm = CmmUtil.nvl(doc.getString("SEOUL_LOCATION_NM"));
            double storeCount = doc.getDouble("STORE_COUNT");

            log.info("locationCd : " + locationCd + " / locationNm : " + locationNm + " / monthSales : " + storeCount);

            SeoulSiMarketDTO pDTO = SeoulSiMarketDTO.builder()
                    .seoulLocationCd(locationCd)
                    .seoulLocationNm(locationNm)
                    .storeCount(storeCount)
                    .build();

            rList.add(pDTO);

        }


        log.info(this.getClass().getName() + ".getSortedStoreByIndutyNm End!");

        return rList;
    }

    @Override
    public List<SeoulSiMarketDTO> getSortedStoreByIndutyCd(String year, String indutCd, String colNm) {

        log.info(this.getClass().getName() + ".getSortedStoreByIndutyCd Start!");

        List<SeoulSiMarketDTO> rList = new LinkedList<>();

        Document query = new Document();

        query.append("SEOUL_GU_YEAR", year);
        query.append("INDUTY_CD", new Document("$regex", "^" +  indutCd));

        Document projection = new Document();

        projection.append("SEOUL_LOCATION_CD", "$SEOUL_LOCATION_CD");
        projection.append("SEOUL_LOCATION_NM", "$SEOUL_LOCATION_NM");
        projection.append("STORE_COUNT", "$STORE_COUNT");
        projection.append("_id", 0);


        // 컬렉션 이름이랑 같은 db 데이터 가져오기
        MongoCollection<Document> col = mongodb.getCollection(colNm);
        FindIterable<Document> rs = col.find(query).projection(projection);

        for (Document doc : rs) {

            String locationCd = CmmUtil.nvl(doc.getString("SEOUL_LOCATION_CD"));
            String locationNm = CmmUtil.nvl(doc.getString("SEOUL_LOCATION_NM"));
            double storeCount = doc.getDouble("STORE_COUNT");

            log.info("locationCd : " + locationCd + " / locationNm : " + locationNm + " / storeCount : " + storeCount);

            SeoulSiMarketDTO pDTO = SeoulSiMarketDTO.builder()
                    .seoulLocationCd(locationCd)
                    .seoulLocationNm(locationNm)
                    .storeCount(storeCount)
                    .build();

            rList.add(pDTO);

        }


        log.info(this.getClass().getName() + ".getSortedStoreByIndutyCd End!");

        return rList;
    }

}
