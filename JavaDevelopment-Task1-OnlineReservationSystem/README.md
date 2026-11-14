# Online Reservation System

A desktop train ticket reservation system built with **Java Swing**, **JDBC** and **SQLite**.
Users can log in, book tickets (a unique PNR is generated) and cancel bookings using the PNR.

> **Internship:** Oasis Infobyte (Java Development)
> **Task:** Online Reservation System
> **Author:** _Your Name_

---

## Features

| Requirement | Status |
|---|---|
| Login form (username + password), access denied for invalid credentials | Done |
| Reservation form: passenger name, train number, train name (auto-filled), class type, date of journey, source, destination | Done |
| Book button saves to the database and generates a unique PNR | Done |
| Confirmation dialog showing the booking details | Done |
| Cancellation form: PNR field + Fetch button showing full booking details | Done |
| Confirm cancellation with an "Are you sure?" dialog, then the booking is removed from the database | Done |
| Input validation: no empty fields, valid date format, numeric train number | Done |

Extra touches: passwords stored as SHA-256 hashes, `PreparedStatement` for every query
(prevents SQL injection), past dates rejected, source and destination must differ, logout button.

---

## Tech Stack

- Java 11 or higher
- Java Swing (GUI)
- JDBC with SQLite (`org.xerial:sqlite-jdbc`), no separate database server needed
- Maven (build and dependency management)

---

## Project Structure

```
OnlineReservationSystem/
├── pom.xml
├── README.md
└── src/main/java/com/oasis/reservation/
    ├── Main.java                  # entry point
    ├── db/DBHelper.java           # connection, table creation, sample data
    ├── model/Reservation.java     # booking entity
    ├── dao/UserDAO.java           # login check
    ├── dao/TrainDAO.java          # train number -> train name
    ├── dao/ReservationDAO.java    # book / find by PNR / cancel
    ├── ui/LoginFrame.java         # login window
    ├── ui/MainFrame.java          # tabs after login
    ├── ui/BookingPanel.java       # reservation form
    ├── ui/CancelPanel.java        # cancellation form
    └── util/                      # Validator, PasswordUtil, UiUtil
```

---

## Database Schema (SQLite)

The file `reservation.db` is created automatically in the folder where you run the app.

```sql
users        (username TEXT PRIMARY KEY, password_hash TEXT NOT NULL)
trains       (train_no INTEGER PRIMARY KEY, train_name TEXT NOT NULL)
reservations (pnr INTEGER PRIMARY KEY, username TEXT, passenger_name TEXT,
              train_no INTEGER, train_name TEXT, class_type TEXT,
              journey_date TEXT, source TEXT, destination TEXT, booked_at TEXT)
```

---

## How to Run

### Prerequisites
- JDK 11 or newer (`java -version` and `javac -version` should both work)
- Maven 3.6 or newer (`mvn -version`)
- Internet connection the first time, so Maven can download the SQLite driver

### Option 1: Command line
```bash
cd OnlineReservationSystem
mvn clean package
java -jar target/OnlineReservationSystem.jar
```

### Option 2: IDE (IntelliJ IDEA / Eclipse / NetBeans)
1. Open the IDE and choose **Open / Import Project**, then select `pom.xml` (import as a Maven project).
2. Wait for Maven to download the dependencies.
3. Run `com.oasis.reservation.Main`.

---

## Demo Login

| Username | Password |
|---|---|
| student | pass123 |
| admin | admin123 |

## Available Trains (sample data)

| Train No | Train Name |
|---|---|
| 12627 | Karnataka Express |
| 12301 | Howrah Rajdhani Express |
| 12951 | Mumbai Rajdhani Express |
| 12009 | Shatabdi Express |
| 12723 | Telangana Express |
| 22691 | Bengaluru Rajdhani Express |
| 12839 | Chennai Mail |
| 12615 | Grand Trunk Express |

---

## How to Use

1. **Login** with the demo account. Wrong credentials show "Access Denied".
2. **Book Ticket** tab:
   - Enter the passenger name and train number (the train name fills in automatically).
   - Choose the class, enter the date as `dd-MM-yyyy` (for example `25-12-2026`), then the source and destination.
   - Click **Book Ticket**. A confirmation dialog shows all details and your **10-digit PNR**.
3. **Cancel Ticket** tab:
   - Enter the PNR and click **Fetch** to see the full booking details.
   - Click **Confirm Cancellation**, then **Yes** in the "Are you sure?" dialog. The booking is deleted from the database.

## Validation Rules

- All fields are required.
- Train number must be numeric and must exist in the `trains` table.
- Date must be a real date in `dd-MM-yyyy` format (31-02-2026 is rejected) and cannot be in the past.
- Source and destination must be different.
- PNR must be a 10-digit number.

## Test Cases

| # | Action | Expected result |
|---|---|---|
| 1 | Login with `student / wrong` | "Access Denied" message |
| 2 | Login with `student / pass123` | Main window opens |
| 3 | Type train no `12627` | Train name shows "Karnataka Express" |
| 4 | Type train no `abc` | Name field shows "Invalid train number"; booking is blocked |
| 5 | Submit with an empty field | "Please fill in all the required fields." |
| 6 | Date `31-02-2026` | "Enter a valid date" error |
| 7 | Valid booking | Confirmation dialog with new PNR |
| 8 | Fetch a non-existent PNR | "No booking found" message |
| 9 | Fetch valid PNR, confirm cancel | Booking removed; fetching the PNR again finds nothing |

## Troubleshooting

- **`No suitable driver found for jdbc:sqlite`**: run from the shaded jar (`java -jar target/OnlineReservationSystem.jar`) or through Maven so the driver is on the classpath.
- **`mvn` not recognised**: install Maven and add it to your PATH, or use an IDE with built-in Maven.
- **Reset all data**: close the app and delete `reservation.db`; it is recreated on the next run.

## Possible Improvements

- User registration and role-based access
- Seat availability and fare calculation
- Printable e-ticket
- Cancel only your own bookings
