package com.cheezburger.simple;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by IntelliJ IDEA.
 * User: john
 * Date: 2/29/12
 * Time: 9:36 AM
 * To change this template use File | Settings | File Templates.
 */
public class AssetFragment extends Fragment {
    
    public static Fragment newInstance(String imageUrl) {
        Bundle b = new Bundle();
        b.putString("imageUrl", imageUrl);
        Fragment f = new AssetFragment();
        f.setArguments(b);
        return f;
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.asset_fragment, container, false);

        final ImageView image = (ImageView) root.findViewById(R.id.asset_img);
        
        new AsyncTask<Void, Void, Drawable>() {
            @Override
            protected Drawable doInBackground(Void... voids) {
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpGet request = new HttpGet(getArguments().getString("imageUrl"));

                InputStream inputStream = null;

                try {
                    HttpResponse response = httpClient.execute(request);
                    inputStream = response.getEntity().getContent();
                }
                catch (Exception e) {
                    Log.e("DownloadImage.doInBackground", e.getMessage());
                }

                return Drawable.createFromStream(inputStream, "src");
            }

            @Override
            protected void onPreExecute() {
                getActivity().setProgressBarIndeterminateVisibility(true);
            }

            @Override
            protected void onPostExecute(Drawable drawable) {
                image.setImageDrawable(drawable);
                getActivity().setProgressBarIndeterminateVisibility(false);
            }
        }.execute();

        return root;
    }
}
