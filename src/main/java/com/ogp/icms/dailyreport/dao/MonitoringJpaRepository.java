package com.ogp.icms.dailyreport.dao;

import com.ogp.icms.dailyreport.domain.Monitoring;
import com.ogp.icms.dailyreport.request.MonitoringSearchCondition;
import com.ogp.icms.global.repository.support.Querydsl4RepositorySupport;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.HashMap;
import java.util.List;

import static com.ogp.icms.board.domain.QArticle.article;
import static com.ogp.icms.dailyreport.domain.QMonitoring.monitoring;
import static org.springframework.util.StringUtils.hasText;

@Slf4j
@Repository
public class MonitoringJpaRepository extends Querydsl4RepositorySupport {
    public MonitoringJpaRepository() {
        super(Monitoring.class);
    }

    /**
     * 검색과 페이징(JPQL)
     * @// TODO: 2022-06-28 나중에 queryDsl 같은것으로 변경
     */
    public List<Monitoring> getListByReportId(Long reportId) {

        TypedQuery<Monitoring> query = getEntityManager().createQuery("SELECT c FROM Monitoring c" +
                " WHERE c.dailyreportId=:reportId ORDER BY c.id DESC", Monitoring.class);
        query.setParameter("reportId", reportId);
        return query.getResultList();
    }

    /**
     * 검색과 페이징(JPQL)
     */
    public Page<Monitoring> getPage(MonitoringSearchCondition searchCondition, Pageable pageable) {
        return applyPagination(pageable, query -> query
                .selectFrom(monitoring)
                .where(barrierLevelEq(searchCondition.getBarrierLevel()),
                        workDgubunEq(searchCondition.getWorkDgubun()),
                        dateGoe(searchCondition.getStartDate()),
                        situStartWith(searchCondition.getSitu()),
                        actionCodeEq(searchCondition.getActionCode()),
                        dateLoe(searchCondition.getEndDate()),
                        useridContains(searchCondition.getUserid()),
                        cctvIndexContains(searchCondition.getCctvIndex()))
                .orderBy(monitoring.id.desc())
        );
    }
    public Page<Monitoring> getList(MonitoringSearchCondition searchCondition, Pageable pageable) {
        if(searchCondition.getWorkDgubun() != null) {
            if(searchCondition.getWorkDgubun().equals("D01")) searchCondition.setSitu(null);
            else searchCondition.setBarrierLevel(null);
        }

        return applyPagination(pageable, query -> query
                .selectFrom(monitoring)
                .where(barrierLevelEq(searchCondition.getBarrierLevel()),
                        workDgubunEq(searchCondition.getWorkDgubun()),
                        dateGoe(searchCondition.getStartDate()),
                        situStartWith(searchCondition.getSitu()),
                        actionCodeEq(searchCondition.getActionCode()),
                        dateLoe(searchCondition.getEndDate()),
                        useridContains(searchCondition.getUserid()),
                        cctvIndexContains(searchCondition.getCctvIndex()))
                .orderBy(monitoring.id.desc())
        );
    }

    public List<Monitoring> getAllNotRepairedAssets() {
        JPAQuery<Monitoring> query = selectFrom(monitoring)
                .where(workDgubunEq("D01"),actionCodeEq("R02"),
                        dateGoe("20221001"))
                .orderBy(monitoring.id.desc());
        List<Monitoring> list = query.fetch();

        return list;
    }

    public List<Monitoring> getAllSituations() {
        JPAQuery<Monitoring> query = selectFrom(monitoring)
                .where(workDgubunEq("D02"),actionCodeEq("R11"),
                        dateGoe("20221001"))
                .orderBy(monitoring.id.desc());
        List<Monitoring> list = query.fetch();

        return list;
    }

    public List<Monitoring> getList(MonitoringSearchCondition searchCondition) {
        JPAQuery<Monitoring> query = selectFrom(monitoring)
                .where(barrierLevelEq(searchCondition.getBarrierLevel()),
                        workDgubunEq(searchCondition.getWorkDgubun()),
                        dateGoe(searchCondition.getStartDate()),
                        situStartWith(searchCondition.getSitu()),
                        dateLoe(searchCondition.getEndDate()))
                .orderBy(monitoring.id.desc());
        List<Monitoring> list = query.fetch();

        return list;
    }

    public void clear() {
        getEntityManager().flush();
        getEntityManager().clear();
    }

    private BooleanExpression barrierLevelEq(String barrierLevel) {
        return !hasText(barrierLevel) ? null : monitoring.barrierLevel.eq(barrierLevel);
    }
    private BooleanExpression workDgubunEq(String workDgubun) {
        return !hasText(workDgubun) ? null : monitoring.workDgubun.eq(workDgubun);
    }
    private BooleanExpression situStartWith(String situ) {
        return !hasText(situ) ? null : monitoring.barrierLevel.startsWith(situ);
    }
    private BooleanExpression actionCodeEq(String actionCode) {
        return !hasText(actionCode) ? null : monitoring.actionCode.eq(actionCode);
    }
    private BooleanExpression actionCodeNotEq(String actionCode) {
        return !hasText(actionCode) ? null : monitoring.actionCode.ne(actionCode);
    }
    private BooleanExpression dateGoe(String date) {
        return date == null ? null : monitoring.workDate.goe(date);
    }
    private BooleanExpression dateLoe(String date) {
        return date == null ? null : monitoring.workDate.loe(date);
    }
    private BooleanExpression useridContains(String id) { return !hasText(id) ? null : monitoring.userid.contains(id);}
    private BooleanExpression cctvIndexContains(String index) { return !hasText(index) ? null : monitoring.cctvIndex.contains(index);}

}
