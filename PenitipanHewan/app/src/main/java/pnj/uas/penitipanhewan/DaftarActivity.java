package pnj.uas.penitipanhewan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class DaftarActivity extends AppCompatActivity {
    EditText regUser, regPass, regRepass;
    TextView navLogin;
    Button btnReg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar);

        regUser = findViewById(R.id.regUser);
        regPass = findViewById(R.id.regPass);
        regRepass = findViewById(R.id.regRepass);

        navLogin = findViewById(R.id.navLogin);

        btnReg = findViewById(R.id.btnReg);

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid()){
                    register();
                }
            }
        });

        navLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DaftarActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }

    boolean isValid(){
        String inUser = regUser.getText().toString();
        String inPass = regPass.getText().toString();
        String inRepass = regRepass.getText().toString();

        if(inUser.equals("") || inPass.equals("") || inRepass.equals("")){
            Toast.makeText(this, "Isi semua fill yang kosong", Toast.LENGTH_SHORT).show();
            return false;
        }else{
            if (inPass.equals(inRepass)){
                return true;
            }else {
                Toast.makeText(this, "Re-password Tidak sama", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
    }

    void register(){
        String isiFile = regUser.getText().toString() + ";" + regPass.getText().toString();

        File file = new File(getFilesDir(), regUser.getText().toString());
        FileOutputStream outputStream;

        try {
            file.createNewFile();
            outputStream = new FileOutputStream(file, false);
            outputStream.write(isiFile.getBytes());
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Toast.makeText(this, "Register Berhasil", Toast.LENGTH_SHORT).show();
        onBackPressed();
    }
}
