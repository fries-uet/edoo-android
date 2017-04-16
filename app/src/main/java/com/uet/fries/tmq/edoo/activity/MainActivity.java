package com.uet.fries.tmq.edoo.activity;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.uet.fries.tmq.edoo.R;
import com.uet.fries.tmq.edoo.app.AppController;
import com.uet.fries.tmq.edoo.fragment.TimetableFragment;
import com.uet.fries.tmq.edoo.helper.PrefManager;
import com.uet.fries.tmq.edoo.helper.dao.DaoSession;
import com.uet.fries.tmq.edoo.helper.dao.User;
import com.uet.fries.tmq.edoo.helper.dao.UserDao;
import com.uet.fries.tmq.edoo.rest.RestClient;
import com.uet.fries.tmq.edoo.rest.model.ItemResponse;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String URL_DOWNLOAD_APK = "https://edoo.vn/";
    private static final int REQUEST_CODE_EDIT = 1234;

    private User user;
    private View header;

    private TimetableFragment timetableFragment = new TimetableFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        showFragment(timetableFragment);

        setupUserInfo();
    }

    private void setupUserInfo() {
        if (!PrefManager.isLoggedIn()) {
            logout();
            return;
        }

        if (PrefManager.isFirstLoggedIn()) {
            // Todo: First Login -> IntroActivity
        }

        DaoSession daoSession = ((AppController) getApplication()).getDaoSession();
        UserDao userDao = daoSession.getUserDao();
        user = userDao.loadAll().get(0);
        Log.i(TAG, "user = " + user.getName());
        daoSession.clear();

        // Todo: replace fragment moi

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        header = navigationView.getHeaderView(0);

        TextView tvName = (TextView) header.findViewById(R.id.tv_name);
        TextView tvEmail = (TextView) header.findViewById(R.id.tv_email);

        String capability = user.getCapability();
        if (capability.equalsIgnoreCase("student")) {
            capability = "Sinh viên";
        } else if (capability.equalsIgnoreCase("teacher")) {
            capability = "Giảng viên";
        }
        tvName.setText(user.getName() + " (" + capability + ")");
        tvEmail.setText(user.getEmail());

        updateAvatar();
    }

    private void updateAvatar() {
        CircleImageView ivAvatar = (CircleImageView) header.findViewById(R.id.iv_avatar);
        ivAvatar.setFillColor(Color.WHITE);

        String urlAvatar = user.getAvatar();
        if (urlAvatar.isEmpty()) urlAvatar += "...";
        Picasso.with(this).invalidate(urlAvatar);
        Picasso.with(this)
                .load(urlAvatar).fit()
                .placeholder(R.drawable.ic_user)
                .error(R.drawable.ic_user)
                .into(ivAvatar);
    }

    private void showFragment(Fragment lopFragment) {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        }
        getFragmentManager().beginTransaction().replace(R.id.container, lopFragment).commit();
    }

    private long prevTime = 0;
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            long currentTime = System.currentTimeMillis();
            if (currentTime - prevTime <= 1500) {
                super.onBackPressed();
            } else {
                Toast.makeText(this, getResources().getString(R.string.lert_press_back), Toast.LENGTH_SHORT).show();
            }
            prevTime = currentTime;
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
                showFragment(timetableFragment);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_EDIT:
                if (resultCode == RESULT_OK) {
                    updateAvatar();
                }
                break;
        }
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
