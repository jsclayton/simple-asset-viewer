package com.cheezburger.simple;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created with IntelliJ IDEA.
 * User: john
 * Date: 5/23/12
 * Time: 9:35 AM
 * To change this template use File | Settings | File Templates.
 */
public class ImageSubmissionFragment extends Fragment {

    public static Fragment newInstance(String imagePath) {
        Bundle b = new Bundle();
        b.putString("imagePath", imagePath);
        Fragment f = new ImageSubmissionFragment();
        f.setArguments(b);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.image_submission_fragment, container, false);

        ImageView imageView = (ImageView) root.findViewById(R.id.asset_img);
        imageView.setImageBitmap(BitmapFactory.decodeFile(getArguments().getString("imagePath")));

        return root;
    }
}
