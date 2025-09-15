package ca.hitkumar.journalApp.services;

import ca.hitkumar.journalApp.api.response.WeatherResponse;
import ca.hitkumar.journalApp.cache.AppCache;
import ca.hitkumar.journalApp.constants.Placeholders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {

    @Value("${weather.api.key}")
    private String apikey;

    private static final String API = "http://api.weatherstack.com/current?access_key=API_KEY&query=CITY";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AppCache appCache;

    public WeatherResponse getWeather(String city){

        String finalAPIUrl = appCache.appCache.get(AppCache.keys.WEATHER_API.toString()).replace(Placeholders.API_KEY, apikey).replace(Placeholders.CITY, city);

//        Set Headers
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("key", "value");

//        can use any 1 method from below 2 methods
//        String requestBody = "{ \"username\": \"hit\", \"password\": \"123\" }";
//        HttpEntity<String> httpEntity = new HttpEntity<>(requestBody);

//        UserEntry user = UserEntry.builder().username("hit").password("hit").build();
//        HttpEntity<UserEntry> httpEntity = new HttpEntity<>(user, httpHeaders);

//        POST
//      ResponseEntity<WeatherResponse> response = restTemplate.exchange(finalAPIUrl, HttpMethod.POST, httpEntity, WeatherResponse.class);

//        GET
        ResponseEntity<WeatherResponse> response = restTemplate.exchange(finalAPIUrl, HttpMethod.GET, null, WeatherResponse.class);
        return response.getBody();
    }
}
