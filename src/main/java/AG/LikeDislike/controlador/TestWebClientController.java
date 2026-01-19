package AG.LikeDislike.controlador;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@RestController
@RequestMapping("/api/test")

public class TestWebClientController {
        private final WebClient webClient;

    public TestWebClientController(WebClient webClient) {
        this.webClient = webClient;
    }

    @SuppressWarnings("unchecked")
    @GetMapping("/rickmorty")
    public Map<String, Object> testRickMorty() {
        try {
            log.info("Testing Rick and Morty API...");
            
            String url = "https://rickandmortyapi.com/api/character/1";
            
            Map<String, Object> response = webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();
            
            log.info("Rick and Morty API Response: {}", response);
            return response;
            
        } catch (Exception e) {
            log.error("Error testing Rick and Morty API", e);
            return Map.of("error", e.getMessage());
        }
    }

    @GetMapping("/pokemon")
    public Map<String, Object> testPokemon() {
        try {
            log.info("Testing Pokemon API...");
            
            String url = "https://pokeapi.co/api/v2/pokemon/25";
            
            Map<String, Object> response = webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();
            
            log.info("Pokemon API Response: {}", response);
            return response;
            
        } catch (Exception e) {
            log.error("Error testing Pokemon API", e);
            return Map.of("error", e.getMessage());
        }
    }

    @GetMapping("/superhero")
    public Map<String, Object> testSuperhero() {
        try {
            log.info("Testing Superhero API...");
            
            String url = "https://superheroapi.com/api/2648e74d6106278362f373210efdca82/70";
            
            Map<String, Object> response = webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();
            
            log.info("Superhero API Response: {}", response);
            return response;
            
        } catch (Exception e) {
            log.error("Error testing Superhero API", e);
            return Map.of("error", e.getMessage());
        }
    }
}
