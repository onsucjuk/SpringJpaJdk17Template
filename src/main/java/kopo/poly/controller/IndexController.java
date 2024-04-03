package kopo.poly.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@RequestMapping(value = "/html")
@Controller
public class IndexController {

    @GetMapping(value = "index")
    public String goIndex() {

        log.info(this.getClass().getName() + ".html/index Start!");

        log.info(this.getClass().getName() + ".html/index End!");

        return "html/index";

    }

}
