package com.gechu.crawl.game.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.gechu.crawl.game.dto.GameDto;
import com.gechu.crawl.game.entity.GameEntity;
import com.gechu.crawl.game.repository.GameRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GameService {

	private final GameRepository gameRepository; // 초기화 필수

	public void insertGame(GameDto gameDto) {
		GameEntity gameEntity = GameDto.toEntity(gameDto);
		gameRepository.save(gameEntity);
	}

	public void insertAllGames(List<GameDto> gameDtoList) {
		List<GameEntity> gameEntities = gameDtoList.stream().map(GameDto::toEntity).collect(Collectors.toList());
		gameRepository.saveAll(gameEntities);
		gameDtoList.clear();
	}
}