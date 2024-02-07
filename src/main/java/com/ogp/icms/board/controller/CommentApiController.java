package com.ogp.icms.board.controller;

import com.ogp.icms.board.domain.Comment;
import com.ogp.icms.board.domain.Comments;
import com.ogp.icms.board.service.CommentServiceImpl;
import com.ogp.icms.global.util.ResultCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RestController
public class CommentApiController {
    private final CommentServiceImpl commentService;
    
    @GetMapping("/api/comment/{idx}")
    public Optional<Comment> getOne(@PathVariable Long idx) throws IOException {
        return commentService.getOne(idx);
    }

    @GetMapping("/api/comment/list/{id}")
    public List<Comment> getList(@PathVariable Long id) throws IOException {
        return commentService.getList(id);
    }

    @GetMapping("/api/comment/lists/{ids}")
    public List<Comments> getList(@PathVariable String ids) throws IOException {
        //System.out.println(ids +" "+ids.replaceAll("_", ","));
        String[] _ids = ids.split("_");
        List<String> __ids = new ArrayList<>();
        for (int i=0; i<_ids.length; i++) {
            __ids.add(_ids[i]);
        }
        return commentService.getList(__ids);
    }

    @GetMapping("/api/comment/under/{pos}")
    public List<Comment> getUnderList(@PathVariable Long pos) throws IOException {
        return commentService.getUnderList(pos);
    }

    @PostMapping("/api/comment/write")
    public ResultCode writeComment(Comment comment, HttpServletRequest request) throws IOException {
        comment.setUserip(request.getRemoteAddr());
        return commentService.addComment(comment);
    }

    @PostMapping("/api/comment/edit")
    public ResultCode editComment(@ModelAttribute Comment comment, @RequestParam Long idx, HttpServletRequest request) throws IOException {
        comment.setUserip(request.getRemoteAddr());
        return commentService.editComment(comment,idx);
    }

    @DeleteMapping("/api/comment/{idx}")
    public ResultCode deleteComment(@PathVariable Long idx) throws IOException {
        return commentService.deleteComment(idx);
    }
}
