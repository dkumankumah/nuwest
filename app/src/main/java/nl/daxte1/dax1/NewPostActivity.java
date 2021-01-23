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

public class NewPostActivity extends AppCompatActivity {

//    Firebase
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private StorageReference storageReference;

//    Var's
    private ImageView postImage;
    private EditText title;
    private EditText description;
    private Button postBtn;
    private Uri postImageUri = null;
    private ProgressBar progressBar;
    private String user_id;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

//        firebase
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

//        Var's waarde
        postImage = (ImageView) findViewById(R.id.newPost_img);
        title = (EditText) findViewById(R.id.newPost_title);
        description = (EditText) findViewById(R.id.newPost_desc);
        postBtn = (Button) findViewById(R.id.newPost_btn);
        progressBar = (ProgressBar) findViewById(R.id.newPost_progres);
        user_id = firebaseAuth.getCurrentUser().getUid();

        postImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                imagePicker();

            }
        });

        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String titel = title.getText().toString();
                final String desc = description.getText().toString();

                if (!TextUtils.isEmpty(desc) && postImageUri != null && !TextUtils.isEmpty(titel)){

                    progressBar.setVisibility(View.VISIBLE);

                    final String randomName = UUID.randomUUID().toString();

                    final StorageReference filepath = storageReference.child("post_images").child(randomName);

                    filepath.putFile(postImageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()){
                                throw task.getException();
                            }
                            return filepath.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()){

                                Uri downUri = task.getResult();


                                //                                info voor doc in FireFirestore
                                Map<String, Object> postMap = new HashMap<>();
                                postMap.put("title", titel);
                                postMap.put("description", desc);
                                postMap.put("image", downUri.toString());
                                postMap.put("user_id", user_id);
                                postMap.put("timestamp", FieldValue.serverTimestamp());

                                firebaseFirestore.collection("Posts").add(postMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentReference> task) {

                                        if (task.isSuccessful()){
                                            Toast.makeText(NewPostActivity.this, "Post toegevoegd!", Toast.LENGTH_LONG).show();

                                            openHome();
                                            finish();

                                        } else {
                                            Toast.makeText(NewPostActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();

                                        }

                                        progressBar.setVisibility(View.INVISIBLE);


                                    }
                                });

                            } else{
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        }
                    });


                } else {

                }


            }
        });


    }//end onCreate

    private void openHome() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                postImageUri = result.getUri();

                postImage.setImageURI(postImageUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();

            }
        }
    }

    private void imagePicker() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMinCropResultSize(512,512)
                .setAspectRatio(2,1)
                .start(NewPostActivity.this);
    }
}
