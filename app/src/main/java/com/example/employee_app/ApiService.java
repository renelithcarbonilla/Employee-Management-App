package com.example.employee_app;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {
    // Network: Configuration for Node.js backend
    // KINI PARA SA LOGIN (Step 1)
    @FormUrlEncoded
    @POST("login")
    Call<LoginResponse> loginUser(
            @Field("username") String username,
            @Field("password") String password
    );

    // KINI PARA SA ADD EMPLOYEE (Step 3)
    @FormUrlEncoded
    @POST("api/add_employee.php")
    Call<LoginResponse> insertStaffToMySQL(
            @Field("fname") String fname,
            @Field("lname") String lname,
            @Field("gender") String gender,
            @Field("emp_type") String type,
            @Field("division") String division,
            @Field("contact") String contact,
            @Field("address") String address,
            @Field("email") String email,
            @Field("from_android") String source
    );

    @FormUrlEncoded
    @POST("api_attendance.php") // Siguroha nga sakto ang path sa imong PHP file
    Call<AttendanceResponse> recordAttendance(
            @Field("user_id") int userId,
            @Field("action") String action
    );

    // I-add ni sa imong ApiService interface
    @GET("get_payslip.php")
    Call<PayslipResponse> getPayslip(@Query("user_id") int userId);

    @GET("get_profile.php")
    Call<LoginResponse> getProfile(@Query("user_id") int userId);
}