package com.ogp.icms.board.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ogp.icms.attachment.domain.UploadFile;
import com.ogp.icms.board.domain.Article;
import com.ogp.icms.board.request.ArticleEditForm;
import com.ogp.icms.board.request.ArticleSearchCondition;
import com.ogp.icms.cctv.domain.Camera;
import com.ogp.icms.global.repository.support.Querydsl4RepositorySupport;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;

import static com.ogp.icms.board.domain.QArticle.article;
import static com.ogp.icms.cctv.domain.QCamera.camera;
import static org.springframework.util.StringUtils.hasText;

@Slf4j
@Repository
public class BoardJpaRepository extends Querydsl4RepositorySupport {
    private final BoardRepository boardRepository;
    private final ObjectMapper objectMapper;

    public BoardJpaRepository(BoardRepository boardRepository) {
        super(Article.class);
        this.boardRepository = boardRepository;
        objectMapper = new ObjectMapper();
    }

    /**
     * 검색과 페이징
     * @TODO search 고도화
     */
    public Page<Article> findByQuery(String table, ArticleSearchCondition search, Pageable pageable) {
        /*
        JPAQuery<Article> query = selectFrom(article)
                .where(usernameContains(search.getUsername()),
                        titleContains(search.getTitle()),
                        bodyContains(search.getBody()))
                .orderBy(article.id.desc());
        Page<Article> list = getQuerydsl().applyPagination(pageable, query).fetch();
         */
        return applyPagination(pageable, query -> query
            .selectFrom(article)
            .where(usernameContains(search.getUsername()),
                    titleContains(search.getTitle()),
                    bodyContains(search.getBody()))
            .orderBy(article.id.desc())
        );
    }

    public void modifyArticle(Long id, ArticleEditForm newArticle, ArrayNode arrayNode, String[] oldFiles) throws JsonProcessingException {
        Article oldArticle = boardRepository.getById(id);

        // 1. 기존 파일
        JsonNode jsonNode = objectMapper.readTree(oldArticle.getUserFile());
        for(int i = 0; i < jsonNode.size(); i++) {
            for (String oldFile : oldFiles) {
                if(oldFile.equals(jsonNode.get(i).get("store").asText())) {
                    arrayNode.add(jsonNode.get(i));
                    continue;
                }
            }
        }

        oldArticle.setUserFile(arrayNode.toString());
        oldArticle.setTitle(newArticle.getTitle());
        oldArticle.setBody(newArticle.getBody());
        oldArticle.setNoticeYn(newArticle.getNoticeYn());
        oldArticle.setNoticeEdate(newArticle.getNoticeEdate());

        getEntityManager().flush();
        getEntityManager().clear();
    }


    /**
     * 최신 공지만 가져온다.
     */
    public List<Article> findLatest() {
        JPAQuery<Article> query = selectFrom(article)
                .where(isImportant(),
                        isLatestNotice())
                .orderBy(article.id.desc());
        return query.fetch();
    }

    private BooleanExpression usernameContains(String username) {
        return !hasText(username) ? null : article.username.contains(username);
    }
    private BooleanExpression titleContains(String title) {
        return title == null ? null : article.title.contains(title);
    }
    private BooleanExpression bodyContains(String body) {
        return body == null ? null : article.body.contains(body);
    }
    private BooleanExpression isImportant() {
        return article.noticeYn.eq("1");
    }
    private BooleanExpression isLatestNotice() {
        return article.noticeEdate.after(Expressions.dateTemplate(LocalDateTime.class, "function('current_date')"));
    }
}
