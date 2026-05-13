package com.example.employee_app;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {
    TextView profName, profType, profDivision;
    TextView tvFullname, tvType, tvDivision;
    Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Siguroha nga kani nga IDs ang naa sa imong activity_profile.xml
        profName = findViewById(R.id.profName);
        profType = findViewById(R.id.profType);
        profDivision = findViewById(R.id.profDivision);
        btnBack = findViewById(R.id.btnBack);

        // 1. Kuhaa ang user_id (Dili emp_id, kay sa user table man ang imong login)
        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
        int userId = prefs.getInt("user_id", 0);

        btnBack.setOnClickListener(v -> finish());

        // 2. Tawga ang database
        ApiService apiService = RetrofitClient.getApiService();

        // Gamita ang userId (4) nga nakuha gikan sa login
        apiService.getProfile(userId).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse profile = response.body();

                    if (profile.getStatus().equals("success")) {
                        // DIREKTA i-set sa imong TextViews para ma-overwrite ang "N/A"
                        profName.setText("Name: " + profile.getFullname());
                        profType.setText("Position: " + profile.getEmpType());
                        profDivision.setText("Division: " + profile.getDivision());
                    } else {
                        Toast.makeText(ProfileActivity.this, "Profile not found in DB", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}