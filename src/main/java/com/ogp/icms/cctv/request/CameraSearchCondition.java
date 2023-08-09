package com.ogp.icms.cctv.request;

import com.ogp.icms.global.entity.SearchCondition;
import lombok.Data;

@Data
public class CameraSearchCondition implements SearchCondition {
    private String cctvGubun;
    private String index;
    private String juso;
    private String ymd;
}
