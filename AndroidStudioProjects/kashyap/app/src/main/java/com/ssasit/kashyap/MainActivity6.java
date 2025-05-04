package com.ssasit.kashyap;

import android.content.Intent;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.io.IOException;

public class MainActivity6 extends AppCompatActivity implements
        SurfaceHolder.Callback {
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private Camera camera;
    private Button recordAudioBtn, playAudioBtn, recordVideoBtn, playVideoBtn;
    private VideoView videoView;
    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;
    private boolean isRecordingAudio = false;
    private boolean isRecordingVideo = false;
    private boolean isPlayingAudio = false;
    private boolean isPlayingVideo = false;
    private String audioPath;
    private String videoPath;
    private static final int PERMISSIONS_REQUEST = 100;
    private Toolbar tb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main6);

        tb = findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        audioPath = new File(getExternalFilesDir(null),
                "recorded_audio.m4a").getAbsolutePath();
        videoPath = new File(getExternalFilesDir(null),
                "recorded_video.mp4").getAbsolutePath();
        recordAudioBtn = findViewById(R.id.recordAudioBtn);
        playAudioBtn = findViewById(R.id.playAudioBtn);
        recordVideoBtn = findViewById(R.id.recordVideoBtn);
        playVideoBtn = findViewById(R.id.playVideoBtn);
        surfaceView = findViewById(R.id.surfaceView);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        videoView = findViewById(R.id.videoView);
        requestPermissions();
        recordAudioBtn.setOnClickListener(v -> {
            if (!isRecordingAudio) startAudioRecording();
            else stopAudioRecording();
        });
        playAudioBtn.setOnClickListener(v -> {
            if (!isPlayingAudio) playAudio();
            else pauseAudio();
        });
        recordVideoBtn.setOnClickListener(v -> {
            if (!isRecordingVideo) startVideoRecording();
            else stopVideoRecording();
        });
        playVideoBtn.setOnClickListener(v -> {
            if (!isPlayingVideo) playVideo();
            else pauseVideo();
        });    }
    private void requestPermissions() {
        String[] permissions = {
                android.Manifest.permission.RECORD_AUDIO,
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        ActivityCompat.requestPermissions(this, permissions,
                PERMISSIONS_REQUEST);
    }
    private void startAudioRecording() {
        try {
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            mediaRecorder.setOutputFile(audioPath);
            mediaRecorder.prepare();
            mediaRecorder.start();
            isRecordingAudio = true;
            recordAudioBtn.setText("Stop Audio");
            Toast.makeText(this, "Recording audio...", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Failed to start audio recording",
                    Toast.LENGTH_SHORT).show();
        }    }
    private void stopAudioRecording() {
        try {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
            isRecordingAudio = false;
            recordAudioBtn.setText("Record Audio");
            Toast.makeText(this, "Audio recording stopped",
                    Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Error stopping audio recording",
                    Toast.LENGTH_SHORT).show();
        }    }
    private void playAudio() {
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(audioPath);
            mediaPlayer.prepare();
            mediaPlayer.start();
            isPlayingAudio = true;
            playAudioBtn.setText("Pause Audio");
            mediaPlayer.setOnCompletionListener(mp -> {
                isPlayingAudio = false;
                playAudioBtn.setText("Play Audio");
            });
        } catch (IOException e) {
            Toast.makeText(this, "Failed to play audio",
                    Toast.LENGTH_SHORT).show();
        }    }
    private void pauseAudio() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            isPlayingAudio = false;
            playAudioBtn.setText("Play Audio");
        }    }
    private void startVideoRecording() {
        try {            camera = Camera.open();
            camera.setDisplayOrientation(90);
            camera.unlock();
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setCamera(camera);

            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
            mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
            mediaRecorder.setVideoSize(1920, 1080);
            mediaRecorder.setOrientationHint(90);
            mediaRecorder.setOutputFile(videoPath);
            mediaRecorder.setPreviewDisplay(surfaceHolder.getSurface());
            mediaRecorder.prepare();
            mediaRecorder.start();
            isRecordingVideo = true;
            recordVideoBtn.setText("Stop Video");
            Toast.makeText(this, "Recording video...", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Failed to start video recording",
                    Toast.LENGTH_SHORT).show();
        }    }
    private void stopVideoRecording() {
        try {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
            camera.lock();
            camera.release();
            camera = null;
            isRecordingVideo = false;
            recordVideoBtn.setText("Record Video");
            Toast.makeText(this, "Video recording stopped",
                    Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Error stopping video recording",
                    Toast.LENGTH_SHORT).show();
        }    }
    private void playVideo() {
        videoView.setVideoURI(Uri.parse(videoPath));
        videoView.start();
        isPlayingVideo = true;
        playVideoBtn.setText("Pause Video");
        videoView.setOnCompletionListener(mp -> {
            isPlayingVideo = false;
            playVideoBtn.setText("Play Video");
        });    }
    private void pauseVideo() {
        if (videoView.isPlaying()) {
            videoView.pause();
            isPlayingVideo = false;
            playVideoBtn.setText("Play Video");
        }    }
    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {    }
    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int
            width, int height) {    }
    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        if (camera != null) {
            camera.stopPreview();
            camera.release();
            camera = null;
        }    }
    @Override
    protected void onPause() {
        super.onPause();
        if (isRecordingAudio) stopAudioRecording();
        if (isRecordingVideo) stopVideoRecording();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mnu = getMenuInflater();
        mnu.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu1) {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        } else if (item.getItemId() == R.id.menu2) {
            Intent i = new Intent(this, MainActivity2.class);
            startActivity(i);
        } else if (item.getItemId() == R.id.menu3) {
            Intent i = new Intent(this, MainActivity3.class);
            startActivity(i);
        } else if (item.getItemId() == R.id.menu4) {
            Intent i = new Intent(this, MainActivity5.class);
            startActivity(i);
        } else if (item.getItemId() == R.id.menu5) {
            Intent i = new Intent(this, MainActivity4.class);
            startActivity(i);
        } else if (item.getItemId() == R.id.menu6) {
            Intent i = new Intent(this, MainActivity6.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }


}
