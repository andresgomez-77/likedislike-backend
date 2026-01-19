package AG.LikeDislike.servicio;

import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import AG.LikeDislike.dto.PersonajeDTO;
import AG.LikeDislike.enums.PersonajeFuente;
import AG.LikeDislike.excepciones.ExternalApiException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RickMortyServicio {

    private final WebClient webClient;
    private final Random random = new Random();

    @Value("${api.rickmorty.base-url}")
    private String baseUrl;

    public RickMortyServicio(WebClient webClient) {
        this.webClient = webClient;
    }

    @SuppressWarnings("unchecked")
    public PersonajeDTO getRandomCharacter() {
        try {
            int randomId = random.nextInt(826) + 1;

            log.info("Fetching Rick and Morty character with ID: {}", randomId);

            String url = baseUrl + "/character/" + randomId;

            Map<String, Object> response = webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            if (response == null) {
                throw new ExternalApiException("Failed to fetch character from Rick and Morty API");
            }

            return mapToPersonajeDTO(response, randomId);
        } catch (ExternalApiException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error getting random Rick and Morty character", e);
            throw new ExternalApiException("Error connecting to Rick and Morty API: " + e.getMessage());
        }
    }

    private PersonajeDTO mapToPersonajeDTO(Map<String, Object> response, int apiId) {
        return PersonajeDTO.builder()
                .idPersonaje("rickmorty-" + apiId)
                .nombre((String) response.get("name"))
                .imagen((String) response.get("image"))
                .source(PersonajeFuente.RICK_MORTY)
                .likes(0)
                .dislikes(0)
                .totalVotes(0)
                .build();
    }
}
