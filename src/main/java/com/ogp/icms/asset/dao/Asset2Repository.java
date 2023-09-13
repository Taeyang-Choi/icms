package com.ogp.icms.asset.dao;

import com.ogp.icms.asset.domain.Asset2;
import com.ogp.icms.cctv.domain.Camera;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface Asset2Repository extends JpaRepository<Asset2, String> {
    Optional<Asset2> findByRefId(String refId);
}
