package com.aurora.controller;

import com.aurora.annotation.OptLog;
import com.aurora.model.dto.TalkAdminDTO;
import com.aurora.model.dto.TalkDTO;
import com.aurora.enums.FilePathEnum;
import com.aurora.model.vo.ResultVO;
import com.aurora.service.TalkService;
import com.aurora.strategy.context.UploadStrategyContext;
import com.aurora.model.vo.ConditionVO;
import com.aurora.model.dto.PageResultDTO;
import com.aurora.model.vo.TalkVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

import static com.aurora.constant.OptTypeConstant.*;

@Api(tags = "说说模块")
@RestController
public class TalkController {

    @Autowired
    private TalkService talkService;

    @Autowired
    private UploadStrategyContext uploadStrategyContext;

    @ApiOperation(value = "查看说说列表")
    @GetMapping("/talks")
    public ResultVO<PageResultDTO<TalkDTO>> listTalks() {
        return ResultVO.ok(talkService.listTalks());
    }

    @ApiOperation(value = "根据id查看说说")
    @ApiImplicitParam(name = "talkId", value = "说说id", required = true, dataType = "Integer")
    @GetMapping("/talks/{talkId}")
    public ResultVO<TalkDTO> getTalkById(@PathVariable("talkId") Integer talkId) {
        return ResultVO.ok(talkService.getTalkById(talkId));
    }

    @OptLog(optType = UPLOAD)
    @ApiOperation(value = "上传说说图片")
    @ApiImplicitParam(name = "file", value = "说说图片", required = true, dataType = "MultipartFile")
    @PostMapping("/admin/talks/images")
    public ResultVO<String> saveTalkImages(MultipartFile file) {
        return ResultVO.ok(uploadStrategyContext.executeUploadStrategy(file, FilePathEnum.TALK.getPath()));
    }

    @OptLog(optType = SAVE_OR_UPDATE)
    @ApiOperation(value = "保存或修改说说")
    @PostMapping("/admin/talks")
    public ResultVO<?> saveOrUpdateTalk(@Valid @RequestBody TalkVO talkVO) {
        talkService.saveOrUpdateTalk(talkVO);
        return ResultVO.ok();
    }

    @OptLog(optType = DELETE)
    @ApiOperation(value = "删除说说")
    @DeleteMapping("/admin/talks")
    public ResultVO<?> deleteTalks(@RequestBody List<Integer> talkIds) {
        talkService.deleteTalks(talkIds);
        return ResultVO.ok();
    }

    @ApiOperation(value = "查看后台说说")
    @GetMapping("/admin/talks")
    public ResultVO<PageResultDTO<TalkAdminDTO>> listBackTalks(ConditionVO conditionVO) {
        return ResultVO.ok(talkService.listBackTalks(conditionVO));
    }

    @ApiOperation(value = "根据id查看后台说说")
    @ApiImplicitParam(name = "talkId", value = "说说id", required = true, dataType = "Integer")
    @GetMapping("/admin/talks/{talkId}")
    public ResultVO<TalkAdminDTO> getBackTalkById(@PathVariable("talkId") Integer talkId) {
        return ResultVO.ok(talkService.getBackTalkById(talkId));
    }

}
