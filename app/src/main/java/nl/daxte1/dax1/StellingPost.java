package nl.daxte1.dax1;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import java.util.Date;

public class StellingPost extends BlogPostId {

    public String stelling;
    public Date timestamp;
    public Button Verstuur;

    public StellingPost(){}

    public StellingPost(String stelling, Date timestamp) {
        this.stelling = stelling;
        this.timestamp = timestamp;
    }

    public String getStelling() {
        return stelling;
    }

    public void setStelling(String stelling) {
        this.stelling = stelling;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

}
