package com.cheezburger.simple;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by IntelliJ IDEA.
 * User: john
 * Date: 3/21/12
 * Time: 9:12 AM
 * To change this template use File | Settings | File Templates.
 */
public class CheezburgerClient {

    private String clientId;
    private String accessToken;
    private Handler handler = new Handler();

    public CheezburgerClient(Context context) {

        clientId = context.getResources().getString(R.string.client_id);
        accessToken = context.getResources().getString(R.string.access_token);
    }

    // TODO: Include a callback for on success
    public void getImageUrlForAsset(final long assetId, final CheezburgerOnResultHandler<String> onResultHandler) {
        run(new Runnable() {
            @Override
            public void run() {
                DefaultHttpClient httpClient = new DefaultHttpClient();

                // TODO: How to define stuff like client ID in resource. Securely?
                HttpGet request = new HttpGet("https://api.cheezburger.com/v1/assets/" + assetId + "?access_token=" + accessToken);

                String jsonText = "{}";
                try {
                    HttpResponse response = httpClient.execute(request);
                    jsonText = EntityUtils.toString(response.getEntity());
                } catch (Exception e) {
                    failure(new CheezburgerException(e.getMessage(), e), onResultHandler);
                    return;
                }

                try {
                    JSONObject json = new JSONObject(jsonText);
                    String imageUrl = json.getJSONArray("items").getJSONObject(0).getString("image_url");
                    success(imageUrl, onResultHandler);
                } catch (JSONException e) {
                    failure(new CheezburgerException(e.getMessage(), e), onResultHandler);
                }
            }
        });
    }

    private void failure(final CheezburgerException exception, final CheezburgerOnResultHandler<?> onResultHandler) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                onResultHandler.onFailure(exception);
            }
        });
    }

    private <TResult> void success(final TResult result, final CheezburgerOnResultHandler<TResult> onResultHandler) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                onResultHandler.onSuccess(result);
            }
        });
    }
    
    private void run(final Runnable runnable) {
        new Thread(runnable).run();
    }
}

