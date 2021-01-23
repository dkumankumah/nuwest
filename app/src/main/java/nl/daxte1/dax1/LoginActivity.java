package nl.daxte1.dax1;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    //    Var's
    private Button loginBtn;
    EditText tUsername;
    EditText tPassword;
    private ProgressBar progressBar;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_login);

//        Login Var hebben een waarde
        tUsername = findViewById(R.id.email_input);
        tPassword = findViewById(R.id.password_input);
        loginBtn = (Button) findViewById(R.id.login_btn);
        progressBar = (ProgressBar) findViewById(R.id.lProgress);
        firebaseAuth= FirebaseAuth.getInstance();

//        Onclick voor login
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inProgress(true);
                firebaseAuth.signInWithEmailAndPassword(tUsername.getText().toString(),
                        tPassword.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    openHome();
                                    inProgress(false);
                                } else {
                                    Toast.makeText(LoginActivity.this , task.getException().getMessage()
                                            , Toast.LENGTH_LONG).show();
                                    inProgress(false);

                                }
                            }
                        });
            }
        });


    }//end onCreate

    //    Open register door knop
    public void openRegister(){
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }//end openRegister
    public void openRegister(View v)
    {
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }

    //    Open home fucntion
    public void openHome(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public void openHome(View v)
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    //    Open wachtwoord vergeten function
    public void openPassforgot(){
        Intent intent = new Intent(this, ForgotPasswordActivity.class);
        startActivity(intent);
    }
    public void openPassforgot(View v)
    {
        Intent intent = new Intent(this, ForgotPasswordActivity.class);
        startActivity(intent);
    }

//    Open permissions
    public void openPermissions(){
    Intent intent = new Intent(this, PermissionActivity.class);
    startActivity(intent);
    }


//    ProgressBar
    public void inProgress(boolean x){
        if(x){
            progressBar.setVisibility(View.VISIBLE);
            loginBtn.setEnabled(false);
        } else {
            progressBar.setVisibility(View.GONE);
            loginBtn.setEnabled(true);
        }
    }


}
