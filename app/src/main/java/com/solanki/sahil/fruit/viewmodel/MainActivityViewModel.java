package com.solanki.sahil.fruit.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.solanki.sahil.fruit.model.Model;
import com.solanki.sahil.fruit.repository.UserRepository;

import java.util.List;


public class MainActivityViewModel extends AndroidViewModel {
    private UserRepository repository;
    private LiveData<List<Model>> list;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
    }

    public void init(Context context) {
        if(list !=null){
            return;
        }
        repository = UserRepository.getInstance(context);
        list = repository.getRepo();
    }


    public LiveData<List<Model>> getList()
    {
        return list;
    }
}
