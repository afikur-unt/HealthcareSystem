# Healthcare System
This is a minimal version of a smart healthcare system for small clinics. The system includes role-based access for doctors, patients, and admins. Doctors can manage patient records and prescriptions, patients can view their history, and admins get basic analytics of the system.

# System Modules and Responsibilities
- User Authentication and Authorization (Admin, Doctor, Patient)
- Patient Management (Doctor)
- Prescription Management (Doctor)
- Patient Portal (Patient)
- Analytics Dashboard (Admin)

# Live Link
[https://healthcare-mf3o.onrender.com](https://healthcare-mf3o.onrender.com/login)
![image](https://github.com/user-attachments/assets/bb0bf4e3-66bf-47c9-8c3c-16b98eaaafbc)

# Compile and Run the Program

## Prerequisites
1. Ensure Java 17 is installed. You can check your Java version by running:
   ```sh
   java -version
   ```
   If Java is not installed, download it from [Oracle](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) or use your package manager.

2. Install Docker Desktop from [here](https://www.docker.com/products/docker-desktop/).

## Steps to Run the Application
1. Clone the repository from the main branch:
   ```sh
   git https://github.com/afikur-unt/HealthcareSystem.git
   ```

2. Navigate to the project directory:
   ```sh
   cd HealthcareSystem
   ```

3. Start PostgreSQL using Docker:
   ```sh
   docker-compose up postgresql -d
   ```

4. Compile and run the application:
   ```sh
   ./mvnw spring-boot:run
   ```

5. Run the Spring Boot tests:
   ```sh
   ./mvnw test
   ```

6. Access the application at [http://localhost:8080](http://localhost:8080).
