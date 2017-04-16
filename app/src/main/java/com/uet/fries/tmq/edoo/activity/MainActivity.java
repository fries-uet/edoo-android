package com.uet.fries.tmq.edoo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.uet.fries.tmq.edoo.R;
import com.uet.fries.tmq.edoo.app.AppController;
import com.uet.fries.tmq.edoo.helper.PrefManager;
import com.uet.fries.tmq.edoo.helper.dao.DaoSession;
import com.uet.fries.tmq.edoo.helper.dao.User;
import com.uet.fries.tmq.edoo.helper.dao.UserDao;
import com.uet.fries.tmq.edoo.rest.RestClient;
import com.uet.fries.tmq.edoo.rest.model.ItemResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String URL_DOWNLOAD_APK = "https://edoo.vn/";

    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        DaoSession daoSession = ((AppController) getApplication()).getDaoSession();
        UserDao userDao = daoSession.getUserDao();
        user = userDao.loadAll().get(0);
        Log.i(TAG, "user = " + user.getName());
        daoSession.clear();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_lopMonHoc:
                break;
            case R.id.nav_introduction:
                break;
            case R.id.nav_share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Cùng trải nghiệm môi trường học tập khác biệt với " + URL_DOWNLOAD_APK);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                break;
            case R.id.nav_thoikhoabieu:
                break;
            case R.id.nav_updateAccount:
                break;
            case R.id.nav_logout:
                logout();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logout() {
        RestClient restClient = new RestClient(this, null);
        restClient.getApiService().logout(PrefManager.getTokenLogin()).enqueue(new Callback<ItemResponse>() {
            @Override
            public void onResponse(Call<ItemResponse> call, Response<ItemResponse> response) {
                Toast.makeText(MainActivity.this, "Đăng xuất", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ItemResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
        // xoa session
        PrefManager.setLogin(false);

        // xoa user, classes
        DaoSession daoSession = ((AppController) getApplication()).getDaoSession();
        UserDao userDao = daoSession.getUserDao();
        user = userDao.loadAll().get(0);
        userDao.delete(user);
        user = null;
        daoSession.clear();

        // thoat ra man hinh dang nhap
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
