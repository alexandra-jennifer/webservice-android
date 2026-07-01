package com.example.webservice_app;

public class Mahasiswa {
    private String nim, nama, jurusan, alamat, foto;

    public Mahasiswa(String nim, String nama, String jurusan, String alamat,String foto) {
        this.nim = nim; this.nama = nama;
        this.jurusan = jurusan; this.alamat = alamat;
        this.foto = foto;
    }

    public String getNim()     { return nim; }
    public String getNama()    { return nama; }
    public String getJurusan() { return jurusan; }
    public String getAlamat()  { return alamat; }
    public String getFoto() { return foto; }
}
