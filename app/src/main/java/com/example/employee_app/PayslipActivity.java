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

public class PayslipActivity extends AppCompatActivity {
    //Payslip Activity: Implementation for Payslip
    TextView tvName, tvNetPay, tvOvertime, tvBonus, tvDeduction;
    Button btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payslip);

        // 1. Initialize UI
        tvName = findViewById(R.id.tvName);
        tvNetPay = findViewById(R.id.tvNetPay);
        tvOvertime = findViewById(R.id.tvOvertime);
        tvBonus = findViewById(R.id.tvBonus);
        tvDeduction = findViewById(R.id.tvDeduction);

        // 2. Kuhaon ang user_id gikan sa SharedPreferences (same name sa ProfileActivity)
        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
        int userId = prefs.getInt("user_id", 0);

        if (userId != 0) {
            fetchPayslip(userId);
        } else {
            Toast.makeText(this, "User ID not found, please login again", Toast.LENGTH_SHORT).show();
        }
    }


    private void fetchPayslip(int userId) {
        ApiService apiService = RetrofitClient.getApiService();
        apiService.getPayslip(userId).enqueue(new Callback<PayslipResponse>() {
            @Override
            public void onResponse(Call<PayslipResponse> call, Response<PayslipResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    PayslipResponse data = response.body();

                    if (data.getStatus().equals("success")) {
                        // 3. I-display ang data (i-convert ang numbers to String gamit ang String.valueOf)
                        tvName.setText(data.getFullname());
                        tvNetPay.setText("₱ " + String.valueOf(data.getNetPay()));
                        tvOvertime.setText("Overtime: ₱ " + String.valueOf(data.getOvertime()));
                        tvBonus.setText("Bonus: ₱ " + String.valueOf(data.getBonus()));
                        tvDeduction.setText("Deductions: ₱ " + String.valueOf(data.getDeduction()));
                    } else {
                        Toast.makeText(PayslipActivity.this, "No payslip data found", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<PayslipResponse> call, Throwable t) {
                Toast.makeText(PayslipActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}