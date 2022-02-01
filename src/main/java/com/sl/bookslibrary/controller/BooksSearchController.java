package com.sl.bookslibrary.controller;

import com.sl.bookslibrary.model.SearchResult;
import com.sl.bookslibrary.model.SearchResultBook;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

import static com.sl.bookslibrary.utils.AppConstants.COVER_ID_ROOT;

@Controller
@Slf4j
public class BooksSearchController {

    private final WebClient webClient;

    public BooksSearchController(WebClient.Builder webclientBuilder) {
        this.webClient = webclientBuilder.exchangeStrategies(ExchangeStrategies.builder()
                .codecs(configurer -> configurer
                            .defaultCodecs()
                            .maxInMemorySize(16 * 1024 * 1024))
                        .build())
                .baseUrl("http://openlibrary.org/search.json")
                .filter(logRequest())
                .build();
    }

    @GetMapping(value = "/search")
    public String getSearchResults(@RequestParam String queryParam, Model model) {
        Mono<SearchResult> result = webClient.get().uri("?q={queryParam}", queryParam)
                .retrieve().bodyToMono(SearchResult.class);

        SearchResult sResult = result.block();

        log.info("> uri str: {}", webClient.get().toString());

        log.info("> Executed search for query: {}", queryParam);
        log.info("> Found results of size: {}", sResult.getDocs().size());

        List<SearchResultBook> books = sResult.getDocs()
                .stream()
                .limit(10)
                .map(sBook -> {
                    sBook.setKey(sBook.getKey().replace("/works/", ""));
                    if(StringUtils.hasText(sBook.getCover_i())) {
                        sBook.setCover_i(COVER_ID_ROOT + sBook.getCover_i() + "-M.jpg");
                    } else {
                        sBook.setCover_i("/images/no-image-found.PNG");
                    }
                    return sBook;
                })
                .collect(Collectors.toList());

        model.addAttribute("searchResults", books);
        return "search-results";
    }

    private static ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            log.info("> Request: {} {}", clientRequest.method(), clientRequest.url());
            clientRequest.headers().forEach((name, values) -> values.forEach(value -> log.info("{}={}", name, value)));
            return Mono.just(clientRequest);
        });
    }
}
