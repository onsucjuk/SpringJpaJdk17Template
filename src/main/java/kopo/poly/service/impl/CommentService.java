package kopo.poly.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import kopo.poly.dto.CommentDTO;
import kopo.poly.repository.CommentRepository;
import kopo.poly.repository.entity.CommentEntity;
import kopo.poly.repository.entity.UserInfoEntity;
import kopo.poly.service.ICommentService;
import kopo.poly.util.CmmUtil;
import kopo.poly.util.DateUtil;
import kopo.poly.util.EncryptUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class CommentService implements ICommentService {

    private final CommentRepository commentRepository;


    @Override
    public List<CommentDTO> getCommentList(CommentDTO pDTO) {

        log.info(this.getClass().getName() + ".getCommentList Start!");

        Long noticeSeq = pDTO.noticeSeq();

        log.info("noticeSeq : " + noticeSeq);

        List<CommentEntity> rList = commentRepository.findByNoticeSeqOrderByCommentSeqAsc(noticeSeq);

        List<CommentDTO> nList = new ObjectMapper().convertValue(rList,
                new TypeReference<>() {
                });

        log.info(this.getClass().getName() + ".getCommentList End!");

        return nList;
    }

    @Override
    public int updateComment(CommentDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".updateUserInfo Start!");

        Long commentSeq = pDTO.commentSeq();

        int res = 0;

        Optional<CommentEntity> rEntity = commentRepository.findByCommentSeq(commentSeq);

        if(rEntity.isPresent()){

            String userId = pDTO.userId();
            /*String userName = pDTO.userName();
            String email = EncryptUtil.encAES128CBC(pDTO.email());
            String addr1 = pDTO.addr1();
            String addr2 = pDTO.addr2();

            log.info("userId : " + userId);
            log.info("userName : " + userName);
            log.info("email : " + email);
            log.info("addr1 : " + addr1);
            log.info("addr2 : " + addr2);

            // 회원정보 DB에 저장
            commentRepository.updateBoardComments(userId, email,userName,addr1, addr2);*/

            res = 1;

        }

        log.info(this.getClass().getName() + ".updateUserInfo END!");

        return res;


    }

    @Override
    public void deleteComment(CommentDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".deleteComment Start!");

        Long commentSeq = pDTO.commentSeq();

        log.info("noticeSeq : " + commentSeq);

        // 데이터 수정하기
        commentRepository.deleteById(pDTO.commentSeq());

        log.info(this.getClass().getName() + ".deleteComment End!");

    }

    @Override
    public void insertComment(CommentDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".insertComment Start!");

        String userId = CmmUtil.nvl(pDTO.userId());
        String contents = CmmUtil.nvl(pDTO.commentContents());
        Long noticeSeq = pDTO.noticeSeq();

        log.info("userId : " + userId);
        log.info("contents : " + contents);
        log.info("noticeSeq : " + noticeSeq);

        // 공지사항 저장을 위해서는 PK 값은 빌더에 추가하지 않는다.
        // JPA에 자동 증가 설정을 해놨음
        CommentEntity pEntity = CommentEntity.builder()
                .noticeSeq(noticeSeq)
                .userId(userId)
                .commentContents(contents)
                .commentRegId(userId)
                .commentRegDt(DateUtil.getDateTime("yyyy-MM-dd hh:mm:ss"))
                .commentChgId(userId)
                .commentChgDt(DateUtil.getDateTime("yyyy-MM-dd hh:mm:ss"))
                .build();

        // 공지사항 저장하기
        commentRepository.save(pEntity);

        log.info(this.getClass().getName() + ".insertComment End!");

    }

}
