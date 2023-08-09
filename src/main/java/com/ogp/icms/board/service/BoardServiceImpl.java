package com.ogp.icms.board.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ogp.icms.attachment.FileStore;
import com.ogp.icms.attachment.domain.UploadFile;
import com.ogp.icms.board.dao.BoardJpaRepository;
import com.ogp.icms.board.dao.BoardRepository;
import com.ogp.icms.board.domain.Article;
import com.ogp.icms.board.request.ArticleEditForm;
import com.ogp.icms.board.request.ArticleSearchCondition;
import com.ogp.icms.global.util.ResultCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class BoardServiceImpl {
    private final BoardRepository boardRepository;
    private final BoardJpaRepository boardJpaRepository;
    private final FileStore fileStore;

    /**
     * 글 작성
     */
    public ResultCode writeArticle(String table, Article article, List<MultipartFile> attachments) throws IOException {
        // 1. 파일 업로드
        List<UploadFile> attachFiles = fileStore.storeFiles(attachments, "board/");

        // 2. 파일 오브젝트 생성
        ObjectMapper om = new ObjectMapper();
        ArrayNode arrayNode = om.createArrayNode();

        for(UploadFile attachFile : attachFiles) {
            ObjectNode objectNode = om.createObjectNode();
            objectNode.put("store", attachFile.getStoreFileName());
            objectNode.put("real", attachFile.getUploadFileName());
            arrayNode.add(objectNode);
        }

        article.setUserFile(arrayNode.toString());
        boardRepository.saveAndFlush(article);
        return new ResultCode(0, "글을 작성했습니다.");
    }

    public Page<Article> getList(String table, ArticleSearchCondition searchCondition, Pageable pageable) {
        int page = pageable.getPageNumber()-1;
        PageRequest newPageRequest = PageRequest.of((page <= 0) ? 0 : page, 10);
        return boardJpaRepository.findByQuery(table, searchCondition, newPageRequest);
    }

    public Article getOne(String table, Long id) {
        Optional<Article> boardOptional = boardRepository.findById(id);

        if(boardOptional.isPresent()) {
            boardOptional.get().setCount(boardOptional.get().getCount()+1);
            return boardOptional.get();
        }
        else {
            return new Article();
        }
    }

    public List<Article> getLatest() {
        return boardJpaRepository.findLatest();
    }

    public ResultCode delete(String tableName, Long id) {
        boardRepository.deleteById(id);
        return new ResultCode(0, "해당 글을 삭제했습니다.");
    }

    public ResultCode updateArticle(Long id, ArticleEditForm newArticle, List<MultipartFile> attachments, String[] oldFiles) throws IOException {
        // 1. 파일 업로드
        List<UploadFile> attachFiles = fileStore.storeFiles(attachments, "board/");

        // 2. 파일 오브젝트 생성
        ObjectMapper om = new ObjectMapper();
        ArrayNode arrayNode = om.createArrayNode();

        for(UploadFile attachFile : attachFiles) {
            ObjectNode objectNode = om.createObjectNode();
            objectNode.put("store", attachFile.getStoreFileName());
            objectNode.put("real", attachFile.getUploadFileName());
            arrayNode.add(objectNode);
        }

        boardJpaRepository.modifyArticle(id, newArticle, arrayNode, oldFiles);

        return new ResultCode(0, "수정을 완료했습니다.");
    }
}
