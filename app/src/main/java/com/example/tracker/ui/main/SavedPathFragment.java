package com.example.tracker.ui.main;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.tracker.R;
import com.example.tracker.SavePathEvent;
import com.example.tracker.SavedPath;
import com.example.tracker.TravelPath;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * A fragment representing a list of Items.
 */
public class SavedPathFragment extends Fragment {


    private static final String ARG_COLUMN_COUNT = "column-count";

    private int mColumnCount = 1;
    RecyclerView recyclerView;
    SavedPath savedPath = new SavedPath();
    MySavedPathRecyclerViewAdapter adapter = new MySavedPathRecyclerViewAdapter(savedPath.ITEMS, savedPath);

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SavedPathFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static SavedPathFragment newInstance(int columnCount) {
        SavedPathFragment fragment = new SavedPathFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
        SavedPath.setContext(getActivity());
        SavedPath.loadSavedPath();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /*
        getParentFragmentManager().setFragmentResultListener("save", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                TravelPath result = bundle.getParcelable("PathToSave");
                Toast.makeText(getContext(),"result.getSavedName()",Toast.LENGTH_SHORT).show();
                //SavedPath.addItem(result);
                //adapter.notifyDataSetChanged();
            }
        });
         */
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_saved_path_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            recyclerView.setAdapter(adapter);
        }
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSavePath(SavePathEvent received){
            savedPath.addItem(received.getPathToSave());
            Toast.makeText(getContext(), "Path Saved", Toast.LENGTH_SHORT).show();
            //adapter.notifyDataSetChanged();
    }
}