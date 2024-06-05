package com.example.project1722.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;

import com.example.project1722.Domain.SliderItems2;
import com.example.project1722.R;

import java.util.ArrayList;



import android.content.ClipData;
import android.content.ClipboardManager;
;
import android.widget.Toast;


import androidx.appcompat.app.AlertDialog;




public class SliderAdapter2 extends RecyclerView.Adapter<SliderAdapter2.SliderViewholder> {
    private ArrayList<SliderItems2> sliderItems;
    private ViewPager2 viewPager2;
    private Context context;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            sliderItems.addAll(sliderItems);
            notifyDataSetChanged();
        }
    };

    public SliderAdapter2(ArrayList<SliderItems2> sliderItems, ViewPager2 viewPager2) {
        this.sliderItems = sliderItems;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public SliderAdapter2.SliderViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new SliderViewholder(LayoutInflater.from(context).inflate(R.layout.slider_item_container, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SliderAdapter2.SliderViewholder holder, int position) {
        holder.setImage(sliderItems.get(position));
        holder.itemView.setOnClickListener(v -> {
            String discountCode = sliderItems.get(position).getDiscountCode();
            showAlertDialogWithCopy(context, discountCode);
        });
        if (position == sliderItems.size() - 2) {
            viewPager2.post(runnable);
        }
    }

    @Override
    public int getItemCount() {
        return sliderItems.size();
    }

    public class SliderViewholder extends RecyclerView.ViewHolder {
        private ImageView imageView;

        public SliderViewholder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageSlider);
        }

        void setImage(SliderItems2 sliderItems) {
            RequestOptions requestOptions = new RequestOptions();
            requestOptions = requestOptions.transform(new CenterCrop());
            Glide.with(context)
                    .load(sliderItems.getUrl())
                    .apply(requestOptions)
                    .into(imageView);
        }
    }

    private void showAlertDialogWithCopy(Context context, String discountCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Discount Code");
        builder.setMessage("Use code: " + discountCode);
        builder.setPositiveButton("Copy", (dialog, which) -> {
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("discountCode", discountCode);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.show();
    }
}

