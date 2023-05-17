package com.hiteshjangid.attendance.ui.main;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import com.hiteshjangid.attendance.Adapter.ClassListNewAdapter;
import com.hiteshjangid.attendance.Insert_class_Activity;
import com.hiteshjangid.attendance.databinding.ActivityMainBinding;
import com.hiteshjangid.attendance.model.Class_Names;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    String room_ID;
    BottomAppBar bottomAppBar;
    FloatingActionButton fab_main;
    RecyclerView recyclerView;
    List<Class_Names> classNamesList;
    ClassListNewAdapter classListNewAdapter;
    ActivityMainBinding binding;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWindow().setEnterTransition(null);
        MainViewModel mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        room_ID = getIntent().getStringExtra("gradeDetailroom_ID");

        bottomAppBar = binding.bottomAppBar;
        fab_main = binding.fabMain;
        fab_main.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, Insert_class_Activity.class);
            intent.putExtra("gradeDetailName", room_ID);
            startActivity(intent);
        });


        recyclerView = binding.recyclerViewMain;
        recyclerView.setHasFixedSize(true);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(staggeredGridLayoutManager);

        classNamesList = new ArrayList<>();
        classListNewAdapter = new ClassListNewAdapter(MainActivity.this, classNamesList);
        recyclerView.setAdapter(classListNewAdapter);


        mainViewModel.getListMutableLiveData(room_ID).observe(this, class_names -> {
            classListNewAdapter = new ClassListNewAdapter(MainActivity.this, class_names);
            recyclerView.setAdapter(classListNewAdapter);
        });

    }
}
