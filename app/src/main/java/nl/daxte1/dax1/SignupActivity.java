package nl.daxte1.dax1;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

//    Var's
    private Button registerBtn;
    EditText email;
    EditText password;
    private ProgressBar progressBar;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

//        Firebase shit
        firebaseAuth = FirebaseAuth.getInstance();

////        Logo
//        logo = (ImageView) findViewById(R.id.logo_img);
//        logo.setImageResource(R.drawable.logo2);

//        button hebben een waarde
        registerBtn = (Button) findViewById(R.id.sRegister_btn);
        email = (EditText) findViewById(R.id.sEmail_input);
        password = (EditText) findViewById(R.id.sPassword);
        progressBar = (ProgressBar) findViewById(R.id.rprogress);

//        Sign up
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inProgress(true);
                firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(),
                        password.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                                        Toast.makeText(SignupActivity.this, "Geregistreerd!", Toast.LENGTH_LONG).show();
                                        openPermissions();
                                        inProgress(false);
                            }
                         else {
                            Toast.makeText(SignupActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            inProgress(false);
                        }
                    }
                });
            }
        });

    }//end onCreate

    public void openLogin(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
    public void openLogin(View v)
    {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void openPermissions(){
        Intent intent = new Intent(this, PermissionActivity.class);
        startActivity(intent);
    }

    public void inProgress(boolean x){
        if(x){
            progressBar.setVisibility(View.VISIBLE);
            registerBtn.setEnabled(false);
        } else {
            progressBar.setVisibility(View.GONE);
            registerBtn.setEnabled(true);
        }
    }
}
