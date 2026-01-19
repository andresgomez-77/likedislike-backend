package AG.LikeDislike.servicio;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import AG.LikeDislike.dto.EstadisticasDTO;
import AG.LikeDislike.dto.PersonajeDTO;
import AG.LikeDislike.dto.PreferenciasUsuarioDTO;
import AG.LikeDislike.dto.VoteDTO;
import AG.LikeDislike.enums.PersonajeFuente;
import AG.LikeDislike.enums.TypeVote;
import AG.LikeDislike.excepciones.PersonajesNoExepciones;
import AG.LikeDislike.modelo.Personaje;
import AG.LikeDislike.repositorio.RepositorioPersonaje;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PersonajeServicio {

    private final RepositorioPersonaje repositorioPersonaje;
    private final RickMortyServicio rickMortyServicio;
    private final PokemonServicio pokemonServicio;
    private final SuperheroeServicio superheroServicio;
    private final Random random = new Random();

    public PersonajeServicio(
            RepositorioPersonaje repositorioPersonaje,
            RickMortyServicio rickMortyServicio,
            PokemonServicio pokemonServicio,
            SuperheroeServicio superheroServicio) {
        this.repositorioPersonaje = repositorioPersonaje;
        this.rickMortyServicio = rickMortyServicio;
        this.pokemonServicio = pokemonServicio;
        this.superheroServicio = superheroServicio;
    }

    public PersonajeDTO getRandomCharacter() {
        // Randomly select a source (0 = Rick&Morty, 1 = Pokemon, 2 = Superhero)
        int fuenteIndice = random.nextInt(3);

        PersonajeDTO PersonajeDTO;

        switch (fuenteIndice) {
            case 0 -> {
                log.info("Fetching random Rick and Morty character");
                PersonajeDTO = rickMortyServicio.getRandomCharacter();
            }
            case 1 -> {
                log.info("Fetching random Pokemon");
                PersonajeDTO = pokemonServicio.getRandomCharacter();
            }
            case 2 -> {
                log.info("Fetching random Superhero");
                PersonajeDTO = superheroServicio.getRandomCharacter();
            }
            default ->
                throw new IllegalStateException("Invalid source index: " + fuenteIndice);
        }

        return enrichWithDatabaseInfo(PersonajeDTO);
    }

    // NUEVO: Método con algoritmo inteligente basado en preferencias
    public PersonajeDTO getRandomCharacterWithPreferences(PreferenciasUsuarioDTO preferences) {
        log.info("Fetching character with user preferences: {}", preferences);

        // Seleccionar fuente basada en preferencias
        PersonajeFuente selectedSource = selectSourceByPreferences(preferences);

        log.info("Selected source based on preferences: {}", selectedSource);

        // Obtener personaje de la fuente seleccionada
        PersonajeDTO personajeDTO = fetchCharacterFromSource(selectedSource);

        return enrichWithDatabaseInfo(personajeDTO);
    }

    // Algoritmo de selección ponderada por preferencias
    private PersonajeFuente selectSourceByPreferences(PreferenciasUsuarioDTO preferences) {
        Map<String, Integer> likes = preferences.getSourceLikes();

        if (likes == null || likes.isEmpty()) {
            log.info("No preferences found, using random selection");
            return PersonajeFuente.values()[random.nextInt(PersonajeFuente.values().length)];
        }

        // Calcular total de likes
        int totalLikes = likes.values().stream()
                .mapToInt(Integer::intValue)
                .sum();

        if (totalLikes == 0) {
            log.info("No likes registered, using random selection");
            return PersonajeFuente.values()[random.nextInt(PersonajeFuente.values().length)];
        }

        // Obtener pesos de cada fuente
        int rickMortyWeight = likes.getOrDefault("RICK_MORTY", 0);
        int pokemonWeight = likes.getOrDefault("POKEMON", 0);
        int superheroWeight = likes.getOrDefault("SUPERHERO", 0);

        // Agregar peso mínimo (15%) para mantener variedad
        // Esto asegura que siempre haya algo de diversidad
        int minWeight = Math.max(1, totalLikes / 6);
        rickMortyWeight = Math.max(rickMortyWeight, minWeight);
        pokemonWeight = Math.max(pokemonWeight, minWeight);
        superheroWeight = Math.max(superheroWeight, minWeight);

        int totalWeight = rickMortyWeight + pokemonWeight + superheroWeight;
        int randomValue = random.nextInt(totalWeight);

        log.info("Weights - Rick&Morty: {}, Pokemon: {}, Superhero: {}, Total: {}, Random: {}",
                rickMortyWeight, pokemonWeight, superheroWeight, totalWeight, randomValue);

        // Selección ponderada
        if (randomValue < rickMortyWeight) {
            return PersonajeFuente.RICK_MORTY;
        } else if (randomValue < rickMortyWeight + pokemonWeight) {
            return PersonajeFuente.POKEMON;
        } else {
            return PersonajeFuente.SUPERHERO;
        }
    }

    // Helper: Obtener personaje de una fuente específica
    private PersonajeDTO fetchCharacterFromSource(PersonajeFuente source) {
        return switch (source) {
            case RICK_MORTY -> {
                log.info("Fetching Rick and Morty character");
                yield rickMortyServicio.getRandomCharacter();
            }
            case POKEMON -> {
                log.info("Fetching Pokemon character");
                yield pokemonServicio.getRandomCharacter();
            }
            case SUPERHERO -> {
                log.info("Fetching Superhero character");
                yield superheroServicio.getRandomCharacter();
            }
        };
    }

    // Helper: Enriquecer DTO con información de la base de datos
    private PersonajeDTO enrichWithDatabaseInfo(PersonajeDTO personajeDTO) {
        Optional<Personaje> existingCharacter = repositorioPersonaje
                .findByIdPersonaje(personajeDTO.getIdPersonaje());

        if (existingCharacter.isPresent()) {
            Personaje personaje = existingCharacter.get();
            personajeDTO.setLikes(personaje.getLikes());
            personajeDTO.setDislikes(personaje.getDislikes());
            personajeDTO.setTotalVotes(personaje.getTotalVotes());
            personajeDTO.setLastEvaluatedAt(personaje.getLastEvaluatedAt());
            personajeDTO.setCreatedAt(personaje.getCreatedAt());
        }

        return personajeDTO;
    }

    @Transactional
    public PersonajeDTO voteCharacter(VoteDTO voteDTO) {
        String idPersonaje = voteDTO.getIdPersonaje();
        TypeVote voteType = voteDTO.getTypeVote();

        log.info("Processing vote: {} for character: {}", voteType, idPersonaje);

        // Find or create character
        Personaje personaje = repositorioPersonaje.findByIdPersonaje(idPersonaje)
                .orElseGet(() -> createNewCharacter(idPersonaje));

        // Update votes
        if (voteType == TypeVote.LIKE) {
            personaje.incrementLikes();
        } else {
            personaje.incrementDislikes();
        }

        personaje.setUpdatedAt(LocalDateTime.now());

        // Save to database
        Personaje savedPersonaje = repositorioPersonaje.save(personaje);

        log.info("Vote saved successfully for character: {}", idPersonaje);

        return mapToDTO(savedPersonaje);
    }

    public PersonajeDTO getMostLiked() {
        log.info("Fetching character with most likes");

        return repositorioPersonaje.findTopByOrderByLikesDesc()
                .map(this::mapToDTO)
                .orElseThrow(() -> new PersonajesNoExepciones("No characters have been voted yet"));
    }

    public PersonajeDTO getMostDisliked() {
        log.info("Fetching character with most dislikes");

        return repositorioPersonaje.findTopByOrderByDislikesDesc()
                .map(this::mapToDTO)
                .orElseThrow(() -> new PersonajesNoExepciones("No characters have been voted yet"));
    }

    public PersonajeDTO getLastEvaluated() {
        log.info("Fetching last evaluated character");

        return repositorioPersonaje.findTopByOrderByLastEvaluatedAtDesc()
                .map(this::mapToDTO)
                .orElseThrow(() -> new PersonajesNoExepciones("No characters have been evaluated yet"));
    }

    public EstadisticasDTO getPikachuStatus() {
        log.info("Fetching Pikachu status");

        Optional<Personaje> pikachuOpt = repositorioPersonaje.findPikachu();

        if (pikachuOpt.isPresent()) {
            Personaje pikachu = pikachuOpt.get();
            return EstadisticasDTO.builder()
                    .personaje(mapToDTO(pikachu))
                    .mensaje("Pikachu exists in database")
                    .existe(true)
                    .build();
        } else {
            // Fetch Pikachu from API to show available data
            PersonajeDTO pikachuDTO = pokemonServicio.getPikachu();
            return EstadisticasDTO.builder()
                    .personaje(pikachuDTO)
                    .mensaje("Pikachu has not been evaluated yet")
                    .existe(false)
                    .build();
        }
    }

    private Personaje createNewCharacter(String idPersonaje) {
        log.info("Creating new character record for: {}", idPersonaje);

        // Extraer fuente del idPersonaje (formato: "fuente-id")
        String sourceStr = idPersonaje.substring(0, idPersonaje.indexOf('-'));
        PersonajeFuente source = PersonajeFuente.fromValue(sourceStr);

        // Obtener datos del personaje usando el helper
        PersonajeDTO personajeDTO = fetchCharacterFromSource(source);

        return Personaje.builder()
                .idPersonaje(idPersonaje)
                .nombre(personajeDTO.getNombre())
                .imagen(personajeDTO.getImagen())
                .source(source)
                .likes(0)
                .dislikes(0)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    private PersonajeDTO mapToDTO(Personaje pikachu) {
        return PersonajeDTO.builder()
                .id(pikachu.getId())
                .idPersonaje(pikachu.getIdPersonaje())
                .nombre(pikachu.getNombre())
                .imagen(pikachu.getImagen())
                .source(pikachu.getSource())
                .likes(pikachu.getLikes())
                .dislikes(pikachu.getDislikes())
                .totalVotes(pikachu.getTotalVotes())
                .lastEvaluatedAt(pikachu.getLastEvaluatedAt())
                .createdAt(pikachu.getCreatedAt())
                .build();
    }
}
