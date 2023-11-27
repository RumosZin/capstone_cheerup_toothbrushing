package com.lite.holistic_tracking;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.util.Log;
import android.util.Size;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.mediapipe.components.CameraHelper;
import com.google.mediapipe.components.CameraXPreviewHelper;
import com.google.mediapipe.components.ExternalTextureConverter;
import com.google.mediapipe.components.FrameProcessor;
import com.google.mediapipe.components.PermissionHelper;
import com.google.mediapipe.formats.proto.LandmarkProto;
import com.google.mediapipe.framework.AndroidAssetUtil;
import com.google.mediapipe.framework.AndroidPacketCreator;
import com.google.mediapipe.framework.Packet;
import com.google.mediapipe.framework.PacketGetter;
import com.google.mediapipe.glutil.EglManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.media.MediaPlayer;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;


public class HolisticActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private static final String INPUT_NUM_HANDS_SIDE_PACKET_NAME = "num_hands";

    private static final int NUM_HANDS = 2;

    private static final String OUTPUT_LANDMARKS_STREAM_NAME = "hand_landmarks";

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

    private MediaPlayer mediaPlayer;



    //zz
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

    private ImageView toothImageView;
    private ImageView toothImageOpened;
    private ImageView ballImageView;
    private ImageView circularballImageView;

    private int toothIndex = 0;
    float initialX;
    float initialY;

    final Handler handler = new Handler();
    private float radius; // Adjust the radius as needed
    private float angle;
    private ValueAnimator circularAnimator;

    private boolean isFirstCall = true;
    private boolean dialogOK = false;






    // activity가 생성될 때 호출되는 메서드
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.holistic);
        toothImageView = findViewById(R.id.toothImage);
        ballImageView = findViewById(R.id.ballImage);
        circularballImageView = findViewById(R.id.circularBallImage);
        toothImageOpened = findViewById(R.id.toothImageOpened);
        radius = 50.0f;
        angle = 0.0f;

        // AndroidManifest.xml 파일에서 정의된 메타 데이터를 포함
        // 나중에 앱의 동작을 구성하는데 사용됨
        try {
            applicationInfo =
                    getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "Cannot find application info: " + e);
        }

        // SurfaceView를 생성하여 previewDisplayView 변수에 할당
        // SurfaceView는 카메라 프리뷰를 표시하는데 사용
        previewDisplayView = new SurfaceView(this);
        setupPreviewDisplayView(); // SurfaceView의 초기화 및 설정 수행

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
                        applicationInfo.metaData.getString("outputVideoStreamName")
                        );

        processor
                .getVideoSurfaceOutput()
                .setFlipY(
                        applicationInfo.metaData.getBoolean("flipFramesVertically", FLIP_FRAMES_VERTICALLY));

        PermissionHelper.checkAndRequestCameraPermissions(this);


        showConfirmationDialog();

        // 여기부터
        Log.d("in onCreate", "1");
        AndroidPacketCreator packetCreator = processor.getPacketCreator();
        Map<String, Packet> inputSidePackets = new HashMap<>();
        inputSidePackets.put(INPUT_NUM_HANDS_SIDE_PACKET_NAME, packetCreator.createInt32(NUM_HANDS));
        processor.setInputSidePackets(inputSidePackets);

        // To show verbose logging, run:
        // adb shell setprop log.tag.MainActivity VERBOSE
        if (Log.isLoggable(TAG, Log.VERBOSE)) {
            processor.addPacketCallback(
                    OUTPUT_LANDMARKS_STREAM_NAME,
                    (packet) -> {
                        Log.v(TAG, "Received multi-hand landmarks packet.");
                        List<LandmarkProto.NormalizedLandmarkList> multiHandLandmarks =
                                PacketGetter.getProtoVector(packet, LandmarkProto.NormalizedLandmarkList.parser());
                        Log.v(
                                TAG,
                                "[TS:"
                                        + packet.getTimestamp()
                                        + "] "
                                        + getMultiHandLandmarksDebugString(multiHandLandmarks));
                    });
        }
        mediaPlayer = MediaPlayer.create(this, R.raw.mountain);

        ImageView ballImage = findViewById(R.id.ballImage);
        @SuppressLint("ResourceType") Animation rotateAnimation = AnimationUtils.loadAnimation(this, R.animator.left_circular_animation);
        ballImage.startAnimation(rotateAnimation);
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
        converter.close();
        stopSong();
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
        playSong();
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
    
    // 프리뷰 화면의 크기가 변경될 때 호출되는 메서드
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

    private String getMultiHandLandmarksDebugString(List<LandmarkProto.NormalizedLandmarkList> multiHandLandmarks) {
        if (multiHandLandmarks.isEmpty()) {
            return "No hand landmarks";
        }
        String multiHandLandmarksStr = "Number of hands detected: " + multiHandLandmarks.size() + "\n";
        int handIndex = 0;
        for (LandmarkProto.NormalizedLandmarkList landmarks : multiHandLandmarks) {
            multiHandLandmarksStr +=
                    "\t#Hand landmarks for hand[" + handIndex + "]: " + landmarks.getLandmarkCount() + "\n";
            int landmarkIndex = 0;
            for (LandmarkProto.NormalizedLandmark landmark : landmarks.getLandmarkList()) {
                multiHandLandmarksStr +=
                        "\t\tLandmark ["
                                + landmarkIndex
                                + "]: ("
                                + landmark.getX()
                                + ", "
                                + landmark.getY()
                                + ", "
                                + landmark.getZ()
                                + ")\n";
                ++landmarkIndex;
            }
            ++handIndex;
        }
        return multiHandLandmarksStr;
    }

    private void printRightHandLandmarkCoordinates(List<LandmarkProto.NormalizedLandmarkList> multiHandLandmarks) {
        if (multiHandLandmarks.isEmpty()) {
            Log.d(TAG, "No hand landmarks");
            return;
        }

        // 오른손의 landmark 정보를 찾기 위한 루프
        int handIndex = 0;
        for (LandmarkProto.NormalizedLandmarkList landmarks : multiHandLandmarks) {
            if (handIndex == 1) { // 0번 인덱스는 왼손, 1번 인덱스는 오른손
                int landmarkIndex = 0;
                for (LandmarkProto.NormalizedLandmark landmark : landmarks.getLandmarkList()) {
                    if (landmarkIndex == 18) { // 18번 landmark에 해당하는 정보
                        float x = landmark.getX();
                        float y = landmark.getY();
                        float z = landmark.getZ();
                        Log.d(TAG, "Right Hand Landmark 18 - X: " + x + ", Y: " + y + ", Z: " + z);
                        // 여기에서 x, y, z 좌표를 원하는 방식으로 화면에 출력하거나 활용할 수 있습니다.
                    }
                    landmarkIndex++;
                }
            }
            handIndex++;
        }
    }

    private void playSong() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    private void stopSong() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();

        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }



    private void startAnimation() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                toothImageView.setImageResource(toothImages[toothIndex]);
                initialY = (toothImageView.getY() + (toothImageView.getHeight() - circularballImageView.getHeight()) / 2.0f)+50;

                if (3 <= toothIndex && toothIndex <= 6) {
                    toothImageOpened.setVisibility(View.VISIBLE);
                } else {
                    toothImageOpened.setVisibility(View.INVISIBLE);
                }

                if (0 <= toothIndex && toothIndex <= 2) {
                    ballImageView.setVisibility(View.INVISIBLE);
                    circularballImageView.setVisibility(View.VISIBLE);
                } else {
                    ballImageView.setVisibility(View.VISIBLE);
                    circularballImageView.setVisibility(View.INVISIBLE);
                }

                startBallControl();
                toothIndex = (toothIndex + 1) % toothImages.length;

                // loop
                startAnimation();
            }
        }, calculateDelay());  // Set a delay based on BPM
    }

    @SuppressLint("ResourceType")
    private void startBallControl() {
        int animationIndex;
        Animation animation;

        circularAnimator = ValueAnimator.ofFloat(0, 360);
        circularAnimator.setDuration(1000); // Set the duration of one complete rotation (in milliseconds)
        circularAnimator.setRepeatCount(ValueAnimator.INFINITE); // Infinite rotation
        circularAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (float) animation.getAnimatedValue();
                updateCircularPosition(animatedValue);
            }
        });
//
//        circularAnimator.addListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationStart(Animator animation) {
//                // Animation start
//            }
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                // Animation end
//            }
//        });

        switch(toothIndex){
            case 0:
                ballImageView.clearAnimation();
                initialX = toothImageView.getX() + toothImageView.getWidth() * 0.15f;
                circularAnimator.start();
                break;
            case 1:

                initialX = toothImageView.getX() + toothImageView.getWidth() * 0.5f;
                circularAnimator.start();
                break;
            case 2:
                initialX = toothImageView.getX() + toothImageView.getWidth() * 0.75f;
                circularAnimator.start();
                break;
            case 3:
                animationIndex = R.animator.h_left_lower;
                animation = AnimationUtils.loadAnimation(this, animationIndex);
                ballImageView.startAnimation(animation);
                break;
            case 4:
                animationIndex = R.animator.h_right_lower;
                animation = AnimationUtils.loadAnimation(this, animationIndex);
                ballImageView.startAnimation(animation);
                break;
            case 5:
                animationIndex = R.animator.h_left_upper;
                animation = AnimationUtils.loadAnimation(this, animationIndex);
                ballImageView.startAnimation(animation);
                break;
            case 6:
                animationIndex = R.animator.h_right_upper;
                animation = AnimationUtils.loadAnimation(this, animationIndex);
                ballImageView.startAnimation(animation);
                break;
            case 7:
                animationIndex = R.animator.h_left_lower_inner;
                animation = AnimationUtils.loadAnimation(this, animationIndex);
                ballImageView.startAnimation(animation);
                break;
            case 8:
                animationIndex = R.animator.h_mid_vertical_lower_inner;
                animation = AnimationUtils.loadAnimation(this, animationIndex);
                ballImageView.startAnimation(animation);
                break;
            case 9:
                animationIndex = R.animator.h_right_lower_inner;
                animation = AnimationUtils.loadAnimation(this, animationIndex);
                ballImageView.startAnimation(animation);
                break;
            case 10:
                animationIndex = R.animator.h_left_upper_inner;
                animation = AnimationUtils.loadAnimation(this, animationIndex);
                ballImageView.startAnimation(animation);
                break;
            case 11:
                animationIndex = R.animator.h_mid_vertical_upper_inner;
                animation = AnimationUtils.loadAnimation(this, animationIndex);
                ballImageView.startAnimation(animation);
                break;
            case 12:
                animationIndex = R.animator.h_right_upper_inner;
                animation = AnimationUtils.loadAnimation(this, animationIndex);
                ballImageView.startAnimation(animation);
                break;
        }
    }


    private void updateCircularPosition(float animatedValue) {
        // Calculate the new position based on the angle
        float x = (float) (radius * Math.cos(Math.toRadians(animatedValue)));
        float y = (float) (radius * Math.sin(Math.toRadians(animatedValue)));

        // Set the new position for the ImageView
        circularballImageView.setX(initialX + x - circularballImageView.getWidth() / 2.0f);
        circularballImageView.setY(initialY + y - circularballImageView.getHeight() / 2.0f);

    }

    private long calculateDelay() {
        // TODO: Implement delay calculation based on BPM
        // Example: return (long) (60000 / bpm); for beats per minute
        if (isFirstCall) {
            isFirstCall = false;
            return 0;
        } else {
            return 5000;
        }
    }


    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmation");
        builder.setMessage("Do you want to proceed?");

        // Add the buttons
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked Yes button
                dialogOK = true;
                dialog.dismiss();
                startAnimation();
                
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked No button
                // Handle cancellation or show a message
                dialog.dismiss(); // Dismiss the dialog
            }
        });

        // Create the AlertDialog
        AlertDialog dialog = builder.create();

        // Show the dialog
        dialog.show();
    }

}