package pnj.uas.penitipanhewan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mText;
    private String text;
    private Button mData, mLokasi;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("login", MODE_PRIVATE );

        mText = findViewById(R.id.textView1);
        text = sharedPreferences.getString("nama", "gagal");
        text = "Selamat Datang "+text;
        mText.setText(text);

        mData = findViewById(R.id.actionData);
        mLokasi = findViewById(R.id.actionLokasi);

        mData.setOnClickListener(this);
        mLokasi.setOnClickListener(this);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.home:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.action_logout:
                SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();
                editor.commit();

                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.actionData:
                startActivity(new Intent(this, DataPenitipActivity.class));
                break;
            case R.id.actionLokasi:
                startActivity(new Intent(this, MapsActivity.class));
                break;
        }
    }

}