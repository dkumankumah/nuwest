package nl.daxte1.dax1;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NewSurveyActivity extends AppCompatActivity {

    //    Firebase
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private StorageReference storageReference;

    //    Var's
    private EditText stelling;
    private Button postBtn;
    private ProgressBar progressBar;
    private String user_id;
    private String admin_id = "f59iEslTsSeRaLVATiUNVViTKz03";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_survey);

        //        firebase
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

//        Var's waarde
        stelling = (EditText) findViewById(R.id.addSurvey_edittext);
        postBtn = (Button) findViewById(R.id.addSurvey_btn);
        progressBar = (ProgressBar) findViewById(R.id.newSurvey_progress);
        user_id = firebaseAuth.getCurrentUser().getUid();

        if (!user_id.equals(admin_id)){
            openLogin();
            finish();
        }

        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String rack = stelling.getText().toString();

                if (!rack.isEmpty()){
                    progressBar.setVisibility(View.VISIBLE);

                    final String randomName = UUID.randomUUID().toString();

                    Map<String, Object> postMap = new HashMap<>();
                    postMap.put("stelling", rack);
                    postMap.put("timestamp", FieldValue.serverTimestamp());

                    firebaseFirestore.collection("Stellingen").add(postMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(NewSurveyActivity.this, "Stelling toegevoegd!", Toast.LENGTH_LONG).show();
                                openHome();
                                finish();

                            } else {
                                Toast.makeText(NewSurveyActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();

                            }
                        }
                    });


                }else{
                    Toast.makeText(NewSurveyActivity.this, "Geen stelling ingevuld", Toast.LENGTH_LONG).show();
                }



            }
        });

    }//end onCreate

    private void openHome() {
        Intent intent = new Intent(this, MainActivity.class);
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
