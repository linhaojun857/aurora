package com.aurora.service;

import com.aurora.dto.PhotoAlbumAdminDTO;
import com.aurora.dto.PhotoAlbumDTO;
import com.aurora.entity.PhotoAlbum;
import com.aurora.vo.ConditionVO;
import com.aurora.vo.PageResult;
import com.aurora.vo.PhotoAlbumVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface PhotoAlbumService extends IService<PhotoAlbum> {

    void saveOrUpdatePhotoAlbum(PhotoAlbumVO photoAlbumVO);

    PageResult<PhotoAlbumAdminDTO> listPhotoAlbumsAdmin(ConditionVO condition);

    List<PhotoAlbumDTO> listPhotoAlbumInfosAdmin();

    PhotoAlbumAdminDTO getPhotoAlbumByIdAdmin(Integer albumId);

    void deletePhotoAlbumById(Integer albumId);

    List<PhotoAlbumDTO> listPhotoAlbums();
}
