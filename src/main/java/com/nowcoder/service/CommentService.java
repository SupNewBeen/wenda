package com.nowcoder.service;

import com.nowcoder.dao.CommentDao;
import com.nowcoder.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;


@Service
public class CommentService {
    @Autowired(required = false)
    CommentDao commentDao;

    @Autowired
    SensitiveService sensitiveService;

    public List<Comment> getCommentsByEntity(int entityId, int entityType) {
        return commentDao.selectCommentByEntity(entityId, entityType);
    }

    public int addComment(Comment comment) {
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        comment.setContent(sensitiveService.filter(comment.getContent()));
        return commentDao.addComment(comment) > 0 ? comment.getId() : 0;
    }

    public int getCommentCount(int entityId, int entityType) {
        return commentDao.getCommentCount(entityId, entityType);
    }

    public boolean deleteComment(int commentId) {
        return commentDao.updateStatus(commentId, 1) > 0;
    }

    public Comment getCommentById(int id) {
        return commentDao.getCommentById(id);
    }
}
