package com.ubuphailin.lenovo.connection;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends ActionBarActivity {


    @BindView(R.id.userName) EditText editTextUserName;
    @BindView(R.id.userPassword) EditText editTextPassword;
    @BindView(R.id.btn_connect) Button btn_login;

    public static final String USER_NAME = "USERNAME";

    private String username ="";
    private String password ="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        editTextUserName = (EditText) findViewById(R.id.userName);
        editTextPassword = (EditText) findViewById(R.id.userPassword);
        btn_login =(Button)findViewById(R.id.btn_connect);


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                username = editTextUserName.getText().toString();
                password = editTextPassword.getText().toString();

                if (username.length() <= 0) {
                    editTextUserName.setError("กรุณากรอกชื่อผู้ใช้");
                    return;
                }
                if (password.length() <= 0) {
                    editTextPassword.setError("กรุณากรอกรหัสผ่าน");
                    return;
                }
                login(username, password);
            }

            private void login(final String username, String password) {

                class LoginAsync extends AsyncTask<String, Void, String>{

                    private Dialog loadingDialog;

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        loadingDialog = ProgressDialog.show(MainActivity.this, "กรุณารอสักครู่", "กำลังโหลด...");
                    }

                    @Override
                    protected String doInBackground(String... params) {
                        String username2 = params[0];
                        String password2 = params[1];

                        InputStream is = null;
                        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                        nameValuePairs.add(new BasicNameValuePair("username", username2));
                        nameValuePairs.add(new BasicNameValuePair("password", password2));
                        String result = null;

                        try{
                            HttpClient httpClient = new DefaultHttpClient();
                            HttpPost httpPost = new HttpPost(
                                    "http://logvehicle.esy.es/connection.php");
                            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                            HttpResponse response = httpClient.execute(httpPost);

                            HttpEntity entity = response.getEntity();

                            is = entity.getContent();

                            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
                            StringBuilder sb = new StringBuilder();

                            String line = null;
                            while ((line = reader.readLine()) != null)
                            {
                                sb.append(line + "\n");
                            }
                            result = sb.toString();
                        } catch (ClientProtocolException e) {
                            e.printStackTrace();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return result;
                    }

                    @Override
                    protected void onPostExecute(String result){
                        String s = result.trim();
                        loadingDialog.dismiss();


                        if(s.equalsIgnoreCase("success")){
                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                            intent.putExtra(USER_NAME, username);
                            finish();
                            startActivity(intent);
                        }else {
                            Toast.makeText(getApplicationContext(), "ชื่อผู้ใช้หรือรหัสผ่านไม่ถูกต้อง", Toast.LENGTH_LONG).show();
                        }
                    }
                }

                LoginAsync la = new LoginAsync();
                la.execute(username, password);

            }});}


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}