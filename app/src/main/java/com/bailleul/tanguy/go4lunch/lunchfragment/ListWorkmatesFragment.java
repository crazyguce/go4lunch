package com.bailleul.tanguy.go4lunch.lunchfragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bailleul.tanguy.go4lunch.R;
import com.bailleul.tanguy.go4lunch.firestore.UserHelper;
import com.bailleul.tanguy.go4lunch.view.ListOfWorkmatesAdapter;
import com.bumptech.glide.Glide;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.auth.User;

import io.reactivex.annotations.Nullable;

/public class ListWorkmatesFragment extends Fragment {
    private ListOfWorkmatesAdapter adapter;
    private RecyclerView recyclerView;
    private String PLACEIDRESTO = "resto_place_id";

    public ListWorkmatesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view;
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_list_workmates, container, false);
        recyclerView = view.findViewById(R.id.fragment_workmates_recyclerview);

        setupRecyclerView();
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void setupRecyclerView() {

        Query allUsers= UserHelper.getAllUsers()
                .orderBy("restoTodayName", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<user> options = new FirestoreRecyclerOptions.Builder<User>()
                .setQuery(allUsers, User.class)
                .build();

        adapter = new ListOfWorkmatesAdapter(options, Glide.with(recyclerView));
        recyclerView.setHasFixedSize(true); // for performances reasons
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(adapter);



    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}