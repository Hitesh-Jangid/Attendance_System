package com.hiteshjangid.attendance.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.RecyclerView;

import com.hiteshjangid.attendance.ClassDetail_Activity;
import com.hiteshjangid.attendance.R;
import com.hiteshjangid.attendance.common.Common;
import com.hiteshjangid.attendance.model.Class_Names;
import com.hiteshjangid.attendance.model.Students_List;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ClassListNewAdapter extends RecyclerView.Adapter<ClassListNewAdapter.ViewHolder> {

    private Context mContext;
    private List<Class_Names> classNamesList;
    private Activity mActivity;
    private DatabaseReference reference;

    public ClassListNewAdapter(Context mContext, List<Class_Names> classNamesList) {
        this.mContext = mContext;
        this.classNamesList = classNamesList;
        this.reference = FirebaseDatabase.getInstance().getReference("Classes");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.class_adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Class_Names classNames = classNamesList.get(position);
        numberOfStudent(holder.total_students, classNames);
        holder.class_name.setText(classNames.getName_class());
        holder.subject_name.setText(classNames.getName_subject());

        if (classNames.getPosition_bg().equals("0")) {
            int[] colors = {0xFFFDEB71, 0xFFF8D800};
            GradientDrawable gradientDrawable = new GradientDrawable(
                    GradientDrawable.Orientation.BL_TR, colors);
            gradientDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
            gradientDrawable.setGradientCenter(0.5f, 0.5f);
            gradientDrawable.setGradientRadius(1);
            holder.frameLayout.setBackground(gradientDrawable);
            holder.subject_name.setTextColor(mContext.getResources().getColor(R.color.black));
            holder.class_name.setTextColor(mContext.getResources().getColor(R.color.text_color_secondary));
            holder.total_students.setTextColor(mContext.getResources().getColor(R.color.text_color_secondary));
        }

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), ClassDetail_Activity.class);
            intent.putExtra("theme", classNamesList.get(holder.getAdapterPosition()).getPosition_bg());
            intent.putExtra("className", classNamesList.get(holder.getAdapterPosition()).getName_class());
            Common.currentClassName = classNamesList.get(holder.getAdapterPosition()).getName_class() + classNames.getName_subject();
            intent.putExtra("subjectName", classNamesList.get(holder.getAdapterPosition()).getName_subject());
            intent.putExtra("classroom_ID", classNamesList.get(holder.getAdapterPosition()).getId());
            Pair<View, String> p1 = Pair.create(holder.cardView, "ExampleTransition");
            view.getContext().startActivity(intent);
        });
    }

    private void numberOfStudent(TextView total_students, Class_Names classNames) {
        DatabaseReference classReference = reference.child(classNames.getId()).child("Student_List");
        classReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int count = 0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Students_List students_list = dataSnapshot.getValue(Students_List.class);
                    if (students_list != null && students_list.getClass_id().equals(classNames.getName_class() + classNames.getName_subject())) {
                        count++;
                    }
                }
                total_students.setText("Total Students: " + count);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error if necessary
            }
        });
    }

    @Override
    public int getItemCount() {
        return classNamesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView class_name;
        public TextView subject_name;
        public TextView total_students;
        public ImageView imageView_bg;
        public RelativeLayout frameLayout;
        public CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            class_name = itemView.findViewById(R.id.className_adapter);
            subject_name = itemView.findViewById(R.id.subjectName_adapter);
            imageView_bg = itemView.findViewById(R.id.imageClass_adapter);
            frameLayout = itemView.findViewById(R.id.frame_bg);
            cardView = itemView.findViewById(R.id.cardView_adapter);
            total_students = itemView.findViewById(R.id.totalStudents_adapter);
        }
    }
}
