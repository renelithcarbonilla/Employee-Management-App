package com.example.employee_app;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AttendanceActivity extends AppCompatActivity {

    private Button btnTimeIn, btnTimeOut, btnBack;;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        btnTimeIn = findViewById(R.id.btnTimeIn);
        btnTimeOut = findViewById(R.id.btnTimeOut);

        // Kuhaa ang user_id gikan sa SharedPreferences nga atong gi-save sa Login
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        userId = sharedPreferences.getInt("user_id", -1);

        btnTimeIn.setOnClickListener(v -> sendAttendance("Time In"));
        btnTimeOut.setOnClickListener(v -> sendAttendance("Time Out"));
    }

    private void sendAttendance(String action) {
        ApiService apiService = RetrofitClient.getApiService();
        Call<AttendanceResponse> call = apiService.recordAttendance(userId, action);

        call.enqueue(new Callback<AttendanceResponse>() {
            @Override
            public void onResponse(Call<AttendanceResponse> call, Response<AttendanceResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(AttendanceActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<AttendanceResponse> call, Throwable t) {
                Toast.makeText(AttendanceActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}