package com.example.project1722.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;

import com.example.project1722.Adapter.CategoryAdapter;
import com.example.project1722.Adapter.PopularAdapter;
//import com.example.project1722.Adapter.SliderAdapter;
import com.example.project1722.Domain.CategoryDomain;
import com.example.project1722.Domain.ItemsDomain;
//import com.example.project1722.Domain.SliderItems;
import com.example.project1722.Adapter.SliderAdapter2;
import com.example.project1722.Domain.SliderItems2;
import com.example.project1722.R;
import com.example.project1722.databinding.ActivityMainBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {
    private ActivityMainBinding binding;
    private String userEmail;
    EditText editTextSearch,editTextSearch1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        userEmail = getIntent().getStringExtra("user_email");
        editTextSearch = findViewById(R.id.editTextText);
        editTextSearch1=binding.editTextText;
        initPopular1(editTextSearch1);
        initBanner();
        initCategory();
        initPopular();
        bottomNavigation();
        initPopular2();


    }

    private void bottomNavigation() {
        binding.cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, CartActivity.class);
                i.putExtra("user_email", userEmail);
                startActivity(i);
            }
        });
        binding.BtnPro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ProfileActivity.class);
                i.putExtra("user_email", userEmail);
                startActivity(i);

            }
        });

    }
    // product trending :
    private void initPopular2() {
        DatabaseReference myref = database.getReference("Items");
        binding.progressBarPopularhet.setVisibility(View.VISIBLE);
        ArrayList<ItemsDomain> items = new ArrayList<>();
        myref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        items.add(issue.getValue(ItemsDomain.class));
                    }

                    // Lấy 2 sản phẩm cuối cùng
                    ArrayList<ItemsDomain> trendingItems = new ArrayList<>();
                    if (items.size() >= 2) {
                        trendingItems.add(items.get(items.size() - 2));
                        trendingItems.add(items.get(items.size() - 1));
                    } else {
                        trendingItems.addAll(items);
                    }


                    if (!trendingItems.isEmpty()) {
                        binding.recyclerViewPopularhet.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
                        binding.recyclerViewPopularhet.setAdapter(new PopularAdapter(trendingItems));
                    }
                    binding.progressBarPopularhet.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }




    private void initPopular() {
        DatabaseReference myref=database.getReference("Items");
        binding.progressBarPopular.setVisibility(View.VISIBLE);
        ArrayList<ItemsDomain> items=new ArrayList<>();
        myref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot issue:snapshot.getChildren()){
                        items.add(issue.getValue(ItemsDomain.class));

                    }
                    if(!items.isEmpty()){
                        binding.recyclerViewPopular.setLayoutManager(new GridLayoutManager(MainActivity.this,2));
                        binding.recyclerViewPopular.setAdapter(new PopularAdapter(items));
                    }
                    binding.progressBarPopular.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void initCategory() {
        DatabaseReference myref=database.getReference("Category");
        binding.progressBarOffical.setVisibility(View.VISIBLE);
        ArrayList<CategoryDomain> items= new ArrayList<>();

        System.out.println(items);
        myref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot issue:snapshot.getChildren()){
                        items.add(issue.getValue(CategoryDomain.class));

                    }
                    if(!items.isEmpty()){
                        binding.recyclerViewOffical.setLayoutManager(new LinearLayoutManager(MainActivity.this,LinearLayoutManager.HORIZONTAL,
                                false));
                        binding.recyclerViewOffical.setAdapter(new CategoryAdapter(items));
                    }
                    binding.progressBarOffical.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

//    private void initBanner() {
//        DatabaseReference myRef=database.getReference("Banner");
//        binding.progressBarBanner.setVisibility(View.VISIBLE);
//        ArrayList<SliderItems2> items = new ArrayList<>();
//        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.exists()){
//                    for(DataSnapshot issue:snapshot.getChildren()){
//                        items.add(issue.getValue(SliderItems2.class));
//                    }
//                    banners(items);
//                    binding.progressBarBanner.setVisibility(View.GONE);
//                }
//            }
//
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }
//    private void banners(ArrayList<SliderItems> items) {
//        binding.viewpagerSlider.setAdapter(new SliderAdapter(items,binding.viewpagerSlider));
//        binding.viewpagerSlider.setClipToPadding(false);
//        binding.viewpagerSlider.setClipChildren(false);
//        binding.viewpagerSlider.setOffscreenPageLimit(3);
//        binding.viewpagerSlider.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
//
//        CompositePageTransformer compositePageTransformer=new CompositePageTransformer();
//        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
//
//        binding.viewpagerSlider.setPageTransformer(compositePageTransformer);
//
//    }
    // thử :
    private void initBanner() {
        DatabaseReference myRef = database.getReference("Banner");
        binding.progressBarBanner.setVisibility(View.VISIBLE);
        ArrayList<SliderItems2> items = new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        items.add(issue.getValue(SliderItems2.class));
                    }
                    banners(items);
                    binding.progressBarBanner.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void banners(ArrayList<SliderItems2> items) {
        binding.viewpagerSlider.setAdapter(new SliderAdapter2(items, binding.viewpagerSlider));
        binding.viewpagerSlider.setClipToPadding(false);
        binding.viewpagerSlider.setClipChildren(false);
        binding.viewpagerSlider.setOffscreenPageLimit(3);
        binding.viewpagerSlider.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));

        binding.viewpagerSlider.setPageTransformer(compositePageTransformer);
    }



    private void initPopular1(EditText editText) {
        DatabaseReference myref = database.getReference("Items");
        ArrayList<ItemsDomain> items = new ArrayList<>();

        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    items.clear();
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        items.add(issue.getValue(ItemsDomain.class));
                    }
                    updateRecyclerView(items);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        myref.addListenerForSingleValueEvent(listener);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterList(s.toString(), items);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void filterList(String query, ArrayList<ItemsDomain> items) {
        ArrayList<ItemsDomain> filteredItems = new ArrayList<>();
        for (ItemsDomain item : items) {
            if (item.getTitle().toLowerCase().contains(query.toLowerCase())) {
                filteredItems.add(item);
            }
        }
        updateRecyclerView(filteredItems);
    }

    private void updateRecyclerView(ArrayList<ItemsDomain> items) {
        if (!items.isEmpty()) {
            binding.recyclerViewPopular.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
            binding.recyclerViewPopular.setAdapter(new PopularAdapter(items));
        } else {
            binding.recyclerViewPopular.setAdapter(null);
        }
        binding.progressBarPopular.setVisibility(View.GONE);
    }



}