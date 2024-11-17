package com.example.SkinSwap.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Controller
public class SteamApiService
{
    @Value("${steam.api.key}")
    private String apiKey;

    @Value("${steam.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate;

    public SteamApiService(RestTemplate restTemplate)
    {
        this.restTemplate = restTemplate;
    }

    public Map<String, Object> getPlayerSummary(String steamId) {
        String url = String.format("%s?key=%s&steamids=%s", apiUrl, apiKey, steamId);

        try {
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            if (response != null && response.containsKey("response")) {
                Map<String, Object> playerResponse = (Map<String, Object>) response.get("response");
                if (playerResponse.containsKey("players")) {
                    return ((Map<String, Object>) ((java.util.List<?>) playerResponse.get("players")).get(0));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
