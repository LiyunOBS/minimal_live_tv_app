package com.mini.livetvapp;

import static android.content.Intent.getIntent;

import android.content.Context;
import android.content.Intent;
import android.media.tv.TvInputInfo;
import android.media.tv.TvInputManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.media.tv.TvInputManager;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.mini.livetvapp.databinding.FragmentFirstBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;

//    private Context mContext;
    private TvInputManager mTvInputManager;

    private List<TvInputInfo> mInputs = new ArrayList<>();;
//    private final Map<String, Integer> mInputStateMap = new HashMap<>();

//    private final TvInputManager delegate;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        Log.d("test for check point", "just after onCreateView");

        mTvInputManager = (TvInputManager) getContext().getSystemService(Context.TV_INPUT_SERVICE);
        Log.d("test for check point", "just after creating mTvInputManager");

        initInputList();
        Log.d("test for check point", "just after initInputList");

        for (TvInputInfo i : mInputs) {
            Log.d("test for TvInputInfos", i.toString());
        }

        binding = FragmentFirstBinding.inflate(inflater, container, false);

        return binding.getRoot();

    }

    private void initInputList() {

        mInputs.clear();
        Log.d("test in initInputMaps", String.valueOf(mTvInputManager.getTvInputList().size()));
        for (TvInputInfo input : mTvInputManager.getTvInputList()) {

            String inputId = input.getId();
            Log.d("test for inputId", inputId);

            int state = mTvInputManager.getInputState(inputId);
            Log.d("test for state", String.valueOf(state));
            Log.d("test for isPassThroughInput", Boolean.toString(input.isPassthroughInput()));
            Log.d("test for getServiceInfo", input.getServiceInfo().toString());
//            Log.d("test for parentId", input.getParentId());
            Log.d("test for type", String.valueOf(input.getType()));
            Log.d("test for tunerCount", String.valueOf(input.getTunerCount()));

            mInputs.add(input);
        }
    }

    publ
    ic void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                NavHostFragment.findNavController(FirstFragment.this)
//                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
//            }
//        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}