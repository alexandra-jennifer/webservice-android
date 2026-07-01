# 📱 CampusSync — Android Web Service App

> Aplikasi mobile Android untuk manajemen data mahasiswa secara *real-time* menggunakan Web Service (PHP REST API + MySQL).
> Final Project — Mata Kuliah Web Service, Teknik Informatika, Universitas Ma Chung (2025/2026).

---

## ✨ Features

| Feature | Description |
|---|---|
| 🔐 **Login** | User authentication via PHP backend |
| 📋 **CRUD** | Create, Read, Update, Delete student data |
| 📸 **Photo Upload** | Pick photo from gallery, compress as Base64, store on server |
| 🔍 **Live Search** | Instant client-side filtering by Name or NIM |
| 🔄 **Swipe Refresh** | Pull-to-refresh syncs latest data from server |
| 🗑️ **Delete Photo** | Permanently removes photo file from server |
| ✅ **Input Validation** | Double-layer validation (Android + PHP) |

---

## 🏗️ Architecture

```
Android Client (Java)
        │
        │  HTTP (Volley Library)
        │  JSON Request/Response
        ▼
PHP Web Service (REST API)
        │
        │  MySQLi
        ▼
MySQL Database (db_mahasiswa)
```

### Tech Stack
- **Client:** Android (Java), Volley, Glide, Material Design 3
- **Server:** PHP, Apache (XAMPP)
- **Database:** MySQL

---

## 📥 Download

Get the latest APK from the [**Releases**](https://github.com/alexandra-jennifer/webservice-android/releases) page.

---

## 🚀 Setup & Running

### Server Requirements
1. Install [XAMPP](https://www.apachefriends.org/)
2. Copy PHP server files to `htdocs/mahasiswa/`
3. Create an empty folder `htdocs/mahasiswa/uploads/`
4. Import the SQL schema into phpMyAdmin (`db_mahasiswa`)
5. Start **Apache** and **MySQL** in XAMPP

### Android App
1. Clone this repository
2. Open in Android Studio
3. In `APIConfig.java`, set `BASE_URL` to your server IP:
   ```java
   private static final String BASE_URL = "http://192.168.x.x/mahasiswa/";
   ```
4. Run the app on a device or emulator on the **same network** as the server

---

## 👥 Team

| Name | NIM | Role |
|---|---|---|
| Alexandra Jennifer Matahurila | 312310004 | Android Client & API Integration |
| Elizabeth Anndini Shayna Putri | 312310014 | PHP Server, Database & App Logo |
| Sutri Ajeng Neng Rahayu | 312310030 | UI/UX Design & XML Layout |
