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
@RequestMapping(value = "/seoul")
@RequiredArgsConstructor
@Controller
public class SeoulSiController {

    private final ISiMarketService siMarketService;

    @GetMapping(value = "test")
    public String test(ModelMap model) throws Exception {

        int rank = 10;

        List<SeoulSiMarketDTO> rList = siMarketService.getSiMarketRes(rank);

        model.addAttribute("rList", rList);

        return "market/test";
    }

}
