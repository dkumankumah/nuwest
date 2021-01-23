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

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.List;

public class SurveyRecyclerAdapter extends RecyclerView.Adapter<SurveyRecyclerAdapter.ViewHolder> {

    public List<StellingPost> survey_list;
    public Context context;

    public ImageView Like_btn;


    private FirebaseFirestore firebaseFirestore;

    public SurveyRecyclerAdapter(List<StellingPost> survey_list){

        this.survey_list = survey_list;
    }

    @NonNull
    @Override
    public SurveyRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        firebaseFirestore = FirebaseFirestore.getInstance();

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.stelling_list_item, parent, false);
        context = parent.getContext();

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String blogPostId = survey_list.get(position).BlogPostId;

        String stelling_data = survey_list.get(position).getStelling();
        holder.setStelText(stelling_data);

        long milliseconds = survey_list.get(position).getTimestamp().getTime();
        String dateString = DateFormat.format("dd/MM/yyyy", new Date(milliseconds)).toString();
        holder.setTime(dateString);

        //likes
        Like_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

    @Override
    public int getItemCount() {
        return survey_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View mView;

        //        Var's uit database
        private TextView stellingView;
        private TextView surveyDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

            Like_btn = mView.findViewById(R.id.Like_btn);

        }

        public void setStelText(String stellingText){

            stellingView = mView.findViewById(R.id.list_item_stelling);
            stellingView.setText(stellingText);

        }

        public void setTime(String date){

            surveyDate = mView.findViewById(R.id.stelling_date);
            surveyDate.setText(date);

        }


    }//end end
}
