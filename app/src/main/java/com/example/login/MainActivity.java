package com.example.login;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private ImageView imageView;
    private Button btSave;
    private Button btRead;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        imageView=findViewById( R.id.picture);
        btSave =findViewById( R.id.save );
        btRead = findViewById( R.id.read );

        btSave.setOnClickListener( this );
        btRead.setOnClickListener( this );




    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            //写入
            case R.id.save :
                  saveToSD("picture.jpg");

                break;

            //读取
            case R.id.read:
                   readToSD("y.jpg");

                break;

        }


    }

    //读取
    private void readToSD(String f) {

  if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
      if (ContextCompat.checkSelfPermission( this,Manifest.permission.READ_EXTERNAL_STORAGE) !=
              PackageManager.PERMISSION_GRANTED){

          ActivityCompat.requestPermissions( this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                  2 );
      }


      String path = Environment.getExternalStoragePublicDirectory( "" ).getPath()
              +File.separator
              +Environment.DIRECTORY_PICTURES;
      File file = new File( path,f );

      try {
          //2、创建file的文件输入流
          FileInputStream inputStream = new FileInputStream( file );

          //3、将文件写入imageView
          imageView.setImageBitmap( BitmapFactory.decodeStream( inputStream ));

          //4、关闭文件输入流
          inputStream.close();

      } catch (IOException e) {
          e.printStackTrace();
      }


  }




    }

//写入sd
    private void saveToSD(String filename) {

        //1、申请写入SD的权限，要求Android版本大于6.0(Build.VERSION_CODES.M)
   if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){

        if (ContextCompat.checkSelfPermission( this, Manifest.permission.WRITE_EXTERNAL_STORAGE )!=
             PackageManager.PERMISSION_GRANTED){
                 ActivityCompat.requestPermissions( this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE} ,
                 1);

                 return;

     }
       String path = Environment.getExternalStoragePublicDirectory( "" ).getPath()
               +File.separator
               +Environment.DIRECTORY_PICTURES;
       File file = new File( path,filename );

       try {
           if (file.createNewFile()){

               BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
               Bitmap bitmap = drawable.getBitmap();

               FileOutputStream outputStream = new FileOutputStream( file );

               bitmap.compress( Bitmap.CompressFormat.JPEG,100,outputStream );

               outputStream.flush();
               outputStream.close();

           }
       } catch (IOException e) {
           e.printStackTrace();
       }

   }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult( requestCode, permissions, grantResults );
        if (grantResults.length==0||grantResults[0]!=PackageManager.PERMISSION_GRANTED){

            Toast.makeText( this,"权限申请被拒绝",Toast.LENGTH_LONG ).show();
            return;
        }

        switch (requestCode){
            case 1:
                saveToSD( "picture.jpg" );
                break;
            case 2:
                readToSD( "y.jpg" );

        }

    }


}
