package com.gechu.web.elasticsearch.controller;

import com.gechu.web.elasticsearch.service.ElasticsearchService;
import com.gechu.web.game.dto.GameResponseDto;
import com.gechu.web.game.service.GameServiceClient;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/elasticsearch")
@RequiredArgsConstructor
public class ElasticsearchController {

    private final ElasticsearchService elasticsearchService;
    private final GameServiceClient gameServiceClient;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getTopGameSeqBySearchWord(@RequestParam String searchWord) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status;

        try {
            List<String> topGameSlugs = elasticsearchService.getTopGameSeqBySearchWord(searchWord);
            List<GameResponseDto> gameResponseDto = gameServiceClient.findGamesBySlugs(topGameSlugs);
            resultMap.put("success", true);
            resultMap.put("games", gameResponseDto);
            status = HttpStatus.OK;
        } catch (Exception e) {
            resultMap.put("success", false);
            resultMap.put("message", e.getMessage());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(resultMap, status);
    }
}
