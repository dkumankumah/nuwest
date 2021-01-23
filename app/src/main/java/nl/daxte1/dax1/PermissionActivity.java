package nl.daxte1.dax1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;


public class PermissionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);

    }//end onCreate

    public void openLogin(View v){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void permissionAction(View v){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

            if (ContextCompat.checkSelfPermission(PermissionActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                Toast.makeText(PermissionActivity.this, "Rechten niet gegeven", Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(PermissionActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1 );
            } else {
                Toast.makeText(PermissionActivity.this, "Rechten gegeven", Toast.LENGTH_LONG).show();
            }

        }
    }
}
