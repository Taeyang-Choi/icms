package com.ogp.icms.board.controller;

import com.ogp.icms.board.domain.Article;
import com.ogp.icms.board.request.ArticleEditForm;
import com.ogp.icms.board.request.ArticleSearchCondition;
import com.ogp.icms.board.service.BoardServiceImpl;
import com.ogp.icms.global.util.ResultCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class BoardApiController {
    private final BoardServiceImpl boardServiceImpl;


    // 글 목록 가져오기
    @GetMapping("/api/board/{table}")
    public Page<Article> getPage(@PathVariable String table, Pageable pageable, ArticleSearchCondition searchCondition) {
        return boardServiceImpl.getList(table, searchCondition, pageable);
    }

    // 글 하나 가져오기
    @GetMapping("/api/board/{table}/{id}")
    public Article getOne(@PathVariable String table, @PathVariable Long id) {
        return boardServiceImpl.getOne(table, id);
    }

    // 최신 공지만 가져오기
    @GetMapping("/api/board/notices/latest")
    public List<Article> getLatest() {
        return boardServiceImpl.getLatest();
    }

    /**
     * 글 작성
     */
    @PostMapping("/api/board/{table}")
    public ResultCode writeArticle(@PathVariable String table, @ModelAttribute Article article, List<MultipartFile> attachments) throws IOException {
        return boardServiceImpl.writeArticle(table, article, attachments);
    }

    // 글 수정
    @PutMapping("/api/board/{table}/{id}")
    public ResultCode updateArticle(@PathVariable String table, @PathVariable Long id, @ModelAttribute ArticleEditForm article,
                                    @RequestParam String oldFiles, List<MultipartFile> attachments) throws IOException {
        return boardServiceImpl.updateArticle(id, article, attachments, oldFiles.split(","));
    }

    /**
     * 글 삭제
     */
    @DeleteMapping("/api/board/{table}/{id}")
    public ResultCode deleteArticle(@PathVariable String table, @PathVariable Long id) {
        return boardServiceImpl.delete(table, id);
    }
}
