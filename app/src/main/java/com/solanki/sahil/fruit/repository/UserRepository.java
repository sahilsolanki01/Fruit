package com.solanki.sahil.fruit.repository;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;

import com.solanki.sahil.fruit.database.Dao;
import com.solanki.sahil.fruit.database.Model;
import com.solanki.sahil.fruit.database.MyDatabase;

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
    private static UserRepository instance;
    private ArrayList<Model> dataSet = new ArrayList<>();
    private LiveData<List<Model>> list;
    private MyDatabase database;
    private Dao dao;


    public static UserRepository getInstance(Application application) {
        if (instance == null) {
            instance = new UserRepository(application);
        }
        return instance;
    }

    public UserRepository(Application application) {
        database = MyDatabase.getInstance(application);
        dao = database.dao();
        list = dao.getListFromTable();
    }


    public void makeRequestCall(final Context context) {
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
                                        insert(dataSet);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Toast.makeText(context, " " + e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Toast.makeText(context, " " + response.message(), Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                t.printStackTrace();
                                Toast.makeText(context, " " + t.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(context, " " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(context, " " + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(context, " " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void insert(List<Model> list) {

        new InsertAsyncTask(dao).execute(list);
    }

    public LiveData<List<Model>> getList() {
        return list;
    }


    private static class InsertAsyncTask extends AsyncTask<List<Model>, Void, Void> {
        private Dao dao;

        public InsertAsyncTask(Dao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(List<Model>... lists) {
            dao.insertListToTable(lists[0]);
            return null;
        }
    }

}
