package com.ogp.icms.dailyreport.dao;

import com.ogp.icms.dailyreport.domain.DailyReport;
import com.ogp.icms.dailyreport.request.DailyReportSearchCondition;
import com.ogp.icms.dailyreport.request.DailyReportSubmitRequest;
import com.ogp.icms.global.repository.support.Querydsl4RepositorySupport;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.ogp.icms.board.domain.QArticle.article;
import static com.ogp.icms.dailyreport.domain.QDailyReport.dailyReport;
import static org.springframework.util.StringUtils.hasText;

@Slf4j
@Repository
public class DailyReportJpaRepository extends Querydsl4RepositorySupport {

    public DailyReportJpaRepository() {
        super(DailyReport.class);
    }

    /**
     * 검색과 페이징(JPQL)
     * @// TODO: 2022-06-28 나중에 queryDsl 같은것으로 변경
     */
    public Page<DailyReport> getPage(DailyReportSearchCondition searchCondition, Pageable pageable) {
        return applyPagination(pageable, query -> query
            .selectFrom(dailyReport)
            .where(usernameContains(searchCondition.getUsername()),
                    dateEq(searchCondition.getWorkDateFrom()),
                    useridEq(searchCondition.getUserid()))
            .orderBy(dailyReport.id.desc())
        );
    }

    /**
     * 수정 기능
     */
    public void submitDailyReport(DailyReportSubmitRequest submitRequest) {
        DailyReport savedDailyReport = findById(submitRequest.getId()).get();

        savedDailyReport.setSubmitUserid(submitRequest.getSubmitUserid());
        savedDailyReport.setSubmitDate(LocalDateTime.now());
        savedDailyReport.setStatus(1L);

        getEntityManager().flush();
        getEntityManager().clear();
    }

    public Optional<DailyReport> findById(Long id) {
        DailyReport member = getEntityManager().find(DailyReport.class, id);
        return Optional.ofNullable(member);
    }

    private BooleanExpression usernameEq(String username) {
        return !hasText(username) ? null : dailyReport.username.eq(username);
    }
    private BooleanExpression usernameContains(String username) {
        return !hasText(username) ? null : dailyReport.username.contains(username);
    }
    private BooleanExpression dateEq(String workDateFrom) {
        return !hasText(workDateFrom) ? null : dailyReport.workDateFrom.eq(workDateFrom.replaceAll("-", ""));
    }
    private BooleanExpression useridEq(String userid) {
        return !hasText(userid) ? null : dailyReport.userid.eq(userid);
    }

}
