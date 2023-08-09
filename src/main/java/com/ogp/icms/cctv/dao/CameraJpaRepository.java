package com.ogp.icms.cctv.dao;

import com.ogp.icms.cctv.domain.Camera;
import com.ogp.icms.cctv.request.CameraSearchCondition;
import com.ogp.icms.dailyreport.domain.Monitoring;
import com.ogp.icms.global.repository.support.Querydsl4RepositorySupport;
import com.ogp.icms.global.util.ResultCode;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.HashMap;
import java.util.List;

import static com.ogp.icms.cctv.domain.QCamera.camera;
import static org.springframework.util.StringUtils.hasText;

@Slf4j
@Repository
public class CameraJpaRepository extends Querydsl4RepositorySupport {

    public CameraJpaRepository() {
        super(Camera.class);
    }

    /**
     * 검색과 페이징(JPQL)
     * @// TODO: 2022-06-28 나중에 queryDsl 같은것으로 변경 
     */
    public List<Camera> findByQuery(CameraSearchCondition searchCondition, Pageable pageable) {
        JPAQuery<Camera> query = selectFrom(camera)
                .where(cctvIndexEq(searchCondition.getIndex()),
                        jusoContains(searchCondition.getJuso()),
                        cctvGubunEq(searchCondition.getCctvGubun()),
                        installDateEq(searchCondition.getYmd()));

        if(pageable.getPageSize() > 10) {
            query.orderBy(camera.id.desc());
        }
        else {
            query.orderBy(camera.cctvGubun.desc());
        }

        List<Camera> list = getQuerydsl().applyPagination(pageable, query).fetch();

        return list;
    }


    public Camera save(Camera camera) {
        getEntityManager().persist(camera);
        return camera;
    }

    /**
     * 카메라 수정
     * @param camera
     */
    public void updateCamera(Camera camera) {
        Camera savedCamera = getEntityManager().find(Camera.class, camera.getId());
        if(savedCamera == null) return;

        savedCamera.setCctvGubun(camera.getCctvGubun());
        savedCamera.setDept(camera.getDept());
        savedCamera.setJuso(camera.getJuso());
        savedCamera.setCctvIndex(camera.getCctvIndex());
        savedCamera.setLocation(camera.getLocation());

        savedCamera.setCameraCategory(camera.getCameraCategory());
        savedCamera.setMovement(camera.getMovement());
        savedCamera.setNightvision(camera.getNightvision());
        savedCamera.setShage(camera.getShage());
        savedCamera.setInstallymd(camera.getInstallymd());
        savedCamera.setManufacturer(camera.getManufacturer());

        savedCamera.setModel(camera.getModel());
        savedCamera.setPixel(camera.getPixel());
        savedCamera.setConnectCnt(camera.getConnectCnt());
        savedCamera.setCameraCnt(camera.getCameraCnt());
        savedCamera.setIntegrationCnt(camera.getIntegrationCnt());

        savedCamera.setConnectType(camera.getConnectType());
        savedCamera.setConnectIp(camera.getConnectIp());
        savedCamera.setConnectPort(camera.getConnectPort());
        savedCamera.setConnectId(camera.getConnectId());
        savedCamera.setConnectPw(camera.getConnectPw());
        savedCamera.setConnectModel(camera.getConnectModel());
        savedCamera.setConnectServerType(camera.getConnectServerType());

        savedCamera.setSmCompany(camera.getSmCompany());
        savedCamera.setSmPerson(camera.getSmPerson());
        savedCamera.setSmTel(camera.getSmTel());

        savedCamera.setPtzUseyn(camera.getPtzUseyn());
        savedCamera.setPresetUseyn(camera.getPresetUseyn());

        savedCamera.setFallCamera(camera.getFallCamera());
        savedCamera.setFallDefinition(camera.getFallDefinition());
        savedCamera.setFallEquipment(camera.getFallEquipment());
        savedCamera.setFallNetwork(camera.getFallNetwork());
    }

    private BooleanExpression cctvIndexEq(String cctvIndex) {
        return !hasText(cctvIndex) ? null : camera.cctvIndex.contains(cctvIndex);
    }
    private BooleanExpression jusoContains(String juso) {
        return !hasText(juso) ? null : camera.juso.contains(juso);
    }
    private BooleanExpression cctvGubunEq(String data) {
        return !hasText(data) ? null : camera.cctvGubun.endsWith(data);
    }
    private BooleanExpression installDateEq(String data) {
        return !hasText(data) ? null : camera.installymd.eq(data);
    }
}
