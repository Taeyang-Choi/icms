package com.ogp.icms.board.dao;

import com.ogp.icms.board.domain.Comment;
import com.ogp.icms.board.domain.Comments;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    public Optional<Comment> findByIdx(Long idx);

    public List<Comment> findAllById(Long id);

    @Query(value = "SELECT c.id, count(*) comments FROM gs_board_comment c where c.id in (:ids) group by c.id", nativeQuery = true)
    public List<Comments> findAllByIds(@Param("ids") List<String> ids);

    public List<Comment> findAllByPos(Long pos);

    public Comment getByIdx(Long idx);

    public Optional<Comment> deleteByIdx(Long idx);

    public List<Comment> deleteByPos(Long idx);
}
