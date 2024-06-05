package com.example.project1722.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.project1722.Adapter.CartAdapter;
import com.example.project1722.Domain.Invoice;
import com.example.project1722.Domain.ItemsDomain;
import com.example.project1722.Helper.ChangeNumberItemsListener;
import com.example.project1722.Helper.ManagmentCart;
import com.example.project1722.R;
import com.example.project1722.databinding.ActivityCartBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CartActivity extends BaseActivity {
    ActivityCartBinding binding;
    private double tax;
    private ManagmentCart managmentCart;
    DatabaseReference databaseReference;
    private boolean isLoginActivityStarted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        managmentCart=new ManagmentCart(this);
        databaseReference = FirebaseDatabase.getInstance().getReference("Invoices");
        calculatorCart();
        setVariable();
        initCartList();
    }

    private void initCartList() {
        if(managmentCart.getListCart().isEmpty()){
            binding.emptyTxt.setVisibility(View.VISIBLE);
            binding.scrollViewCart.setVisibility(View.GONE);
        }else{
            binding.emptyTxt.setVisibility(View.GONE);
            binding.scrollViewCart.setVisibility(View.VISIBLE);
        }
        binding.cartView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        binding.cartView.setAdapter(new CartAdapter(managmentCart.getListCart(), this, new ChangeNumberItemsListener() {
            @Override
            public void changed() {
                calculatorCart();
            }
        }));
    }

    private void setVariable() {
        binding.backBtn.setOnClickListener(v -> finish());
        binding.checkOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String customerEmail = getIntent().getStringExtra("user_email");
                if (TextUtils.isEmpty(customerEmail)) {
                   if(!isLoginActivityStarted) {
                        isLoginActivityStarted = true;
                       showAlertDialog("Thông báo", "Bạn cần đăng nhập trước khi check out", () -> {
                           startActivity(new Intent(CartActivity.this, LoginActivity.class));
                       });
                    }
                } else {

                    createAndSendInvoice();
                }
            }
        });
        binding.button.setOnClickListener(v -> {
            applyDiscount();
        });
    }

//    private void applyDiscount() {
//        String discountCode = binding.editTextText2.getText().toString().trim();
//        if (!TextUtils.isEmpty(discountCode)) {
//            double totalDiscount = managmentCart.calculateDiscount(discountCode);
//            if (totalDiscount > 0) {
//                calculatorCart();
//            } else {
//                Toast.makeText(CartActivity.this, "Mã giảm giá không hợp lệ", Toast.LENGTH_SHORT).show();
//            }
//        } else {
//            Toast.makeText(CartActivity.this, "Vui lòng nhập mã giảm giá", Toast.LENGTH_SHORT).show();
//        }
//    }
//    //test:
private void applyDiscount() {
    String discountCode = binding.editTextText2.getText().toString().trim();
    if (!TextUtils.isEmpty(discountCode)) {
        managmentCart.calculateDiscount(discountCode, new ManagmentCart.OnDiscountCalculatedListener() {
            @Override
            public void onDiscountCalculated(double totalDiscount) {
                if (totalDiscount > 0) {
                    calculatorCart();
                } else {
                    Toast.makeText(CartActivity.this, "Mã giảm giá không hợp lệ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    } else {
        Toast.makeText(CartActivity.this, "Vui lòng nhập mã giảm giá", Toast.LENGTH_SHORT).show();
    }
}


//    private void createAndSendInvoice() {
//        String customerEmail = getIntent().getStringExtra("user_email");
//        ArrayList<ItemsDomain> items = new ManagmentCart(this).getListCart();
//        double subtotal = new ManagmentCart(this).getTotalFee();
//        double tax = 0.02 * subtotal; //  tính thuế
//        double delivery = 10; // phí vận chuyển
//        double total = subtotal + tax + delivery;
//
//
//        String invoiceId = databaseReference.push().getKey();
//        Invoice invoice = new Invoice(invoiceId, customerEmail, items, subtotal, tax, delivery, total);
//        databaseReference.child(invoiceId).setValue(invoice)
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        showAlertDialog("Thông báo", "Bạn đã đặt hàng thành công!", () -> {
//                            managmentCart.clearCart();
//                            initCartList();
//                            calculatorCart();
//                        });
//                    } else {
//                        showAlertDialog("Thông báo", "Bạn đã đặt hàng không thành công!", null);
//                    }
//                });
//    }
//private void createAndSendInvoice() {
//    String customerEmail = getIntent().getStringExtra("user_email");
//    ArrayList<ItemsDomain> items = new ManagmentCart(this).getListCart();
//    double subtotal = new ManagmentCart(this).getTotalFee();
//    double tax = this.tax; // Sử dụng giá trị đã tính ở hàm calculatorCart()
//    double delivery = 10; // phí vận chuyển
//    // Nhập mã giảm giá từ người dùng
//    String discountCode = binding.editTextText2.getText().toString().trim();
//    double totalDiscount = managmentCart.calculateDiscount(discountCode);
//
//
//    // Tính tổng tiền trước khi áp dụng giảm giá
//    double totalBeforeDiscount = subtotal + tax + delivery;
//    double totalAfterDiscount = totalBeforeDiscount - totalDiscount;
//
//    // Tính giá trị cần ghi vào hóa đơn (chot)
//    double chot = (totalDiscount > 0) ? totalAfterDiscount : totalBeforeDiscount;
//
//    // Set giá trị cho TextView totalTxt
//    binding.totalTxt.setText("$" + chot);
//
//
//    String invoiceId = databaseReference.push().getKey();
//    Invoice invoice = new Invoice(invoiceId, customerEmail, items, subtotal, tax, delivery, chot);
//    databaseReference.child(invoiceId).setValue(invoice)
//            .addOnCompleteListener(task -> {
//                if (task.isSuccessful()) {
//                    showAlertDialog("Thông báo", "Bạn đã đặt hàng thành công!", () -> {
//                        managmentCart.clearCart();
//                        initCartList();
//                        calculatorCart();
//                    });
//                } else {
//                    showAlertDialog("Thông báo", "Bạn đã đặt hàng không thành công!", null);
//                }
//            });
//}
//test:
private void createAndSendInvoice() {
    String customerEmail = getIntent().getStringExtra("user_email");
    ArrayList<ItemsDomain> items = new ManagmentCart(this).getListCart();
    double subtotal = new ManagmentCart(this).getTotalFee();
    double tax = this.tax; // Sử dụng giá trị đã tính ở hàm calculatorCart()
    double delivery = 10; // phí vận chuyển

    String discountCode = binding.editTextText2.getText().toString().trim();

    managmentCart.calculateDiscount(discountCode, new ManagmentCart.OnDiscountCalculatedListener() {
        @Override
        public void onDiscountCalculated(double totalDiscount) {
            double totalBeforeDiscount = subtotal + tax + delivery;
            double totalAfterDiscount = totalBeforeDiscount - totalDiscount;
            double chot = (totalDiscount > 0) ? totalAfterDiscount : totalBeforeDiscount;

            binding.totalTxt.setText("$" + chot);

            String invoiceId = databaseReference.push().getKey();
            Invoice invoice = new Invoice(invoiceId, customerEmail, items, subtotal, tax, delivery, chot);
            databaseReference.child(invoiceId).setValue(invoice)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            showAlertDialog("Thông báo", "Bạn đã đặt hàng thành công!", () -> {
                                managmentCart.clearCart();
                                initCartList();
                                calculatorCart();
                            });
                        } else {
                            showAlertDialog("Thông báo", "Bạn đã đặt hàng không thành công!", null);
                        }
                    });
        }
    });
}



    //        private void calculatorCart() {
//        double pecentTax= 0.02;
//        double delivery=10;
//        tax= Math.round((managmentCart.getTotalFee()*pecentTax*100.0))/100.0;
//
//        double total=Math.round((managmentCart.getTotalFee()+tax+delivery)*100.0)/100.0;
//        double itemTotal=Math.round(managmentCart.getTotalFee()*100)/100;
//        binding.totalFeeTxt.setText("$"+itemTotal);
//        binding.taxTxt.setText("$"+tax);
//        binding.deliveryTxt.setText("$"+delivery);
//        binding.totalTxt.setText("$"+total);
//    }
//    private void calculatorCart() {
//        double percentTax = 0.02;
//        double delivery = 10;
//        tax = Math.round((managmentCart.getTotalFee() * percentTax * 100.0)) / 100.0;
//
//        String discountCode = binding.editTextText2.getText().toString().trim();
//
//        // Tính tổng giảm giá dựa trên mã giảm giá được nhập
//        double totalDiscount = managmentCart.calculateDiscount(discountCode);
//
//        // Tính tổng tiền trước khi áp dụng giảm giá
//        double totalBeforeDiscount = managmentCart.getTotalFee() + tax + delivery;
//
//        // Tính tổng tiền sau khi áp dụng giảm giá
//        double totalAfterDiscount = totalBeforeDiscount - totalDiscount;
//
//        // Cập nhật tổng tiền và các thông số khác trên giao diện
//        double itemTotal = Math.round(managmentCart.getTotalFee() * 100) / 100;
//        binding.totalFeeTxt.setText("$" + itemTotal);
//        binding.taxTxt.setText("$" + tax);
//        binding.deliveryTxt.setText("$" + delivery);
//        binding.totalTxt.setText("$" + totalBeforeDiscount);
//
//        // Hiển thị total sau khi giảm giá (nếu có)
//        if (totalDiscount > 0) {
//            binding.textViewTotalAfterDiscount.setVisibility(View.VISIBLE);
//            binding.totalAfterDiscountTxt.setVisibility(View.VISIBLE);
//            binding.totalAfterDiscountTxt.setText("$" + Math.round(totalAfterDiscount * 100.0) / 100.0);
//        } else {
//            binding.textViewTotalAfterDiscount.setVisibility(View.GONE);
//            binding.totalAfterDiscountTxt.setVisibility(View.GONE);
//        }
//    }
    private void calculatorCart() {
        double percentTax = 0.02;
        double delivery = 10;
        tax = Math.round((managmentCart.getTotalFee() * percentTax * 100.0)) / 100.0;

        String discountCode = binding.editTextText2.getText().toString().trim();

        managmentCart.calculateDiscount(discountCode, new ManagmentCart.OnDiscountCalculatedListener() {
            @Override
            public void onDiscountCalculated(double totalDiscount) {
                double totalBeforeDiscount = managmentCart.getTotalFee() + tax + delivery;
                double totalAfterDiscount = totalBeforeDiscount - totalDiscount;

                double itemTotal = Math.round(managmentCart.getTotalFee() * 100) / 100;
                binding.totalFeeTxt.setText("$" + itemTotal);
                binding.taxTxt.setText("$" + tax);
                binding.deliveryTxt.setText("$" + delivery);
                binding.totalTxt.setText("$" + totalBeforeDiscount);

                if (totalDiscount > 0) {
                    binding.textViewTotalAfterDiscount.setVisibility(View.VISIBLE);
                    binding.totalAfterDiscountTxt.setVisibility(View.VISIBLE);
                    binding.totalAfterDiscountTxt.setText("$" + Math.round(totalAfterDiscount * 100.0) / 100.0);
                } else {
                    binding.textViewTotalAfterDiscount.setVisibility(View.GONE);
                    binding.totalAfterDiscountTxt.setVisibility(View.GONE);
                }
            }
        });
    }



    private void showAlertDialog(String title, String message, Runnable onPositiveButtonClick) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> {
                    dialog.dismiss();
                    if (onPositiveButtonClick != null) {
                        onPositiveButtonClick.run();
                    }
                })
                .show();
    }
}