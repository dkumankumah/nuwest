package nl.daxte1.dax1;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

//    Var's
    private RecyclerView blog_list_view;
    private List<BlogPost> blog_list;
    private BlogRecyclerAdapter blogRecyclerAdapter;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container,false);

//        Firebase
        firebaseAuth = firebaseAuth.getInstance();

//        Var's hebben waarde
        blog_list = new ArrayList<>();
        blog_list_view = view.findViewById(R.id.layout_recycler_view);

        blogRecyclerAdapter = new BlogRecyclerAdapter(blog_list);
        blog_list_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        blog_list_view.setAdapter(blogRecyclerAdapter);

        if (firebaseAuth.getCurrentUser() != null){

//            Firebase
        firebaseFirestore = FirebaseFirestore.getInstance();

        Query firstQuery = firebaseFirestore.collection("Posts").orderBy("timestamp", Query.Direction.DESCENDING);

        firstQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException e) {

                if (e != null){
                    Log.d(TAG,"Error:"+e.getMessage());
                } else {


                for (DocumentChange doc: documentSnapshots.getDocumentChanges()) {
                    if (doc.getType() == DocumentChange.Type.ADDED){

                        BlogPost blogPost = doc.getDocument().toObject(BlogPost.class);
                        blog_list.add(blogPost);

                        blogRecyclerAdapter.notifyDataSetChanged();

                    }//end if
                }//end for
                }
            }
        });
        }

        // Inflate the layout for this fragment
        return view;
        }

    }
