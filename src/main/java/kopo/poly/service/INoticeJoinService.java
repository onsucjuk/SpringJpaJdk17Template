package kopo.poly.service;

import kopo.poly.dto.NoticeDTO;

import java.util.List;

public interface INoticeJoinService {

    List<NoticeDTO> getNoticeListUsingJoinColumn();

    /**
     * NativeQuery 사용하여 공지사항 전체 가져오기
     */
    List<NoticeDTO> getNoticeListUsingNativeQuery();

    /**
     *  QueryDSL Fetch Join
     */

    List<NoticeDTO> getNoticeListForQueryDSL();

    NoticeDTO getNoticeInfoForQueryDSL(NoticeDTO pDTO, boolean type) throws Exception;

}
