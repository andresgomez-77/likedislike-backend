package AG.LikeDislike.configurador;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.client.MongoClient;

@Configuration
public class MongoConfig {

    @Bean
    public MongoTemplate mongoTemplate(MongoClient mongoClient) {
        // Especificar explícitamente la base de datos 'likedislike'
        return new MongoTemplate(mongoClient, "likedislike");
    }
}
