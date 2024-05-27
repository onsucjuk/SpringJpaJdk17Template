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
     *  그래프 기능
     *
     */



}
