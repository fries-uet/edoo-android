package com.uet.fries.tmq.edoo.rest.model;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * Created by tmq on 12/04/2017.
 */

public class ItemTypeAdapterFactory implements TypeAdapterFactory {
    private static final String TAG = ItemTypeAdapterFactory.class.getSimpleName();
    private String field;

    public ItemTypeAdapterFactory(String field) {
        this.field = field;
    }

    @Override
    public <T> TypeAdapter<T> create(Gson gson, final TypeToken<T> type) {

        final TypeAdapter<T> delegate = gson.getDelegateAdapter(this, type);
        final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);

        return new TypeAdapter<T>() {

            public void write(JsonWriter out, T value) throws IOException {
                delegate.write(out, value);
            }

            public T read(JsonReader in) throws IOException {

                JsonElement jsonElement = elementAdapter.read(in);
                if (jsonElement.isJsonObject()) {
                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    if (jsonObject.has("data") && !jsonObject.get("data").isJsonNull()) {
                        jsonElement = jsonObject.get("data");
                        Log.i(TAG, jsonElement.toString());
                        JsonObject jsonData = jsonElement.getAsJsonObject();
                        if (jsonData.has(field) && !jsonData.get(field).isJsonNull()) {
                            jsonElement = jsonData.get(field);
                        }
                    }

                }

                return delegate.fromJsonTree(jsonElement);
            }
        }.nullSafe();
    }

}
