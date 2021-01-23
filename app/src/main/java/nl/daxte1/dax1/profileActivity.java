package nl.daxte1.dax1;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.security.PrivateKey;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

import de.hdodenhof.circleimageview.CircleImageView;

public class profileActivity extends AppCompatActivity {

    private CircleImageView profilePicture;
    private Uri mainImageUri = null;
    private EditText editName;
    private Button submit;
    private ProgressBar progressBar;

    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private String user_id;
    private boolean isChanged = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profilePicture = findViewById(R.id.stProfile_img);
        editName = (EditText) findViewById(R.id.stName_input);
        submit = (Button) findViewById(R.id.stSubmit_btn);
        progressBar = (ProgressBar) findViewById(R.id.stProgressbar);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        user_id = firebaseAuth.getCurrentUser().getUid();

        progressBar.setVisibility(View.VISIBLE);
        submit.setEnabled(false);

        firebaseFirestore.collection("Users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()){

                    if (task.getResult().exists()){

                        String name = task.getResult().getString("name");
                        String image = task.getResult().getString("image");

                        mainImageUri = Uri.parse(image);

                        editName.setText(name);

                        RequestOptions placeholderRequest = new RequestOptions();
                        placeholderRequest.placeholder(R.drawable.default_profile_picture);

                        Glide.with(profileActivity.this).setDefaultRequestOptions(placeholderRequest).load(image).into(profilePicture);


                    } else {

                    }
                    progressBar.setVisibility(View.INVISIBLE);
                    submit.setEnabled(true);

                } else {
                    Toast.makeText(profileActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        });



        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                final String username = editName.getText().toString();

                if (isChanged){

                progressBar.setVisibility(View.VISIBLE);

                if (!TextUtils.isEmpty(username) && mainImageUri != null){
//                    id van user van database
                    user_id = firebaseAuth.getCurrentUser().getUid();

//                    naam van image in database
                    final StorageReference image_path = storageReference.child("profile_images").child(user_id + ".jpg");


                    image_path.putFile(mainImageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()){
                                throw task.getException();
                            }
                            return image_path.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()){

                                Uri downUri = task.getResult();


                                //                                info voor doc in FireFirestore
                                Map<String, String> userMap = new HashMap<>();
                                userMap.put("name", username);
                                userMap.put("image", downUri.toString());

                                firebaseFirestore.collection("Users").document(user_id).set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()){
                                            Toast.makeText(profileActivity.this, "Profiel instellingen gewijzigd", Toast.LENGTH_LONG).show();
                                            progressBar.setVisibility(View.INVISIBLE);
                                            openHome();

                                        } else {
                                            Toast.makeText(profileActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();

                                        }

                                    }
                                });

                            } else{}
                        }
                    });

                } else {
                    Toast.makeText(profileActivity.this, "Profiel foto of naam is leeg.", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.INVISIBLE);
                    finish();
                }
                } else {

                    if (TextUtils.isEmpty(username) && mainImageUri == null){
                        Toast.makeText(profileActivity.this, "Profiel foto of naam is leeg.", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.INVISIBLE);
                        openHome();
                        finish();
                    } else {
//                    id van user van database)

                    Uri downUri = mainImageUri;

                    Map<String, String> userMap = new HashMap<>();
                    userMap.put("name", username);
                    userMap.put("image", downUri.toString());

                    firebaseFirestore.collection("Users").document(user_id).set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()){
                                Toast.makeText(profileActivity.this, "Profiel instellingen gewijzigd", Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.INVISIBLE);
                                openHome();

                            } else {
                                Toast.makeText(profileActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();

                            }

                        }
                    });
                    }
                }
            }
        });



        profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                imagePicker();
            }
        });

    }

    private void imagePicker() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1,1)
                .start(profileActivity.this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                mainImageUri = result.getUri();
                profilePicture.setImageURI(mainImageUri);

                isChanged = true;

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();

            }
        }
    }

    public void openHome (View v){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    public void openHome (){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

}
