package com.example.doan_ltdd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.doan_ltdd.Admin.CategoryActivity;
import com.example.doan_ltdd.Admin.ProductActivity;
import com.example.doan_ltdd.Class.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class SigninActivity extends AppCompatActivity {

    EditText edt_email, edt_password;
    Button btn_Signup, btn_Signin;
    ImageView img_admin;

    private FirebaseAuth mAuth;
    FirebaseUser mUser;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            reload();
        }
    }

    private void reload()
    {
        Intent intent =new Intent(SigninActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        edt_email = findViewById(R.id.edt_email);
        edt_password = findViewById(R.id.edt_password);
        btn_Signin = findViewById(R.id.btn_Signin);
        btn_Signup = findViewById(R.id.btn_Signup);
//        img_admin = findViewById(R.id.img_admin);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        btn_Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(SigninActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

        btn_Signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Signin();
            }
        });

//        img_admin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent =new Intent(SigninActivity.this, ProductActivity.class);
//                startActivity(intent);
//            }
//        });
    }

    public void Signin()
    {
        String email = edt_email.getText().toString().trim();
        String password = edt_password.getText().toString().trim();

        if (!isValid(email))
        {
            edt_email.setError("Email không hợp lệ");
            return;
        }

        if(password.isEmpty()){
            edt_password.setError("Nhập mật khẩu");
            return;
        }
//        else if (password.length()<6)
//        {
//            edt_password.setError("Minimum 6 number");
//        }
        else
        {
            mAuth.signInWithEmailAndPassword(email,password) .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(SigninActivity.this,"Đăng nhập thành công",Toast.LENGTH_SHORT).show();
                        Intent intent =new Intent(SigninActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(SigninActivity.this,"Đăng nhập thất bại",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    public static boolean isValid(String email)
    {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";
        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }
}