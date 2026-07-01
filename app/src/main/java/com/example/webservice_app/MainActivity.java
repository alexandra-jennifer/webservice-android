package com.example.webservice_app;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView rvMahasiswa;
    SwipeRefreshLayout swipeRefresh;
    EditText etCari;
    FloatingActionButton btnTambah;
    TextView tvJumlah;

    List<Mahasiswa> listAll     = new ArrayList<>();
    List<Mahasiswa> listDisplay = new ArrayList<>();
    MahasiswaAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvMahasiswa  = findViewById(R.id.rvMahasiswa);
        swipeRefresh = findViewById(R.id.swipeRefresh);
        etCari       = findViewById(R.id.etCari);
        btnTambah    = findViewById(R.id.btnTambah);
        tvJumlah     = findViewById(R.id.tvJumlah);

        // Setup adapter
        adapter = new MahasiswaAdapter(this, listDisplay, mhs -> {
            Intent intent = new Intent(this, FormActivity.class);
            intent.putExtra("mode","edit");
            intent.putExtra("nim",mhs.getNim());
            intent.putExtra("nama",mhs.getNama());
            intent.putExtra("jurusan",mhs.getJurusan());
            intent.putExtra("alamat",mhs.getAlamat());
            intent.putExtra("foto",mhs.getFoto());
            startActivity(intent);
        });

        rvMahasiswa.setLayoutManager(new LinearLayoutManager(this));
        rvMahasiswa.setAdapter(adapter);

        // FAB tombol tambah
        btnTambah.setOnClickListener(v ->
                startActivity(new Intent(this, FormActivity.class).putExtra("mode", "tambah"))
        );

        // Swipe refresh
        swipeRefresh.setColorSchemeColors(
                getResources().getColor(R.color.colorPrimary),
                getResources().getColor(R.color.colorFAB)
        );
        swipeRefresh.setOnRefreshListener(() -> {
            ambilData();
            swipeRefresh.setRefreshing(false);
        });

        // Pencarian real-time
        etCari.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int a, int b, int c) {}
            public void afterTextChanged(Editable s) {}
            public void onTextChanged(CharSequence s, int a, int b, int c) {
                filterData(s.toString());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ambilData();
    }

    private void ambilData() {
        StringRequest req = new StringRequest(
                Request.Method.GET, APIConfig.URL_MAHASISWA,
                response -> {
                    try {
                        JSONObject obj   = new JSONObject(response);
                        JSONArray  array = obj.getJSONArray("data");
                        listAll.clear();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject item = array.getJSONObject(i);
                            listAll.add(new Mahasiswa(
                                    item.getString("NIM"),
                                    item.getString("Nama"),
                                    item.getString("Jurusan"),
                                    item.getString("Alamat"),
                                    item.optString("foto","")
                            ));
                        }
                        filterData(etCari.getText().toString());
                    } catch (JSONException e) {
                        Toast.makeText(this, "Error membaca data!", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this,
                        "Gagal konek ke server! Cek IP di ApiConfig.java",
                        Toast.LENGTH_LONG).show()
        );
        Volley.newRequestQueue(this).add(req);
    }

    private void filterData(String keyword) {
        listDisplay.clear();
        for (Mahasiswa m : listAll) {
            if (m.getNama().toLowerCase().contains(keyword.toLowerCase()) ||
                    m.getNim().toLowerCase().contains(keyword.toLowerCase())) {
                listDisplay.add(m);
            }
        }
        // Update teks jumlah
        tvJumlah.setText(listDisplay.size() + " mahasiswa terdaftar");
        adapter.notifyDataSetChanged();
    }
}