package com.ogp.icms.gis.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.ogp.icms.cctv.domain.Camera;
import com.ogp.icms.gis.domain.CrimeRequest;
import com.ogp.icms.gis.domain.User;
import com.ogp.icms.gis.service.GisServiceImpl;
import com.ogp.icms.global.util.ResultCode;
import com.ogp.icms.visit.domain.Visiting;
import com.ogp.icms.visit.domain.VisitingSearchCondition;
import com.ogp.icms.visit.request.VisitorOutRequest;
import com.ogp.icms.visit.service.VisitServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

@RestController
@Slf4j
@Controller
public class GisController {
    private final GisServiceImpl gisService;

    public GisController(GisServiceImpl gisService) {
        this.gisService = gisService;
    }

    //방문자 로드
    @GetMapping("/api/gis/{date1}/{date2}")
    public List<CrimeRequest> getVisitPage(@PathVariable String date1, @PathVariable String date2) {
        return gisService.findAllCrimeRequest(date1, date2);
    }

    @GetMapping("/api/gis/counts")
    public HashMap<String, Integer> getAllCount() {
        return gisService.getAllCount();
    }

    @GetMapping("/api/gis/update")
    public void updateDataBase() {
        gisService.UpdateDataBase();
    }

    @GetMapping("/api/gis/code/{code}")
    public String getCode(@PathVariable String code) {
        return gisService.getCode(code);
    }

    @GetMapping("/api/gis/cameras")
    public List<Camera> getCameras() {
        return gisService.getCameras();
    }

    @GetMapping("/api/gis/camera/{code}")
    public String getCamera(@PathVariable String code) {
        return gisService.getCamera(code);
    }

    @GetMapping("/api/gis/code-group/{code}")
    public String getCodeGroup(@PathVariable String code) {
        return gisService.getCodeGroup(code);
    }

    @GetMapping("/api/gis/table/{name}")
    public String printTable(@PathVariable String name) {
        return gisService.printTable(name);
    }
}
