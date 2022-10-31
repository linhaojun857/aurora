package com.aurora.service;

import com.aurora.model.dto.PageResultDTO;
import com.aurora.model.dto.PhotoAdminDTO;
import com.aurora.model.dto.PhotoAlbumAdminDTO;
import com.aurora.model.dto.PhotoDTO;
import com.aurora.entity.Photo;
import com.aurora.model.vo.*;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface PhotoService extends IService<Photo> {

    PageResultDTO<PhotoAdminDTO> listPhotos(ConditionVO conditionVO);

    void updatePhoto(PhotoInfoVO photoInfoVO);

    void savePhotos(PhotoVO photoVO);

    void updatePhotosAlbum(PhotoVO photoVO);

    void updatePhotoDelete(DeleteVO deleteVO);

    void deletePhotos(List<Integer> photoIds);

    PhotoDTO listPhotosByAlbumId(Integer albumId);

}
