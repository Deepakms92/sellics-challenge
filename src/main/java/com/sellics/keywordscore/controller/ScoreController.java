package com.sellics.keywordscore.controller;

/**
 * Controller class for finding the score
 *
 * @author Deepak Srinivas
 */

import com.sellics.keywordscore.datatransferobject.ScoreDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sellics.keywordscore.service.score.ScoreService;


@RestController
public class ScoreController
{

    private final ScoreService scoreService;

    @Autowired
    public ScoreController(final ScoreService scoreService)
    {
        this.scoreService = scoreService;
    }


    @GetMapping("estimate")
    public ScoreDTO getEstimate(@RequestParam String keyword)
    {
        return scoreService.getKeywordScore(keyword);
    }

}
