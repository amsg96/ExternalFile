package com.example.novinpendar.externalfile;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    EditText fileName,folderName, content;
    TextView path;
    Button save,load;
    String file_name,folder_name,file_content;
    File extDirectory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fileName = (EditText) findViewById(R.id.edtFileName);
        folderName= (EditText) findViewById(R.id.edtFolderName);
        content = (EditText) findViewById(R.id.edtContent);
        path = (TextView) findViewById(R.id.txtShowPath);
        save = (Button) findViewById(R.id.btnSave);
        load= (Button) findViewById(R.id.btnLoad);


        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
            }
            else {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},100);
            }
        }


        extDirectory = Environment.getExternalStorageDirectory();
        path.setText(extDirectory.getAbsolutePath());




        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                file_name = fileName.getText().toString().trim();
                folder_name = folderName.getText().toString().trim();
                if (file_name.isEmpty()){
                    fileName.setError("Wrong File Name");
                    fileName.requestFocus();
                    return;
                }
                File fDir = new File(extDirectory,folder_name);
                content.setText(readExternalFole(fDir,file_name));
            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                file_name = fileName.getText().toString().trim();
                folder_name = folderName.getText().toString().trim();
                file_content = content.getText().toString().trim();

                if (file_name.isEmpty()){
                    fileName.setError("Wrong File Name");
                    fileName.requestFocus();
                    return;
                }
                if (file_content.isEmpty()){
                    content.setError("no Content");
                    content.requestFocus();
                    return;
                }

                File fDir = new File(extDirectory,folder_name);
                createExternalFile(fDir,file_name,file_content);
            }
        });

    }

    private String readExternalFole(File fDir,String file_name) {
        file_name = file_name.replace(' ','_');

        File file = new File(fDir,file_name);

        if (!file.exists()){
            Toast.makeText(this, "File Not Found!", Toast.LENGTH_SHORT).show();
            return "";

        }

        try {
            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream reader = new BufferedInputStream(fis);

            StringBuilder stringBuilder = new StringBuilder();

            while (reader.available()!=0){
                stringBuilder.append((char)reader.read());
            }

            reader.close();
            fis.close();
            return stringBuilder.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }


    private void createExternalFile(File fDir,String file_name, String file_content) {
        if (!fDir.exists())
            fDir.mkdirs();

        file_name = file_name.replace(' ','_');

        File file = new File(fDir,file_name);

        if (file.exists())
            file.delete();

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(file_content.getBytes());
            fileOutputStream.close();
            Toast.makeText(this, "file Created.", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
