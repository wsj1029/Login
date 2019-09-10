package com.example.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {


    private EditText userName;
    private EditText passWord;

    private Button  login;
    private CheckBox remember;
    private String  fileName="login.txt";





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_login );

        userName=findViewById( R.id.user_name );
        passWord=findViewById( R.id.pass_word );

        login=findViewById( R.id.login );
        login.setOnClickListener( this );

        remember = findViewById( R.id.remember );

        //获取存储的用户信息，若有则写入
//        String username = readprfs();

//        String username = readDateInternal(fileName);

        String username=readDatePrivate(fileName);

        if (username!=null){
            userName.setText( username );
        }

    }


    //内部存储
    //保存
    private void saveDateInternal(String fileName, String username) {

        //内部存储目录 ：data/data/包名/files
        try {
            //1、打开文件输出流
            FileOutputStream out = openFileOutput( fileName, Context.MODE_PRIVATE );
            //2、创建BufferedWriter对象
            BufferedWriter writer = new BufferedWriter( new OutputStreamWriter( out ) );
            //3、写入数据
            writer.write( username );
            //4、关闭输出流
            writer.close();


        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    //读取
    private String readDateInternal(String fileName) {
        String data= null;
        try {

            FileInputStream in = openFileInput( fileName );

            BufferedReader reader = new BufferedReader( new InputStreamReader( in ) );

            data =  reader.readLine(  );

            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


        return data;
    }


    //外部存私有存储
    private void saveDatePrivate(String fileName, String username) {

        try {
            //1、打开文件输出流
            File file = new File( getExternalFilesDir( "" ),fileName );//path+fileNam
            //          FileOutputStream out = openFileOutput( fileName, Context.MODE_PRIVATE );
            FileOutputStream out = new FileOutputStream( file );
            //2、创建BufferedWriter对象
            BufferedWriter writer = new BufferedWriter( new OutputStreamWriter( out ) );
            //3、写入数据
            writer.write( username );
            //4、关闭输出流
            writer.close();


        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    //外读取
    private String readDatePrivate(String fileName) {
        String data= null;

        try {

            File file = new File( getExternalFilesDir( "" ),fileName );
//            FileInputStream in = openFileInput( fileName );

            FileInputStream in = new FileInputStream( file );
            BufferedReader reader = new BufferedReader( new InputStreamReader( in ) );

            data =  reader.readLine(  );

            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


        return data;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.login:

                //获取输入的用记名密码

                String username = userName.getText().toString();

                String password = passWord.getText().toString();

                //判断用户名是否勾选，若已选则存储户名，未选则清空存储的用户名
                if (remember.isChecked()){
                    //勾选
                    savePref(username);
                    saveDateInternal(fileName,username);
                    saveDatePrivate(fileName,username);
                }else{
                    //未勾选
                     clearPref();
                }

                //判断用户名密码
                if (username.equals( "meng" ) && password.equals( "123" )) {

                    Intent intent = new Intent( LoginActivity.this,MainActivity.class );
                     startActivity( intent );
                    Toast.makeText( this, "登录成功", Toast.LENGTH_SHORT ).show();


                }else{

                    Toast.makeText( this,"登录失败",Toast.LENGTH_LONG ).show();

                }


                break;

        }
    }



    //清空用户名
    private void clearPref() {

        SharedPreferences.Editor editor = getSharedPreferences( "userInfo",MODE_PRIVATE ).
                edit();
            editor.clear().apply();
    }

       //存储用户名
    private void savePref(String username) {

        SharedPreferences.Editor editor = getSharedPreferences( "userInfo",MODE_PRIVATE ).
                edit();

        editor.putString( "username",username );
        editor.apply();
    }
    private String readprfs() {

        SharedPreferences sp = getSharedPreferences( "userInfo",MODE_PRIVATE );

        return sp.getString( "username","" );

    }
}
