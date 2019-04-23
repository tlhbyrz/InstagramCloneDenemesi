package com.example.boyraztalha.instagramclonedenemesi.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.boyraztalha.instagramclonedenemesi.Fragment.PostDetailFragment;
import com.example.boyraztalha.instagramclonedenemesi.Model.Post;
import com.example.boyraztalha.instagramclonedenemesi.R;

import java.util.List;

public class MyfotosAdapter extends RecyclerView.Adapter<MyfotosAdapter.ViewHolder>{

    Context context;
    List<Post> posts;

    public MyfotosAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.myfotos_item,viewGroup,false);
        return new MyfotosAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        final Post post = posts.get(i);

        Glide.with(context).load(post.getPostimage()).into(viewHolder.my_fotos);

        viewHolder.my_fotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = context.getSharedPreferences("PREFS",Context.MODE_PRIVATE).edit();
                editor.putString("postid",post.getPublisher());
                editor.apply();

                ((FragmentActivity)context).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container,new PostDetailFragment()).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView my_fotos;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            my_fotos = itemView.findViewById(R.id.my_fotos);
        }
    }

}
