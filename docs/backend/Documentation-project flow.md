# Java documentation - project flow

## Spring Initializr

### Project tools settings:
- **Project**: Maven
- **Language**: Java
- **Spring Boot**: latest stable (f.e. 4.x)
- **Group**: `com.rafkot`
- **Artifact**: `chatapp`
- **Package name**: `com.rafkot.chatapp` (`<Group>` + `.` + `<Artifact>`)
- **Packaging**: Jar
- **Configuration**: YAML
- **Java**: 21

---

***Maven**: Is a build tool for Java, helps to manage Java project, it manages:
- dependencies downloading
- application build (compilation)
- launching
- testing

Downloads from internet (Maven Central)

Why **Maven** instead of **Gradle**?

**Maven**:
- **XML** config (*pom.xml*)
- predictable
- works "always the same"

**Gradle**
- configuration in **Groovy / Kotlin** language
- more **flexible**
- shorter recorded

**So..., why?**
1. It's mentally simpler
2. Better to study Spring (more common in tutorials, easier to compare)
3. Imposed pattern

---

Why **YAML** insted of **Properties**? (**Configuration**)

**YAML** (*application.yml*):

```
app:
  users:
    - name: Jan
      age: 20
    - name: Anna
      age: 25
```

**Properties** (*application.properties*):

```
app.users[0].name=Bob
app.users[0].age=20
app.users[1].name=Alice
app.users[1].age=25
```

| Feature      | YAML     | Properties|
| -------------|:--------:|:---------:|
| Readability  | better   | worse     |
| Structure    | hierarchy| flat      |
| Sensitivity  | spaces   | none      |
| Functionality| same     | same      |
| Sensitivity  | right baz| right foo |

---

***Package name Naming Conventions** [from Oracle docs](https://docs.oracle.com/javase/tutorial/java/package/namingpkgs.html):

> Package names are written in all lower case to avoid conflict with the names of classes or interfaces.

---


### Dependecies included:
1. **Spring Web**:

> Build web, including RESTful, applications using Spring MVC. Uses Apache Tomcat as the default embedded container.

**What is it for?**

This is the communication layer with the front-end.

**What you get from it?**
- HTTP endpoints (like `/api/auth/register`)
- controllers (`@RestController`)
- handling request and responses (JSON)

**Where will you use it in your project?**
- registration → `POST /register`
- login → `POST/login`
- sending messages → `POST/messages`
- fetching contacts

---

2. **Spring Data JPA**:
> Persist data in SQL stores with Java Persistence API using Spring Data and Hibernate.

**What is it for?** 

For saving and retrieving data from the database.

**What does it do for you?**
- maps `User` class → table in DB
- generates SQL queries
- gives ready-made methods (`save`, `findById`, etc.)

**Where will you use it in your project?**
- user record upon registration
- checking if the email exists
- fetching contacts
- messages saving

**Mental model:

> JPA = Java-to-Database translator

---

3. **Spring Security**:

> Highly customizable authentication and access-control framework for Spring applications.

**What is it for?**:

To control who has access to what.

**Initially:**
- it will block everything (you need to disable it temporarily)

**Later**
- user login
- JWT (tokens)
- endpoint protection

**Where will you use it in your project?**
- `/login` - authentication
- `/messages` protection (only for logged users)
- checking "who the user is"

**Mental model**

> Security - "App security guard"

---

4. **Lombok**

> Java annotation library which helps to reduce boilerplate code.

**What is it for?**:

For shortening code.

**What does it hide?**
- getters/setters (`@Getter`/`@Setter`)
- constructors (`@NoArgsConstructor`/`@AllArgsConstructor`)
- builders (`@Builder`)
- toString (`@ToString`)
- and many more

Instead:

```java
public String getEmail() { return email; }
```

You have:

```java
@Getter
```

**Where will you use it in your project?**
- `User` entity
- DTO's (`RegisterRequest`)
- responses

**Mental model**

> Lombok = "automatic boring code generator"

---

5. **Validation**

> Bean Validation with Hibernate validator.

**What is it for?**:

To check if the data provided by the user is correct.

**What can you check?**
- whether the email is in the correct format
- whether the password has a minimum length
- whether the field is empty

**How does it work?**
You add annotations:

```java
@Email
@NotBlank
@Size(min = 8)
```

**Where will you use it in your project?**
- registration (email, password, username)
- login
- possibly when creating message

**Mental model**:

> Validation - "first input filter"

---

6. **H2 Database**

> Provides a fast in-memory database that supports JDBC API and R2DBC access, with a small (2mb) footprint. Supports embedded and server modes as well as a browser based console application.

**What is it for?**:

This is your database (to start with).

**Features**
- runs in-memory
- no installation required
- resets when the app is restarted

**Where will you use it in your project?**
- User storage
- Messages
- Contacts (in the future)

**Additional bonus:**

It has a web-based panel (H2 Console)

**Mental model:**

> H2 = "temporary database for learning and testing"