package com.umut.weather.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Constant {
    public static String API_URL;
    public static String API_KEY;

    public static final String ACCESS_KEY_PARAM = "?access_key=";
    public static final String QUERY_KEY_PARAM = "&query=";

    // runtimeda environmenttan okuyor ve static olmasına rağmen boş string olarak set etmiyor.
    @Value("${weather.api-url}")
    public void setApiUrl(String apiUrl) {
        Constant.API_URL = apiUrl;
    }

    @Value("${weather.api-key}")
    public void setApiKey(String apiKey) {
        Constant.API_KEY = apiKey;
    }

    public static class ERROR_MESSAGES {
        public static final String GENERIC = "Unknown Error!";
        public static final String REQUEST_LIMIT_EXCEEDED = "Request Limit Exceeded!";
    }

}
