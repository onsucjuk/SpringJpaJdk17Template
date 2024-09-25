package kopo.poly;

import kopo.poly.service.IWalkService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@EnableFeignClients
@EnableScheduling
@SpringBootApplication
@RequiredArgsConstructor
public class SpringJpaJdk17TemplatePrjApplication {

    private final IWalkService walkService;

    public static void main(String[] args) {
        SpringApplication.run(SpringJpaJdk17TemplatePrjApplication.class, args);
    }

    /*// @Scheduled 애너테이션을 클래스 내 직접 사용
    @Scheduled(cron = "15 46 14 * * ?")  // 매일 00:10에 실행 (기존 cron 표현식 유지)
    public void insertWalkData() throws Exception {
        // 데이터 삽입 로직을 여기에 작성
        walkService.collectWalk();
    }*/
}
