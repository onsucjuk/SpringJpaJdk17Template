package kopo.poly.controller;

import jakarta.servlet.http.HttpSession;
import kopo.poly.dto.NoticeDTO;
import kopo.poly.service.INoticeJoinService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequestMapping(value = "/notice")
@RequiredArgsConstructor
@Controller
public class NoticeJoinController {

    private final INoticeJoinService noticeJoinService;

    @GetMapping(value = "noticeListUsingJoinColumn")
    public String noticeListUsingJoinColumn(HttpSession session, ModelMap model)
        throws Exception {

        log.info(this.getClass().getName() + ".noticeListUsingJoinColumn Start!");

        /**
         *
         * USER_INFO 테이블에 아이디 정보가 없으니까 임시로 "USER01" 세팅
         *
         */
        session.setAttribute("SESSION_USER_ID", "USER01");

        List<NoticeDTO> rList = Optional.ofNullable(noticeJoinService.getNoticeListUsingJoinColumn())
                .orElseGet(ArrayList::new);

        model.addAttribute("rList", rList);

        log.info(this.getClass().getName() + ".noticeListUsingJoinColumn End!");

        return "notice/noticeListJoin";
    }

    @GetMapping(value = "noticeListUsingNativeQuery")
    public String noticeListUsingNativeQuery(HttpSession session, ModelMap model)
            throws Exception {

        log.info(this.getClass().getName() + ".noticeListUsingNativeQuery Start!");

        /**
         *
         * USER_INFO 테이블에 아이디 정보가 없으니까 임시로 "USER01" 세팅
         *
         */
        session.setAttribute("SESSION_USER_ID", "USER01");

        List<NoticeDTO> rList = Optional.ofNullable(noticeJoinService.getNoticeListUsingNativeQuery())
                .orElseGet(ArrayList::new);

        model.addAttribute("rList", rList);

        log.info(this.getClass().getName() + ".noticeListUsingNativeQuery End!");

        return "notice/noticeListJoin";
    }

}
