package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.DexterBuilder;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements Playable {
    private ListView listView ;
    private String[] items;
    private NotificationManager notificationManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.listViewSong);
        runtimePermission();

    }



    public void runtimePermission(){
        Dexter.withContext(this).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        displaySong();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();

                    }
                }).check();

    }
    public ArrayList<File> findsong(File file){
        ArrayList<File> arrayList = new ArrayList<>();
        File[] files = file.listFiles();

        for (File sigleFile: files){
            if (sigleFile.isDirectory() && !sigleFile.isHidden()){
                arrayList.addAll(findsong(sigleFile));
            } else {
                if (sigleFile.getName().endsWith(".mp3") ){
                    arrayList.add(sigleFile);
                }
            }
        }
        return arrayList;
    }
    public void displaySong(){
        final ArrayList<File> mysongs = findsong(Environment.getExternalStorageDirectory());
        items = new String[mysongs.size()];

        for (int i =0 ;i<mysongs.size();i++){
            items[i] = mysongs.get(i).getName().toString().replace(".mp3","");

            customAdapter customAdapter = new customAdapter();
            listView.setAdapter(customAdapter);
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                createChanel();
            }
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String songName = (String) listView.getItemAtPosition(i);
                    startActivity(new Intent(getApplicationContext(),player_Activity.class)
                            .putExtra("songs",mysongs)
                            .putExtra("songname",songName)
                            .putExtra("pos",i));
                    CreateNotification.createNotification(MainActivity.this,items[i],R.drawable.pause,1,items[i]);
                }
            });
        }
    }
    private void createChanel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(CreateNotification.CHANNEL_ID,"MusicPlayer", NotificationManager.IMPORTANCE_LOW);
            notificationManager = getSystemService(NotificationManager.class);

        }
    }
    @Override
    public void onTrackPrevious() {

    }

    @Override
    public void onTrackPlay() {

    }

    @Override
    public void onTrackPause() {

    }

    @Override
    public void voidTrackNext() {

    }

    public class customAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return items.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View myView = getLayoutInflater().inflate(R.layout.single_list_item,null) ;
            TextView textsong = myView.findViewById(R.id.title);
            textsong.setSelected(true);
            textsong.setText(items[i]);

            return  myView;
        }

    }


}