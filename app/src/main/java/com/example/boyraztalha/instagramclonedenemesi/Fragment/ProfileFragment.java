package com.example.boyraztalha.instagramclonedenemesi.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.boyraztalha.instagramclonedenemesi.Model.Post;
import com.example.boyraztalha.instagramclonedenemesi.Model.User;
import com.example.boyraztalha.instagramclonedenemesi.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ProfileFragment extends Fragment {

    ImageView image_profile,options;
    TextView posts,followers,following,fullname,bio,username;
    Button edit_profile;

    FirebaseUser firebaseUser;
    String profileid;

    ImageButton my_fotos,saved_fotos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        image_profile = view.findViewById(R.id.image_profile);
        options = view.findViewById(R.id.options);
        followers = view.findViewById(R.id.followers);
        posts = view.findViewById(R.id.posts);
        following = view.findViewById(R.id.following);
        fullname = view.findViewById(R.id.fullname);
        bio = view.findViewById(R.id.bio);
        username = view.findViewById(R.id.username);
        edit_profile = view.findViewById(R.id.edit_profile);
        my_fotos = view.findViewById(R.id.my_fotos);
        saved_fotos = view.findViewById(R.id.saved_fotos);

        SharedPreferences preferences = getContext().getSharedPreferences("PREFS",Context.MODE_PRIVATE);
        profileid = preferences.getString("profileid","none");

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        get_user_info();
        get_number_of_posts();
        get_follow_infos();

        if (firebaseUser.getUid().equals(profileid)){
            edit_profile.setText("Edit profile");
        }else {
            check_following();
            saved_fotos.setVisibility(View.GONE);
        }

        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String btn_current = edit_profile.getText().toString();

                if (btn_current.equals("follow")){
                    FirebaseDatabase.getInstance().getReference("Follow").child(firebaseUser.getUid())
                            .child("following").child(profileid).setValue(true);
                    FirebaseDatabase.getInstance().getReference("Follow").child(profileid)
                            .child("followers").child(firebaseUser.getUid()).setValue(true);

                }else if(btn_current.equals("following")){
                    FirebaseDatabase.getInstance().getReference("Follow").child(firebaseUser.getUid())
                            .child("following").child(profileid).removeValue();
                    FirebaseDatabase.getInstance().getReference("Follow").child(profileid)
                            .child("followers").child(firebaseUser.getUid()).removeValue();

                }else if(btn_current.equals("Edit profile")){
                    //TODO Edit ekranini hazirlayip oraya salacagiz.
                }
            }
        });

        return view;
    }

    private void check_following(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Follow")
                .child(firebaseUser.getUid()).child("following");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(profileid).exists()){
                    edit_profile.setText("following");
                }else {
                    edit_profile.setText("follow");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void get_follow_infos(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Follow").child(profileid)
                .child("following");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                following.setText(""+dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference reference_follower = FirebaseDatabase.getInstance().getReference("Follow")
                .child(profileid).child("followers");

        reference_follower.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                followers.setText(""+dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void get_number_of_posts(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Post post = snapshot.getValue(Post.class);
                    int total_post = 0;
                    if (post.getPublisher().equals(profileid)){
                        total_post++;
                    }
                    posts.setText(""+total_post);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void get_user_info(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(profileid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (getContext() == null){
                    return;
                }

                User user = dataSnapshot.getValue(User.class);

                Glide.with(getContext()).load(user.getImageUrl()).into(image_profile);
                username.setText(user.getUsername());
                fullname.setText(user.getFullname());
                bio.setText(user.getBio());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
