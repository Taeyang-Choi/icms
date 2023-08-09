package com.ogp.icms.develop.test;

import com.ogp.icms.cctv.dao.CameraErrorRepository;
import com.ogp.icms.cctv.dao.CameraRepository;
import com.ogp.icms.cctv.domain.Camera;
import com.ogp.icms.cctv.domain.CameraError;
import com.ogp.icms.cctv.service.CameraErrorServiceImpl;
import com.ogp.icms.dailyreport.dao.MonitoringRepository;
import com.ogp.icms.dailyreport.domain.Monitoring;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@Transactional
@RequiredArgsConstructor
public class TestController {
    private final MonitoringRepository monitoringRepository;
    private final CameraRepository cameraRepository;
    private final CameraErrorServiceImpl cameraErrorService;
    private final CameraErrorRepository cameraErrorRepository;

    @GetMapping("/dev/insert-cctv-id-into-monitoring")
    public void dev01() {
        // 1. detail 로딩
        List<Monitoring> list = monitoringRepository.findAll();

        for(int i = 0; i < list.size(); i++) {
            Monitoring monitoring = list.get(i);

            Optional<Camera> cameraOptional = cameraRepository.findByCctvIndex(monitoring.getCctvIndex());
            if(!cameraOptional.isPresent()) continue;
            monitoring.setCctvId(cameraOptional.get().getId());
        }
    }
    
    
    // cctv_error 테이블 기본 데이터 생성
    @GetMapping("/dev/cctv/new-data")
    public void dev02() {
        // 1. detail 로딩
        List<Camera> list = cameraRepository.findAll();

        for(int i = 0; i < list.size(); i++) {
            Camera camera = list.get(i);
            CameraError cameraError = new CameraError();
            cameraError.setCctvId(camera.getId());
            cameraError.setCctvIndex(camera.getCctvIndex());
            cameraError.setCctvGubun(camera.getCctvGubun());
            cameraError.setJuso(camera.getJuso());
            cameraError.setModel(camera.getModel());
            cameraError.setYmd(camera.getInstallymd());
            cameraError.setData("{}");

            cameraErrorRepository.save(cameraError);
        }
    }
}
