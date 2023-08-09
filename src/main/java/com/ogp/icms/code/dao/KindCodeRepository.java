package com.ogp.icms.code.dao;

import com.ogp.icms.code.domain.KindCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KindCodeRepository extends JpaRepository<KindCode, Long> {
}
