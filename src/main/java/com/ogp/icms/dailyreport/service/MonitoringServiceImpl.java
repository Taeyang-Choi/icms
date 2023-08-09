package com.ogp.icms.dailyreport.service;

import com.ogp.icms.cctv.dao.CameraRepository;
import com.ogp.icms.cctv.domain.Camera;
import com.ogp.icms.cctv.service.CameraErrorServiceImpl;
import com.ogp.icms.dailyreport.dao.MonitoringJpaRepository;
import com.ogp.icms.dailyreport.dao.MonitoringRepository;
import com.ogp.icms.dailyreport.domain.Monitoring;
import com.ogp.icms.dailyreport.request.MonitoringActionRequest;
import com.ogp.icms.dailyreport.request.MonitoringSearchCondition;
import com.ogp.icms.global.util.ResultCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.management.monitor.Monitor;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class MonitoringServiceImpl {
    private final CameraRepository cameraRepository;
    private final MonitoringRepository monitoringRepository;
    private final MonitoringJpaRepository monitoringJpaRepository;
    private final DailyReportServiceImpl dailyReportService;
    private final CameraErrorServiceImpl cameraErrorService;

    public Page<Monitoring> getList(MonitoringSearchCondition searchCondition, Pageable pageable) {
        int page = pageable.getPageNumber()-1;
        PageRequest newPageRequest = PageRequest.of((page <= 0) ? 0 : page, 10);
        return monitoringJpaRepository.getList(searchCondition, newPageRequest);
    }

    public List<Monitoring> getDailyEvents(Long reportId) {
        return monitoringJpaRepository.getListByReportId(reportId);
    }

    public List<Monitoring> findByDate(String date) {
        return monitoringRepository.findByWorkDate(date);
    }


    public ResultCode addDailyReport(Monitoring monitoring) {
        
        if(monitoring.getWorkDgubun().equals("D01")) { // 장애
            monitoring.setActionCode("R02");
        }
        else { // 상황
            monitoring.setActionCode("R11");
        }

        monitoringRepository.save(monitoring);

        // updated camera error
        cameraErrorService.updateCameraError(monitoring.getCctvId());
        dailyReportService.updateDailyReport(monitoring);
        return new ResultCode(0, "근무 모니터링을 작성하였습니다.");
    }

    public ResultCode modify(Long id, Monitoring monitoring) {
        Monitoring savedMonitoring = monitoringRepository.getById(id);
        Long oldCameraIndex = savedMonitoring.getCctvId();

        savedMonitoring.setWorkDate(monitoring.getWorkDate());
        savedMonitoring.setWorkDtimeFrom(monitoring.getWorkDtimeFrom());
        savedMonitoring.setWorkDgubun(monitoring.getWorkDgubun());
        savedMonitoring.setCctvId(monitoring.getCctvId());
        savedMonitoring.setCctvIndex(monitoring.getCctvIndex());
        savedMonitoring.setWorkContent(monitoring.getWorkContent());
        savedMonitoring.setBarrierLevel(monitoring.getBarrierLevel());

        monitoringRepository.flush();

        // updated camera error
        cameraErrorService.updateCameraError(oldCameraIndex);
        cameraErrorService.updateCameraError(savedMonitoring.getCctvId());
        dailyReportService.updateDailyReport(savedMonitoring);

        return new ResultCode(0, "근무 모니터링을 수정하였습니다.");
    }
    
    public ResultCode delete(Long id) {
        Monitoring old = monitoringRepository.getById(id);

        monitoringRepository.deleteById(id);
        monitoringRepository.flush();

        // updated camera error
        cameraErrorService.updateCameraError(old.getCctvId());
        dailyReportService.updateDailyReport(old);

        return new ResultCode(0, "근무 모니터링을 삭제하였습니다.");
    }

    /**
     * 모니터링 조치
     */
    public ResultCode addAction(Long id, MonitoringActionRequest actionRequest) {
        Monitoring savedDetail = monitoringRepository.getById(id);
        savedDetail.setActionCode(actionRequest.getActionCode());
        savedDetail.setActionResult(actionRequest.getActionResult());
        savedDetail.setActionRegdate(LocalDateTime.now());
        savedDetail.setActionUserid(actionRequest.getActionUserid());
        savedDetail.setBarrierLevel(actionRequest.getBarrierLevel());

        // updated camera error
        if(savedDetail.getCctvId() == null) {
            Optional<Camera> optionalCamera = cameraRepository.findByCctvIndex(savedDetail.getCctvIndex());
            if(optionalCamera.isPresent()) {
                long cctvId = optionalCamera.get().getId();
                cameraErrorService.updateCameraError(cctvId);
                savedDetail.setCctvId(cctvId);
            }
        }
        else {
            cameraErrorService.updateCameraError(savedDetail.getCctvId());
        }

        monitoringJpaRepository.clear();
        return new ResultCode(0, "수정되었습니다. ");
    }

    public List<Monitoring> getMonitoringListByCameraId(Long id) {
        return monitoringRepository.findByCctvId(id);
    }

    public List<Monitoring> getAll(MonitoringSearchCondition searchCondition) {
        return monitoringJpaRepository.getList(searchCondition);
    }
    public Monitoring getMonitoringEvent(Long id) {
        return monitoringRepository.findById(id).get();
    }

    public List<Monitoring> getByCctvId(Long id) {
        return monitoringRepository.getByCctvId(id);
    }

    public void updateCctvIdFromCctvIndex() {

        List<Monitoring> cameraIdNullList = monitoringRepository.findByCctvIdIsNull();

        for(int i = 0; i < cameraIdNullList.size(); i++) {
            Monitoring monitoring = cameraIdNullList.get(i);

            if(monitoring.getCctvIndex() == null) continue;
            Optional<Camera> cameraOptional = cameraRepository.findByCctvIndex(monitoring.getCctvIndex());

            if(!cameraOptional.isPresent()) continue;
            Camera camera = cameraOptional.get();

            monitoring.setCctvId(camera.getId());

            if(i % 100 == 0) {
                log.info("MonitoringServiceImpl.updateCctvIdFromCctvIndex({}/{})", i, cameraIdNullList.size());
                monitoringRepository.flush();
            }
        }

        monitoringRepository.flush();
    }

    public List<Monitoring> getAllNotRepairedAssets() {
        return monitoringJpaRepository.getAllNotRepairedAssets();
    }

    public List<Monitoring> getAllSituations() {
        return monitoringJpaRepository.getAllSituations();
    }
}
