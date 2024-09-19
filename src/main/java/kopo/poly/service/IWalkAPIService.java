package kopo.poly.service;

import feign.Param;
import feign.RequestLine;
import kopo.poly.dto.WalkDTO;
import org.springframework.cloud.openfeign.FeignClient;


@FeignClient(name="WalkAPI", url="http://openapi.seoul.go.kr:8088")
public interface IWalkAPIService {

    @RequestLine("GET /{apiKey}/{dataType}/IotVdata018/{startIdx}/{endIdx}")
    WalkDTO getWalkInfo(
            @Param("apiKey") String apiKey,
            @Param("dataType") String dataType,
            @Param("startIdx") String startIdx,
            @Param("endIdx") String endIdx
    );

}
