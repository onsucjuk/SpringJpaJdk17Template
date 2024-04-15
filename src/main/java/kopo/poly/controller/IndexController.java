package kopo.poly.controller;

import kopo.poly.dto.SeoulSiMarketDTO;
import kopo.poly.dto.YoutubeDTO;
import kopo.poly.service.ISiMarketService;
import kopo.poly.service.IYoutubeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@RequestMapping(value = "/html")
@RequiredArgsConstructor
@Controller
public class IndexController {

    private final ISiMarketService siMarketService;
    private final IYoutubeService youtubeService;

    @GetMapping(value = "index")
    public String goIndex(ModelMap model) throws Exception {

        log.info(this.getClass().getName() + ".html/index Start!");

        int rank = 3;
    // 전분기
        String preYear = "20232";
        // 이번분기
        String recYear = "20233";

        List<SeoulSiMarketDTO> rList = siMarketService.getSiMarketRes(rank, preYear, recYear);
        List<YoutubeDTO> yList = youtubeService.getYoutueInfo();

        model.addAttribute("rList", rList);
        model.addAttribute("yList", yList);

        log.info(this.getClass().getName() + ".html/index End!");

        return "html/index";

    }

}
