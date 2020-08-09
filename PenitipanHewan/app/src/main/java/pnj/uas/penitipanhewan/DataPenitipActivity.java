package pnj.uas.penitipanhewan;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import pnj.uas.penitipanhewan.adapter.AdapterPenitip;
import pnj.uas.penitipanhewan.model.ModelPenitip;
import pnj.uas.penitipanhewan.utils.Config;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DataPenitipActivity extends AppCompatActivity {

    private ListView mListviewMain;
    private AdapterPenitip adapter;
    private FloatingActionButton mFabAdd;
    private CoordinatorLayout rootLayout;

    public static final int REQUEST_ADD = 100;
    public static final int RESULT_ADD = 101;
    public static final int REQUEST_UPDATE = 200;
    public static final int RESULT_UPDATE = 201;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_penitip);

        mListviewMain = findViewById(R.id.listview_main);
        mFabAdd = findViewById(R.id.fab_add);
        rootLayout = findViewById(R.id.coordinator);

        adapter = new AdapterPenitip(this, R.layout.item_list);
        mListviewMain.setAdapter(adapter);

        mListviewMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ModelPenitip model = (ModelPenitip) parent.getAdapter().getItem(position);

                Intent intent = new Intent(DataPenitipActivity.this, DetailActivity.class);
                intent.putExtra("id", model.getId());
                startActivityForResult(intent, REQUEST_UPDATE);
            }
        });

        mListviewMain.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ModelPenitip model = (ModelPenitip) parent.getAdapter().getItem(position);
                showAlertDialog(model.getId());

                return true;
            }
        });

        mFabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DataPenitipActivity.this, AddDataActivity.class);
                startActivityForResult(intent, REQUEST_ADD);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        getData();
    }

    private void getData() {
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Config._LIST_PENITIP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("RESPONSE", "" + response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("penitip");
                            ArrayList<ModelPenitip> datas = new ArrayList<>();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject item = jsonArray.getJSONObject(i);

                                ModelPenitip model = new ModelPenitip();
                                model.setId(item.getString("id"));
                                model.setNama(item.getString("name"));
                                model.setTelepon(item.getString("telepon"));
                                model.setHewan(item.getString("hewan"));

                                datas.add(model);
                            }

                            adapter.clear();
                            adapter.addAll(datas);
                            adapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("RESPONSE", "" + error.getMessage());
            }
        });

        queue.add(stringRequest);
    }

    private void deleteData(String idPenitip) {
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                Config._DELETE_PENITIP + "?id=" + idPenitip,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("RESPONSE", "" + response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("status").equals("OK")) {
                                showSnackbar("Berhasil Menghapus Data");
                            } else {
                                showSnackbar("Gagal Menghapus Data");
                            }

                            getData();

                        } catch (JSONException e) {
                            Toast.makeText(DataPenitipActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("RESPONSE", "" + error.getMessage());
            }
        });

        queue.add(stringRequest);
    }

    private void showSnackbar(String message) {
        Snackbar snackbar = Snackbar.make(rootLayout,
                message,
                Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    private void showAlertDialog(final String idPenitip) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Apakah Anda ingin menghapus data ini?");

        builder.setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteData(idPenitip);
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ADD) {
            if (resultCode == RESULT_ADD) {
                showSnackbar("Berhasil Menambahkan Data");
            }
        } else if (requestCode == REQUEST_UPDATE) {
            if (resultCode == RESULT_UPDATE) {
                showSnackbar("Berhasil Memperbarui Data");
            }
        }
    }
}