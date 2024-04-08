package kopo.poly.repository;

import kopo.poly.repository.entity.CommentEntity;
import kopo.poly.repository.entity.NoticeEntity;
import kopo.poly.repository.entity.UserInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

    /*
     *  CrudRepository
     *  단순 CRUD 구현
     *
     *  JpaRepository
     *  조회할 때, 정렬, 페이징 기능 등 사용
     *  왠만해선 JpaRepository 사용 !!
     *
     *  JpaRepository<CommentEntity, Long>
     *  JpaRepository<엔티티명, PK타입>
     *
     */


    /**
     * 댓글 리스트
     *
     * @param noticeSeq 댓글 FK
     *
     */
    List<CommentEntity> findByNoticeSeqOrderByCommentSeqAsc(Long noticeSeq);

    /**
     * 댓글 리스트
     *
     * @param commentSeq 공지사항 PK
     */
    Optional<CommentEntity> findByCommentSeq(Long commentSeq);

    /**
     *
     * @Query 네이티브 쿼리 사용시 DB값은 변경되나 Entity값(캐시값)은 변경 x
     * @Modifying(clearAutomatically = true) 사용해서 객체 정보 초기화(Entity 정보 삭제) 후 캐시 갱신
     *
     */
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE USER_INFO A SET A.email = ?2, A.USER_NAME = ?3, A.ADDR1 = ?4, A.ADDR2 = ?5 WHERE A.USER_ID = ?1",
            nativeQuery = true)
    int updateBoardComments(String userId, String email, String userName, String addr1, String add2);

}
