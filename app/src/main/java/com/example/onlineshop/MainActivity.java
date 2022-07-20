package com.example.onlineshop;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.example.onlineshop.Admin.AdminHomePageActivity;
import com.example.onlineshop.Buyers.HomeActivity;
import com.example.onlineshop.Buyers.LoginActivity;
import com.example.onlineshop.Model.Users;
import com.example.onlineshop.Prevalent.AdminPrevalent;
import com.example.onlineshop.Prevalent.Prevalent;
import com.firebase.ui.auth.AuthMethodPickerLayout;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.Arrays;
import java.util.List;

import io.paperdb.Paper;


public class MainActivity extends AppCompatActivity {

    private final static int LOGIN_REQUEST_CODE = 7171;
    private List<AuthUI.IdpConfig> providers;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener listener;

    private Button userLoginButton, adminLoginButton;




    FirebaseDatabase database;
    DatabaseReference userRef;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        Paper.init(this);

        adminLoginButton = (Button) findViewById(R.id.admin_login);
        userLoginButton = (Button) findViewById(R.id.main_login_btn);


        database = FirebaseDatabase.getInstance();
        userRef = database.getReference("Users");


        providers = Arrays.asList(
                new AuthUI.IdpConfig.PhoneBuilder().build());

        firebaseAuth = FirebaseAuth.getInstance();



        adminLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });


        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user!=null)
        {

            String UserPhoneKey = Paper.book().read(Prevalent.UserPhoneKey);
            String UserNameKey = Paper.book().read(Prevalent.UserNameKey);

            if (UserPhoneKey != "" && UserNameKey != "")
            {
                if (!TextUtils.isEmpty(UserPhoneKey)  &&  !TextUtils.isEmpty(UserNameKey))
                {
                    Users model = new Users();
                    model.setName(UserNameKey);
                    model.setPhone(UserPhoneKey);
                    Prevalent.currentOnlineUser = model;
                }
            }
              Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }

        else if(Paper.book().read(AdminPrevalent.AdminPhoneKey)!= null &&
        Paper.book().read(AdminPrevalent.AdminPasswordKey) != null)
        {
            Intent intent = new Intent(MainActivity.this, AdminHomePageActivity.class);
            startActivity(intent);
            finish();
        }
        else {
                    userLoginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AuthMethodPickerLayout authMethodPickerLayout = new AuthMethodPickerLayout
                            .Builder(R.layout.activity_main)
                            .setPhoneButtonId(R.id.main_login_btn)
                            .build();

                    startActivityForResult(AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAuthMethodPickerLayout(authMethodPickerLayout)
                            .setIsSmartLockEnabled(false)
                            .setAvailableProviders(providers)
                            .build(), LOGIN_REQUEST_CODE);
                }
            });
        }

    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == LOGIN_REQUEST_CODE)
        {
            if (data!=null) {
                IdpResponse response = IdpResponse.fromResultIntent(data);
                if (resultCode == RESULT_OK) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    showRegisterLayout();
                } else {
                    Toast.makeText(this, "[ERROR:]" + response.getError().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }
        }
    }


    private void showRegisterLayout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View itemView = LayoutInflater.from(this).inflate(R.layout.activity_register,null);

        final EditText edt_first_name = (EditText)itemView.findViewById(R.id.register_username_input);
         final EditText edt_phone = (EditText)itemView.findViewById(R.id.register_phone_number_input);

        Button btn_continue = (Button)itemView.findViewById(R.id.register_btn);

        //set data
        if(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber() !=null &&
                !TextUtils.isEmpty(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()))
            edt_phone.setText(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());


        //set view
        builder.setView(itemView);
        final AlertDialog dialog = builder.create();
        dialog.show();

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(edt_first_name.getText().toString()))
                {
                    Toast.makeText(MainActivity.this,"Please enter your name",Toast.LENGTH_SHORT).show();
                    return;
                }else  if(TextUtils.isEmpty(edt_phone.getText().toString()))
                {
                    Toast.makeText(MainActivity.this,"Please enter your phone",Toast.LENGTH_SHORT).show();
                    return;
                }
                else
                {
                    final Users model = new Users();
                    model.setName(edt_first_name.getText().toString());
                    model.setPhone(edt_phone.getText().toString());

                    Prevalent.currentOnlineUser = model;
                    Paper.book().write(Prevalent.UserPhoneKey, edt_phone.getText().toString());
                    Paper.book().write(Prevalent.UserNameKey, edt_first_name.getText().toString());


                    userRef.child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber())
                            .setValue(model)
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    dialog.dismiss();
                                    Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            }).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(MainActivity.this,"register successfully!", Toast.LENGTH_SHORT)
                                    .show();
                            dialog.dismiss();

                            startActivity(new Intent(MainActivity.this, MapNewActivity.class));
                            finish();
                        }
                    });


                }
            }
        });
    }
}