package com.example.boyraztalha.instagramclonedenemesi.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.boyraztalha.instagramclonedenemesi.Adapter.NotificationAdapter;
import com.example.boyraztalha.instagramclonedenemesi.Model.NotificationModel;
import com.example.boyraztalha.instagramclonedenemesi.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NotificationFragment extends Fragment {

    RecyclerView recyclerView;
    NotificationAdapter notificationAdapter;
    List<NotificationModel> notifications;

    FirebaseUser firebaseUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        notifications = new ArrayList<>();
        notificationAdapter = new NotificationAdapter(getContext(),notifications);
        recyclerView.setAdapter(notificationAdapter);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        read_notifications();

        return view;
    }

    private void read_notifications() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications")
                .child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                notifications.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    NotificationModel model = snapshot.getValue(NotificationModel.class);
                    notifications.add(model);
                }

                Collections.reverse(notifications);
                notificationAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
