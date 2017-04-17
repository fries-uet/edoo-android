package com.uet.fries.tmq.edoo.rest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.widget.Toast;

import com.google.gson.Gson;
import com.uet.fries.tmq.edoo.activity.LoginActivity;
import com.uet.fries.tmq.edoo.helper.PrefManager;
import com.uet.fries.tmq.edoo.rest.model.ItemResponse;

import java.io.IOException;

import retrofit2.Response;

/**
 * Created by tmq on 17/04/2017.
 */

public class ErrorResponse {

    public static ItemResponse getItemResponse(Response response) {
        ItemResponse itemResponse = new ItemResponse();
        Gson gson = new Gson();
        try {
            itemResponse = gson.fromJson(response.errorBody().string(), ItemResponse.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return itemResponse;
    }

    public static void handleError(Response response, Context context){
        ItemResponse itemResponse;
        Gson gson = new Gson();
        try {
            itemResponse = gson.fromJson(response.errorBody().string(), ItemResponse.class);

            switch (itemResponse.getError()) {
                case "Unauthorized":
                    PrefManager.setLogin(false);
                    final Intent intent = new Intent(context, LoginActivity.class);
                    Toast.makeText(context, "Phiên làm việc đã hết. Đăng nhập lại", Toast.LENGTH_SHORT).show();
                    context.startActivity(intent);
                    ((Activity) context).finish();
                    break;
                default:
                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
