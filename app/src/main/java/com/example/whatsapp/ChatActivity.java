package com.example.whatsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView chatListView;
    private ArrayList<String> chatsList;
    private ArrayAdapter adapter;
    private String selectedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        selectedUser = getIntent().getStringExtra("selectedUser");
        setTitle(selectedUser);

        findViewById(R.id.fabSend).setOnClickListener(ChatActivity.this);

        chatListView = findViewById(R.id.chatListView);
        chatsList = new ArrayList<>();
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, chatsList);
        chatListView.setAdapter(adapter);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

            try {

            ParseQuery<ParseObject> firstUserChatQuery = ParseQuery.getQuery("Chat");
            ParseQuery<ParseObject> secondUserChatQuery = ParseQuery.getQuery("Chat");

            firstUserChatQuery.whereEqualTo("waSender", ParseUser.getCurrentUser().getUsername());
            firstUserChatQuery.whereEqualTo("waTargetRecipient", selectedUser);

            secondUserChatQuery.whereEqualTo("waSender", selectedUser);
            secondUserChatQuery.whereEqualTo("waTargetRecipient", ParseUser.getCurrentUser().getUsername());



            ArrayList<ParseQuery<ParseObject>> allQueries = new ArrayList<>();
            allQueries.add(firstUserChatQuery);
            allQueries.add(secondUserChatQuery);


            ParseQuery<ParseObject> myQuery = ParseQuery.or(allQueries);
            myQuery.orderByAscending("createdAt");


            myQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (objects.size() > 0 && e == null){
                        for (ParseObject chatObject : objects){
                            String waMessage = chatObject.get("waMessage") + "";
                            if (chatObject.get("waSender").equals(ParseUser.getCurrentUser().getUsername())){
                                waMessage = ParseUser.getCurrentUser().getUsername() + " : " + waMessage;
                            }
                            if (chatObject.get("waSender").equals(selectedUser)){
                                waMessage = selectedUser + " : " + waMessage;
                            }

                            chatsList.add(waMessage);
                        }
                        adapter.notifyDataSetChanged();
                    }
                }
            });
            } catch (Exception e) {
                e.printStackTrace();
            }
                chatsList.clear();
            }

        },0,1000);

    }

    @Override
    public void onClick(View view) {
        final EditText edtSend = findViewById(R.id.edtSend);

        ParseObject chat = new ParseObject("Chat");
        chat.put("waSender", ParseUser.getCurrentUser().getUsername());
        chat.put("waTargetRecipient", selectedUser);
        chat.put("waMessage", edtSend.getText().toString());
        chat.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null){
                    chatsList.add(ParseUser.getCurrentUser().getUsername() + " : " + edtSend.getText().toString());
                    adapter.notifyDataSetChanged();
                    edtSend.setText("");
                }
            }
        });
    }
}