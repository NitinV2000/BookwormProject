package com.tintin.bookworm.search;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@RestController
public class SearchController {

    private final WebClient webClient;

    public SearchController(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.exchangeStrategies(ExchangeStrategies.builder()
        .codecs(configurer -> configurer
                  .defaultCodecs()
                  .maxInMemorySize(16 * 1024 * 1024))
                .build()).baseUrl("http://openlibrary.org/search.json").build();
    }

    @GetMapping(value = "/search")
    public SearchResult getSearchResults(@RequestParam String query) {
        Mono<SearchResult> resultsMono = this.webClient.get()
            .uri("?q={query}", query)
            .retrieve().bodyToMono(SearchResult.class);
        SearchResult result = resultsMono.block();
        List<SearchResultBook> books = result.getDocs()
            .stream()
            .limit(10)
            .map(bookResult -> {
                bookResult.setKey(bookResult.getKey().replace("/works/", ""));
                return bookResult;
            })
            .collect(Collectors.toList());
        result.setDocs(books);

        return result;
    }
    
}
