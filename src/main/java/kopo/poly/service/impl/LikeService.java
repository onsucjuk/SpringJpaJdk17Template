package kopo.poly.service.impl;

import kopo.poly.dto.LikeDTO;
import kopo.poly.repository.LikeReopsitory;
import kopo.poly.repository.entity.LikeEntity;
import kopo.poly.repository.entity.LikePK;
import kopo.poly.service.ILikeService;
import kopo.poly.util.CmmUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class LikeService implements ILikeService {

    private final LikeReopsitory likeReopsitory;
    @Override
    public LikeDTO likeExists(LikeDTO pDTO) {

        log.info(this.getClass().getName() + ".likeExists Start!");

        LikeDTO rDTO;

        String userId = CmmUtil.nvl(pDTO.userId());
        long noticeSeq = pDTO.noticeSeq();

        log.info("userId : " + userId);
        log.info("noticeSeq : " + noticeSeq);

        LikePK likePK = LikePK.builder()
                .noticeSeq(noticeSeq)
                .userId(userId)
                .build();

        Optional<LikeEntity> rEntity = likeReopsitory.findById(likePK);

        if (rEntity.isPresent()) {
            rDTO = LikeDTO.builder().existsYn("Y").build();
        } else {
            rDTO = LikeDTO.builder().existsYn("N").build();
        }

        log.info(this.getClass().getName() + ".likeExists End!");

        return rDTO;
    }

    @Override
    public void insertLike(LikeDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".insertLike Start!");

        String userId = CmmUtil.nvl(pDTO.userId());
        Long noticeSeq = pDTO.noticeSeq();

        log.info("userId : " + userId);
        log.info("noticeSeq : " + noticeSeq);

        LikeEntity pEntity = LikeEntity.builder()
                .noticeSeq(noticeSeq)
                .userId(userId)
                .build();

        likeReopsitory.save(pEntity);

        log.info(this.getClass().getName() + ".insertLike End!");

    }

    @Override
    public void deleteLike(LikeDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".deleteLike Start!");

        String userId = CmmUtil.nvl(pDTO.userId());
        Long noticeSeq = pDTO.noticeSeq();

        log.info("userId : " + userId);
        log.info("noticeSeq : " + noticeSeq);

        LikePK likePK = LikePK.builder()
                .noticeSeq(noticeSeq)
                .userId(userId)
                .build();

        // 좋아요 삭제하기
        likeReopsitory.deleteById(likePK);

        log.info(this.getClass().getName() + ".deleteLike End!");

    }


}
