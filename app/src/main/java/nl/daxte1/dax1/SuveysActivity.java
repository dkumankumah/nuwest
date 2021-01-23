package nl.daxte1.dax1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import static android.support.constraint.Constraints.TAG;

public class SuveysActivity extends AppCompatActivity {

    //    Var's
    private RecyclerView survey_list_view;
    private List<StellingPost> survey_list;
    private SurveyRecyclerAdapter surveyRecyclerAdapter;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suveys);

        //        Firebase
        firebaseAuth = firebaseAuth.getInstance();

//        Var's hebben waarde
        survey_list = new ArrayList<>();
        survey_list_view = findViewById(R.id.layout_recycler_view_survey);

        surveyRecyclerAdapter = new SurveyRecyclerAdapter(survey_list);
        survey_list_view.setLayoutManager(new LinearLayoutManager(this));
        survey_list_view.setAdapter(surveyRecyclerAdapter);

        if (firebaseAuth.getCurrentUser() != null) {

//            Firebase
            firebaseFirestore = FirebaseFirestore.getInstance();

            Query firstQuery = firebaseFirestore.collection("Stellingen").orderBy("timestamp", Query.Direction.DESCENDING);

            firstQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException e) {

                    if (e != null) {
                        Log.d(TAG, "Error:" + e.getMessage());
                    } else {


                        for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                            if (doc.getType() == DocumentChange.Type.ADDED) {

                                String blogPostId = doc.getDocument().getId();

                                StellingPost stellingPost = doc.getDocument().toObject(StellingPost.class).withId(blogPostId);
                                survey_list.add(stellingPost);

                                surveyRecyclerAdapter.notifyDataSetChanged();

                            }//end if
                        }//end for
                    }
                }
            });
        }


    }
}
