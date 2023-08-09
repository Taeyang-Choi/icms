package com.ogp.icms.web.assets;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cctv")
public class AssetController {

    @GetMapping("/error/detail")
    public String assetErrorDetail() {return "cctv/error/detail";}

    @GetMapping("/error/list")
    public String assetErrorList() {return "cctv/error/list";}

    @GetMapping("/info/detail")
    public String assetInfoDetail() {return "cctv/info/detail";}

    @GetMapping("/info/edit")
    public String assetInfoEdit() {return "cctv/info/edit";}

    @GetMapping("/info/list")
    public String assetInfoList() {return "cctv/info/list";}

    @GetMapping("/info/write")
    public String assetInfoWrite() {return "cctv/info/write";}

    @GetMapping("/map")
    public String cameraMap() {return "cctv/map";}
}
