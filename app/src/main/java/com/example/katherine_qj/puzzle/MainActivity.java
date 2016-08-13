package com.example.katherine_qj.puzzle;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private final String ALBUM_PATH = Environment.getExternalStorageDirectory().getPath() + "/logo";
    private final String ALBUM_ALL_PATH = Environment.getExternalStorageDirectory().getPath() + "/logo/logo.png";
    public static final int CHOICE_PHOTO = 1;
    public static final int CROP_PHOTO = 2;
    private Button SettingBtn;
    private Button BeginBtn;
    private Button PapaBtn;
    private Button LevelBtn;
    private Button MusicBtn;
    private Button newpapaBtn;
    private Bitmap bitmap_papa;
    private Button ReBtn;
    private Button back;
    private TextView weizhi;
    private MyAPP myAPP;
    private ContentResolver resolver;
    private Uri originalUri;
    private Uri imageUri;
    private ListView listView_pa;
    private int level = 3;
    private int flag_stu =1;
    private Boolean isGameStart = false;
    private Boolean isLeve = false;
    //1代表当前是go  2 代表当前是三角  3 代表当前是||
    private Boolean ismusic=false;
    private GridLayout MainGame_min;
    private GridLayout MainGame_middle;
    private LinearLayout linearLayout_photo;
    private PopupWindow popupwindow_setting;
    private PopupWindow Popupwindow_photo;
    private PopupWindow Popupwindow_success;
    private WindowManager windowManager;
    private Animation operatingAnim;
    private int Screen_width;
    private int Sreen_height;
    private ImageView[][] min_game_arr = new ImageView[3][3];
    private ImageView[][] middle_game_arr = new ImageView[4][4];
    private ImageView[][] max_game_arr = new ImageView[5][5];

    private SoundPool soundPool;
    private int sound;
    private float soundLoad;


    private GestureDetector gestureDetector;


    private ImageView main_nullImage;
    private ImageView Lose_view;


    private List<PaPa> list = new ArrayList<>();
    private PaPaAdapter papaAdapter;

    private View customView;
    private View customView_photo;
    private View customView_success;
    private int[] location;


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        return gestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        gestureDetector.onTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gestureDetector = new GestureDetector(this, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                changeByDir( getDirByGes(e1.getX(), e1.getY(), e2.getX(), e2.getY()));
                //手势执行一瞬间的方法操作

                //   Toast.makeText(MainActivity.this,""+getDirByGes(e1.getX(),e1.getY(),e2.getX(),e2.getY()),Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        setContentView(R.layout.activity_main);
        soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
        sound = soundPool.load(this, R.raw.pa, 1);
        InitView();
        MinGradeGame();
        SettingBtn.setOnClickListener(this);
        PapaBtn.setOnClickListener(this);
        BeginBtn.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.seeting:
                SettingBtn.startAnimation(operatingAnim);
                if (popupwindow_setting != null && popupwindow_setting.isShowing()) {
                    popupwindow_setting.dismiss();
                    return;
                } else {
                    InitPopupWindowsViewSetting();
                    popupwindow_setting.showAsDropDown(v);

                }
                break;
            case R.id.photo:
                if (Popupwindow_photo != null && Popupwindow_photo.isShowing()) {
                    linearLayout_photo.setBackgroundColor(Color.parseColor("#ffffff"));
                    Popupwindow_photo.dismiss();
                    return;
                } else {
                    InitPopupWindowsViewPhoto();
                    Popupwindow_photo.showAsDropDown(v);
                    linearLayout_photo.setBackgroundColor(Color.parseColor("#a6b3c3"));

                }
                break;
            case R.id.difficulty:
                if (level == 3) {
                    level = 4;
                    MainGame_min.setVisibility(View.GONE);
                    MainGame_middle.setVisibility(View.VISIBLE);
                    LevelBtn.setBackgroundResource(R.mipmap.level4);
                } else {
                    level = 3;
                    MainGame_middle.setVisibility(View.GONE);
                    MainGame_min.setVisibility(View.VISIBLE);
                    LevelBtn.setBackgroundResource(R.mipmap.level3);

                }
                ChangePaPaDifficuty();
                break;
            case R.id.music:
                if (ismusic==false){
                    MusicBtn.setBackgroundResource(R.mipmap.music_on);
                    ismusic=true;
                }else if (ismusic==true){
                    MusicBtn.setBackgroundResource(R.mipmap.music_off);
                    ismusic=false;

                }
                break;
            case R.id.new_papa:
                flag_stu =1;
                isGameStart = false;
                isLeve = false;
                BeginBtn.setBackgroundResource(R.mipmap.go);
                levelBtn();
                OpenAbumlAndCrop();

                break;
            case R.id.begin:
                if (flag_stu==1) {
                    Bitmap bitmap = myAPP.getBitmap();
                    InitPaPaGame(bitmap);
                    BeginBtn.setBackgroundResource(R.mipmap.re);
                    flag_stu = 2;
                    levelBtn();
                }else if (flag_stu==2) {
                        randomMove(15);
                        BeginPaPaGame();
                        levelBtn();
                        ReBtn();
                    }
                break;
            case R.id.back:
                Popupwindow_success.dismiss();
                isGameStart=false;
                isLeve=false;
                ChangePaPaDifficuty();
                flag_stu=1;
                BeginBtn.setBackgroundResource(R.mipmap.go);
                levelBtn();
                ReBtn();
                break;



        }
    }


    public void InitView() {

        SettingBtn = (Button) findViewById(R.id.seeting);
        BeginBtn = (Button) findViewById(R.id.begin);
        PapaBtn = (Button) findViewById(R.id.photo);
        weizhi = (TextView)findViewById(R.id.weizhi);
         location = new int[2];
        weizhi.getLocationOnScreen(location);
        MainGame_min = (GridLayout) findViewById(R.id.main_game_min);
        MainGame_middle = (GridLayout) findViewById(R.id.main_game_middle);
        windowManager = this.getWindowManager();
        Screen_width = windowManager.getDefaultDisplay().getWidth();
        Sreen_height = windowManager.getDefaultDisplay().getHeight();
        linearLayout_photo = (LinearLayout) findViewById(R.id.linearlayout_photo);
        papaAdapter = new PaPaAdapter(MainActivity.this,list);

        AudioManager audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        //获取当前系统音量
        float qian = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        //获取系统最大音量
        float max = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        //计算得到播放音量
        soundLoad = max;
        //设置播放文件的音量

        operatingAnim = AnimationUtils.loadAnimation(this, R.anim.rotate);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
        myAPP = new MyAPP();
        resolver = getContentResolver();
        bitmap_papa = ((BitmapDrawable) getResources().getDrawable(R.mipmap.papa_game)).getBitmap();
        myAPP.setBitmap(bitmap_papa);
        customView = getLayoutInflater().inflate(R.layout.setting_pop, null, false);
        ReBtn = (Button)customView.findViewById(R.id.reset);
        ReBtn.setOnClickListener(this);
        LevelBtn = (Button) customView.findViewById(R.id.difficulty);
        MusicBtn = (Button) customView.findViewById(R.id.music);
        MusicBtn.setOnClickListener(this);
        LevelBtn.setOnClickListener(this);
        customView_photo = getLayoutInflater().inflate(R.layout.photo_pop, null, false);
        customView_success = getLayoutInflater().inflate(R.layout.success_pop,null,false);
        back = (Button)customView_success.findViewById(R.id.back);
        back.setOnClickListener(this);
        newpapaBtn = (Button) customView_photo.findViewById(R.id.new_papa);
        listView_pa = (ListView)customView_photo.findViewById(R.id.listview_pa);
        listView_pa.setAdapter(papaAdapter);
        newpapaBtn.setOnClickListener(this);
        ReBtn();
    }

    public void InitPopupWindowsViewSetting() {
       /* View customView = getLayoutInflater().inflate(R.layout.setting_pop, null, false);
        LevelBtn = (Button) customView.findViewById(R.id.difficulty);
        MusicBtn = (Button) customView.findViewById(R.id.music);
        MusicBtn.setOnClickListener(this);
        LevelBtn.setOnClickListener(this);*/
        popupwindow_setting = new PopupWindow(customView, Screen_width / 7, Sreen_height);
        popupwindow_setting.setTouchable(true);
        popupwindow_setting.setFocusable(true);
        popupwindow_setting.setBackgroundDrawable(new BitmapDrawable());
        popupwindow_setting.setOutsideTouchable(true);
        popupwindow_setting.setAnimationStyle(R.style.AnimationFade);
    }

    public void InitPopupWindowsViewPhoto() {
        /*View customView_photo = getLayoutInflater().inflate(R.layout.photo_pop, null, false);
        newpapaBtn = (Button) customView_photo.findViewById(R.id.new_papa);
        listView_pa = (ListView)customView_photo.findViewById(R.id.listview_pa);
        listView_pa.setAdapter(papaAdapter);
        newpapaBtn.setOnClickListener(this);*/
        Popupwindow_photo = new PopupWindow(customView_photo, Screen_width / 6, Sreen_height);
        Popupwindow_photo.setAnimationStyle(R.style.AnimationFade_pa);
    }
    public void InitPopupWindowsViewSuccess(){
        Popupwindow_success = new PopupWindow(customView_success, Screen_width, Sreen_height);
        Popupwindow_success.setAnimationStyle(R.style.AnimationFade_success);
        Popupwindow_success.showAtLocation(weizhi, Gravity.NO_GRAVITY, location[0], location[1] - Popupwindow_success.getHeight());
    }


    public void MinGradeGame() {
        Bitmap big = myAPP.getBitmap();
        //  Bitmap big = ((BitmapDrawable)getResources().getDrawable(R.mipmap.papa_game)).getBitmap();
        Bitmap BB = zoomImg(big, 900, 900);
        int tuWandH = BB.getHeight() / 3;
        for (int i = 0; i < min_game_arr.length; i++) {
            for (int j = 0; j < min_game_arr[0].length; j++) {
                Bitmap bm = Bitmap.createBitmap(BB, j * tuWandH, i * tuWandH, tuWandH, tuWandH);
                min_game_arr[i][j] = new ImageView(this);
                min_game_arr[i][j].setImageBitmap(bm);//设置每一个小方块的图案
                min_game_arr[i][j].setPadding(2, 2, 2, 2);//设置方块之间的间距
            }
        }
        MainGame_min.removeAllViews();
        for (int i = 0; i < min_game_arr.length; i++) {
            for (int j = 0; j < min_game_arr[0].length; j++) {
                MainGame_min.addView(min_game_arr[i][j]);
            }

        }

    }

    public void MiddleGradeGame() {
        Bitmap big = myAPP.getBitmap();
        // Bitmap big = ((BitmapDrawable)getResources().getDrawable(R.mipmap.papa_game)).getBitmap();
        Bitmap BB = zoomImg(big, 900, 900);
        int tuWandH = BB.getHeight() / 4;
        for (int i = 0; i < middle_game_arr.length; i++) {
            for (int j = 0; j < middle_game_arr[0].length; j++) {
                Bitmap bm = Bitmap.createBitmap(BB, j * tuWandH, i * tuWandH, tuWandH, tuWandH);
                middle_game_arr[i][j] = new ImageView(this);
                middle_game_arr[i][j].setImageBitmap(bm);//设置每一个小方块的图案
                middle_game_arr[i][j].setPadding(2, 2, 2, 2);//设置方块之间的间距
            }
        }
        MainGame_middle.removeAllViews();
        for (int i = 0; i < middle_game_arr.length; i++) {
            for (int j = 0; j < middle_game_arr[0].length; j++) {
                MainGame_middle.addView(middle_game_arr[i][j]);
            }

        }

    }

    public void ChangePaPaDifficuty() {
        if (level == 3) {
            MinGradeGame();
        } else if (level == 4) {
            MiddleGradeGame();
        }


    }


    //手动改变图片大小来控制显示的大小
    public static Bitmap zoomImg(Bitmap bm, int newWidth, int newHeight) {
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
       /* BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        newbm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        InputStream sbs = new ByteArrayInputStream(baos.toByteArray());
        newbm=BitmapFactory.decodeStream(sbs, null, opt);*/
        return newbm;
    }

    public void OpenAbumlAndCrop() {

        Intent intent = new Intent();
                /* 开启Pictures画面Type设定为image */
        intent.setType("image/*");
                /* 使用Intent.ACTION_GET_CONTENT这个Action */
        intent.setAction(Intent.ACTION_GET_CONTENT);
                /* 取得相片后返回本画面 */
        startActivityForResult(intent, CHOICE_PHOTO);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent_pa) {
        switch (requestCode) {
            case CHOICE_PHOTO :
                if (resultCode == RESULT_OK) {

                    File outputImage=new File(Environment.getExternalStorageDirectory(),"logo1.jpg");
                    try
                    {
                        if(outputImage.exists())
                        {
                            outputImage.delete();
                        }
                        outputImage.createNewFile();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    imageUri=Uri.fromFile(outputImage);


                    originalUri = intent_pa.getData(); //获得图片的uri
                        Log.e("url", originalUri + "");

                    try
                    {
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        savePicture(bitmap);

                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    Intent intent=new Intent("com.android.camera.action.CROP");
                    intent.setType("image");
                    intent.putExtra("crop", false);//允许裁剪
                    intent.putExtra("scale", false);//允许缩放
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    intent.setDataAndType(originalUri, "image");
                    startActivityForResult(intent, CROP_PHOTO);

            }
                break;
            case CROP_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                         Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                         myAPP.setBitmap(bitmap);
                         PaPa paPa = new PaPa(bitmap);
                         list.add(paPa);
                         ChangePaPaDifficuty();
                         BeginBtn.setVisibility(View.VISIBLE);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                }
        }

    }
    public void savePicture(Bitmap bitmap) {
        String pictureName = "/mnt/sdcard/" + "newpapa" + ".jpg";
        File file = new File(pictureName);
        FileOutputStream out;
        try {
            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void InitPaPaGame(Bitmap bitmap_papagame) {
        isLeve=true;
        if (level == 3) {
            Bitmap BB = zoomImg(bitmap_papagame, 900, 900);
            int tuWandH = BB.getHeight() / 3;
            for (int i = 0; i < min_game_arr.length; i++) {
                for (int j = 0; j < min_game_arr[0].length; j++) {
                    Bitmap bm = Bitmap.createBitmap(BB, j * tuWandH, i * tuWandH, tuWandH, tuWandH);
                    //根据行列切成若干个小方块
                    min_game_arr[i][j] = new ImageView(getApplication());
                    min_game_arr[i][j].setImageBitmap(bm);//设置每一个小方块的图案
                    min_game_arr[i][j].setPadding(2, 2, 2, 2);//设置方块之间的间距
                    min_game_arr[i][j].setTag(new GameDate(i, j, bm));//绑定自定义的数据
                }

            }
            MainGame_min.removeAllViews();
            Lose_view = min_game_arr[0][0];
            for (int i = 0; i < min_game_arr.length; i++) {
                for (int j = 0; j < min_game_arr[0].length; j++) {
                    MainGame_min.addView(min_game_arr[i][j]);
                    if (i == 0 && j == 0) {
                        setNullImageView(min_game_arr[0][0]);
                    }
                }

            }
        }
        if (level == 4) {
            Bitmap BB = zoomImg(bitmap_papagame, 900, 900);
            int tuWandH = BB.getHeight() / 4;
            for (int i = 0; i < middle_game_arr.length; i++) {
                for (int j = 0; j < middle_game_arr[0].length; j++) {
                    Bitmap bm = Bitmap.createBitmap(BB, j * tuWandH, i * tuWandH, tuWandH, tuWandH);
                    //根据行列切成若干个小方块
                    middle_game_arr[i][j] = new ImageView(getApplication());
                    middle_game_arr[i][j].setImageBitmap(bm);//设置每一个小方块的图案
                    middle_game_arr[i][j].setPadding(2, 2, 2, 2);//设置方块之间的间距
                    middle_game_arr[i][j].setTag(new GameDate(i, j, bm));//绑定自定义的数据
                }

            }
            MainGame_middle.removeAllViews();
            Lose_view = middle_game_arr[0][0];
            for (int i = 0; i < middle_game_arr.length; i++) {
                for (int j = 0; j < middle_game_arr[0].length; j++) {
                    MainGame_middle.addView(middle_game_arr[i][j]);
                    if (i == 0 && j == 0) {
                        setNullImageView(middle_game_arr[0][0]);
                    }
                }

            }
        }

    }
    /*设置某个方块为缺口方块*/
    public void setNullImageView(ImageView nullImageView) {

        nullImageView.setImageBitmap(null);
        main_nullImage = nullImageView;
    }

    /*  每个游戏小方块的数据
      实际位置x
      实际位置y
      每个方块的图片bm
      每个小方块图片的位置p_x
      每个小方块图片的位置 p_y*/
    class GameDate{
        public int x = 0;
        public  int y = 0;
        public Bitmap bm;
        public  int p_x=0;
        public int p_y=0;
        public GameDate(int x,int y,Bitmap bm){
            this.bm = bm;
            this.x=x;
            this.y = y;
            this.p_x = x;
            this.p_y = y;

        }
        //判断每个小方块的位置是否正确
        public Boolean isTrue(){
            if (x==p_x&&y==p_y){
                return  true;
            }
            return false;
        }


    }
    public void BeginPaPaGame(){
        isGameStart = true;
        if(level==3){
            for (int i = 0; i < min_game_arr.length; i++) {
                for (int j = 0; j < min_game_arr[0].length; j++) {
                    min_game_arr[i][j].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            boolean flag = ifHasNullImageView((ImageView) v);
                            if (flag) {//如果是真的相邻关系就交换
                                changeDataByImageView((ImageView)v,true);
                            }
                        }
                    });
                }
            }
        }if (level==4){
            for (int i = 0; i < middle_game_arr.length; i++) {
                for (int j = 0; j < middle_game_arr[0].length; j++) {
                    middle_game_arr[i][j].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            boolean flag = ifHasNullImageView((ImageView) v);
                            if (flag) {//如果是真的相邻关系就交换
                                changeDataByImageView((ImageView)v,true);
                            }
                        }
                    });
                }
            }
        }
    }

    public boolean ifHasNullImageView(ImageView clickImageView){
        //分别获取当前空方块的位置和点击方块的位置的实例
        GameDate nullImage = (GameDate) main_nullImage.getTag();
        GameDate gameDate = (GameDate)clickImageView.getTag();

        if (nullImage.y==gameDate.y&&nullImage.x==gameDate.x+1){//上面
            return true;

        }else if (nullImage.y==gameDate.y&&nullImage.x==gameDate.x-1){//下面
            return  true;

        }else if (nullImage.y==gameDate.y+1&&nullImage.x==gameDate.x){//左边
            return  true;

        }else if (nullImage.y==gameDate.y-1&&nullImage.x==gameDate.x){//右边
            return  true;

        }
        return  false;
    }
    public void changeDataByImageView(final ImageView imageView){
        changeDataByImageView(imageView,true);
    }

    public  void changeDataByImageView(final ImageView imageView,Boolean istrue){

        if (!istrue){
            GameDate gameDate = (GameDate)imageView.getTag();
            main_nullImage.setImageBitmap(gameDate.bm);
            GameDate nullgamedata = (GameDate)main_nullImage.getTag();
            nullgamedata.bm = gameDate.bm;
            nullgamedata.p_x = gameDate.p_x;
            nullgamedata.p_y = gameDate.p_y;
            setNullImageView(imageView);
            if (isGameStart) {
                isGameOver();
            }
            return;
        }
        TranslateAnimation translateAnimation=null;
        if (imageView.getX()>main_nullImage.getX()){//根据点击位置和空方块的位置来设置动画
            translateAnimation = new TranslateAnimation(0.1f,-imageView.getHeight(),0.1f,0.1f);
        }else if(imageView.getX()<main_nullImage.getX()){
            translateAnimation = new TranslateAnimation(0.1f,imageView.getHeight(),0.1f,0.1f);
        }else if(imageView.getY()>main_nullImage.getY()){
            translateAnimation = new TranslateAnimation(0.1f,0.1f,0.1f,-imageView.getHeight());
        }else if(imageView.getY()<main_nullImage.getY()){
            translateAnimation = new TranslateAnimation(0.1f,0.1f,0.1f,imageView.getHeight());
        }
        translateAnimation.setDuration(70);//设置时长
        translateAnimation.setFillAfter(true);//设置是否停留
       /*
        设置动画监听*/
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //动画结束之后开始真正交换数据
                imageView.clearAnimation();
                GameDate gameDate = (GameDate)imageView.getTag();
                main_nullImage.setImageBitmap(gameDate.bm);
                GameDate nullgamedata = (GameDate)main_nullImage.getTag();
                nullgamedata.bm = gameDate.bm;
                nullgamedata.p_x = gameDate.p_x;
                nullgamedata.p_y = gameDate.p_y;
                setNullImageView(imageView);
                if (isGameStart) {
                    isGameOver();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        imageView.startAnimation(translateAnimation);
    }

    //判断游戏结束 在游戏开始之前是不判断的
    public void isGameOver() {
        Boolean isGameOver = true;
        if (level == 3) {
            for (int i = 0; i < min_game_arr.length; i++) {
                for (int j = 0; j < min_game_arr[0].length; j++) {
                    if (min_game_arr[i][j] == main_nullImage) {
                        continue;
                    }
                    GameDate gameDate = (GameDate) min_game_arr[i][j].getTag();
                    if (!gameDate.isTrue()) {
                        isGameOver = false;
                        break;
                    }

                }
            }
        }
        if (level==4){
            for (int i = 0; i < middle_game_arr.length; i++) {
                for (int j = 0; j < middle_game_arr[0].length; j++) {
                    if (middle_game_arr[i][j] == main_nullImage) {
                        continue;
                    }
                    GameDate gameDate = (GameDate) middle_game_arr[i][j].getTag();
                    if (!gameDate.isTrue()) {
                        isGameOver = false;
                        break;
                    }

                }
            }
        }
        if (isGameOver) {

               /* isGameStart=false;
                isLeve=false;
                ChangePaPaDifficuty();
                flag_stu=1;
                 BeginBtn.setBackgroundResource(R.mipmap.go);
                levelBtn();
                ReBtn();*/
                InitPopupWindowsViewSuccess();
                Popupwindow_success.showAsDropDown(weizhi);


        }
    }
    public void changeByDir(int type){
        changeByDir(type, true);
    }
    public void changeByDir(int type,Boolean ishas) {
        if (ismusic==true){
            soundPool.play(sound, soundLoad, soundLoad, 1, 0, 1.0f);
        }
        if (level == 3) {
            GameDate nullGameDate = (GameDate)main_nullImage.getTag();
            int new_x = nullGameDate.x;
            int new_y = nullGameDate.y;
            if (type == 1) {
                new_x++;
            } else if (type == 2) {
                new_x--;
            } else if (type == 3) {
                new_y++;
            } else if (type == 4) {
                new_y--;
            }
            //判断这个新坐标是否存在
            if (new_x >= 0 && new_x < min_game_arr.length && new_y >= 0 && new_y < min_game_arr[0].length) {
                if (ishas) {
                    changeDataByImageView(min_game_arr[new_x][new_y]);
                } else {
                    changeDataByImageView(min_game_arr[new_x][new_y], false);

                }
            }
        }
        if (level==4){
            GameDate nullGameDate = (GameDate)main_nullImage.getTag();
            int new_x = nullGameDate.x;
            int new_y = nullGameDate.y;
            if (type == 1) {
                new_x++;
            } else if (type == 2) {
                new_x--;
            } else if (type == 3) {
                new_y++;
            } else if (type == 4) {
                new_y--;
            }
            //判断这个新坐标是否存在
            if (new_x >= 0 && new_x < middle_game_arr.length && new_y >= 0 && new_y < middle_game_arr[0].length) {
                if (ishas) {
                    changeDataByImageView(middle_game_arr[new_x][new_y]);
                } else {
                    changeDataByImageView(middle_game_arr[new_x][new_y], false);

                }
            }
        }
    }

    /*手势判断 是向哪个方向滑动根据滑动的手势之间的坐标值来决定的 */
    public int getDirByGes(float start_x,float start_y,float end_x,float end_y){
        boolean isLeftorRight = (Math.abs(start_x-end_x)>Math.abs(start_y-end_y))?true:false;
        if(isLeftorRight){
            boolean isLeft = (start_x-end_x)>0?true:false;
            if(isLeft){
                return  3;

            }else{
                return  4;
            }

        }else{
            boolean isup = (start_y-end_y)>0?true:false;
            if(isup){
                return  1;

            }else{
                return  2;

            }
        }

    }
    public void randomMove(int t){

        for (int i = 0;i<t;i++){
            int type =(int)(Math.random()*4)+1;
            changeByDir(type,false);
        }
    }
    public void levelBtn(){
        if (isLeve==true){
            LevelBtn.setClickable(false);
            if (level==3){
                LevelBtn.setBackgroundResource(R.mipmap.level3_off);
            }
            if (level==4){
                LevelBtn.setBackgroundResource(R.mipmap.level4_off);
            }
        }else if (isLeve==false){
            LevelBtn.setClickable(true);
            if (level==3){
                LevelBtn.setBackgroundResource(R.mipmap.level3);
            }
            if (level==4){
                LevelBtn.setBackgroundResource(R.mipmap.level4);
            }
        }
    }
    public void ReBtn(){
        if (isGameStart==true){
            ReBtn.setBackgroundResource(R.mipmap.re);
            ReBtn.setClickable(true);
            Log.e("123","qwe");
        }
        if (isGameStart==false){
            ReBtn.setBackgroundResource(R.mipmap.re_off);
            ReBtn.setClickable(false);
        }
    }
}


