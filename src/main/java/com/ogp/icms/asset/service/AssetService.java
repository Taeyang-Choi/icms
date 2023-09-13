package com.ogp.icms.asset.service;

import com.ogp.icms.asset.dao.Asset2JpaRepository;
import com.ogp.icms.asset.dao.Asset2Repository;
import com.ogp.icms.asset.dao.AssetJpaRepository;
import com.ogp.icms.asset.domain.Asset;
import com.ogp.icms.asset.domain.Asset2;
import com.ogp.icms.asset.request.Asset2SearchCondition;
import com.ogp.icms.cctv.api.CameraApiController;
import com.ogp.icms.cctv.dao.CameraRepository;
import com.ogp.icms.cctv.domain.Camera;
import com.ogp.icms.cctv.request.CameraSearchCondition;
import com.ogp.icms.cctv.service.CameraErrorServiceImpl;
import com.ogp.icms.gis.repository.GisRepository;
import com.ogp.icms.global.util.ResultCode;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.locationtech.proj4j.BasicCoordinateTransform;
import org.locationtech.proj4j.CRSFactory;
import org.locationtech.proj4j.CoordinateReferenceSystem;
import org.locationtech.proj4j.ProjCoordinate;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class AssetService {

    private final CameraErrorServiceImpl cameraErrorService;

    private final AssetJpaRepository assetJpaRepository;
    private final Asset2Repository asset2Repository;
    private final Asset2JpaRepository asset2JpaRepository;

    private final CameraRepository cameraRepository;

    public List<Asset> getAll() {
        return assetJpaRepository.findAll();
    }

    public ResultCode upload(ArrayList<Asset> assetList) {
        assetJpaRepository.deleteAll();

        for (Asset asset: assetList) {
            log.info("{}", asset);
        }

        assetJpaRepository.saveAllAndFlush(assetList);
        return new ResultCode(0,"업로드 했습니다.");
    }

    // asset2

    public List<Asset2> getAll2() {
        return asset2Repository.findAll();
    }

    public List<Asset2> getAsset2Page(Asset2SearchCondition searchCondition, Pageable pageable) {
        int page = pageable.getPageNumber()-1;
        PageRequest newPageRequest = PageRequest.of((page <= 0) ? 0 : page, 10);
        return asset2JpaRepository.findByQuery(searchCondition, newPageRequest);
    }

    public int updateDataBase() {
        List<Camera> cameraList = cameraRepository.findAll();
        List<Asset2> newList = new ArrayList<>();

        try {
            for (int i=0; i<cameraList.size(); i++) {
                Camera camera = cameraList.get(i);
                Optional<Asset2> asset2ByIdOptional = asset2Repository.findById(camera.getCctvIndex());
                Asset2 item;
                if (asset2ByIdOptional.isPresent()) {
                    item = asset2ByIdOptional.get();
                } else {
                    item = new Asset2();
                }

                // primary key값
                item.setName(camera.getCctvIndex());

                item.setPurpose(camera.getCctvGubun());
                item.setVmsId(camera.getId().toString());
                item.setAddress(camera.getJuso());
                item.setRefId(camera.getId().toString());
                item.setLat(camera.getLat());
                item.setLng(camera.getLng());
                item.setDirection(camera.getDirection());

                JSONObject jsonobj = new JSONObject();
                jsonobj.put("manufacture", camera.getManufacturer());
                jsonobj.put("model", camera.getModel());
                jsonobj.put("ip", camera.getConnectIp());
                jsonobj.put("id", camera.getConnectId());
                jsonobj.put("pass", camera.getConnectPw());
                jsonobj.put("direction", camera.getDirection());
                jsonobj.put("cctvmanufacture", camera.getManufacturer());
                jsonobj.put("cctvmodel", camera.getModel());
                jsonobj.put("digital", ""); // 수기
                jsonobj.put("rotation", ""); // 수기
                jsonobj.put("form", ""); // 수기
                jsonobj.put("pixel", ""); // 수기
                jsonobj.put("price", ""); // 수기
                jsonobj.put("screen", ""); // 수기
                jsonobj.put("fdate", ""); // 수기
                jsonobj.put("pole", ""); // 수기
                jsonobj.put("company", ""); // 수기
                jsonobj.put("manager", ""); // 수기
                jsonobj.put("phone", ""); // 수기
                jsonobj.put("insdate", ""); // 수기
                jsonobj.put("mtnend", ""); // 수기
                jsonobj.put("mtn", ""); // 수기
                jsonobj.put("agency", ""); // 수기
                jsonobj.put("exten", ""); // 수기
                jsonobj.put("agencynum", ""); // 수기
                jsonobj.put("agencyip", ""); // 수기
                jsonobj.put("agencyins", ""); // 수기
                jsonobj.put("renew", ""); // 수기
                jsonobj.put("agencyend", ""); // 수기
                jsonobj.put("electnum", ""); // 수기
                jsonobj.put("electext", ""); // 수기
                jsonobj.put("vendor", ""); // 수기
                jsonobj.put("etcmoel", ""); // 수기
                jsonobj.put("etcch", ""); // 수기
                jsonobj.put("etcvolume", ""); // 수기
                jsonobj.put("etcip", ""); // 수기
                jsonobj.put("etcid", ""); // 수기
                jsonobj.put("etcpass", ""); // 수기
                //jsonobj.put("", "");
                item.setJsonobj(jsonobj.toJSONString());

                newList.add(item);
            }

            if(newList.size() > 0) asset2Repository.saveAll(newList);
            return newList.size();


        } catch (Exception e) {
            e.printStackTrace();
            return newList.size();
        }
    }

    /**
     * id로 카메라 반환
     * @param name
     * @return
     */
    public Asset2 findOne(String name) {
        Optional<Asset2> asset2Optional = asset2Repository.findById(name);

        if(asset2Optional.isPresent()) {
            return asset2Optional.get();
        } else {
            return new Asset2();
        }
    }

    public Asset2 findOneByRefId(String id) {
        Optional<Asset2> asset2Optional = asset2Repository.findByRefId(id);
        if(asset2Optional.isPresent()) {
            return asset2Optional.get();
        } else {
            return new Asset2();
        }
    }

    /**
     * asset 카메라 수정
     */
    public ResultCode editCamera(Asset2 asset2) {
        //System.out.println(asset2);
        asset2JpaRepository.updateCamera(asset2);
        cameraErrorService.updateCameraError(Long.parseLong(asset2.getRefId()));
        return new ResultCode(0, "카메라를 수정했습니다.");
    }

    /**
     * 엑셀로 업로드
     */
    public ResultCode uploadCameraList(List<Asset2> list) {
        try {
            //ArrayList<Camera> newList = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                Optional<Asset2> asset2Optional = asset2Repository.findById(list.get(i).getName());
                if (asset2Optional.isPresent()) {
                    asset2Repository.deleteById(asset2Optional.get().getName());
                    asset2Repository.flush();
                }
                //if(byCctvIndex == null) newList.add(list.get(i));
            }
            asset2Repository.saveAll(list);
            return new ResultCode(0, list.size() + "개의 카메라 정보를 업로드 했습니다.");
        } catch(Exception e) {
            return new ResultCode(e);
        }
    }

    public ResultCode deleteCamera(String id) {
        asset2Repository.deleteById(id);
        return new ResultCode(0, "카메라를 삭제했습니다.");
    }

    public ResultCode save(Asset2 asset2) {
        asset2Repository.save(asset2);
        cameraErrorService.updateCameraError(Long.parseLong(asset2.getRefId()));
        return new ResultCode(0, "카메라를 생성했습니다.");
    }

    public Long getAsset2LicenseCount() {
        return asset2Repository.count();
    }
}
