package com.example.game.megpt;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieDrawable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    TextView welcomeText;
    EditText sendMsg;
    Button sendBtn;
    Toolbar toolbar;
    List<msgModel> messageList;
    msgAdapter msgAdapter;
    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");

    OkHttpClient client = new OkHttpClient();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        messageList = new ArrayList<>();
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recyclerView);
        welcomeText = findViewById(R.id.centerTxt);
        sendMsg = findViewById(R.id.edtText);
        sendBtn = findViewById(R.id.sendBtn);
        setSupportActionBar(toolbar);
        toolbar.setTitle("meGPT");
        toolbar.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_dark));
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));


        msgAdapter = new msgAdapter(messageList);
        recyclerView.setAdapter(msgAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String question = sendMsg.getText().toString().trim();
                addToChat(question,msgModel.SENT_BY_ME);
                callAPI(question);
                sendMsg.setText("");

                welcomeText.setVisibility(View.GONE);
            }
        });
    }
    void addToChat(String message,String sentBy){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messageList.add(new msgModel(message,sentBy));
                msgAdapter.notifyDataSetChanged();
                recyclerView.smoothScrollToPosition(msgAdapter.getItemCount());
            }
        });
    }
    void addResponse(String response){
        messageList.remove(messageList.size()-1);
        addToChat(response,msgModel.SENT_BY_BOT);
    }
    void callAPI(String question){
        messageList.add(new msgModel("Typing... ",msgModel.SENT_BY_BOT));
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("model","text-davinci-003");
            jsonBody.put("prompt",question);
            jsonBody.put( "max_tokens",2000);
            jsonBody.put( "temperature",0.1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(jsonBody.toString(),JSON);
        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/completions")
                .addHeader("Authorization","Bearer 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")
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                addResponse("Failed to load due to "+e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                if(response.isSuccessful()){
                    JSONObject jsonObject ;
                    try {
                        jsonObject = new JSONObject(response.body().string());
                        JSONArray jsonArray = jsonObject.getJSONArray("choices");
                        String result = jsonArray.getJSONObject(0).getString("text");
                        addResponse(result.trim());
                    } catch (JSONException e) {
                        Log.d("Error",response.body().toString());
                        e.printStackTrace();
                    }

                }
                else{

                    addResponse("Failed to load due to "+response.body().string());
                }
            }
        });

    }
}