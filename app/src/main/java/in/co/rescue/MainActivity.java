package in.co.rescue;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity  {

    RecyclerView recyclerView;
    public static RecyclerView.Adapter<StateAdapter.ViewHolder> adapter;
    public static List<State> lststate;

    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lststate = new ArrayList<State>();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_state);
        recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this,2));

        try {
            Thread.sleep(1000);
            loadstates();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
    }


    private void loadstates() {

        pDialog= ProgressDialog.show(MainActivity.this,"","Loading...",false,false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url.URL_GETSTATES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                           Toast.makeText(MainActivity.this, response, Toast.LENGTH_LONG).show();
                        pDialog.dismiss();
                        try {
                            JSONObject object = new JSONObject(response);
                            JSONArray jsonArray = object.getJSONArray("result");

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject request = jsonArray.getJSONObject(i);

                                String state = request.getString("state");
                                String count = request.getString("count");

                                lststate.add(new State(state,count));
                            }
                            adapter = new StateAdapter(MainActivity.this, lststate);
                            recyclerView.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        pDialog.dismiss();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
