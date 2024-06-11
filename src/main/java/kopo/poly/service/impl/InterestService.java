package kopo.poly.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import kopo.poly.dto.InterestDTO;
import kopo.poly.repository.InterestRepository;
import kopo.poly.repository.entity.InterestEntity;
import kopo.poly.service.IInterestService;
import kopo.poly.util.CmmUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static kopo.poly.repository.entity.QUserInfoEntity.userInfoEntity;

@Slf4j
@RequiredArgsConstructor
@Service
public class InterestService implements IInterestService {

    private final InterestRepository interestRepository;

    @Override
    public List<InterestDTO> getInterestList(InterestDTO pDTO)  {

        log.info(this.getClass().getName() + ".getInterestList Start!");

        String userId = pDTO.userId();

        List<InterestEntity> rList = interestRepository.findByUserIdOrderByInterestSeqDesc(userId);

        // 엔티티의 값들을 DTO에 맞게 넣어주기
        List<InterestDTO> nList = new ObjectMapper().convertValue(rList,
                new TypeReference<>() {
                });

        log.info(this.getClass().getName() + ".getInterestList End!");

        return nList;
    }

    @Override
    public InterestDTO getInterestInfo(InterestDTO pDTO)  {

        log.info(this.getClass().getName() + ".getInterestInfo Start!");

        Long interestSeq = Long.parseLong(pDTO.interestSeq());

        InterestDTO rDTO;

        Optional<InterestEntity> rEntity = interestRepository.findById(interestSeq);

        if (rEntity.isPresent()) {

            InterestEntity interestEntity = rEntity.get();

            String returnInterestSeq = interestEntity.getInterestSeq().toString();;
            String userId = interestEntity.getUserId();
            String seoulLocationCd = interestEntity.getSeoulLocationCd();
            String seoulLocationNm = interestEntity.getSeoulLocationNm();
            String indutyCd = interestEntity.getIndutyCd();
            String indutyNm = interestEntity.getIndutyNm();

            rDTO = InterestDTO.builder()
                    .interestSeq(returnInterestSeq)
                    .userId(userId)
                    .seoulLocationCd(seoulLocationCd)
                    .seoulLocationNm(seoulLocationNm)
                    .indutyCd(indutyCd)
                    .indutyNm(indutyNm)
                    .existsYn("Y")
                    .build();
        } else {

            rDTO = InterestDTO.builder()
                    .existsYn("N")
                    .build();
        }

        log.info(this.getClass().getName() + ".getInterestInfo End!");

        return rDTO;
    }

    @Override
    public void updateInterestInfo(InterestDTO pDTO)  {

        log.info(this.getClass().getName() + ".updateInterestInfo Start!");

        Long interestSeq = Long.parseLong(pDTO.interestSeq());
        String userId = pDTO.userId();
        String seoulLocationCd = pDTO.seoulLocationCd();
        String seoulLocationNm = pDTO.seoulLocationNm();
        String indutyNm = pDTO.indutyNm();
        String indutyCd = pDTO.indutyCd();

        Optional<InterestEntity> rEntity = interestRepository.findById(interestSeq);

        if (rEntity.isPresent()) {

            InterestEntity pEntity = InterestEntity.builder()
                    .interestSeq(interestSeq)
                    .userId(userId)
                    .seoulLocationNm(seoulLocationNm)
                    .seoulLocationCd(seoulLocationCd)
                    .indutyNm(indutyNm)
                    .indutyCd(indutyCd)
                    .build();

            interestRepository.save(pEntity);

        }

        log.info(this.getClass().getName() + ".updateInterestInfo End!");

    }

    @Override
    public void deleteInterestInfo(InterestDTO pDTO)  {

        log.info(this.getClass().getName() + ".deleteInterestInfo Start!");

        Long interestSeq = Long.parseLong(pDTO.interestSeq());

        log.info("삭제할 interestSeq : " + interestSeq);

        interestRepository.deleteById(interestSeq);

        log.info(this.getClass().getName() + ".deleteInterestInfo End!");

    }

    @Override
    public void insertInterestInfo(InterestDTO pDTO) {

        log.info(this.getClass().getName() + ".insertInterestInfo Start!");

        String userId = CmmUtil.nvl(pDTO.userId());
        String seoulLocationCd = CmmUtil.nvl(pDTO.seoulLocationCd());
        String seoulLocationNm = CmmUtil.nvl(pDTO.seoulLocationNm());
        String indutyCd = CmmUtil.nvl(pDTO.indutyCd());
        String indutyNm = CmmUtil.nvl(pDTO.indutyNm());

        InterestEntity pEntity = InterestEntity.builder()
                .userId(userId)
                .indutyCd(indutyCd)
                .indutyNm(indutyNm)
                .seoulLocationCd(seoulLocationCd)
                .seoulLocationNm(seoulLocationNm)
                .build();

        // 관심 업종 저장하기
        interestRepository.save(pEntity);

        log.info(this.getClass().getName() + ".insertInterestInfo End!");

    }

    @Override
    public InterestDTO getInterestDataExists(InterestDTO pDTO) {

        log.info(this.getClass().getName() + ".getInterestDataExists Start!");

        InterestDTO rDTO;

        String userId = CmmUtil.nvl(pDTO.userId());
        String indutyNm = CmmUtil.nvl(pDTO.indutyNm());
        String seoulLocationCd = CmmUtil.nvl(pDTO.seoulLocationCd());

        Optional<InterestEntity> rEntity = interestRepository.findByUserIdAndIndutyNmAndSeoulLocationCd(userId, indutyNm, seoulLocationCd);

        if (rEntity.isPresent()) {
            rDTO = InterestDTO.builder()
                    .existsYn("Y")
                    .build();
        } else {
            rDTO = InterestDTO.builder()
                    .existsYn("N")
                    .build();
        }

        log.info(this.getClass().getName() + ".getInterestDataExists End!");

        return rDTO;
    }
}
