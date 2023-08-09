package com.ogp.icms.asset.service;

import com.ogp.icms.asset.dao.AssetJpaRepository;
import com.ogp.icms.asset.domain.Asset;
import com.ogp.icms.global.util.ResultCode;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class AssetService {
    private final AssetJpaRepository assetJpaRepository;

    public List<Asset> getAll() {
        return assetJpaRepository.findAll();
    }

    public ResultCode upload(ArrayList<Asset> assetList) {
        assetJpaRepository.deleteAll();

        for (Asset asset: assetList) {
            log.info("{}", asset);
        }

        assetJpaRepository.saveAllAndFlush(assetList);
        return new ResultCode(0,"업로드 했습니다.");
    }
}
