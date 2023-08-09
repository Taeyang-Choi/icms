package com.ogp.icms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/common")
public class CommonController {

    @GetMapping("/admin/edit")
    public String adminEdit() {
        return "common/admin/edit";
    }

    @GetMapping("/admin/list")
    public String adminList() {
        return "common/admin/list";
    }

    @GetMapping("/admin/write")
    public String adminWrite() {return "common/admin/write";}

    @GetMapping("/code/edit")
    public String codeEdit() {return "common/code/edit";}

    @GetMapping("/code/list")
    public String codeList() {return "common/code/list";}

    @GetMapping("/code/write")
    public String codeWrite() {return "common/code/write";}

    @GetMapping("/code/sel/edit")
    public String codeSelEdit() {return "common/code/sel/edit";}

    @GetMapping("/code/sel/list")
    public String codeSelList() {return "common/code/sel/list";}

    @GetMapping("/code/sel/write")
    public String codeSelWrite() {return "common/code/sel/write";}

    @GetMapping("/license/list")
    public String licenseList() {return "common/license/list";}

    @GetMapping("/license/detail")
    public String licenseDetail() {return "common/license/detail";}

}
