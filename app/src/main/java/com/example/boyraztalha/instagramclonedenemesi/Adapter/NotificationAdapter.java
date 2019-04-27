package com.example.boyraztalha.instagramclonedenemesi.Adapter;

import android.app.Notification;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.boyraztalha.instagramclonedenemesi.Fragment.PostDetailFragment;
import com.example.boyraztalha.instagramclonedenemesi.Fragment.ProfileFragment;
import com.example.boyraztalha.instagramclonedenemesi.Model.NotificationModel;
import com.example.boyraztalha.instagramclonedenemesi.Model.Post;
import com.example.boyraztalha.instagramclonedenemesi.Model.User;
import com.example.boyraztalha.instagramclonedenemesi.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.Viewholder>{

    Context context;
    List<NotificationModel> notifications;

    public NotificationAdapter(Context context, List<NotificationModel> notifications) {
        this.context = context;
        this.notifications = notifications;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.notification_item,viewGroup,false);
        return new NotificationAdapter.Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder viewholder, int i) {

        final NotificationModel notification = notifications.get(i);

        viewholder.comment.setText(notification.getText());

        get_user_info(notification.getUserid(), viewholder.image_profile, viewholder.username);

        if (notification.isIspost()){
            viewholder.image_post.setVisibility(View.VISIBLE);
            get_image_post(notification.getPostd(), viewholder.image_post);
        }else{
            viewholder.image_post.setVisibility(View.GONE);
        }

        viewholder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (notification.isIspost()){
                    SharedPreferences.Editor editor = context.getSharedPreferences("PREFS",Context.MODE_PRIVATE).edit();
                    editor.putString("postid",notification.getPostd());
                    editor.apply();

                    ((FragmentActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new PostDetailFragment()).commit();
                }else {
                    SharedPreferences.Editor editor = context.getSharedPreferences("PREFS",Context.MODE_PRIVATE).edit();
                    editor.putString("profileid",notification.getPostd());
                    editor.apply();

                    ((FragmentActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new ProfileFragment()).commit();
                }
            }
        });

    }

    private void get_image_post(final String postd, final ImageView image_post) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts").child(postd);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Post post = dataSnapshot.getValue(Post.class);

                Glide.with(context).load(post.getPostimage()).into(image_post);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void get_user_info(String userid, final ImageView image_profile, final TextView username) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                Glide.with(context).load(user.getImageUrl()).into(image_profile);
                username.setText(user.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder{

        ImageView image_post, image_profile;
        TextView comment,username;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            image_post = itemView.findViewById(R.id.image_post);
            image_profile = itemView.findViewById(R.id.image_profile);
            comment = itemView.findViewById(R.id.comment);
            username = itemView.findViewById(R.id.username);
        }
    }
}
