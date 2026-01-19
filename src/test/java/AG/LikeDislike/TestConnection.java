// src/main/java/AG/LikeDislike/TestConnection.java
package AG.LikeDislike;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.MongoTemplate;

@SpringBootApplication
public class TestConnection implements CommandLineRunner {

    private final MongoTemplate mongoTemplate;

    public TestConnection(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public static void main(String[] args) {
        SpringApplication.run(TestConnection.class, args);
    }

    @Override
    @SuppressWarnings("CallToPrintStackTrace")
    public void run(String... args) {
        try {
            // Obtener la base de datos especificada en la URI (likedislike)
            String dbName = mongoTemplate.getDb().getName();
            System.out.println("✅ ¡CONEXIÓN EXITOSA A MONGODB!");
            System.out.println("✅ Base de datos: " + dbName);
        } catch (Exception e) {
            System.err.println("❌ Error conectando a MongoDB:");
            e.printStackTrace();
        }
    }
}
