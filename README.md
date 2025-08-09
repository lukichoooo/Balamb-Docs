# Balamb Docs

Balamb Docs is an **online document editor** with user authentication, document sharing, and real-time permission management.  
Built using **Java Spring Boot** for the back end and **React** for the front end.

---

## Why
I created Balamb Docs to offer a **simple and fast** way for users to manage small text documents. My goal was to make:
- **Editing** quick and intuitive for brief notes or drafts.
- **Sharing** and permission management straightforward for collaboration.

I wanted a lightweight alternative to bulky editors, perfect for jotting down ideas on the fly.

---

## Features

- Create and edit text documents  
- User authentication and privacy management using JWT  
- Share documents with collaborators  
- Manage document visibility and assign roles  

---

## Tech Stack

### Front-End
- React  
- React Router  
- CSS Modules  
- Vite  

### Back-End
- Java Spring Boot  
- Spring Security  
- Spring Data JPA  
- MySQL


---

## Back-End Testing

- Written using **JUnit** and **Mockito**  
- Focused heavily on privacy, access control, and permission correctness  
- Tests include:  
  - Role-based access restrictions  
  - Unauthorized access rejection  

---

## Screenshots

![Screenshot 1](https://github.com/user-attachments/assets/d9c3291a-d49f-4921-b193-479c9fe96688)

![Screenshot 2](https://github.com/user-attachments/assets/d26f885e-f2a3-4661-9a98-caf473d1b297)

![Screenshot 3](https://github.com/user-attachments/assets/199c1919-01b0-4d99-b6ba-ace209d24ec8)

![Screenshot 4](https://github.com/user-attachments/assets/1a80de1e-449c-4fa4-b0fa-5e7e044cbc7c)

![Screenshot 5](https://github.com/user-attachments/assets/d45fc409-40fd-493b-8436-497c329effab)

## Using Web Service

**the deployed site needs at least a minute to start up while its inactive**
if you want to log in / register after a long interval 

## Notable Possible Improvements

- Use a `DocumentRole` class instead of an enum (to allow custom roles)  
- Real-time collaborative editing  
- Rich text formatting/editing  
- Email authentication  
