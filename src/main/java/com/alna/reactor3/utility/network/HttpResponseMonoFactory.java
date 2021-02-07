package com.alna.reactor3.utility.network;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.time.Duration;
import java.util.Observable;
import java.util.concurrent.CompletableFuture;

public class HttpResponseMonoFactory {

    private final static Logger log = LoggerFactory.getLogger(HttpResponseMonoFactory.class);

    public static Mono<Integer> additionRequestResponseObservable(URI httpUri) {

        log.info("Creating observable for: {}", httpUri.toString());

        HttpClient httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofSeconds(20))
                .build();

        HttpRequest request = HttpRequest.newBuilder(httpUri)
                .GET()
                .build();

        CompletableFuture<HttpResponse<String>> futureResponse =
                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString(Charset.forName("UTF-8")));

        return Mono.fromFuture(futureResponse)
                .map(HttpResponse::body)
                .map(Integer::parseInt)
                .doOnNext(responseInteger -> log.info("Response: {}", responseInteger));
    }
}
