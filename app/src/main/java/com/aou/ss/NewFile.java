package com.aou.ss;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import com.blakequ.rsa.FileEncryptionManager;
import com.blakequ.rsa.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

public class NewFile extends AppCompatActivity {
    FileEncryptionManager mFileEncryptionManager;
    String publicKey="d6f5g4sr68gs8r4g8s4rg";
    String privateKey="s35fr4a6e8r4fa84rf86va4er";
    Integer PICKFILE_RESULT_CODE=1234;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_file);
        mFileEncryptionManager = FileEncryptionManager.getInstance();
        AppCompatButton chooseFile=findViewById(R.id.chooseFile);
    }

    public void select(View v) {
        Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFile.setType("*/*");
        chooseFile = Intent.createChooser(chooseFile, "Choose a file");
        startActivityForResult(chooseFile, PICKFILE_RESULT_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==PICKFILE_RESULT_CODE && requestCode== Activity.RESULT_OK){
            Uri uri = data.getData();
            String src = uri.getPath();
            try {
                mFileEncryptionManager.setRSAKey(publicKey, privateKey, false);
                byte[] data1 = FileUtils.getBytesFromInputStream(new FileInputStream(src));

                File file=new File(Environment.getExternalStorageDirectory()+"/folderName");

                if (!file.exists())
                {
                    file.mkdirs();
                }



                File myappFile=new File(file
                        + File.separator + "myapp.bin");


                byte[] result = mFileEncryptionManager.encryptFileByPublicKey(data1,myappFile );
            }catch (Exception e){
                Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
            }
        }
    }
}