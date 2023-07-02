package com.hiteshjangid.attendance.ui.gradeDetail;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hiteshjangid.attendance.Adapter.GradeDetailAdapter;
import com.hiteshjangid.attendance.InsertGradeDetailActivity;
import com.hiteshjangid.attendance.databinding.ActivityGradeDetailBinding;

public class GradeDetailActivity extends AppCompatActivity {

    static final Integer WRITE_EXST = 0x3;
    static final Integer READ_EXST = 0x4;
    BottomAppBar bottomAppBar;
    FloatingActionButton fab_main;
    Button exportGrade;
    RecyclerView recyclerView;
    String gradeName;
    String room_ID;

    GradeDetailAdapter gradeListNewAdapter;
    GradeDetailViewModel gradeDetailViewModel;
    ActivityGradeDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGradeDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        gradeDetailViewModel = ViewModelProviders.of(this).get(GradeDetailViewModel.class);
        gradeName = getIntent().getStringExtra("gradeName");
        room_ID = getIntent().getStringExtra("graderoom_ID");
        fab_main = binding.fabMain;

        recyclerView = binding.recyclerViewMain;
        recyclerView.setHasFixedSize(true);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);

        Toast.makeText(GradeDetailActivity.this, gradeName, Toast.LENGTH_SHORT).show();

        gradeDetailViewModel.getGradeDetailNames(gradeName).observe(this, gradeNames -> {
            gradeListNewAdapter = new GradeDetailAdapter(GradeDetailActivity.this, gradeNames);
            recyclerView.setAdapter(gradeListNewAdapter);

        });


        fab_main.setOnClickListener(view -> {
            Intent intent = new Intent(GradeDetailActivity.this, InsertGradeDetailActivity.class);
            intent.putExtra("gradeName", gradeName);
            startActivity(intent);
        });
    }
}
