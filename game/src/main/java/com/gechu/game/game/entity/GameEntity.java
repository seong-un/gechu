package com.gechu.game.game.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.data.domain.Persistable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor @AllArgsConstructor
@Entity
@Table(name = "game", indexes = {
	@Index(name = "idx_game_slug", columnList = "game_slug")
})
@Builder
public class GameEntity implements Persistable<Integer> {

	@Id
	private Integer seq;

	@NotNull
	private String gameTitle;
	@NotNull
	@Column(name = "game_slug")
	private String gameSlug;
	@NotNull
	private String gameTitleImageUrl;
	private String develop;
	private String publish;
	private LocalDateTime createDate;
	private Integer metaScore;
	private Integer openScore;
	private String steamScore;

	@OneToMany(mappedBy = "gameEntity", cascade = CascadeType.REMOVE)
	List<NewsEntity> newsEntityList = new ArrayList<>();

	@Override
	public Integer getId() {
		return this.seq;
	}

	@Override
	public boolean isNew() {
		return true;
	}
}