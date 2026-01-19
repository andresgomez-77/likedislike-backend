package AG.LikeDislike.dto;

import AG.LikeDislike.enums.TypeVote;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoteDTO {

    @NotNull(message = "Character ID cannot be null")
    private String idPersonaje;

    @NotNull(message = "Vote type cannot be null")
    private TypeVote typeVote;
}
