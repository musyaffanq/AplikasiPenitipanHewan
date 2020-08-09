package pnj.uas.penitipanhewan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class LoginActivity extends AppCompatActivity {

    EditText username, password;
    Button btnLogin;
    TextView navReg;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);

        username = findViewById(R.id.inuser);
        password = findViewById(R.id.inpass);

        navReg = findViewById(R.id.navReg);

        btnLogin = findViewById(R.id.btnlgn);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid()){
                    loginFromFile();
                }
            }
        });

        navReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, DaftarActivity.class);
                startActivity(intent);
            }
        });
    }

    boolean isValid(){
        String inUser = username.getText().toString();
        String inPass = password.getText().toString();

        if(inUser.equals("") || inPass.equals("")){
            Toast.makeText(this, "Isi semua fill yang kosong", Toast.LENGTH_SHORT).show();
            return false;
        }else{
            return true;
        }
    }

    void loginFromFile(){
        File file = new File(getFilesDir(), username.getText().toString());
        if (file.exists()){
            StringBuilder text = new StringBuilder();
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line = br.readLine();
                while (line!=null){
                    text.append(line);
                    line = br.readLine();
                }
            } catch (IOException e) {
                System.out.println("Error" + e.getMessage());
            }

            String data = text.toString();
            String[] dataUser = data.split(";");

            if (dataUser[1].equals(password.getText().toString())){
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isLogin", true);
                editor.putString("nama", dataUser[0]);
                editor.apply();

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }else {
                Toast.makeText(this, "Password Salah", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(this, "Maaf User Tidak Ditemukan", Toast.LENGTH_SHORT).show();
        }
    }

//    void checkIflogin(){
//        if(username.getText().toString().equals("admin") && password.getText().toString().equals("admin") ){
//
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//            editor.putBoolean("isLogin", true);
//            editor.putString("username", username.getText().toString());
//            editor.commit();
//
//            Intent intent = new Intent(login.this, HalamanUtamaActivity.class);
//            startActivity(intent);
//            finish();
//        } else {
//            AlertDialog.Builder alert = new AlertDialog.Builder(this);
//            alert.setTitle("login");
//            alert.setMessage("Username atau Password salah");
//            alert.setIcon(R.drawable.ic_warning_black_24dp);
//            alert.setNegativeButton("OK", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int i) {
//                    dialog.dismiss();
//                }
//            });
//        }
//    }


}
