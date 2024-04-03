package kopo.poly.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import kopo.poly.dto.NoticeDTO;
import kopo.poly.repository.NoticeJoinRepository;
import kopo.poly.repository.NoticeSQLRepository;
import kopo.poly.repository.entity.NoticeEntity;
import kopo.poly.repository.entity.NoticeJoinEntity;
import kopo.poly.repository.entity.NoticeSQLEntity;
import kopo.poly.service.INoticeJoinService;
import kopo.poly.util.CmmUtil;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class NoticeJoinService implements INoticeJoinService {


    private final NoticeJoinRepository noticeJoinRepository;
    private final NoticeSQLRepository noticeSQLRepository;


    @Override
    public List<NoticeDTO> getNoticeListUsingJoinColumn() {

        log.info(this.getClass().getName() + ".getNoticeListUsingJoinColumn Start!");

        // 공지사항 전체 리스트 조회하기
        List<NoticeJoinEntity> rList = noticeJoinRepository.findAllByOrderByNoticeSeqDesc();

        List<NoticeDTO> list = new LinkedList<>();

        rList.forEach(rEntity -> {

            long noticeSeq = rEntity.getNoticeSeq();
            String noticeYn = CmmUtil.nvl(rEntity.getNoticeYn());
            String title = CmmUtil.nvl(rEntity.getTitle());
            long readCnt = rEntity.getReadCnt();
            String userName = CmmUtil.nvl(rEntity.getUserInfoEntity().getUserName());
            String regDt = CmmUtil.nvl(rEntity.getRegDt());

            log.info("noticeSeq : " + noticeSeq);
            log.info("noticeYn : " + noticeYn);
            log.info("title : " + title);
            log.info("readCnt : " + readCnt);
            log.info("userName : " + userName);
            log.info("regDt : " + regDt);
            log.info("-----------------------------");

            NoticeDTO rDTO = NoticeDTO.builder()
                    .noticeSeq(noticeSeq).noticeYn(noticeYn)
                    .title(title).readCnt(readCnt).userName(userName).regDt(regDt).build();

            list.add(rDTO);
        });

        log.info(this.getClass().getName() + ".getNoticeListUsingJoinColumn End!");

        return list;
    }

    @Override
    public List<NoticeDTO> getNoticeListUsingNativeQuery() {

        log.info(this.getClass().getName() + ".getNoticeListUsingNativeQuery Start!");

        List<NoticeSQLEntity> rList = noticeSQLRepository.getNoticeListUsingSQL();

        List<NoticeDTO> nList = new ObjectMapper().convertValue(rList,
                new TypeReference<List<NoticeDTO>>() {
                });

        log.info(this.getClass().getName() + ".getNoticeListUsingNativeQuery End!");

        return nList;
    }

    @Override
    public List<NoticeDTO> getNoticeListForQueryDSL() {
        return null;
    }

    @Override
    public NoticeDTO getNoticeInfoForQueryDSL(NoticeDTO pDTO, boolean type) throws Exception {
        return null;
    }
}
