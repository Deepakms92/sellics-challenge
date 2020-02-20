package com.sellics.keywordscore.service.score;

/**
 * Service Class that does the score calculations and things required for score calculation.
 *
 * @author Deepak Srinivas
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.sellics.keywordscore.datatransferobject.ScoreDTO;
import com.sellics.keywordscore.service.autocompletion.AutoCompletionService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ScoreServiceImpl implements ScoreService
{

    @Value("${executor.timeout.value}")
    private Integer timeout;

    private final AutoCompletionService autoCompletionService;

    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    @Autowired
    public ScoreServiceImpl(final AutoCompletionService autoCompletionService)
    {
        this.autoCompletionService = autoCompletionService;
    }


    /**
     *Method to fetch the score of the keyword which hits the amazon API.
     * @param keyword keyword of which the score is to be calculated.
     * @return scoreDTO which is the response Entity .
     */
    @Override
    public ScoreDTO getKeywordScore(String keyword)
    {
        if (StringUtils.isBlank(keyword))
        {
            return new ScoreDTO(keyword, 0);
        }
        List<List<String>> apiResponseList = getApiResponseList(keyword);
        if (apiResponseList.isEmpty())
        {
            return new ScoreDTO(keyword, 0);
        }
        return new ScoreDTO(keyword, getCalculatedScore(keyword, apiResponseList));
    }


    /**
     * Method to split the Keyword word by word and keep it increasing,
     * sequentially and make call to the amazon API with the prefix formed.
     * @param keyword keyword of which the score is to be calculated and used to hit the amazon API.
     * @return List of result list from amazon API.
     */
    private List<List<String>> getApiResponseList(String keyword)
    {
        List<Callable<List<String>>> callables =
            new ArrayList<>();
        //split the keyword and add them up sequentially to form a prefix attach it to a callable list
        for (int i = 0; i < keyword.length(); i++)
        {
            String prefix = keyword.substring(0, i + 1);
            Callable<List<String>> listCallable = () -> autoCompletionService.getAutoCompletionData(prefix);
            callables.add(listCallable);
        }

        //execute the tasks in executor service within the time limit.
        try
        {
            List<Future<List<String>>> futureTasks = executorService.invokeAll(callables, timeout, TimeUnit.SECONDS);
            //get the future tasks and invoke each of tasks and put the result to a list
            return futureTasks.stream().map(future -> {
                try
                {
                    return future.get();
                }
                catch (InterruptedException e)
                {
                    log.error("the future task execution was interrupted", e);
                }
                catch (ExecutionException e)
                {
                    log.error("the future exception occurred", e);
                }
                return null;
            }).filter(Objects::nonNull)
                .collect(Collectors.toList());
        }
        catch (InterruptedException e)
        {
            log.error("there was an exception in invoking he future task ", e);
        }
        return Collections.emptyList();
    }


    /**This method os ised to find the score which is calculated by using the number of matches found
     * and the response list which is got as a percentage.
     * @param keyword keyword of which the score is to be calculated.
     * @param apiResponseList the list of responses which is got by the above method.
     * @return The score calculated.
     */
    private Integer getCalculatedScore(String keyword, List<List<String>> apiResponseList)
    {
        //pattern that matches the keyword in the result.
        Pattern pattern = Pattern.compile("\\b" + keyword + "\\b");
        //to find the number of matches from the response list.
        long matchesFound = apiResponseList.stream().flatMap(List::stream).filter(i -> pattern.matcher(i).find(0)).count();
        log.info("the number of matches found is {}", matchesFound);
        log.info("the api response list size is {}", apiResponseList.size());
        //score calculation logic.
        Integer totalScore = Math.toIntExact(Math.round((double) matchesFound / (apiResponseList.size() * 10) * 100));
        return totalScore;

    }

}
