package com.solanki.sahil.fruit.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.solanki.sahil.fruit.database.Model;
import com.solanki.sahil.fruit.repository.UserRepository;

import java.util.List;


public class MainActivityViewModel extends AndroidViewModel {
    private UserRepository repository;
    private LiveData<List<Model>> list;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        repository = UserRepository.getInstance(application);
        list = repository.getList();
    }

    public void init(Context context) {
        repository.makeRequestCall(context);
    }


    public LiveData<List<Model>> getList() {
        return list;
    }
}
