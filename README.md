# OurSpace – Projektauftrag üK 223

📌 **Starter Project Spring Boot**  
Dies ist das Starter-Projekt für die Social Media Website *OurSpace* im Rahmen des üK 223.  
Es enthält ein Full-Stack Setup aus **Spring Boot, React und PostgreSQL**.  
Umgesetzt ist der **Use-Case 4.1 – User Profile**.

---

## 🚀 Technologien
- React (Frontend, TypeScript)
- Spring Boot (Backend, Java 17+)
- PostgreSQL (Datenbank)
- Spring Security mit JWT
- JPA / Hibernate
- JUnit, Postman, Cypress (Tests)
- Swagger (API-Dokumentation)

---

## ⚙️ Setup

### Docker command (PostgreSQL starten)
```bash
docker run --name postgres_db \
 -e POSTGRES_USER=postgres \
 -e POSTGRES_PASSWORD=postgres \
 -p 5432:5432 \
 -d postgres
```

### Backend starten
```bash
.\gradlew bootRun
```
➡ Läuft unter **http://localhost:8080**

---

## 🔑 Security
- **JWT-Authentifizierung**
- Rollen: `USER`, `ADMIN`
- Zugriff auf **UserProfile**:
    - User darf nur eigenes Profil sehen/bearbeiten/löschen
    - Admin kann jedes Profil verwalten & durchsuchen

---

## 📖 Endpoints – UserProfile

### **User**
- `POST /api/user-profiles` → eigenes Profil erstellen
- `GET /api/user-profiles/me` → eigenes Profil lesen
- `PUT /api/user-profiles/{id}` → eigenes Profil ändern
- `DELETE /api/user-profiles/{id}` → eigenes Profil löschen

### **Admin**
- `GET /api/user-profiles/{id}` → beliebiges Profil lesen
- `PUT /api/user-profiles/{id}` → beliebiges Profil ändern
- `DELETE /api/user-profiles/{id}` → beliebiges Profil löschen
- `GET /api/admin/user-profiles?page=0&size=10&sort=id,asc`  
  → Profile mit Pagination durchsuchen

---

## ✅ Use-Cases (4.1)
- User erstellt eigenes Profil
- User liest, aktualisiert oder löscht eigenes Profil
- Administrator liest, aktualisiert oder löscht Profile
- Administrator durchsucht/filtern/sortieren via Pagination
- System prüft Zugriff: Nur Besitzer/Admin erlaubt

---

## 🧪 Testing-Strategie
- **Postman**: Tests für CRUD- und Security-Endpunkte
- **Cypress**: E2E-Tests im Frontend

### Beispiel-UC-Test (Cypress)
**UC2 – User liest/aktualisiert eigenes Profil**
- Erfolgsfall: User ruft `/me` auf → Profil zurückgegeben
- Fehlerfall: User ruft fremdes Profil via `/api/user-profiles/{id}` auf → **403 Forbidden**

---

## 📊 Dokumentation
- **Swagger UI**:  
  [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

- **Domänenmodell (Auszug):**
  ```
  User (1) --- (1) UserProfile
  ```
- Weitere Diagramme:
    - Use-Case-Diagramm (UC1–UC5)
    - Sequenzdiagramm (Profil-Update)

---

## 👥 Multiuser-Fähigkeit
- Transaktionssicherheit dank PostgreSQL & JPA
- Sauberes Rollenkonzept (`USER`, `ADMIN`)
- Zugriffskontrolle per Security Layer

---

## 🛠️ Troubleshooting
**Fehler beim Starten:**
```
org.postgresql.util.PSQLException: ERROR: relation "role_authority" does not exist
```

➡ Lösung: Anwendung neu starten. Hibernate erstellt die Tabellen manchmal asynchron.  