package AG.LikeDislike;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
@EnableMongoAuditing
public class LikeDislikeApplication {

    public static void main(String[] args) {
        log.info("=".repeat(60));
        log.info("Iniciando Aplicacion LikeDislike...");
        log.info("=".repeat(60));

        // 🔍 DEBUG: Ver qué URI está usando
        String mongoUri = System.getenv("SPRING_DATA_MONGODB_URI");
        System.out.println("================================================");
        System.out.println("🔍 MONGODB_URI desde variable de entorno:");
        System.out.println(mongoUri != null ? mongoUri : "❌ NO ENCONTRADA");
        System.out.println("================================================");

        SpringApplication.run(LikeDislikeApplication.class, args);

        log.info("=".repeat(60));
        log.info("LikeDislike Aplicacion Iniciada con Exito!");
        log.info("Server correindo en el puerto: http://localhost:8080");
        log.info("API Endpoints:");
        log.info("   GET  /api/personaje/aleatorio");
        log.info("   POST /api/personaje/vote");
        log.info("   GET  /api/estadisticas/mas-gustado");
        log.info("   GET  /api/estadisticas/menos-gustado");
        log.info("   GET  /api/estadisticas/ultimo-evaluado");
        log.info("   GET  /api/estadisticas/pikachu");
        log.info("=".repeat(60));
    }

}
