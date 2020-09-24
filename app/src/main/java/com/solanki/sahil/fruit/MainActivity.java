package com.solanki.sahil.fruit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.Configuration;
import android.os.Bundle;

import com.solanki.sahil.fruit.adapter.MyRecyclerViewAdapter;
import com.solanki.sahil.fruit.database.Model;
import com.solanki.sahil.fruit.viewmodel.MainActivityViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private MainActivityViewModel viewModel;
    private List<Model> mList;
    private MyRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();

        viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        viewModel.init(this);
        viewModel.getList().observe(this, new Observer<List<Model>>() {
            @Override
            public void onChanged(List<Model> list) {
                recyclerView.setAdapter(adapter);
                adapter.getCommitList(list);
            }
        });
    }


    private void initialize() {
        recyclerView = findViewById(R.id.recyclerview);
        mList = new ArrayList<>();
        adapter = new MyRecyclerViewAdapter(MainActivity.this, mList);
        adapter.notifyDataSetChanged();


        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        }
        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }
}
