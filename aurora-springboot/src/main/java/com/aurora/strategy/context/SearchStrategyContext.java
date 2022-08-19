package com.aurora.strategy.context;

import com.aurora.dto.ArticleSearchDTO;
import com.aurora.strategy.SearchStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.aurora.enums.SearchModeEnum.getStrategy;


/**
 * @author 花未眠
 * 搜索策略上下文
 */
@Service
public class SearchStrategyContext {
    /**
     * 搜索模式
     */
    @Value("${search.mode}")
    private String searchMode;

    @Autowired
    private Map<String, SearchStrategy> searchStrategyMap;

    /**
     * 执行搜索策略
     */
    public List<ArticleSearchDTO> executeSearchStrategy(String keywords) {
        return searchStrategyMap.get(getStrategy(searchMode)).searchArticle(keywords);
    }

}
