package com.aurora.dto;

import com.aurora.entity.Article;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArchiveDTO implements Comparable<ArchiveDTO> {
    private String Time;
    private List<ArticleCardDTO> articles;

    @Override
    public int compareTo(ArchiveDTO o) {
        return 0;
    }
}
