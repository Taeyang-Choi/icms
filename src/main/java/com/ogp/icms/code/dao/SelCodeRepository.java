package com.ogp.icms.code.dao;

import com.ogp.icms.code.domain.SelCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SelCodeRepository extends JpaRepository<SelCode, Long> {
    List<SelCode> getByKindCode(String kindCode);

    SelCode getByName(String name);
}
