package com.ogp.icms.asset.dao;

import com.ogp.icms.asset.domain.Asset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssetJpaRepository extends JpaRepository<Asset, String> {

}
