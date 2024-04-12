package kopo.poly.controller;

import kopo.poly.dto.SeoulSiMarketDTO;
import kopo.poly.service.ISiMarketService;
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

    @GetMapping(value = "index")
    public String goIndex(ModelMap model) throws Exception {

        log.info(this.getClass().getName() + ".html/index Start!");

        int rank = 3;

        List<SeoulSiMarketDTO> rList = siMarketService.getSiMarketRes(rank);

        model.addAttribute("rList", rList);

        log.info(this.getClass().getName() + ".html/index End!");

        return "html/index";

    }

}
