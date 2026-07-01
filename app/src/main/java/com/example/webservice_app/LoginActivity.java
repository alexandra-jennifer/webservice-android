package com.example.webservice_app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    TextInputEditText etUsername, etPassword;
    MaterialButton btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin   = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(v -> prosesLogin());
    }

    private void prosesLogin() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (username.isEmpty()) { etUsername.setError("Username wajib diisi!"); return; }
        if (password.isEmpty()) { etPassword.setError("Password wajib diisi!"); return; }

        // Mencegah tombol di-klik dua kali
        btnLogin.setEnabled(false);
        btnLogin.setText("Loading...");

        StringRequest req = new StringRequest(
                Request.Method.POST, APIConfig.URL_LOGIN,
                response -> {
                    btnLogin.setEnabled(true);
                    btnLogin.setText("MASUK");
                    try {
                        JSONObject obj = new JSONObject(response);
                        Toast.makeText(this, obj.getString("message"), Toast.LENGTH_SHORT).show();

                        if ("success".equals(obj.getString("status"))) {
                            // Pindah ke halaman utama
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish(); // Tutup halaman login agar tidak bisa di-back
                        }
                    } catch (JSONException e) {
                        Toast.makeText(this, "Format balasan server salah", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    btnLogin.setEnabled(true);
                    btnLogin.setText("MASUK");
                    Toast.makeText(this, "Gagal konek ke server!", Toast.LENGTH_SHORT).show();
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);
                return params;
            }
        };

        Volley.newRequestQueue(this).add(req);
    }
}
