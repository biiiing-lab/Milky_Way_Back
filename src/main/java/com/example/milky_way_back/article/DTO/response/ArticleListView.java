package com.example.milky_way_back.article.DTO.response;

import com.example.milky_way_back.article.entity.Article;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ArticleListView {
    private final Long article_no;
    private final String articleType;
    private final String title;
    private final boolean findMentor;
    private final String endDay;
    private final boolean recruit;
    private final int apply;
    private final int applyNow;
    private final int likes;
    private final String startDay;
    private final LocalDateTime regDate;

    public ArticleListView(Article article){
        this.article_no = article.getArticle_no();
        this.articleType = article.getArticleType();
        this.title = article.getTitle();
        this.apply = article.getApply();
        this.findMentor = article.isFindMentor();
        this.recruit = article.getRecruit();
        this.applyNow = article.getApplyNow();
        this.likes = article.getLikes();
        this.startDay = article.getStartDay();
        this.endDay = article.getEndDay();
        this.regDate = article.getRegDate();
    }
}
