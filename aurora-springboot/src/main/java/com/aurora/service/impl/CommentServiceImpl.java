package com.aurora.service.impl;

import com.alibaba.fastjson.JSON;
import com.aurora.model.dto.*;
import com.aurora.entity.Article;
import com.aurora.entity.Comment;
import com.aurora.entity.Talk;
import com.aurora.entity.UserInfo;
import com.aurora.enums.CommentTypeEnum;
import com.aurora.exception.BizException;
import com.aurora.mapper.ArticleMapper;
import com.aurora.mapper.CommentMapper;
import com.aurora.mapper.TalkMapper;
import com.aurora.mapper.UserInfoMapper;
import com.aurora.service.AuroraInfoService;
import com.aurora.service.CommentService;
import com.aurora.util.HTMLUtil;
import com.aurora.util.PageUtil;
import com.aurora.util.UserUtil;
import com.aurora.model.vo.CommentVO;
import com.aurora.model.vo.ConditionVO;
import com.aurora.model.vo.PageResult;
import com.aurora.model.vo.ReviewVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.SneakyThrows;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.aurora.constant.CommonConstant.*;
import static com.aurora.constant.MQPrefixConstant.EMAIL_EXCHANGE;
import static com.aurora.enums.CommentTypeEnum.*;

@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Value("${website.url}")
    private String websiteUrl;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private TalkMapper talkMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private AuroraInfoService auroraInfoService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private static final List<Integer> types = new ArrayList<>();

    @PostConstruct
    public void init() {
        CommentTypeEnum[] values = CommentTypeEnum.values();
        for (CommentTypeEnum value : values) {
            types.add(value.getType());
        }
    }

    @Override
    public void saveComment(CommentVO commentVO) {
        checkCommentVO(commentVO);
        WebsiteConfigDTO websiteConfig = auroraInfoService.getWebsiteConfig();
        Integer isCommentReview = websiteConfig.getIsCommentReview();
        commentVO.setCommentContent(HTMLUtil.filter(commentVO.getCommentContent()));
        Comment comment = Comment.builder()
                .userId(UserUtil.getUserDetailsDTO().getUserInfoId())
                .replyUserId(commentVO.getReplyUserId())
                .topicId(commentVO.getTopicId())
                .commentContent(commentVO.getCommentContent())
                .parentId(commentVO.getParentId())
                .type(commentVO.getType())
                .isReview(isCommentReview == TRUE ? FALSE : TRUE)
                .build();
        commentMapper.insert(comment);
        String fromNickname = UserUtil.getUserDetailsDTO().getNickname();
        if (websiteConfig.getIsEmailNotice().equals(TRUE)) {
            CompletableFuture.runAsync(() -> notice(comment, fromNickname));
        }
    }

    @Override
    public PageResult<CommentDTO> listComments(CommentVO commentVO) {
        Integer commentCount = commentMapper.selectCount(new LambdaQueryWrapper<Comment>()
                .eq(Objects.nonNull(commentVO.getTopicId()), Comment::getTopicId, commentVO.getTopicId())
                .eq(Comment::getType, commentVO.getType())
                .isNull(Comment::getParentId)
                .eq(Comment::getIsReview, TRUE));
        if (commentCount == 0) {
            return new PageResult<>();
        }
        List<CommentDTO> commentDTOs = commentMapper.listComments(PageUtil.getLimitCurrent(), PageUtil.getSize(), commentVO);
        if (CollectionUtils.isEmpty(commentDTOs)) {
            return new PageResult<>();
        }
        List<Integer> commentIds = commentDTOs.stream()
                .map(CommentDTO::getId)
                .collect(Collectors.toList());
        List<ReplyDTO> replyDTOS = commentMapper.listReplies(commentIds);
        Map<Integer, List<ReplyDTO>> replyMap = replyDTOS.stream()
                .collect(Collectors.groupingBy(ReplyDTO::getParentId));
        commentDTOs.forEach(item -> {
            item.setReplyDTOs(replyMap.get(item.getId()));
        });
        return new PageResult<>(commentDTOs, commentCount);
    }

    @Override
    public List<ReplyDTO> listRepliesByCommentId(Integer commentId) {
        return commentMapper.listReplies(Collections.singletonList(commentId));
    }

    @Override
    public List<CommentDTO> listTopSixComments() {
        return commentMapper.listTopSixComments();
    }

    @SneakyThrows
    @Override
    public PageResult<CommentAdminDTO> listCommentsAdmin(ConditionVO conditionVO) {
        CompletableFuture<Integer> asyncCount = CompletableFuture.supplyAsync(() -> commentMapper.countComments(conditionVO));
        List<CommentAdminDTO> commentBackDTOList = commentMapper.listCommentsAdmin(PageUtil.getLimitCurrent(), PageUtil.getSize(), conditionVO);
        return new PageResult<>(commentBackDTOList, asyncCount.get());
    }

    @Override
    public void updateCommentsReview(ReviewVO reviewVO) {
        List<Comment> comments = reviewVO.getIds().stream().map(item -> Comment.builder()
                        .id(item)
                        .isReview(reviewVO.getIsReview())
                        .build())
                .collect(Collectors.toList());
        this.updateBatchById(comments);
    }

    public void checkCommentVO(CommentVO commentVO) {
        // 校验评论类型是否存在
        if (!types.contains(commentVO.getType())) {
            throw new BizException("参数校验异常");
        }
        // 类型为文章和说说时，类型id不能为空，且判断文章或说说是否存在
        if (Objects.requireNonNull(getCommentEnum(commentVO.getType())) == ARTICLE || Objects.requireNonNull(getCommentEnum(commentVO.getType())) == TALK) {
            // 类型id为空则报异常
            if (Objects.isNull(commentVO.getTopicId())) {
                throw new BizException("参数校验异常");
            } else {
                // 类型id不为空判断文章是否存在
                if (Objects.requireNonNull(getCommentEnum(commentVO.getType())) == ARTICLE) {
                    Article article = articleMapper.selectOne(new LambdaQueryWrapper<Article>().select(Article::getId, Article::getUserId).eq(Article::getId, commentVO.getTopicId()));
                    if (Objects.isNull(article)) {
                        throw new BizException("参数校验异常");
                    }
                }
                // 类型id不为空判断说说是否存在
                if (Objects.requireNonNull(getCommentEnum(commentVO.getType())) == TALK) {
                    Talk talk = talkMapper.selectOne(new LambdaQueryWrapper<Talk>().select(Talk::getId, Talk::getUserId).eq(Talk::getId, commentVO.getTopicId()));
                    if (Objects.isNull(talk)) {
                        throw new BizException("参数校验异常");
                    }
                }
            }
        }
        // 类型为友链,about,留言时，topicId必须为空
        if (Objects.requireNonNull(getCommentEnum(commentVO.getType())) == LINK
                || Objects.requireNonNull(getCommentEnum(commentVO.getType())) == ABOUT
                || Objects.requireNonNull(getCommentEnum(commentVO.getType())) == MESSAGE) {
            if (Objects.nonNull(commentVO.getTopicId())) {
                throw new BizException("参数校验异常");
            }
        }
        // 父评论时 replyUserId 为空
        if (Objects.isNull(commentVO.getParentId())) {
            if (Objects.nonNull(commentVO.getReplyUserId())) {
                throw new BizException("参数校验异常");
            }
        }
        // 子评论时
        if (Objects.nonNull(commentVO.getParentId())) {
            Comment parentComment = commentMapper.selectOne(new LambdaQueryWrapper<Comment>().select(Comment::getId, Comment::getParentId, Comment::getType).eq(Comment::getId, commentVO.getParentId()));
            if (Objects.isNull(parentComment)) {
                throw new BizException("参数校验异常");
            }
            if (Objects.nonNull(parentComment.getParentId())) {
                throw new BizException("参数校验异常");
            }
            if (!commentVO.getType().equals(parentComment.getType())) {
                throw new BizException("参数校验异常");
            }
            if (Objects.isNull(commentVO.getReplyUserId())) {
                throw new BizException("参数校验异常");
            } else {
                UserInfo existUser = userInfoMapper.selectOne(new LambdaQueryWrapper<UserInfo>().select(UserInfo::getId).eq(UserInfo::getId, commentVO.getReplyUserId()));
                if (Objects.isNull(existUser)) {
                    throw new BizException("参数校验异常");
                }
            }
        }
    }

    private void notice(Comment comment, String fromNickname) {
        // 评论自己不发邮件提醒
        if (comment.getUserId().equals(comment.getReplyUserId())) {
            return;
        }
        // 博主自己发评论不发邮件提醒
        if (comment.getUserId().equals(BLOGGER_ID) && Objects.isNull(comment.getParentId())) {
            return;
        }
        // 查询回复用户邮箱号
        String title;
        Integer userId = BLOGGER_ID;
        String topicId = Objects.nonNull(comment.getTopicId()) ? comment.getTopicId().toString() : "";
        if (Objects.nonNull(comment.getReplyUserId())) {
            userId = comment.getReplyUserId();
        } else {
            switch (Objects.requireNonNull(getCommentEnum(comment.getType()))) {
                case ARTICLE:
                    userId = articleMapper.selectById(comment.getTopicId()).getUserId();
                    break;
                case TALK:
                    userId = talkMapper.selectById(comment.getTopicId()).getUserId();
                default:
                    break;
            }
        }
        if (Objects.requireNonNull(getCommentEnum(comment.getType())).equals(ARTICLE)) {
            title = articleMapper.selectById(comment.getTopicId()).getArticleTitle();
        } else {
            title = Objects.requireNonNull(getCommentEnum(comment.getType())).getDesc();
        }
        UserInfo user = userInfoMapper.selectById(userId);
        if (StringUtils.isNotBlank(user.getEmail())) {
            EmailDTO emailDTO = getEmailDTO(comment, user, fromNickname, topicId, title, userId);
            rabbitTemplate.convertAndSend(EMAIL_EXCHANGE, "*", new Message(JSON.toJSONBytes(emailDTO), new MessageProperties()));
        }
    }

    private EmailDTO getEmailDTO(Comment comment, UserInfo user, String fromNickname, String topicId, String title, Integer userId) {
        EmailDTO emailDTO = new EmailDTO();
        if (comment.getIsReview().equals(TRUE)) {
            Map<String, Object> map = new HashMap<>();
            String url = websiteUrl + getCommentPath(comment.getType()) + topicId;
            if (Objects.isNull(comment.getParentId())) {
                emailDTO.setEmail(user.getEmail());
                emailDTO.setSubject("评论提醒");
                emailDTO.setTemplate("owner.html");
                String createTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(comment.getCreateTime());
                map.put("time", createTime);
                map.put("url", url);
                map.put("title", title);
                map.put("nickname", fromNickname);
                map.put("content", comment.getCommentContent());
            } else {
                Comment parentComment = commentMapper.selectOne(new LambdaQueryWrapper<Comment>().select(Comment::getCommentContent, Comment::getCreateTime).eq(Comment::getId, comment.getParentId()));
                emailDTO.setEmail(user.getEmail());
                emailDTO.setSubject("评论提醒");
                emailDTO.setTemplate("user.html");
                map.put("url", url);
                map.put("title", title);
                String createTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(parentComment.getCreateTime());
                map.put("time", createTime);
                map.put("toUser", user.getNickname());
                map.put("fromUser", fromNickname);
                map.put("parentComment", parentComment.getCommentContent());
                map.put("replyComment", comment.getCommentContent());
            }
            emailDTO.setCommentMap(map);
        } else {
            String adminEmail = userInfoMapper.selectById(BLOGGER_ID).getEmail();
            emailDTO.setEmail(adminEmail);
            emailDTO.setSubject("审核提醒");
            emailDTO.setContent("您收到了一条新的回复，请前往后台管理页面审核");
        }
        return emailDTO;
    }
}
