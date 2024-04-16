package kopo.poly.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kopo.poly.dto.SeoulSiMarketDTO;
import kopo.poly.service.IGuMarketService;
import kopo.poly.service.ISiMarketService;
import kopo.poly.util.CmmUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@RequestMapping(value = "/seoul")
@RequiredArgsConstructor
@Controller
public class SeoulSiController {

    private final ISiMarketService siMarketService;
    private final IGuMarketService guMarketService;

    @GetMapping(value = "siAnalysis")
    public String test(HttpSession session, ModelMap model) throws Exception {

        String userId = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID"));

        log.info("userId : userId");

        List<SeoulSiMarketDTO> rSalesList;

        if (userId.length() > 0) {

            int rank = 10;
            // 전분기
            String preYear = "20232";
            // 이번분기
            String recYear = "20233";

            rSalesList = siMarketService.getSiMarketRes(rank, preYear, recYear);

        } else {

            return "user/login";

        }

        model.addAttribute("rSalesList", rSalesList);

        return "seoul/siAnalysis";
    }

    /**
     * 매출액 데이터 ajax로 넘겨주기
     */
    @ResponseBody
    @PostMapping(value = "siSalesList")
    public List<SeoulSiMarketDTO> siSalesList(HttpServletRequest request) throws Exception {

        log.info(this.getClass().getName() + ".siSalesList Start!");

        String seoulLocationCd = CmmUtil.nvl(request.getParameter("selectedValue"));

        //판별 길이
        int length = seoulLocationCd.length();

        log.info("seoulLocationCd : " + seoulLocationCd);

        List<SeoulSiMarketDTO> rSalesList = new ArrayList<>();

        int rank = 10;
        // 전분기
        String preYear = "20232";
        // 이번분기
        String recYear = "20233";

        if(length==2) { // 지역 이름이 서울 전체면 서울시 전체 데이터에서 가져오기 (서울 전체 value = 11)

            rSalesList = siMarketService.getSiMarketRes(rank, preYear, recYear);

        } else if (length==5) { // 아니면 구 데이터에서 가져오기 [만들 예정] (구 코드 길이 = 11211 5)

            rSalesList = guMarketService.getGuMarketRes(rank, preYear, recYear, seoulLocationCd);

        } else { // 동 아니면 구 [만들 예정]


        }

        log.info(this.getClass().getName() + ".siSalesList End!");

        return rSalesList;
    }

    /**
     * 점포 데이터 ajax로 넘겨주기
     */
    @ResponseBody
    @PostMapping(value = "siStoreList")
    public List<SeoulSiMarketDTO> siStoreList(HttpServletRequest request) throws Exception {

        log.info(this.getClass().getName() + ".siStoreList Start!");

        String seoulLocationCd = CmmUtil.nvl(request.getParameter("selectedValue"));

        log.info("seoulLocationCd : " + seoulLocationCd);

        List<SeoulSiMarketDTO> rStoreList = new ArrayList<>();

        int rank = 10;
        // 전분기
        String preYear = "20232";
        // 이번분기
        String recYear = "20233";
        //판별 길이
        int length = seoulLocationCd.length();

        if(length==2) { // 지역 이름이 서울 전체면 서울시 전체 데이터에서 가져오기 (서울 전체 value = 11)

            rStoreList = siMarketService.getSiStoreRes(rank, preYear, recYear);

        } else if (length==5) {  // 아니면 구 데이터에서 가져오기 [만들 예정] (구 코드 길이 = 11211 5)

            rStoreList = guMarketService.getGuStoreRes(rank, preYear, recYear, seoulLocationCd);

        } else { // 아니면 동 데이터에서 가져오기 [만들 예정]



        }

        log.info(this.getClass().getName() + ".siStoreList End!");

        return rStoreList;
    }

    /**
     * 점포 폐업 데이터 ajax로 넘겨주기
     */
    @ResponseBody
    @PostMapping(value = "siStoreCloseList")
    public List<SeoulSiMarketDTO> siStoreCloseList(HttpServletRequest request) throws Exception {

        log.info(this.getClass().getName() + ".siStoreCloseList Start!");

        String seoulLocationCd = CmmUtil.nvl(request.getParameter("selectedValue"));

        log.info("seoulLocationCd : " + seoulLocationCd);

        List<SeoulSiMarketDTO> rStoreCloseList = new ArrayList<>();

        int rank = 10;
        // 전분기
        String preYear = "20232";
        // 이번분기
        String recYear = "20233";
        //판별 길이
        int length = seoulLocationCd.length();

        if(length==2) { // 지역 이름이 서울 전체면 서울시 전체 데이터에서 가져오기 (서울 전체 value = 11)

            rStoreCloseList = siMarketService.getSiStoreCloseRes(rank, preYear, recYear);

        } else if (length==5) {  // 아니면 구 데이터에서 가져오기 [만들 예정] (구 코드 길이 = 11211 5)

            rStoreCloseList = guMarketService.getGuCloseStoreRes(rank, preYear, recYear, seoulLocationCd);

        } else { // 아니면 동 데이터에서 가져오기 [만들 예정]

        }

        log.info(this.getClass().getName() + ".siStoreCloseList End!");

        return rStoreCloseList;
    }

}
