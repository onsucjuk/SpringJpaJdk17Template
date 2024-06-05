package kopo.poly.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kopo.poly.dto.SeoulSiMarketDTO;
import kopo.poly.service.IDongMarketService;
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
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;


@Slf4j
@RequestMapping(value = "/seoul")
@RequiredArgsConstructor
@Controller
public class SeoulSiController {

    private final ISiMarketService siMarketService;
    private final IGuMarketService guMarketService;
    private final IDongMarketService dongMarketService;

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

            return "redirect:/user/login";

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

        if (length == 2) { // 지역 이름이 서울 전체면 서울시 전체 데이터에서 가져오기 (서울 전체 value = 11)

            rSalesList = siMarketService.getSiMarketRes(rank, preYear, recYear);

        } else if (length == 5) { // 아니면 구 데이터에서 가져오기 [만들 예정] (구 코드 길이 = 11211 5)

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

        if (length == 2) { // 지역 이름이 서울 전체면 서울시 전체 데이터에서 가져오기 (서울 전체 value = 11)

            rStoreList = siMarketService.getSiStoreRes(rank, preYear, recYear);

        } else if (length == 5) {  // 아니면 구 데이터에서 가져오기 [만들 예정] (구 코드 길이 = 11211 5)

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

        if (length == 2) { // 지역 이름이 서울 전체면 서울시 전체 데이터에서 가져오기 (서울 전체 value = 11)

            rStoreCloseList = siMarketService.getSiStoreCloseRes(rank, preYear, recYear);

        } else if (length == 5) {  // 아니면 구 데이터에서 가져오기 [만들 예정] (구 코드 길이 = 11211 5)

            rStoreCloseList = guMarketService.getGuCloseStoreRes(rank, preYear, recYear, seoulLocationCd);

        } else { // 아니면 동 데이터에서 가져오기 [만들 예정]

        }

        log.info(this.getClass().getName() + ".siStoreCloseList End!");

        return rStoreCloseList;
    }

    /**
     * 위치 좌표 ajax로 넘겨주기
     */
    @ResponseBody
    @PostMapping(value = "getLatLon")
    public SeoulSiMarketDTO getLatLon(HttpServletRequest request) throws Exception {

        log.info(this.getClass().getName() + ".getLatLon Start!");

        String seoulLocationCd = CmmUtil.nvl(request.getParameter("selectedValue"));

        log.info("seoulLocationCd : " + seoulLocationCd);

        SeoulSiMarketDTO rDTO = SeoulSiMarketDTO.builder().build();

        //판별 길이
        int length = seoulLocationCd.length();

        if (length == 5) {  // 아니면 구 데이터에서 가져오기 (구 코드 길이 = 11211 5)

            rDTO = guMarketService.getGuLatLon(seoulLocationCd);

        } else { // 아니면 동 데이터에서 가져오기 [만들 예정]

        }

        log.info(this.getClass().getName() + ".getLatLon End!");

        return rDTO;
    }


    /**
     * ###################################################################################################
     *
     *
     *                                  구 기준 업종 분석 페이지 관련 컨트롤러
     *
     *
     * ###################################################################################################
     * */

    /**
     * 매출액 데이터 ajax로 넘겨주기
     */
    @ResponseBody
    @PostMapping(value = "guSalesList")
    public List<SeoulSiMarketDTO> guSalesList(HttpServletRequest request) throws Exception {

        log.info(this.getClass().getName() + ".guSalesList Start!");

        String seoulLocationCd = CmmUtil.nvl(request.getParameter("selectedValue"));
        String indutySort = CmmUtil.nvl(request.getParameter("indutySortValue"));
        String indutyName = CmmUtil.nvl(request.getParameter("indutyNameValue"));

        log.info("seoulLocationCd : " + seoulLocationCd);
        log.info("indutySort : " + indutySort);
        log.info("indutyName : " + indutyName);

        if (indutySort.equals("전체")) {

            indutySort = "";

        } else if (indutySort.equals("외식업")) {

            indutySort = "CS1";

        } else if (indutySort.equals("서비스업")) {

            indutySort = "CS2";

        } else if (indutySort.equals("소매업")) {

            indutySort = "CS3";

        }

        log.info("Changed indutySort : " + indutySort);

        List<SeoulSiMarketDTO> rSalesList = new ArrayList<>();

        int rank = 10;
        // 전분기
        String preYear = "20232";
        // 이번분기
        String recYear = "20233";

        rSalesList = dongMarketService.getDongMarketRes(rank, preYear, recYear, seoulLocationCd, indutySort, indutyName);

        log.info(this.getClass().getName() + ".guSalesList End!");

        return rSalesList;
    }

    /**
     * 점포 데이터 ajax로 넘겨주기
     */
    @ResponseBody
    @PostMapping(value = "guStoreList")
    public List<SeoulSiMarketDTO> guStoreList(HttpServletRequest request) throws Exception {

        log.info(this.getClass().getName() + ".siStoreList Start!");

        String seoulLocationCd = CmmUtil.nvl(request.getParameter("selectedValue"));
        String indutySort = CmmUtil.nvl(request.getParameter("indutySortValue"));
        String indutyName = CmmUtil.nvl(request.getParameter("indutyNameValue"));

        log.info("seoulLocationCd : " + seoulLocationCd);
        log.info("indutySort : " + indutySort);
        log.info("indutyName : " + indutyName);

        if (indutySort.equals("전체")) {

            indutySort = "";

        } else if (indutySort.equals("외식업")) {

            indutySort = "CS1";

        } else if (indutySort.equals("서비스업")) {

            indutySort = "CS2";

        } else if (indutySort.equals("소매업")) {

            indutySort = "CS3";

        }

        log.info("Changed indutySort : " + indutySort);

        List<SeoulSiMarketDTO> rStoreList = new ArrayList<>();

        int rank = 10;
        // 전분기
        String preYear = "20232";
        // 이번분기
        String recYear = "20233";
        //판별 길이

        rStoreList = dongMarketService.getDongStoreRes(rank, preYear, recYear, seoulLocationCd, indutySort, indutyName);

        log.info(this.getClass().getName() + ".guStoreList End!");

        return rStoreList;
    }

    /**
     * 점포 데이터 ajax로 넘겨주기
     */
    @ResponseBody
    @PostMapping(value = "guCustomerList")
    public List<SeoulSiMarketDTO> guCustomerList(HttpServletRequest request) throws Exception {

        log.info(this.getClass().getName() + ".guCustomerList Start!");

        String seoulLocationCd = CmmUtil.nvl(request.getParameter("selectedValue"));
        String indutySort = CmmUtil.nvl(request.getParameter("indutySortValue"));
        String indutyName = CmmUtil.nvl(request.getParameter("indutyNameValue"));

        log.info("seoulLocationCd : " + seoulLocationCd);
        log.info("indutySort : " + indutySort);
        log.info("indutyName : " + indutyName);

        if (indutySort.equals("전체")) {

            indutySort = "";

        } else if (indutySort.equals("외식업")) {

            indutySort = "CS1";

        } else if (indutySort.equals("서비스업")) {

            indutySort = "CS2";

        } else if (indutySort.equals("소매업")) {

            indutySort = "CS3";

        }

        log.info("Changed indutySort : " + indutySort);

        List<SeoulSiMarketDTO> rStoreList = new ArrayList<>();

        // 이번분기
        String recYear = "20233";
        //판별 길이

        rStoreList = dongMarketService.getDongCustomerRes(recYear, seoulLocationCd, indutySort, indutyName);

        log.info(this.getClass().getName() + ".guCustomerList End!");

        return rStoreList;
    }

    /**
     ############################################################################

     종합 분석

     ############################################################################
     */

    @GetMapping(value = "totalAnalysis")
    public String totalAnalysis(HttpServletRequest request, ModelMap model, HttpSession session) throws Exception {

        log.info(this.getClass().getName() + ".totalAnalysis Start!");

        String userId =  CmmUtil.nvl((String) session.getAttribute("SS_USER_ID"));

        if(userId.length() > 0) {

            List<SeoulSiMarketDTO> seoulList = new ArrayList<>();
            List<SeoulSiMarketDTO> guList = new ArrayList<>();
            List<SeoulSiMarketDTO> seoulIndutyList = new ArrayList<>();
            List<SeoulSiMarketDTO> sortedList = new LinkedList<>();

            String guSelect = CmmUtil.nvl(request.getParameter("guSelect")); // 지역코드
            String guName = CmmUtil.nvl(request.getParameter("guName")); // 지역명
            String induty = CmmUtil.nvl(request.getParameter("induty")); // 선택 업종


            log.info("guSelect : " + guSelect);
            log.info("induty : " + induty);

            SeoulSiMarketDTO pDTO = SeoulSiMarketDTO.builder()
                    .seoulLocationNm(guName)
                    .indutyNm(induty)
                    .build();

            int sort = 0;

            if (induty.equals("외식업")) {

                induty = "CS1";

            } else if (induty.equals("서비스업")) {

                induty = "CS2";

            } else if (induty.equals("소매업")) {

                induty = "CS3";

            } else { // 나머지의 경우 indutyName으로 검색

                sort = 1;

            }

            if (sort == 0) { // 업종 대분류 검색

                // 서울 전체
                seoulList = siMarketService.getSeoulMarketLikeIndutyCd(induty);
                // 구
                guList = guMarketService.getGuMarketLikeIndutyCd(induty, guSelect);
                // 동
                seoulIndutyList = guMarketService.getIndutyMarket();
                // 지역 매출 비중
                sortedList = guMarketService.getSortedMarketByIndutyCd(induty);

            } else { // 업종 소분류

                // 서울 전체
                seoulList = siMarketService.getSeoulMarketByIndutyNm(induty);
                // 구
                guList = guMarketService.getGuMarketIndutyNm(induty, guSelect);
                // 동
                seoulIndutyList = guMarketService.getIndutyMarket();
                // 지역 매출 비중
                sortedList = guMarketService.getSortedMarketByIndutyNm(induty);

            }

            model.addAttribute("seoulList", seoulList);
            model.addAttribute("guList", guList);
            model.addAttribute("seoulIndutyList", seoulIndutyList);
            model.addAttribute("sortedList", sortedList);
            model.addAttribute("pDTO", pDTO);

            sort = 0; // 초기화

        } else { // 아이디가 없으면 로그인 창으로 이동

            return "redirect:/user/login";
        }

        return "seoul/totalAnalysis";

    }


    /**
     ############################################################################

                                창업 지역 분석

     ############################################################################
     */

    @GetMapping(value = "locationAnalysis")
    public String locationAnalysis(HttpServletRequest request, ModelMap model, HttpSession session) throws Exception {

        log.info(this.getClass().getName() + ".locationAnalysis Start!");

        String userId =  CmmUtil.nvl((String) session.getAttribute("SS_USER_ID"));

        if(userId.length() > 0) {

/*            List<SeoulSiMarketDTO> seoulIndutyList = new ArrayList<>();
            List<SeoulSiMarketDTO> sortedList = new LinkedList<>();*/

            String indutyNm = CmmUtil.nvl(request.getParameter("indutyNm")); // 선택 업종
            String ageSales = CmmUtil.nvl(request.getParameter("ageSales")); // 주 고객층 나이대
            String genderSales = CmmUtil.nvl(request.getParameter("genderSales")); // 주 고객층 성별
            String timeSales = CmmUtil.nvl(request.getParameter("timeSales")); // 피크타임


            log.info("선택 업종 : " + indutyNm);
            log.info("주 고객층 나이대 : " + ageSales);
            log.info("주 고객층 성별 : " + genderSales);
            log.info("피크 타임 : " + timeSales);

            SeoulSiMarketDTO pDTO = SeoulSiMarketDTO.builder()
                    .indutyNm(indutyNm)
                    .maxAge(ageSales)
                    .maxGender(genderSales)
                    .maxTime(timeSales)
                    .build();

            List<SeoulSiMarketDTO> salesRateList = Optional.ofNullable(dongMarketService.getDongMarketRes(3, "20232", "20233", "11", "", indutyNm))
                    .orElseGet(ArrayList::new);

            List<List<SeoulSiMarketDTO>> rList = Optional.ofNullable(dongMarketService.getLocationMarketRes(pDTO))
                    .orElseGet(ArrayList::new);


            model.addAttribute("pDTO", pDTO);
            model.addAttribute("salesList", rList.get(0));
            model.addAttribute("storeList", rList.get(1));
            model.addAttribute("ageList", rList.get(2));
            model.addAttribute("genderList", rList.get(3));
            model.addAttribute("timeList", rList.get(4));
            model.addAttribute("salesRateList", salesRateList);

        } else { // 아이디가 없으면 로그인 창으로 이동

            return "redirect:/user/login";
        }

        return "seoul/locationAnalysis";

    }

}