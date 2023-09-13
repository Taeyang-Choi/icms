package com.ogp.icms.web.assets;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/cctv")
public class AssetController {

    @GetMapping("/error/detail")
    public String assetErrorDetail() {return "cctv/ver1/error/detail";}

    @GetMapping("/error/list")
    public String assetErrorList() {return "cctv/error/list";}

    @GetMapping("/info/detail")
    public String assetInfoDetail() {return "cctv/ver1/info/detail";}

    @GetMapping("/info/edit")
    public String assetInfoEdit() {return "cctv/ver1/info/edit";}

    @GetMapping("/info/list")
    public String assetInfoList() {return "cctv/ver1/info/list";}

    @GetMapping("/info/write")
    public String assetInfoWrite() {return "cctv/ver1/info/write";}

    @GetMapping("/map")
    public String cameraMap() {return "cctv/map";}

    @GetMapping("/info/list2")
    public String asset2InfoList() {return "cctv/ver2/info/list";}
    @GetMapping("/info/detail2")
    public String asset2InfoDetail() {return "cctv/ver2/info/detail";}
    @GetMapping("/info/edit2")
    public String asset2InfoEdit() {return "cctv/ver2/info/edit";}
    @GetMapping("/info/write2")
    public String asset2InfoWrite() {return "cctv/ver2/info/write";}
    @GetMapping("/error/detail2")
    public String asset2ErrorDetail() {return "cctv/ver2/error/detail";}
}
