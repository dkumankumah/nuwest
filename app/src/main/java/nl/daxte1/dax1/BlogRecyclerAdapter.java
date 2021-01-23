package nl.daxte1.dax1;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class BlogRecyclerAdapter extends RecyclerView.Adapter<BlogRecyclerAdapter.ViewHolder> {

    public List<BlogPost> blog_list;
    public Context context;

    private FirebaseFirestore firebaseFirestore;

    public BlogRecyclerAdapter(List<BlogPost> blog_list){

        this.blog_list = blog_list;
    }

    @NonNull
    @Override
    public BlogRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        firebaseFirestore = FirebaseFirestore.getInstance();

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.blog_list_item, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final BlogRecyclerAdapter.ViewHolder holder, int position) {

        String desc_data = blog_list.get(position).getDescription();
        holder.setDescText(desc_data);

        String title_data = blog_list.get(position).getTitle();
        holder.setTitle(title_data);

        final String image_uri = blog_list.get(position).getImage();
        holder.setBlogImage(image_uri);


        String user_id = blog_list.get(position).getUser_id();
        firebaseFirestore.collection("Users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()){

                    String username = task.getResult().getString("name");
                    String profile_uri = task.getResult().getString("image");

                    holder.setBlogUsername(username, profile_uri);

                }

            }
        });

        long milliseconds = blog_list.get(position).getTimestamp().getTime();
        String dateString = DateFormat.format("dd/MM/yyyy", new Date(milliseconds)).toString();
        holder.setTime(dateString);



    }

    @Override
    public int getItemCount() {
        return blog_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View mView;

//        Var's uit database
        private TextView descView;
        private TextView titleView;
        private ImageView blogImageView;
        private TextView blogDate;
        private TextView blogUsername;
        private CircleImageView blogUserImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

        }

        public void setDescText(String descText){

            descView = mView.findViewById(R.id.blog_desc);
            descView.setText(descText);

        }

        public void setBlogImage(String downloadUri){

            blogImageView = mView.findViewById(R.id.blog_img);

            RequestOptions placeholderOptions = new RequestOptions();
            placeholderOptions.placeholder(R.drawable.default_thumbnail);

            Glide.with(context).applyDefaultRequestOptions(placeholderOptions).load(downloadUri).into(blogImageView);

        }

        public void setTitle(String titleText){

            titleView = mView.findViewById(R.id.blog_title);
            titleView.setText(titleText);

        }

        public void setBlogUsername(String username, String image){

            blogUsername = mView.findViewById(R.id.blog_user_name);
            blogUsername.setText(username);

            blogUsername.setText(username);

            blogUserImage = mView.findViewById(R.id.blog_user_image);

            RequestOptions placeholderOptions = new RequestOptions();
            placeholderOptions.placeholder(R.drawable.default_profile_picture);

            Glide.with(context).applyDefaultRequestOptions(placeholderOptions).load(image).into(blogUserImage);

        }

        public void setTime(String date){

            blogDate = mView.findViewById(R.id.blog_date);
            blogDate.setText(date);

        }


    }//end end
}
