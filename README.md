
#  Smart Hostel Management System

A desktop application for managing day-to-day hostel operations — students, rooms, room allocation and maintenance complaints — built with **Java Swing** and **MySQL**.

---

## 📖 Overview

Managing a hostel on paper or spreadsheets leads to double-booked rooms, lost complaints and no clear picture of occupancy. **Smart Hostel Management System** replaces that with a single role-based desktop app:

- **Admins (wardens)** manage students and rooms, allocate rooms with the help of a scoring-based recommendation, and track every complaint.
- **Students** log in to see their allocated room and to raise and follow maintenance complaints.

The project follows a layered architecture (**UI → Service → DAO → Database**) with plain JDBC for data access, input validation in the service layer, and transactional room allocation so occupancy can never go out of sync.

---

##  Features

###  Authentication
- Login screen with **role-based access** (Admin / Student)
- Passwords stored as **SHA-256 hashes**

###  Admin Portal
- **Dashboard** – live counts of total students, total rooms, available rooms and pending / in-progress complaints
- **Student management** – add, update, delete and browse students in a sortable table (click a row to load it into the form)
- **Room management** – add and delete rooms (2-, 3- or 4-seater; capacity 1–8) and view occupancy and status
- **Smart room allocation** – enter a student ID plus a preferred block and room type; the system ranks the available rooms by score and allocates the one you choose
- **Complaint management** – view all complaints (highest priority first) and update their status: `PENDING` → `IN_PROGRESS` → `RESOLVED`

###  Student Portal
- **My Room** – see the allocated room number, block and type
- **Submit Complaint** – choose a category (Electrical, Water, Cleaning, Internet, Other), a severity and a description (max 500 characters)
- **My Complaints** – track the priority and status of every complaint raised

###  Built-in Logic
- **Room recommendation score (max 100):** +40 for the preferred block, +30 for the preferred room type, +30 if the room has a free bed. Full and under-maintenance rooms are never suggested.
- **Automatic complaint priority:**

  | Condition | Priority |
  |---|---|
  | Severity is *High* | `HIGH` |
  | Category is Electrical or Water | `HIGH` |
  | Category is Internet | `MEDIUM` |
  | Category is Cleaning | `LOW` |
  | Anything else | `MEDIUM` |

- **Safe allocation:** allocation runs in a database transaction — a student can hold only one room, a room can never exceed its capacity, and its status (`AVAILABLE` / `PARTIALLY_OCCUPIED` / `FULL`) updates automatically.
- **Input validation:** required fields, 10-digit phone numbers, email format, year 1–6, room capacity 1–8.

---

##  Technologies / Tools Used

| Area | Technology |
|---|---|
| Language | Java (project compiled for **Java 25**) |
| GUI | Java Swing / AWT |
| Database | MySQL 8 |
| Database access | JDBC with `mysql-connector-j` 8.4.0 |
| Build tool | Apache Maven |
| Testing | JUnit 5 (5.11.0), Maven Surefire 3.5.0 |
| Version control | Git & GitHub |

---

##  Project Structure

```
SmartHostelManagementSystem/
├── database/
│   └── schema.sql                  # Tables + demo data
├── src/
│   ├── main/java/com/smarthostel/
│   │   ├── Main.java               # Application entry point
│   │   ├── dao/                    # JDBC data access (Student, Room, Allocation, Complaint, User, Dashboard)
│   │   ├── model/                  # Student, Room, Allocation, Complaint, User
│   │   ├── service/                # Business logic (validation, recommendation, priority)
│   │   ├── ui/                     # Swing screens (Login, Admin, Student)
│   │   └── util/                   # PasswordUtil, Validator
│   └── test/java/com/smarthostel/  # Unit tests
└── pom.xml
```

---

##  Steps to Install & Run

### Prerequisites
- **JDK 25** (the `pom.xml` targets Java 25 — change `maven.compiler.source/target` if you use an older JDK such as 21)
- **Apache Maven 3.9+**
- **MySQL Server 8+**
- Git

### 1. Clone the repository
```bash
git clone https://github.com/Roopesh1506/Smart-Hostel-Management-System.git
cd Smart-Hostel-Management-System
```

### 2. Create the database
Run the schema script — it creates the `smart_hostel` database, all tables and some demo data.

```bash
mysql -u root -p < database/schema.sql
```
*(Or open `database/schema.sql` in MySQL Workbench and execute it.)*

### 3. Configure the database connection
The app reads its connection settings from environment variables. Defaults are used when a variable is not set.

| Variable | Default | Purpose |
|---|---|---|
| `HOSTEL_DB_URL` | `jdbc:mysql://localhost:3306/smart_hostel?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true` | JDBC URL |
| `HOSTEL_DB_USER` | `root` | MySQL username |
| `HOSTEL_DB_PASSWORD` | *(none)* | MySQL password |

**Windows (PowerShell)**
```powershell
$env:HOSTEL_DB_PASSWORD = "your_mysql_password"
```

**Linux / macOS**
```bash
export HOSTEL_DB_PASSWORD="your_mysql_password"
```

### 4. Build and run
```bash
mvn compile exec:java -Dexec.mainClass=com.smarthostel.Main
```

Alternatively, open the project in **IntelliJ IDEA / VS Code**, let Maven import the dependencies, and run `com.smarthostel.Main`. (If you run it from an IDE, set the environment variables in the run configuration.)

### 5. Log in with the demo accounts

| Role | Username | Password |
|---|---|---|
| Admin | `admin` | `admin123` |
| Student | `student` | `student123` |

>  These credentials are for demonstration only. Change them before using the system with real data.

---

## 🧪 Instructions for Testing

### Automated tests
Run the JUnit 5 unit tests (no database required):

```bash
mvn test
```

They cover the core logic:
- `ComplaintPriorityServiceTest` – Electrical → HIGH, Cleaning + Low → LOW, High severity overrides the category
- `RoomRecommendationServiceTest` – the room matching the preferred block and type is ranked first with a score of 100

A standalone self-check for both algorithms is also included. It uses Java `assert`, so run it with assertions enabled:

```bash
mvn test-compile
java -ea -cp target/classes:target/test-classes com.smarthostel.AlgorithmSelfTest
```
*(On Windows use `;` instead of `:` in the classpath.)*
Expected output: `All algorithm tests passed.`

### Manual test checklist
With the app running and the demo data loaded:

1. **Login** – sign in as `admin`; try a wrong password and confirm "Invalid credentials" is shown.
2. **Students** – add a student (e.g. `STU002`), edit it, then delete it. Try an invalid phone number or email to see validation errors.
3. **Rooms** – add a room, then delete it.
4. **Allocation** – on *Rooms & Allocation*, enter `STU001`, pick a preferred block and type, click **Recommend**, then **Allocate Selected Recommendation**. Confirm the room's occupancy and status update. Allocating the same student again should be rejected.
5. **Student portal** – log out, sign in as `student`, and check that *My Room* shows the allocated room.
6. **Complaints** – submit an *Electrical* complaint (it should get `HIGH` priority), then sign in as admin and change its status to `RESOLVED`; confirm the student sees the update.

---


##  Author
https://github.com/kumarsumed205-debug/hostel-management-system.git
