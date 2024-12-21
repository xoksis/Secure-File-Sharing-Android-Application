package com.example.securefiletransfer.file_share.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.securefiletransfer.R;
import com.example.securefiletransfer.file_share.model.FileShared;

import java.util.ArrayList;
import java.util.List;

public class ShowFileReceivedAdapter extends RecyclerView.Adapter<ShowFileReceivedAdapter.viewholder> {

    Context context;
    List<FileShared> fileSharedList = new ArrayList<>();

    public ShowFileReceivedAdapter(Context context, List<FileShared> fileSharedList) {
        this.context = context;
        this.fileSharedList = fileSharedList;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_file_received, parent, false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {

        FileShared model = fileSharedList.get(position);
        holder.tv_fileName.setText(model.getFilename());
        holder.tv_fileSender.setText("sender: " + model.getSender());

        holder.itemView.setOnClickListener(v -> {

            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(model.getFileUrl()));
            context.startActivity(browserIntent);
        });

    }

    @Override
    public int getItemCount() {
        return fileSharedList.size();
    }

    class viewholder extends RecyclerView.ViewHolder {

        TextView tv_fileName, tv_fileSender;

        public viewholder(@NonNull View itemView) {
            super(itemView);

            tv_fileName = itemView.findViewById(R.id.tv_fileName);
            tv_fileSender = itemView.findViewById(R.id.tv_fileSender);

        }
    }
}
