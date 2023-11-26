package com.ssafy.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@RestController
@RequestMapping("/api/search")
public class KakaoSearchProxy {
    private static final String KakaoAK_KEY = "1124ee806e4487f632b0cff20ac28f7d";

    private WebClient client;

    public KakaoSearchProxy(){
        client = WebClient.builder().baseUrl("https://dapi.kakao.com/v2/search/blog")
                .defaultHeader("Authorization", "KakaoAK " + KakaoAK_KEY)
                .build();
    }

    @GetMapping
    public ResponseEntity<String> blogSearch(@RequestParam String keyword){
        ResponseEntity<String> res = client.get().uri("?query=" + keyword).retrieve().toEntity(String.class).block();
        //log.debug("res : {}", res);

        return res;
    }
}

