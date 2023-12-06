package com.lite.holistic_tracking;


import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;

import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.lifecycle.ProcessCameraProvider;

import android.util.Log;
import android.util.Size;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.media.MediaPlayer;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.google.mediapipe.components.CameraHelper;
import com.google.mediapipe.components.CameraXPreviewHelper;
import com.google.mediapipe.components.ExternalTextureConverter;
import com.google.mediapipe.components.FrameProcessor;
import com.google.mediapipe.components.PermissionHelper;
import com.google.mediapipe.framework.AndroidAssetUtil;
import com.google.mediapipe.framework.AndroidPacketCreator;
import com.google.mediapipe.framework.Packet;
import com.google.mediapipe.framework.PacketGetter;
import com.google.mediapipe.glutil.EglManager;
import com.lite.holistic_tracking.Database.MorebrushingDB;
import com.lite.holistic_tracking.Database.SongDB;
import com.lite.holistic_tracking.Entity.Child;
import com.lite.holistic_tracking.Entity.Morebrushing;
import com.lite.holistic_tracking.Entity.Song;
import com.lite.holistic_tracking.Entity.Toothbrushing;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.mediapipe.formats.proto.LandmarkProto.NormalizedLandmarkList;

import com.google.mediapipe.formats.proto.LandmarkProto.NormalizedLandmark;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Collections;


public class HolisticActivity extends AppCompatActivity {

    private Song song;
    private ProcessCameraProvider cameraProvider;
    private MediaPlayer mediaPlayer;
    private MediaPlayer mediaPlayer2;
    private Toothbrushing toothbrushing_passing;
    private static final String TAG = "MainActivity";
    private static final String H = "HeeJun";
    private static final String INPUT_NUM_HANDS_SIDE_PACKET_NAME = "num_hands";

    private static final int NUM_HANDS = 2;

    private static final String OUTPUT_FACE_LANDMARKS_STREAM_NAME = "face_landmarks";
    private static final String OUTPUT_HAND_LANDMARKS_STREAM_NAME = "left_hand_landmarks";
//    private Button seedButton;

    // Flips the camera-preview frames vertically by default, before sending them into FrameProcessor
    // to be processed in a MediaPipe graph, and flips the processed frames back when they are
    // displayed. This maybe needed because OpenGL represents images assuming the image origin is at
    // the bottom-left corner, whereas MediaPipe in general assumes the image origin is at the
    // top-left corner.
    // NOTE: use "flipFramesVertically" in manifest metadata to override this behavior.
    private static final boolean FLIP_FRAMES_VERTICALLY = true;

    // 애플리케이션에 필요한 native library를 로드
    static {
        // Load all native libraries needed by the app.
        System.loadLibrary("mediapipe_jni");
        try {
            System.loadLibrary("opencv_java3");
        } catch (UnsatisfiedLinkError e) {
            // Some example apps (e.g. template matching) require OpenCV 4.
            System.loadLibrary("opencv_java4");
        }
    }

    // Sends camera-preview frames into a MediaPipe graph for processing, and displays the processed
    // frames onto a {@link Surface}.
    protected FrameProcessor processor;
    // Handles camera access via the {@link CameraX} Jetpack support library.
    protected CameraXPreviewHelper cameraHelper;

    // {@link SurfaceTexture} where the camera-preview frames can be accessed.
    private SurfaceTexture previewFrameTexture;
    // {@link SurfaceView} that displays the camera-preview frames processed by a MediaPipe graph.
    private SurfaceView previewDisplayView;

    // Creates and manages an {@link EGLContext}.
    private EglManager eglManager;
    // Converts the GL_TEXTURE_EXTERNAL_OES texture from Android camera into a regular texture to be
    // consumed by {@link FrameProcessor} and the underlying MediaPipe graph.
    private ExternalTextureConverter converter;

    // ApplicationInfo for retrieving metadata defined in the manifest.
    private ApplicationInfo applicationInfo;

    private ArrayList<Float> sizearr = new ArrayList<>();
    private ArrayList<Float> c_distances = new ArrayList<>();
    private ArrayList<Float> degs = new ArrayList<>();
    private int sizeofDegs;
    private float average;
    float sum;
//    private ArrayList<Float> checkHeights = new ArrayList<>();
    private int[] action_seq = {0, 0, 0};
    private int numElementsToPrint = 30;
    private boolean inside = false; // used for boolean, can't modify in lambda expression
    private boolean count = false; // used for boolean, can't modify in lambda expression
    int toothbrushing = 0;
    private SurfaceView cameraPreview;
    private TextView overlayText;
    String this_action;
    private ImageView countdownImageView;
    private ImageView effectImageView;
    private ImageView comboImageView;
    private ImageView digit100ImageView;
    private ImageView digit10ImageView;
    private ImageView digit1ImageView;
    private int digitResource;
    private int comboCount = 0;


    private ImageView backgroundImageView;
    private int currentBrushingSection = 0;
    private int brushing0 = 0;
    private int brushing1 = 0;
    private int brushing2 = 0;
    private int brushing3 = 0;
    private int brushing4 = 0;
    private int brushing5 = 0;
    private int brushing6 = 0;
    private int brushing7 = 0;
    private int brushing8 = 0;
    private int brushing9 = 0;
    private int brushing10 = 0;
    private int brushing11 = 0;
    private int brushing12 = 0;
    private int brushing13 = 0;

    /* HeeJun member field */
    private final int[] toothImages = {
            R.drawable.left_circular_image,
            R.drawable.mid_circular_image,
            R.drawable.right_circular_image,
            R.drawable.left_lower_image,
            R.drawable.right_lower_image,
            R.drawable.left_upper_image,
            R.drawable.right_upper_image,
            R.drawable.left_lower_inner_image,
            R.drawable.mid_lower_inner_image,
            R.drawable.right_lower_inner_image,
            R.drawable.left_upper_inner_image,
            R.drawable.mid_upper_inner_image,
            R.drawable.right_upper_inner_image,
    };
    private ImageView toothImageView, toothImageOpened, ballImageView, circularballImageView;

    private boolean stopAnimation = false;
    private int toothIndex = 0;
    private float radius, initialX, initialY;

    private int bpm = 143;
    private final int howManyBeatsPerArea = 16;
    final Handler handler = new Handler();
    SpitTimeDialog spitTimeDialog;

    int toothcount;
    int toothlength;
    // 수정 - 양치 추가시간에 적용될 영역, 여기에 DB에서 정보 받아와야함
    int[] toothIndexes = {}; // 빈 index 설정F
    private boolean morebrushingflag;

    /* HeeJun member field */
    private float score_per_count;

    private String songTitle;
    private Child child;
    private String animalName;
    private int combo = 0;
    private boolean comboflag;
    private String guide_action;

    private final Handler handDetectionHandler = new Handler();
    private Runnable handDetectionRunnable;


    public static float calculateAverage(ArrayList<Float> list) {
        float sum = 0;
        if (!list.isEmpty()) {
            for (Float num : list) {
                sum += num;
            }
            return sum / list.size();
        }
        return sum;
    }

    public class SharedLandmarkData {
        private volatile NormalizedLandmarkList faceLandmarks = null;
        private volatile NormalizedLandmarkList handLandmarks = null;
        private final Object lock = new Object();

        public void updateFaceLandmarks(NormalizedLandmarkList landmarks) {
            synchronized (lock) {
                if(landmarks != null){
                    this.faceLandmarks = landmarks;
                }
            }
        }

        public void updateHandLandmarks(NormalizedLandmarkList landmarks) {
            synchronized (lock) {
                if(landmarks != null){
                    this.handLandmarks = landmarks;
                }
            }
        }

        // 필요에 따라 얼굴 랜드마크 데이터를 가져오는 메서드
        public NormalizedLandmarkList getFaceLandmarks() {
            synchronized (lock) {
                return faceLandmarks;
            }
        }

        // 필요에 따라 손 랜드마크 데이터를 가져오는 메서드
        public NormalizedLandmarkList getHandLandmarks() {
            synchronized (lock) {
                return handLandmarks;
            }
        }
    }

    private int lastScoreForImage = 0;

    private void addMedalImage(int score) {
        LinearLayout medalLayout = findViewById(R.id.paw_medal);
        LinearLayout effectLayout = findViewById(R.id.sparkle_effect);
        int scoreThreshold = 20; // 이미지가 추가될 점수의 기준

        // 새로운 이미지를 추가해야 하는지 확인
        if (score >= lastScoreForImage + scoreThreshold) {
            // 새 ImageView를 생성하고 설정
            ImageView newMedal = new ImageView(this);
            ImageView sparkle_effect = new ImageView(this);
            GlideDrawableImageViewTarget sparkle_effectImage = new GlideDrawableImageViewTarget(sparkle_effect);
            Glide.with(this).load(R.drawable.sparkle_effect).into(sparkle_effectImage);

            newMedal.setImageResource(R.drawable.paw_medal); // 메달 이미지 리소스

            // 여기에서 이미지의 크기를 설정합니다. 예를 들어 100dp x 100dp
            int imageSize = dpToPx(50); // 100dp를 픽셀로 변환
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    imageSize, // width
                    imageSize  // height
            );

            // ImageView를 조금 위로 올리려면 여기에 마진을 추가합니다.
            int bottomMargin = dpToPx(20); // 10dp를 픽셀로 변환
            layoutParams.bottomMargin = bottomMargin;

            // ImageView를 LinearLayout에 추가
            medalLayout.addView(newMedal, 0, layoutParams); // 맨 아래부터 차근차근 추가
            effectLayout.addView(sparkle_effect, 0, layoutParams);

            // 다음 이미지 추가를 위한 점수 업데이트
            lastScoreForImage += scoreThreshold;
        }
    }

    // dp를 픽셀로 변환하는 메서드
    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }


    private float totalScore = 0;
    private ArrayList<Float> trimmedList;
    private float size = 0;
    private OverlayView overlayView;
    private List<NormalizedLandmark> currentLandmarks = Collections.emptyList();
    private void updateLandmarks(List<NormalizedLandmark> landmarks) {
        runOnUiThread(() -> {
            if (overlayView != null) {
                overlayView.setLandmarks(landmarks);
            }
        });
    }

    private void updatePoints(float[][] points){
        runOnUiThread(() -> {
            if(overlayView != null){
                overlayView.setPoints(points, isHandDetected);
                Log.d(TAG, "setPoints");
            }
        });
    }


    private boolean isHandDetected = false;

    // 손 감지 상태를 업데이트하는 메소드
    private void updateHandDetectionStatus(boolean detected) {
        isHandDetected = detected;
    }

    // activity가 생성될 때 호출되는 메서드
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.holistic);

        Log.v("backpress check", "holistic activity's onCreate ** 3 **");
        Log.d("MyTag", "**** START ****");

        // 넘겨 받은 song 정보 / child 정보 / animal 정보
        Intent intent = getIntent();
        this.songTitle = intent.getStringExtra("songTitle");
        Log.d("MyTag", "***** 1 *****");
        Log.v("backpress check", "### 3 ###");

        String childNameIntent = intent.getStringExtra("childName");
        String birthDateIntent = intent.getStringExtra("birthDate");
        String genderIntent = intent.getStringExtra("gender");
        int seedIntent = intent.getIntExtra("seed", 0);
        Log.d("MyTag", "***** 2 *****");
        Log.v("backpress check", "### 4 ###");

        Log.d("MyTag", childNameIntent + " first");
        Log.d("MyTag", birthDateIntent + " first");
        Log.d("MyTag", genderIntent + " first");
        //Log.d("MyTag", String.valueOf(seedIntent) + " first");

        child = new Child();

        child.setChildName(childNameIntent); // intent에서 받은 child 정보로 child 설정
        child.setBirthDate(birthDateIntent);
        child.setGender(genderIntent);
        child.setSeed(seedIntent);
        Log.v("backpress check", "### 5 ###");

        Log.d("MyTag", childNameIntent);

        Log.d("MyTag", "***** 3 *****");

        String animalNameIntent = intent.getStringExtra("animalName");
        this.animalName = animalNameIntent;

        Log.d("MyTag", "***** 4 *****");
        Log.v("backpress check", "### 6 ###");
        
        // MorebrushingDB에서 자녀 이름으로 검색해서 list 가져 와야 함
        new Thread(new Runnable() {
            @Override
            public void run() {

                // 노래 길이 받아옴
                song = SongDB.getInstance(getApplicationContext()).songDao().getSongByTitle(songTitle);

                // MorebrushingDB 확인 - 넘겨 받은 childNameIntent로 검색
                Log.v("backpress check", "### 7 ###");
                Morebrushing morebrushing = MorebrushingDB.getDatabase(getApplicationContext()).morebrushingDao().getMorebrushingByChildName(childNameIntent);
                ArrayList<Integer> tempToothIndexes = new ArrayList<>();

                if (morebrushing != null) {
                    // 필요한 필드 확인 및 처리
                    if (morebrushing.getLeft_circular() == 1) {
                        tempToothIndexes.add(0); // 예시에서는 0번째 tooth에 해당하는 index 추가
                    }

                    if (morebrushing.getMid_circular() == 1) {
                        tempToothIndexes.add(1); // 예시에서는 1번째 tooth에 해당하는 index 추가
                    }

                    if (morebrushing.getRight_circular() == 1) {
                        tempToothIndexes.add(2); // 예시에서는 1번째 tooth에 해당하는 index 추가
                    }

                    if (morebrushing.getLeft_lower() == 1) {
                        tempToothIndexes.add(3); // 예시에서는 1번째 tooth에 해당하는 index 추가
                    }

                    if (morebrushing.getLeft_upper() == 1) {
                        tempToothIndexes.add(5); // 예시에서는 1번째 tooth에 해당하는 index 추가
                    }

                    if (morebrushing.getRight_lower() == 1) {
                        tempToothIndexes.add(4); // 예시에서는 1번째 tooth에 해당하는 index 추가
                    }

                    if (morebrushing.getRight_upper() == 1) {
                        tempToothIndexes.add(6); // 예시에서는 1번째 tooth에 해당하는 index 추가
                    }

                    if (morebrushing.getMid_vertical_lower() == 1) {
                        tempToothIndexes.add(8); // 예시에서는 1번째 tooth에 해당하는 index 추가
                    }

                    if (morebrushing.getMid_vertical_upper() == 1) {
                        tempToothIndexes.add(11); // 예시에서는 1번째 tooth에 해당하는 index 추가
                    }

                    // 나머지 필드들에 대해서도 필요한 처리를 추가할 수 있습니다.
                }
                Log.v("backpress check", "### 8 ###");

                // ArrayList를 배열로 변환
                toothIndexes = new int[tempToothIndexes.size()];
                Log.d("moremore", "tempToothIndexes.size(): " + tempToothIndexes.size());
                for (int i = 0; i < tempToothIndexes.size(); i++) {
                    toothIndexes[i] = tempToothIndexes.get(i);
                    Log.d("moremore", "toothIndexes[i]: " + toothIndexes[i]);

                }


            }
        }).start();

        toothImageView = findViewById(R.id.toothImage);
        ballImageView = findViewById(R.id.ballImage);
        circularballImageView = findViewById(R.id.circularBallImage);
        toothImageOpened = findViewById(R.id.toothImageOpened);
        radius = 50.0f;
        spitTimeDialog = new SpitTimeDialog(HolisticActivity.this);
        toothcount = 0;
//        Log.d("MyTag", "init bpm" + bpm);
        score_per_count = 100/(song.getSongLength() * ((float) song.getBpm() /60)); // 100점, 120 노래길이
        comboflag = false;
        Log.d("score_check", String.valueOf(score_per_count) + ", "+String.valueOf(song.getSongLength()) + ", " + String.valueOf(song.getBpm()));
        comboImageView = findViewById(R.id.combo_image);
        digit100ImageView = findViewById(R.id.digit100);
        digit10ImageView = findViewById(R.id.digit10);
        digit1ImageView = findViewById(R.id.digit1);
        digitResource = R.drawable.digit_0;
        guide_action = "left";



        Log.d("score", "init score_per_count = " + score_per_count);
        Log.d("score", "toothIndexes.length = " + toothIndexes.length);
        if (toothIndexes.length != 0) morebrushingflag = true;
        else morebrushingflag = false;

        Log.d("MyTag", "1. onCreate()");
        Log.d("MyTag", "onCreate() -> toothcount = " + toothcount);

        // AndroidManifest.xml 파일에서 정의된 메타 데이터를 포함
        // 나중에 앱의 동작을 구성하는데 사용됨
        try {
            applicationInfo =
                    getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "Cannot find application info: " + e);
        }



        FrameLayout previewLayout = findViewById(R.id.preview_display_layout);
        overlayView = new OverlayView(this);
        previewLayout.addView(overlayView, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));



        // SurfaceView를 생성하여 previewDisplayView 변수에 할당
        // SurfaceView는 카메라 프리뷰를 표시하는데 사용
        previewDisplayView = new SurfaceView(this);
        setupPreviewDisplayView(); // SurfaceView의 초기화 및 설정 수행
        overlayView.bringToFront();



        // Initialize asset manager so that MediaPipe native libraries can access the app assets, e.g.,
        // binary graphs.
        // MediaPipe의 네이티브 라이브러리가 앱 자산에 액세스 가능하도록 함
        AndroidAssetUtil.initializeNativeAssetManager(this);
        eglManager = new EglManager(null);
        processor =
                new FrameProcessor(
                        this,
                        eglManager.getNativeContext(),
                        applicationInfo.metaData.getString("binaryGraphName"),
                        applicationInfo.metaData.getString("inputVideoStreamName"),
                        applicationInfo.metaData.getString("inputVideoStreamName")
                );

        processor
                .getVideoSurfaceOutput()
                .setFlipY(
                        applicationInfo.metaData.getBoolean("flipFramesVertically", FLIP_FRAMES_VERTICALLY));

        PermissionHelper.checkAndRequestCameraPermissions(this);

        showPreviousDialogs();


        // 여기부터
        Log.d("in onCreate", "1");
        AndroidPacketCreator packetCreator = processor.getPacketCreator();
        Map<String, Packet> inputSidePackets = new HashMap<>();
        inputSidePackets.put(INPUT_NUM_HANDS_SIDE_PACKET_NAME, packetCreator.createInt32(NUM_HANDS));
        processor.setInputSidePackets(inputSidePackets);

        Log.v(TAG, "verbose is active: " + Log.isLoggable(TAG, Log.VERBOSE));
        Log.d(TAG, "debug is active: " + Log.isLoggable(TAG, Log.DEBUG));
        Log.i(TAG, "info is active: " + Log.isLoggable(TAG, Log.INFO));
        Log.w(TAG, "warn is active: " + Log.isLoggable(TAG, Log.WARN));
        Log.e(TAG, "error is active: " + Log.isLoggable(TAG, Log.ERROR));

        // song 이름에 따라서 노래 틀기
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.rabbit);
        if ("뚱보새".equals(songTitle)) {
            Log.v("Mytag", "뚱보새 클릭");
            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.fat_bird_song);
            bpm = 120;
        } else if ("산중호걸".equals(songTitle)) {
            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.mountain_tiger_song);
            bpm = 122;
        } else if ("우유송".equals(songTitle)) {
            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.milk_song);
            bpm = 143;
        } else if ("아기 염소".equals(songTitle)) {
            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.goat_song);
            bpm = 132;
        } else if ("꿈빛 파티시엘".equals(songTitle)) {
            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.party_song);
            bpm = 173;
        } else {
            Log.v("Mytag", songTitle);
        }


        // animal 이름에 따라서 테마 적용
        backgroundImageView = findViewById(R.id.background_image); // 동물 테마 적용하기
        if(animalName.equals("고양이")) {
            backgroundImageView.setImageResource(R.drawable.cat_theme_image);
        }
        else if(animalName.equals("강아지")) {
            backgroundImageView.setImageResource(R.drawable.dog_theme_image);
        }
        else if(animalName.equals("돼지")) {
            backgroundImageView.setImageResource(R.drawable.pig_theme_image);
        }
        else if(animalName.equals("양")) {
            backgroundImageView.setImageResource(R.drawable.sheep_theme_image);
        }
        else if(animalName.equals("여우")) {
            backgroundImageView.setImageResource(R.drawable.fox_theme_image);
        }
        else if(animalName.equals("토끼")) {
            backgroundImageView.setImageResource(R.drawable.rabbit_theme_image);
        }
        else if(animalName.equals("사자")) {
            backgroundImageView.setImageResource(R.drawable.lion_theme_image);
        }
        
        


        // When music ends, this listener will make this dialog open
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.d("MyTag", "1차 노래 멈춤"); // 1차 노래가 여기서 멈췄음
                stopAnimation(); // 1차 애니메이션 종료
                // Tootbrushing 객체에 이번 양치 정보 저장 해야 함
                if(morebrushingflag) moreBrushingDialog(); // 2차 가이드 시작
                else{
                    showAfterDialogs();
                }
                // 마무리 입헹구기 dialog
                // 씨앗 dialog

            }
        });

        // 주기적으로 손 감지 상태를 확인하는 Runnable 정의
        handDetectionRunnable = new Runnable() {
            @Override
            public void run() {
                if (!isHandDetected) {
                    float[] endPointOut = null;
                    float[] p1Out = null;
                    float[][] currentOut = null;
                    updatePoints(currentOut);
                    Log.d("handdetected", "hand is false");

                }
                // 상태를 다시 false로 설정하여 다음 주기에 대비
                isHandDetected = false;
                // Runnable을 일정 간격(예: 100ms)으로 반복
                handDetectionHandler.postDelayed(this, 100);
            }
        };

        // Runnable 시작
        handDetectionRunnable.run();

        float[] endPointOut = null;
        float[] p1Out = null;
        float[][] currentOut = null;
//        updatePoints(currentOut);

        // To show verbose logging, run:
        // adb shell setprop log.tag.MainActivity VERBOSE
//        if (Log.isLoggable(TAG, Log.VERBOSE)) {
        SharedLandmarkData sharedLandmarkData = new SharedLandmarkData();

        processor.addPacketCallback(
                OUTPUT_FACE_LANDMARKS_STREAM_NAME,
                (packet) -> {
                    byte[] landmarksRaw = PacketGetter.getProtoBytes(packet);

                    try {
                        NormalizedLandmarkList landmarks = NormalizedLandmarkList.parseFrom(landmarksRaw);
                        if (landmarks == null) {
                            Log.d(TAG, "[TS:" + packet.getTimestamp() + "] No face landmarks.");
                            return;
                        }
                        sharedLandmarkData.updateFaceLandmarks(landmarks);

//                            NormalizedLandmarkList filteredLandmarks = filterLandmarks(landmarks, desiredFaceIndices);
//                            Log.d(TAG,
//                                    "[TS:" + packet.getTimestamp()
//                                            + "] #Landmarks for face: "
//                                            + landmarks.getLandmarkCount());
//                            Log.d(TAG, getLandmarksDebugString(filteredLandmarks));
                    } catch (InvalidProtocolBufferException e) {
                        Log.e(TAG, "Couldn't Exception received - " + e);
                    }
                });

        processor.addPacketCallback(
                OUTPUT_HAND_LANDMARKS_STREAM_NAME,
                (packet) -> {
                    byte[] landmarksRaw = PacketGetter.getProtoBytes(packet);
                    try {
                        NormalizedLandmarkList landmarks = NormalizedLandmarkList.parseFrom(landmarksRaw);

                        float[] endPoint = endPointOut;
                        float[] p1 = p1Out;
                        float[][] currentPoints = currentOut;

                        if (landmarks == null) {
                            Log.d(TAG, "[TS:" + packet.getTimestamp() + "] No hand landmarks.");
                            return;
                        }
                        Log.d("yejunbug", "landmarks is not null");

//                            StringBuilder landmarksString = new StringBuilder();
//                            landmarksString.append(landmarks.getLandmark(17).getX());


//                        List<NormalizedLandmark> landmarkList = landmarks.getLandmarkList();
//                        currentLandmarks = new ArrayList<>(landmarkList);
//                        updateLandmarks(currentLandmarks);


                        sharedLandmarkData.updateHandLandmarks(landmarks);
                        NormalizedLandmarkList faceLandmarks = sharedLandmarkData.getFaceLandmarks();

                        if (faceLandmarks == null) {
                            Log.d(TAG, "[TS:" + packet.getTimestamp() + "] No face landmarks.");
                            return;
                        }


                        updateHandDetectionStatus(true);

                        float[] face1 = {faceLandmarks.getLandmark(359).getX(), faceLandmarks.getLandmark(359).getY(), faceLandmarks.getLandmark(359).getZ()};
                        float[] face2 = {faceLandmarks.getLandmark(130).getX(), faceLandmarks.getLandmark(130).getY(), faceLandmarks.getLandmark(130).getZ()};
                        float[] midFace = {(face1[0] + face2[0]) / 2,
                                (face1[1] + face2[1]) / 2,
                                (face1[2] + face2[2]) / 2};
//                            Log.d(TAG, String.valueOf(face[0])+", "+String.valueOf(face[1])+", "+String.valueOf(face[2]));
                        if(isHandDetected){
                            Log.d("handdetected", "hand is true");
                        }
                        else{
                            Log.d("handdetected", "hand is false");

                        }
                        float[] select_p1 = {
                                (landmarks.getLandmark(6).getX() + landmarks.getLandmark(5).getX()) / 2,
                                (landmarks.getLandmark(6).getY() + landmarks.getLandmark(5).getY()) / 2,
                                (landmarks.getLandmark(6).getZ() + landmarks.getLandmark(5).getZ()) / 2
                        };
                        float[] select_p2 = {
                                (landmarks.getLandmark(13).getX() + landmarks.getLandmark(14).getX()) / 2,
                                (landmarks.getLandmark(13).getY() + landmarks.getLandmark(14).getY()) / 2,
                                (landmarks.getLandmark(13).getZ() + landmarks.getLandmark(14).getZ()) / 2
                        };

                        float[] v = {(select_p1[0] - select_p2[0]),
                                (select_p1[1] - select_p2[1]),
                                (select_p1[2] - select_p2[2])};

                        float[] lower_p1 = {landmarks.getLandmark(17).getX(), landmarks.getLandmark(17).getY(), landmarks.getLandmark(17).getZ()};
                        float[] lower_p2 = {landmarks.getLandmark(18).getX(), landmarks.getLandmark(18).getY(), landmarks.getLandmark(18).getZ()};
                        float[] lower_p3 = {landmarks.getLandmark(19).getX(), landmarks.getLandmark(19).getY(), landmarks.getLandmark(19).getZ()};
                        float[] lower_p4 = {landmarks.getLandmark(20).getX(), landmarks.getLandmark(20).getY(), landmarks.getLandmark(20).getZ()};

                        p1 = new float[]{(lower_p1[0] + lower_p2[0] + lower_p3[0] + lower_p4[0]) / 4,
                                         (lower_p1[1] + lower_p2[1] + lower_p3[1] + lower_p4[1]) / 4,
                                         (lower_p1[2] + lower_p2[2] + lower_p3[2] + lower_p4[2]) / 4};

                        float[] first = {landmarks.getLandmark(6).getX(), landmarks.getLandmark(6).getY(), landmarks.getLandmark(6).getZ()};
                        float[] second = {landmarks.getLandmark(18).getX(), landmarks.getLandmark(18).getY(), landmarks.getLandmark(18).getZ()};

                        float distance = (float) Math.sqrt(Math.pow((first[0] - second[0]), 2) + Math.pow((first[1] - second[1]), 2) + Math.pow((first[2] - second[2]), 2));
                        float t = (distance * 3) / (float) Math.sqrt(v[0] * v[0] + v[1] * v[1] + v[2] * v[2]);

                        endPoint = new float[] {(p1[0] + v[0] * t),
                                                (p1[1] + v[1] * t),
                                                (p1[2] + v[2] * t)};

                        currentPoints = new float[][]{p1, endPoint};
                        updatePoints(currentPoints);



//  영역구분 코드 시작
                        double[] vFixed = {1.0, 0.0}; // Python의 vFixed와 동일


                        // 내적 계산
                        double dotProduct = vFixed[0] * v[0] + vFixed[1] * v[1];

                        // 두 벡터의 노름 계산
                        double normVFixed = Math.sqrt(vFixed[0] * vFixed[0] + vFixed[1] * vFixed[1]);
                        double normV = Math.sqrt(v[0] * v[0] + v[1] * v[1]);

                        // 각도 계산 (아크코사인 사용)
                        double angleRadians = Math.acos(dotProduct / (normVFixed * normV));

                        // 라디안을 도로 변환
                        double angleDegrees = Math.toDegrees(angleRadians);
//                        double angleDegrees;
//
//                        degs.add((float)angleDegrees1);
//                        sizeofDegs = degs.size();
//
//                        if (sizeofDegs < 30) {
//                            sum = 0;
//                            for (float value : degs) {
//                                sum += value;
//                            }
//                            angleDegrees = sum / sizeofDegs;
//                        } else {
//                            List<Float> last30Elements = degs.subList(sizeofDegs - 30, sizeofDegs);
//                            sum = 0;
//                            for (float value : last30Elements) {
//                                sum += value;
//                            }
//                            angleDegrees = sum / 30;
//                        }

                        double Yaxis = faceLandmarks.getLandmark(168).getX();
                        double Xinterval = endPoint[0] - Yaxis;
//                        Log.d(H,"Xinterval: "+Xinterval);

                        String action = "?";
//                        String checkCircular = "?";


//                        Log.d(H,"칫솔벡터 각도: " + angleDegrees + "160이상 mid임");
//                        Log.d(H, "왼: "+faceLandmarks.getLandmark(39).getX()+"중: "+endPoint[0]+"오: "+faceLandmarks.getLandmark(267).getX());
                        if (170 < angleDegrees) {
//                            action = "mid horizontal";
                            action = "mid";
//                            checkCircular = "Circular";
                        } else {
                            if (63 < angleDegrees && angleDegrees < 117) {

                                action = "mid";
//                                Log.d(D,"39번점: " + faceLandmarks.getLandmark(39).getX());
//                                checkCircular = "Linear";
//                                if (v[1] > 0) {
//                                    action = "mid vertical lower";
//                                } else {
//                                    action = "mid vertical upper";
//                                }
                            } else {
                                if (Xinterval > 0) {
                                    action = "right";
                                } else {
                                    action = "left";
                                }
                            }
                        }


                        //                        왼ㅉ꼬 : 160
//                                -0.3
//                                오른쪽 : 10
//                                        0.3
//                                                중간 : 175
//                                                        -0.05

//                        왼쪽 : 130~160 -
//                        오른쪽 : 167, 
//                        중간 : 176, 0.15


//                        /* Circular vs Linear: not activated cause the input is ambiguous */
//                        double c_distance = Math.sqrt(Math.pow((p1[0] - faceLandmarks.getLandmark(168).getX()), 2) + Math.pow((p1[1] - faceLandmarks.getLandmark(168).getY()), 2));
//                        Log.d(H,"c_distance: " + c_distance);
//                        c_distances.add((float) c_distance);
//
//                        int sizeofcdist = c_distances.size();
//                        ArrayList<Float> trimmedDist;
//                        if (sizeofcdist > 60) {
//                            List<Float> last60Elements = c_distances.subList(sizeofcdist - 60, sizeofcdist);
//                            trimmedDist = new ArrayList<>(last60Elements);
//                        } else {
//                            trimmedDist = new ArrayList<>(c_distances);
//                        }
//
//                        if (sizeofcdist > 60) {
//                            float average = calculateAverage(trimmedDist);
//
//                            ArrayList<Float> abs_distances = new ArrayList<>();
//                            for (float c_dist : c_distances) {
//                                abs_distances.add(Math.abs(average - c_dist));
//                            }
//                            Collections.sort(abs_distances);
//
//                            Queue<Float> queue = new LinkedList<>(abs_distances);
////                            Log.d(H,"queue list 1: " + queue);
//
//                            for (int i = 0; i < 10; i++) {
//                                if (!queue.isEmpty()) {
//                                    float temp = queue.poll(); // 가장 작은 값 제거
////                                    Log.d(H,"first value: " + temp);
//
//                                }
//                                if (!queue.isEmpty()) {
//                                    float lastVal = ((LinkedList<Float>) queue).removeLast(); // 가장 큰 값 제거 및 반환
////                                    Log.d(H, "last  value: " + lastVal);
//                                }
//                            }
////                            Log.d(H,"queue list 2: " + queue);
//
//                            float abs_distance = 0;
////                            Log.d(H,"abs_distance 1: "+ abs_distance);
//
//                            for (float d : queue) {
//                                abs_distance += d;
////                                Log.d(H,"abs_distance 2: "+ abs_distance);
//
//                            }
//
//                            Log.d(H,"abs_distance 3: "+abs_distance);
//
//                            if (action.contains("mid vertical")) {
//                                checkCircular = "Linear";
//                            } else if (action.contains("mid horizontal")) {
//                                checkCircular = "Circular";
//                            } else {
//                                if (abs_distance > 0.58) {
//                                    checkCircular = "Circular";
//                                } else {
//                                    checkCircular = "Linear";
//                                }
//                            }
//                        }
//
//                        if (action.contains("right") || action.contains("left")) {
////                            if (currentBrushingSection != 0 && currentBrushingSection != 1 && currentBrushingSection != 2) {
//                                float mid = (faceLandmarks.getLandmark(138).getY() + faceLandmarks.getLandmark(367).getY()) / 2;
//                                checkHeights.add(mid - endPoint[1]);
//                                Log.d(H,"new_value: " + (mid - endPoint[1]));
//
//                            if (checkHeights.size() > 5) {
//                                    float sumCheckHeights = 0;
//                                    for (Float num : checkHeights) {
//                                        sumCheckHeights += num;
//                                    }
//                                    sumCheckHeights /= checkHeights.size();
//                                Log.d(H,"sumCheckHeights: " + sumCheckHeights + "양수면 upper 음수면 lower임");
//
//                                    if (sumCheckHeights > 0) {
//                                        action += " upper";
//                                    } else {
//                                        action += " lower";
//                                    }
//                                }
////                            }
//                        }

                        Log.d(H, "action!: " + action);

                        if(action.contains("left")) action_seq[0] += 1;
                        else if(action.contains("mid")) action_seq[1] += 1;
                        else if(action.contains("right")) action_seq[2] += 1;

                        int maxIndex = 0;
                        int maxValue = action_seq[0];

                        for (int i = 1; i < action_seq.length; i++) {
                            if (action_seq[i] > maxValue) {
                                maxValue = action_seq[i];
                                maxIndex = i;
                            }
                        }

                        this_action = "?";
                        if(maxIndex == 0) this_action = "left";
                        else if(maxIndex == 1) this_action = "mid";
                        else if(maxIndex == 2) this_action = "right";

// 영역구분 코드 끝

                        float[] vector = {(endPoint[0] - midFace[0]) / 2,
                                (endPoint[1] - midFace[1]) / 2,
                                (endPoint[2] - midFace[2]) / 2};
//                            Log.d(TAG, String.valueOf(p1[0]) + ", " + String.valueOf(p1[1]) + ", " + String.valueOf(p1[2]));

                        size = vector[0] * vector[0] + vector[1] * vector[1] + vector[2] * vector[2];

                        sizearr.add(size);
                        int sizeofArr = sizearr.size();
                        if (sizeofArr > numElementsToPrint) {
                            // 리스트의 크기가 30보다 큰 경우, 마지막 30개 원소만을 포함하는 부분 리스트 생성
                            List<Float> last30Elements = sizearr.subList(sizeofArr - numElementsToPrint, sizeofArr);
                            // 필요한 경우, 새 ArrayList로 변환
                            trimmedList = new ArrayList<>(last30Elements);
                        } else {
                            // 리스트의 크기가 30 이하인 경우, 원본 리스트 사용
                            trimmedList = new ArrayList<>(sizearr);
                        }
//                            StringBuilder sb = new StringBuilder();
//                            int start = Math.max(sizearr.size() - 30, 0);
//                            for (int i = start; i < sizearr.size(); i++) {
//                                sb.append(sizearr.get(i)).append(", ");
//                            }
//
//                            Log.d(TAG, "Last 30 elements: " + sb.toString().trim());

                        float min = Collections.min(trimmedList);
                        float max = Collections.max(trimmedList);


                        if ((max - min) / max > 0.01) {
                            if (size < min + (max - min) * 0.9) {
                                inside = true;
                            } else {
                                inside = false;
                                count = false;
                            }

                            if (inside == true) {
                                if (count == false) {
                                    Log.d("this_action", "(count)this_action = " + this_action);
                                    Log.d("this_action", "(count)guide_action = " + guide_action);
                                    if (this_action.equals(guide_action)){
                                        switch (currentBrushingSection) {
                                            case 0: // left Circular
                                                if (this_action.contains("left")) {//&& checkCircular.contains("Circular")) {
                                                    brushing0 += 1;
                                                }
                                                break;
                                            case 1: // mid Circular
                                                if (this_action.contains("mid")) {//&& checkCircular.contains("Circular")){
                                                    brushing1 += 1;
                                                }
                                                break;
                                            case 2: // right Circular
                                                if (this_action.contains("right")) {//&& checkCircular.contains("Circular")){
                                                    brushing2 += 1;
                                                }
                                                break;
                                            case 3: // left lower
                                                if (this_action.contains("left")) {
                                                    brushing3 += 1;
                                                }
                                                break;
                                            case 4: // right lower
                                                if (this_action.contains("right")) {
                                                    brushing4 += 1;
                                                }
                                                break;
                                            case 5: // left upper
                                                if (this_action.contains("left")) {
                                                    brushing5 += 1;
                                                }
                                                break;
                                            case 6: // right upper
                                                if (this_action.contains("right")) {
                                                    brushing6 += 1;
                                                }
                                                break;
                                            case 7: // left lower inner
                                                if (this_action.contains("left")) {
                                                    brushing7 += 1;
                                                }
                                                break;
                                            case 8: // mid lower inner
                                                if (this_action.contains("mid")) {
                                                    brushing8 += 1;
                                                }
                                                break;
                                            case 9: // right lower inner
                                                if (this_action.contains("right")) {
                                                    brushing9 += 1;
                                                }
                                                break;
                                            case 10: // left upper inner
                                                if (this_action.contains("left")) {
                                                    brushing10 += 1;
                                                }
                                                break;
                                            case 11: // mid upper inner
                                                if (this_action.contains("mid")) {
                                                    brushing11 += 1;
                                                }
                                                break;
                                            case 12: // right upper inner
                                                if (this_action.contains("right")) {
                                                    brushing12 += 1;
                                                }
                                                break;
                                            case 13: // ??
                                                brushing13 += 1;
                                                break;

                                        }
                                    }//                                    toothbrushing += 1;
                                    count = true;
                                    Log.d("checkCount", "***brusing***\n" + "0: " + brushing0 + "\n1: " + brushing1 + "\n2: " + brushing2 + "\n3: " + brushing3 + "\n4: " + brushing4 + "\n5: " + brushing5 + "\n6: " + brushing6 + "\n7: " + brushing7 + "\n8: " + brushing8 + "\n9: " + brushing9 + "\n10: " + brushing10 + "\n11: " + brushing11 + "\n12: " + brushing12 + "\n13: " + brushing13);
                                    Log.d(TAG, String.valueOf(brushing0));
                                    Log.d(TAG, String.valueOf(brushing1));
                                    Log.d(TAG, String.valueOf(brushing2));
                                    Log.d(TAG, String.valueOf(brushing3));
                                    Log.d(TAG, String.valueOf(brushing4));
                                    Log.d(TAG, String.valueOf(brushing5));
                                    Log.d(TAG, String.valueOf(brushing6));
                                    Log.d(TAG, String.valueOf(brushing7));
                                    Log.d(TAG, String.valueOf(brushing8));
                                    Log.d(TAG, String.valueOf(brushing9));
                                    Log.d(TAG, String.valueOf(brushing10));
                                    Log.d(TAG, String.valueOf(brushing11));
                                    Log.d(TAG, String.valueOf(brushing12));
                                    Log.d(TAG, String.valueOf(brushing13));


                                }
                            }
                        }
                        Log.d("totalScoreCheck", String.valueOf(totalScore));
                        Log.d("countprint", String.valueOf((perfect+great+good+miss)*score_per_count));
//                            NormalizedLandmarkList filteredLandmarks = filterLandmarks(landmarks, desiredHandIndices);
//                            Log.d(TAG,
//                                    "[TS:" + packet.getTimestamp()
//                                            + "] #Landmarks for hand: "
//                                            + landmarks.getLandmarkCount());
//                            Log.d(TAG, getLandmarksDebugString(filteredLandmarks));

                    } catch (InvalidProtocolBufferException e) {
                        Log.d("yejunbug", "landmarks is null");

                        Log.e(TAG, "Couldn't Exception received - " + e);
                    }
                });

    }
//    }




    @Override
    protected void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onDestroy();
    }


    // activity가 활성화 될 때 호출되는 메서드
    // 카메라 권한이 허용되면
    // 카메라를 시작하고 프레임 처리를 위한 초기화 작업 수행
    @Override
    protected void onResume() {
        super.onResume();
        converter = new ExternalTextureConverter(eglManager.getContext());
        converter.setFlipY(
                applicationInfo.metaData.getBoolean("flipFramesVertically", FLIP_FRAMES_VERTICALLY));
        converter.setConsumer(processor);
        if (PermissionHelper.cameraPermissionsGranted(this)) {
            startCamera();
        }
    }

    // activity가 일시 중지될 때 호출되는 메서드
    // 프레임 처리 관련 작업을 정리하고, 외부 텍스처 변환기를 닫음
    @Override
    protected void onPause() {
        super.onPause();
        Log.d("score", "onPause function is called");

        converter.close();
    }

    // 권한 요청 결과를 처리하기 위한 메서드
    @Override
    public void onRequestPermissionsResult(
            int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    // 카메라가 시작될 때 호출되는 메서드
    // surface texture를 받아 프리뷰 프레임에 엑세스
    protected void onCameraStarted(SurfaceTexture surfaceTexture) {
        previewFrameTexture = surfaceTexture;
        // Make the display view visible to start showing the preview. This triggers the
        // SurfaceHolder.Callback added to (the holder of) previewDisplayView.
        previewDisplayView.setVisibility(View.VISIBLE);
    }

    protected Size cameraTargetResolution() {
        return null; // No preference and let the camera (helper) decide.
    }

    // 카메라를 시작하고, CameraXPreviewHelper를 사용하여 프리뷰 화면을 설정
    public void startCamera() {
        cameraHelper = new CameraXPreviewHelper();
        cameraHelper.setOnCameraStartedListener(
                surfaceTexture -> {
                    onCameraStarted(surfaceTexture);
                });
        CameraHelper.CameraFacing cameraFacing =
                applicationInfo.metaData.getBoolean("cameraFacingFront", true)
                        ? CameraHelper.CameraFacing.FRONT
                        : CameraHelper.CameraFacing.BACK;
        // front camera로 시작
        cameraHelper.startCamera(
                this, CameraHelper.CameraFacing.FRONT, /*surfaceTexture=*/ null, cameraTargetResolution());
    }

    // 뷰의 크기를 계산하는 메서드
    protected Size computeViewSize(int width, int height) {
        return new Size(width, height);
    }

    // 프리뷰 화면의 크기가 변경될 때 호출 되는 메서드
    // 카메라 프레임을 변환하여 화면에 표시하기 위해 필요한 설정 수행
    protected void onPreviewDisplaySurfaceChanged(
            SurfaceHolder holder, int format, int width, int height) {
        // (Re-)Compute the ideal size of the camera-preview display (the area that the
        // camera-preview frames get rendered onto, potentially with scaling and rotation)
        // based on the size of the SurfaceView that contains the display.
        Size viewSize = computeViewSize(width, height);
        Size displaySize = cameraHelper.computeDisplaySizeFromViewSize(viewSize);
        boolean isCameraRotated = cameraHelper.isCameraRotated();

        // Connect the converter to the camera-preview frames as its input (via
        // previewFrameTexture), and configure the output width and height as the computed
        // display size.
        converter.setSurfaceTextureAndAttachToGLContext(
                previewFrameTexture,
                isCameraRotated ? displaySize.getHeight() : displaySize.getWidth(),
                isCameraRotated ? displaySize.getWidth() : displaySize.getHeight());
    }

    // 프리뷰 화면을 설정하는 메서드
    // SurfaceView를 초기화하고
    // 프리뷰 화면에 대한 SurfaceHolder.Callback을 추가
    private void setupPreviewDisplayView() {
        previewDisplayView.setVisibility(View.GONE);
        ViewGroup viewGroup = findViewById(R.id.preview_display_layout);
        viewGroup.addView(previewDisplayView);

        previewDisplayView
                .getHolder()
                .addCallback(
                        new SurfaceHolder.Callback() {
                            @Override
                            public void surfaceCreated(SurfaceHolder holder) {
                                processor.getVideoSurfaceOutput().setSurface(holder.getSurface());
                            }

                            @Override
                            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                                onPreviewDisplaySurfaceChanged(holder, format, width, height);
                            }

                            @Override
                            public void surfaceDestroyed(SurfaceHolder holder) {
                                processor.getVideoSurfaceOutput().setSurface(null);
                            }
                        });
    }

    private static String getLandmarksDebugString(NormalizedLandmarkList landmarks) {
        int landmarkIndex = 0;
        StringBuilder landmarksString = new StringBuilder();
        for (NormalizedLandmark landmark : landmarks.getLandmarkList()) {
            landmarksString.append("\t\tLandmark[").append(landmarkIndex).append("]: (").append(landmark.getX()).append(", ").append(landmark.getY()).append(", ").append(landmark.getZ()).append(")\n");
            ++landmarkIndex;
        }
        return landmarksString.toString();
    }

    private static NormalizedLandmarkList filterLandmarks(NormalizedLandmarkList landmarks, int[] indices) {
        NormalizedLandmarkList.Builder filteredLandmarksBuilder = NormalizedLandmarkList.newBuilder();
        for (int index : indices) {
            if (index < landmarks.getLandmarkCount()) {
                filteredLandmarksBuilder.addLandmark(landmarks.getLandmark(index));
            }
        }
        return filteredLandmarksBuilder.build();
    }

    
    private void makeComboFast(){
        Log.d("fastCombo","factCombo called");
        String accuracy = "Miss";
        float score = 0;
        if(trimmedList != null && !trimmedList.isEmpty() && size!=0){
            accuracy = calculateAccuracy(trimmedList, size);

            showEffectImage(accuracy);
            if(accuracy.contains("Perfect")){
                score = (float)(1 * score_per_count);
                perfect++;
            }
            else if(accuracy.contains("Great")){
                score = (float)(0.8 * score_per_count);
                great++;
            }
            else if(accuracy.contains("Good")){
                score = (float)(0.5 * score_per_count);
                good++;
            }
            else if(accuracy.contains("Miss")){
                score = 0;
                miss++;
            }
        }
        if(!isHandDetected){
            score = 0;
        }
        if(totalScore < 100) {
            totalScore += score;
        }
        else{
            totalScore = 100;
        }
    }



    /* HeeJun Function */
    private void startAnimation() {
        Log.d("combo2", "startAnimation() called");
        if (!stopAnimation) {
//            checkHeights = new ArrayList<>(); // 이거 upper 구분임
            Arrays.fill(action_seq, 0);

            for(int i = 0; i < (howManyBeatsPerArea); i++) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        makeComboFast();
                    }
                }, setBPM() * (i));
            }
            addMedalImage((int)totalScore);
            setToothImage();     // set tooth image and ball location
            setBallAnimation(); // set the ball animation according to tooth image
            toothIndex = (toothIndex + 1) % (toothImages.length+1); // loop, +1 = 양치 뱉기화면 시간추가
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startAnimation(); // loop
                }
            }, setBPM() * howManyBeatsPerArea);
        } else {
            Log.d("MyTag", "startAnimation() else called");
            toothImageView.setVisibility(View.INVISIBLE);
            ballImageView.setVisibility(View.INVISIBLE);
            circularballImageView.setVisibility(View.INVISIBLE);
        }
    }

    private int perfect = 0;
    private int great = 0;
    private int good = 0;
    private int miss = 0;
    private void startAnimation(int[] toothIndexes) {

        toothlength = toothIndexes.length;
        if (!stopAnimation && toothlength != 0) {
            Arrays.fill(action_seq, 0);

            for(int i = 0; i < (howManyBeatsPerArea/2); i++) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        makeComboFast();
                    }
                }, setBPM() * (i*2));
            }


            addMedalImage((int)totalScore);
            if (toothcount < toothlength) {
                toothIndex = toothIndexes[toothcount];
                setToothImage();     // set tooth image and ball location
                setBallAnimation(); // set the ball animation according to tooth image
                toothcount++;
                Log.d("MyTag", "startAnimation(toothIndexes) if called, handler called");
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startAnimation(toothIndexes); // loop
                    }
                }, setBPM() * howManyBeatsPerArea);
            } else {
                Log.d("moremore", "else");
                Log.d("MyTag", "startAnimation(toothIndexes) else called");
                stopPlaying();
                toothImageView.setVisibility(View.INVISIBLE);
                toothImageOpened.setVisibility(View.INVISIBLE);
                ballImageView.setVisibility(View.INVISIBLE);
                circularballImageView.setVisibility(View.INVISIBLE);
            }
        } else {
            Log.d("moremore", "else");
            Log.d("MyTag", "startAnimation(toothIndexes) else called");
            stopPlaying();
            toothImageView.setVisibility(View.INVISIBLE);
            toothImageOpened.setVisibility(View.INVISIBLE);
            ballImageView.setVisibility(View.INVISIBLE);
            circularballImageView.setVisibility(View.INVISIBLE);
        }
    }



    private void stopAnimation() {
        Log.d("MyTag","StopAnimation() called");
        stopAnimation = true;

    }

    private void setToothImage() {

        // image setting
        if ( toothIndex == 13) {
            toothImageView.setVisibility(View.INVISIBLE);
            spitTimeDialog.show();
        } else {
            spitTimeDialog.dismiss();
            toothImageView.setImageResource(toothImages[toothIndex]);

            if (toothImageView.getVisibility() == View.INVISIBLE)
                toothImageView.setVisibility(View.VISIBLE);
        }
        initialY = (toothImageView.getY() + (toothImageView.getHeight() - circularballImageView.getHeight()) / 2.0f) + 50;

        // Set visibility for toothImageOpened
        if (3 <= toothIndex && toothIndex <= 6) {
            toothImageOpened.setVisibility(View.VISIBLE);
        } else {
            toothImageOpened.setVisibility(View.INVISIBLE);
        }

        // Set visibility for ballImageView and circularballImageView
        if (0 <= toothIndex && toothIndex <= 2) {
            ballImageView.setVisibility(View.INVISIBLE);
            circularballImageView.setVisibility(View.VISIBLE);
        } else {
            ballImageView.setVisibility(View.VISIBLE);
            circularballImageView.setVisibility(View.INVISIBLE);
        }


    }
    private ValueAnimator circularAnimator;
    @SuppressLint("ResourceType")
    private void setBallAnimation() {
        circularAnimator = ValueAnimator.ofFloat(0, 360);
        circularAnimator.setDuration(setBPM()); // Set the duration of one complete rotation (in milliseconds)
        circularAnimator.setRepeatCount(howManyBeatsPerArea); // Infinite rotation
        circularAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (float) animation.getAnimatedValue();
                setCircularPosition(animatedValue);
            }
        });
        switch(toothIndex){
            case 0:     // left_circular
                guide_action = "left";
                currentBrushingSection = 0;
                ballImageView.clearAnimation();
                initialX = toothImageView.getX() + toothImageView.getWidth() * 0.15f;
                circularAnimator.start();
                break;
            case 1:     // mid_circular
                guide_action = "mid";
                currentBrushingSection = 1;
                initialX = toothImageView.getX() + toothImageView.getWidth() * 0.5f;
                circularAnimator.start();
                break;
            case 2:     // right_circular
                guide_action = "right";
                currentBrushingSection = 2;
                initialX = toothImageView.getX() + toothImageView.getWidth() * 0.75f;
                circularAnimator.start();
                break;
            case 3:     // left_lower
                guide_action = "left";
                currentBrushingSection = 3;
                setLinearPosition(0,75,0,75);
                break;
            case 4:     // right_lower
                guide_action = "right";
                currentBrushingSection = 4;
                setLinearPosition(380,305,0,75);
                break;
            case 5:     // left_upper
                guide_action = "left";
                currentBrushingSection = 5;
                setLinearPosition(0,75,0,-75);
                break;
            case 6:     // right_upper
                guide_action = "right";
                currentBrushingSection = 6;
                setLinearPosition(380,305,0,-75);
                break;
            case 7:     // left_lower_inner
                guide_action = "left";
                currentBrushingSection = 7;
                setLinearPosition(20,75,0,50);
                break;
            case 8:     // mid_lower_inner
                guide_action = "mid";
                currentBrushingSection = 8;
                setLinearPosition(180,180,50,25);
                break;
            case 9:     // right_lower_inner
                guide_action = "right";
                currentBrushingSection = 9;
                setLinearPosition(360,305,0,50);
                break;
            case 10:    // left_upper_inner
                guide_action = "left";
                currentBrushingSection = 10;
                setLinearPosition(20,75,20,-10);
                break;
            case 11:    // mid_upper_inner
                guide_action = "mid";
                currentBrushingSection = 11;
                setLinearPosition(180,180,-10,20);
                break;
            case 12:    // right_upper_inner
                guide_action = "right";
                currentBrushingSection = 12;
                setLinearPosition(360,305,20,-10);
                break;
            case 13:
                currentBrushingSection = 13;
                setLinearPosition(180,180,-120,-120);
                break;
        }
    }

    private void setCircularPosition(float animatedValue) {
        // Calculate the new position based on the angle in the opposite direction (counter-clockwise)
        float x = (float) (radius * Math.cos(Math.toRadians(-animatedValue)));
        float y = (float) (radius * Math.sin(Math.toRadians(-animatedValue)));

        // Set the new position for the ImageView
        circularballImageView.setX(initialX + x - circularballImageView.getWidth() / 2.0f);
        circularballImageView.setY(initialY + y - circularballImageView.getHeight() / 2.0f);
    }


    private AnimatorSet animatorSet;
    private void setLinearPosition(int fromXDelta, int toXDelta, int fromYDelta, int toYDelta) {
        ObjectAnimator translateX = ObjectAnimator.ofFloat(ballImageView, "translationX", fromXDelta, toXDelta);
        ObjectAnimator translateY = ObjectAnimator.ofFloat(ballImageView, "translationY", fromYDelta, toYDelta);

        translateX.setRepeatMode(ObjectAnimator.REVERSE);
        translateX.setRepeatCount(ObjectAnimator.INFINITE);
        translateY.setRepeatMode(ObjectAnimator.REVERSE);
        translateY.setRepeatCount(ObjectAnimator.INFINITE);

        animatorSet = new AnimatorSet();
        animatorSet.playTogether(translateX, translateY);
        animatorSet.setInterpolator(new LinearInterpolator());

        animatorSet.setInterpolator(new DecelerateInterpolator());

        // 애니메이션의 duration을 동적으로 변경
        animatorSet.setDuration(setBPM()/2);

        // 애니메이션 시작
        animatorSet.start();
    }

    private long setBPM() {

        return 60000 / bpm;    // 1 beat당 소요되는 시간
    }

    private void moreBrushingDialog() {
        MoreBrushingDialog moreBrushingDialog = new MoreBrushingDialog(HolisticActivity.this);
        moreBrushingDialog.show(); // 추가 양치 시간 dialog
        Log.d("moremore", "moreBrushingDialog.show()");
        mediaPlayer2 = MediaPlayer.create(this, R.raw.rabbit); // 이 부분 넘겨 받은 노래 제목으로 설정 해야 함

        moreBrushingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mediaPlayer2.start();
                Log.d("moremore", "mediaPlayer2.start() in moreBrushing");
                stopAnimation = false;
                Log.d("moremore", "mediaPlayer2.isPlaying(): "+mediaPlayer2.isPlaying());
                startAnimation(toothIndexes);
                Log.d("moremore", "mediaPlayer2.isPlaying(): "+mediaPlayer2.isPlaying());

            }
        });
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                moreBrushingDialog.dismiss();
                Log.d("moremore", "moreBrushingDialog.dismiss()");

            }
        }, 2000);

    }

    private void stopPlaying() {
        Log.d("moremore", "stopPlaying() called");
        Log.d("moremore", "mediaPlayer2 "+mediaPlayer2+" mediaPlayer2.isPlaying() "+mediaPlayer2.isPlaying());

        mediaPlayer2.stop();
        Log.d("moremore", "mediaPlayer.stop() called");

        stopAnimation();
        Log.d("moremore", "stopAnimation() called");
        showAfterDialogs();
        Log.d("moremore", "showAfterDialogs() called");
    }


    private void showPreviousDialogs() {
        CamFixDialog camFixDialog = new CamFixDialog(HolisticActivity.this);
        TubeDialog tubeDialog = new TubeDialog(HolisticActivity.this);
        GrabDialog grabDialog = new GrabDialog(HolisticActivity.this);
        RealStartDialog realStartDialog = new RealStartDialog(HolisticActivity.this);

        camFixDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                // CamFixDialog dismissed, show TubeDialog
                tubeDialog.show();
            }
        });

        tubeDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                // TubeDialog dismissed, show BeReadyDialog
                grabDialog.show();
            }
        });

        grabDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                // TubeDialog dismissed, show BeReadyDialog
                realStartDialog.show();
            }
        });

        realStartDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                // 이제 진짜 시작하면, Toast 불러오는 화면 call
                // 3 2 1 count 보여주기
                startCountdown();
            }
        });

        // Show the first dialog
        camFixDialog.show();
        Log.d("MyTag", "2. showPreviousDialogs() -> 카메라고정, 치약짜, 카운트다운");

    }
    private CountDownTimer countDownTimer;
    private void startCountdown() {
        countDownTimer = new CountDownTimer(4000, 1000) {
            public void onTick(long millisUntilFinished) {
                // 여기에서 3, 2, 1 이미지를 표시
                Log.d("MyTag", "millis = " + millisUntilFinished);
                double temp = (double) millisUntilFinished / 1000;
                Log.d("MyTag", "temp = " + temp);
                long secondsRemaining = Math.round(temp) - 1;
                Log.d("MyTag", "secondsRemaining = " + secondsRemaining);
                showCountdownImage(secondsRemaining);
            }

            public void onFinish() {
                // Countdown이 끝났을 때 추가 작업을 수행
                countdownImageView.setVisibility(View.GONE);
                mediaPlayer.start();
                startAnimation();
            }
        }.start();
    }

    private void showCountdownImage(long secondsRemaining) {
        // 이미지를 표시하는 코드
        // secondsRemaining에 따라 다른 이미지를 표시하도록

        countdownImageView = findViewById(R.id.countdown_image);

        if (countdownImageView != null) {
            int imageResource;

            switch ((int) secondsRemaining) {
                case 3:
                    imageResource = R.drawable.three_image; // Replace with your image resource
                    break;
                case 2:
                    imageResource = R.drawable.two_image; // Replace with your image resource
                    break;
                case 1:
                    imageResource = R.drawable.one_image; // Replace with your image resource
                    break;
                case 0:
                    imageResource = R.drawable.start_image;
                    ImageView note = (ImageView) findViewById(R.id.note);
                    GlideDrawableImageViewTarget noteImage = new GlideDrawableImageViewTarget(note);
                    Glide.with(this).load(R.drawable.note).into(noteImage);
                    break;
                default:
                    return;
            }
            countdownImageView.setImageResource(imageResource);
        }


    }


    private void startPulseEffect(ImageView imageView) {
        // 이미지를 1.0배에서 1.2배로 커졌다가 다시 1.0배로 작아지게 하는 애니메이션
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(imageView, "scaleX", 1.0f, 1.2f, 1.0f);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(imageView, "scaleY", 1.0f, 1.2f, 1.0f);

        // 애니메이션의 지속시간을 설정 (예: 800 밀리초)
        scaleXAnimator.setDuration(setBPM());
        scaleYAnimator.setDuration(setBPM());

        // 애니메이션을 무한 반복하게 설정
        scaleXAnimator.setRepeatCount(ValueAnimator.INFINITE);
        scaleYAnimator.setRepeatCount(ValueAnimator.INFINITE);

        // 애니메이션을 반전시켜서 실행
        scaleXAnimator.setRepeatMode(ValueAnimator.REVERSE);
        scaleYAnimator.setRepeatMode(ValueAnimator.REVERSE);

        // 애니메이션 시작
        scaleXAnimator.start();
        scaleYAnimator.start();
    }

    // 기존의 showEffectImage 메서드에 위에서 정의한 startPulseEffect 메서드 호출 추가

    private void setComboDigits(){
        combo++;
        comboCount++;
        Log.d("combo2", "combo = " + combo);
        String comboString = String.valueOf(combo);
        int digits = comboString.length();
        for (int i = 0 ; i < digits; i++) {
            char digitChar = comboString.charAt(i);
            int digit = Character.getNumericValue(digitChar);

            String findID = "digit_" + digit;
            digitResource = getResources().getIdentifier(findID, "drawable", getPackageName());

            if (digits == 1) {
                digit100ImageView.setImageResource(R.drawable.digit_0);
                digit10ImageView.setImageResource(R.drawable.digit_0);
                digit1ImageView.setImageResource(digitResource);
            } else if (digits == 2){
                digit100ImageView.setImageResource(R.drawable.digit_0);
                if (i == 0) digit10ImageView.setImageResource(digitResource);
                else if (i == 1) digit1ImageView.setImageResource(digitResource);
            } else {
                if (i == 0) digit100ImageView.setImageResource(digitResource);
                else if (i == 1) digit10ImageView.setImageResource(digitResource);
                else if (i == 2) digit1ImageView.setImageResource(digitResource);
            }
        }

        comboImageView.setVisibility(View.VISIBLE);
        digit100ImageView.setVisibility(View.VISIBLE);
        digit10ImageView.setVisibility(View.VISIBLE);
        digit1ImageView.setVisibility(View.VISIBLE);

        if(comboCount < howManyBeatsPerArea){
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.d("combo2","handler() called ");
                    setComboDigits();
                }
            }, setBPM());
        }
        Log.d("Combo2", "Handler Queue Size: " + handler.getLooper().getQueue().toString());

    }

    private void showEffectImage(String accuracy) {
        effectImageView = findViewById(R.id.effect_image);

        if (effectImageView != null) {
            int imageResource = R.drawable.miss_image;

            if (accuracy.contains("Perfect")) imageResource = R.drawable.perfect_image;
            else if (accuracy.contains("Great")) imageResource = R.drawable.great_image;
            else if (accuracy.contains("Good")) imageResource = R.drawable.good_image;
            else if (accuracy.contains("Miss")) imageResource = R.drawable.miss_image;
            effectImageView.setImageResource(imageResource);

            // 반짝이는 효과와 펄스 효과 시작
            startSparkleEffect(effectImageView);
            startPulseEffect(effectImageView);
        }
    }

    private void startSparkleEffect(ImageView imageView) {
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(imageView, "alpha", 0.4f, 1.0f, 0.4f);
        alphaAnimator.setDuration(setBPM()); // 800ms 동안 애니메이션 진행
        alphaAnimator.setRepeatCount(ValueAnimator.INFINITE); // 무한 반복
        alphaAnimator.setRepeatMode(ValueAnimator.REVERSE); // 애니메이션 반복 모드 설정 (반전)
        alphaAnimator.start();
    }

    private void showAfterDialogs() {

        // 현재 날짜를 가져오기
        Date currentDate = Calendar.getInstance().getTime();

        // 날짜를 "yyyy-MM-dd" 형식의 문자열로 변환
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dateFormat.format(currentDate);

        // 현재 시간을 "hh시 mm분" 형식의 문자열로 변환
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh시 mm분");
        String formattedTime = timeFormat.format(currentDate);

        // toothbrushing 객체를 넘겨받은 값에 맞게 수정 필요
        Toothbrushing toothbrushing = new Toothbrushing(child.getChildName(), formattedDate, formattedTime
                , brushing0, brushing1, brushing2, brushing5+brushing10, brushing3+brushing7, brushing6+brushing12, brushing4+brushing9, brushing11, brushing8
                , (int) totalScore);

        Log.v("test my score", String.valueOf((int)totalScore));
        // ToothbrushingDB.getDatabase(getApplicationContext()).toothbrushingDao().insert(toothbrushing);

        WaterDialog waterDialog = new WaterDialog(HolisticActivity.this);
        RhythmDialog rhythmDialog = new RhythmDialog(HolisticActivity.this, perfect, great, good, miss, combo_max);
        GetSeedDialog getSeedDialog = new GetSeedDialog(HolisticActivity.this, toothbrushing);
        RandomRewardDialog randomRewardDialog = new RandomRewardDialog(HolisticActivity.this, toothbrushing);
        
        // 1. 입 헹구기
        waterDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                // TubeDialog dismissed, show BeReadyDialog
                // 2. 씨앗 얻기
                rhythmDialog.show();
            }
        });

        // 2. 콤보 확인하기
        waterDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                // TubeDialog dismissed, show BeReadyDialog
                // 2. 씨앗 얻기
                getSeedDialog.show();
            }
        });
        
        

        // 3. 씨앗 얻기
        getSeedDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                // TubeDialog dismissed, show BeReadyDialog
                // 3. 랜덤 보상 얻기
                randomRewardDialog.show();
            }
        });

        waterDialog.show();
    }

    // 양치질 정확도를 계산하는 메서드
    // 정확도를 계산하는 보조 메서드
    private String calculateAccuracy(ArrayList<Float> trimmedList, float size) {
        float min = Collections.min(trimmedList);
        float max = Collections.max(trimmedList);
        String accuracy = "";
        comboCount = 0;
        Log.d("this_action", "(Perfect)this_action = " + this_action);
        Log.d("this_action", "(Perfect)guide_action = " + guide_action);
        if (!this_action.equals(guide_action)) {
            accuracy = "Miss";
            combo = 0;
            comboflag = false;
        } else {
            if (size < (min + (max - min) * 0.3)) {
                Log.d("combo3", "Perfect called");
                accuracy = "Perfect";
                comboflag = true;
            } else if (size < (min + (max - min) * 0.6)) {
                Log.d("combo3", "Great called");
                accuracy = "Great";
                comboflag = true;
            } else if (size < (min + (max - min) * 0.9)) {
                Log.d("combo3", "Good called");
                accuracy = "Good";
                comboflag = true;
            } else {
                Log.d("combo3", "Miss called");
                accuracy = "Miss";
                combo = 0;
                comboflag = false;
            }
        }
        Log.d("combo","comboflag = " + comboflag);
        Log.d("combo2", "setComboDigits() called");

        if (comboflag) setComboDigits();
        else {
            combo = 0;
            comboImageView.setVisibility(View.INVISIBLE);
            digit100ImageView.setVisibility(View.INVISIBLE);
            digit10ImageView.setVisibility(View.INVISIBLE);
            digit1ImageView.setVisibility(View.INVISIBLE);
        }

        return accuracy;
    }

    @Override
    public void onBackPressed() {
        Log.d("score", "onBackPressed function is called");


        if (animatorSet != null) {
            animatorSet.cancel();
        }

        if (circularAnimator!= null) {
            circularAnimator.cancel();
        }

        // 핸들러 작업 제거
        handler.removeCallbacksAndMessages(null);

        // MediaPlayer 해제
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }

        if (mediaPlayer2 != null) {
            mediaPlayer2.release();
            mediaPlayer2 = null;
        }

        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        // Converter 해제
        if (converter != null) {
            converter.close();
        }

        // 카메라 리소스 해제
        if (cameraProvider != null) {
            cameraProvider.unbindAll();
        }

        // Converter를 닫아 리소스 해제
        if (converter != null) {
            converter.close();
        }

        // EGL 관리자를 종료
        if (eglManager != null) {
            eglManager.release();
        }

        // 액티비티 종료
        // 항상 MainMenuActivity
        Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
        intent.putExtra("childName", child.getChildName());
        startActivity(intent);
        finish(); // 현재 액티비티 종료
        super.onBackPressed();

    }
}