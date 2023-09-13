package com.ogp.icms.asset.dao;

import com.ogp.icms.asset.domain.Asset2;
import com.ogp.icms.asset.request.Asset2SearchCondition;
import com.ogp.icms.cctv.dao.CameraRepository;
import com.ogp.icms.cctv.domain.Camera;
import com.ogp.icms.cctv.request.CameraSearchCondition;
import com.ogp.icms.global.repository.support.Querydsl4RepositorySupport;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import static com.ogp.icms.asset.domain.QAsset2.asset2;
import static com.ogp.icms.cctv.domain.QCameraError.cameraError;
import static org.springframework.util.StringUtils.hasText;

import java.util.List;

@Slf4j
@Repository
public class Asset2JpaRepository extends Querydsl4RepositorySupport {
    public Asset2JpaRepository() {
        super(Asset2.class);
    }

    public List<Asset2> findByQuery(Asset2SearchCondition searchCondition, Pageable pageable) {
        //System.out.println(searchCondition.getName() + ", " + searchCondition.getAddress());
        JPAQuery<Asset2> query = selectFrom(asset2)
                .where(nameContains(searchCondition.getName()),
                        addressContains(searchCondition.getAddress()),
                        purposeEq(searchCondition.getPurpose()));
        /*
                        cctvGubunEq(searchCondition.getCctvGubun()),
                        installDateEq(searchCondition.getYmd()));*/

        query.orderBy(asset2.purpose.desc());
        List<Asset2> list = getQuerydsl().applyPagination(pageable, query).fetch();
        //System.out.println(list.size() + ", "  + list);

        return list;
    }

    /**
     * 카메라 수정
     */
    public void updateCamera(Asset2 asset2) {
        Asset2 savedCamera = getEntityManager().find(Asset2.class, asset2.getName());
        if(savedCamera == null) return;

        JSONObject savedJsonobj = null, jsonobj = null;
        try {
            JSONParser parser = new JSONParser();
            savedJsonobj = (JSONObject) parser.parse(savedCamera.getJsonobj());
            jsonobj = (JSONObject) parser.parse(asset2.getJsonobj());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(jsonobj == null || savedJsonobj == null) return;

        savedCamera.setPurpose(asset2.getPurpose());
        savedCamera.setType(asset2.getType());
        savedCamera.setDept(asset2.getDept());
        savedCamera.setProjectName(asset2.getProjectName());
        savedCamera.setProjectType(asset2.getProjectType());
        savedCamera.setAssetId(asset2.getAssetId());

        // vms 연계
        savedCamera.setVmsId(asset2.getVmsId());
        savedJsonobj.put("manufacture", jsonobj.get("manufacture").toString());
        savedJsonobj.put("model", jsonobj.get("model").toString());
        savedJsonobj.put("ip", jsonobj.get("ip").toString());
        savedJsonobj.put("id", jsonobj.get("id").toString());
        savedJsonobj.put("pass", jsonobj.get("pass").toString());

        // 주소
        savedCamera.setEmd(asset2.getEmd());
        savedCamera.setLi(asset2.getLi());
        savedCamera.setTown(asset2.getTown());
        savedCamera.setAddress(asset2.getAddress());

        // gis 연계
        savedCamera.setRefId(asset2.getRefId());
        savedJsonobj.put("direction", jsonobj.get("direction").toString());
        savedCamera.setDirection(asset2.getDirection());
        savedCamera.setLat(asset2.getLat());
        savedCamera.setLng(asset2.getLng());

        // 카메라
        savedJsonobj.put("cctvmanufacture", jsonobj.get("cctvmanufacture").toString());
        savedJsonobj.put("cctvmodel", jsonobj.get("cctvmodel").toString());
        savedJsonobj.put("digital", jsonobj.get("digital").toString());
        savedJsonobj.put("rotation", jsonobj.get("rotation").toString());
        savedJsonobj.put("form", jsonobj.get("form").toString());
        savedJsonobj.put("pixel", jsonobj.get("pixel").toString());
        savedJsonobj.put("price", jsonobj.get("price").toString());
        savedJsonobj.put("screen", jsonobj.get("screen").toString());
        savedJsonobj.put("fdate", jsonobj.get("fdate").toString());

        //
        savedJsonobj.put("pole", jsonobj.get("pole").toString());

        // 유지보수
        savedJsonobj.put("company", jsonobj.get("company").toString());
        savedJsonobj.put("manager", jsonobj.get("manager").toString());
        savedJsonobj.put("phone", jsonobj.get("phone").toString());
        savedJsonobj.put("insdate", jsonobj.get("insdate").toString());
        savedJsonobj.put("mtnend", jsonobj.get("mtnend").toString());
        savedJsonobj.put("mtn", jsonobj.get("mtn").toString());

        // 통신회선
        savedJsonobj.put("agency", jsonobj.get("agency").toString());
        savedJsonobj.put("exten", jsonobj.get("exten").toString());
        savedJsonobj.put("agencynum", jsonobj.get("agencynum").toString());
        savedJsonobj.put("agencyip", jsonobj.get("agencyip").toString());
        savedJsonobj.put("agencyins", jsonobj.get("agencyins").toString());
        savedJsonobj.put("renew", jsonobj.get("renew").toString());
        savedJsonobj.put("agencyend", jsonobj.get("agencyend").toString());

        // 전기인입
        savedJsonobj.put("electnum", jsonobj.get("electnum").toString());
        savedJsonobj.put("electext", jsonobj.get("electext").toString());

        // 기타기기
        savedJsonobj.put("vendor", jsonobj.get("vendor").toString());
        savedJsonobj.put("etcmoel", jsonobj.get("etcmoel").toString());
        savedJsonobj.put("etcch", jsonobj.get("etcch").toString());
        savedJsonobj.put("etcvolume", jsonobj.get("etcvolume").toString());
        savedJsonobj.put("etcip", jsonobj.get("etcip").toString());
        savedJsonobj.put("etcid", jsonobj.get("etcid").toString());
        savedJsonobj.put("etcpass", jsonobj.get("etcpass").toString());

        savedCamera.setJsonobj(savedJsonobj.toJSONString());
    }

    private BooleanExpression nameContains(String name) {
        return !hasText(name) ? null : asset2.name.contains(name);
    }
    private BooleanExpression addressContains(String address) {
        return !hasText(address) ? null : asset2.address.contains(address);
    }

    private BooleanExpression purposeEq(String purpose) {
        return !hasText(purpose) ? null : asset2.purpose.endsWith(purpose);
    }


}
