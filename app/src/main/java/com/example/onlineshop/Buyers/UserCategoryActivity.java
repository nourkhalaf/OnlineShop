package com.example.onlineshop.Buyers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.onlineshop.Admin.AdminAddNewProductActivity;
import com.example.onlineshop.Admin.AdminHomePageActivity;
import com.example.onlineshop.R;

public class UserCategoryActivity extends AppCompatActivity {


    private CardView shirts, tShirts, femaleDresses, jackets;
    private CardView glasses, hatsCaps, walletsBagsPurses, shoes;
    private CardView headPhonesHandFree, Laptops, watches, mobilePhones;
    private CardView trousers, cosmetics;


    @Override
    public void onBackPressed() {

        Intent intent = new Intent(UserCategoryActivity.this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_category);


        shirts = (CardView) findViewById(R.id.user_shirts);
        tShirts = (CardView) findViewById(R.id.user_sports_t_shirts);
        femaleDresses = (CardView) findViewById(R.id.user_female_dresses);
        jackets = (CardView) findViewById(R.id.user_sweathers);

        glasses = (CardView) findViewById(R.id.user_glasses);
        hatsCaps = (CardView) findViewById(R.id.user_hats_caps);
        walletsBagsPurses = (CardView) findViewById(R.id.user_purses_bags_wallets);
        shoes = (CardView) findViewById(R.id.user_shoes);

        headPhonesHandFree = (CardView) findViewById(R.id.user_headphones_handfree);
        Laptops = (CardView) findViewById(R.id.user_laptop_pc);
        watches = (CardView) findViewById(R.id.user_watches);
        mobilePhones = (CardView) findViewById(R.id.user_mobilephones);
        trousers = (CardView) findViewById(R.id.user_trousers);
        cosmetics = (CardView) findViewById(R.id.user_cosmetics);


        shirts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(UserCategoryActivity.this, ShowCategoryProductsActivity.class);
                intent.putExtra("category", "Shirts");
                startActivity(intent);
            }
        });


        tShirts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(UserCategoryActivity.this, ShowCategoryProductsActivity.class);
                intent.putExtra("category", "T-Shirts");
                startActivity(intent);
            }
        });


        femaleDresses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(UserCategoryActivity.this, ShowCategoryProductsActivity.class);
                intent.putExtra("category", "Dresses");
                startActivity(intent);
            }
        });


        jackets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(UserCategoryActivity.this, ShowCategoryProductsActivity.class);
                intent.putExtra("category", "Jackets");
                startActivity(intent);
            }
        });


        glasses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(UserCategoryActivity.this, ShowCategoryProductsActivity.class);
                intent.putExtra("category", "Glasses");
                startActivity(intent);
            }
        });


        hatsCaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(UserCategoryActivity.this, ShowCategoryProductsActivity.class);
                intent.putExtra("category", "Hats Caps");
                startActivity(intent);
            }
        });



        walletsBagsPurses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(UserCategoryActivity.this, ShowCategoryProductsActivity.class);
                intent.putExtra("category", "Bags");
                startActivity(intent);
            }
        });


        shoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(UserCategoryActivity.this, ShowCategoryProductsActivity.class);
                intent.putExtra("category", "Shoes");
                startActivity(intent);
            }
        });



        headPhonesHandFree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(UserCategoryActivity.this, ShowCategoryProductsActivity.class);
                intent.putExtra("category", "HeadPhones");
                startActivity(intent);
            }
        });


        Laptops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(UserCategoryActivity.this, ShowCategoryProductsActivity.class);
                intent.putExtra("category", "Laptops");
                startActivity(intent);
            }
        });


        watches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(UserCategoryActivity.this, ShowCategoryProductsActivity.class);
                intent.putExtra("category", "Watches");
                startActivity(intent);
            }
        });


        mobilePhones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(UserCategoryActivity.this, ShowCategoryProductsActivity.class);
                intent.putExtra("category", "Mobile Phones");
                startActivity(intent);
            }
        });

        trousers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(UserCategoryActivity.this, ShowCategoryProductsActivity.class);
                intent.putExtra("category", "Trousers");
                startActivity(intent);
            }
        });

        cosmetics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(UserCategoryActivity.this, ShowCategoryProductsActivity.class);
                intent.putExtra("category", "Cosmetics");
                startActivity(intent);
            }
        });
    }
}