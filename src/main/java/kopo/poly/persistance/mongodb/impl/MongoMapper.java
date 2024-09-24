package kopo.poly.persistance.mongodb.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import kopo.poly.dto.SeoulSiMarketDTO;
import kopo.poly.dto.WalkDTO;
import kopo.poly.persistance.mongodb.AbstractMongoDBCommon;
import kopo.poly.persistance.mongodb.IMongoMapper;
import kopo.poly.util.CmmUtil;
import kopo.poly.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class MongoMapper extends AbstractMongoDBCommon implements IMongoMapper {

    private final MongoTemplate mongodb;
    @Override
    public int insertData(List<WalkDTO> pList, String colNm) {

        log.info(this.getClass().getName() + ".insertData Start");

        int res = 0;

        // 데이터를 저장할 컬렉션 생성
        super.createCollection(mongodb, colNm, "modelNm");

        // 저장할 컬렉션 객체 생성
        MongoCollection<Document> col = mongodb.getCollection(colNm);

        for (WalkDTO pDTO : pList) {
            // 레코드 한개씩 저장하기
            col.insertOne(new Document(new ObjectMapper().convertValue(pDTO, Map.class)));
        }

        res = 1;

        log.info(this.getClass().getName() + ".insertData End!");

        return res;
    }

    @Override
    public int dropCollection(String colNm) {
        log.info(this.getClass().getName() + ".dropCollection Start!");

        int res = 0;

        try {
            mongodb.dropCollection(colNm);
        } catch (Exception e) {
            log.info("Exception : " + e);
            res = 1;
        }

        log.info(this.getClass().getName() + ".dropCollection End!");

        return res;
    }

    @Override
    public List<WalkDTO> getWalkInfoList(String serialNo) {

        log.info(this.getClass().getName() + ".getWalkList Start!");

        // 가져와야하는 데이터
        // CCTVNO와 일치하는 데이터
        // 시간순으로 정렬
        String colNm = "SEOUL_WALK_INFO";
        List<WalkDTO> rList = new LinkedList<>();

        Document query = new Document();

        // serialNo이 일치하는 값 가져오기
        /*query.append("serialNo", new Document("$regex", ".*" + serialNo + "$"));*/
        query.append("serialNo", serialNo);

        Document projection = new Document();

        projection.append("sensingTime", "$sensingTime");
        projection.append("visitorCount", "$visitorCount");
        projection.append("_id", 0);

        Document sort = new Document();

        sort.append("sensingTime", 1);


        // 컬렉션 이름이랑 같은 db 데이터 가져오기
        MongoCollection<Document> col = mongodb.getCollection(colNm);
        FindIterable<Document> rs = col.find(query).sort(sort).projection(projection);

        for (Document doc : rs) {

            String sensingTime = CmmUtil.nvl(doc.getString("sensingTime"));
            String visitorCount = CmmUtil.nvl(doc.getString("visitorCount"));

            log.info("sensingTime : " + sensingTime + "visitorCount : " + visitorCount);


            WalkDTO pDTO = WalkDTO.builder()
                    .sensingTime(sensingTime)
                    .visitorCount(visitorCount)
                    .build();

            rList.add(pDTO);

        }

        log.info(this.getClass().getName() + ".getWalkList End!");

        return rList;
    }

    @Override
    public List<WalkDTO> getWalkList() {
        log.info(this.getClass().getName() + ".getWalkList Start!");

        // 가져와야하는 데이터
        // CCTVNO와 일치하는 데이터
        // 시간순으로 정렬
        String colNm = "SEOUL_WALK_LATLON";
        List<WalkDTO> rList = new LinkedList<>();

        Document query = new Document();

        Document projection = new Document();

        projection.append("ADDR", "$ADDR");
        projection.append("SERIAL_NO", "$SERIAL_NO");
        projection.append("LAT", "$LAT");
        projection.append("LON", "$LON");
        projection.append("_id", 0);
        projection.append("_id", 0);

        Document sort = new Document();

        sort.append("ADDR", 1);


        // 컬렉션 이름이랑 같은 db 데이터 가져오기
        MongoCollection<Document> col = mongodb.getCollection(colNm);
        FindIterable<Document> rs = col.find(query).sort(sort).projection(projection);

        for (Document doc : rs) {

            String addr = CmmUtil.nvl(doc.getString("ADDR"));
            String serialNo = CmmUtil.nvl(doc.getString("SERIAL_NO"));
            String lat = CmmUtil.nvl(doc.getString("LAT"));
            String lon = CmmUtil.nvl(doc.getString("LON"));

            log.info("addr : " + addr + " / serialNo : " + serialNo + " / lat  " + lat + " / lon  " +  lon);


            WalkDTO pDTO = WalkDTO.builder()
                    .serialNo(serialNo)
                    .addr(addr)
                    .lat(lat)
                    .lon(lon)
                    .build();

            rList.add(pDTO);

        }

        log.info(this.getClass().getName() + ".getWalkList End!");

        return rList;
    }
}
