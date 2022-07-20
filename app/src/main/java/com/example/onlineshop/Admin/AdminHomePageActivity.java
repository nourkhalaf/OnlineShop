package com.example.onlineshop.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.onlineshop.R;
import com.example.onlineshop.MainActivity;

import io.paperdb.Paper;


public class AdminHomePageActivity extends AppCompatActivity {

    private Button logOutBtn, checkOrderBtn, maintainProductsBtn, addNewProductBtn ;


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home_page);


        Paper.init(this);


        logOutBtn = (Button) findViewById(R.id.admin_logout_btn);
        checkOrderBtn = (Button) findViewById(R.id.check_orders_btn);
        maintainProductsBtn = (Button) findViewById(R.id.maintain_btn);
        addNewProductBtn= (Button) findViewById(R.id.admin_add_product_btn);


        addNewProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomePageActivity.this,AdminCategoryActivity.class);
                startActivity(intent);
                finish();
            }
        });


        maintainProductsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomePageActivity.this,AdminHomeActivity.class);
                startActivity(intent);
                finish();
            }
        });


        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Paper.book().destroy();

                Intent intent = new Intent(AdminHomePageActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        checkOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomePageActivity.this,AdminNewOrderActivity.class);
                startActivity(intent);
            }
        });




    }
}