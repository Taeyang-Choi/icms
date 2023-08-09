package com.ogp.icms.cctv.dao;


import com.ogp.icms.cctv.domain.CameraLicense;
import com.ogp.icms.global.util.SqlUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class LicenseRepository {
    private final SqlUtil sqlUtil;
    private final EntityManager em;

    public LicenseRepository(SqlUtil sqlUtil, EntityManager em) {
        this.sqlUtil = sqlUtil;
        this.em = em;
    }

    public List<CameraLicense> selectAll() {
        return em.createQuery("select m from camera_license m", CameraLicense.class).getResultList();
    }

    public Optional<CameraLicense> selectByIndex(String index) {
        List<CameraLicense> result = em.createQuery("select m from camera_license m where m.name = :name",
                CameraLicense.class).setParameter("index", index).getResultList();
        return result.stream().findAny();
    }

}
