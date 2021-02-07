//package com.alna.reactor3;
//
//import reactor.core.publisher.Mono;
//
//import java.net.http.HttpClient;
//
//public class FlatMap {
//  private HttpClient httpClient;
//
//  Mono<User> findUser(String username) {
//    String queryUrl = "http://my-api-address/users/" + username;
//
//    return Mono.fromCallable(() -> httpClient.get(queryUrl)).
//        flatMap(response -> {
//          if (response.statusCode == 404) return Mono.error(new NotFoundException("User " + username + " not found"));
//          else if (response.statusCode == 500) return Mono.error(new InternalServerErrorException());
//          else if (response.statusCode != 200) return Mono.error(new Exception("Unknown error calling my-api"));
//          return Mono.just(response.data);
//        });
//  }
//
//}
//  public static void main(String[] args) {
//
//  }
//}
