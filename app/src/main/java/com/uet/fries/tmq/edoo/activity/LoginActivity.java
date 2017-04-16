package com.uet.fries.tmq.edoo.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.widget.EditText;
import android.widget.Toast;


import com.uet.fries.tmq.edoo.R;
import com.uet.fries.tmq.edoo.app.AppController;
import com.uet.fries.tmq.edoo.helper.dao.DaoSession;
import com.uet.fries.tmq.edoo.helper.dao.UserDao;
import com.uet.fries.tmq.edoo.rest.RestClient;
import com.uet.fries.tmq.edoo.rest.model.ItemLogin;
import com.uet.fries.tmq.edoo.util.CommonVLs;
import com.uet.fries.tmq.edoo.helper.PrefManager;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends Activity {
    private static final String TAG = LoginActivity.class.getSimpleName();

    private ProgressDialog pDialog;

    @BindView(R.id.edt_email)
    EditText edtEmail;

    @BindView(R.id.edt_password)
    EditText edtPassword;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        if (PrefManager.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        if (!CommonVLs.isHasNetworkPermissions(this)) {
            CommonVLs.verifyInternetStatePermissions(this);
        }
    }

    @OnClick(R.id.btn_login)
    void login() {
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (!email.isEmpty() && !password.isEmpty()) {
            checkLogin(email, password);
        } else {
            Toast.makeText(getApplicationContext(), "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * function to verify login details in mysql db
     */
    private void checkLogin(final String email, final String password) {
        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Đăng nhập ...");
        pDialog.show();

        RestClient restClient = new RestClient(this, null);
        restClient.getApiService().login(email, password).enqueue(new Callback<ItemLogin>() {
            @Override
            public void onResponse(Call<ItemLogin> call, Response<ItemLogin> response) {
                ItemLogin itemLogin = response.body();

                PrefManager.setTokenLogin(itemLogin.getToken());
                PrefManager.setLogin(true);

                // Todo: Add User into Database
                DaoSession daoSession = ((AppController)getApplication()).getDaoSession();
                UserDao userDao = daoSession.getUserDao();
                userDao.insert(itemLogin.getUser());

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

                pDialog.dismiss();

                Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ItemLogin> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getApplicationContext(), "Lỗi đăng nhập", Toast.LENGTH_SHORT).show();
                pDialog.dismiss();
            }
        });
    }

    @OnClick(R.id.btn_forgot_password)
    void forgotPassword() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle("Thông báo");
        builder.setMessage("Chức năng đang được phát triển. \nĐể khôi phục mật khẩu, bạn vui lòng liên hệ với đội phát triển thông qua mail:\nFries.uet@gmail.com");
        builder.setPositiveButton("OK", null);
        builder.show();
    }
}
