package com.example.whatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class WhatsappUsers extends AppCompatActivity implements AdapterView.OnItemClickListener {

    final ArrayList<String> waUsers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whatsapp_users);

        final ListView listView = findViewById(R.id.listView);
        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, waUsers);

        final SwipeRefreshLayout swipeRefresh = findViewById(R.id.swipeRefresh);

        listView.setOnItemClickListener(this);

        try {
            ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
            parseQuery.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
            parseQuery.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> objects, ParseException e) {
                    if (objects.size() > 0 && e == null){
                        for (ParseUser user : objects){
                            waUsers.add(user.getUsername());
                        }
                        listView.setAdapter(adapter);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    ParseQuery<ParseUser> query = ParseUser.getQuery();
                    query.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
                    query.whereNotContainedIn("username", waUsers);
                    query.findInBackground(new FindCallback<ParseUser>() {
                        @Override
                        public void done(List<ParseUser> objects, ParseException e) {
                            if (objects.size() > 0){
                                if (e == null){
                                    for (ParseUser user : objects){
                                        waUsers.add(user.getUsername());
                                    }
                                    adapter.notifyDataSetChanged();
                                    if (swipeRefresh.isRefreshing()){
                                        swipeRefresh.setRefreshing(false);
                                    }
                                }
                            }
                            else {
                                if (swipeRefresh.isRefreshing()){
                                    swipeRefresh.setRefreshing(false);
                                }
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        ParseUser.getCurrentUser().logOutInBackground(new LogOutCallback() {
            @Override
            public void done(ParseException e) {
                Intent intent = new Intent(WhatsappUsers.this, Login.class);
                startActivity(intent);
                finish();
            }
        });
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(WhatsappUsers.this, ChatActivity.class);
        intent.putExtra("selectedUser", waUsers.get(i));
        startActivity(intent);
    }
}