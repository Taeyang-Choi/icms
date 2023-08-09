package com.ogp.icms.gis.service;

import com.ogp.icms.cctv.dao.CameraRepository;
import com.ogp.icms.cctv.domain.Camera;
import com.ogp.icms.gis.domain.CrimeRequest;
import com.ogp.icms.gis.repository.GisRepository;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static com.ogp.icms.gis.connection.ConnectionConst.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Transactional(value = "transactionManager")
@Slf4j
@Service
public class GisServiceImpl {
    private final GisRepository gisRepository;
    private final CameraRepository cameraRepository;

    public GisServiceImpl(GisRepository gisRepository, CameraRepository cameraRepository) {
        String activeProfile = System.getProperty("spring.profiles.active");
        this.cameraRepository = cameraRepository;
        HikariDataSource dataSource = new HikariDataSource();

        if(activeProfile.equals("prod")) dataSource.setJdbcUrl(URL);
        else  dataSource.setJdbcUrl("jdbc:postgresql://localhost:5432/xeus_platform_gyeongnam_goseong_v2");
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);
        this.gisRepository = new GisRepository(dataSource);
    }

    public List<CrimeRequest> findAllCrimeRequest(String date1, String date2) {
        return gisRepository.findByDate(date1, date2);
    }

    public HashMap<String, Integer> getAllCount() {
        return gisRepository.getAllCount();
    }

    /**
     * 데이터 베이스를 업데이트한다.
     */
    public int UpdateDataBase() {
        ArrayList<Camera> list = gisRepository.getAllCameras();
        ArrayList<Camera> newList = new ArrayList<>();
        try {

            for (int i = 0; i < list.size(); i++) {
                Camera listItem = list.get(i);
                Optional<Camera> cameraByIdOptional = cameraRepository.findById(listItem.getId());

                if (cameraByIdOptional.isPresent()) { // 있다면(VMS에서 가져왔다면, 정보만 변경)
                    if(i % 50 == 0) log.info("camera is exist {}, {}", i , listItem);
                    Camera oldCamera = cameraByIdOptional.get();
                    oldCamera.setLocation(listItem.getLocation());
                    oldCamera.setCctvGubun(listItem.getCctvGubun());
                    oldCamera.setAnnox(listItem.getAnnox());
                    oldCamera.setAnnoy(listItem.getAnnoy());
                }
                else { // 기존에 없으면 추가
                    if(i % 50 == 0) log.info("camera is new {}, {}", i , listItem);
                    newList.add(listItem);
                }
            }

            if(newList.size() > 0) cameraRepository.saveAll(newList);
            return list.size();
        }
        catch (Exception e) {
            e.printStackTrace();
            return list.size();
        }
    }

    public void test() {
        gisRepository.dummy();
    }

    public String getCode(String code) {
        return gisRepository.getCode(code);
    }

    public String getCodeGroup(String codeGroup) {
        return gisRepository.getCodeGroup(codeGroup);
    }

    public List<Camera> getCameras() {
        return gisRepository.getAllCameras();
    }
    public String getCamera(String code) {
        return gisRepository.getCamera(code);
    }

    public String printTable(String table) {
        return gisRepository.printTable(table);
    }
}
