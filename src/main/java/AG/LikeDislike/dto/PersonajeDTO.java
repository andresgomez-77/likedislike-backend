package AG.LikeDislike.dto;

import java.time.LocalDateTime;

import AG.LikeDislike.enums.PersonajeFuente;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class PersonajeDTO {
    private String id;
    private String idPersonaje;
    private String nombre;
    private String imagen;
    private PersonajeFuente source;
    private Integer likes;
    private Integer dislikes;
    private Integer totalVotes;
    private LocalDateTime lastEvaluatedAt;
    private LocalDateTime createdAt;
}
