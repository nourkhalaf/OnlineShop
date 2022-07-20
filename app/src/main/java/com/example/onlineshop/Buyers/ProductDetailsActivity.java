package com.example.onlineshop.Buyers;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.onlineshop.Model.Products;
import com.example.onlineshop.Prevalent.Prevalent;
import com.example.onlineshop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetailsActivity extends AppCompatActivity {

    private Button addToCartBtn;
    private ImageView productImage ,fill_favorite, empty_favorite;
    private ElegantNumberButton numberButton;
    private TextView productName,productDescription,productPrice;
    private String productID = "", state = "Normal";


    @Override
    public void onBackPressed() {

        Intent intent = new Intent(ProductDetailsActivity.this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        productID = getIntent().getStringExtra("pid");


        addToCartBtn = (Button) findViewById(R.id.add_product_to_cart_btn);
        numberButton = (ElegantNumberButton) findViewById(R.id.number_btn);
        productImage = (ImageView) findViewById(R.id.product_image_details);
        productName = (TextView) findViewById(R.id.product_name_details);
        productPrice = (TextView) findViewById(R.id.product_price_details);
        productDescription = (TextView) findViewById(R.id.product_description_details);

        getProductDetails(productID);

        addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(state.equals("Order Placed") || state.equals("Order Shipped"))
                {
                    Toast.makeText(ProductDetailsActivity.this,"You can purchase more products, once your orderis shipped or confirmed. ",Toast.LENGTH_LONG).show();
                }
                else
                {
                    addingToCartList();
                }

            }
        });

        fill_favorite = (ImageView) findViewById(R.id.fill_favorite_icon);
        empty_favorite = (ImageView) findViewById(R.id.empty_favorite_icon);


        fill_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fill_favorite.setVisibility(View.GONE);
                empty_favorite.setVisibility(View.VISIBLE);
                removeItemFromFavorite();
            }
        });

        empty_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fill_favorite.setVisibility(View.VISIBLE);
                empty_favorite.setVisibility(View.GONE);
                addItemToFavorite();

            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();

        checkOrderState();
    }

    private void removeItemFromFavorite() {
        // DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Products").child(productID);
       //productsRef.child("favorite").setValue("no");
        Toast.makeText(ProductDetailsActivity.this,"Removed successfully"  ,Toast.LENGTH_SHORT).show();
    }

    private void addItemToFavorite() {
       // DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Products").child(productID);
       //   productsRef.child("favorite").setValue("yes");
        Toast.makeText(ProductDetailsActivity.this,"Added successfully"  ,Toast.LENGTH_SHORT).show();


    }

    private void addingToCartList()
    {
        String saveCurrentTime, saveCurrentDate;

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");

        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("pid",productID);
        cartMap.put("pname",productName.getText().toString());
        cartMap.put("price",productPrice.getText().toString());
        cartMap.put("date",saveCurrentDate);
        cartMap.put("time",saveCurrentTime);
        cartMap.put("quantity",numberButton.getNumber());
        cartMap.put("discount","");

        cartListRef.child("User View").child(Prevalent.currentOnlineUser.getPhone())
                .child("Products").child(productID)
                .updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if(task.isSuccessful())
                        {
                            cartListRef.child("Admin View").child(Prevalent.currentOnlineUser.getPhone())
                                    .child("Products").child(productID)
                                    .updateChildren(cartMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task)
                                        {
                                            if(task.isSuccessful())
                                            {
                                                Toast.makeText(ProductDetailsActivity.this,"Added to cart list.",Toast.LENGTH_SHORT).show();

                                                Intent intent = new Intent(ProductDetailsActivity.this , HomeActivity.class);
                                                startActivity(intent);
                                            }

                                        }
                                    });
                        }
                    }
                });


    }

    private void getProductDetails(String productID)
    {        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Products");

        productsRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if(snapshot.exists())
                {
                    Products products = snapshot.getValue(Products.class);

                    productName.setText(products.getPname());
                    productDescription.setText(products.getDescription());
                    productPrice.setText(products.getPrice());

                    Picasso.get().load(products.getImage()).into(productImage);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void checkOrderState()
    {
        DatabaseReference ordersRef;
        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentOnlineUser.getPhone());

        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if(snapshot.exists())
                {
                    String shippingState = snapshot.child("state").getValue().toString();

                    if(shippingState.equals("shipped"))
                    {
                        state = "Order Shipped";
                    }
                    else if(shippingState.equals("not shipped"))
                    {
                        state = "Order Placed";
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}