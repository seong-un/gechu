package com.gechu.crawl.game.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.gechu.crawl.game.dto.GameApiDto;
import com.gechu.crawl.game.dto.GamePlatformDto;
import com.gechu.crawl.game.entity.GamePlatformEntity;
import com.gechu.crawl.game.repository.GamePlatformRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GamePlatformService {

	private final GamePlatformRepository gamePlatformRepository;

	public void insertGamePlatform(GameApiDto gameApiDto) {
		Integer gameSeq = gameApiDto.getId();
		List<Integer> platforms = gameApiDto.getPlatforms();
		for (Integer platformSeq : platforms) {
			gamePlatformRepository.save(GamePlatformEntity.builder()
				.gameSeq(gameSeq)
				.platformSeq(platformSeq)
				.build());
		}
	}

	public void insertAllGamePlatforms(List<GamePlatformDto> gamePlatformDtos) {
		List<GamePlatformEntity> gamePlatformEntities = gamePlatformDtos.stream().map(GamePlatformDto::toEntity).collect(
			Collectors.toList());
		gamePlatformRepository.saveAll(gamePlatformEntities);
		gamePlatformDtos.clear();
	}
}