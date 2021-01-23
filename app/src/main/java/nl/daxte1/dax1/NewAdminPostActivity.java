package nl.daxte1.dax1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class NewAdminPostActivity extends AppCompatActivity {

    //    Firebase
    private FirebaseAuth firebaseAuth;
    //    Var's
    private String user_id;
    private String admin_id = "f59iEslTsSeRaLVATiUNVViTKz03";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_admin_post);

        //        firebase
        firebaseAuth = FirebaseAuth.getInstance();

//        Var's hebben waarde
        user_id = firebaseAuth.getCurrentUser().getUid();

        if (!user_id.equals(admin_id)){
            openLogin();
            finish();
        }



    }//end onCreate

    private void openHome() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void openVragenlijst(View v) {
        Intent intent = new Intent(this, NewSurveyActivity.class);
        startActivity(intent);
    }

    public void openNewpost(View v)
    {
        Intent intent = new Intent(this, NewPostActivity.class);
        startActivity(intent);
    }

    //    GA naar login als op knop is gedrukt
    public void openLogin(){
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
