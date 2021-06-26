package com.example.wallpaper;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.IOError;
import java.io.IOException;

public class FullScreenWallpaper extends AppCompatActivity {


    String originalUrl;
    PhotoView photoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_wallpaper);
        Intent intent=getIntent();
        originalUrl=intent.getStringExtra("originalUrl");
        photoView=findViewById(R.id.photoView);
        Glide.with(this).load(originalUrl).into(photoView);
        getSupportActionBar().hide();
    }

    public void setWallPaperMethod(View view) {
        WallpaperManager wallpaperManager=WallpaperManager.getInstance(this);
        Bitmap bitmap=((BitmapDrawable)photoView.getDrawable()).getBitmap();

        try{
               wallpaperManager.setBitmap(bitmap);
               toast("Setting wallpaper...");

        }catch (IOException s)
        {
           toast(s.toString());
        }

    }

    public void downloadWallPaperMethod(View view) {
        DownloadManager downloadManager=(DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri=Uri.parse(originalUrl);
        DownloadManager.Request request=new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        downloadManager.enqueue(request);
        toast("Downloading...");
    }

    private void toast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}