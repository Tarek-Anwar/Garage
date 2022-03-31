package com.HomeGarage.garage.service;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FcmNotificationsSender  {

    private final String postUrl = "https://fcm.googleapis.com/fcm/send";
    private final String fcmServerKey ="AAAAyqSchTQ:APA91bGjkuA9FNg2MZKjq2u4SN3mtkQMGzqTQ93AmTGCXPS-FGmpKVxHPnoHNJAuV9bNCMNs9YH9uJ8UaOcrmFI_FgcGIGrx2qMy2tInT9FJdHVjVkG4czCDKi30uv4dUxLsnb1V2ttg";
    String userFcmToken;
    String title;
    String body;
    String id_reser;
    Context mContext;
    private RequestQueue requestQueue;

    public FcmNotificationsSender(String userFcmToken, String title, String body,String id_reser, Context mContext) {
        this.userFcmToken = userFcmToken;
        this.title = title;
        this.body = body;
        this.id_reser = id_reser;
        this.mContext = mContext;
    }

    public void SendNotifications() {

        requestQueue = Volley.newRequestQueue(mContext);
        JSONObject mainObj = new JSONObject();
        try {
            mainObj.put("to","/topics/"+ userFcmToken);
            JSONObject notiObject = new JSONObject();
            notiObject.put("title", title);
            notiObject.put("body", body);
            notiObject.put("tag" , id_reser);
            mainObj.put("notification", notiObject);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, postUrl, mainObj, response -> {
            }, error -> {
            }) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> header = new HashMap<>();
                    header.put("content-type", "application/json");
                    header.put("authorization", "key=" + fcmServerKey);
                    return header;
                }
            };
            requestQueue.add(request);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
