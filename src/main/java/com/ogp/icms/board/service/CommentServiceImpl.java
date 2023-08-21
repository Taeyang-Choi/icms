package com.ogp.icms.board.service;

import com.ogp.icms.board.dao.CommentRepository;
import com.ogp.icms.board.domain.Comment;
import com.ogp.icms.board.domain.Comments;
import com.ogp.icms.global.util.ResultCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class CommentServiceImpl {
    private final CommentRepository commentRepository;

    public Optional<Comment> getOne(Long idx) throws IOException {
        return commentRepository.findByIdx(idx);
    }

    public List<Comment> getList(Long id) throws IOException {
        return commentRepository.findAllById(id);
    }

    public List<Comments> getList(List<String> ids) throws IOException {
        return commentRepository.findAllByIds(ids);
    }

    public List<Comment> getUnderList(Long pos) throws IOException {
        return commentRepository.findAllByPos(pos);
    }

    public ResultCode addComment(Comment comment) throws IOException {
        comment.setTbl("notice");
        comment.setRegdate(LocalDateTime.now());
        commentRepository.save(comment);

        return new ResultCode(0, "댓글을 작성했습니다.");
    }

    public ResultCode editComment(Comment comment,Long idx) throws IOException {
        Comment oldComment = commentRepository.getByIdx(idx);

        oldComment.setMsg_text(comment.getMsg_text());

        return new ResultCode(0, "댓글을 수정했습니다.");
    }

    public ResultCode deleteComment(Long idx) throws IOException {
        commentRepository.deleteByIdx(idx);
        commentRepository.deleteByPos(idx);

        return new ResultCode(0,"댓글을 삭제했습니다.");
    }
}
