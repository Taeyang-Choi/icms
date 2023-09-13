package com.ogp.icms.cctv.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ogp.icms.asset.dao.Asset2Repository;
import com.ogp.icms.asset.domain.Asset2;
import com.ogp.icms.cctv.dao.CameraErrorJpaRepository;
import com.ogp.icms.cctv.dao.CameraErrorRepository;
import com.ogp.icms.cctv.dao.CameraRepository;
import com.ogp.icms.cctv.domain.Camera;
import com.ogp.icms.cctv.domain.CameraError;
import com.ogp.icms.cctv.request.CameraErrorSearchCondition;
import com.ogp.icms.dailyreport.dao.MonitoringRepository;
import com.ogp.icms.dailyreport.domain.Monitoring;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.springframework.util.StringUtils.hasText;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CameraErrorServiceImpl {
    private final MonitoringRepository monitoringRepository;
    private final CameraRepository cameraRepository;
    private final CameraErrorJpaRepository cameraErrorJpaRepository;
    private final CameraErrorRepository cameraErrorRepository;
    private final Asset2Repository asset2Repository;
    private final ObjectMapper om;

    /**
     * 카메라 장애 페이지
     */
    public Page<CameraError> getCameraErrorList(CameraErrorSearchCondition searchCondition, Pageable pageable) {
        int page = pageable.getPageNumber()-1;
        PageRequest newPageRequest = PageRequest.of((page <= 0) ? 0 : page, 10);
        return cameraErrorJpaRepository.findByQuery(searchCondition, newPageRequest);
    }

    public Page<CameraError> getCameraErrorAll() {
        PageRequest newPageRequest = PageRequest.of(0, 2000);
        return cameraErrorJpaRepository.findByQuery(new CameraErrorSearchCondition(), newPageRequest);
    }


    /**
     * CameraError를 생성하거나 갱신 한다.
     * @param cctvId
     */
    public void updateCameraError(Long cctvId) {
        HashMap<String, Integer> hashMap = new HashMap<String, Integer>();

        // 1. select from daily report detail by cctvId
        List<Monitoring> monitoringList = monitoringRepository.findByCctvIdAndWorkDgubun(cctvId, "D01");

        // 2. count by cases
        for (Monitoring monitoring : monitoringList ) {
            String actionCode = monitoring.getActionCode();
            String barrierLevel = monitoring.getBarrierLevel();

            int count = 0;
            // 1). action code
            if(hasText(actionCode)) {
                count = hashMap.containsKey(actionCode) ? hashMap.get(actionCode) : 0;
                hashMap.put(actionCode, ++count);
            }

            // 2). barrier level
            if(hasText(barrierLevel) ) {
                count = hashMap.containsKey(barrierLevel) ? hashMap.get(barrierLevel) : 0;
                hashMap.put(barrierLevel, ++count);
            }
        }

        // 4. convert to jsonNode for marshaling
        JsonNode jsonNode = om.createObjectNode();
        if(hashMap.size() > 0) {
            jsonNode= om.valueToTree(hashMap);
        }

        // 5. find CctvError
        Optional<CameraError> optionalCameraError = cameraErrorRepository.findByCctvId(cctvId);
        CameraError cameraError = null;

        if(optionalCameraError.isPresent()) {
            cameraError = optionalCameraError.get();
            cameraError.setData(jsonNode.toString());
        } else {
            Optional<Asset2> optionalCamera = asset2Repository.findByRefId(cctvId.toString());
            if(optionalCamera.isPresent()) {
                Asset2 asset2 = optionalCamera.get();
                JSONObject jsonobj = new JSONObject();
                try {
                    jsonobj = (JSONObject) (new JSONParser()).parse(asset2.getJsonobj());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                cameraError = new CameraError();
                cameraError.setCctvId(Long.parseLong(asset2.getRefId()));
                cameraError.setCctvIndex(asset2.getName());
                cameraError.setCctvGubun(asset2.getPurpose());
                cameraError.setJuso(asset2.getAddress());
                cameraError.setModel(jsonobj.get("cctvmodel").toString());
                cameraError.setYmd(jsonobj.get("insdate").toString());
                cameraError.setData(jsonNode.toString());
                cameraErrorRepository.save(cameraError);
            } else {
                log.info("no camera({})", cctvId);
            }
        }
    }

    @Deprecated
    public void updateCameraError(Asset2 asset2) {
        List<CameraError> list = cameraErrorRepository.findByCctvGubun(asset2.getName());

        //TODO error도 업데이트
        System.out.println(list);
    }

    /**
     * 기간에 대한 카메라 통계 가져옴
     */
    public List<CameraError> getStatistics(String pariod) {
        return cameraErrorRepository.findByDataNot("{}");
    }

    public void resetCameraErrors() {
        List<Camera> cameraList = cameraRepository.findAll();
        for(int i = 0; i < cameraList.size(); i++) {
            Camera camera = cameraList.get(i);

            updateCameraError(camera.getId());
            if(i % 100 == 0) {
                log.info("CameraErrorServiceImpl.resetCameraErrors({}/{})", i, cameraList.size());
                cameraErrorRepository.flush();
            }
        }

        log.info("CameraErrorServiceImpl.resetCameraErrors() Completed");
    }
}
