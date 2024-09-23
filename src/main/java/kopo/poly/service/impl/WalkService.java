package kopo.poly.service.impl;

import kopo.poly.dto.WalkDTO;
import kopo.poly.persistance.mongodb.IMongoMapper;
import kopo.poly.service.IWalkAPIService;
import kopo.poly.service.IWalkService;
import kopo.poly.util.CmmUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class WalkService implements IWalkService {

    private final IMongoMapper mongoMapper;
    @Value("${siMarket.api.key}")
    private String apikey;
    private final IWalkAPIService walkAPIService;


    public List<WalkDTO> doCollect() throws Exception {

        log.info(this.getClass().getName() + ".doCollect Start!");

        List<WalkDTO> pList = new LinkedList<>();


        int start = 1;
        int end = 1000;

        while(end < 15000) {

            String startIdx = String.valueOf(start);
            String endIdx = String.valueOf(end);

            WalkDTO rDTO = walkAPIService.getWalkInfo(apikey, "json", startIdx, endIdx);

            List<Map<String, Object>> row = (List<Map<String, Object>>) rDTO.IotVdata018().get("row");

            for (Map<String, Object> walkInfo : row) {

                String modelNm = CmmUtil.nvl((String) walkInfo.get("MODEL_NM")); // 모델 번호
                String serialNo = CmmUtil.nvl((String) walkInfo.get("SERIAL_NO")); // 시리얼 번호
                int len = serialNo.length();
                serialNo = serialNo.substring(len-4);
                String sensingTime = CmmUtil.nvl((String) walkInfo.get("SENSING_TIME")); // 측정 시간
                String visitorCount = CmmUtil.nvl((String) walkInfo.get("VISITOR_COUNT")); // 방문자 수

                log.info("modelNm : " + modelNm);
                log.info("serialNo : " + serialNo);
                log.info("sensingTime : " + sensingTime);
                log.info("visitorCount : " + visitorCount);

                // 데이터가 모두 수집되었다면 저장함
                if ((!modelNm.isEmpty()) && (!serialNo.isEmpty()) && (!sensingTime.isEmpty()) && (!visitorCount.isEmpty())) {

                    WalkDTO pDTO = WalkDTO.builder()
                            .modelNm(modelNm)
                            .serialNo(serialNo)
                            .sensingIme(sensingTime)
                            .visitorCount(visitorCount)
                            .build();

                    // 한번에 여러개의 데이터를 MongoDB에 저장할 List 형태의 데이터 저장하기
                    pList.add(pDTO);
                }
            }

            // 1000개씩 조회 가능이므로 다음 회차 횟수만큼 추가
            start += 1000;
            end += 1000;
        }

        log.info("최종 추가 데이터 수 : " + pList.size());

        // 초기화
        start = 1;
        end = 1000;

        log.info(this.getClass().getName() + ".doCollect End!");

        return pList;
    }

    @Override
    public int collectWalk() throws Exception {
        int res = 0;
        int dropRes = 0;

        // 생성할 컬렉션명
        String colNm = "SEOUL_WALK_INFO";

        dropRes = mongoMapper.dropCollection(colNm);

        if(dropRes==1) {
            log.info("mongoCollection 삭제 실패!");
        } else {
            log.info("mongoCollection 삭제 성공!");
        }

        // private 함수로 선언된 doCollect 함수를 호출하여 결과를 받기
        List<WalkDTO> rList = this.doCollect();

        // MongoDB에 데이터저장하기
        res = mongoMapper.insertData(rList, colNm);


        // 로그 찍기(추후 찍은 로그를 통해 이 함수에 접근했는지 파악하기 용이하다.)
        log.info(this.getClass().getName() + ".collectMelonSong End!");

        return res;
    }

    @Override
    public List<WalkDTO> getWalkList() throws Exception {
        return null;
    }
}
