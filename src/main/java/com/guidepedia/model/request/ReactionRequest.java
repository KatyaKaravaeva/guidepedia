package com.guidepedia.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReactionRequest {
    private Long articleId;
    private Boolean reaction;
}