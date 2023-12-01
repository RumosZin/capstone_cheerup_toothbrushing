package com.lite.holistic_tracking;

import static android.view.View.GONE;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.lite.holistic_tracking.Database.SeedDB;
import com.lite.holistic_tracking.Database.SeedDao;
import com.lite.holistic_tracking.Entity.Seed;

public class ItemActivity extends AppCompatActivity {

    private ImageView plant;
    private ImageView flower;
    private ImageView tree;
    private String childName;
    private TextView plantView;
    private TextView flowerView;
    private TextView treeView;
    private int flower_num;
    private int plant_num;
    private int tree_num;

    private int flower_count_num;
    private int plant_count_num;
    private int tree_count_num;

    private TextView plantCountView;
    private TextView flowerCountView;
    private TextView treeCountView;
    private TextView requiredSeedView;
    private TextView frontTextView;
    private TextView backTextView;
    private ImageView teeth1;
    private ImageView teeth2;
    private ImageView teeth3;
    private ImageView teeth4;


    private static final String IMAGEVIEW_TAG = "드래그 이미지";
    private MediaPlayer mediaPlayer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forest);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setSubtitle("");

        // Initialize MediaPlayer with the background music
        mediaPlayer = MediaPlayer.create(this, R.raw.background_item_and_shop);
        mediaPlayer.setLooping(true); // Loop the music


        plant = (ImageView) findViewById(R.id.plant);
        flower = (ImageView) findViewById(R.id.flower);
        tree = (ImageView) findViewById(R.id.tree);

        plantView = findViewById(R.id.plantText);
        flowerView = findViewById(R.id.flowerText);
        treeView = findViewById(R.id.treeText);
        
        // 남은 정도를 표시
        plantCountView = findViewById(R.id.plantgage);
        flowerCountView = findViewById(R.id.flowergage);
        treeCountView = findViewById(R.id.treegage);

        frontTextView = findViewById(R.id.fronttext);
        backTextView = findViewById(R.id.backtext);

        // 치아 진화 정도를 표시
        teeth1 = findViewById(R.id.teeth_1);
        teeth2 = findViewById(R.id.teeth_2);
        teeth3 = findViewById(R.id.teeth_3);
        teeth4 = findViewById(R.id.teeth_4);

        plant.setTag("plant");
        flower.setTag("flower");
        tree.setTag("tree");

        requiredSeedView = findViewById(R.id.required_seed);

        plant.setOnLongClickListener(new LongClickListener());
        flower.setOnLongClickListener(new LongClickListener());
        tree.setOnLongClickListener(new LongClickListener());

        findViewById(R.id.toplinear).setOnDragListener(new DragListener());
        findViewById(R.id.teethImage).setOnDragListener(new DragListener());

        // main activity에서 받아온 데이터
        Intent intent = getIntent();
        childName = intent.getStringExtra("childName");


        // 치아 키우기 누르면 무조건 call 됨
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 자녀 정보 가져오는 코드 (getChildByName 메서드 사용)
                //Child child = ChildDB.getInstance(getApplicationContext()).childDao().getChildByName(childName);
                Seed seed = SeedDB.getDatabase(getApplicationContext()).seedDao().getSeedByChildName(childName);
                plant_num = seed.getPlant();
                flower_num = seed.getFlower();
                tree_num = seed.getTree();

                plant_count_num = seed.getTeethPlant();
                flower_count_num = seed.getTeethFlower();
                tree_count_num = seed.getTeethTree();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(seed != null) {

                            // 가지고 있는 plant, flower, tree의 개수 띄우기
                            plantView.setText(String.valueOf(plant_num));
                            flowerView.setText(String.valueOf(flower_num));
                            treeView.setText(String.valueOf(tree_num));


                            // 싱그러움, 향기로움, 강인함의 개수 띄우기
                            plantCountView.setText(String.valueOf(plant_count_num));
                            flowerCountView.setText(String.valueOf(flower_count_num));
                            treeCountView.setText(String.valueOf(tree_count_num));

                            // 싱, 향, 강의 개수에 따라서 치아 강해지도록 하기
                            // 총 30개
                            // 10개 - teeth2
                            // 20개 - teeth3
                            // 30개 - teeth4
                            int total_count = plant_count_num + flower_count_num + tree_count_num;

                            if(total_count >= 30) {
                                requiredSeedView.setText("진화 완료!");
                                // 진화 완료 글씨 띄우기
                                frontTextView.setVisibility(GONE);
                                backTextView.setVisibility(GONE);
                            }

                            if (total_count < 30) {
                                teeth4.setVisibility(GONE);

                                int result_count = 30 - total_count;
                                requiredSeedView.setText(String.valueOf(result_count)); // 20개 이상이면 30에서 빼야 함
                            }

                            if (total_count < 20) {
                                teeth3.setVisibility(GONE);

                                int result_count = 20 - total_count;
                                requiredSeedView.setText(String.valueOf(result_count)); // 10개 이상이면 20에서 빼야 함
                            }

                            if (total_count < 10) {
                                teeth2.setVisibility(GONE); // 10개 이하이면 꾸미기 이미지 안나옴

                                int result_count = 10 - total_count;
                                requiredSeedView.setText(String.valueOf(result_count));
                            }




//                            int total_count = plant_count_num + flower_count_num + tree_count_num;
//
//                            if(total_count < 10){
//                                int result_count = 10 - total_count;
//                                requiredSeedView.setText(String.valueOf(result_count));
//                            }
//                            if (total_count >= 10) {
//                                teeth2.setVisibility(View.VISIBLE); // 치아 위에 꾸미기
//
//                                int result_count = 20 - total_count;
//                                requiredSeedView.setText(String.valueOf(result_count)); // 10개 이상이면 20에서 빼야 함
//                            }
//                            if (total_count >= 20) { // 오른팔
//                                teeth3.setVisibility(View.VISIBLE);
//
//                                int result_count = 30 - total_count;
//                                requiredSeedView.setText(String.valueOf(result_count)); // 20개 이상이면 30에서 빼야 함
//                            }
//                            if (total_count >= 30) { // 왼팔
//                                // 30개 이상 먹이를 주면 왼팔 나오고 진화 완료임
//                                teeth4.setVisibility(View.VISIBLE); // 팔 다 나오고 진화 완료 이미지
//
//                                // 진화 완료 글씨 띄우기
//                                frontTextView.setVisibility(GONE);
//                                backTextView.setVisibility(GONE);
//                                requiredSeedView.setText("진화 완료!");
//                            }

                        }
                    }
                });
            }
        }).start();
    }

    private final class LongClickListener implements View.OnLongClickListener {

        public boolean onLongClick(View view) {
            // 태그 생성
            ClipData.Item item = new ClipData.Item((CharSequence) view.getTag());

            String[] mimeTypes = { ClipDescription.MIMETYPE_TEXT_PLAIN };
            ClipData data = new ClipData(view.getTag().toString(), mimeTypes, item);
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);

            view.startDragAndDrop(data, // data to be dragged
                    shadowBuilder, // drag shadow
                    view, // 드래그 드랍할  Vew
                    0 // 필요없는 플래그
            );

            // view.setVisibility(View.INVISIBLE);
            return true;
        }
    }

    class DragListener implements View.OnDragListener {
        Drawable normalShape = getResources().getDrawable(R.color.white);
        Drawable targetShape = getResources().getDrawable(R.color.blue_small);

        public boolean onDrag(View v, DragEvent event) {

            // 이벤트 시작
            switch (event.getAction()) {

                // 이미지를 드래그 시작될때
                case DragEvent.ACTION_DRAG_STARTED:
                    Log.d("DragClickListener", "ACTION_DRAG_STARTED");
                    break;

                // 드래그한 이미지를 옮길려는 지역으로 들어왔을때
                case DragEvent.ACTION_DRAG_ENTERED:
                    Log.d("DragClickListener", "ACTION_DRAG_ENTERED");
                    // 이미지가 들어왔다는 것을 알려주기 위해 배경이미지 변경
                    //v.setBackground(targetShape);
                    break;

                // 드래그한 이미지가 영역을 빠져 나갈때
                case DragEvent.ACTION_DRAG_EXITED:
                    Log.d("DragClickListener", "ACTION_DRAG_EXITED");
                    //v.setBackground(normalShape);
                    break;

                // 이미지를 드래그해서 드랍시켰을때
                case DragEvent.ACTION_DROP:
                    Log.d("DragClickListener", "ACTION_DROP");
                    View view = (View) event.getLocalState();

                    if (v == findViewById(R.id.teethImage)) {
                        
                        // drop 가능한지 확인하기
                        if(canDrop(view.getTag().toString())) {
                            // 복제된 이미지 생성
                            ImageView clonedImageView = new ImageView(ItemActivity.this);
                            clonedImageView.setImageDrawable(((ImageView) view).getDrawable());
                            clonedImageView.setTag(view.getTag()); // 태그 복사

                            //LinearLayout containView = (LinearLayout) v;
                            FrameLayout containView = (FrameLayout) v;
                            // Reset visibility of the original view
                            view.setVisibility(View.VISIBLE);

                            // UI에서 상단 개수 감소, 하단 개수 증가
                            if (view.getTag().equals("flower")) {
                                flower_num--;
                                flower_count_num++;
                                flowerView.setText(String.valueOf(flower_num));
                                flowerCountView.setText(String.valueOf(flower_count_num));
                            } else if (view.getTag().equals("plant")) {
                                plant_num--;   // plant를 하나 줄임
                                plant_count_num++;
                                plantView.setText(String.valueOf(plant_num));
                                plantCountView.setText(String.valueOf(plant_count_num));
                            } else if (view.getTag().equals("tree")) {
                                tree_num--;    // tree를 하나 줄임
                                tree_count_num++;
                                treeView.setText(String.valueOf(tree_num));
                                treeCountView.setText(String.valueOf(tree_count_num));
                            }

                            // 10개, 20개, 30개가 된다면 visibility 수정
                            // 싱, 향, 강의 개수에 따라서 치아 강해지도록 하기
                            // 총 30개
                            // 10개 - teeth2
                            // 20개 - teeth3
                            // 30개 - teeth4
                            int total_count = plant_count_num + flower_count_num + tree_count_num;

                            if(total_count < 10){
                                int result_count = 10 - total_count;
                                requiredSeedView.setText(String.valueOf(result_count));
                            }
                            if (total_count >= 10) {
                                teeth2.setVisibility(View.VISIBLE); // 치아 위에 꾸미기

                                int result_count = 20 - total_count;
                                requiredSeedView.setText(String.valueOf(result_count)); // 10개 이상이면 20에서 빼야 함
                            }
                            if (total_count >= 20) { // 오른팔
                                teeth3.setVisibility(View.VISIBLE);

                                int result_count = 30 - total_count;
                                requiredSeedView.setText(String.valueOf(result_count)); // 20개 이상이면 30에서 빼야 함
                            }
                            if (total_count >= 30) { // 왼팔
                                // 30개 이상 먹이를 주면 왼팔 나오고 진화 완료임
                                teeth4.setVisibility(View.VISIBLE); // 팔 다 나오고 진화 완료 이미지

                                // 진화 완료 글씨 띄우기
                                frontTextView.setVisibility(GONE);
                                backTextView.setVisibility(GONE);
                                requiredSeedView.setText("진화 완료!");
                            }


                            // DB에서 갯수 떨어뜨리기
                            new Thread(new Runnable() {
                                @Override
                                public void run() {

                                    SeedDB seedDB = SeedDB.getDatabase(getApplicationContext());
                                    SeedDao seedDao = seedDB.seedDao();
                                    Seed seed = seedDao.getSeedByChildName(childName);

                                    if (seed != null) {
                                        int plantCount = seed.getPlant();
                                        int flowerCount = seed.getFlower();
                                        int treeCount = seed.getTree();

                                        int teethPlant = seed.getTeethPlant();
                                        int teethFlower = seed.getTeethFlower();
                                        int teethTree = seed.getTeethTree();

                                        // 여기서 원하는 작업을 수행하고 데이터베이스 업데이트
                                        // 예를 들어, flower를 드랍했을 경우
                                        if (view.getTag().equals("flower")) {
                                            flowerCount--;  // flower를 하나 줄임
                                            teethFlower++;
                                        } else if (view.getTag().equals("plant")) {
                                            plantCount--;   // plant를 하나 줄임
                                            teethPlant++;
                                        } else if (view.getTag().equals("tree")) {
                                            treeCount--;    // tree를 하나 줄임
                                            teethTree++;
                                        }

                                        // 업데이트된 개수를 데이터베이스에 반영
                                        seedDao.updatePlantCount(childName, plantCount);
                                        seedDao.updateFlowerCount(childName, flowerCount);
                                        seedDao.updateTreeCount(childName, treeCount);

                                        seedDao.updateTeethPlantCount(childName, teethPlant);
                                        seedDao.updateTeethFlowerCount(childName, teethFlower);
                                        seedDao.updateTeethTreeCount(childName, teethTree);
                                    }
                                }
                            }).start();

                            String imageName = view.getTag().toString();
                            int iconResourceId = R.drawable.app_icon_final; // 기본 아이콘 리소스 ID
                            String message = "";

                            if(imageName.equals("plant")) {
                                message = "~새싹의 싱그러운 기운~";
                                iconResourceId = R.drawable.plant_image; // 식물 아이콘 리소스 ID
                            } else if(imageName.equals("flower")) {
                                message = "~꽃의 향기로운 기운~";
                                iconResourceId = R.drawable.flower_image; // 꽃 아이콘 리소스 ID
                            } else if(imageName.equals("tree")) {
                                message = "~나무의 강인한 기운~";
                                iconResourceId = R.drawable.tree_image; // 나무 아이콘 리소스 ID
                            }

                            CustomToast.showCustomToast(getApplicationContext(), message, iconResourceId);

                        } else {
                            // 드랍할 수 없는 경우
                            String imageName = view.getTag().toString();
                            if(imageName == "plant") {
                                Toast.makeText(getApplicationContext(), "양치를 해서 새싹을 모아줘.", Toast.LENGTH_SHORT).show();
                            }
                            else if(imageName == "flower") {
                                Toast.makeText(getApplicationContext(), "양치를 해서 꽃을 모아줘.", Toast.LENGTH_SHORT).show();
                            }
                            else if(imageName == "tree") {
                                Toast.makeText(getApplicationContext(), "양치를 해서 나무를 모아줘.", Toast.LENGTH_SHORT).show();
                            }
                        }


                    }else if (v == findViewById(R.id.toplinear)) {
                        // top linear에는 drop 할 수 없음

                    }
                    else {
                        //View view = (View) event.getLocalState();
                        view.setVisibility(View.VISIBLE);
                        Context context = getApplicationContext();
                        Toast.makeText(context,
                                "이미지를 다른 지역에 드랍할수 없습니다.",
                                Toast.LENGTH_LONG).show();
                        break;
                    }
                    break;

                case DragEvent.ACTION_DRAG_ENDED:
                    Log.d("DragClickListener", "ACTION_DRAG_ENDED");
                    //v.setBackground(normalShape); // go back to normal shape

                default:
                    break;
            }
            return true;
        }

    }

    private boolean canDrop(String tag) {
        // 여기에서 tag에 따라서 canDrop 여부를 판단하고 반환
        if (tag.equals("flower")) {
            return flower_num > 0;
        } else if (tag.equals("plant")) {
            return plant_num > 0;
        } else if (tag.equals("tree")) {
            return tree_num > 0;
        }
        return false;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed(); // 뒤로가기 동작 수행
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Start playing background music when the activity resumes
        mediaPlayer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Pause background music when the activity is paused
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Release resources when the activity is destroyed
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}