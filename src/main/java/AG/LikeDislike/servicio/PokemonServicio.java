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
public class PokemonServicio {

    private final WebClient webClient;
    private final Random random = new Random();

    @Value("${api.pokemon.base-url}")
    private String baseUrl;

    public PokemonServicio(WebClient webClient) {
        this.webClient = webClient;
    }

    @SuppressWarnings("unchecked")
    public PersonajeDTO getRandomCharacter() {
        try {
            int randomId = random.nextInt(898) + 1;

            log.info("Fetching Pokemon with ID: {}", randomId);

            String url = baseUrl + "/pokemon/" + randomId;

            Map<String, Object> response = webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            if (response == null) {
                throw new ExternalApiException("Failed to fetch character from Pokemon API");
            }

            return mapToPersonajeDTO(response, randomId);
        } catch (ExternalApiException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error getting random Pokemon", e);
            throw new ExternalApiException("Error connecting to Pokemon API: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private PersonajeDTO mapToPersonajeDTO(Map<String, Object> response, int apiId) {
        String name = (String) response.get("name");
        String capitalizedName = name.substring(0, 1).toUpperCase() + name.substring(1);

        Map<String, Object> sprites = (Map<String, Object>) response.get("sprites");
        Map<String, Object> other = (Map<String, Object>) sprites.get("other");
        Map<String, Object> officialArtwork = (Map<String, Object>) other.get("official-artwork");
        String imageUrl = (String) officialArtwork.get("front_default");

        return PersonajeDTO.builder()
                .idPersonaje("pokemon-" + apiId)
                .nombre(capitalizedName)
                .imagen(imageUrl)
                .source(PersonajeFuente.POKEMON)
                .likes(0)
                .dislikes(0)
                .totalVotes(0)
                .build();
    }

    @SuppressWarnings("unchecked")
    public PersonajeDTO getPikachu() {
        try {
            log.info("Fetching Pikachu (ID: 25)");

            String url = baseUrl + "/pokemon/25";

            Map<String, Object> response = webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            if (response == null) {
                throw new ExternalApiException("Failed to fetch Pikachu from Pokemon API");
            }

            return mapToPersonajeDTO(response, 25);
        } catch (ExternalApiException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error getting Pikachu", e);
            throw new ExternalApiException("Error connecting to Pokemon API: " + e.getMessage());
        }
    }
}
