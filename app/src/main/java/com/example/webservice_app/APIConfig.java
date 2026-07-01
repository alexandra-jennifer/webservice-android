package com.example.webservice_app;

public class APIConfig {
    private static final String BASE_URL = "http://172.20.10.3/webservice/uaswebservice/mahasiswa/";

    public static final String URL_MAHASISWA = BASE_URL + "mahasiswa.php";
    public static final String URL_INSERT    = BASE_URL + "insert.php";
    public static final String URL_UPDATE    = BASE_URL + "update.php";
    public static final String URL_DELETE    = BASE_URL + "delete.php";
    public static final String URL_LOGIN     = BASE_URL + "login.php";
    public static final String URL_UPLOADS   = BASE_URL + "uploads/";
}
