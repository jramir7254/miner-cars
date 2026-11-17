## Getting Started

Welcome to the VS Code Java world. Here is a guideline to help you get started to write Java code in Visual Studio Code.

## Folder Structure

The workspace contains two folders by default, where:

- `src`: the folder to maintain sources
- `lib`: the folder to maintain dependencies

Meanwhile, the compiled output files will be generated in the `bin` folder by default.

> If you want to customize the folder structure, open `.vscode/settings.json` and update the related settings there.

## Dependency Management

The `JAVA PROJECTS` view allows you to manage your dependencies. More details can be found [here](https://github.com/microsoft/vscode-java-dependency#manage-dependencies).

---

# Person 1 – User Management

Member 1
Responsible for customers, admins, registration, login, and profiles.
Java classes:
User.java
Person.java
Admin.java
UserManager.java (additional class)
Responsibilities:
Create the User, Person, and Admin classes.
Implement customer registration.
Generate unique user IDs, usernames, and passwords.
Implement login and authentication.
Implement customer profile viewing and updating.
Implement admin user management:
Add users
View/search users
Update users
Delete users
Read and save user information to files.

---

# Person 2 – Car Inventory & Purchases

Member 2
Responsible for the vehicle system, inventory, browsing, and purchases.

## Java classes:

- Car.java
- Sedan.java
- SUV.java
- Hatchback.java
- Pickup.java
- CarManager.java (additional class)

## Implement car attributes:

- ID
- Type
- Model
- Condition
- Color
- Capacity
- Mileage
- Fuel type
- Transmission
- VIN
- Price
- Availability
- Implement displaying all available cars.
- Implement filtering cars by ID, type, condition, and price.
- Implement purchasing cars.
- Update vehicle inventory.

## Implement admin car management:

- Add cars
- View/search cars
- Update cars
- Delete cars
- Read and save car information to files.

---

# Person 3 – Main System & Integration

Responsible for connecting everything and implementing the menus and system-wide features.
Java classes:
RunShop.java
Log.java
Invoice.java (additional class)
FileManager.java (additional class)
Responsibilities:
Implement the main method.
Create the main menu:
Register
Login
Exit
Implement the customer menus.
Implement the admin menus.
Connect Person 1's user management functionality.
Connect Person 2's car inventory functionality.
Implement logout functionality.
Implement system activity logging.
Implement persistent storage and saving updated files when exiting.
Coordinate integration and test the complete program
