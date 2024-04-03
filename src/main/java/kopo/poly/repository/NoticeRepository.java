package kopo.poly.repository;

import kopo.poly.repository.entity.NoticeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface NoticeRepository extends JpaRepository<NoticeEntity, Long> {

    /*
     *  CrudRepository
     *  단순 CRUD 구현
     *
     *  JpaRepository
     *  조회할 때, 정렬, 페이징 기능 등 사용
     *  왠만해선 JpaRepository 사용 !!
     *
     *  JpaRepository<NoticeEntity, Long>
     *  JpaRepository<엔티티명, PK타입>
     *
     */


    /**
     * 공지사항 리스트
     */
    List<NoticeEntity> findAllByOrderByNoticeSeqDesc();

    /**
     * 공지사항 리스트
     *
     * @param noticeSeq 공지사항 PK
     */
    NoticeEntity findByNoticeSeq(Long noticeSeq);

    /**
     * 공지사항 상세 보기할 때, 조회수 증가하기
     *
     * @param noticeSeq 공지사항 PK
     */

    /**
     *
     * @Query 네이티브 쿼리 사용시 DB값은 변경되나 Entity값(캐시값)은 변경 x
     * @Modifying(clearAutomatically = true) 사용해서 객체 정보 초기화(Entity 정보 삭제) 후 캐시 갱신
     *
     */
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE NOTICE A SET A.READ_CNT = IFNULL(A.READ_CNT, 0) + 1 WHERE A.NOTICE_SEQ = ?1",
            nativeQuery = true)
    int updateReadCnt(Long noticeSeq);

}
