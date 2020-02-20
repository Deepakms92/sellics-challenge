package com.sellics.keywordscore.service.score;

import com.sellics.keywordscore.datatransferobject.ScoreDTO;

public interface ScoreService {

    public ScoreDTO getKeywordScore(String keyword);
}
