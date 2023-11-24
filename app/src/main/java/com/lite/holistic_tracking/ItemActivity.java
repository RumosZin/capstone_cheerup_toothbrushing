package com.lite.holistic_tracking;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.lite.holistic_tracking.Database.ChildDB;
import com.lite.holistic_tracking.Database.ForestDB;
import com.lite.holistic_tracking.Database.ForestDao;
import com.lite.holistic_tracking.Entity.Child;
import com.lite.holistic_tracking.Entity.Forest;

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

    private static final String IMAGEVIEW_TAG = "드래그 이미지";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forest);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setSubtitle("");


        plant = (ImageView) findViewById(R.id.plant);
        flower = (ImageView) findViewById(R.id.flower);
        tree = (ImageView) findViewById(R.id.tree);

        plantView = findViewById(R.id.plantText);
        flowerView = findViewById(R.id.flowerText);
        treeView = findViewById(R.id.treeText);

        plant.setTag("plant");
        flower.setTag("flower");
        tree.setTag("tree");

        plant.setOnLongClickListener(new LongClickListener());
        flower.setOnLongClickListener(new LongClickListener());
        tree.setOnLongClickListener(new LongClickListener());

        findViewById(R.id.toplinear).setOnDragListener(new DragListener());
        findViewById(R.id.teethImage).setOnDragListener(new DragListener());

        // main activity에서 받아온 데이터
        Intent intent = getIntent();
        childName = intent.getStringExtra("childName");


        // 자녀 정보 가져오기
        // main menu 화면으로 올 때 무조건 call됨
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 자녀 정보 가져오는 코드 (getChildByName 메서드 사용)
                //Child child = ChildDB.getInstance(getApplicationContext()).childDao().getChildByName(childName);
                Forest forest = ForestDB.getDatabase(getApplicationContext()).forestDao().getForestByChildName(childName);
                plant_num = forest.getPlant();
                flower_num = forest.getFlower();
                tree_num = forest.getTree();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(forest != null) {
                            plantView.setText(String.valueOf(plant_num));
                            flowerView.setText(String.valueOf(flower_num));
                            treeView.setText(String.valueOf(tree_num));
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
                    0 // 필요없은 플래그
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

//                            // change the text
//                            TextView text = (TextView) v.findViewById(R.id.text);
//                            text.setText("이미지가 드랍되었습니다.");

                            LinearLayout containView = (LinearLayout) v;
                            // Reset visibility of the original view
                            view.setVisibility(View.VISIBLE);

                            // UI에서 개수 감소

                            if (view.getTag().equals("flower")) {
                                flower_num--;
                                flowerView.setText(String.valueOf(flower_num));
                            } else if (view.getTag().equals("plant")) {
                                plant_num--;   // plant를 하나 줄임
                                plantView.setText(String.valueOf(plant_num));
                            } else if (view.getTag().equals("tree")) {
                                tree_num--;    // tree를 하나 줄임
                                treeView.setText(String.valueOf(tree_num));
                            }

                            // UI에서 plant, flower, tree 갯수 떨어뜨리기
                            new Thread(new Runnable() {
                                @Override
                                public void run() {

                                    ForestDB forestDB = ForestDB.getDatabase(getApplicationContext());
                                    ForestDao forestDao = forestDB.forestDao();
                                    Forest forest = forestDao.getForestByChildName(childName);

                                    if (forest != null) {
                                        int plantCount = forest.getPlant();
                                        int flowerCount = forest.getFlower();
                                        int treeCount = forest.getTree();

                                        // 여기서 원하는 작업을 수행하고 데이터베이스 업데이트
                                        // 예를 들어, flower를 드랍했을 경우
                                        if (view.getTag().equals("flower")) {
                                            flowerCount--;  // flower를 하나 줄임
                                        } else if (view.getTag().equals("plant")) {
                                            plantCount--;   // plant를 하나 줄임
                                        } else if (view.getTag().equals("tree")) {
                                            treeCount--;    // tree를 하나 줄임
                                        }

                                        // 업데이트된 개수를 데이터베이스에 반영
                                        forestDao.updatePlantCount(childName, plantCount);
                                        forestDao.updateFlowerCount(childName, flowerCount);
                                        forestDao.updateTreeCount(childName, treeCount);
                                    }
                                }
                            }).start();
                            // 토스트 메시지 표시
                            String imageName = view.getTag().toString();
                            Toast.makeText(getApplicationContext(), "<" + imageName + ">를 드랍했다!", Toast.LENGTH_SHORT).show();


                        } else {
                            // 드랍할 수 없는 경우
                            Toast.makeText(getApplicationContext(), "더 이상 드랍할 수 없습니다.", Toast.LENGTH_SHORT).show();
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
}