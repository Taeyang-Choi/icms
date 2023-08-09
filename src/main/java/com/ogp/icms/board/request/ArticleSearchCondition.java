package com.ogp.icms.board.request;

import com.ogp.icms.global.entity.SearchCondition;
import lombok.Data;

@Data
public class ArticleSearchCondition implements SearchCondition {
    private String title;
    private String body;
    private String username;
}
