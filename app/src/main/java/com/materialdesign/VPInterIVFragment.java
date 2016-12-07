package com.materialdesign;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class VPInterIVFragment extends Fragment {

    private static final String KEY_POS = "pos";

    public static VPInterIVFragment newIntance(int pos) {
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_POS, pos);
        VPInterIVFragment fragment = new VPInterIVFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.vp_inter_iv, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        Bundle bundle = getArguments();
        int pos = 0;
        if (bundle != null) {
            pos = bundle.getInt(KEY_POS);
        }
        imageView.setImageResource(pos == 0 ? R.drawable.pokeball : R.drawable.pangding);
    }

}
