package com.example.onlineshop.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.onlineshop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class AdminMaintainProductsActivity extends AppCompatActivity {

    private EditText name, description, price;
    private Button applyChangesBtn, deleteBtn;
    private ImageView imageView;

    private String productID = "";
    private DatabaseReference productRef;




    @Override
    public void onBackPressed() {

        Intent intent = new Intent(AdminMaintainProductsActivity.this, AdminHomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_maintain_products);


        productID = getIntent().getStringExtra("pid");
        productRef = FirebaseDatabase.getInstance().getReference().child("Products").child(productID);

        applyChangesBtn = (Button) findViewById(R.id.apply_changes_btn);
        deleteBtn = (Button) findViewById(R.id.delete_product_btn);

        name = (EditText) findViewById(R.id.product_name_maintain);
        price = (EditText) findViewById(R.id.product_price_maintain);
        description = (EditText) findViewById(R.id.product_description_maintain);
        imageView = (ImageView) findViewById(R.id.product_image_maintain);


        displaySpecificProductInfo();

        applyChangesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
             applyChanges();
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                deleteThisProduct();
            }
        });
    }



    private void deleteThisProduct()
    {
        productRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                Toast.makeText(AdminMaintainProductsActivity.this,"The product is deleted successfully!!",Toast.LENGTH_SHORT).show();


                Intent intent = new Intent(AdminMaintainProductsActivity.this, AdminCategoryActivity.class);
                startActivity(intent);
            }
        });

    }


    private void applyChanges()
    {
       String pName = name.getText().toString();
       String pPrice = price.getText().toString();
       String pDescription = description.getText().toString();

       if(pName.equals(""))
       {
           Toast.makeText(this,"Write down Product Name",Toast.LENGTH_SHORT).show();
       }
       else if(pPrice.equals(""))
       {
           Toast.makeText(this,"Write down Product Price",Toast.LENGTH_SHORT).show();
       }
       else if(pDescription.equals(""))
       {
           Toast.makeText(this,"Write down Product Description",Toast.LENGTH_SHORT).show();
       }
       else
       {
           HashMap<String, Object> productMap = new HashMap<>();
           productMap.put("pid", productID);
           productMap.put("description", pDescription);
           productMap.put("price", pPrice);
           productMap.put("pname", pName);

           productRef.updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
               @Override
               public void onComplete(@NonNull Task<Void> task)
               {
                   if(task.isSuccessful())
                   {
                       Toast.makeText(AdminMaintainProductsActivity.this,"Changes applied successfully!!",Toast.LENGTH_SHORT).show();

                       Intent intent = new Intent(AdminMaintainProductsActivity.this, AdminHomeActivity.class);
                       startActivity(intent);
                   }

               }
           });
       }
     }



    private void displaySpecificProductInfo()
    {
        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if(snapshot.exists())
                {
                    String pName = snapshot.child("pname").getValue().toString();
                    String pPrice = snapshot.child("price").getValue().toString();
                    String pDescription = snapshot.child("description").getValue().toString();
                    String pImage = snapshot.child("image").getValue().toString();

                    name.setText(pName);
                    price.setText(pPrice);
                    description.setText(pDescription);

                    Picasso.get().load(pImage).into(imageView);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}