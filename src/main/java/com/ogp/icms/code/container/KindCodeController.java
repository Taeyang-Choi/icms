package com.ogp.icms.code.container;

import com.ogp.icms.code.domain.KindCode;
import com.ogp.icms.code.domain.SelCode;
import com.ogp.icms.code.service.KindCodeServiceImpl;
import com.ogp.icms.global.util.ResultCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class KindCodeController {
    private final KindCodeServiceImpl kindCodeService;

    @PostMapping("/api/kind-codes")
    public ResultCode saveKindCode(@ModelAttribute KindCode kindCode) {
        return kindCodeService.save(kindCode);
    }

    @GetMapping("/api/kind-codes")
    public List<KindCode> getAll() {
        return kindCodeService.getAll();
    }


    @PutMapping("/api/kind-codes/{id}")
    public ResultCode putCode(@ModelAttribute KindCode kindCode, @PathVariable Long id) {
        return kindCodeService.modifyKindCode(kindCode, id);
    }

    @DeleteMapping("/api/kind-codes/{id}")
    public ResultCode deleteCode(@PathVariable Long id) {
        return kindCodeService.deleteById(id);
    }

}
