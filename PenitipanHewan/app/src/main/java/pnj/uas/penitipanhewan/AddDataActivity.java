package pnj.uas.penitipanhewan;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import pnj.uas.penitipanhewan.utils.Config;
import pnj.uas.penitipanhewan.utils.VolleyMultipartRequest;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddDataActivity extends AppCompatActivity {

    private EditText mEdtNama, mEdtTelepon, mEdtHewan;
    private Button mBtnSave, mBtnImage;
    private ImageView imageView;

    private static final int REQUEST_PERMISSION = 100;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int CAMERA_REQUEST = 2;
    private String filepath;
    private Bitmap image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data);

        // Set toolbar name dan back button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Tambah Data Penitip");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Casting
        mEdtNama = findViewById(R.id.edtNama);
        mEdtTelepon = findViewById(R.id.edtTelepon);
        mEdtHewan = findViewById(R.id.edtHewan);
        mBtnSave = findViewById(R.id.actionSimpan);
        mBtnImage = findViewById(R.id.actionImage);
        imageView = findViewById(R.id.image);

        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEdtNama.getText().toString().length() > 0 &&
                        mEdtTelepon.getText().toString().length() > 0 &&
                        mEdtHewan.getText().toString().length() > 0 && image != null
                ) {
                    saveData();
                } else {
                    Toast.makeText(AddDataActivity.this, "Harap Lengkapi Data", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mBtnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) ||
                        (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                        || (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)) {

                    ActivityCompat.requestPermissions(AddDataActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, REQUEST_PERMISSION);

                } else {

                    AlertDialog.Builder alert = new AlertDialog.Builder(AddDataActivity.this);
                    alert.setItems(new String[]{"Pilih Gambar", "Camera"}, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == 0) {
                                showFileChooser();
                            } else {
                                showCamera();
                            }
                        }
                    });
                    alert.show();

                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            Picasso.get().load(data.getData()).into(imageView);
            try {
                image = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
            } catch (IOException e) {
                Toast.makeText(AddDataActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            File file = new File(filepath);
            Uri photoUri = FileProvider.getUriForFile(this,
                    "pnj.uas.penitipanhewan.fileprovider",
                    file);
            Picasso.get().load(photoUri).into(imageView);

            image = BitmapFactory.decodeFile(filepath);
        }
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Pilih Gambar"), PICK_IMAGE_REQUEST);
    }

    private void showCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Toast.makeText(AddDataActivity.this, "" + ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "pnj.uas.penitipanhewan.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        filepath = image.getAbsolutePath();
        return image;
    }

    private byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void saveData() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        VolleyMultipartRequest request = new VolleyMultipartRequest(Request.Method.POST,
                Config._ADD_PENITIP,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            JSONObject jsonObject = new JSONObject(new String(response.data));
                            Toast.makeText(AddDataActivity.this, jsonObject.getString("message"),
                                    Toast.LENGTH_SHORT).show();

                            if (jsonObject.getString("status").equals("OK")) {
                                Toast.makeText(AddDataActivity.this, jsonObject.getString("message"),
                                        Toast.LENGTH_SHORT).show();
                                finish();
                            }

                        } catch (JSONException ex) {
                            Toast.makeText(AddDataActivity.this, "" + ex.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddDataActivity.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> parameter = new HashMap<>();
                parameter.put("name", mEdtNama.getText().toString());
                parameter.put("telepon", mEdtTelepon.getText().toString());
                parameter.put("hewan", mEdtHewan.getText().toString());

                return parameter;
            }

            @Override
            protected Map<String, DataPart> getByteData() throws AuthFailureError {
                HashMap<String, DataPart> datas = new HashMap<>();
                datas.put("image", new DataPart(System.currentTimeMillis()+".jpeg", getFileDataFromDrawable(image)));
                return datas;
            }
        };
        requestQueue.add(request);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
