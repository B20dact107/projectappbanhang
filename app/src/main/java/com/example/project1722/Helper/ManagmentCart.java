package com.example.project1722.Helper;

import android.content.Context;
import android.widget.Toast;


import com.example.project1722.Domain.ItemsDomain;
import com.example.project1722.Domain.SliderItems2;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ManagmentCart {
    private DatabaseReference discountRef;

    private Context context;
    private TinyDB tinyDB;

    public ManagmentCart(Context context) {
        this.context = context;
        this.tinyDB = new TinyDB(context);
        discountRef = FirebaseDatabase.getInstance().getReference("Banner");
    }


    public void insertFood(ItemsDomain item) {
        ArrayList<ItemsDomain> listfood = getListCart();
        boolean existAlready = false;
        int n = 0;
        for (int y = 0; y < listfood.size(); y++) {
            if (listfood.get(y).getTitle().equals(item.getTitle())) {
                existAlready = true;
                n = y;
                break;
            }
        }
        if (existAlready) {
            listfood.get(n).setNumberinCart(item.getNumberinCart());
        } else {
            listfood.add(item);
        }
        tinyDB.putListObject("CartList", listfood);
        Toast.makeText(context, "Added to your Cart", Toast.LENGTH_SHORT).show();
    }

    public ArrayList<ItemsDomain> getListCart() {
        return tinyDB.getListObject("CartList");
    }

    public void minusItem(ArrayList<ItemsDomain> listfood, int position, ChangeNumberItemsListener changeNumberItemsListener) {
        if (listfood.get(position).getNumberinCart() == 1) {
            listfood.remove(position);
        } else {
            listfood.get(position).setNumberinCart(listfood.get(position).getNumberinCart() - 1);
        }
        tinyDB.putListObject("CartList", listfood);
        changeNumberItemsListener.changed();
    }

    public void plusItem(ArrayList<ItemsDomain> listfood, int position, ChangeNumberItemsListener changeNumberItemsListener) {
        listfood.get(position).setNumberinCart(listfood.get(position).getNumberinCart() + 1);
        tinyDB.putListObject("CartList", listfood);
        changeNumberItemsListener.changed();
    }

    public Double getTotalFee() {
        ArrayList<ItemsDomain> listfood2 = getListCart();
        double fee = 0;
        for (int i = 0; i < listfood2.size(); i++) {
            fee = fee + (listfood2.get(i).getPrice() * listfood2.get(i).getNumberinCart());
        }
        return fee;
    }
    public void clearCart() {
        tinyDB.putListObject("CartList", new ArrayList<>());
    }
//    public double calculateDiscount(String discountCode) {
//        double totalDiscount = 0;
//
//        // Truy vấn cơ sở dữ liệu Firebase để lấy danh sách mã giảm giá hiện có
//
//
//        ArrayList<String> availableDiscountCodes = new ArrayList<>();
//        availableDiscountCodes.add("DISCOUNT50");
//        availableDiscountCodes.add("DISCOUNT30");
//
//
//        if (availableDiscountCodes.contains(discountCode)) {
//            if (discountCode.equals("DISCOUNT50")) {
//                totalDiscount = getTotalFee() * 0.5; // Giảm giá 50%
//            } else if (discountCode.equals("DISCOUNT30")) {
//                totalDiscount = getTotalFee() * 0.3; // Giảm giá 30%
//            }
//        }
//
//        return totalDiscount;
//    }
    public void calculateDiscount(String discountCode, OnDiscountCalculatedListener listener) {
        discountRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                double totalDiscount = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    SliderItems2 discountItem = snapshot.getValue(SliderItems2.class);
                    if (discountItem != null && discountCode.equals(discountItem.getDiscountCode())) {
                        if (discountCode.equals("DISCOUNT50")) {
                            totalDiscount = getTotalFee() * 0.5; // Giảm giá 50%
                        } else if (discountCode.equals("DISCOUNT30")) {
                            totalDiscount = getTotalFee() * 0.3; // Giảm giá 30%
                        }
                        break;
                    }
                }
                listener.onDiscountCalculated(totalDiscount);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onDiscountCalculated(0);
            }
        });
    }

    public interface OnDiscountCalculatedListener {
        void onDiscountCalculated(double totalDiscount);
    }
}




