package com.facebookapi.fb.controller;

import com.facebookapi.fb.entity.page_access_token.PageAccessToken;
import com.facebookapi.fb.entity.published_posts.PublishedPosts;
import com.facebookapi.fb.entity.published_posts.PublishedPostsSummary;
import com.facebookapi.fb.exception.SystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
public class FacebookApiController {

    @Autowired
    RestTemplate restTemplate;

    @GetMapping("total_counts")
    public ResponseEntity<PublishedPostsSummary> posts() throws SystemException {

        String uriStringForPageAccessToken = "https://graph.facebook.com/v8.0/101761407924128?fields=access_token&access_token=EAAESORP5PhEBAMsnBgfG2UMqEYYRVEoGXmNhBqwUO20nZBKv3to4xGd49qjyDvMIGcZAjmeNyJgJBSZBD6vIZAfJdoNIQkmuV5BJJgwfyMwFiYsK3WDE5G4C8LraSEbZCPCBQ4xuGVqUIDhxvpNVlPDuJ2IJX4rg0QFCl4ZBcZAxLyITgvZCI5seVMRXP2Nm4B0ZD";
        URI uriForPageAccessToken;
        try {
            uriForPageAccessToken = new URI(uriStringForPageAccessToken);
        } catch (URISyntaxException e) {
            throw new SystemException("URIに指定した文字列が不正です。" + uriStringForPageAccessToken, e);
        }

        ResponseEntity<PageAccessToken> pageAccessTokenResponseEntity = restTemplate.getForEntity(uriForPageAccessToken,PageAccessToken.class);

        String pageAccessToken = pageAccessTokenResponseEntity.getBody().getAccessToken();

        String uriStringForPublishedPostsCount = "https://graph.facebook.com/v8.0/101761407924128/published_posts?limit=1&summary=total_count&since=1";

        String accessTokenParamString = "&access_token=" + pageAccessToken;
        uriStringForPublishedPostsCount += accessTokenParamString;

        URI uriForPublishedPostsCount;
        try {
            uriForPublishedPostsCount = new URI(uriStringForPublishedPostsCount);
        } catch (URISyntaxException e) {
            throw new SystemException("URIに指定した文字列が不正です。" + uriStringForPublishedPostsCount, e);
        }

        ResponseEntity<PublishedPosts> publishedPostsResponseEntity = restTemplate.getForEntity(uriForPublishedPostsCount, PublishedPosts.class);

        PublishedPostsSummary publishedPostsSummary= publishedPostsResponseEntity.getBody().getSummary();

        return ResponseEntity.ok().body(publishedPostsSummary);
    }
}
