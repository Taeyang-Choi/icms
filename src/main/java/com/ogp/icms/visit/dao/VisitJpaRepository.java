package com.ogp.icms.visit.dao;

import com.ogp.icms.visit.domain.Visiting;
import com.ogp.icms.visit.domain.VisitingMonthlyCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VisitJpaRepository extends JpaRepository<Visiting, Long> {
    List<Visiting> findByStatus(String n);

    @Query("SELECT new com.ogp.icms.visit.domain.VisitingMonthlyCount(YEAR(v.createdAt), MONTH(v.createdAt), COUNT(v)) FROM Visiting v" +
            " GROUP BY YEAR(v.createdAt), MONTH(v.createdAt)")
    List<VisitingMonthlyCount> countAllByMonthly();
}
