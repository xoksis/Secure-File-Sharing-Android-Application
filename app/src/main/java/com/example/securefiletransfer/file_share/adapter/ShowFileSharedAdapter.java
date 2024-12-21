package com.example.securefiletransfer.file_share.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.securefiletransfer.R;
import com.example.securefiletransfer.file_share.model.FileShared;

import java.util.List;

public class ShowFileSharedAdapter extends RecyclerView.Adapter<ShowFileSharedAdapter.viewholder> {

    List<FileShared> fileSharedList;
    Context context;

    public ShowFileSharedAdapter(Context context, List<FileShared> fileSharedList) {
        this.fileSharedList = fileSharedList;
        this.context = context;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_file_shared, parent, false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        FileShared model = fileSharedList.get(position);
        holder.tv_fileName.setText(model.getFilename());
        holder.tv_key.setText(model.getKey());

        holder.iv_clipboard.setOnClickListener(v -> {

            ClipboardManager clipboard = (ClipboardManager) holder.itemView.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("label", holder.tv_key.getText().toString());
            clipboard.setPrimaryClip(clip);

            // Show a message to the user indicating the text was copied
            Toast.makeText(holder.itemView.getContext(), "Text copied to clipboard", Toast.LENGTH_SHORT).show();
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(model.getFileUrl()));
                context.startActivity(browserIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return fileSharedList.size();
    }

    class viewholder extends RecyclerView.ViewHolder {

        TextView tv_fileName, tv_key;
        ImageView iv_clipboard;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            tv_fileName = itemView.findViewById(R.id.tv_fileName);
            tv_key = itemView.findViewById(R.id.tv_key);
            iv_clipboard = itemView.findViewById(R.id.iv_clipboard);
        }
    }
}
