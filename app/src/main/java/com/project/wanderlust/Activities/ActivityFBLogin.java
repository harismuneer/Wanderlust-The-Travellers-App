package com.project.wanderlust.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.project.wanderlust.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public class ActivityFBLogin extends ActionBarMenu {

    private CallbackManager callbackManager;

    private LoginButton loginButton;
    private String userId;

    static public  String firstName,lastName;
    static public URL profilePicture;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(this);


        setContentView(R.layout.activity_fb_login);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        com.facebook.AccessToken token = com.facebook.AccessToken.getCurrentAccessToken();

        //Already Logged In
        if (token != null)
        {
            Intent main = new Intent(ActivityFBLogin.this,ActivityFBPage.class);

            main.putExtra("name",firstName);
            main.putExtra("surname",lastName);

            if (profilePicture != null) {
                main.putExtra("imageUrl", profilePicture.toString());
            }

            startActivity(main);
            finish();
        }


        callbackManager = CallbackManager.Factory.create();

        loginButton = (LoginButton) findViewById(R.id.login_button);


        FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>()
        {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {

                        try
                        {
                            userId = object.getString("id");
                            profilePicture = new URL("https://graph.facebook.com/" + userId + "/picture?width=500&height=500");
                            if(object.has("first_name"))
                                firstName = object.getString("first_name");
                            if(object.has("last_name"))
                                lastName = object.getString("last_name");

                            Intent main = new Intent(ActivityFBLogin.this,ActivityFBPage.class);

                            main.putExtra("name",firstName);
                            main.putExtra("surname",lastName);
                            main.putExtra("imageUrl",profilePicture.toString());

                            startActivity(main);
                            finish();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                    }
                });

                //Here we put the requested fields to be returned from the JSONObject
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, first_name, last_name");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException e) {
                e.printStackTrace();
            }
        };


        loginButton.setReadPermissions("email", "user_birthday", "user_posts");
        loginButton.registerCallback(callbackManager, callback);
    }


    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent intent)
    {
        super.onActivityResult(requestCode, responseCode, intent);
        callbackManager.onActivityResult(requestCode, responseCode, intent);
    }
}
