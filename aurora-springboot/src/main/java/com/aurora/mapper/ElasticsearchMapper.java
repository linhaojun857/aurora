package com.aurora.mapper;

import com.aurora.model.dto.ArticleSearchDTO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;


/**
 * @author 花未眠
 * elasticsearch
 */
@Repository
public interface ElasticsearchMapper extends ElasticsearchRepository<ArticleSearchDTO,Integer> {

}
