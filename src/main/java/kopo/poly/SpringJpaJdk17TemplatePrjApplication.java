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
public class SpringJpaJdk17TemplatePrjApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringJpaJdk17TemplatePrjApplication.class, args);
    }

    @RequiredArgsConstructor
    @Component
    class InsertWalkData {

        private final IWalkService walkService;

        @Scheduled(cron = "0 10 0 * * ?")  // 매일 00:10에 실행
        public void insertWalkData() throws Exception {

            // 데이터 삽입 로직을 여기에 작성
            walkService.collectWalk();

        }
    }
}
