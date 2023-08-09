package com.ogp.icms.code.service;

import com.ogp.icms.code.dao.KindCodeRepository;
import com.ogp.icms.code.domain.KindCode;
import com.ogp.icms.global.util.ResultCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class KindCodeServiceImpl {
    private final KindCodeRepository kindCodeRepository;

    public ResultCode save(KindCode kindCode) {
        kindCode.setEssential(false);
        kindCodeRepository.save(kindCode);
        return new ResultCode(0, "새로운 코드 종류를 추가하였습니다.");
    }

    public List<KindCode> getAll() {
        return kindCodeRepository.findAll();
    }

    /**
     * 코드 수정
     * @TODO 하위 코드가 있을 경우엔 수정 못하게 해야할것들이 있음.
     */
    public ResultCode modifyKindCode(KindCode newCode, Long originalId) {
        KindCode old = kindCodeRepository.getById(originalId);

        // 기본 코드가 아니라면
        if(!old.getEssential()) {
            old.setName(newCode.getName());
            old.setCode(newCode.getCode());
            old.setActive(newCode.getActive());
            old.setCodeFormat(newCode.getCodeFormat());
        }
        old.setSeq(newCode.getSeq());

        return new ResultCode(0, "코드 종류를 수정였습니다.");
    }

    /**
     * @TODO 하위 코드 있을 시 삭제 불가능하게.
     * @param id
     * @return
     */
    public ResultCode deleteById(Long id) {
        KindCode old = kindCodeRepository.getById(id);
        if(old.getEssential()) {
            return new ResultCode(-1, "해당 코드는 시스템 필수 코드입니다.");
        }
        else {
            // 하위 코드 없을 시
            kindCodeRepository.deleteById(id);
            return new ResultCode(0, "코드 종류를 삭제 했습니다.");
        }
    }
}
