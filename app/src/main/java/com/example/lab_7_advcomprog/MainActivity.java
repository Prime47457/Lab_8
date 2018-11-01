package com.example.lab_7_advcomprog;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;



public class MainActivity extends AppCompatActivity {
    AccessTokenTracker accessTokenTracker;
    AccessToken accessToken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        accessToken = AccessToken.getCurrentAccessToken();

        callbackManager = CallbackManager.Factory.create();

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {
                // Set the access token using
                // currentAccessToken when it's loaded or set.

            }
        };
        LoginButton authButton = this.findViewById(R.id.login_button);
        authButton.setReadPermissions(Arrays.asList("user_status", "user_posts"));



    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        accessTokenTracker.stopTracking();
    }
    CallbackManager callbackManager = CallbackManager.Factory.create();
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void onPost(View view){
        accessToken = AccessToken.getCurrentAccessToken();
        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        TextView editText = findViewById(R.id.textView2);
                        String string1 = "";
                        try {
                            Toast.makeText(MainActivity.this, "Getting ID:" + object.getString("id"),
                            Toast.LENGTH_SHORT).show();
                            JSONArray postDataArray = object.getJSONObject("posts").getJSONArray("data");
                            for( int postIdx = 0; postIdx < postDataArray.length(); postIdx++) {
                                JSONObject post = postDataArray.getJSONObject(postIdx);
                                if (post.has("message")) {
                                    string1 = string1 + post.getString("id")
                                            + " : " + post.getString("message").substring(0, 3) + "\n";
                                }
                            }
                            editText.setText(string1);
                        }catch (JSONException e) {
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "posts");
        request.setParameters(parameters);
        request.executeAsync();

    }
}
