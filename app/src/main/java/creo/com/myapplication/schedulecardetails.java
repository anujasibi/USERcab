package creo.com.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import creo.com.myapplication.utils.Global;
import creo.com.myapplication.utils.SessionManager;

public class schedulecardetails extends AppCompatActivity {
    TextView name;
    SessionManager sessionManager;
    private String URLline ="https://creocabs.herokuapp.com/user/get_schedule_trip_details/";
    private ProgressDialog dialog ;
    Context context=this;
    private String tripid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedulecardetails);
        sessionManager = new SessionManager(this);

        name=findViewById(R.id.name);
        Bundle bundle = getIntent().getExtras();
        tripid = bundle.getString("tripid");
        car();
    }
    public void car(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLline,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       // dialog.dismiss();
                        Toast.makeText(schedulecardetails.this,response,Toast.LENGTH_LONG).show();
                        //parseData(response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String ot = jsonObject.optString("message");
                            String status=jsonObject.optString("code");
                            String rate=jsonObject.optString("data");
                            JSONArray jsonArray=new JSONArray();
                            JSONObject jsonObject1=jsonArray.getJSONObject(0);
                            String name=jsonObject1.getString("driver");
                            Log.d("otp","mm"+name);
                            if(status.equals("200")){
                                Toast.makeText(schedulecardetails.this, ot, Toast.LENGTH_LONG).show();
                            }
                            else{
                                Toast.makeText(schedulecardetails.this, "Invalid Password."+ot, Toast.LENGTH_LONG).show();


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("response","hhh"+response);


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(schedulecardetails.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("trip_id",tripid);

                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Token "+sessionManager.getTokens());
                return params;
            }


        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);


    }
}
