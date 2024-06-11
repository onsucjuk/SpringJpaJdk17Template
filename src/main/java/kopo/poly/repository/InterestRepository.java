package kopo.poly.repository;

import kopo.poly.repository.entity.InterestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InterestRepository extends JpaRepository<InterestEntity, Long> {

    /**
     * 관심 업종 조회
     */
    List<InterestEntity> findByUserIdOrderByInterestSeqDesc(String userId);

    /**
     * 관심 업종 중복 데이터 확인
     */
    Optional<InterestEntity> findByUserIdAndIndutyNmAndSeoulLocationCd(String userId, String indutyNm, String seoulLocationCd);
}
