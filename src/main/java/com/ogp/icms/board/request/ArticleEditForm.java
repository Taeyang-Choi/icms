package com.ogp.icms.board.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.ogp.icms.global.entity.DateEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;
import java.time.LocalDateTime;

@NoArgsConstructor
@Data
public class ArticleEditForm {
    private String userid;
    private String username;
    private String title;
    private String body;
    private String noticeYn;
    private String userFile;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime noticeEdate;
}
