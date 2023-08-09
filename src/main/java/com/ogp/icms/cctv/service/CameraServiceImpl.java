package com.ogp.icms.cctv.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.ogp.icms.cctv.api.CameraApiController;
import com.ogp.icms.cctv.dao.CameraJpaRepository;
import com.ogp.icms.cctv.dao.CameraRepository;
import com.ogp.icms.cctv.domain.Camera;
import com.ogp.icms.cctv.request.CameraSearchCondition;
import com.ogp.icms.global.util.ResultCode;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class CameraServiceImpl {
    private final CameraRepository cameraRepository;
    private final CameraJpaRepository cameraJpaRepository;
    private final CameraErrorServiceImpl cameraErrorService;

    private final ObjectMapper objectMapper;

    public List<Camera> getCameraPage(CameraSearchCondition searchCondition, Pageable pageable) {
        int page = pageable.getPageNumber()-1;
        PageRequest newPageRequest = PageRequest.of((page <= 0) ? 0 : page, 10);
        return cameraJpaRepository.findByQuery(searchCondition, newPageRequest);
    }

    public List<Camera> getCameraPage50(CameraSearchCondition searchCondition, Pageable pageable) {
        int page = pageable.getPageNumber()-1;
        PageRequest newPageRequest = PageRequest.of((page <= 0) ? 0 : page, 50);
        return cameraJpaRepository.findByQuery(searchCondition, newPageRequest);
    }

    /**
     * id로 카메라 반환
     * @param id
     * @return
     */
    public Camera findOne(Long id) {
        Optional<Camera> cameraOptional = cameraRepository.findById(id);

        if(cameraOptional.isPresent()) {
            return cameraOptional.get();
        }
        else {
            return new Camera();
        }
    }

    /**
     * 카메라 생성
     * @param camera
     */
    public ResultCode save(Camera camera) {
        cameraRepository.save(camera);
        cameraErrorService.updateCameraError(camera.getId());
        return new ResultCode(0, "카메라를 생성했습니다.");
    }

    public ResultCode deleteCamera(Long id) {
        cameraRepository.deleteById(id);
        return new ResultCode(0, "카메라를 삭제했습니다.");
    }

    /**
     * 카메라 수정
     * @see CameraApiController#editCamera(Camera)
     */
    public ResultCode editCamera(Camera camera) {
        cameraJpaRepository.updateCamera(camera);
        cameraErrorService.updateCameraError(camera.getId());
        return new ResultCode(0, "카메라를 수정했습니다.");
    }

    public Long getCameraLicenseCount() {
        return cameraRepository.count();
    }

    /**
     * 엑셀로 업로드
     */
    public ResultCode excelUpload(List<Camera> list) {
        try {
            //ArrayList<Camera> newList = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                Optional<Camera> cameraOptional = cameraRepository.findByCctvIndex(list.get(i).getCctvIndex());
                if (cameraOptional.isPresent()) {
                    cameraRepository.deleteById(cameraOptional.get().getId());
                    cameraRepository.flush();
                }
                //if(byCctvIndex == null) newList.add(list.get(i));
            }
            cameraRepository.saveAll(list);
            return new ResultCode(0, list.size() + "개의 자산 정보를 업로드 했습니다.");
        }
        catch(Exception e) {
            return new ResultCode(e);
        }
    }

    public List<Camera>findAll() {
        return cameraRepository.findAll();
    }

    public ArrayNode ping(String ipAddress) {
        int timeout = 10000;

        ArrayNode arrayNode = objectMapper.createArrayNode();

        long elapsedTime = pingWithElapsedTime(ipAddress, timeout);
        arrayNode.add(elapsedTime);
        return arrayNode;
    }

    public long pingWithElapsedTime(String ipAddress, int timeout) {
        long startTime = System.currentTimeMillis();
        try {
            InetAddress target = InetAddress.getByName(ipAddress);
            if (target.isReachable(timeout)) {
                long elapsedTime = System.currentTimeMillis() - startTime;
                return elapsedTime;
            }
        } catch (UnknownHostException e) {
        } catch (IOException e) {
        }
        return -1;
    }
}
