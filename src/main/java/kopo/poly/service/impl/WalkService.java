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
        String yesterday = "";

        while(end < 15000) {

            String startIdx = String.valueOf(start);
            String endIdx = String.valueOf(end);

            WalkDTO rDTO = walkAPIService.getWalkInfo(apikey, "json", startIdx, endIdx);

            List<Map<String, Object>> row = (List<Map<String, Object>>) rDTO.IotVdata018().get("row");

            if(start==1){
                Map<String, Object> firstWalkInfo = row.get(0); // 첫 번째 항목 가져오기
                yesterday = CmmUtil.nvl((String) firstWalkInfo.get("SENSING_TIME")).substring(5,10); // 측정 시간 가져오기
            }

            for (Map<String, Object> walkInfo : row) {

                String modelNm = CmmUtil.nvl((String) walkInfo.get("MODEL_NM")); // 모델 번호
                String serialNo = CmmUtil.nvl((String) walkInfo.get("SERIAL_NO")); // 시리얼 번호
                int len = serialNo.length();
                serialNo = serialNo.substring(len-4);
                String sensingTime = CmmUtil.nvl((String) walkInfo.get("SENSING_TIME")); // 측정 시간
                String visitorCount = CmmUtil.nvl((String) walkInfo.get("VISITOR_COUNT")); // 방문자 수

                String tempTime = sensingTime.substring(5,10);

                log.info("yesterday : " + yesterday);
                log.info("tempTime : " + tempTime);
                log.info("modelNm : " + modelNm);
                log.info("serialNo : " + serialNo);
                log.info("sensingTime : " + sensingTime);
                log.info("visitorCount : " + visitorCount);

                // 어제 날짜와 같은지 확인
                boolean check = yesterday.equals(tempTime);

                // 어제와 날짜가 같다면 저장, 이외의 경우 데이터가 모두 수집되었다면 저장함
                if ((!modelNm.isEmpty()) && (!serialNo.isEmpty()) && (!sensingTime.isEmpty()) && (!visitorCount.isEmpty()) && check) {

                    WalkDTO pDTO = WalkDTO.builder()
                            .modelNm(modelNm)
                            .serialNo(serialNo)
                            .sensingTime(sensingTime)
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

    /**
     *  서울열린데이터 - 유동 인구 API의 데이터 수집해서 MongoDB에 저장
     **/
    @Override
    public int collectWalk() throws Exception {

        log.info(this.getClass().getName() + ".collectWalk Start!");

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
        log.info(this.getClass().getName() + ".collectWalk End!");

        return res;
    }

    @Override
    public List<WalkDTO> getWalkList() throws Exception {

        log.info(this.getClass().getName() + ".getWalkList Start!");

        List<WalkDTO> rList = mongoMapper.getWalkList();

        log.info(this.getClass().getName() + ".getWalkList End!");

        return rList;
    }

    @Override
    public WalkDTO getWalkInfoList(WalkDTO pDTO) throws Exception {

        String serialNo = CmmUtil.nvl(pDTO.serialNo());

        List<WalkDTO> rList = mongoMapper.getWalkInfoList(serialNo);
        long[] tempTimeCount = new long[24];

        for(WalkDTO tempDTO : rList) {
            int time = Integer.parseInt(tempDTO.sensingTime().substring(11,13));
            int count = Integer.parseInt(tempDTO.visitorCount());
            // 시간과 일차하는 리스트의 index의 값(방문자수) 비교 후 최대값으로 대체
            if(tempTimeCount[time] < count) {
                tempTimeCount[time] = count;
            }
            log.info("시간 : " + time);
            log.info("유동 인구 : " + count);
        }

        WalkDTO rDTO = WalkDTO.builder()
                .timeVisitor(tempTimeCount)
                .build();

        return rDTO;
    }
}
