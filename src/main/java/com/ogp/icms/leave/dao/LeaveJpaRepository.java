package com.ogp.icms.leave.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ogp.icms.global.repository.support.Querydsl4RepositorySupport;
import com.ogp.icms.leave.domain.Leave;
import com.ogp.icms.leave.request.LeaveSearchCondition;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import static com.ogp.icms.board.domain.QArticle.article;
import static com.ogp.icms.leave.domain.QLeave.leave;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Slf4j
@Repository
public class LeaveJpaRepository  extends Querydsl4RepositorySupport {
    private final ObjectMapper objectMapper;

    public LeaveJpaRepository() {
        super(Leave.class);
        objectMapper = new ObjectMapper();
    }

    public Page<Leave> findByQuery(LeaveSearchCondition leaveSearchCondition, Pageable pageable) {

        return applyPagination(pageable, query -> query
                .selectFrom(leave)
                .where(dateBetween(leaveSearchCondition.getSearchStartDate(), leaveSearchCondition.getSearchEndDate()))
        );
    }

    private BooleanExpression dateBetween(LocalDate start, LocalDate end) {
//        log.info("{}", start.atStartOfDay());
//        log.info("{}", LocalDateTime.of(end, LocalTime.MAX).withNano(0));

        return (start == null || end == null)? null : leave.startDate.between(start.atStartOfDay() , LocalDateTime.of(end, LocalTime.MAX).withNano(0));

        //return article.noticeEdate.after(Expressions.dateTemplate(LocalDateTime.class, "function('current_date')"));
    }
}
