package com.example.employee_app;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InsertActivity extends AppCompatActivity {

    // Gi-update ang variables para sa bag-ong fields
    EditText edt_fname, edt_lname, edt_position;
    Button btn_submit;

    String sfname = "", slname = "", sposition = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        // Siguroha nga ang IDs sa imong XML (activity_insert.xml)
        // motakdo ani o i-update ang IDs diri.
        edt_fname = (EditText) findViewById(R.id.edt_name); // Gamiton una nato ang karaan nga ID
        edt_lname = (EditText) findViewById(R.id.edt_lname); // Pagdugang og EditText sa XML para sa Lname
        edt_position = (EditText) findViewById(R.id.edt_position);

        btn_submit = (Button) findViewById(R.id.btn_submit);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Check if empty
                if (edt_fname.getText().toString().trim().isEmpty() ||
                        edt_position.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Enter value", Toast.LENGTH_SHORT).show();
                } else {
                    sfname = edt_fname.getText().toString().trim();

                    // Kung wala pa kay edt_lname sa XML, butangi lang una og temporary string
                    // o i-check kung null ba para dili mag-error
                    slname = (edt_lname != null) ? edt_lname.getText().toString().trim() : "";
                    sposition = edt_position.getText().toString().trim();

                    // Karon, tulo na ang sulod sa constructor: fname, lname, division
                    Staff staff = new Staff(sfname, slname, sposition);

                    StaffRepository staffRepository = new StaffRepository(getApplicationContext());
                    staffRepository.InsertTask(staff);

                    // 2. SAVE TO ONLINE (MySQL via Retrofit)
                    // Mao ni ang bag-ong step para sa integration
                    // Kinahanglan 9 gyud ka strings ang ipasa:
                    ApiService apiService = RetrofitClient.getApiService();
                    Call<LoginResponse> call = apiService.insertStaffToMySQL(
                            sfname,         // 1. fname
                            slname,         // 2. lname
                            "Not Specified",// 3. gender (default)
                            "Full Time",    // 4. emp_type (default)
                            sposition,      // 5. division
                            "None",         // 6. contact (default)
                            "None",         // 7. address (default)
                            "None",         // 8. email (default)
                            "true"          // 9. from_android (flag)
                    );
                    call.enqueue(new Callback<LoginResponse>() { // Usba ang String ngadto sa LoginResponse
                        @Override
                        public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                LoginResponse res = response.body();

                                // Dinhi nimo i-check ang status gikan sa PHP
                                if (res.getStatus().equals("success")) {
                                    Toast.makeText(getApplicationContext(), res.getMessage(), Toast.LENGTH_SHORT).show();
                                    // Pwede na ka mobalhin sa sunod nga screen diri
                                } else {
                                    Toast.makeText(getApplicationContext(), res.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<LoginResponse> call, Throwable t) {
                            // Inig naay error sa koneksyon (e.g. sayop nga IP address)
                            Toast.makeText(getApplicationContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    Toast.makeText(InsertActivity.this, "Employee Added", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }
}