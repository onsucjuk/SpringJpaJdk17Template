package kopo.poly.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kopo.poly.dto.InterestDTO;
import kopo.poly.dto.MsgDTO;
import kopo.poly.dto.SeoulSiMarketDTO;
import kopo.poly.dto.UserInfoDTO;
import kopo.poly.service.IGuMarketService;
import kopo.poly.service.IInterestService;
import kopo.poly.service.IUserInfoService;
import kopo.poly.util.CmmUtil;
import kopo.poly.util.EncryptUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequestMapping(value = "/user")
@RequiredArgsConstructor
@Controller
public class UserInfoController {

    // @RequiredArgsConstructor 를 통해 메모리에 올라간 서비스 객체를 Controller에서 사용할 수 있게 주입함

    private final IUserInfoService userInfoService;
    private final IInterestService iInterestService;
    private final IGuMarketService guMarketService;

    @GetMapping(value = "searchUserId")
    public String searchUserId() {

        log.info(this.getClass().getName() + ".user/searchUserId Start!");

        log.info(this.getClass().getName() + ".user/searchUserId End!");

        return "user/searchUserId";
    }

    @GetMapping(value = "searchPassword")
    public String searchPassword() {

        log.info(this.getClass().getName() + ".user/searchPassword Start!");

        log.info(this.getClass().getName() + ".user/searchPassword End!");

        return "user/searchPassword";
    }

    @GetMapping(value = "newPasswordResult")
    public String newPasswordResult() {

        log.info(this.getClass().getName() + ".user/newPasswordResult Start!");

        log.info(this.getClass().getName() + ".user/newPasswordResult End!");

        return "user/newPasswordResult";
    }

    @PostMapping(value = "newPassword")
    public String searchPasswordProc(HttpServletRequest request, HttpSession session) throws Exception {
        log.info(this.getClass().getName() + ".user/newPassword Start!");

        String userId = CmmUtil.nvl((request.getParameter("userId"))); // 아이디
        String userName = CmmUtil.nvl((request.getParameter("userName"))); // 이름
        String email = CmmUtil.nvl((request.getParameter("email"))); // 이메일

        log.info("userId : " + userId);
        log.info("userName : " + userName);
        log.info("email : " + email);

        UserInfoDTO pDTO = UserInfoDTO.builder()
                .userId(userId)
                .userName(userName)
                .email(EncryptUtil.encAES128CBC(email))
                .build();

        // 비밀번호 찾기 가능한지 확인하기
        int res = userInfoService.searchUserIdOrPasswordPro(pDTO);

        // 비밀번호 재생성 화면은 보안을 위해 반드시 NEW_PASSWORD 세션이 존재해야 접속 가능하도록 구현
        // userId 값을 넣은 이유는 비밀번호 재설정하는 newPasswordProc 함수에서 사용하기 위함

        if (res > 0) {

            session.setAttribute("NEW_PASSWORD", userId);

            log.info(this.getClass().getName() + ".user/newPassword End!");

            return "user/newPassword";
        }

        else {

            session.setAttribute("NEW_PASSWORD", "");

            log.info(this.getClass().getName() + ".user/newPassword End!");

            return "user/newPassword";
        }
    }

    @PostMapping(value = "updatePassword")
    public String newPasswordProc(HttpServletRequest request, HttpSession session) throws Exception {

        log.info(this.getClass().getName() + ".user/updatePassword Start!");

        String msg = ""; // 웹에 보여줄 메세지

        // 정상적인 접근인지 체크
        String newPassword = CmmUtil.nvl((String) session.getAttribute("NEW_PASSWORD"));

        if(newPassword.length() > 0) { //정상접근
            String password = CmmUtil.nvl(request.getParameter("password")); //신규 비밀번호

            log.info("password : " + password);

            UserInfoDTO pDTO = UserInfoDTO.builder()
                    .userId(newPassword)
                    .password(EncryptUtil.encHashSHA256(password))
                    .build();

            int res = userInfoService.updatePassword(pDTO);

            // 비밀번호 재생성 화면은 보안을 위해 생성한 NEW_PASSWORD 세션 삭제
            session.setAttribute("NEW_PASSWORD", "");
            session.removeAttribute("NEW_PASSWORD");

            if(res > 0) {
                msg = "비밀번호가 재설정되었습니다.";
            } else {
                msg = "비밀번호 변경에 문제가 생겼습니다. 다시 시도해주세요.";
            }

        } else { //비정상 접근
            msg = "비정상 접근입니다.";
        }

        session.setAttribute("MSG", msg);

        log.info("msg : " + msg);

        log.info(this.getClass().getName() + ".user/updatePassword End!");

        return "/user/newPasswordResult";
    }



    @ResponseBody
    @PostMapping(value="getUserIdExists")
    public UserInfoDTO getUserExists(HttpServletRequest request) throws Exception {

        log.info(this.getClass().getName() + ".getUserIdExists Start!");

        String userId = CmmUtil.nvl(request.getParameter("userId"));

        log.info("userId : " + userId);

        UserInfoDTO pDTO = UserInfoDTO.builder().userId(userId).build();

        UserInfoDTO rDTO = Optional.ofNullable(userInfoService.getUserIdExists(pDTO))
                .orElseGet(() -> UserInfoDTO.builder().build());

        log.info(this.getClass().getName() + ".getUserIdExists End!");

        return rDTO;
    }


    @ResponseBody
    @PostMapping(value = "sendEmailAuth")
    public UserInfoDTO SendEmailAuth(HttpServletRequest request) throws Exception {
        log.info(this.getClass().getName() + ".sendEmailAuth Start!");

        String email = CmmUtil.nvl(request.getParameter("email")); // 이메일

        log.info("email : " + email);

        UserInfoDTO pDTO =  UserInfoDTO.builder()
                .email(EncryptUtil.encAES128CBC(email))
                .build();

        UserInfoDTO rDTO = Optional.ofNullable(userInfoService.sendEmailAuth(pDTO))
                .orElseGet(() -> UserInfoDTO.builder().build());

        log.info(this.getClass().getName() + ".sendEmailAuth End!");

        return rDTO;
    }

    @ResponseBody
    @PostMapping(value = "sendPassEmailAuth")
    public UserInfoDTO sendPassEmailAuth(HttpServletRequest request) throws Exception {
        log.info(this.getClass().getName() + ".sendEmailAuth Start!");

        String email = CmmUtil.nvl(request.getParameter("email")); // 이메일
        String userId = CmmUtil.nvl(request.getParameter("userId")); // 아이디

        log.info("email : " + email);
        log.info("userId : " + userId);

        UserInfoDTO pDTO =  UserInfoDTO.builder()
                .email(EncryptUtil.encAES128CBC(email))
                .userId(userId)
                .build();

        UserInfoDTO rDTO = Optional.ofNullable(userInfoService.sendPasswordEmailAuth(pDTO))
                .orElseGet(() -> UserInfoDTO.builder().build());

        log.info(this.getClass().getName() + ".sendEmailAuth End!");

        return rDTO;
    }

    @ResponseBody
    @PostMapping(value = "insertUserInfo")
    public MsgDTO inserUserInfo(HttpServletRequest request) throws Exception {

        log.info(this.getClass().getName() + ".insertUserInfo Start!");

        String msg;

        String userId = CmmUtil.nvl(request.getParameter("userId"));
        String userName = CmmUtil.nvl(request.getParameter("userName"));
        String password = CmmUtil.nvl(request.getParameter("password"));
        String email = CmmUtil.nvl(request.getParameter("email"));
        String addr1 = CmmUtil.nvl(request.getParameter("addr1"));
        String addr2 = CmmUtil.nvl(request.getParameter("addr2"));

        log.info("userId : " + userId);
        log.info("userName : " + userName);
        log.info("password : " + password);
        log.info("email : " + email);
        log.info("addr1 : " + addr1);
        log.info("addr2 : " + addr2);

        UserInfoDTO pDTO = UserInfoDTO.builder()
                .userId(userId)
                .userName(userName)
                .password(EncryptUtil.encHashSHA256(password))
                .email(EncryptUtil.encAES128CBC(email))
                .addr1(addr1)
                .addr2(addr2)
                .build();

        int res = userInfoService.insertUserInfo(pDTO);

        log.info("회원가입 결과(res) : " + res);

        if (res == 1) {
            msg = "회원가입되었습니다.";

        } else if (res == 2) {
            msg = "이미 가입된 아이디입니다.";
        } else {
            msg = "오류로 인해 회원가입이 실패했습니다.";
        }

        MsgDTO dto = MsgDTO.builder().result(res).msg(msg).build();

        log.info(this.getClass().getName() + ".insertUserInfo End!");

        return dto;
    }

    @GetMapping(value = "login")
    public String login() {

        log.info(this.getClass().getName() + ".user/login Start!");

        log.info(this.getClass().getName() + ".user/login End!");

        return  "user/login";
    }

    /**
     * 비밀번호 변경 고유 세션 삭제 후 이동
     */
    @GetMapping(value = "passProc")
    public String passProc(HttpSession session) {

        log.info(this.getClass().getName() + ".user/passProc Start!");

        // 비밀번호 재생성 화면은 보안을 위해 생성한 NEW_PASSWORD 세션 삭제
        session.setAttribute("NEW_PASSWORD", "");
        session.removeAttribute("NEW_PASSWORD");

        log.info(this.getClass().getName() + ".user/passProc End!");

        return  "redirect:/user/login";
    }

    @ResponseBody
    @PostMapping(value = "loginProc")
    public MsgDTO loginProc(HttpServletRequest request, HttpSession session) throws Exception {

        log.info(this.getClass().getName() + ".loginProc Start!");

        String msg;

        String userId = CmmUtil.nvl(request.getParameter("userId"));
        String password = CmmUtil.nvl(request.getParameter("password"));

        UserInfoDTO pDTO = UserInfoDTO.builder()
                .userId(userId)
                .password(EncryptUtil.encHashSHA256(password)).build();

        int res = userInfoService.getUserLogin(pDTO);

        log.info("res : " +res);

        if (res == 1) {

            msg = "로그인이 성공했습니다.";
            session.setAttribute("SS_USER_ID", userId);

        } else {
            msg = "아이디와 비밀번호가 올바르지 않습니다.";
        }

        MsgDTO dto = MsgDTO.builder().result(res).msg(msg).build();
        log.info(this.getClass().getName() + ".loginProc End!");

        return dto;
    }

    @GetMapping(value = "loginSuccess")
    public String loginSuccess() {
        log.info(this.getClass().getName() + ".user/loginSuccess Start!");

        log.info(this.getClass().getName() + ".user/loginSuccess End!");

        return "user/loginSuccess";

    }

    @ResponseBody
    @PostMapping(value = "logout")
    public MsgDTO logout(HttpSession session) {

        log.info(this.getClass().getName() + ".logout Start!");

        /*session.setAttribute("SS_USER_ID", "");
        session.removeAttribute("SS_USER_ID");*/

        session.invalidate();

        MsgDTO dto = MsgDTO.builder().result(1).msg("로그아웃하였습니다.").build();

        log.info(this.getClass().getName() + ".logout End!");

        return dto;

    }

    /**
     * ###########################################################################
     *
     *  이메일 있는지 체크
     *
     * ###########################################################################
     */

    /**
     * 회원 가입 전 이메일 중복체크하기(Ajax를 통해 입력한 아이디 정보 받음)
     * 유효한 이메일인지 확인하기 위해 입력된 이메일에 인증번호 포함하여 메일 발송
     */
    @ResponseBody
    @PostMapping(value = "getEmailExists")
    public UserInfoDTO getEmailExists(HttpServletRequest request) throws Exception {
        log.info(this.getClass().getName() + ".getEmailExists Start!");

        String email = CmmUtil.nvl(request.getParameter("email")); // 이메일

        log.info("email : " + email);

        UserInfoDTO pDTO = UserInfoDTO.builder()
                .email(EncryptUtil.encAES128CBC(email))
                .build();

        //이메일을 통해 중복된 이메일인지 조회
        UserInfoDTO rDTO = Optional.ofNullable(userInfoService.getEmailExists(pDTO))
                .orElseGet(() -> UserInfoDTO.builder().build());

        log.info(this.getClass().getName() + ".getEmailExists End!");

        return rDTO;
    }

    @ResponseBody
    @PostMapping(value = "signUpEmailExists")
    public UserInfoDTO signUpEmailExists(HttpServletRequest request) throws Exception {
        log.info(this.getClass().getName() + ".signUpEmailExists Start!");

        String email = CmmUtil.nvl(request.getParameter("email")); // 이메일

        log.info("email : " + email);

        UserInfoDTO pDTO = UserInfoDTO.builder()
                .email(EncryptUtil.encAES128CBC(email))
                .build();

        //이메일을 통해 중복된 이메일인지 조회
        UserInfoDTO rDTO = Optional.ofNullable(userInfoService.getEmailExists(pDTO))
                .orElseGet(() -> UserInfoDTO.builder().build());

        String existsYn = rDTO.existsYn();

        log.info("existsYn : " + existsYn);

        if (existsYn == "N") {

            //이메일을 통해 중복된 이메일인지 조회
            rDTO = Optional.ofNullable(userInfoService.sendSignUpEmailAuth(pDTO))
                    .orElseGet(() -> UserInfoDTO.builder().build());

        }

        log.info(this.getClass().getName() + ".signUpEmailExists End!");

        return rDTO;
    }

    /**
     *
     * ##################################################################################
     *
     *                                      MyPage
     *
     * ##################################################################################
     */

    @GetMapping(value = "myPage")
    public String myPage(HttpSession session, ModelMap model) throws Exception {

        log.info(this.getClass().getName() + ".user/myPage Start!");

        String userId = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID"));
        log.info("userId : " + userId);


        if (userId.length() > 0) {

            UserInfoDTO pDTO = UserInfoDTO.builder()
                    .userId(userId)
                    .build();

            UserInfoDTO rDTO = Optional.ofNullable(userInfoService.getUserInfo(pDTO))
                    .orElseGet(() -> UserInfoDTO.builder().build());

            model.addAttribute("rDTO", rDTO);

        } else {

            return "redirect:/user/login";

        }

        log.info(this.getClass().getName() + ".user/myPage End!");

        return  "user/myPage";

    }



    // 유저 정보 수정 페이지 이동

    @GetMapping(value = "myPageEdit")
    public String myPageEdit(HttpSession session, ModelMap model) throws Exception {

        log.info(this.getClass().getName() + ".user/myPageEdit Start!");

        String userId = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID"));
        log.info("userId : " + userId);


        if (userId.length() > 0) {

            UserInfoDTO pDTO = UserInfoDTO.builder()
                    .userId(userId)
                    .build();

            UserInfoDTO rDTO = Optional.ofNullable(userInfoService.getUserInfo(pDTO))
                    .orElseGet(() -> UserInfoDTO.builder().build());

            model.addAttribute("rDTO", rDTO);

        } else {

            return "redirect:/user/login";

        }

        log.info(this.getClass().getName() + ".user/myPageEdit End!");

        return  "user/myPageEdit";

    }

    // 비밀 번호 변경 (MyPage) 페이지 이동
    @GetMapping(value = "myNewPassword")
    public String myNewPassword(HttpSession session) {

        log.info(this.getClass().getName() + ".user/myNewPassword Start!");

        String userId = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID"));

        if (userId.length() > 0) {

        } else {


            return "redirect:/user/login";

        }

        return "user/myNewPassword";

    }


    @ResponseBody
    @PostMapping(value = "myUpdatePassword")
    public MsgDTO myUpdatePassword(HttpServletRequest request, HttpSession session) throws Exception {

        log.info(this.getClass().getName() + ".user/myUpdatePassword Start!");
        
        String userId = CmmUtil.nvl((String)session.getAttribute("SS_USER_ID"));

        String msg = ""; // 웹에 보여줄 메세지
        int result = 0;

        if(userId.length() > 0) { //정상접근
            
            String password = CmmUtil.nvl(request.getParameter("password")); //신규 비밀번호

            log.info("password : " + password);

            UserInfoDTO pDTO = UserInfoDTO.builder()
                    .userId(userId)
                    .password(EncryptUtil.encHashSHA256(password))
                    .build();

            int res = userInfoService.updatePassword(pDTO);
            

            if(res > 0) {
                
                msg = "비밀번호가 재설정되었습니다.";
                result = 0;
                
            } else {
                
                msg = "비밀번호 변경에 문제가 생겼습니다. 다시 시도해주세요.";

                result = 1;
                
            }

        } else { //비정상 접근
            
            msg = "비정상 접근입니다.";
            result = 2;
            
        }

        MsgDTO mDTO = MsgDTO.builder()
                .msg(msg)
                .result(result)
                .build();

        log.info("msg : " + msg);

        log.info(this.getClass().getName() + ".user/myUpdatePassword End!");

        return mDTO;
    }


    // 유저 정보 수정
    @ResponseBody
    @PostMapping(value = "updateUserInfo")
    public MsgDTO userInfoUpdate(HttpServletRequest request, HttpSession session) throws Exception {

        log.info(this.getClass().getName() + ".updateUserInfo Start!");

        String msg;

        String userId = CmmUtil.nvl((String)session.getAttribute("SS_USER_ID"));
        String userName = CmmUtil.nvl(request.getParameter("userName"));
        String email = CmmUtil.nvl(request.getParameter("email"));
        String addr1 = CmmUtil.nvl(request.getParameter("addr1"));
        String addr2 = CmmUtil.nvl(request.getParameter("addr2"));

        log.info("userId : " + userId);
        log.info("userName : " + userName);
        log.info("email : " + (email));
        log.info("addr1 : " + addr1);
        log.info("addr2 : " + addr2);

        UserInfoDTO pDTO = UserInfoDTO.builder()
                .userId(userId)
                .userName(userName)
                .email(email)
                .addr1(addr1)
                .addr2(addr2)
                .build();

        int res = userInfoService.updateUserInfo(pDTO);

        log.info("호윈 정보 수정 결과(res) : " + res);

        if (res == 1) {
            msg = "회원 정보 수정되었습니다..";

        } else {

            msg = "오류로 인해 회원 정보 수정에 실패했습니다.";

        }

        MsgDTO dto = MsgDTO.builder().result(res).msg(msg).build();

        log.info(this.getClass().getName() + ".updateUserInfo End!");

        return dto;
    }

    /**
     *
     *  ################ 관심 업종 관련 기능 ######################
     *
     *                     관심 업종 CRUD
     *
     * #########################################################
     */
    @GetMapping(value = "interestList")
    public String interestList(HttpSession session, ModelMap model) {

        log.info(this.getClass().getName() + ".user/interestList Start!");

            List<InterestDTO> rList;
            String userId = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID"));

            InterestDTO pDTO = InterestDTO.builder()
                    .userId(userId)
                    .build();

            if(userId.length() > 0) {

                rList = iInterestService.getInterestList(pDTO);

            } else {

                return "redirect:/user/login";

            }

            model.addAttribute("rList", rList);

        log.info(this.getClass().getName() + ".user/interestList End!");

        return "user/interestList";
    }

    @GetMapping(value = "interestReg")
    public String interestReg(HttpSession session) {

        log.info(this.getClass().getName() + ".user/interestReg Start!");

        String userId = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID"));

        if(userId.length() > 0) {

        } else {

            return "redirect:/user/login";

        }

        log.info(this.getClass().getName() + ".user/interestReg End!");

        return "user/interestReg";
    }

    @GetMapping(value = "interestEdit")
    public String interestEdit(HttpSession session, HttpServletRequest request, ModelMap model) {

        log.info(this.getClass().getName() + ".user/interestEdit Start!");

        String userId = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID"));
        String interestSeq = CmmUtil.nvl(request.getParameter("nSeq"));

        log.info("interestSeq : " + interestSeq);

        if(userId.length() > 0) {

            InterestDTO pDTO = InterestDTO.builder()
                    .interestSeq(interestSeq)
                    .build();

            InterestDTO rDTO = Optional.ofNullable(iInterestService.getInterestInfo(pDTO))
                    .orElseGet(() -> InterestDTO.builder().build());

            String existsYn = rDTO.existsYn();
            String indutyCd = rDTO.indutyCd();
            String locationCd = rDTO.seoulLocationCd();

            log.info("indutyCd : " + indutyCd);
            log.info("locationCd : " + locationCd);

            if (existsYn.equals("Y")) { // 정보가 있다면 데이터 build

                model.addAttribute("rDTO", rDTO);

            }

        } else {

            return "redirect:/user/login";

        }

        log.info(this.getClass().getName() + ".interestEdit End!");

        return "user/interestEdit";
    }

    @GetMapping(value = "interestInfo")
    public String interestInfo(HttpSession session, HttpServletRequest request, ModelMap model) {

        log.info(this.getClass().getName() + ".user/interestInfo Start!");

        List<SeoulSiMarketDTO> guList;
        String interestSeq = CmmUtil.nvl(request.getParameter("nSeq"));

        log.info("interestSeq : " + interestSeq);

        String userId = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID"));

        if(userId.length() > 0) {

            InterestDTO pDTO = InterestDTO.builder()
                    .interestSeq(interestSeq)
                    .build();

            /* interestSeq로 정보 받와와서 model 객체로 넘겨주기 */
            InterestDTO rDTO = iInterestService.getInterestInfo(pDTO);

            String induty = rDTO.indutyNm();
            String guSelect = rDTO.seoulLocationCd();

            guList = guMarketService.getGuMarketIndutyNm(induty, guSelect);

            model.addAttribute("guList", guList);
            model.addAttribute("rDTO", rDTO);

        } else {

            return "redirect:/user/login";

        }

        log.info(this.getClass().getName() + ".user/interestInfo End!");

        return "user/interestInfo";
    }

    @ResponseBody
    @PostMapping(value = "insertInterest")
    public MsgDTO insertInterest(HttpServletRequest request, HttpSession session) {

        log.info(this.getClass().getName() + ".user/insertInterest Start!");

        // 결과 리턴 변수 초기화
        MsgDTO mDTO;
        int res = 1;
        String msg = "";

        String userId = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID"));
        String seoulLocationCd = CmmUtil.nvl(request.getParameter("seoulLocationCd"));
        String seoulLocationNm = CmmUtil.nvl(request.getParameter("seoulLocationNm"));
        String indutyCd = CmmUtil.nvl(request.getParameter("indutyCd"));
        String indutyNm = CmmUtil.nvl(request.getParameter("indutyNm"));

        log.info("userId : " + userId);
        log.info("seoulLocationCd : " + seoulLocationCd);
        log.info("seoulLocationNm : " + seoulLocationNm);
        log.info("indutyCd : " + indutyCd);
        log.info("indutyNm : " + indutyNm);

        InterestDTO pDTO = InterestDTO.builder()
                .userId(userId)
                .seoulLocationCd(seoulLocationCd)
                .seoulLocationNm(seoulLocationNm)
                .indutyCd(indutyCd)
                .indutyNm(indutyNm)
                .build();

        InterestDTO rDTO = iInterestService.getInterestDataExists(pDTO);

        String existsYn = rDTO.existsYn();

        if(existsYn.equals("N")) {

            try {

                iInterestService.insertInterestInfo(pDTO);

                msg = "관심 업종 등록에 성공하였습니다.";

            } catch (Exception e) { //모든 에러 다 잡기

                res = 0; // 등록 실패
                msg = "관심 업종 등록에 실패했습니다. 에러는 다음과 같습니다. " + e;
                log.info("[ERROR] " + this.getClass().getName() + ".insertInterest : " + e);

            } finally {

                mDTO = MsgDTO.builder()
                        .result(res)
                        .msg(msg)
                        .build();

                log.info(this.getClass().getName() + ".insertInterest End!");
            }
        } else { // "Y" 이미 등록되어 있는 정보

            res = 0; // 등록 실패
            msg = "이미 등록되어 있는 관심 업종입니다. 다른 항목을 선택해주세요.";

            mDTO = MsgDTO.builder()
                    .result(res)
                    .msg(msg)
                    .build();

        }

        return mDTO;
    }

    @ResponseBody
    @PostMapping(value = "udateInterest")
    public MsgDTO udateInterest(HttpServletRequest request, HttpSession session) {

        log.info(this.getClass().getName() + ".user/udateInterest Start!");

        // 결과 리턴 변수 초기화
        MsgDTO mDTO;
        int res = 1;
        String msg = "";

        String interestSeq = CmmUtil.nvl(request.getParameter("interestSeq"));
        String userId = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID"));
        String seoulLocationCd = CmmUtil.nvl(request.getParameter("seoulLocationCd"));
        String seoulLocationNm = CmmUtil.nvl(request.getParameter("seoulLocationNm"));
        String indutyCd = CmmUtil.nvl(request.getParameter("indutyCd"));
        String indutyNm = CmmUtil.nvl(request.getParameter("indutyNm"));

        log.info("interestSeq : " + interestSeq);

        InterestDTO pDTO = InterestDTO.builder()
                .interestSeq(interestSeq)
                .userId(userId)
                .seoulLocationCd(seoulLocationCd)
                .seoulLocationNm(seoulLocationNm)
                .indutyCd(indutyCd)
                .indutyNm(indutyNm)
                .build();

        try {

            iInterestService.updateInterestInfo(pDTO);

            msg = "해당 관심 업종을 수정하였습니다.";

        } catch (Exception e) { //모든 에러 다 잡기

            res = 0; // 등록 실패
            msg = "관심 업종 등록에 실패했습니다. 에러는 다음과 같습니다. : " + e;
            log.info("[ERROR] " + this.getClass().getName() + ".insertInterest : " + e);

        } finally {

            mDTO = MsgDTO.builder()
                    .result(res)
                    .msg(msg)
                    .build();
        }

        log.info(this.getClass().getName() + ".udateInterest End!");

        return mDTO;
    }

    @ResponseBody
    @PostMapping(value = "deleteInterest")
    public MsgDTO deleteInterest(HttpServletRequest request) {

        log.info(this.getClass().getName() + ".user/deleteInterest Start!");

        // 결과 리턴 변수 초기화
        MsgDTO mDTO;
        int res = 1;
        String msg = "";

        String interestSeq = CmmUtil.nvl(request.getParameter("interestSeq"));

        log.info("interestSeq : " + interestSeq);

        InterestDTO pDTO = InterestDTO.builder()
                .interestSeq(interestSeq)
                .build();

        try {

            iInterestService.deleteInterestInfo(pDTO);

            msg = "해당 관심 업종을 삭제하였습니다.";

        } catch (Exception e) { //모든 에러 다 잡기

            res = 0; // 등록 실패
            msg = "관심 업종 등록에 실패했습니다. 에러는 다음과 같습니다. : " + e;
            log.info("[ERROR] " + this.getClass().getName() + ".insertInterest : " + e);

        } finally {

            mDTO = MsgDTO.builder()
                    .result(res)
                    .msg(msg)
                    .build();
        }

        log.info(this.getClass().getName() + ".deleteInterest End!");

        return mDTO;
    }

    // 회원 탈퇴 페이지
    @GetMapping(value = "withdrawal")
    public String deleteUserInfo(HttpSession session) {

        log.info(this.getClass().getName() + ".user/withdrawal Start!");

        String userId = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID"));

        if(userId.length()>0){

        } else {
            return "redirect:/user/login";
        }

        log.info(this.getClass().getName() + ".user/withdrawal End!");

        return "user/withdrawal";
    }


    /**
     * 유저 정보 삭제
     */
    @ResponseBody
    @PostMapping(value = "deleteUserInfo")
    public MsgDTO noticeDelete(HttpSession session) {

        log.info(this.getClass().getName() + ".deleteUserInfo Start!");

        String msg = ""; // 메시지 내용
        MsgDTO dto = null; // 결과 메시지 구조

        try {
            String userId = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID")); // 글번호(PK)

            log.info("userId : " + userId);

            /*
             * 값 전달은 반드시 DTO 객체를 이용해서 처리함 전달 받은 값을 DTO 객체에 넣는다.
             */
            UserInfoDTO pDTO = UserInfoDTO.builder()
                    .userId(userId)
                    .build();

            // 게시글 삭제하기 DB
            userInfoService.deleteUserInfo(pDTO);

            session.invalidate();

            msg = "탈퇴하였습니다.";

        } catch (Exception e) {
            msg = "실패하였습니다. : " + e.getMessage();
            log.info(e.toString());
            e.printStackTrace();

        } finally {

            // 결과 메시지 전달하기
            dto = MsgDTO.builder().msg(msg).build();

            log.info(this.getClass().getName() + ".deleteUserInfo End!");

        }

        return dto;
    }

}
