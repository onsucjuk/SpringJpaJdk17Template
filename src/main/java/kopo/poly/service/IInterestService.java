package kopo.poly.service;

import kopo.poly.dto.InterestDTO;
import kopo.poly.dto.UserInfoDTO;
import kopo.poly.repository.entity.InterestEntity;

import java.util.List;

public interface IInterestService {

    /**
     *  즐겨찾기 정보 가져오기
     *
     * @param pDTO 즐겨찾기 가져오기 위한 정보(userId)
     *
     * @return 조회 결과
     *
     */
    List<InterestDTO> getInterestList(InterestDTO pDTO);

    /**
     *  즐겨찾기 상세 정보가져오기
     *
     * @param pDTO 즐겨찾기 상세 가져오기 위한 정보
     *
     * @return 조회 결과
     *
     */
    InterestDTO getInterestInfo(InterestDTO pDTO);

    /**
     * 해당 관심정보 수정하기
     *
     * @param pDTO 즐겨찾기 수정하기 위한 정보
     *
     */
    void updateInterestInfo(InterestDTO pDTO);

    /**
     * 해당 관심정보 삭제하기
     *
     * @param pDTO 즐겨찾기 삭제하기 위한 정보
     *
     */
    void deleteInterestInfo(InterestDTO pDTO);

    /**
     * 해당 관심정보 저장하기
     *
     * @param pDTO 즐겨찾기 저장하기 위한 정보
     *
     */
    void insertInterestInfo(InterestDTO pDTO);

    /**
     * 관심정보 저장 전에 등록여부 확인하기
     */
    InterestDTO getInterestDataExists(InterestDTO pDTO);

}
