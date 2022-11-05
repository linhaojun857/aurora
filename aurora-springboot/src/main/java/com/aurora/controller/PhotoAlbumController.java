package com.aurora.controller;

import com.aurora.annotation.OptLog;
import com.aurora.model.dto.PhotoAlbumAdminDTO;
import com.aurora.model.dto.PhotoAlbumDTO;
import com.aurora.enums.FilePathEnum;
import com.aurora.model.vo.ResultVO;
import com.aurora.service.PhotoAlbumService;
import com.aurora.strategy.context.UploadStrategyContext;
import com.aurora.model.vo.ConditionVO;
import com.aurora.model.dto.PageResultDTO;
import com.aurora.model.vo.PhotoAlbumVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

import java.util.List;

import static com.aurora.constant.OptTypeConstant.*;

@Api(tags = "相册模块")
@RestController
public class PhotoAlbumController {

    @Autowired
    private UploadStrategyContext uploadStrategyContext;

    @Autowired
    private PhotoAlbumService photoAlbumService;


    @OptLog(optType = UPLOAD)
    @ApiOperation(value = "上传相册封面")
    @ApiImplicitParam(name = "file", value = "相册封面", required = true, dataType = "MultipartFile")
    @PostMapping("/admin/photos/albums/upload")
    public ResultVO<String> savePhotoAlbumCover(MultipartFile file) {
        return ResultVO.ok(uploadStrategyContext.executeUploadStrategy(file, FilePathEnum.PHOTO.getPath()));
    }

    @OptLog(optType = SAVE_OR_UPDATE)
    @ApiOperation(value = "保存或更新相册")
    @PostMapping("/admin/photos/albums")
    public ResultVO<?> saveOrUpdatePhotoAlbum(@Valid @RequestBody PhotoAlbumVO photoAlbumVO) {
        photoAlbumService.saveOrUpdatePhotoAlbum(photoAlbumVO);
        return ResultVO.ok();
    }

    @ApiOperation(value = "查看后台相册列表")
    @GetMapping("/admin/photos/albums")
    public ResultVO<PageResultDTO<PhotoAlbumAdminDTO>> listPhotoAlbumBacks(ConditionVO conditionVO) {
        return ResultVO.ok(photoAlbumService.listPhotoAlbumsAdmin(conditionVO));
    }

    @ApiOperation(value = "获取后台相册列表信息")
    @GetMapping("/admin/photos/albums/info")
    public ResultVO<List<PhotoAlbumDTO>> listPhotoAlbumBackInfos() {
        return ResultVO.ok(photoAlbumService.listPhotoAlbumInfosAdmin());
    }

    @ApiOperation(value = "根据id获取后台相册信息")
    @ApiImplicitParam(name = "albumId", value = "相册id", required = true, dataType = "Integer")
    @GetMapping("/admin/photos/albums/{albumId}/info")
    public ResultVO<PhotoAlbumAdminDTO> getPhotoAlbumBackById(@PathVariable("albumId") Integer albumId) {
        return ResultVO.ok(photoAlbumService.getPhotoAlbumByIdAdmin(albumId));
    }

    @OptLog(optType = DELETE)
    @ApiOperation(value = "根据id删除相册")
    @ApiImplicitParam(name = "albumId", value = "相册id", required = true, dataType = "Integer")
    @DeleteMapping("/admin/photos/albums/{albumId}")
    public ResultVO<?> deletePhotoAlbumById(@PathVariable("albumId") Integer albumId) {
        photoAlbumService.deletePhotoAlbumById(albumId);
        return ResultVO.ok();
    }

    @ApiOperation(value = "获取相册列表")
    @GetMapping("/photos/albums")
    public ResultVO<List<PhotoAlbumDTO>> listPhotoAlbums() {
        return ResultVO.ok(photoAlbumService.listPhotoAlbums());
    }

}
