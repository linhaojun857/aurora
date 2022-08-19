package com.aurora.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author 花未眠
 * 网站配置信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(description = "网站配置")
public class WebsiteConfigVO {

    /**
     * 网站名称
     */
    @ApiModelProperty(name = "name", value = "网站名称", required = true, dataType = "String")
    private String name;

    /**
     * 网站名称（英文）
     */
    @ApiModelProperty(name = "nickName", value = "网站作者昵称", required = true, dataType = "String")
    private String englishName;

    /**
     * 网站作者
     */
    @ApiModelProperty(name = "author", value = "网站作者", required = true, dataType = "String")
    private String author;


    /**
     * 作者头像
     */
    @ApiModelProperty(name = "avatar", value = "网站头像", required = true, dataType = "String")
    private String authorAvatar;


    /**
     * 网站作者介绍
     */
    @ApiModelProperty(name = "description", value = "网站作者介绍", required = true, dataType = "String")
    private String authorIntro;

    /**
     * 网站logo
     */
    @ApiModelProperty(name = "logo", value = "网站logo", required = true, dataType = "String")
    private String logo;

    /**
     * 多语言
     */
    @ApiModelProperty(name = "multiLanguage", value = "多语言", required = true, dataType = "Integer")
    private Integer multiLanguage;

    /**
     * 网站公告
     */
    @ApiModelProperty(name = "notice", value = "网站公告", required = true, dataType = "String")
    private String notice;

    /**
     * 网站创建时间
     */
    @ApiModelProperty(name = "websiteCreateTime", value = "网站创建时间", required = true, dataType = "LocalDateTime")
    private String websiteCreateTime;

    /**
     * 网站备案号
     */
    @ApiModelProperty(name = "beianNumber", value = "网站备案号", required = true, dataType = "String")
    private String beianNumber;

    /**
     * qq登录
     */
    @ApiModelProperty(name = "qqLogin", value = "QQ登录", required = true, dataType = "Integer")
    private Integer qqLogin;

    /**
     * github
     */
    @ApiModelProperty(name = "github", value = "github", required = true, dataType = "String")
    private String github;

    /**
     * gitee
     */
    @ApiModelProperty(name = "gitee", value = "gitee", required = true, dataType = "String")
    private String gitee;

    /**
     * qq
     */
    @ApiModelProperty(name = "qq", value = "qq", required = true, dataType = "String")
    private String qq;

    /**
     * 微信
     */
    @ApiModelProperty(name = "weChat", value = "微信", required = true, dataType = "String")
    private String weChat;

    /**
     * 微博
     */
    @ApiModelProperty(name = "weibo", value = "微博", required = true, dataType = "String")
    private String weibo;

    /**
     * CSDN
     */
    @ApiModelProperty(name = "csdn", value = "csdn", required = true, dataType = "String")
    private String csdn;

    /**
     * 知乎
     */
    @ApiModelProperty(name = "zhihu", value = "zhihu", required = true, dataType = "String")
    private String zhihu;

    /**
     * 掘金
     */
    @ApiModelProperty(name = "juejin", value = "juejin", required = true, dataType = "String")
    private String juejin;

    /**
     * twitter
     */
    @ApiModelProperty(name = "twitter", value = "twitter", required = true, dataType = "String")
    private String twitter;

    /**
     * stackoverflow
     */
    @ApiModelProperty(name = "stackoverflow", value = "stackoverflow", required = true, dataType = "String")
    private String stackoverflow;

    /**
     * 游客头像
     */
    @ApiModelProperty(name = "touristAvatar", value = "游客头像", required = true, dataType = "String")
    private String touristAvatar;

    /**
     * 用户头像
     */
    @ApiModelProperty(name = "userAvatar", value = "用户头像", required = true, dataType = "String")
    private String userAvatar;

    /**
     * 是否评论审核
     */
    @ApiModelProperty(name = "isCommentReview", value = "是否评论审核", required = true, dataType = "Integer")
    private Integer isCommentReview;


    /**
     * 是否邮箱通知
     */
    @ApiModelProperty(name = "isEmailNotice", value = "是否邮箱通知", required = true, dataType = "Integer")
    private Integer isEmailNotice;

    /**
     * 是否打赏
     */
    @ApiModelProperty(name = "isReward", value = "是否打赏", required = true, dataType = "Integer")
    private Integer isReward;

    /**
     * 微信二维码
     */
    @ApiModelProperty(name = "weiXinQRCode", value = "微信二维码", required = true, dataType = "String")
    private String weiXinQRCode;

    /**
     * 支付宝二维码
     */
    @ApiModelProperty(name = "alipayQRCode", value = "支付宝二维码", required = true, dataType = "String")
    private String alipayQRCode;

}
