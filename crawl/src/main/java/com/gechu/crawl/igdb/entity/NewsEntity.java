package com.gechu.crawl.igdb.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.web.bind.annotation.GetMapping;

import com.sun.istack.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "news")
public class NewsEntity {

	@Id
	@GeneratedValue
	private Long seq;

	@NotNull
	private String headline;
	@NotNull
	private String company;
	@NotNull
	private String url;
	@NotNull
	private String content;
	private String imageUrl;
	@NotNull
	private LocalDateTime uploadDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "game_seq")
	private GameEntity gameEntity;
}
