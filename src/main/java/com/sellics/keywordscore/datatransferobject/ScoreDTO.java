package com.sellics.keywordscore.datatransferobject;

/**
 * DTO class for Score , which is the Response Entity.
 *
 * @author Deepak Srinivas
 */
import java.util.Objects;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ScoreDTO
{

    private String keyword;
    private Integer score;

    public ScoreDTO(String keyword, Integer score) {
        this.keyword = keyword;
        this.score = score;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ScoreDTO scoreDTO = (ScoreDTO) o;
        return Objects.equals(keyword, scoreDTO.keyword)
            &&
            Objects.equals(score, scoreDTO.score);
    }


    @Override
    public int hashCode()
    {
        return Objects.hash(keyword, score);
    }
}
