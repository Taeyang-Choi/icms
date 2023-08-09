package com.ogp.icms.cctv.dao;

import com.ogp.icms.cctv.domain.Camera;
import com.ogp.icms.cctv.domain.CameraError;
import com.ogp.icms.cctv.request.CameraErrorSearchCondition;
import com.ogp.icms.cctv.request.CameraSearchCondition;
import com.ogp.icms.global.repository.support.Querydsl4RepositorySupport;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

import static com.ogp.icms.cctv.domain.QCamera.camera;
import static com.ogp.icms.cctv.domain.QCameraError.cameraError;
import static com.ogp.icms.dailyreport.domain.QDailyReport.dailyReport;
import static com.ogp.icms.dailyreport.domain.QMonitoring.monitoring;
import static org.springframework.util.StringUtils.hasText;

@Repository
@Slf4j
public class CameraErrorJpaRepository extends Querydsl4RepositorySupport {

    public CameraErrorJpaRepository() {
        super(CameraError.class);
    }

    public Page<CameraError> findByQuery(CameraErrorSearchCondition searchCondition, Pageable pageable) {
        return applyPagination(pageable, query -> query
            .selectFrom(cameraError)
            .where(cctvIndexEq(searchCondition.getIndex()),
                    dataEq("{}"),
                    jusoContains(searchCondition.getJuso()),
                    ymdEq(searchCondition.getYmd()),
                    cctvGubunEq(searchCondition.getCctvGubun()))
            .orderBy(cameraError.id.desc())
        );
    }

    private BooleanExpression ymdEq(String ymd) {
        return !hasText(ymd) ? null : cameraError.ymd.eq(ymd);
    }
    private BooleanExpression dataEq(String text) {
        return !hasText(text) ? null : cameraError.data.ne(text);
    }
    private BooleanExpression cctvIndexEq(String cctvIndex) {
        return !hasText(cctvIndex) ? null : cameraError.cctvIndex.contains(cctvIndex);
    }
    private BooleanExpression jusoContains(String juso) {
        return !hasText(juso) ? null : cameraError.juso.contains(juso);
    }
    private BooleanExpression cctvGubunEq(String data) {
        return !hasText(data) ? null : cameraError.cctvGubun.eq(data);
    }
}
