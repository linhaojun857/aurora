package com.aurora.controller;

import com.aurora.annotation.OptLog;
import com.aurora.model.dto.AboutDTO;
import com.aurora.model.dto.AuroraAdminInfoDTO;
import com.aurora.model.dto.AuroraHomeInfoDTO;
import com.aurora.model.dto.WebsiteConfigDTO;
import com.aurora.enums.FilePathEnum;
import com.aurora.model.vo.ResultVO;
import com.aurora.service.AuroraInfoService;
import com.aurora.strategy.context.UploadStrategyContext;
import com.aurora.model.vo.AboutVO;
import com.aurora.model.vo.WebsiteConfigVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

import static com.aurora.constant.OptTypeConstant.UPDATE;
import static com.aurora.constant.OptTypeConstant.UPLOAD;

@Api(tags = "aurora信息")
@RestController
public class AuroraInfoController {

    @Autowired
    private AuroraInfoService auroraInfoService;

    @Autowired
    private UploadStrategyContext uploadStrategyContext;

    @ApiOperation(value = "上报访客信息")
    @PostMapping("/report")
    public ResultVO<?> report() {
        auroraInfoService.report();
        return ResultVO.ok();
    }

    @ApiOperation(value = "获取系统信息")
    @GetMapping("/")
    public ResultVO<AuroraHomeInfoDTO> getBlogHomeInfo() {
        return ResultVO.ok(auroraInfoService.getAuroraHomeInfo());
    }

    @ApiOperation(value = "获取系统后台信息")
    @GetMapping("/admin")
    public ResultVO<AuroraAdminInfoDTO> getBlogBackInfo() {
        return ResultVO.ok(auroraInfoService.getAuroraAdminInfo());
    }

    @OptLog(optType = UPDATE)
    @ApiOperation(value = "更新网站配置")
    @PutMapping("/admin/website/config")
    public ResultVO<?> updateWebsiteConfig(@Valid @RequestBody WebsiteConfigVO websiteConfigVO) {
        auroraInfoService.updateWebsiteConfig(websiteConfigVO);
        return ResultVO.ok();
    }

    @ApiOperation(value = "获取网站配置")
    @GetMapping("/admin/website/config")
    public ResultVO<WebsiteConfigDTO> getWebsiteConfig() {
        return ResultVO.ok(auroraInfoService.getWebsiteConfig());
    }

    @ApiOperation(value = "查看关于我信息")
    @GetMapping("/about")
    public ResultVO<AboutDTO> getAbout() {
        return ResultVO.ok(auroraInfoService.getAbout());
    }

    @OptLog(optType = UPDATE)
    @ApiOperation(value = "修改关于我信息")
    @PutMapping("/admin/about")
    public ResultVO<?> updateAbout(@Valid @RequestBody AboutVO aboutVO) {
        auroraInfoService.updateAbout(aboutVO);
        return ResultVO.ok();
    }

    @OptLog(optType = UPLOAD)
    @ApiOperation(value = "上传博客配置图片")
    @ApiImplicitParam(name = "file", value = "图片", required = true, dataType = "MultipartFile")
    @PostMapping("/admin/config/images")
    public ResultVO<String> savePhotoAlbumCover(MultipartFile file) {
        return ResultVO.ok(uploadStrategyContext.executeUploadStrategy(file, FilePathEnum.CONFIG.getPath()));
    }

}
