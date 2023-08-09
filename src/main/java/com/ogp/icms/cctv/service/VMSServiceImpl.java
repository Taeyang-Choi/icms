package com.ogp.icms.cctv.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ogp.icms.cctv.dao.CameraRepository;
import com.ogp.icms.cctv.domain.Camera;
import com.ogp.icms.code.domain.SelCode;
import com.ogp.icms.code.service.SelCodeServiceImpl;
import com.ogp.icms.global.util.HttpClientTool;
import com.ogp.icms.member.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Transactional
@Slf4j
@Service
public class VMSServiceImpl {
    public final int LOGIN_FAILED = -101;

    @Value("${vms.url}")
    private String IP = "http://211.171.190.220:16118";
    @Value("${vms.id}")
    private String id = "sdk";
    @Value("${vms.pw}")
    private String pw = "Innodep1@";
    private String profile = null;
    private final CameraRepository cameraRepository;
    private final SelCodeServiceImpl selCodeService;
    private HttpURLConnection con = null;
    private ObjectMapper objectMapper = new ObjectMapper();

    public VMSServiceImpl(CameraRepository cameraRepository, SelCodeServiceImpl selCodeService) {
        this.cameraRepository = cameraRepository;
        this.selCodeService = selCodeService;
    }

    /**
     * vms 카메라 동기화
     * @return
     */
    public int syncDeviceList() {
        JsonNode info, data = null;
        int counter = 0;

        profile = System.getProperty("spring.profiles.active");

        //1. 로그인
        logout();
        info = login();
        boolean loginSuccess = info.get("success").asBoolean();
        log.info("{}", info);
        if(loginSuccess) {
            info = info.get("results");
            SelCode vmsInfo = selCodeService.getByName("vms토큰");
            vmsInfo.setRemarks(info.get("auth_token").asText());
        }
        else {
            return LOGIN_FAILED;
        }

        //2. 장비 목록 가져오기
        data = getDeviceList(info);

        //3. 장비 정보 가져오기
        counter += getDeviceInfo(info, data);
        cameraRepository.flush();

        return counter;
    }

    /**
     * VMS 서비스에 로그인
     */
    public JsonNode login() {
        try {
            String result = null;

            // 1. declare
            String url = IP + "/api/login?force-login=true";

            con = (HttpURLConnection) (new URL(url)).openConnection();
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setRequestMethod("GET");
            con.setRequestProperty("x-account-id", id);
            con.setRequestProperty("x-account-pass", pw);
            con.setRequestProperty("x-account-group", "group1");
            con.setRequestProperty("x-license", "licNormalClient");
            result = new HttpClientTool().apiCall("VMSServiceImpl.login", con);

            JsonNode jsonNode = objectMapper.readTree(result);
            return jsonNode;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void logout() {
        SelCode selCode = selCodeService.getByName("vms토큰");
        String token = selCode.getRemarks();
        if(token.length() == 2) return;
        selCode.setRemarks("XX");
        try {
            String url = IP + "/api/logout";

            con = (HttpURLConnection) (new URL(url)).openConnection();
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setRequestMethod("DELETE");
            con.setRequestProperty("x-auth-token", token);
            new HttpClientTool().apiCall("VMSServiceImpl.logout", con);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 5.5 사용자 장비(카메라) 목록 조회
     * @param info
     * @return
     */
    public JsonNode getDeviceList(JsonNode info) {
        try {
            String result = null;

            // 1. declare
            String userSerial = info.get("user_serial").asText();
            String url = IP + "/api/device/list/"+userSerial+"/"+0+"?dev_only=true";

            con = (HttpURLConnection) (new URL(url)).openConnection();
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setRequestMethod("GET");
            con.setRequestProperty("x-auth-token", info.get("auth_token").asText());
            con.setRequestProperty("x-api-serial", (info.get("api_serial").asInt()+1)+"");
            result = new HttpClientTool().apiCall("VMSServiceImpl.login", con);
            JsonNode jsonNode = objectMapper.readTree(result);

            if(jsonNode.has("results")) {
                jsonNode = jsonNode.get("results").get("tree");
            }

            return jsonNode;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 카메라 리스트의 각 카메라 정보를 가져온다.
     * @param info 로그인 정보
     * @param list 카메라 리스트
     */
    public int getDeviceInfo(JsonNode info, JsonNode list) {
        try {
            if(list.size() > 1) {
                log.info("VMSServiceImpl.getDeviceInfo - {} ~ {}",
                        list.get(0).get("dev_serial"),
                        list.get(list.size() - 1).get("dev_serial"));
            }
            else {
                log.info("not has dev_serial - {}", list.get(0));
                return 0;
            }

            // 1. declare
            String url = IP + "/api/device/detail-infos?dev_serial_list=[";
            // marshaling
            for(int i = 0; i < list.size(); i++) {
                url += list.get(i).get("dev_serial").asText();

                if(i != 0 && i % 50 == 0) {
                    url += "]";
                    log.info("{}", url);
                    con = (HttpURLConnection) (new URL(url)).openConnection();
                    con.setDoInput(true);
                    con.setDoOutput(true);
                    con.setRequestMethod("GET");
                    con.setRequestProperty("x-auth-token", info.get("auth_token").asText());
                    con.setRequestProperty("x-api-serial", (info.get("api_serial").asInt()+(i/50))+"");
                    String result = new HttpClientTool().apiCall("VMSServiceImpl.getDeviceInfo", con);
                    //log.info("results - {}", result);
                    JsonNode jsonNode = objectMapper.readTree(result).get("results");

                    //4. save
                    saveCameras(jsonNode);
                    cameraRepository.flush();
                    url = IP + "/api/device/detail-infos?dev_serial_list=[";
                }
                else {
                    url += ",";
                }
            }
            return list.size();
        }
        catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public JsonNode getOne(JsonNode info, String id) {
        try {
            // 1. declare
            String url = IP + "/api/device/detail-infos?dev_serial_list=[" + id + "]";
            con = (HttpURLConnection) (new URL(url)).openConnection();
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setRequestMethod("GET");
            con.setRequestProperty("x-auth-token", info.get("auth_token").asText());
            con.setRequestProperty("x-api-serial", (info.get("api_serial").asInt()+1)+"");
            String result = new HttpClientTool().apiCall("VMSServiceImpl.getDeviceInfo", con);
            //log.info("results - {}", result);
            JsonNode jsonNode = objectMapper.readTree(result).get("results");

            //4. save
            saveCameras(jsonNode);
            cameraRepository.flush();
            return jsonNode;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return objectMapper.createObjectNode();
    }

    /**
     * VMS 카메라 리스트 JSON을 Camera 도메인으로 마샬링
     * @TODO - 카메라 이미 있으면의 처리
     * @param list 카메라 리스트
     */
    public int saveCameras(JsonNode list) {
        int count = 0;
        List<Camera> cameras = new ArrayList<Camera>();

        try {
            for(int i = 0; i < list.size(); i++) {
                JsonNode node = list.get(i);
                //log.info("fetch camera from vms server - {}({})", node.get("dev_name").asText(), node.get("dev_serial").asText());
                Camera camera = convertToCamera(node);

                Optional<Camera> cameraByIdOptional = cameraRepository.findById(camera.getId());

                if (cameraByIdOptional.isPresent()) { // 기존에 없으면 추가
                    if(i % 50 == 0) log.info("vms camera is exist {}, {}", i , camera);
                    Camera oldCamera = cameraByIdOptional.get();
                    oldCamera.setManufacturer(camera.getManufacturer());
                    oldCamera.setModel(camera.getModel());
                    oldCamera.setConnectIp(camera.getConnectIp());
                    oldCamera.setConnectId(camera.getConnectId());
                    oldCamera.setConnectPw(camera.getConnectPw());
                }
                else {
                    if(i % 50 == 0) log.info("vms camera is new {}, {}", i , camera);
                    cameras.add(camera);
                    count++;
                }
            }
            cameraRepository.saveAllAndFlush(cameras);
        }
        catch (Exception e) {
            log.info("{}", e.getLocalizedMessage());
        }

        return count;
    }

    /**
     * Camera 도메인으로 변환
     * @param item
     * @return
     */
    public Camera convertToCamera(JsonNode item) {
        Camera camera = new Camera();
        camera.setCctvGubun("");
        camera.setDept("");

        // 인덱스
        camera.setId(item.get("dev_serial").asLong());
        camera.setCctvIndex(item.get("dev_name").asText());

        // 주소
        if(item.has("dev_property")) {
            JsonNode devProperty = item.get("dev_property");

            camera.setJuso(devProperty.get("location").get("primary").asText());
            camera.setLocation(devProperty.get("location").get("primary").asText());
        }

        camera.setCameraCategory("");
        camera.setMovement("");
        camera.setNightvision("");
        camera.setShage("");
        camera.setInstallymd("");

        camera.setModel(item.get("model_name").asText());

        // 모델 정보
        if(item.has("model_property")) {
            JsonNode modelProperty = item.get("model_property");
            camera.setManufacturer(modelProperty.get("company").asText());

            camera.setConnectId(modelProperty.get("user").asText());
            camera.setConnectPw(modelProperty.get("pass").asText());
        }

        // 장비 채널 정보
        if(item.has("dch")) {
            JsonNode deviceChannel = item.get("dch").get(0);// 미디어 정보

            if(item.has("med")) {
                JsonNode mediaProperty = deviceChannel.get("med").get(0);
                camera.setPtzUseyn(mediaProperty.get("ptz_enabled").asText());

                camera.setPixel(mediaProperty.get("dchm_width").asInt() * mediaProperty.get("dchm_height").asInt() + "");
            }
        }

        // 접속정보
        camera.setConnectType("");
        camera.setConnectIp(item.get("dev_addr").asText());
        camera.setConnectPort(item.get("dev_wport").asText());
        camera.setConnectModel("");
        camera.setConnectServerType("");

        return camera;
    }

    public Camera convertToCameraV2(JsonNode item) {
        Camera camera = new Camera();

        camera.setId(item.get("dev_serial").asLong());
        camera.setCctvIndex(item.get("dev_name").asText());
        camera.setConnectIp(item.get("dev_addr").asText());

        return camera;
    }

    /**
     * 카메라 수동 동기화
     * @param id
     */
    public JsonNode manualSync(String id) {
        JsonNode info, data = null;
        int counter = 0;

        profile = System.getProperty("spring.profiles.active");

        //1. 로그인
        logout();
        info = login();
        boolean loginSuccess = info.get("success").asBoolean();
        log.info("{}", info);
        if(loginSuccess) {
            info = info.get("results");
            SelCode vmsInfo = selCodeService.getByName("vms토큰");
            vmsInfo.setRemarks(info.get("auth_token").asText());
        }
        else {
            return info;
        }

        //2. 장비 정보 가져오기
        return getOne(info, id);
    }
}

