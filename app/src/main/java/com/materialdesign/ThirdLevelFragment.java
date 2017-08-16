package com.materialdesign;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by cwj on 17/3/8.
 */

public class ThirdLevelFragment extends Fragment {

    private static final String INDEX_KEY = "index";

    private int index;

    public static Fragment getInstance(Context context, int index) {
        Bundle bundle = new Bundle();
        bundle.putInt(INDEX_KEY, index);
        return Fragment.instantiate(context, ThirdLevelFragment.class.getName(), bundle);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        index = getArguments().getInt(INDEX_KEY);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.third_level_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button button = (Button) view.findViewById(R.id.jumpBtn);
        button.setText("index:" + index);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .add(R.id.container, getInstance(getContext(), index + 1))
                        .addToBackStack(null)
                        .commitAllowingStateLoss();
            }
        });
    }
}
