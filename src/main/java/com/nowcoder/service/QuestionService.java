package com.nowcoder.service;

import com.nowcoder.dao.QuestionDao;
import com.nowcoder.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class QuestionService {
    @Autowired(required = false)
    QuestionDao questionDao;

    @Autowired
    SensitiveService sensitiveService;

    public List<Question> getLatestQuestions(int userId, int offset, int limit){
        return questionDao.selectLatestQuestion(userId,offset,limit);
    }

    public int addQuestion(Question question){
        //敏感词过滤
        question.setContent(sensitiveService.filter(HtmlUtils.htmlEscape(question.getContent())));
        question.setTitle(sensitiveService.filter(HtmlUtils.htmlEscape(question.getTitle())));
        return questionDao.addQuestion(question) > 0 ? question.getId() : 0;
    }

    public Question getById(int id){
        return questionDao.selectById(id);
    }

    public int updateCommentCount(int id, int count) {
        return questionDao.updateCommentCount(id, count);
    }
}
