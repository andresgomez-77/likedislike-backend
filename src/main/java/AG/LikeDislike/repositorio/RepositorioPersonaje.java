package AG.LikeDislike.repositorio;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import AG.LikeDislike.modelo.Personaje;

@Repository

public interface RepositorioPersonaje extends MongoRepository<Personaje, String> {
    // Find character by idPersonaje (unique identifier: source-id)

    Optional<Personaje> findByIdPersonaje(String idPersonaje);

    // Check if character exists by idPersonaje
    boolean existsByIdPersonaje(String idPersonaje);

    // Find character with most likes
    Optional<Personaje> findTopByOrderByLikesDesc();

    // Find character with most dislikes
    Optional<Personaje> findTopByOrderByDislikesDesc();

    // Find last evaluated character
    Optional<Personaje> findTopByOrderByLastEvaluatedAtDesc();

    // Find Pikachu specifically (pokemon-25)
    @Query("{ 'idPersonaje': 'pokemon-25' }")
    Optional<Personaje> findPikachu();
}
