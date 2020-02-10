package org.lineageos.jelly;

import android.Manifest;
import android.app.SearchManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.lineageos.jelly.utils.TabUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class LaunchFileActivity extends AppCompatActivity {

    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();

        if (intent != null && intent.getAction() != null) {

            if (intent.getAction().equals(Intent.ACTION_SEND)) {
                url = intent.getStringExtra(Intent.EXTRA_TEXT);
                int indexOfUrl = url.toLowerCase().indexOf("http");
                if (indexOfUrl == -1)
                    finish();
                else {
                    String containsURL = url.substring(indexOfUrl);

                    int endOfUrl = containsURL.indexOf(" ");

                    if (endOfUrl != -1) {
                        url = containsURL.substring(0, endOfUrl);
                    } else {
                        url = containsURL;
                    }
                }
                TabUtils.openInNewTab(this, url, true);
            }else if (intent.getAction().equals(Intent.ACTION_PROCESS_TEXT)
                    && intent.getStringExtra(Intent.EXTRA_PROCESS_TEXT) != null){
                TabUtils.openInNewTab(this, intent.getStringExtra(Intent.EXTRA_PROCESS_TEXT), true);
            }else if (intent.getAction().equals(Intent.ACTION_WEB_SEARCH)
                    && intent.getStringExtra(SearchManager.QUERY) != null){
                TabUtils.openInNewTab(this, intent.getStringExtra(SearchManager.QUERY), true);
            } else if (intent.getScheme() != null &&
                    (intent.getScheme().equals("content")
                            || intent.getScheme().equals("file"))) {
                if (intent.getScheme().equals("content")) {
                    File f = new File(getBaseContext().getCacheDir(), intent.getData().getLastPathSegment().replace(":", "").replace("/", "."));
                    try {
                        FileOutputStream fos = new FileOutputStream(f);
                        InputStream input = getBaseContext().getContentResolver().openInputStream(intent.getData());
                        byte[] buffer = new byte[1024 * 4];
                        int n = 0;
                        while (-1 != (n = input.read(buffer))) {
                            fos.write(buffer, 0, n);
                        }
                    } catch (IOException | NullPointerException e) {
                        Log.e("errro", e.toString());
                    }
                    url = "file:///" + f.getPath();
                } else {
                    url = intent.getDataString();
                }
                if (!hasStoragePermissionRead()) {
                    //finish();
                } else {
                    Toast.makeText(this, "permission READ_storage granted", Toast.LENGTH_LONG).show();
                }
                //TabUtils.openInNewTab(this, url, true);
            }
        }
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "permission validation", Toast.LENGTH_LONG).show();
            TabUtils.openInNewTab(this, url, true);
        } else {
            Toast.makeText(this, "permission READ_storage DENIED" , Toast.LENGTH_LONG).show();
            ActivityCompat.finishAffinity(this);
        }
    }

    private boolean hasStoragePermissionRead() {
        if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)){
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }else ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            return false;
        } else {
            TabUtils.openInNewTab(this, url, true);
        } return true;
    }
}
