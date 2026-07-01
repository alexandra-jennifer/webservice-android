package com.example.webservice_app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FormActivity extends AppCompatActivity {

    TextInputEditText etNIM, etNama, etJurusan, etAlamat;
    MaterialButton btnSimpan, btnDelete, btnBatal, btnPilihFoto, btnHapusFoto;
    ImageView ivPreview;
    TextView tvTitleForm;

    String mode = "tambah";
    String base64Foto = ""; // Variabel penyimpan teks gambar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        // Inisialisasi Komponen UI
        tvTitleForm  = findViewById(R.id.tvTitleForm);
        etNIM        = findViewById(R.id.etNIM);
        etNama       = findViewById(R.id.etNama);
        etJurusan    = findViewById(R.id.etJurusan);
        etAlamat     = findViewById(R.id.etAlamat);
        btnSimpan    = findViewById(R.id.btnSimpan);
        btnDelete    = findViewById(R.id.btnDelete);
        btnBatal     = findViewById(R.id.btnBatal);
        btnHapusFoto = findViewById(R.id.btnHapusFoto);

        // Komponen Baru untuk Foto
        btnPilihFoto = findViewById(R.id.btnPilihFoto);
        ivPreview    = findViewById(R.id.ivPreview);

        // Cek apakah halaman ini dibuka untuk TAMBAH atau EDIT
        mode = getIntent().getStringExtra("mode");

        if ("edit".equals(mode)) {
            tvTitleForm.setText("Edit Data Mahasiswa");
            etNIM.setText(getIntent().getStringExtra("nim"));
            etNama.setText(getIntent().getStringExtra("nama"));
            etJurusan.setText(getIntent().getStringExtra("jurusan"));
            etAlamat.setText(getIntent().getStringExtra("alamat"));

            etNIM.setEnabled(false); // NIM tidak boleh diubah saat edit
            btnDelete.setVisibility(View.VISIBLE); // Tombol hapus dimunculkan

            String fotoLama = getIntent().getStringExtra("foto");
            if (fotoLama != null && !fotoLama.isEmpty()) {
                String urlFoto = APIConfig.URL_UPLOADS + fotoLama;
                com.bumptech.glide.Glide.with(this)
                        .load(urlFoto)
                        .placeholder(android.R.drawable.ic_menu_camera)
                        .diskCacheStrategy(com.bumptech.glide.load.engine.DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(ivPreview); // Tampilkan gambar lama ke ivPreview
            }
        }

        // Aksi Tombol-tombol
        btnPilihFoto.setOnClickListener(v -> bukaGaleri());

        btnHapusFoto.setOnClickListener(v -> {
            // Kembalikan gambar ke logo kamera bawaan
            ivPreview.setImageResource(android.R.drawable.ic_menu_camera);

            // Kirim kata sandi rahasia ke PHP agar PHP menghapus file fisiknya
            base64Foto = "hapus_foto";
        });

        btnSimpan.setOnClickListener(v -> {
            if ("tambah".equals(mode)) {
                insertData();
            } else {
                updateData();
            }
        });

        btnDelete.setOnClickListener(v -> konfirmasiDelete());
        btnBatal.setOnClickListener(v -> finish());
    }

    // ==========================================
    // FUNGSI MEMILIH DAN MENGUBAH FOTO (BASE64)
    // ==========================================
    private void bukaGaleri() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            try {
                Uri uri = data.getData();
                // Membaca gambar dari galeri
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                ivPreview.setImageBitmap(bitmap); // Menampilkan di layar

                // Mengompres dan mengubah wujudnya jadi String Base64
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos); // Kompres kualitas 50%
                byte[] bytes = baos.toByteArray();
                base64Foto = Base64.encodeToString(bytes, Base64.DEFAULT); // Masukkan ke variabel base64Foto

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void insertData() {
        String nim     = etNIM.getText().toString().trim();
        String nama    = etNama.getText().toString().trim();
        String jurusan = etJurusan.getText().toString().trim();
        String alamat  = etAlamat.getText().toString().trim();

        // Validasi Input
        if (nim.isEmpty()) { etNIM.setError("Wajib diisi!"); return; }
        if (nama.isEmpty()) { etNama.setError("Wajib diisi!"); return; }

        // Sesuai Panduan: Mengirimkan base64Foto sebagai parameter ke-6
        kirimRequest(APIConfig.URL_INSERT, nim, nama, jurusan, alamat, base64Foto);
    }

    private void updateData() {
        String nim     = etNIM.getText().toString().trim();
        String nama    = etNama.getText().toString().trim();
        String jurusan = etJurusan.getText().toString().trim();
        String alamat  = etAlamat.getText().toString().trim();

        if (nama.isEmpty()) { etNama.setError("Wajib diisi!"); return; }

        // Sesuai Panduan: Mengirimkan base64Foto sebagai parameter ke-6
        kirimRequest(APIConfig.URL_UPDATE, nim, nama, jurusan, alamat, base64Foto);
    }

    // Sesuai Panduan: Parameter String foto ditambahkan di ujung argumen
    private void kirimRequest(String url, String nim, String nama, String jurusan, String alamat, String foto) {
        StringRequest req = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        Toast.makeText(this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                        if ("success".equals(obj.getString("status"))) finish();
                    } catch (JSONException e) {
                        Toast.makeText(this, "Error format JSON", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Gagal konek ke server!", Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> p = new HashMap<>();
                p.put("nim", nim);
                p.put("nama", nama);
                p.put("jurusan", jurusan);
                p.put("alamat", alamat);
                p.put("foto", foto); // Sesuai Panduan: String foto dikirim ke PHP
                return p;
            }
        };
        Volley.newRequestQueue(this).add(req);
    }

    private void konfirmasiDelete() {
        new AlertDialog.Builder(this)
                .setTitle("Hapus Data")
                .setMessage("Yakin hapus data ini?")
                .setPositiveButton("Ya", (d, w) -> deleteData())
                .setNegativeButton("Batal", null)
                .show();
    }

    private void deleteData() {
        StringRequest req = new StringRequest(Request.Method.POST, APIConfig.URL_DELETE,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        Toast.makeText(this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                        if ("success".equals(obj.getString("status"))) finish();
                    } catch (JSONException e) {
                        Toast.makeText(this, "Error JSON", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Gagal konek!", Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> p = new HashMap<>();
                p.put("nim", etNIM.getText().toString().trim());
                return p;
            }
        };
        Volley.newRequestQueue(this).add(req);
    }
}
