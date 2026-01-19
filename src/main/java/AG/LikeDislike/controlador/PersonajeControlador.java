package AG.LikeDislike.controlador;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import AG.LikeDislike.dto.PersonajeDTO;
import AG.LikeDislike.dto.PreferenciasUsuarioDTO;
import AG.LikeDislike.dto.VoteDTO;
import AG.LikeDislike.servicio.PersonajeServicio;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/personaje")
public class PersonajeControlador {

    private final PersonajeServicio personajeServicio;

    public PersonajeControlador(PersonajeServicio personajeServicio) {
        this.personajeServicio = personajeServicio;
    }

    /**
     * GET /api/personaje/aleatorio Obtiene un personaje aleatorio de Rick &
     * Morty, Pokemon o Superhero
     */
    @GetMapping("/aleatorio")
    public ResponseEntity<PersonajeDTO> getRandomCharacter() {
        log.info("Request received: GET /api/personaje/aleatorio");

        try {
            PersonajeDTO personaje = personajeServicio.getRandomCharacter();
            log.info("Returning random character: {} ({})", personaje.getNombre(), personaje.getSource());
            return ResponseEntity.ok(personaje);
        } catch (Exception e) {
            log.error("Error getting random character", e);
            throw e;
        }
    }

    /**
     * POST /api/personaje/recomendado Obtiene un personaje basado en las
     * preferencias del usuario
     */
    @PostMapping("/recomendado")
    public ResponseEntity<PersonajeDTO> getRecommendedCharacter(
            @RequestBody PreferenciasUsuarioDTO preferences) {
        log.info("Request received: POST /api/personaje/recomendado with preferences: {}",
                preferences);

        PersonajeDTO personaje = personajeServicio.getRandomCharacterWithPreferences(preferences);
        return ResponseEntity.ok(personaje);
    }

    /**
     * POST /api/personaje/vote Registra un voto (like o dislike) para un
     * personaje
     *
     * Body ejemplo: { "idPersonaje": "pokemon-25", "voteType": "LIKE" }
     */
    @PostMapping("/vote")
    public ResponseEntity<PersonajeDTO> voteCharacter(@Valid @RequestBody VoteDTO voteDTO) {
        log.info("Request received: POST /api/personaje/vote - Character: {}, Vote: {}",
                voteDTO.getIdPersonaje(), voteDTO.getTypeVote());

        try {
            PersonajeDTO personaje = personajeServicio.voteCharacter(voteDTO);
            log.info("Vote registered successfully for character: {}", personaje.getNombre());
            return ResponseEntity.status(HttpStatus.CREATED).body(personaje);
        } catch (Exception e) {
            log.error("Error voting for character: {}", voteDTO.getIdPersonaje(), e);
            throw e;
        }
    }
}
