package com.guidepedia.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleRequest {
    private String categoryName;
    private String title;
    private String description;
    private String text;
    private String image;
    private Boolean draft;
}