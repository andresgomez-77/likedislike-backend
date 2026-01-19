#  Like/Dislike - Backend API

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-6DB33F?style=for-the-badge&logo=springboot)
![Java](https://img.shields.io/badge/Java-17-007396?style=for-the-badge&logo=java)
![MongoDB](https://img.shields.io/badge/MongoDB-6.0-47A248?style=for-the-badge&logo=mongodb)

API RESTful para sistema de votación de personajes con integración de múltiples fuentes (Rick & Morty, Pokémon, Superhéroes) y sistema de recomendaciones basado en preferencias del usuario.

---

## 📋 Tabla de Contenidos

- [Características](#-características)
- [Stack Tecnológico](#-stack-tecnológico)
- [Requisitos Previos](#-requisitos-previos)
- [Instalación](#-instalación)
- [Configuración](#-configuración)
- [Ejecución](#-ejecución)
- [Estructura del Proyecto](#-estructura-del-proyecto)
- [Endpoints de la API](#-endpoints-de-la-api)
- [Modelos de Datos](#-modelos-de-datos)
- [APIs Externas](#-apis-externas)
- [Sistema de Recomendaciones](#-sistema-de-recomendaciones)
- [Base de Datos](#-base-de-datos)
- [Testing](#-testing)
- [Troubleshooting](#-troubleshooting)
- [Licencia](#-licencia)

---

##  Características

-  **Integración con 3 APIs externas**: Rick & Morty, PokéAPI, Superhero API
-  **Sistema de recomendaciones** basado en preferencias del usuario
-  **Estadísticas en tiempo real** de votaciones
-  **Búsqueda aleatoria** de personajes con distribución equitativa
-  **CORS configurado** para comunicación con frontend
-  **Logging detallado** para debugging
-  **Caché de personajes** para optimizar rendimiento
-  **Manejo robusto de errores** con mensajes descriptivos

---

## 🛠 Stack Tecnológico

| Tecnología | Versión | Propósito |
|-----------|---------|-----------|
| **Java** | 17 LTS | Lenguaje de programación |
| **Spring Boot** | 3.x | Framework backend |
| **Spring Data MongoDB** | 3.x | ODM para MongoDB |
| **MongoDB** | 6.0+ | Base de datos NoSQL |
| **Lombok** | 1.18+ | Reducción de boilerplate |
| **RestTemplate** | - | Cliente HTTP para APIs externas |
| **Maven** | 3.8+ | Gestor de dependencias |
| **Railway** | - | Plataforma de despliegue |

---

##  Requisitos Previos

Antes de comenzar, asegúrate de tener instalado:

-  **Java JDK** >= 17 ([Descargar OpenJDK](https://adoptium.net/))
-  **Maven** >= 3.8 ([Descargar aquí](https://maven.apache.org/download.cgi))
-  **MongoDB** >= 6.0 ([Descargar aquí](https://www.mongodb.com/try/download/community))
-  **Git** ([Descargar aquí](https://git-scm.com/))
-  **IDE recomendado**: IntelliJ IDEA o Eclipse

Verifica las versiones instaladas:
```bash
java -version
mvn -version
mongod --version
git --version
```

---

##  Instalación

### 1️- Clonar el repositorio

```bash
git clone https://github.com/andresgomez-77/LikeDislike-Backend.git
cd LikeDislike-Backend
```

### 2️- Configurar MongoDB

**Opción 1: MongoDB local**

1. **Instalar MongoDB Community:**
   - Sigue la [guía de instalación oficial](https://www.mongodb.com/docs/manual/installation/)

2. **Iniciar el servicio:**
```bash
# Linux
sudo systemctl start mongod
sudo systemctl status mongod

# macOS (con Homebrew)
brew services start mongodb-community

# Windows
# Inicia MongoDB como servicio o ejecuta mongod.exe
```

3. **Verificar conexión:**
```bash
mongosh
# o
mongo
```

**Opción 2: MongoDB Atlas (Cloud - Gratis)**

1. Crea una cuenta en [MongoDB Atlas](https://www.mongodb.com/cloud/atlas)
2. Crea un cluster gratuito (M0)
3. Configura acceso de red (IP Whitelist)
4. Crea un usuario de base de datos
5. Obtén la connection string:
```
mongodb+srv://usuario:password@cluster.mongodb.net/likedislike_db
```

### 3- Instalar dependencias

```bash
mvn clean install
```

Este comando:
- Descarga todas las dependencias del `pom.xml`
- Compila el proyecto
- Ejecuta los tests (si existen)
- Genera el archivo JAR en `target/`

---

##  Configuración

### application.properties

Edita `src/main/resources/application.properties`:

**Configuración local (desarrollo):**
```properties
# Servidor
server.port=8080

# MongoDB local
spring.data.mongodb.uri=mongodb://localhost:27017/likedislike_db
# o con autenticación
spring.data.mongodb.uri=mongodb://usuario:password@localhost:27017/likedislike_db

# Logging
logging.level.com.andresgomez.likedislike=DEBUG
logging.level.org.springframework.web=INFO
logging.level.org.springframework.data.mongodb=DEBUG

# CORS (ajusta según tu frontend)
cors.allowed.origins=http://localhost:5173,http://localhost:3000
```

**Configuración con MongoDB Atlas (Cloud):**
```properties
# MongoDB Atlas
spring.data.mongodb.uri=mongodb+srv://usuario:password@cluster.mongodb.net/likedislike_db?retryWrites=true&w=majority

# Producción
logging.level.com.andresgomez.likedislike=INFO
logging.level.org.springframework.data.mongodb=WARN

# CORS (permite tu dominio)
cors.allowed.origins=https://andresgomez-77.github.io
```

### Variables de Entorno

Para producción, configura estas variables:

```bash
MONGODB_URI=mongodb+srv://usuario:password@cluster.mongodb.net/likedislike_db
# o para MongoDB local
MONGODB_URI=mongodb://localhost:27017/likedislike_db
```

---

##  Ejecución

### Modo Desarrollo

**Opción 1: Con Maven**
```bash
mvn spring-boot:run
```

**Opción 2: Con Java**
```bash
# Primero compila
mvn clean package

# Luego ejecuta el JAR
java -jar target/likedislike-backend-0.0.1-SNAPSHOT.jar
```

**Opción 3: Desde IDE**
- Abre el proyecto en IntelliJ/Eclipse
- Ejecuta la clase principal con `@SpringBootApplication`

La API estará disponible en: **http://localhost:8080**

### Verificar que funciona

```bash
# Health check
curl http://localhost:8080/api/personaje/aleatorio

# Debe devolver un personaje aleatorio
```

---

##  Estructura del Proyecto

```
likedislike-backend/
├── src/
│   ├── main/
│   │   ├── java/com/andresgomez/likedislike/
│   │   │   ├── controller/         
│   │   │   │   ├── PersonajeController.java
│   │   │   │   └── EstadisticasController.java
│   │   │   ├── service/            
│   │   │   │   ├── PersonajeService.java
│   │   │   │   ├── RickMortyService.java
│   │   │   │   ├── PokemonService.java
│   │   │   │   └── SuperheroService.java
│   │   │   ├── repository/          
│   │   │   │   └── PersonajeRepository.java
│   │   │   ├── model/               
│   │   │   │   └── Personaje.java
│   │   │   ├── dto/                
│   │   │   │   ├── PersonajeDTO.java
│   │   │   │   ├── VotoRequest.java
│   │   │   │   └── EstadisticasDTO.java
│   │   │   ├── config/             
│   │   │   │   ├── CorsConfig.java
│   │   │   │   └── RestTemplateConfig.java
│   │   │   └── LikeDislikeApplication.java 
│   │   └── resources/
│   │       ├── application.properties      
│   │       └── application-prod.properties 
│   └── test/
│       └── java/                  
├── pom.xml                        
├── .gitignore
└── README.md                      
```

---

##  Endpoints de la API

### Base URL
```
http://localhost:8080/api
```

---

###  Personajes

#### **GET** `/personaje/aleatorio`
Obtiene un personaje aleatorio de las 3 fuentes.

**Query Parameters (opcional):**
- `source` - Filtra por fuente: `RICK_MORTY`, `POKEMON`, `SUPERHERO`

**Request:**
```http
GET /api/personaje/aleatorio
```

**Response:** `200 OK`
```json
{
  "id": 123,
  "nombre": "Rick Sanchez",
  "imagen": "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
  "source": "RICK_MORTY",
  "likes": 45,
  "dislikes": 12
}
```

**Con filtro por fuente:**
```http
GET /api/personaje/aleatorio?source=POKEMON
```

**Errores posibles:**
- `404 Not Found` - No hay personajes disponibles
- `500 Internal Server Error` - Error al conectar con APIs externas

---

#### **POST** `/personaje/votar`
Registra un voto (like o dislike) para un personaje.

**Request:**
```http
POST /api/personaje/votar
Content-Type: application/json

{
  "id": 123,
  "like": true
}
```

**Response:** `200 OK`
```json
{
  "id": 123,
  "nombre": "Rick Sanchez",
  "imagen": "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
  "source": "RICK_MORTY",
  "likes": 46,
  "dislikes": 12
}
```

**Errores posibles:**
- `404 Not Found` - Personaje no encontrado
- `400 Bad Request` - Request inválido

---

###  Estadísticas

#### **GET** `/estadisticas/mas-gustado`
Obtiene el personaje con más likes.

**Response:** `200 OK`
```json
{
  "id": 1,
  "nombre": "Pikachu",
  "imagen": "https://...",
  "source": "POKEMON",
  "likes": 150,
  "dislikes": 5
}
```

---

#### **GET** `/estadisticas/menos-gustado`
Obtiene el personaje con más dislikes.

**Response:** `200 OK`
```json
{
  "id": 42,
  "nombre": "Jerry Smith",
  "imagen": "https://...",
  "source": "RICK_MORTY",
  "likes": 8,
  "dislikes": 89
}
```

---

#### **GET** `/estadisticas/ultimo-evaluado`
Obtiene el último personaje que recibió un voto.

**Response:** `200 OK`
```json
{
  "id": 99,
  "nombre": "Batman",
  "imagen": "https://...",
  "source": "SUPERHERO",
  "likes": 78,
  "dislikes": 23
}
```

---

#### **GET** `/estadisticas/pikachu`
Verifica si Pikachu existe en la base de datos y sus estadísticas.

**Response:** `200 OK`
```json
{
  "existe": true,
  "mensaje": "¡Pikachu está en la base de datos con 150 likes!",
  "personaje": {
    "id": 25,
    "nombre": "Pikachu",
    "imagen": "https://...",
    "source": "POKEMON",
    "likes": 150,
    "dislikes": 5
  }
}
```

**Si no existe:**
```json
{
  "existe": false,
  "mensaje": "Pikachu aún no ha sido evaluado",
  "personaje": null
}
```

---

##  Modelos de Datos

### Documento Personaje

```java
@Document(collection = "personajes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Personaje {
    @Id
    private String id;  // MongoDB usa String por defecto para _id
    
    @Indexed  // Índice para búsquedas más rápidas
    private String nombre;
    
    private String imagen;
    
    @Indexed
    private PersonajeFuente source;
    
    private Integer likes = 0;
    
    private Integer dislikes = 0;
    
    @CreatedDate
    private LocalDateTime fechaCreacion;
    
    @LastModifiedDate
    private LocalDateTime ultimaActualizacion;
    
    // Getters, setters, constructores generados por Lombok...
}
```

### Repository con MongoDB

```java
@Repository
public interface PersonajeRepository extends MongoRepository<Personaje, String> {
    
    // Buscar por nombre (case insensitive)
    Optional<Personaje> findByNombreIgnoreCase(String nombre);
    
    // Buscar por fuente
    List<Personaje> findBySource(PersonajeFuente source);
    
    // Personaje con más likes
    Optional<Personaje> findTopByOrderByLikesDesc();
    
    // Personaje con más dislikes
    Optional<Personaje> findTopByOrderByDislikesDesc();
    
    // Último actualizado
    Optional<Personaje> findTopByOrderByUltimaActualizacionDesc();
    
    // Verificar existencia por nombre
    boolean existsByNombreIgnoreCase(String nombre);
}
```

### Enum PersonajeFuente

```java
public enum PersonajeFuente {
    RICK_MORTY,
    POKEMON,
    SUPERHERO
}
```

---

##  APIs Externas

### 1. Rick and Morty API

**Base URL:** `https://rickandmortyapi.com/api`

**Endpoints usados:**
- `GET /character/{id}` - Obtener personaje por ID
- IDs válidos: 1-826

**Ejemplo de respuesta:**
```json
{
  "id": 1,
  "name": "Rick Sanchez",
  "status": "Alive",
  "species": "Human",
  "image": "https://rickandmortyapi.com/api/character/avatar/1.jpeg"
}
```

---

### 2. PokéAPI

**Base URL:** `https://pokeapi.co/api/v2`

**Endpoints usados:**
- `GET /pokemon/{id}` - Obtener Pokémon por ID
- IDs válidos: 1-1010

**Ejemplo de respuesta:**
```json
{
  "id": 25,
  "name": "pikachu",
  "sprites": {
    "front_default": "https://raw.githubusercontent.com/.../25.png"
  }
}
```

---

### 3. Superhero API

**Base URL:** `https://superheroapi.com/api/{token}`

**Endpoints usados:**
- `GET /{id}` - Obtener superhéroe por ID
- IDs válidos: 1-731

**Ejemplo de respuesta:**
```json
{
  "id": "69",
  "name": "Batman",
  "image": {
    "url": "https://www.superherodb.com/pictures2/portraits/10/100/639.jpg"
  }
}
```

**Nota:** Requiere API token (obtener en [superheroapi.com](https://superheroapi.com))

---

##  Sistema de Recomendaciones

El backend soporta recomendaciones basadas en preferencias del usuario.

### Funcionamiento

1. **Frontend envía preferencia:**
```http
GET /api/personaje/aleatorio?source=POKEMON
```

2. **Backend prioriza esa fuente:**
```java
if (source != null) {
    return obtenerPersonajeDeSource(source);
} else {
    return obtenerPersonajeAleatorioDeTodasLasFuentes();
}
```

3. **Distribución equitativa** cuando no hay preferencia:
   - 33% Rick & Morty
   - 33% Pokémon
   - 33% Superhéroes

### Lógica del servicio

```java
public Personaje obtenerPersonajeAleatorio(PersonajeFuente source) {
    if (source != null) {
        // Obtiene de la fuente preferida
        return obtenerPersonajeDeSource(source);
    }
    
    // Rotación aleatoria entre las 3 fuentes
    int random = new Random().nextInt(3);
    switch(random) {
        case 0: return rickMortyService.obtenerPersonajeAleatorio();
        case 1: return pokemonService.obtenerPersonajeAleatorio();
        case 2: return superheroService.obtenerPersonajeAleatorio();
    }
}
```

---

##  Base de Datos

### Esquema MongoDB

MongoDB no requiere esquema fijo, pero esta es la estructura de documentos:

```javascript
// Colección: personajes
{
  "_id": "507f1f77bcf86cd799439011",  // ObjectId generado por MongoDB
  "nombre": "Pikachu",
  "imagen": "https://raw.githubusercontent.com/.../25.png",
  "source": "POKEMON",
  "likes": 150,
  "dislikes": 5,
  "fechaCreacion": ISODate("2026-01-15T10:30:00Z"),
  "ultimaActualizacion": ISODate("2026-01-17T14:22:00Z")
}
```

### Índices MongoDB

Para optimizar consultas, crea estos índices:

```javascript
// En MongoDB Shell o Compass
use likedislike_db

// Índice por nombre (búsquedas)
db.personajes.createIndex({ "nombre": 1 })

// Índice por source (filtros)
db.personajes.createIndex({ "source": 1 })

// Índice por likes (estadísticas)
db.personajes.createIndex({ "likes": -1 })

// Índice por dislikes (estadísticas)
db.personajes.createIndex({ "dislikes": -1 })

// Índice por última actualización (último evaluado)
db.personajes.createIndex({ "ultimaActualizacion": -1 })

// Índice compuesto (búsquedas complejas)
db.personajes.createIndex({ "source": 1, "likes": -1 })
```

### Queries comunes en MongoDB

**Personaje más gustado:**
```javascript
db.personajes.find().sort({ likes: -1 }).limit(1)
```

**Último evaluado:**
```javascript
db.personajes.find().sort({ ultimaActualizacion: -1 }).limit(1)
```

**Buscar Pikachu:**
```javascript
db.personajes.findOne({ nombre: /pikachu/i })
```

**Contar por fuente:**
```javascript
db.personajes.aggregate([
  { $group: { _id: "$source", total: { $sum: 1 } } }
])
```

**Top 10 más gustados:**
```javascript
db.personajes.find().sort({ likes: -1 }).limit(10)
```

---

4. **Configurar variables de entorno:**
```
MONGODB_URI=${MONGO_URL}
SPRING_PROFILES_ACTIVE=prod
```

5. **Deploy automático:**
   - Cada push a `main` desplegará automáticamente


---

##  Testing

### Ejecutar tests

```bash
mvn test
```

### Test con Postman

Importa esta colección:

```json
{
  "info": {
    "name": "LikeDislike API"
  },
  "item": [
    {
      "name": "Get Random Character",
      "request": {
        "method": "GET",
        "url": "http://localhost:8080/api/personaje/aleatorio"
      }
    },
    {
      "name": "Vote Character",
      "request": {
        "method": "POST",
        "url": "http://localhost:8080/api/personaje/votar",
        "body": {
          "mode": "raw",
          "raw": "{\"id\": 1, \"like\": true}"
        }
      }
    }
  ]
}
```

---

##  Troubleshooting

###  Error: "Could not connect to MongoDB"

**Síntomas:**
```
com.mongodb.MongoTimeoutException: Timed out after 30000 ms
```

**Soluciones:**
1. Verifica que MongoDB esté corriendo:
```bash
# Linux/Mac
sudo systemctl status mongod

# Windows
# Verifica en Servicios que MongoDB está activo
```

2. Verifica la URI en `application.properties`:
```properties
spring.data.mongodb.uri=mongodb://localhost:27017/likedislike_db
```

3. Prueba conexión manual:
```bash
mongosh
# o
mongo
```

4. **Si usas MongoDB Atlas:**
   - Verifica que tu IP esté en la whitelist
   - Comprueba usuario y contraseña
   - Asegúrate que el cluster esté activo

---

###  Error: "Port 8080 already in use"

**Solución:**
```bash
# Encuentra el proceso
lsof -i :8080

# Mata el proceso
kill -9 <PID>

# O cambia el puerto en application.properties
server.port=8081
```

---

###  Error: APIs externas no responden

**Síntomas:**
```
java.net.ConnectException: Connection timed out
```

**Soluciones:**
1. Verifica conexión a internet
2. Comprueba que las URLs de las APIs sean correctas
3. Revisa si las APIs están caídas (status pages)
4. Implementa retry logic y timeouts

---

###  Error: "Collection 'personajes' doesn't exist"

**Solución:**

MongoDB crea colecciones automáticamente al insertar el primer documento. No necesitas crear la colección manualmente.

Si quieres crearla manualmente:
```javascript
use likedislike_db
db.createCollection("personajes")
```

**Verificar colecciones existentes:**
```javascript
show collections
```

---

### Herramientas de Gestión MongoDB

**MongoDB Compass (GUI):**
- [Descargar aquí](https://www.mongodb.com/products/compass)
- Conecta con: `mongodb://localhost:27017`

**Mongosh (CLI):**
```bash
mongosh
use likedislike_db
db.personajes.find().pretty()
db.personajes.countDocuments()
```

---

##  Licencia

© 2026 **Andrés Gómez**. Todos los derechos reservados.

Este proyecto fue creado con fines educativos y de demostración.

---

##  Contacto

**Andrés Gómez**
-  GitHub: [@andresgomez-77](https://github.com/andresgomez-77)
-  Email: andresgomez-77@hotmail.com
-  LinkedIn: www.linkedin.com/in/andrés-gómez
