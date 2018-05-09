package com.example.chat;

import android.*;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.renderscript.Sampler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private String sdPath = Environment.getExternalStorageDirectory().getPath() + "/log2.txt";

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public static void verifyStoragePermissions(Activity activity) {
// Check if we have read or write permission
        int writePermission = ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readPermission = ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.READ_EXTERNAL_STORAGE);

        if (writePermission != PackageManager.PERMISSION_GRANTED || readPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }



    /*private String userId;
    public class Todo {
        private String title;
        private Boolean isDone;

        public Todo(String title, Boolean isDone){
            this.title = title;
            this.isDone = isDone;
        }
        public String getTitle(){
            return title;
        }
        public Boolean isDone(){
            return isDone;
        }
        public void setDone(){
            this.isDone = true;
        }

        @Exclude
        public Map<String, Object> toMap(){
            HashMap<String, Object> hashmap = new HashMap<>();
            hashmap.put("title", title);
            hashmap.put("isDone", isDone);
            return hashmap;
        }
    }
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView textView = (TextView) findViewById(R.id.textview);
        final TextView textview = (TextView) findViewById(R.id.textView);
        verifyStoragePermissions(this);
        // Write a message to the database
         //Firebase myFirebaseRef = new Firebase("https://chat-e6583.firebaseio.com/");
        // FirebaseDatabase database = FirebaseDatabase.getInstance();
       // myFirebaseRef.child("message").setValue("Do you have data? You'll love Firebase.");
        DatabaseReference messageRef = FirebaseDatabase.getInstance().getReference("message");

        Log.d(TAG,"Firebase");

        // Read from the database
        messageRef.child("lat").addValueEventListener(new ValueEventListener() {
            /*@Override
            public void onDataAdded(DataSnapshot dataSnapshot, String s) {
                String key = dataSnapshot.getKey();
               // String foo = dataSnapshot.getValue(String.class);
                String lat = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " );
               // Log.d("Firebase", String.format("sender:%s, body:%s", foo, lat));
               // String title = (String) dataSnapshot.child("message").getValue();
               // Boolean isDone = (Boolean) dataSnapshot.child("isDone").getValue();

                String str2 = lat;
                int result3 = str2.length();
               // Log.d("Firebase", String.format("sender:%s, body:%s", str2, lat));

                if (result3 == 3) {
                    Log.d(TAG, "fight" );
                    textView.setText(String.valueOf(lat));
                    Log.d(TAG, "postTransaction:onComplete:");
                }

                // 追加されたTodoのkey、title、isDoneが取得できているので、
                // 保持しているデータの更新や描画処理を行う。
            }
*/
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String lat = dataSnapshot.getValue(String.class);

                //String str2 = value;
                //int result3 = str2.length();
               // textView.setText(String.valueOf(foo));
                //if (result3 == 2) {
                    Log.d(TAG, "fight" );
                    textView.setText(String.valueOf(lat));

                    Log.d(TAG, "postTransaction:onComplete:");
                //SDカードの状態を取得
                String sdCardState = Environment.getExternalStorageState();
                //上で取得したSDカードの状態毎に処理を分離
                //書き込み処理が可能な場合
                if (sdCardState.equals(Environment.MEDIA_MOUNTED)) {
                    String str = "緯度:" +String.valueOf(lat) ;
                    try {
                        FileOutputStream fos = new FileOutputStream(sdPath,true);
                        //trueを帰して追記可能に
                        OutputStreamWriter osw = new OutputStreamWriter(fos);
                        BufferedWriter bw = new BufferedWriter(osw);
                        bw.write(str.toCharArray());
                        //http://www.c-lang.net/java-lang/2016/06/02/%E3%83%90%E3%83%83%E3%83%95%E3%82%A1%E5%8C%96%E3%81%97%E3%81%A6%E5%8A%B9%E7%8E%87char%E6%9B%B8%E3%81%8D%E8%BE%BC%E3%81%BF/
                        bw.newLine();
                        bw.flush();
                        bw.close();

                        //Toast.makeText(MapsActivity.this, str + "\n登録しました。", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, "登録できませんでした。SDカードを確認してください。", Toast.LENGTH_LONG).show();
                    }
                    //SDカードが読取専用の場合
                } else if (sdCardState.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
                    Toast.makeText(MainActivity.this, "このSDカードは読取専用です。", Toast.LENGTH_LONG).show();
                    //SDカードが挿入されていない場合
                } else if (sdCardState.equals(Environment.MEDIA_REMOVED)) {
                    Toast.makeText(MainActivity.this, "SDカードが挿入されていません。", Toast.LENGTH_LONG).show();
                    //SDカードがマウントされていない場合
                } else if (sdCardState.equals(Environment.MEDIA_UNMOUNTED)) {
                    Toast.makeText(MainActivity.this, "SDカードがマウントされていません。", Toast.LENGTH_LONG).show();
                    //その他の場合
                } else {
                    Toast.makeText(MainActivity.this, "SDカードを確認してください。", Toast.LENGTH_LONG).show();
                }



                                                          // }
              //  Log.d("Firebase", String.format("sender:%s, body:%s", foo, lat));
                // Changed
            }



           /* @Override
            public void onDataRemoved(DataSnapshot dataSnapshot) {
                // Removed
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                // Moved
            }*/

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Error
                Log.d(TAG,"MISS");
            }
        });
        messageRef.child("lon").addValueEventListener(new ValueEventListener() {
            /*@Override
            public void onDataAdded(DataSnapshot dataSnapshot, String s) {
                String key = dataSnapshot.getKey();
               // String foo = dataSnapshot.getValue(String.class);
                String lat = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " );
               // Log.d("Firebase", String.format("sender:%s, body:%s", foo, lat));
               // String title = (String) dataSnapshot.child("message").getValue();
               // Boolean isDone = (Boolean) dataSnapshot.child("isDone").getValue();

                String str2 = lat;
                int result3 = str2.length();
               // Log.d("Firebase", String.format("sender:%s, body:%s", str2, lat));

                if (result3 == 3) {
                    Log.d(TAG, "fight" );
                    textView.setText(String.valueOf(lat));
                    Log.d(TAG, "postTransaction:onComplete:");
                }

                // 追加されたTodoのkey、title、isDoneが取得できているので、
                // 保持しているデータの更新や描画処理を行う。
            }
*/
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String lon = dataSnapshot.getValue(String.class);

                //String str2 = value;
                //int result3 = str2.length();
                // textView.setText(String.valueOf(foo));
                //if (result3 == 2) {
                Log.d(TAG, "fight" );
                textview.setText(String.valueOf(lon));

                Log.d(TAG, "postTransaction:onComplete:");
                //SDカードの状態を取得
                String sdCardState = Environment.getExternalStorageState();
                //上で取得したSDカードの状態毎に処理を分離
                //書き込み処理が可能な場合
                if (sdCardState.equals(Environment.MEDIA_MOUNTED)) {
                    String str = "経度:" +String.valueOf(lon) ;
                    try {
                        FileOutputStream fos = new FileOutputStream(sdPath,true);
                        //trueを帰して追記可能に
                        OutputStreamWriter osw = new OutputStreamWriter(fos);
                        BufferedWriter bw = new BufferedWriter(osw);
                        bw.write(str.toCharArray());
                        //http://www.c-lang.net/java-lang/2016/06/02/%E3%83%90%E3%83%83%E3%83%95%E3%82%A1%E5%8C%96%E3%81%97%E3%81%A6%E5%8A%B9%E7%8E%87char%E6%9B%B8%E3%81%8D%E8%BE%BC%E3%81%BF/
                        bw.newLine();
                        bw.flush();
                        bw.close();

                        //Toast.makeText(MapsActivity.this, str + "\n登録しました。", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, "登録できませんでした。SDカードを確認してください。", Toast.LENGTH_LONG).show();
                    }
                    //SDカードが読取専用の場合
                } else if (sdCardState.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
                    Toast.makeText(MainActivity.this, "このSDカードは読取専用です。", Toast.LENGTH_LONG).show();
                    //SDカードが挿入されていない場合
                } else if (sdCardState.equals(Environment.MEDIA_REMOVED)) {
                    Toast.makeText(MainActivity.this, "SDカードが挿入されていません。", Toast.LENGTH_LONG).show();
                    //SDカードがマウントされていない場合
                } else if (sdCardState.equals(Environment.MEDIA_UNMOUNTED)) {
                    Toast.makeText(MainActivity.this, "SDカードがマウントされていません。", Toast.LENGTH_LONG).show();
                    //その他の場合
                } else {
                    Toast.makeText(MainActivity.this, "SDカードを確認してください。", Toast.LENGTH_LONG).show();
                }

                // }
                //  Log.d("Firebase", String.format("sender:%s, body:%s", foo, lat));
                // Changed
            }



           /* @Override
            public void onDataRemoved(DataSnapshot dataSnapshot) {
                // Removed
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                // Moved
            }*/

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Error
                Log.d(TAG,"MISS");
            }
        });

        // Read from the database
        messageRef.child("roll").addValueEventListener(new ValueEventListener() {
            /*@Override
            public void onDataAdded(DataSnapshot dataSnapshot, String s) {
                String key = dataSnapshot.getKey();
               // String foo = dataSnapshot.getValue(String.class);
                String lat = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " );
               // Log.d("Firebase", String.format("sender:%s, body:%s", foo, lat));
               // String title = (String) dataSnapshot.child("message").getValue();
               // Boolean isDone = (Boolean) dataSnapshot.child("isDone").getValue();

                String str2 = lat;
                int result3 = str2.length();
               // Log.d("Firebase", String.format("sender:%s, body:%s", str2, lat));

                if (result3 == 3) {
                    Log.d(TAG, "fight" );
                    textView.setText(String.valueOf(lat));
                    Log.d(TAG, "postTransaction:onComplete:");
                }

                // 追加されたTodoのkey、title、isDoneが取得できているので、
                // 保持しているデータの更新や描画処理を行う。
            }
*/
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String data = dataSnapshot.getValue(String.class);

                //String str2 = value;
                //int result3 = str2.length();
                // textView.setText(String.valueOf(foo));
                //if (result3 == 2) {
                Log.d(TAG, "fight" );
               // textView.setText(String.valueOf(roll));

                Log.d(TAG, "postTransaction:onComplete:");
                //SDカードの状態を取得
                String sdCardState = Environment.getExternalStorageState();
                //上で取得したSDカードの状態毎に処理を分離
                //書き込み処理が可能な場合
                if (sdCardState.equals(Environment.MEDIA_MOUNTED)) {
                    String str = String.valueOf(data) ;
                    try {
                        FileOutputStream fos = new FileOutputStream(sdPath,true);
                        //trueを帰して追記可能に
                        OutputStreamWriter osw = new OutputStreamWriter(fos);
                        BufferedWriter bw = new BufferedWriter(osw);
                        bw.write(str.toCharArray());
                        //http://www.c-lang.net/java-lang/2016/06/02/%E3%83%90%E3%83%83%E3%83%95%E3%82%A1%E5%8C%96%E3%81%97%E3%81%A6%E5%8A%B9%E7%8E%87char%E6%9B%B8%E3%81%8D%E8%BE%BC%E3%81%BF/
                        bw.newLine();
                        bw.flush();
                        bw.close();

                        //Toast.makeText(MapsActivity.this, str + "\n登録しました。", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, "登録できませんでした。SDカードを確認してください。", Toast.LENGTH_LONG).show();
                    }
                    //SDカードが読取専用の場合
                } else if (sdCardState.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
                    Toast.makeText(MainActivity.this, "このSDカードは読取専用です。", Toast.LENGTH_LONG).show();
                    //SDカードが挿入されていない場合
                } else if (sdCardState.equals(Environment.MEDIA_REMOVED)) {
                    Toast.makeText(MainActivity.this, "SDカードが挿入されていません。", Toast.LENGTH_LONG).show();
                    //SDカードがマウントされていない場合
                } else if (sdCardState.equals(Environment.MEDIA_UNMOUNTED)) {
                    Toast.makeText(MainActivity.this, "SDカードがマウントされていません。", Toast.LENGTH_LONG).show();
                    //その他の場合
                } else {
                    Toast.makeText(MainActivity.this, "SDカードを確認してください。", Toast.LENGTH_LONG).show();
                }



                // }
                //  Log.d("Firebase", String.format("sender:%s, body:%s", foo, lat));
                // Changed
            }



           /* @Override
            public void onDataRemoved(DataSnapshot dataSnapshot) {
                // Removed
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                // Moved
            }*/

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Error
                Log.d(TAG,"MISS");
            }
        });
        // Read from the database
        messageRef.child("pitch").addValueEventListener(new ValueEventListener() {
            /*@Override
            public void onDataAdded(DataSnapshot dataSnapshot, String s) {
                String key = dataSnapshot.getKey();
               // String foo = dataSnapshot.getValue(String.class);
                String lat = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " );
               // Log.d("Firebase", String.format("sender:%s, body:%s", foo, lat));
               // String title = (String) dataSnapshot.child("message").getValue();
               // Boolean isDone = (Boolean) dataSnapshot.child("isDone").getValue();

                String str2 = lat;
                int result3 = str2.length();
               // Log.d("Firebase", String.format("sender:%s, body:%s", str2, lat));

                if (result3 == 3) {
                    Log.d(TAG, "fight" );
                    textView.setText(String.valueOf(lat));
                    Log.d(TAG, "postTransaction:onComplete:");
                }

                // 追加されたTodoのkey、title、isDoneが取得できているので、
                // 保持しているデータの更新や描画処理を行う。
            }
*/
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String pitch = dataSnapshot.getValue(String.class);

                //String str2 = value;
                //int result3 = str2.length();
                // textView.setText(String.valueOf(foo));
                //if (result3 == 2) {
                Log.d(TAG, "fight" );
               // textView.setText(String.valueOf(pitch));

                Log.d(TAG, "postTransaction:onComplete:");
                //SDカードの状態を取得
                String sdCardState = Environment.getExternalStorageState();
                //上で取得したSDカードの状態毎に処理を分離
                //書き込み処理が可能な場合
                if (sdCardState.equals(Environment.MEDIA_MOUNTED)) {
                    String str = "ピッチ:" +String.valueOf(pitch) ;
                    try {
                        FileOutputStream fos = new FileOutputStream(sdPath,true);
                        //trueを帰して追記可能に
                        OutputStreamWriter osw = new OutputStreamWriter(fos);
                        BufferedWriter bw = new BufferedWriter(osw);
                        bw.write(str.toCharArray());
                        //http://www.c-lang.net/java-lang/2016/06/02/%E3%83%90%E3%83%83%E3%83%95%E3%82%A1%E5%8C%96%E3%81%97%E3%81%A6%E5%8A%B9%E7%8E%87char%E6%9B%B8%E3%81%8D%E8%BE%BC%E3%81%BF/
                        bw.newLine();
                        bw.flush();
                        bw.close();

                        //Toast.makeText(MapsActivity.this, str + "\n登録しました。", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, "登録できませんでした。SDカードを確認してください。", Toast.LENGTH_LONG).show();
                    }
                    //SDカードが読取専用の場合
                } else if (sdCardState.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
                    Toast.makeText(MainActivity.this, "このSDカードは読取専用です。", Toast.LENGTH_LONG).show();
                    //SDカードが挿入されていない場合
                } else if (sdCardState.equals(Environment.MEDIA_REMOVED)) {
                    Toast.makeText(MainActivity.this, "SDカードが挿入されていません。", Toast.LENGTH_LONG).show();
                    //SDカードがマウントされていない場合
                } else if (sdCardState.equals(Environment.MEDIA_UNMOUNTED)) {
                    Toast.makeText(MainActivity.this, "SDカードがマウントされていません。", Toast.LENGTH_LONG).show();
                    //その他の場合
                } else {
                    Toast.makeText(MainActivity.this, "SDカードを確認してください。", Toast.LENGTH_LONG).show();
                }



                // }
                //  Log.d("Firebase", String.format("sender:%s, body:%s", foo, lat));
                // Changed
            }



           /* @Override
            public void onDataRemoved(DataSnapshot dataSnapshot) {
                // Removed
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                // Moved
            }*/

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Error
                Log.d(TAG,"MISS");
            }
        });

    }
   /* public class Post {

        public String uid;
        public String author;
        public String title;
        public String body;
        public int starCount = 0;
        public Map<String, Boolean> stars = new HashMap<>();

        public Post() {
            // Default constructor required for calls to DataSnapshot.getValue(Post.class)
        }

        public Post(String uid, String author, String title, String body) {
            this.uid = uid;
            this.author = author;
            this.title = title;
            this.body = body;
        }

        @Exclude
        public Map<String, Object> toMap() {
            HashMap<String, Object> result = new HashMap<>();
            result.put("uid", uid);
            result.put("author", author);
            result.put("title", title);
            result.put("body", body);
            result.put("starCount", starCount);
            result.put("stars", stars);

            return result;
        }

    }
    private void onStarClicked(DatabaseReference postRef) {
        postRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Post p = mutableData.getValue(Post.class);
                if (p == null) {
                    return Transaction.success(mutableData);
                }

                if (p.stars.containsKey(getUid())) {
                    // Unstar the post and remove self from stars
                    p.starCount = p.starCount - 1;
                    p.stars.remove(getUid());
                } else {
                    // Star the post and add self to stars
                    p.starCount = p.starCount + 1;
                    p.stars.put(getUid(), true);
                }

                // Set value and report transaction success
                mutableData.setValue(p);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
                // Transaction completed
                Log.d(TAG, "postTransaction:onComplete:" + databaseError);
            }
        });
    }*/
}