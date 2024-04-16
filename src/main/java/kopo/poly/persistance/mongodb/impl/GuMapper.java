package kopo.poly.persistance.mongodb.impl;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import kopo.poly.dto.SeoulSiMarketDTO;
import kopo.poly.persistance.mongodb.IGuMapper;
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
}
