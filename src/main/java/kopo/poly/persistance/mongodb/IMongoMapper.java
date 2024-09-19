package kopo.poly.persistance.mongodb;

import kopo.poly.dto.WalkDTO;

import java.util.List;

public interface IMongoMapper {

    /**
     * 간단한 데이터 저장하기
     *
     * @param pList 저장될 정보
     * @param colNm 저장할 컬렉션 이름
     * @return 저장 결과
     */

    int insertData(List<WalkDTO> pList, String colNm);

    /**
     * 컬렉션 삭제하기
     *
     * @param colNm 조회할 컬렉션 이름
     * @return 저장 결과
     */

    int dropCollection(String colNm);

}
