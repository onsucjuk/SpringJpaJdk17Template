package kopo.poly.service.impl;

import kopo.poly.dto.MailDTO;
import kopo.poly.dto.UserInfoDTO;
import kopo.poly.repository.NoticeRepository;
import kopo.poly.repository.UserInfoRepository;
import kopo.poly.repository.entity.UserInfoEntity;
import kopo.poly.service.IMailService;
import kopo.poly.service.IUserInfoService;
import kopo.poly.util.CmmUtil;
import kopo.poly.util.DateUtil;
import kopo.poly.util.EncryptUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserInfoService implements IUserInfoService {

    private final UserInfoRepository userInfoRepository;
    private final NoticeRepository noticeRepository;
    private final IMailService mailService; // 메일 발송을 위한 MailService 자바 객체 가져오기

    @Override
    public UserInfoDTO getUserIdExists(UserInfoDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".getUserIdExists Start!");

        UserInfoDTO rDTO;

        String userId = CmmUtil.nvl(pDTO.userId());

        log.info("userId : " + userId);

        Optional<UserInfoEntity> rEntity = userInfoRepository.findByUserId(userId);

        if (rEntity.isPresent()) {
            rDTO = UserInfoDTO.builder().existsYn("Y").build();
        } else {
            rDTO = UserInfoDTO.builder().existsYn("N").build();
        }

        log.info(this.getClass().getName() + ".getUserIdExists End!");

        return rDTO;
    }

    @Override
    public UserInfoDTO getEmailExists(UserInfoDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".getEmailExists Start!");

        UserInfoDTO rDTO;

        String email = CmmUtil.nvl(pDTO.email());

        log.info("email : " + email);

        Optional<UserInfoEntity> rEntity = userInfoRepository.findByEmail(email);

        if (rEntity.isPresent()) {

            UserInfoEntity userInfoEntity = rEntity.get();
            String userId = userInfoEntity.getUserId();
            log.info("userId : " + userId);

            rDTO = UserInfoDTO.builder()
                    .existsYn("Y")
                    .userId(userId)
                    .build();
        } else {
            rDTO = UserInfoDTO.builder().existsYn("N").build();
        }

        log.info(this.getClass().getName() + ".getEmailExists End!");

        return rDTO;

    }

    @Override
    public UserInfoDTO getUserInfo(UserInfoDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".getUserInfo Start!");

        UserInfoDTO rDTO;

        String userId = CmmUtil.nvl(pDTO.userId());

        log.info("userId : " + userId);

        Optional<UserInfoEntity> rEntity = userInfoRepository.findByUserId(userId);

        if (rEntity.isPresent()) {

            String userName = rEntity.get().getUserName();
            String email = EncryptUtil.decAES128CBC(rEntity.get().getEmail());
            String addr1 = rEntity.get().getAddr1();
            String addr2 = rEntity.get().getAddr2();
            String regDt = rEntity.get().getRegDt();

            log.info("userId : " + userId);
            log.info("userName : " + userName);
            log.info("email : " + email);
            log.info("addr1 : " + addr1);
            log.info("addr2 : " + addr2);
            log.info("regDt : " + regDt);

            rDTO = UserInfoDTO.builder()
                    .userId(userId)
                    .userName(userName)
                    .email(email)
                    .addr1(addr1)
                    .addr2(addr2)
                    .regDt(regDt)
                    .existsYn("Y")
                    .build();

        } else {

            rDTO = UserInfoDTO.builder().existsYn("N").build();

        }

        log.info(this.getClass().getName() + ".getUserInfo End!");

        return rDTO;
    }

    @Override
    public int insertUserInfo(UserInfoDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".insertUserInfo Start!");

        // 회원가입 설공 : 1, 아이디 중복으로 인한 가입 취소 : 2, 기타 에러 발생 : 0
        int res = 0;

        String userId = CmmUtil.nvl(pDTO.userId());
        String userName = CmmUtil.nvl(pDTO.userName());
        String password = CmmUtil.nvl(pDTO.password());
        String email = CmmUtil.nvl(pDTO.email());
        String addr1 = CmmUtil.nvl(pDTO.addr1());
        String addr2 = CmmUtil.nvl(pDTO.addr2());

        log.info("pDTO : " + pDTO);

        Optional<UserInfoEntity> rEntity = userInfoRepository.findByUserId(userId);

        if (rEntity.isPresent()) {
            res = 2;
        } else {

            UserInfoEntity pEntity = UserInfoEntity.builder()
                    .userId(userId).userName(userName)
                    .password(password)
                    .email(email)
                    .addr1(addr1).addr2(addr2)
                    .regDt(DateUtil.getDateTime("yyyy-MM-dd hh:mm:ss"))
                    .build();

            // 회원정보 DB에 저장
            userInfoRepository.save(pEntity);

            // 회원가입 여부 확인
            rEntity = userInfoRepository.findByUserId(userId);

            if (rEntity.isPresent()) { // 값이 있으면 가입 성공
                res = 1;
            } else { // 값이 없다면
                res = 0;
            }

        }

        log.info((this.getClass().getName() + ".insertUserInfo End!"));

        return res;
    }

    @Override
    public int getUserLogin(UserInfoDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".getUserLogin Start!");

        int res = 0;

        String userId = CmmUtil.nvl(pDTO.userId());
        String password = CmmUtil.nvl(pDTO.password());

        log.info("userId : " + userId);
        log.info("password : " + password);

        Optional<UserInfoEntity> rEntity = userInfoRepository.findByUserIdAndPassword(userId, password);

        if (rEntity.isPresent()) {
            res = 1;
        }

        log.info(this.getClass().getName() + ".getUserLoginCheck End!");

        return res;
    }

    @Override
    public UserInfoDTO sendEmailAuth(UserInfoDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".sendEmailAuth Start!");

        // DB 이메일이 존재하는지 SQL 쿼리 실행
        // SQL 쿼리에 COUNT()를 사용하기 때문에 반드시 조회 결과 존재
        UserInfoDTO rDTO = getEmailExists(pDTO);
        log.info("rDTO : " + rDTO);

        String existsYn = CmmUtil.nvl(rDTO.existsYn());
        log.info("existsYn : " + existsYn);

        if(existsYn.equals("Y")) {
            // 6자리 랜덤 숫자 생성하기

            int authNumber = ThreadLocalRandom.current().nextInt(100000,1000000);

            log.info("authNumber : " + authNumber);

            // 인증번호 발송 로직
            MailDTO dto = MailDTO.builder()
                    .title("비밀번호 변경 이메일 확인 인증번호 발송 메일")
                    .contents("인증번호는 " + authNumber + "입니다.")
                    .toMail(EncryptUtil.decAES128CBC(pDTO.email()))
                    .build();

            log.info("dto Title : " + dto.title());
            log.info("dto contents : " + dto.contents());
            log.info("dto toMail : " + dto.toMail());

            mailService.doSendMail(dto); // 메일 발송

            //메일 변수값 초기화
            dto = null;

                    rDTO = UserInfoDTO.builder()
                    .authNumber(authNumber)
                    .existsYn(existsYn)
                    .build(); // 인증번호를 결과값에 넣어주기

        }

        log.info(this.getClass().getName() + ".sendEmailAuth End!");

        return rDTO;
    }

    @Override
    public int searchUserIdOrPasswordPro(UserInfoDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".searchUserIdOrPasswordProc Start!");

        String userId = pDTO.userId();
        String userName = pDTO.userName();
        String email = pDTO.email();

        log.info("userId : " + userId);
        log.info("userName : " + userName);
        log.info("email : " + email);

        int res = 0;

        Optional<UserInfoEntity> rEntity = userInfoRepository.findByUserIdAndUserNameAndEmail(userId, userName, email);

        if (rEntity.isPresent()) {
            res = 1;
        }

        log.info(this.getClass().getName() + ".searchUserIdOrPasswordProc End!");

        return res;

    }

    @Override
    public int updatePassword(UserInfoDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".updatePassword Start!");

            String userId = pDTO.userId();
            String password = pDTO.password();
            int res = 0;

            Optional<UserInfoEntity> rEntity = userInfoRepository.findByUserId(userId);

            if(rEntity.isPresent()){

                String userName = rEntity.get().getUserName();
                String email = rEntity.get().getEmail();
                String addr1 = rEntity.get().getAddr1();
                String addr2 = rEntity.get().getAddr2();
                String regDt = rEntity.get().getRegDt();

                log.info("userId : " + userId);
                log.info("userName : " + userName);
                log.info("email : " + email);
                log.info("addr1 : " + addr1);
                log.info("addr2 : " + addr2);
                log.info("regDt : " + regDt);

                UserInfoEntity pEntity = UserInfoEntity.builder()
                        .userId(userId)
                        .userName(userName)
                        .password(password)
                        .email(email)
                        .addr1(addr1)
                        .addr2(addr2)
                        .regDt(regDt)
                        .build();

                // 회원정보 DB에 저장
                userInfoRepository.save(pEntity);

                res = 1;
            }

        log.info(this.getClass().getName() + ".updatePassword END!");

        return res;

    }

    @Override
    public int updateUserInfo(UserInfoDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".updateUserInfo Start!");

        String userId = pDTO.userId();

        int res = 0;

        Optional<UserInfoEntity> rEntity = userInfoRepository.findByUserId(userId);

        if(rEntity.isPresent()){

            String userName = pDTO.userName();
            String email = EncryptUtil.encAES128CBC(pDTO.email());
            String addr1 = pDTO.addr1();
            String addr2 = pDTO.addr2();

            log.info("userId : " + userId);
            log.info("userName : " + userName);
            log.info("email : " + email);
            log.info("addr1 : " + addr1);
            log.info("addr2 : " + addr2);

            // 회원정보 DB에 저장
            userInfoRepository.updateUserInfo(userId,email,userName,addr1, addr2);

            res = 1;

        }

        log.info(this.getClass().getName() + ".updateUserInfo END!");

        return res;

    }

    @Override
    public void deleteUserInfo(UserInfoDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".deleteUserInfo Start!");

        String userId = pDTO.userId();

        log.info("userId : " + userId);


        // 데이터 수정하기
        userInfoRepository.deleteById(userId);

        log.info(this.getClass().getName() + ".deleteUserInfo End!");

    }
}
