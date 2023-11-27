package com.lite.holistic_tracking;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.util.Size;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.media.MediaPlayer;


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
import com.lite.holistic_tracking.Entity.Toothbrushing;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.mediapipe.formats.proto.LandmarkProto.NormalizedLandmarkList;

import com.google.mediapipe.formats.proto.LandmarkProto.NormalizedLandmark;
import com.google.mediapipe.formats.proto.LandmarkProto.NormalizedLandmarkList;
import com.google.mediapipe.framework.PacketCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;

public class HolisticActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    private static final String TAG = "MainActivity";

    private static final String INPUT_NUM_HANDS_SIDE_PACKET_NAME = "num_hands";

    private static final int NUM_HANDS = 2;

    private static final String OUTPUT_FACE_LANDMARKS_STREAM_NAME = "face_landmarks";
    private static final String OUTPUT_HAND_LANDMARKS_STREAM_NAME = "left_hand_landmarks";
    private Button seedButton;

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
    private ArrayList<Float> checkHeights = new ArrayList<>();
    private ArrayList<String> action_seq = new ArrayList<>();
    private int numElementsToPrint = 30;
    private boolean inside = false; // used for boolean, can't modify in lambda expression
    private boolean count = false; // used for boolean, can't modify in lambda expression
    int toothbrushing = 0;
    private SurfaceView cameraPreview;
    private TextView overlayText;


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


    public HolisticActivity() {
    }

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
        private volatile NormalizedLandmarkList faceLandmarks;
        private volatile NormalizedLandmarkList handLandmarks;
        private final Object lock = new Object();

        public void updateFaceLandmarks(NormalizedLandmarkList landmarks) {
            synchronized (lock) {
                this.faceLandmarks = landmarks;
            }
        }

        public void updateHandLandmarks(NormalizedLandmarkList landmarks) {
            synchronized (lock) {
                this.handLandmarks = landmarks;
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

        seedButton = findViewById(R.id.yourButtonId);

        seedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 부모님 화면으로 이동
                Toothbrushing toothbrushing = new Toothbrushing("곽희준", "2023-11-17", "9시 08분"
                        , 10, 8, 12, 5, 9, 7, 8, 10, 10
                        , 88);
                GetSeedDialog getSeedDialog = new GetSeedDialog(HolisticActivity.this, toothbrushing);
                getSeedDialog.show();
            }
        });

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

        mediaPlayer = MediaPlayer.create(this, R.raw.rabbit);
        mediaPlayer.start();

        // To show verbose logging, run:
        // adb shell setprop log.tag.MainActivity VERBOSE
        if (Log.isLoggable(TAG, Log.VERBOSE)) {
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
                            if (landmarks == null) {
                                Log.d(TAG, "[TS:" + packet.getTimestamp() + "] No hand landmarks.");
                                return;
                            }
//                            StringBuilder landmarksString = new StringBuilder();
//                            landmarksString.append(landmarks.getLandmark(17).getX());

                            sharedLandmarkData.updateHandLandmarks(landmarks);
                            NormalizedLandmarkList faceLandmarks = sharedLandmarkData.getFaceLandmarks();
                            if (faceLandmarks == null) {
                                Log.d(TAG, "[TS:" + packet.getTimestamp() + "] No face landmarks.");
                                return;
                            }


                            float[] face1 = {faceLandmarks.getLandmark(359).getX(), faceLandmarks.getLandmark(359).getY(), faceLandmarks.getLandmark(359).getZ()};
                            float[] face2 = {faceLandmarks.getLandmark(130).getX(), faceLandmarks.getLandmark(130).getY(), faceLandmarks.getLandmark(130).getZ()};
                            float[] midFace = {(face1[0] + face2[0]) / 2,
                                    (face1[1] + face2[1]) / 2,
                                    (face1[2] + face2[2]) / 2};
//                            Log.d(TAG, String.valueOf(face[0])+", "+String.valueOf(face[1])+", "+String.valueOf(face[2]));

                            float[] select_p1 = {landmarks.getLandmark(6).getX(), landmarks.getLandmark(6).getY(), landmarks.getLandmark(6).getZ()};
                            float[] select_p2 = {landmarks.getLandmark(13).getX(), landmarks.getLandmark(13).getY(), landmarks.getLandmark(13).getZ()};

                            float[] v = {(select_p1[0] - select_p2[0]),
                                    (select_p1[1] - select_p2[1]),
                                    (select_p1[2] - select_p2[2])};

                            float[] lower_p1 = {landmarks.getLandmark(17).getX(), landmarks.getLandmark(17).getY(), landmarks.getLandmark(17).getZ()};
                            float[] lower_p2 = {landmarks.getLandmark(18).getX(), landmarks.getLandmark(18).getY(), landmarks.getLandmark(18).getZ()};
                            float[] lower_p3 = {landmarks.getLandmark(19).getX(), landmarks.getLandmark(19).getY(), landmarks.getLandmark(19).getZ()};
                            float[] lower_p4 = {landmarks.getLandmark(20).getX(), landmarks.getLandmark(20).getY(), landmarks.getLandmark(20).getZ()};

                            float[] p1 = {(lower_p1[0] + lower_p2[0] + lower_p3[0] + lower_p4[0]) / 4,
                                    (lower_p1[1] + lower_p2[1] + lower_p3[1] + lower_p4[1]) / 4,
                                    (lower_p1[2] + lower_p2[2] + lower_p3[2] + lower_p4[2]) / 4};

                            float[] first = {landmarks.getLandmark(6).getX(), landmarks.getLandmark(6).getY(), landmarks.getLandmark(6).getZ()};
                            float[] second = {landmarks.getLandmark(18).getX(), landmarks.getLandmark(18).getY(), landmarks.getLandmark(18).getZ()};

                            float distance = (float) Math.sqrt(Math.pow((first[0] - second[0]), 2) + Math.pow((first[1] - second[1]), 2) + Math.pow((first[2] - second[2]), 2));
                            float t = (distance * 2) / (float) Math.sqrt(v[0] * v[0] + v[1] * v[1] + v[2] * v[2]);

                            float[] endPoint = {(p1[0] + v[0] * t),
                                    (p1[1] + v[1] * t),
                                    (p1[2] + v[2] * t)};
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

                            double Yaxis = faceLandmarks.getLandmark(168).getX();
                            double Xinterval = endPoint[0] - Yaxis;

                            String action = "?";
                            String checkCircular = "?";

                            if (165 < angleDegrees) {
                                action = "mid horizontal";
                            } else {
                                if (85 < angleDegrees && angleDegrees < 110 && faceLandmarks.getLandmark(39).getX() < endPoint[0] && endPoint[0] < faceLandmarks.getLandmark(267).getX()) {
                                    if (v[1] > 0) {
                                        action = "mid vertical lower";
                                    } else {
                                        action = "mid vertical upper";
                                    }
                                } else {
                                    if (Xinterval > 0) {
                                        action = "right";
                                    } else {
                                        action = "left";
                                    }
                                }
                            }

                            double c_distance = Math.sqrt(Math.pow((p1[0] - faceLandmarks.getLandmark(168).getX()), 2) + Math.pow((p1[1] - faceLandmarks.getLandmark(168).getY()), 2));
                            c_distances.add((float) c_distance);

                            int sizeofcdist = c_distances.size();
                            ArrayList<Float> trimmedDist;
                            if (sizeofcdist > 60) {
                                List<Float> last60Elements = c_distances.subList(sizeofcdist - 60, sizeofcdist);
                                trimmedDist = new ArrayList<>(last60Elements);
                            } else {
                                trimmedDist = new ArrayList<>(c_distances);
                            }

                            if (sizeofcdist > 60) {
                                float average = calculateAverage(trimmedDist);

                                ArrayList<Float> abs_distances = new ArrayList<>();
                                for (float c_dist : c_distances) {
                                    abs_distances.add(Math.abs(average - c_dist));
                                }
                                Collections.sort(abs_distances);

                                Queue<Float> queue = new LinkedList<>(abs_distances);
                                for (int i = 0; i < 10; i++) {
                                    if (!queue.isEmpty()) {
                                        queue.poll(); // 가장 작은 값 제거
                                    }
                                    if (!queue.isEmpty()) {
                                        ((LinkedList<Float>) queue).removeLast(); // 가장 큰 값 제거
                                    }
                                }

                                float abs_distance = 0;

                                for (float d : queue) {
                                    abs_distance += d;
                                }

                                if (abs_distance > 0.58) {
                                    checkCircular = "Circular";
                                } else {
                                    checkCircular = "Linear";
                                }

                            }

                            if ((action == "right" || action == "left") && checkCircular == "Linear") {
                                float mid = (faceLandmarks.getLandmark(138).getY() + faceLandmarks.getLandmark(367).getY()) / 2;
                                checkHeights.add(mid - endPoint[1]);
                                if (!checkHeights.isEmpty()) {
                                    float sumCheckHeights = 0;
                                    for (Float num : checkHeights) {
                                        sumCheckHeights += num;
                                    }
                                    sumCheckHeights /= checkHeights.size();

                                    if (sumCheckHeights > 0) {
                                        action += " upper";
                                    } else {
                                        action += " lower";
                                    }
                                }
                            }
                            action_seq.add(action);

                            String this_action = "?";
                            if (action_seq.size() >= 19) {

                                if (action_seq.get(action_seq.size() - 1) == action_seq.get(action_seq.size() - 2) && action_seq.get(action_seq.size() - 2) == action_seq.get(action_seq.size() - 3)) {
                                    this_action = action;
                                }

                            }

                            Log.d(TAG, "this_action : " + this_action + ", check_Circular : " + checkCircular);

// 영역구분 코드 끝

                            float[] vector = {(endPoint[0] - midFace[0]) / 2,
                                    (endPoint[1] - midFace[1]) / 2,
                                    (endPoint[2] - midFace[2]) / 2};
//                            Log.d(TAG, String.valueOf(p1[0]) + ", " + String.valueOf(p1[1]) + ", " + String.valueOf(p1[2]));

                            float size = vector[0] * vector[0] + vector[1] * vector[1] + vector[2] * vector[2];

                            sizearr.add(size);
                            int sizeofArr = sizearr.size();
                            ArrayList<Float> trimmedList;
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
                                if (size < min + (max - min) * 0.3) {
                                    inside = true;
                                } else {
                                    inside = false;
                                    count = false;
                                }

                                if (inside == true) {
                                    if (count == false) {
                                        toothbrushing += 1;
                                        count = true;
                                        Log.d(TAG, String.valueOf(toothbrushing));
                                    }
                                }
                            }

//                            NormalizedLandmarkList filteredLandmarks = filterLandmarks(landmarks, desiredHandIndices);
//                            Log.d(TAG,
//                                    "[TS:" + packet.getTimestamp()
//                                            + "] #Landmarks for hand: "
//                                            + landmarks.getLandmarkCount());
//                            Log.d(TAG, getLandmarksDebugString(filteredLandmarks));

                        } catch (InvalidProtocolBufferException e) {
                            Log.e(TAG, "Couldn't Exception received - " + e);
                        }
                    });
        }

        startAnimation();
    }


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

}