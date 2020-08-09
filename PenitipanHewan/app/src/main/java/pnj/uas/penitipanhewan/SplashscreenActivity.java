package pnj.uas.penitipanhewan;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.os.Handler;

public class SplashscreenActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    private int waktu_loading=1500;
    //1500=1,5 detik

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        sharedPreferences = getSharedPreferences("login", MODE_PRIVATE );

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (sharedPreferences.getBoolean("isLogin", false)) {
                    Intent intent = new Intent(SplashscreenActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    //setelah loading maka akan langsung berpindah ke login
                    Intent home = new Intent(SplashscreenActivity.this, LoginActivity.class);
                    startActivity(home);
                    finish();
                }
            }
        },waktu_loading);

    }
}
