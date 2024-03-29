package com.cheezburger.simple;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;

public class MainActivity extends SherlockFragmentActivity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        //getSharedPreferences("com.cheezburger.simple", MODE_PRIVATE).edit().putString("key", "value").commit();

        setContentView(R.layout.main);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (Intent.ACTION_SEND.equals(getIntent().getAction())) {
            Bundle extras = getIntent().getExtras();

            if (extras.containsKey(Intent.EXTRA_STREAM)) {
                Uri selectedImageUri = (Uri) extras.getParcelable(Intent.EXTRA_STREAM);
                String filePath = null;
                String[] filePathColumn = {MediaStore.MediaColumns.DATA};
                String scheme = selectedImageUri.getScheme();

                if (scheme.equals("content")) {
                    Cursor cursor = getContentResolver().query(selectedImageUri, filePathColumn, null, null, null);
                    cursor.moveToFirst(); // <--no more NPE

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);

                    filePath = cursor.getString(columnIndex);

                    cursor.close();

                } else if (scheme.equals("file")) {
                    filePath = selectedImageUri.getPath();
                    Log.d("LOADING", "Loading file " + filePath);
                } else {
                    Log.d("LOADING", "Failed to load URI " + selectedImageUri.toString());
                }

                if (filePath != null) {
                    submitImage(filePath);
                    return;
                }
            }
        }


        // TODO: Check to see if we're looking at a view page or a site page.
        // TODO: Site page - get list of assets & their images async
        Uri assetUri = getIntent().getData();
        if (assetUri != null) {
            String lastPath = assetUri.getLastPathSegment();
            getImageUri(lastPath);
            return;
        }

        loadImage("http://chzragecomics.files.wordpress.com/2012/02/rage-comics-summertime.png");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // TODO: Hide login button or switch it to a back button once they've pushed it
            case R.id.login:
                beginLogin();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    private void getImageUri(final String assetId) {
        final Context context = this;

        // Note: this is safe b/c we can alter the UI on the callback
        new CheezburgerClient(context).getImageUrlForAsset(Long.parseLong(assetId), new CheezburgerOnResultHandler<String>() {
            @Override
            public void onSuccess(String s) {
                loadImage(s);
            }

            @Override
            public void onFailure(CheezburgerException exception) {
                Log.e("getImageUri", exception.getMessage());
                loadImage("http://chzragecomics.files.wordpress.com/2012/02/rage-comics-summertime.png");
            }
        });
    }

    private void loadImage(String imageUrl) {
        FragmentManager manager = getSupportFragmentManager();
        Fragment previous = manager.findFragmentByTag("Asset");
        FragmentTransaction ft = manager.beginTransaction();
        Fragment bookFragment = AssetFragment.newInstance(imageUrl);
        if (previous != null)
            ft.remove(previous);
        ft.add(R.id.root, bookFragment, "Asset").show(bookFragment);
        ft.commit();
    }

    private void submitImage(String imagePath) {
        FragmentManager manager = getSupportFragmentManager();
        Fragment previous = manager.findFragmentByTag("Asset");
        FragmentTransaction ft = manager.beginTransaction();
        Fragment bookFragment = ImageSubmissionFragment.newInstance(imagePath);
        if (previous != null)
            ft.remove(previous);
        ft.add(R.id.root, bookFragment, "Asset").show(bookFragment);
        ft.commit();
    }

    private void beginLogin() {
        FragmentManager manager = getSupportFragmentManager();
        Fragment previous = manager.findFragmentByTag("LoginWebView");
        FragmentTransaction ft = manager.beginTransaction();
        Fragment loginFragment = LoginWebViewFragment.newInstance(getResources().getString(R.string.api_auth_url), getResources().getString(R.string.client_id));
        if (previous != null)
            ft.remove(previous);
        ft.replace(R.id.root, loginFragment, "LoginWebView").show(loginFragment);
        ft.addToBackStack(null);
        ft.commit();
    }
}
