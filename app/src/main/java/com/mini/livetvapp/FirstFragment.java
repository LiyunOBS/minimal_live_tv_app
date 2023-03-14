package com.mini.livetvapp;

import android.content.Context;
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
import java.util.List;
import java.util.Map;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;

    private Context mContext;

    private List<TvInputInfo> mInputs;

    private final TvInputManager delegate;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        mContext = Context.getApplicationContext();

        initInputMaps();

        List<TvInputInfo> oldInputs = mInputs;
        mInputs = getTvInputInfos(true, true);

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    private List<TvInputInfo> getTvInputInfos(boolean availableOnly, boolean tunerOnly) {
        ArrayList<TvInputInfo> list = new ArrayList<>();
        for (Map.Entry<String, Integer> pair : mInputStateMap.entrySet()) {
            if (availableOnly && pair.getValue() == TvInputManager.INPUT_STATE_DISCONNECTED) {
                continue;
            }
            TvInputInfo input = getTvInputInfo(pair.getKey());
            if (input == null || isInputBlocked(input)) {
                continue;
            }
            if (tunerOnly && input.getType() != TvInputInfo.TYPE_TUNER) {
                continue;
            }
            list.add(input);
        }
        Collections.sort(list, mTvInputInfoComparator);
        return list;
    }

    static TvInputInfo getTvInputInfo(String inputId) {
        return getTvInputInfo(inputId);
    }



    private void initInputMaps() {
        mInputMap.clear();
        mTvInputLabels.clear();
        mTvInputCustomLabels.clear();
        mTvInputApplicationLabels.clear();
        mTvInputApplicationIcons.clear();
        mTvInputApplicationBanners.clear();
        mInputStateMap.clear();
        mInputIdToPartnerInputMap.clear();
        for (TvInputInfo input : mTvInputManager.getTvInputList()) {
            if (DEBUG) {
                Log.d(TAG, "Input detected " + input);
            }
            String inputId = input.getId();
            if (isInputBlocked(input)) {
                continue;
            }
            mInputMap.put(inputId, new TvInputInfoCompat(mContext, input));
            int state = mTvInputManager.getInputState(inputId);
            mInputStateMap.put(inputId, state);
            mInputIdToPartnerInputMap.put(inputId, isPartnerInput(input));
        }
        SoftPreconditions.checkState(
                mInputStateMap.size() == mInputMap.size(),
                TAG,
                "mInputStateMap not the same size as mInputMap");
    }


    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
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