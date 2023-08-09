package com.ogp.icms.code.container;

import com.ogp.icms.code.domain.KindCode;
import com.ogp.icms.code.domain.SelCode;
import com.ogp.icms.code.service.KindCodeServiceImpl;
import com.ogp.icms.code.service.SelCodeServiceImpl;
import com.ogp.icms.global.util.ResultCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SelCodeController {
    private final SelCodeServiceImpl selCodeService;

    @PostMapping("/api/sel-codes")
    public ResultCode saveKindCode(@ModelAttribute SelCode selCode) {
        return selCodeService.save(selCode);
    }

    @GetMapping("/api/sel-codes")
    public List<SelCode> getAll() {
        return selCodeService.getAll();
    }

    @GetMapping("/api/kind-codes/{kindCode}/sel-codes")
    public List<SelCode> getAll(@PathVariable String kindCode) {
        return selCodeService.getSelCodesByCode(kindCode);
    }

    @GetMapping("/api/sel-codes/{id}")
    public SelCode getOne(@PathVariable Long id) {
        return selCodeService.getById(id);
    }

    @PutMapping("/api/sel-codes/{id}")
    public ResultCode putCode(@ModelAttribute SelCode selCode, @PathVariable Long id) {
        return selCodeService.modifySelCode(selCode, id);
    }
    @DeleteMapping("/api/sel-codes/{id}")
    public ResultCode deleteCode(@PathVariable Long id) {
        return selCodeService.deleteById(id);
    }
}
