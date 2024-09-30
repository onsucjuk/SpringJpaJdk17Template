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

    public static void main(String[] args) {
        SpringApplication.run(SpringJpaJdk17TemplatePrjApplication.class, args);
    }

}
