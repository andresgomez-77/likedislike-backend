package AG.LikeDislike.servicio;

import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import AG.LikeDislike.dto.PersonajeDTO;
import AG.LikeDislike.enums.PersonajeFuente;
import AG.LikeDislike.excepciones.ExternalApiException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SuperheroeServicio {

    private final RestTemplate restTemplate;
    private final Random random = new Random();

    @Value("${api.superhero.base-url}")
    private String baseUrl;

    @Value("${api.superhero.access-token}")
    private String accessToken;

    public SuperheroeServicio() {
        this.restTemplate = new RestTemplate();
    }

    @SuppressWarnings("unchecked")
    public PersonajeDTO getRandomCharacter() {
        int maxAttempts = 10;
        
        for (int attempt = 0; attempt < maxAttempts; attempt++) {
            try {
                int randomId = random.nextInt(731) + 1;
                
                String url = baseUrl + "/" + accessToken + "/" + randomId;
                
                log.info("Fetching Superhero ID: {} - URL: {}", randomId, url);
                
                ResponseEntity<Map> responseEntity = restTemplate.getForEntity(url, Map.class);
                Map<String, Object> response = responseEntity.getBody();

                if (response == null) {
                    log.warn("Null response for Superhero ID: {}", randomId);
                    continue;
                }
                
                String responseStatus = (String) response.get("response");
                
                if (!"success".equals(responseStatus)) {
                    log.warn("Non-success response for ID {}: {}", randomId, responseStatus);
                    continue;
                }
                
                String name = (String) response.get("name");
                if (name == null || name.trim().isEmpty()) {
                    log.warn("Invalid name for Superhero ID: {}", randomId);
                    continue;
                }

                log.info("✅ Successfully fetched Superhero: {}", name);
                return mapToPersonajeDTO(response, randomId);
                
            } catch (RestClientException e) {
                log.warn("RestClient error (Attempt {}): {}", attempt + 1, e.getMessage());
            } catch (Exception e) {
                log.error("Unexpected error (Attempt {}): {}", attempt + 1, e.getMessage());
            }
        }
        
        log.error("❌ Failed to fetch superhero after {} attempts", maxAttempts);
        throw new ExternalApiException("Failed to fetch valid superhero after " + maxAttempts + " attempts");
    }

    @SuppressWarnings("unchecked")
    private PersonajeDTO mapToPersonajeDTO(Map<String, Object> response, int apiId) {
        try {
            String name = (String) response.get("name");
            Map<String, Object> imageMap = (Map<String, Object>) response.get("image");
            String imageUrl = (String) imageMap.get("url");

            return PersonajeDTO.builder()
                    .idPersonaje("superhero-" + apiId)
                    .nombre(name)
                    .imagen(imageUrl)
                    .source(PersonajeFuente.SUPERHERO)
                    .likes(0)
                    .dislikes(0)
                    .totalVotes(0)
                    .build();
        } catch (Exception e) {
            log.error("Error mapping superhero response", e);
            throw new ExternalApiException("Invalid superhero response format");
        }
    }
}
