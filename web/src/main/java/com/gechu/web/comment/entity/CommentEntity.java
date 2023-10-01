package com.gechu.web.comment.entity;

import javax.persistence.*;

import com.gechu.web.article.entity.ArticleEntity;
import com.gechu.web.comment.dto.CommentDto;
import com.gechu.web.comment.dto.CommentResponseDto;
import com.gechu.web.estimate.dto.EstimateDto;
import com.gechu.web.user.entity.UsersEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

@Getter
@NoArgsConstructor @AllArgsConstructor
@Entity
@Table(name = "comment")
@Builder
public class CommentEntity {

    @Id
    @GeneratedValue
    private Long seq;
    private String content;
    @CreationTimestamp
    private LocalDateTime createDate;
    @ColumnDefault("false")
    private String deleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq")
    private UsersEntity users;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_seq")
    private ArticleEntity article;

    public void deleteComment() {
        this.deleted = "true";
    }

    public static CommentResponseDto toResponseDto(CommentEntity comment) {
        return CommentResponseDto.builder()
            .articleSeq(comment.getSeq())
            .userProfile(UsersEntity.toProfileDto(comment.getUsers()))
            .createDate(comment.getCreateDate())
            .content(comment.getContent())
            .build();
    }
}
