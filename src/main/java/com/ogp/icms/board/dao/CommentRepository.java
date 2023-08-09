package com.ogp.icms.board.dao;

import com.ogp.icms.board.domain.Comment;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    public Optional<Comment> findByIdx(Long idx);

    public List<Comment> findAllById(Long id);

    public List<Comment> findAllByPos(Long pos);

    public Comment getByIdx(Long idx);

    public Optional<Comment> deleteByIdx(Long idx);

    public List<Comment> deleteByPos(Long idx);
}
