package com.aurora.consumer;


import com.alibaba.fastjson.JSON;
import com.aurora.dto.EmailDTO;
import com.aurora.entity.Article;
import com.aurora.entity.UserInfo;
import com.aurora.service.ArticleService;
import com.aurora.service.UserInfoService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.aurora.constant.CommonConst.TRUE;
import static com.aurora.constant.MQPrefixConst.SUBSCRIBE_QUEUE;

@Component
@RabbitListener(queues = SUBSCRIBE_QUEUE)
public class SubscribeConsumer {

    /**
     * 邮箱号
     */
    @Value("${spring.mail.username}")
    private String email;

    @Value("${website.url}")
    private String websiteUrl;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private UserInfoService userInfoService;

    @RabbitHandler
    public void process(byte[] data) {
        Integer articleId = JSON.parseObject(new String(data), Integer.class);
        Article article = articleService.getOne(new LambdaQueryWrapper<Article>().eq(Article::getId, articleId));
        List<UserInfo> users = userInfoService.list(new LambdaQueryWrapper<UserInfo>().eq(UserInfo::getIsSubscribe, TRUE));
        List<String> emails = users.stream().map(UserInfo::getEmail).collect(Collectors.toList());
        for (String to : emails) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(email);
            message.setTo(to);
            message.setSubject("文章订阅");
            if (article.getUpdateTime() == null) {
                message.setText("花未眠的个人博客发布了新的文章，地址：" + websiteUrl + "/articles/" + articleId);
            } else {
                message.setText("花未眠的个人博客对《" + article.getArticleTitle() + "》进行了更新，地址：" + websiteUrl + "/articles/" + articleId);
            }
            javaMailSender.send(message);
        }
    }
}
