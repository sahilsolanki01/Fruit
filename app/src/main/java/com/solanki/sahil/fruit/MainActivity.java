package com.solanki.sahil.fruit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import com.solanki.sahil.fruit.adapter.MyRecyclerViewAdapter;
import com.solanki.sahil.fruit.model.Model;
import com.solanki.sahil.fruit.repository.UserRepository;
import com.solanki.sahil.fruit.viewmodel.MainActivityViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private MainActivityViewModel viewModel;
    private List<Model> mList;
    private MyRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private UserRepository repository;

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
//                recyclerView.setAdapter(adapter);
//                adapter.getAllGames(games);
                setRecyclerView(list);
            }
        });
    }

    private void setRecyclerView(List<Model> list) {
        adapter = new MyRecyclerViewAdapter(MainActivity.this,list);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    private void initialize() {
//        repository = new UserRepository(getApplication());
//        mList = new ArrayList<>();
//
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setHasFixedSize(true);
//        adapter = new MyRecyclerViewAdapter(this, mList);

        recyclerView = findViewById(R.id.recyclerview);
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        }
        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }
}
