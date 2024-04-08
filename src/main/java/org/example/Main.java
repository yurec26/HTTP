package org.example;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHeaders;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;


import java.io.IOException;
import java.util.List;

public class Main {
    public record Fact(String id, String text, String type, String user, String upvotes) {
        @JsonCreator
        public static Fact createFact(@JsonProperty("id") String id,
                                      @JsonProperty("text") String text,
                                      @JsonProperty("type") String type,
                                      @JsonProperty("user") String user,
                                      @JsonProperty("upvotes") String upvotes) {
            return new Fact(id, text, type, user, upvotes);
        }
    }

    public static void main(String[] args) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)    // максимальное время ожидание подключения к серверу
                        .setSocketTimeout(30000)    // максимальное время ожидания получения данных
                        .setRedirectsEnabled(false) // возможность следовать редиректу в ответе
                        .build())
                .build();
        //
        HttpGet request = new HttpGet("https://raw.githubusercontent.com/netology-code/jd-homeworks/master/http/task1/cats");
        request.setHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType());
        //
        CloseableHttpResponse response = httpClient.execute(request);
        //
        ObjectMapper objectMapper = new ObjectMapper();
        //
        List<Fact> list = objectMapper.readValue(response.getEntity().getContent(), new TypeReference<>() {
        });
        //
        list.stream().filter(s -> s.upvotes != null).forEach(System.out::println);
    }
}