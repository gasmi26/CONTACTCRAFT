📇 ContactCraft

A full-stack Spring Boot MVC web application for managing contacts with secure authentication, email verification, OAuth login, real-time chat, and performance monitoring.

🚀 Features
🔐 User authentication (Signup/Login)
📧 Email verification using token-based verification link
🌐 OAuth login with Google and GitHub
👤 Secure session-based login system
📇 CRUD operations for managing contacts
📤 Export contacts (CSV / Excel)
💬 Real-time chat feature for users
📊 Performance testing using JMeter
📈 Monitoring using Spring Boot Actuator & Micrometer
🧠 System Architecture
MVC-based architecture using Spring Boot
Controller → Service → Repository layered design
Thymeleaf used for server-side rendering
🛠️ Tech Stack
Backend
Java
Spring Boot
Spring MVC
Spring Security
Spring Data JPA
Frontend
Thymeleaf
HTML5
CSS3
JavaScript
Authentication
OAuth 2.0 (Google, GitHub)
Email Verification (SMTP)
Database
MySQL
Tools & Testing
Apache JMeter
Spring Boot Actuator
Micrometer
Maven
📊 Performance Metrics
⏱ Average Response Time: ~13 ms
📈 Throughput: ~77 requests/sec
⚡ Error Rate: 0% under load testing
🧪 Tested using 100+ concurrent virtual users (JMeter)
📤 Contact Export Feature

Users can export their saved contacts in:

CSV format

💬 Chat Feature
Real-time user chat module inside application
Enhances user interaction and engagement
📸 Screenshots 
Home page
<img width="1904" height="926" alt="image" src="https://github.com/user-attachments/assets/a219f8b3-fed4-4dc2-b3d9-9306955b8318" />


Login page
<img width="1674" height="824" alt="image" src="https://github.com/user-attachments/assets/54b4e499-6a60-4091-94fe-2942fedc72aa" />

User profile page
<img width="1905" height="930" alt="image" src="https://github.com/user-attachments/assets/eed7623b-ed6e-406f-86d8-0e81ef7c4129" />



⚙️ How to Run Locally
# Clone repository
git clone https://github.com/your-username/contactcraft.git

# Move into project
cd contactcraft

# Run Spring Boot app
mvn spring-boot:run
🌐 Application Flow
User signs up
Email verification link is sent
User logs in (or uses Google/GitHub OAuth)
Dashboard opens
User manages contacts (CRUD)
Export contacts if needed
Chat with other users
System performance monitored via Actuator
