package com.sellics.keywordscore.service.autocompletion;

/**
 * Class to fetch the results from Amazon autocompletion API
 *
 * @author Deepak Srinivas
 */
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AutoCompletionServiceImpl implements AutoCompletionService
{

    @Value("${amazon.autocompletion.endpoint}")
    private String endpoint;

    @Value("${autocompletion.default.search.alias}")
    private String searchAlias;

    @Value("${amazon.client.value}")
    private String clientValue;

    @Value("${amazon.mkt.value}")
    private String mktValue;

    private final RestTemplate restTemplate;

    private final ObjectMapper objectMapper;

    @Autowired
    public AutoCompletionServiceImpl(final RestTemplate restTemplate, final ObjectMapper objectMapper)
    {
        this.objectMapper = objectMapper;
        this.restTemplate = restTemplate;
    }


    /**
     * Method that makes a call to the Amazon autocompletion API and fetches,
     * the second JSON from the array which has a list .
     * @param keyword keyword to be searched
     * @return a list of searched result
     */
    @Override
    public List<String> getAutoCompletionData(String keyword)
    {
        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("searchAlias", searchAlias);
        uriVariables.put("client", clientValue);
        uriVariables.put("mkt", mktValue);
        uriVariables.put("keyword", keyword);
        String response = restTemplate.getForObject(endpoint, String.class, uriVariables);
        try
        {
            List<Object> responseList = objectMapper.readValue(response, List.class);
            if (responseList != null || !responseList.isEmpty() || responseList.size() > 1)
            {
                return (List<String>) responseList.get(1);
            }
        }
        catch (IOException e)
        {
            log.error("An error occurred while fetching", e);
        }
        //return an empty list if there's any exception
        return Collections.emptyList();
    }
}
