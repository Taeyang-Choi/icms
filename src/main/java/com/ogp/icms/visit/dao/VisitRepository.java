package com.ogp.icms.visit.dao;

import com.ogp.icms.global.repository.support.Querydsl4RepositorySupport;
import com.ogp.icms.visit.domain.Visiting;
import com.ogp.icms.visit.domain.VisitingSearchCondition;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.ogp.icms.visit.domain.QVisiting.visiting;
import static org.springframework.util.StringUtils.hasText;

@Repository
public class VisitRepository extends Querydsl4RepositorySupport {
    public VisitRepository() {
        super(Visiting.class);
    }


    /**
     * 검색과 페이징
     * @TODO search 고도화
     */
    public List<Visiting> findByQuery(Pageable pageable, VisitingSearchCondition search) {
        JPAQuery<Visiting> query = selectFrom(visiting)
                .where(usernameContains(search.getName()),
                        companyContains(search.getCompany()))
                .orderBy(visiting.id.desc());
        List<Visiting> list = getQuerydsl().applyPagination(pageable, query).fetch();

        return list;
    }

    public void clear() {
        getEntityManager().flush();
        getEntityManager().clear();
    }

    /* 검색 조건*/
    private BooleanExpression usernameContains(String username) {
        return !hasText(username) ? null : visiting.username.contains(username);
    }
    private BooleanExpression companyContains(String title) {
        return title == null ? null : visiting.companyName.contains(title);
    }
}
