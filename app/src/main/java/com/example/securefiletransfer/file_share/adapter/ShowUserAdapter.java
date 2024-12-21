package com.example.securefiletransfer.file_share.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.securefiletransfer.R;
import com.example.securefiletransfer.chat.activities.ChatActivity;
import com.example.securefiletransfer.file_share.activities.ShareFile;
import com.example.securefiletransfer.file_share.activities.UserRegister;
import com.example.securefiletransfer.file_share.model.UserModel;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/*WE DO NOT NEED THIS.*/
public class ShowUserAdapter extends RecyclerView.Adapter<ShowUserAdapter.viewholder> {

    List<UserModel> userModelList = new ArrayList<>();
    Context context;
    String sendUser = "";

    public ShowUserAdapter(Context context, List<UserModel> userModelList, String sendUser) {
        this.userModelList = userModelList;
        this.context = context;
        this.sendUser = sendUser;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_users, parent, false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {

        UserModel model = userModelList.get(position);

        holder.tv_username.setText(model.getUsername());

        if (model.getImage().equals(UserRegister.DEFAULT_IMAGE)) {
            holder.cv_profileImage.setImageResource(R.drawable.avatar);
        } else {
            Glide.with(context).load(model.getImage()).into(holder.cv_profileImage);
        }

        holder.btn_shareFile.setOnClickListener(v -> {
            Intent intent = new Intent(context, ShareFile.class);
            intent.putExtra("username", model.getUsername());
            intent.putExtra("id", model.getId());
            intent.putExtra("email", model.getEmail());
            intent.putExtra("image", model.getImage());
            intent.putExtra("sender", sendUser);
            intent.putExtra("phone", model.getPhone());
            context.startActivity(intent);
        });

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ChatActivity.class);
            intent.putExtra("name", model.getUsername());
            intent.putExtra("uid", model.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return userModelList.size();
    }

    class viewholder extends RecyclerView.ViewHolder {

        Button btn_shareFile;
        TextView tv_username;
        CircleImageView cv_profileImage;

        public viewholder(@NonNull View itemView) {
            super(itemView);

            btn_shareFile = itemView.findViewById(R.id.btn_shareFile);
            tv_username = itemView.findViewById(R.id.tv_username);
            cv_profileImage = itemView.findViewById(R.id.cv_profileImage);

        }
    }
}
