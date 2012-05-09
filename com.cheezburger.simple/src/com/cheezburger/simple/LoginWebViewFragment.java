package com.cheezburger.simple;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

/**
 * Created with IntelliJ IDEA.
 * User: john
 * Date: 5/9/12
 * Time: 9:12 AM
 * To change this template use File | Settings | File Templates.
 */
public class LoginWebViewFragment extends Fragment {

    public static Fragment newInstance(String authUrl, String clientId) {
        Bundle b = new Bundle();
        b.putString("authUrl", authUrl);
        b.putString("clientId", clientId);
        Fragment f = new LoginWebViewFragment();
        f.setArguments(b);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.web_view_fragment, container, false);
        final WebView www = (WebView) root.findViewById(R.id.www);
        www.requestFocus(View.FOCUS_DOWN);
        www.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("https://api.cheezburger.com/oauth/login_success")) {
                    Toast.makeText(getActivity(), "Success! " + url, Toast.LENGTH_LONG).show();
                    getFragmentManager().popBackStack();
                    return true;
                }
                return false;
            }
        });
        www.loadUrl(String.format(getArguments().getString("authUrl"), getArguments().getString("clientId")));
        return root;
    }
}
