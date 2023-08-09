package com.ogp.icms.board.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class ArticleForm {
    private String userid;
    private String username;
    private String title;
    private String content;
    private List<MultipartFile> attachments;

    @JsonAlias("notice_yn")
    private String noticeYn;

}
