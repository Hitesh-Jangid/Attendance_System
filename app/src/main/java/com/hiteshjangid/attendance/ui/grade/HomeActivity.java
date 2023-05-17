package com.hiteshjangid.attendance.ui.grade;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hiteshjangid.attendance.Adapter.GradeListAdapter;
import com.hiteshjangid.attendance.Insert_Grade_Activity;
import com.hiteshjangid.attendance.common.Common;
import com.hiteshjangid.attendance.databinding.ActivityHomeBinding;


public class HomeActivity extends AppCompatActivity {

    ActivityHomeBinding binding;
    BottomAppBar bottomAppBar;
    FloatingActionButton fab_main;
    RecyclerView recyclerView;
    GradeListAdapter gradeListNewAdapter;
    FirebaseUser firebaseUser;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        HomeViewModel homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        bottomAppBar = binding.bottomAppBar;
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        fab_main = binding.fabMain;

        fab_main.setOnClickListener(view -> {
            Intent intent = new Intent(HomeActivity.this, Insert_Grade_Activity.class);
            startActivity(intent);
        });


        recyclerView = binding.recyclerViewMain;
        recyclerView.setHasFixedSize(true);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);


        if (Common.currentUserType.equals("admin")) {
            homeViewModel.getGradeNamesForAdmin().observe(this, gradeNames ->{
                gradeListNewAdapter = new GradeListAdapter(HomeActivity.this, gradeNames);
                recyclerView.setAdapter(gradeListNewAdapter);
            });
        } else {
            homeViewModel.getGradeNamesForUser().observe(this, gradeNames ->{
                gradeListNewAdapter = new GradeListAdapter(HomeActivity.this, gradeNames);
                recyclerView.setAdapter(gradeListNewAdapter);
            });
        }
    }





}