package com.solanki.sahil.fruit.repository;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.lifecycle.MutableLiveData;

import com.solanki.sahil.fruit.model.Model;
import com.solanki.sahil.fruit.network.RetrofitInstance;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {
//    public com.solanki.sahil.trivaapp.database.Dao dao;
//    public LiveData<List<Games>> getAllGames;
//    private MyDatabase database;
//
//
//    public UserRepository(Application application){
//        database = MyDatabase.getInstance(application);
//        dao = database.dao();
//        getAllGames = dao.getAllGames();
//
//    }
//
//    public void addGame(List<Games> games){
//
//        new InsertAsyncTask(dao).execute(games);
//    }
//
//    public LiveData<List<Games>> getAllGames(){
//        return getAllGames;
//    }
//
//
//    private static class InsertAsyncTask extends AsyncTask<List<Games>,Void,Void> {
//        private Dao dao;
//
//        public InsertAsyncTask(Dao dao)
//        {
//            this.dao = dao;
//        }
//        @Override
//        protected Void doInBackground(List<Games>... lists) {
//            dao.addGame(lists[0]);
//            return null;
//        }
//    }

    private static UserRepository instance;
    private ArrayList<Model> dataSet = new ArrayList<>();
    private MutableLiveData<List<Model>> data = new MutableLiveData<>();
    private ProgressDialog mProgressBar;


    public static UserRepository getInstance(Context context) {
        if (instance == null) {
            instance = new UserRepository(context);
        }
        return instance;
    }


    private UserRepository(Context context) {
        mProgressBar = new ProgressDialog(context);
        mProgressBar.setMessage("Data Being Loading ...");
        mProgressBar.show();
    }


    public MutableLiveData<List<Model>> getRepo() {
        Call<ResponseBody> call = new RetrofitInstance().getApi().searchRepo("retrofit");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String result = response.body().string();

                        JSONObject jsonObject = new JSONObject(result);
                        JSONArray jsonArray = jsonObject.getJSONArray("items");
                        JSONObject jsonObject2 = jsonArray.getJSONObject(0);

                        String commits_url = jsonObject2.getString("commits_url");
                        String correct_url = commits_url.substring(0, commits_url.indexOf('{'));
                        Log.e("UserRepository", "onResponse: " + correct_url);

                        Call<ResponseBody> call2 = new RetrofitInstance().getApi().searchCommits(correct_url);
                        call2.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                mProgressBar.dismiss();
                                if (response.isSuccessful()) {
                                    try {
                                        String result = response.body().string();
                                        JSONArray jsonArray = new JSONArray(result);

                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject object = jsonArray.getJSONObject(i);

                                            JSONObject object2 = object.getJSONObject("commit");
                                            String message = object2.getString("message");

                                            JSONObject object3 = object2.getJSONObject("author");
                                            String name = object3.getString("name");
                                            String email = object3.getString("email");
                                            String date = object3.getString("date");
                                            date = date.replace('T', ',');
                                            date = date.replace('Z', ' ');

                                            Model model = new Model(name, email, date, message);
                                            dataSet.add(model);
                                        }
                                        data.setValue(dataSet);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } else {

                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                t.printStackTrace();
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });

        return data;
    }

}
