package kopo.poly.persistance.mongodb.impl;

import com.mongodb.client.AggregateIterable;
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
import java.util.function.Consumer;

@Slf4j
@Component
@RequiredArgsConstructor
public class GuMapper implements IGuMapper {

    private final MongoTemplate mongodb;

    @Override
    public List<SeoulSiMarketDTO> getGuSalesList(String seoulGuYear, String seoulLocationCd, String colNm) throws Exception {

        log.info(this.getClass().getName() + ".getGuSalesList Start!");

        // 가져와야하는 데이터
        // 이번분기 지역 기준, 매출액, 업종명
        // 전분기 지역 기준, 매출액, 업종명

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
    public List<SeoulSiMarketDTO> getGuStoreList(String seoulGuYear, String seoulLocationCd, String colNm) throws Exception {

        log.info(this.getClass().getName() + ".getGuStoreList Start!");

        // 가져와야하는 데이터
        // 이번분기 지역 기준, 업종명, 점포수
        // 전분기 지역 기준, 업종명, 점포수

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
    public List<SeoulSiMarketDTO> getGuCloseStoreList(String seoulGuYear, String seoulLocationCd, String colNm) throws Exception {

        log.info(this.getClass().getName() + ".getGuCloseStoreList Start!");

        // 가져와야하는 데이터
        // 이번분기 지역 기준, 업종명, 점포수
        // 전분기 지역 기준, 업종명, 점포수

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
    public SeoulSiMarketDTO getGuLatLon(String seoulLocationCd, String colNm) throws Exception {

        log.info(this.getClass().getName() + ".getGuLatLon Start!");

        // 가져와야하는 데이터
        // 지역코드랑 같은 좌표 데이터

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

        // 가져와야하는 데이터
        // 조건에 일치하는 매출액 조회

        Document query = new Document();

        query.append("SEOUL_GU_YEAR", year);
        query.append("INDUTY_CD", new Document("$regex", "^" + induty));
        query.append("SEOUL_LOCATION_CD", guSelect);

        Document projection = new Document();

        projection.append("SEOUL_LOCATION_NM", "$SEOUL_LOCATION_NM");
        projection.append("MONTH_SALES", "$MONTH_SALES");
        projection.append("_id", 0);


        // 컬렉션 이름이랑 같은 db 데이터 가져오기
        MongoCollection<Document> col = mongodb.getCollection(colNm);
        FindIterable<Document> rs = col.find(query).projection(projection);

        double sumMonthSales = 0;

        for (Document doc : rs) {

            String seoulLocationNm = CmmUtil.nvl(doc.getString("SEOUL_LOCATION_NM"));
            double monthSales = doc.getDouble("MONTH_SALES");

            log.info("seoulLocationNm : " + seoulLocationNm + " / monthSales : " + monthSales);

            sumMonthSales += monthSales;

        }

        SeoulSiMarketDTO rDTO = SeoulSiMarketDTO.builder()
                .monthSales(sumMonthSales)
                .build();

        log.info(this.getClass().getName() + ".getGuSalesGraphLikeInduty End!");

        return  rDTO;

    }

    @Override
    public SeoulSiMarketDTO getGuStoreGraphLikeInduty(String year, String induty, String guSelect, String colNm) {

        log.info(this.getClass().getName() + ".getGuStoreGraphLikeInduty Start!");

        // 가져와야하는 데이터
        // 조건에 일치하는 매출액 조회

        Document query = new Document();

        // MongoDB 조회 쿼리
        query.append("SEOUL_GU_YEAR", year);
        query.append("INDUTY_CD", new Document("$regex", "^" + induty));
        query.append("SEOUL_LOCATION_CD", guSelect);

        Document projection = new Document();

        projection.append("SEOUL_LOCATION_NM", "$SEOUL_LOCATION_NM");
        projection.append("STORE_COUNT", "$STORE_COUNT");
        projection.append("_id", 0);

        // 컬렉션 이름이랑 같은 db 데이터 가져오기
        MongoCollection<Document> col = mongodb.getCollection(colNm);
        FindIterable<Document> rs = col.find(query).projection(projection);

        double sumStoreCount = 0;

        for (Document doc : rs) {

            String seoulLocationNm = CmmUtil.nvl(doc.getString("SEOUL_LOCATION_NM"));
            double storeCount = doc.getDouble("STORE_COUNT");

            log.info("seoulLocationNm : " + seoulLocationNm + " / storeCount : " + storeCount);

            sumStoreCount += storeCount;

        }

        SeoulSiMarketDTO rDTO = SeoulSiMarketDTO.builder()
                .storeCount(sumStoreCount)
                .build();

        log.info(this.getClass().getName() + ".getGuStoreGraphLikeInduty End!");

        return  rDTO;
    }

    @Override
    public SeoulSiMarketDTO getGuSalesGraphByIndutyNm(String year, String induty, String guSelect, String colNm) {

        log.info(this.getClass().getName() + ".getGuSalesGraphByIndutyNm Start!");

        // 가져와야하는 데이터
        // 조건에 일치하는 매출액 조회

        Document query = new Document();

        query.append("SEOUL_GU_YEAR", year);
        query.append("INDUTY_NM", induty);
        query.append("SEOUL_LOCATION_CD", guSelect);

        Document projection = new Document();

        projection.append("SEOUL_LOCATION_NM", "$SEOUL_LOCATION_NM");
        projection.append("MONTH_SALES", "$MONTH_SALES");
        projection.append("_id", 0);


        // 컬렉션 이름이랑 같은 db 데이터 가져오기
        MongoCollection<Document> col = mongodb.getCollection(colNm);
        FindIterable<Document> rs = col.find(query).projection(projection);

        double sumMonthSales = 0;

        for (Document doc : rs) {

            String seoulLocationNm = CmmUtil.nvl(doc.getString("SEOUL_LOCATION_NM"));
            double monthSales = doc.getDouble("MONTH_SALES");

            log.info("seoulLocationNm : " + seoulLocationNm + " / monthSales : " + monthSales);

            sumMonthSales += monthSales;

        }

        SeoulSiMarketDTO rDTO = SeoulSiMarketDTO.builder()
                .monthSales(sumMonthSales)
                .build();

        log.info(this.getClass().getName() + ".getGuSalesGraphByIndutyNm End!");

        return  rDTO;

    }

    @Override
    public SeoulSiMarketDTO getGuStoreGraphByIndutyNm(String year, String induty, String guSelect, String colNm) {

        log.info(this.getClass().getName() + ".getGuStoreGraphByIndutyNm Start!");

        // 가져와야하는 데이터
        // 조건에 일치하는 매출액 조회

        Document query = new Document();

        // MongoDB 조회 쿼리
        query.append("SEOUL_GU_YEAR", year);
        query.append("INDUTY_NM", induty);
        query.append("SEOUL_LOCATION_CD", guSelect);

        Document projection = new Document();

        projection.append("SEOUL_LOCATION_NM", "$SEOUL_LOCATION_NM");
        projection.append("STORE_COUNT", "$STORE_COUNT");
        projection.append("_id", 0);

        // 컬렉션 이름이랑 같은 db 데이터 가져오기
        MongoCollection<Document> col = mongodb.getCollection(colNm);
        FindIterable<Document> rs = col.find(query).projection(projection);

        double sumStoreCount = 0;

        for (Document doc : rs) {

            String seoulLocationNm = CmmUtil.nvl(doc.getString("SEOUL_LOCATION_NM"));
            double storeCount = doc.getDouble("STORE_COUNT");

            log.info("seoulLocationNm : " + seoulLocationNm + " / storeCount : " + storeCount);

            sumStoreCount += storeCount;

        }

        SeoulSiMarketDTO rDTO = SeoulSiMarketDTO.builder()
                .storeCount(sumStoreCount)
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

}
