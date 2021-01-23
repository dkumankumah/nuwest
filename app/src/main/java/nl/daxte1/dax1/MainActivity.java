package nl.daxte1.dax1;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.app.Fragment;
import android.widget.Toast;


import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;

    private String user_id;
    private String admin_id = "f59iEslTsSeRaLVATiUNVViTKz03";

    //    Var's
    private FloatingActionButton addPostBtn;
    private HomeFragment homeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);

        //        Firebase shit doen
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();


//        Niet logged in? to loggin
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            openLogin();
        } else{

            user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();

            setContentView(R.layout.activity_main);

        //        Var's hebben waarde
        addPostBtn = (FloatingActionButton) findViewById(R.id.mAdd_btn);
        homeFragment = new HomeFragment();

//        Fragment Home
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_container, homeFragment);
        fragmentTransaction.commit();


//                add button


//                Als de admin is ingelogd
                if (admin_id.equals(user_id)){
                    addPostBtn.setImageResource(R.drawable.preferences);

                    Toast.makeText(MainActivity.this, "Welkom admin", Toast.LENGTH_LONG).show();
                    //                Naar openNewPost
                    addPostBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openAdminPost();

                        }
                    });
                } else {
                    addPostBtn.setImageResource(R.drawable.add_icon);

                    //                Naar openNewPost
                    addPostBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openNewPost();

                        }
                    });
                }

        }//end if else
    }//end onCreate

    private void openNewPost() {
        Intent intent = new Intent(this, NewPostActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
            getMenuInflater().inflate(R.menu.main_menu, menu);
            return super.onCreateOptionsMenu(menu);
        }

//        Hamburger Menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.mProfile_btn:
                openProfile();
                break;

            case R.id.mLogout_btn:
                FirebaseAuth.getInstance().signOut();
                    openLogin();
                    break;

            case R.id.mVote_btn:
                openSurvey();
                break;
            }
            return super.onOptionsItemSelected(item);
    }

    private void openProfile() {
        Intent intent = new Intent(this, profileActivity.class);
        startActivity(intent);
    }

    public void openSurvey (){
        Intent intent = new Intent(this, SuveysActivity.class);
        startActivity(intent);
    }

    public void openSurvey (View view){
        Intent intent = new Intent(this, SuveysActivity.class);
        startActivity(intent);
    }

    //    GA naar login als op knop is gedrukt
    public void openLogin(){
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }


    private void openAdminPost() {
        Intent intent = new Intent(this, NewAdminPostActivity.class);
        startActivity(intent);
    }



}
