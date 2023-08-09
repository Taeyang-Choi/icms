package com.ogp.icms.asset.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.ogp.icms.asset.domain.Asset;
import com.ogp.icms.asset.service.AssetService;
import com.ogp.icms.global.util.ResultCode;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

@RestController
@AllArgsConstructor
public class AssetApiController {
    private final AssetService assetService;
    private final ObjectMapper om;

    @GetMapping("/api/assets")
    public List<Asset> getAll() {
        return assetService.getAll();
    }

    @PostMapping("/api/assets/upload")
    public ResultCode upload(String jsonString) throws Exception {
        ArrayList<Asset> assetList = om.readValue(jsonString, new TypeReference<ArrayList<Asset>>() {});

        //ArrayNode arrayNode
        return assetService.upload(assetList);
    }

    @GetMapping("/api/assets/ping")
    public List<Long> pingTest(String ip) {
        ArrayList<Long> list = new ArrayList<>();
        try {
            InetAddress inetAddress = InetAddress.getByName(ip);

            for(int i = 0; i < 3; i++) {
                long startTime = System.currentTimeMillis();
                boolean isReachable = inetAddress.isReachable(5000); // 타임아웃 시간 설정 (5초)
                long endTime = System.currentTimeMillis();
                long duration = endTime - startTime;
                list.add(duration);
            }
        }
        catch (Exception e) {
            list.add(9999L);
        }
        return list;
    }
}
