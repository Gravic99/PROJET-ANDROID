package com.example.qwikpik;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.List;

public class GalleryRecyclerView extends RecyclerView.Adapter<GalleryRecyclerView.ImageViewHolder> {
    private List<Picture> pictureDataSet;


    public GalleryRecyclerView(List<Picture> pictureDataSet) {
        this.pictureDataSet = pictureDataSet;
    }
    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_gallery, parent, false);
        ImageViewHolder todoViewHolder = new ImageViewHolder(v);
        return todoViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ImageViewHolder holder, final int position) {
       final Picture pictureToDisplay = pictureDataSet.get(position);


        holder.textViewTitle.setText(pictureToDisplay.getTitle().substring(0,pictureToDisplay.getTitle().lastIndexOf("_lat:")));
        holder.imageViewImageGallery.setImageBitmap(pictureToDisplay.getImage());
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAndShowNameDialog(pictureToDisplay,v.getContext(),position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return pictureDataSet.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder{
        TextView textViewTitle;
        ImageView imageViewImageGallery;
        View view;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            textViewTitle = itemView.findViewById(R.id.textView_titleImage);
            imageViewImageGallery = itemView.findViewById(R.id.imageView_ImageGallery);

        }
    }
    private void createAndShowNameDialog(final Picture picture, final Context context, final int position){
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.name_dialog);

        Button dialogBtnSave = dialog.findViewById(R.id.btnSave);
        Button dialogBtnCancel = dialog.findViewById(R.id.btnCancel);

        dialogBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editTextName = dialog.findViewById(R.id.editTextName);
              String newTitle =  renamePicture(editTextName.getText().toString(),picture);
                pictureDataSet.get(position).setTitle(newTitle);
                notifyItemChanged(position);


                dialog.dismiss();
            }
        });

        dialogBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private String renamePicture(String newName, Picture picture){
        newName  = newName.replaceAll("\\W", "");
        File directory = picture.getDirectory();
        File from      = new File(picture.getPicturePath());
        String Name = newName.trim() + picture.getTitle().substring(picture.getTitle().lastIndexOf("_lat:"));
        File to        = new File(directory, Name);
        from.renameTo(to);
        return Name;
    }
}