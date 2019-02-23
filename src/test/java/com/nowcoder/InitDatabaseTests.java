package com.nowcoder;

import com.nowcoder.dao.QuestionDao;
import com.nowcoder.dao.UserDao;
import com.nowcoder.model.Question;
import com.nowcoder.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Date;
import java.util.Random;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WendaApplication.class)
@WebAppConfiguration
public class InitDatabaseTests {
	@Autowired(required = false)
	UserDao userDao;

	@Autowired(required = false)
	QuestionDao questionDao;

	@Test
	public void contextLoads() {
		Random random = new Random();
		/*
		for(int i=0;i<11;++i){
			User user = new User();
			user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png",random.nextInt(1000)));
			user.setName(String.format("USER%d",i));
			user.setPassword("");
			user.setSalt("");
			userDao.addUser(user);
		}
		*/
		for(int i=1;i<=11;i++){
			Question question = new Question();
			question.setUserId(i);
			question.setCommentCount(i);
			question.setContent("Hello World");
			question.setCreateDate(new Date());
			question.setTitle("Welcome");
			questionDao.addQuestion(question);
		}
	}

}
