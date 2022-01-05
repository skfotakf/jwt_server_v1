package com.koreait.jwt_server_v1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.koreait.jwt_server_v1.repository.JwtService;
import com.koreait.jwt_server_v1.repository.models.request.ReqLogin;
import com.koreait.jwt_server_v1.repository.models.response.RestLogin;
import com.koreait.jwt_server_v1.utils.KeyboardUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    static final String TAG = LoginActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText loginEt = findViewById(R.id.loginEt);
        EditText passwordEt = findViewById(R.id.passwordEt);
        Button loginBtn = findViewById(R.id.loginBtn);
        TextView moveLoginTv = findViewById(R.id.moveLoginTv);

        loginBtn.setOnClickListener(view -> {


            String id = loginEt.getText().toString();
            String pw = passwordEt.getText().toString();
            KeyboardUtil.hideKeyboard(view.getContext(), view);

            if(id.length()> 3 && pw.length() > 3) {
                JwtService jwtService = JwtService.retrofit.create(JwtService.class);
                ReqLogin reqLogin = new ReqLogin(id, pw);
                jwtService.getLogin(reqLogin).enqueue(new Callback<RestLogin>() {
                    @Override
                    public void onResponse(Call<RestLogin> call, Response<RestLogin> response) {
                        if(response.isSuccessful()) {
                            RestLogin restLogin = response.body();
                            Log.d(TAG, restLogin.toString());
                            Log.d(TAG, response.headers().get("Authorization")+"");

                            SharedPreferences preferences = getSharedPreferences("token", MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("jwt", response.headers().get("Authorization"));
                            editor.apply(); // 비동기 방식으로 저장

                        }
                    }
                    @Override
                    public void onFailure(Call<RestLogin> call, Throwable t) {
                        Snackbar.make(view, "실패", Snackbar.LENGTH_SHORT).show();
                        Log.d(TAG, t.toString());
                    }
                });
            } else {
               // Toast.makeText(view.getContext(), "잘못된 입력입니다", Toast.LENGTH_SHORT).show();
                Snackbar.make(view, "잘못된 입력입니다", Snackbar.LENGTH_SHORT).show();
            }
        });
        moveLoginTv.setOnClickListener(view -> {
            Intent intent = new Intent(this, SignupActivity.class);
            startActivity(intent);
        });
    }
}