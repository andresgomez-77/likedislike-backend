package AG.LikeDislike.modelo;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import AG.LikeDislike.enums.PersonajeFuente;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "personajes")

public class Personaje {

    @Id
    private String id;

    @Indexed(unique = true)
    private String idPersonaje; // Format: "source-id" (e.g., "pokemon-25", "rickmorty-1")

    private String nombre;

    private String imagen;

    @Indexed
    private PersonajeFuente source;

    @Builder.Default
    private Integer likes = 0;

    @Builder.Default
    private Integer dislikes = 0;

    @Indexed
    private LocalDateTime lastEvaluatedAt;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    // Helper method to increment likes
    public void incrementLikes() {
        this.likes++;
        this.lastEvaluatedAt = LocalDateTime.now();
    }

    // Helper method to increment dislikes
    public void incrementDislikes() {
        this.dislikes++;
        this.lastEvaluatedAt = LocalDateTime.now();
    }

    // Helper method to get total votes
    public Integer getTotalVotes() {
        return this.likes + this.dislikes;
    }
}
