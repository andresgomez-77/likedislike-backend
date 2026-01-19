package AG.LikeDislike.controlador;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import AG.LikeDislike.dto.EstadisticasDTO;
import AG.LikeDislike.dto.PersonajeDTO;
import AG.LikeDislike.servicio.PersonajeServicio;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/estadisticas")
public class EstadisticasControlador {

    private final PersonajeServicio personajeServicio;

    public EstadisticasControlador(PersonajeServicio personajeServicio) {
        this.personajeServicio = personajeServicio;
    }

    /**
     * GET /api/estadisticas/mas-gustado Obtiene el personaje con más likes
     */
    @GetMapping("/mas-gustado")
    public ResponseEntity<PersonajeDTO> getMostLiked() {
        log.info("Request received: GET /api/estadisticas/mas-gustado");

        try {
            PersonajeDTO character = personajeServicio.getMostLiked();
            log.info("Most liked character: {} with {} likes",
                    character.getNombre(), character.getLikes());
            return ResponseEntity.ok(character);
        } catch (Exception e) {
            log.error("Error getting most liked character", e);
            throw e;
        }
    }

    /**
     * GET /api/estadisticas/menos-gustado Obtiene el personaje con más dislikes
     */
    @GetMapping("/menos-gustado")
    public ResponseEntity<PersonajeDTO> getMostDisliked() {
        log.info("Request received: GET /api/estadisticas/menos-gustado");

        try {
            PersonajeDTO character = personajeServicio.getMostDisliked();
            log.info("Most disliked character: {} with {} dislikes",
                    character.getNombre(), character.getDislikes());
            return ResponseEntity.ok(character);
        } catch (Exception e) {
            log.error("Error getting most disliked character", e);
            throw e;
        }
    }

    /**
     * GET /api/estadisticas/ultimo-evaluado Obtiene el último personaje evaluado
     */
    @GetMapping("/ultimo-evaluado")
    public ResponseEntity<PersonajeDTO> getLastEvaluated() {
        log.info("Request received: GET /api/estadisticas/ultimo-evaluado");

        try {
            PersonajeDTO character = personajeServicio.getLastEvaluated();
            log.info("Last evaluated character: {} at {}",
                    character.getNombre(), character.getLastEvaluatedAt());
            return ResponseEntity.ok(character);
        } catch (Exception e) {
            log.error("Error getting last evaluated character", e);
            throw e;
        }
    }

    /**
     * GET /api/estadisticas/pikachu Obtiene el estatus de Pikachu (BONUS) Retorna
     * información sobre si Pikachu existe en la BD, likes, dislikes, etc.
     */
    @GetMapping("/pikachu")
    public ResponseEntity<EstadisticasDTO> getPikachuStatus() {
        log.info("Request received: GET /api/estadisticas/pikachu");

        try {
            EstadisticasDTO estadisticas = personajeServicio.getPikachuStatus();
            log.info("Pikachu status: exists={}, message={}",
                    estadisticas.getExiste(), estadisticas.getMensaje());
            return ResponseEntity.ok(estadisticas);
        } catch (Exception e) {
            log.error("Error getting Pikachu status", e);
            throw e;
        }
    }
}
