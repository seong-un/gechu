package com.gechu.game.game.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gechu.game.game.dto.GameResponseDto;
import com.gechu.game.game.service.GameService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/games")
public class GameController {

	private final GameService gameService;

	@PostMapping("/list/seq")
	public ResponseEntity<?> findGamesBySeqs(@RequestBody List<Integer> seqs) {

		List<GameResponseDto> gameResponseDtos = null;

		try{
			gameResponseDtos = gameService.findAllBySeqIn(seqs);
		}catch (Exception e){
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(gameResponseDtos, HttpStatus.OK);
	}

	@GetMapping("/slug")
	public ResponseEntity<?> findGame(@RequestParam("slug") String gameSlug) {
		List<String> genreDummies = new ArrayList<>();
		genreDummies.add("puzzle");
		genreDummies.add("music");
		List<String> platformDummies = new ArrayList<>();
		platformDummies.add("switch");
		platformDummies.add("ps5");
		platformDummies.add("win");

		GameResponseDto gameResponseDto = GameResponseDto.builder()
			.gameTitle(gameSlug)
			.gameSlug(gameSlug)
			.gameTitleImageUrl(
				"https://img.freepik.com/free-photo/adorable-kitty-looking-like-it-want-to-hunt_23-2149167099.jpg")
			.seq(1)
			.publish("LeeChanHeeCompany")
			.genres(genreDummies)
			.develop("LeeChanHeeCompany")
			.platforms(platformDummies)
			.createDate(LocalDateTime.now())
			.build();

		return new ResponseEntity<>(gameResponseDto, HttpStatus.OK);
	}
	@GetMapping("/seq")
	public ResponseEntity<?> findGame(@RequestParam("seq") Integer gameSeq) {
		List<String> genreDummies = new ArrayList<>();
		genreDummies.add("puzzle");
		genreDummies.add("music");
		List<String> platformDummies = new ArrayList<>();
		platformDummies.add("switch");
		platformDummies.add("ps5");
		platformDummies.add("win");

		GameResponseDto gameResponseDto = GameResponseDto.builder()
			.gameTitle("Test King: tears of the test")
			.gameSlug("test-king-tears-of-the-test")
			.gameTitleImageUrl(
				"https://img.freepik.com/free-photo/adorable-kitty-looking-like-it-want-to-hunt_23-2149167099.jpg")
			.seq(gameSeq)
			.publish("LeeChanHeeCompany")
			.genres(genreDummies)
			.develop("LeeChanHeeCompany")
			.platforms(platformDummies)
			.createDate(LocalDateTime.now())
			.build();

		return new ResponseEntity<>(gameResponseDto, HttpStatus.OK);
	}

}
