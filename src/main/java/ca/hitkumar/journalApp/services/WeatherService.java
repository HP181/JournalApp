package ca.hitkumar.journalApp.services;

import ca.hitkumar.journalApp.api.response.WeatherResponse;
import ca.hitkumar.journalApp.cache.AppCache;
import ca.hitkumar.journalApp.constants.Placeholders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {

    private static final Logger log = LoggerFactory.getLogger(WeatherService.class);

    @Value("${weather.api.key}")
    private String apikey;

    private static final String API = "http://api.weatherstack.com/current?access_key=API_KEY&query=CITY";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AppCache appCache;

    @Autowired
    private RedisService redisService;

    public WeatherResponse getWeather(String city) {
        // First check cache
        WeatherResponse weatherResponse = redisService.get("weather_of_" + city, WeatherResponse.class);
        if (weatherResponse != null) {
            log.info("✅ Weather data for '{}' fetched from Redis cache", city);
            return weatherResponse;
        }

        // Build API URL
        String finalAPIUrl = appCache.appCache
                .get(AppCache.keys.WEATHER_API.toString())
                .replace(Placeholders.API_KEY, apikey)
                .replace(Placeholders.CITY, city);

        // Call API
        ResponseEntity<WeatherResponse> response =
                restTemplate.exchange(finalAPIUrl, HttpMethod.GET, null, WeatherResponse.class);

        WeatherResponse body = response.getBody();

        if (body != null) {
            redisService.set("weather_of_" + city, body, 300L); // cache for 5 mins
            log.info("🌐 Weather data for '{}' fetched from API and cached in Redis", city);
        } else {
            log.warn("⚠️ API returned null response for city '{}'", city);
        }

        return body;
    }
}
