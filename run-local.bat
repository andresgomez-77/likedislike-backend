@echo off
set MONGODB_URI=mongodb+srv://likedislike_user:jRRjU5wB6WF5ySM4@likedislikecluster.jsa9pco.mongodb.net/likedislike_dev?retryWrites=true^&w=majority
set SUPERHERO_API_KEY=2648e74d6106278362f373210efdca82
set CORS_ALLOWED_ORIGINS=http://localhost:5173

mvn spring-boot:run -Dspring.profiles.active=local