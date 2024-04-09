package kopo.poly.controller;

import jakarta.servlet.http.HttpServletRequest;
import kopo.poly.dto.SeoulSiMarketDTO;
import kopo.poly.service.ISiMarketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Slf4j
@RequestMapping(value = "/seoul")
@RequiredArgsConstructor
@Controller
public class SeoulSiController {

    private final ISiMarketService siMarketService;

    @GetMapping(value = "test")
    public String test(HttpServletRequest request, ModelMap model) throws Exception {

        SeoulSiMarketDTO rDTO = siMarketService.getSiMarketRes();

        model.addAttribute("rDTO", rDTO);

        return "market/test";
    }

}
