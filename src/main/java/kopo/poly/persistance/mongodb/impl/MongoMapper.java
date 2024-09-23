package kopo.poly.persistance.mongodb.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoCollection;
import kopo.poly.dto.WalkDTO;
import kopo.poly.persistance.mongodb.AbstractMongoDBCommon;
import kopo.poly.persistance.mongodb.IMongoMapper;
import kopo.poly.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

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
    public List<WalkDTO> getWalkList(String serialNo) {

        log.info(this.getClass().getName() + ".getWalkList Start!");

        log.info(this.getClass().getName() + ".getWalkList End!");

        return null;
    }
}
