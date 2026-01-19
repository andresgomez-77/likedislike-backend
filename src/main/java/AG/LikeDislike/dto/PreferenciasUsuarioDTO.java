package AG.LikeDislike.dto;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PreferenciasUsuarioDTO {

    private Map<String, Integer> sourceLikes;      // {POKEMON: 5, RICK_MORTY: 2}
    private Map<String, Integer> sourceDislikes;   // {SUPERHERO: 3}
    private String preferredSource;                 // "POKEMON"
    private Double preferencePercentage;            // 0.60 (60%)
}
