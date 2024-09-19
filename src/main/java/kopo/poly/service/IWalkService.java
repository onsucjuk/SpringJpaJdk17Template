package kopo.poly.service;

import kopo.poly.dto.WalkDTO;

import java.util.List;

public interface IWalkService {

    /**
     * 유동 인구 저장하기
     */
    int collectWalk() throws Exception;

    /**
     * 유동 인구 가져오기
     */
    List<WalkDTO> getWalkList() throws Exception;

}
