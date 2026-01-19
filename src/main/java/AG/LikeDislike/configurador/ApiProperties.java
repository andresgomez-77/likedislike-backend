package AG.LikeDislike.configurador;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "api")
public class ApiProperties {

    private RickMorty rickMorty = new RickMorty();
    private Pokemon pokemon = new Pokemon();
    private Superhero superhero = new Superhero();

    @Getter
    @Setter
    public static class RickMorty {

        private String baseUrl;
    }

    @Getter
    @Setter
    public static class Pokemon {

        private String baseUrl;
    }

    @Getter
    @Setter
    public static class Superhero {

        private String baseUrl;
        private String accessToken;
    }
}
