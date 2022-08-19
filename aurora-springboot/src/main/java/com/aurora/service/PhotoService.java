package com.aurora.service;

import com.aurora.dto.PhotoAdminDTO;
import com.aurora.dto.PhotoAlbumAdminDTO;
import com.aurora.dto.PhotoDTO;
import com.aurora.entity.Photo;
import com.aurora.vo.*;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface PhotoService extends IService<Photo> {

    PageResult<PhotoAdminDTO> listPhotos(ConditionVO conditionVO);

    void updatePhoto(PhotoInfoVO photoInfoVO);

    void savePhotos(PhotoVO photoVO);

    void updatePhotosAlbum(PhotoVO photoVO);

    void updatePhotoDelete(DeleteVO deleteVO);

    PageResult<PhotoAlbumAdminDTO> listPhotoAlbumBacks(ConditionVO conditionVO);

    void deletePhotos(List<Integer> photoIds);

    PhotoDTO listPhotosByAlbumId(Integer albumId);
}
