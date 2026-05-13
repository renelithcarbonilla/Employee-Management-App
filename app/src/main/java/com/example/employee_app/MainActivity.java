package com.example.employee_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View; // Importante ni para sa OnClickListener
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    // Integration: Retrofit setup for API calls
    private TextView tvWelcome;
    private Button btnAttendance, btnViewProfile, btnViewPayslip; // I-declare tanan buttons

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. Initialize UI Elements
        tvWelcome = findViewById(R.id.tvWelcome);
        btnAttendance = findViewById(R.id.btn_attendance);
        btnViewProfile = findViewById(R.id.btnViewProfile); // Siguroha nga naa ni sa activity_main.xml
        btnViewPayslip = findViewById(R.id.btnViewPayslip); // Siguroha nga naa ni sa activity_main.xml

        // 2. Kuhaa ang data gikan sa SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        String fullname = sharedPreferences.getString("fullname", "User");
        tvWelcome.setText("Welcome, " + fullname + "!");

        // 3. Button para sa ATTENDANCE
        btnAttendance.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AttendanceActivity.class);
            startActivity(intent);
        });

        // 4. Button para sa PROFILE (Aron ma-pislit na siya)
        btnViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // I-change ang ProfileActivity.class kung unsa man gani ang ngalan sa imong Profile screen
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        // 5. Button para sa PAYSLIP
        btnViewPayslip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mo-diretso sa PayslipActivity nga imong gihimo kaganina
                Intent intent = new Intent(MainActivity.this, PayslipActivity.class);
                startActivity(intent);
            }
        });
    }
}