package com.ogp.icms.cctv.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.ogp.icms.asset.service.AssetService;
import com.ogp.icms.cctv.request.CameraErrorSearchCondition;
import com.ogp.icms.cctv.request.CameraSearchCondition;
import com.ogp.icms.cctv.service.CameraErrorServiceImpl;
import com.ogp.icms.cctv.domain.Camera;
import com.ogp.icms.cctv.domain.CameraError;
import com.ogp.icms.cctv.service.CameraServiceImpl;
import com.ogp.icms.cctv.service.VMSServiceImpl;
import com.ogp.icms.dailyreport.domain.Monitoring;
import com.ogp.icms.dailyreport.service.MonitoringServiceImpl;
import com.ogp.icms.gis.service.GisServiceImpl;
import com.ogp.icms.global.util.ResultCode;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
public class CameraApiController {
    private final CameraErrorServiceImpl cameraErrorService;
    private final MonitoringServiceImpl monitoringService;
    private final CameraServiceImpl cameraService;
    private final VMSServiceImpl vmsService;
    private final GisServiceImpl gisService;
    private final AssetService assetService;

    //region 카메라

    /**
     * 검색 + 페이징 리스트
     */
    @GetMapping("/api/cctvs")
    public List<Camera> getCameraList(CameraSearchCondition searchCondition, Pageable pageable) {
        return cameraService.getCameraPage(searchCondition, pageable);
    }

    // 모니터링 용 검색
    @GetMapping("/api/cctvs-all")
    public List<Camera> getCameraList50(CameraSearchCondition searchCondition, Pageable pageable) {
        return cameraService.getCameraPage50(searchCondition, pageable);
    }

    /**
     * 카메라 하나 조회
     * @param id
     */
    @GetMapping("/api/cctv/{id}")
    public Camera getCamera(@PathVariable Long id) {
        return cameraService.findOne(id);
    }
    
    // 전체 카메라 조회
    @GetMapping("/api/cctvs/excel")
    public List<Camera> getAllCameras() {
        return cameraService.findAll();
    }

    /**
     * 카메라 등록
     * @param camera
     * @// TODO: 2022-06-29 DTO 만들어서 제공해야함
     */
    @PostMapping("/api/cctv")
    public ResultCode addCamera(@RequestBody Camera camera) {
        return cameraService.save(camera);
    }

    /**
     * 카메라 여러개 등록
     * @// TODO: 2022-06-29 DTO 만들어서 제공해야함
     */
    @PostMapping("/api/cctv/excel/upload")
    public ResultCode addCameraList(@RequestBody List<Camera> list) {
        return cameraService.excelUpload(list);
    }

    /**
     * 카메라 수정
     * @param camera 수정할 오브젝트
     * @// TODO: 2022-06-29 DTO 만들어서 제공해야함
     */
    @PutMapping("/api/cctv")
    public ResultCode editCamera(@RequestBody Camera camera) {
        return cameraService.editCamera(camera);
    }

    /**
     * 카메라 삭제
     * @param id
     */
    @DeleteMapping("/api/cctv/{id}")
    public ResultCode deleteCamera(@PathVariable Long id) {
        return cameraService.deleteCamera(id);
    }
    //endregion

    //region 카메라 장애관리

    // 카메라별 장애 관리
    @GetMapping("/api/cctv-errors")
    public Page<CameraError> getCameraErrorList(CameraErrorSearchCondition searchCondition, Pageable pageable) {
        return cameraErrorService.getCameraErrorList(searchCondition, pageable);
    }

    @GetMapping("/api/cctv-errors/all")
    public Page<CameraError> getCameraErrorList() {
        return cameraErrorService.getCameraErrorAll();
    }

    @GetMapping("/api/cctv-errors/statistics/{pariod}")
    public List<CameraError> getStatistics(@PathVariable String pariod) {
        return cameraErrorService.getStatistics(pariod);
    }

    //카메라별 장애 관리
    @GetMapping("/api/cctv-errors/{id}/monitorings")
    public List<Monitoring> getMonitoringByCameraId(@PathVariable Long id) {
        return monitoringService.getMonitoringListByCameraId(id);
    }

    @GetMapping("/api/cctv-errors/reset1")
    public void resetCameraErrors1() {
        monitoringService.updateCctvIdFromCctvIndex();
        cameraErrorService.resetCameraErrors();
    }

    @GetMapping("/api/cctv-errors/reset2")
    public void resetCameraErrors2() {
        monitoringService.updateCctvIdFromCctvIndex();
        cameraErrorService.resetCameraErrors();
    }
    //endregion

    //region 카메라 라이센스

    /**
     * @return
     */
    @GetMapping("/api/cctv/licenses")
    public List<Camera> getLicenseList(CameraSearchCondition searchCondition, Pageable pageable) {
        return cameraService.getCameraPage(searchCondition, pageable);
    }

    /**
     * 총 등록중인 라이센스 수 반환
     * @return
     */
    @GetMapping("/api/cctv/licenses/count")
    public Long getLicenseCount() {
        return cameraService.getCameraLicenseCount();
    }
    //endregion

    //region DB 동기화 요청
    @PostMapping("/api/vms/sync")
    public ResultCode syncVms() {
        int vmsCount = vmsService.syncDeviceList();
        int gisCount = 0;

        if(vmsCount == -1) {
            return new ResultCode(0, "VMS 로그인 실패");
        }

        String profile = System.getProperty("spring.profiles.active");
        String[] vmsProfiles = {"prod", "dev", "hd"};

        if(profile.equals("hd")) {
            gisCount = assetService.updateDataBase();
        }

        return new ResultCode(0, gisCount + "개 업데이트 완료");
    }

    @PostMapping("/api/gis/sync")
    public ResultCode syncGis() {
        int gisCount = 0;

        String profile = System.getProperty("spring.profiles.active");

        if(profile.equals("prod") || profile.equals("dev")) {
            gisCount = gisService.UpdateDataBase();
        }

        return new ResultCode(0, "gis (" + gisCount + ") 개 업데이트 완료");
    }

    @GetMapping("/api/vms/login")
    public JsonNode loginVms() {
        return vmsService.login();
    }
    
    //endregion DB 동기화 요청


    @GetMapping("/api/cctv/ping")
    public ArrayNode ping(@RequestParam String ip) {
        return cameraService.ping(ip);
    }

    @GetMapping("/api/vms/manual/{id}")
    public ResponseEntity<JsonNode> vmsManualSync(@PathVariable String id) {
        return new ResponseEntity<JsonNode>(vmsService.manualSync(id), HttpStatus.OK);
    }
}
