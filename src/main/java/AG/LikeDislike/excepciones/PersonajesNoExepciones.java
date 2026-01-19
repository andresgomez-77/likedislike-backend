package AG.LikeDislike.excepciones;

public class PersonajesNoExepciones extends RuntimeException {
    
    public PersonajesNoExepciones(String message) {
        super(message);
    }

    public PersonajesNoExepciones(String message, Throwable cause) {
        super(message, cause);
    }
}
