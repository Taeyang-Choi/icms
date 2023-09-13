package com.ogp.icms.asset.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.ogp.icms.asset.domain.Asset;
import com.ogp.icms.asset.domain.Asset2;
import com.ogp.icms.asset.request.Asset2SearchCondition;
import com.ogp.icms.asset.service.AssetService;
import com.ogp.icms.cctv.domain.Camera;
import com.ogp.icms.cctv.request.CameraSearchCondition;
import com.ogp.icms.global.entity.SearchCondition;
import com.ogp.icms.global.util.ResultCode;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

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



    // 엑셀 통합 asset2
    @GetMapping("/api/asset2")
    public List<Asset2> getAll2() {
        return assetService.getAll2();
    }

    @GetMapping("/api/asset2/page")
    public List<Asset2> getCameraList(Asset2SearchCondition searchCondition, Pageable pageable) {
        return assetService.getAsset2Page(searchCondition, pageable);
    }

    @PostMapping("/api/asset2/syncGis")
    public ResultCode syncGis() {
        int gisCount = 0;

        String profile = System.getProperty("spring.profiles.active");

        if(profile.equals("prod") || profile.equals("dev")) {
            gisCount = assetService.updateDataBase();
        }

        return new ResultCode(0, "gis (" + gisCount + ") 개 업데이트 완료");
    }

    @GetMapping("/api/asset2/{id}")
    public Asset2 getCamera(@PathVariable String id) {
        return assetService.findOne(id);
    }

    /**
     * 카메라 수정
     * @param asset 수정할 오브젝트
     * @// TODO: 2022-06-29 DTO 만들어서 제공해야함
     */
    @PutMapping("/api/asset2/edit")
    public ResultCode editCamera(@RequestBody Asset2 asset) {
        return assetService.editCamera(asset);
    }

    @PutMapping("/api/asset2/upload")
    public ResultCode addCameraList(@RequestBody List<Asset2> list) {
        return assetService.uploadCameraList(list);
    }

    /**
     * 카메라 삭제
     * @param id
     */
    @DeleteMapping("/api/asset2/delete/{id}")
    public ResultCode deleteCamera(@PathVariable String id) {
        return assetService.deleteCamera(id);
    }

    @PutMapping("/api/asset2/write")
    public ResultCode addCamera(@RequestBody Asset2 asset) {
        return assetService.save(asset);
    }

    @GetMapping("/api/asset2/refId/{id}")
    public Asset2 getCameraByRefId(@PathVariable String id) {
        return assetService.findOneByRefId(id);
    }

    //region 카메라 라이센스

    /**
     * @return
     */
    /*@GetMapping("/api/asset2/license")
    public List<Asset2> getLicenseList(Asset2SearchCondition searchCondition, Pageable pageable) {
        return assetService.getAsset2Page(searchCondition, pageable);
    }*/

    /**
     * 총 등록중인 라이센스 수 반환
     * @return
     */
    @GetMapping("/api/asset2/license/count")
    public Long getLicenseCount() {
        return assetService.getAsset2LicenseCount();
    }
    //endregion
}
