package com.nowcoder.controller;

import com.nowcoder.dao.ViewObject;
import com.nowcoder.model.HostHolder;
import com.nowcoder.model.Message;
import com.nowcoder.model.User;
import com.nowcoder.service.MessageService;
import com.nowcoder.service.UserService;
import com.nowcoder.util.WendaUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.misc.Request;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class MessageController {
    private static Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    private  MessageService messageService;

    @Autowired
    private  UserService userService;

    @Autowired
    private  HostHolder hostHolder;

    //找出一个用户的所有消息列表(列表是和每一个用户消息的汇总，经过Group By的结果）
    @RequestMapping(path={"msg/list"},method={RequestMethod.GET})
    public String getConversationList(Model model){
        try{
            int userId = hostHolder.getUser().getId();
            //找到和当前用户有关的所有message
            List<Message> conversationList = messageService.getConversationList(userId,0,10);
            List<ViewObject> conversations = new ArrayList<>();
            for(Message message:conversationList){
                ViewObject vo = new ViewObject();
                vo.set("conversation",message);

                //来自对方的消息
                int targetId = message.getFromId() == userId ? message.getToId():message.getFromId();
                User user = userService.getUser(targetId);
                vo.set("user",user);
                vo.set("unread",messageService.getConversationUnreadCount(userId,message.getConversationId()));
                conversations.add(vo);
            }
            model.addAttribute("conversations",conversations);
        }catch(Exception e){
            logger.error("获取站内信列表失败" + e.getMessage());
        }
        return "letter";
    }

    //获取和指定用户的所有往来消息
    @RequestMapping(path={"/msg/detail"},method={RequestMethod.GET})
    public String getConversationDetail(Model model, @Param("conversationId")String conversationId){
        if(StringUtils.isBlank(conversationId)){
            return "/msg/list";
        }
        try{
            //得到两个用户之间的会话列表数据
            List<Message> conversationList = messageService.getConversationDetail(conversationId,0,10);
            List<ViewObject> messages = new ArrayList<>();
            //遍历会话列表，每条会话数据到前端就是一个会话框
            for(Message msg:conversationList){
                ViewObject vo = new ViewObject();
                vo.set("message",msg);

                User user = userService.getUser(msg.getFromId());
                if(user == null){
                    continue;
                }
                vo.set("headUrl",user.getHeadUrl());
                vo.set("userId",user.getId());
                messages.add(vo);
            }
            model.addAttribute("messages",messages);
        }catch(Exception e){
            logger.error("获取消息详情失败" + e.getMessage());
        }
        // 这里还应该把数据库里面会话的阅读状态设置为已经阅读,也就是has_read修改为1
        messageService.updateMessageReadStatus(conversationId);
        return "letterDetail";
    }

    //因为是弹框，所以用json作为返回
    //其实我觉得，站内信是完全可以搞成异步化的
    @RequestMapping(path={"/msg/addMessage"},method={RequestMethod.POST})
    @ResponseBody
    public String addMessage(@RequestParam("toName") String toName,@RequestParam("content") String content){
        try{
            if(hostHolder.getUser() == null){
                return WendaUtil.getJSONString(999,"未登录");
            }
            User user = userService.selectByName(toName);
            if(user == null){
                return WendaUtil.getJSONString(1,"用户不存在");
            }
            Message message = new Message();
            message.setContent(content);
            message.setFromId(hostHolder.getUser().getId());
            message.setToId(user.getId());
            message.setCreatedDate(new Date());
            int fromId = hostHolder.getUser().getId();
            int toId = user.getId();
            message.setConversationId(fromId<toId?String.format("%d_%d",fromId,toId) : String.format("%d_%d",toId,fromId));

            messageService.addMessage(message);
            return WendaUtil.getJSONString(0);
        }catch(Exception e){
            logger.error("增加站内信失败" + e.getMessage());
            return WendaUtil.getJSONString(1, "插入站内信失败");
        }
    }
}
