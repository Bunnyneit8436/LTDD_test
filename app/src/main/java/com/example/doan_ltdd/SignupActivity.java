package com.example.doan_ltdd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.doan_ltdd.Class.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {

    EditText edt_usernamesgup,edt_name,edt_email, edt_phone, edt_address, edt_passwordsgup, edt_confirmpassword;
    Button btn_signup,btn_back;

    private FirebaseAuth mAuth;
    FirebaseDatabase database;

    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        edt_usernamesgup = findViewById(R.id.edt_usernamesgup);
        edt_name = findViewById(R.id.edt_name);
        edt_email = findViewById(R.id.edt_email);
        edt_phone = findViewById(R.id.edt_phone);
        edt_address = findViewById(R.id.edt_address);
        edt_passwordsgup = findViewById(R.id.edt_passwordsgup);
        edt_confirmpassword = findViewById(R.id.edt_confirmpassword);
        btn_signup = findViewById(R.id.btn_signup);
        btn_back = findViewById(R.id.btn_back);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        Drawable drawable = getResources().getDrawable(R.drawable.ic_baseline_arrow_back_24);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(drawable);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance("https://do-an-ltdd-default-rtdb.asia-southeast1.firebasedatabase.app/");

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Signup();
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(SignupActivity.this, SigninActivity.class);
                startActivity(intent);
            }
        });
    }

    public void Signup()
    {
        String username = edt_usernamesgup.getText().toString();
        String name = edt_name.getText().toString();
        String phone = edt_phone.getText().toString();
        String address = edt_address.getText().toString();
        String email = edt_email.getText().toString().trim();
        String password = edt_passwordsgup.getText().toString().trim();
        String confirmPassword = edt_confirmpassword.getText().toString();
        String userId = mAuth.getUid();
        String urlImage="https://firebasestorage.googleapis.com/v0/b/do-an-ltdd.appspot.com/o/image%2Fuser%2Fmy00QjuIgHUPK58m3oS73dXeDUv2%2F1638844187116.jpg?alt=media&token=f7ac52cb-187c-4648-a313-e2a9ec796de8";

        if (!isValid(email))
        {
            edt_email.setError("Email không hợp lệ");
            return;
        }
        if(username.isEmpty())
        {
            edt_usernamesgup.setError("Tên người dùng không thể ");
            return;
        }
        if(password.isEmpty()){
            edt_passwordsgup.setError("Mật khẩu bắt buộc");
            return;
        }
        if(confirmPassword.isEmpty()){
            edt_confirmpassword.setError("Mật khẩu bắt buộc");
            return;
        }

        if (!confirmPassword.equals(password))
        {
            edt_confirmpassword.setError("Mật khẩu sai");
            return;
        }
        else
        {
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Intent intent =new Intent(SignupActivity.this, SigninActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                        setUser(userId, urlImage, username, name, email, phone, password, address);

                        Toast.makeText(SignupActivity.this,"Đăng ký thành công",Toast.LENGTH_SHORT).show();
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(SignupActivity.this,"Đăng ký không thành công"+ task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:break;
        }

        return super.onOptionsItemSelected(item);
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

    public void setUser(String userId, String urlImage, String username, String name , String email, String phone, String password, String address)
    {
        username = edt_usernamesgup.getText().toString();
        name = edt_name.getText().toString();
        phone = edt_phone.getText().toString();
        address = edt_address.getText().toString();
        email = edt_email.getText().toString().trim();
        password = edt_passwordsgup.getText().toString().trim();
        userId = mAuth.getUid();
        urlImage ="https://firebasestorage.googleapis.com/v0/b/do-an-ltdd.appspot.com/o/image%2Fuser%2Fmy00QjuIgHUPK58m3oS73dXeDUv2%2F1638844187116.jpg?alt=media&token=f7ac52cb-187c-4648-a313-e2a9ec796de8";

//        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        User user = new User(userId,urlImage,username, name, email, phone, password, address );
        myRef.child("DbUser").child(userId).setValue(user);


    }
}