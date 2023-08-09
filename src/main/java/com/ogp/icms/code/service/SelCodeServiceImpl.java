package com.ogp.icms.code.service;

import com.ogp.icms.code.dao.SelCodeRepository;
import com.ogp.icms.code.domain.SelCode;
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
public class SelCodeServiceImpl {
    private final SelCodeRepository selCodeRepository;

    public ResultCode save(SelCode selCode) {
        selCodeRepository.save(selCode);
        return new ResultCode(0, "새로운 코드를 추가하였습니다.");
    }

    public List<SelCode> getAll() {
        return selCodeRepository.findAll();
    }

    public SelCode getById(Long id) {
        return selCodeRepository.getById(id);
    }
    public SelCode getByName(String name) {
        return selCodeRepository.getByName(name);
    }

    /**
     * 코드 수정
     * @TODO 하위 코드가 있을 경우엔 수정 못하게 해야할것들이 있음.
     */
    public ResultCode modifySelCode(SelCode newCode, Long originalId) {
        log.info("{}", originalId);
        log.info("{}", newCode);
        SelCode old = selCodeRepository.getById(originalId);

        // 기본 코드가 아니라면
        old.setCode(newCode.getCode());
        old.setName(newCode.getName());
        old.setActive(newCode.getActive());
        old.setSeq(newCode.getSeq());
        old.setRemarks(newCode.getRemarks());

        return new ResultCode(0, "코드를 수정였습니다.");
    }

    /**
     * @TODO 하위 코드 있을 시 삭제 불가능하게.
     * @param id
     * @return
     */
    public ResultCode deleteById(Long id) {
        selCodeRepository.deleteById(id);

        return new ResultCode(0, "코드를 삭제 했습니다.");
    }

    public List<SelCode> getSelCodesByCode(String kindCode) {
        return selCodeRepository.getByKindCode(kindCode);
    }
}
