package AG.LikeDislike.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class EstadisticasDTO {

    private PersonajeDTO personaje;
    private String mensaje;
    private Boolean existe;
}
