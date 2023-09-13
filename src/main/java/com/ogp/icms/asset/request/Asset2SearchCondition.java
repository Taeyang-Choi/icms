package com.ogp.icms.asset.request;

import com.ogp.icms.global.entity.SearchCondition;
import lombok.Data;

@Data
public class Asset2SearchCondition implements SearchCondition {
    private String purpose;
    private String name;
    private String address;
    //private String ymd;
}
