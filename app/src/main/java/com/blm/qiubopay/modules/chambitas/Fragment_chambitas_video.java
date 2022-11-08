package com.blm.qiubopay.modules.chambitas;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blm.qiubopay.R;
import com.blm.qiubopay.components.CViewMenuTop;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import org.jetbrains.annotations.NotNull;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;


import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IAlertButton;
import mx.devapps.utils.interfaces.IFunction;

public class Fragment_chambitas_video extends HFragment {
    public static IFunction execute;
    private String url = "";

    public static Fragment_chambitas_video newInstance(String url) {
        Fragment_chambitas_video fragment = new Fragment_chambitas_video();
        Bundle args = new Bundle();

        args.putString("Fragment_chambitas_video", url);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        url = getArguments().getString("Fragment_chambitas_video");

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_chambitas_video, container, false));
    }

    @Override
    public void initFragment(){

        CViewMenuTop.create(getView())
                .setColorBack(R.color.clear_blue)
                .onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                execute = null;
                getContext().backFragment();
            }
        }).showLogo();


        // url.startsWith("v") (en caso de ya no venga la URL solo el ID)
        if(url.contains("youtu")) {  // Valida que contenga youtube
            initYoutube();     // Se inicializa YT HElPER (pierfrancescosoffritti NO proviene del API de YT)
        }else{
            renderVideo();     // Se inicializa VideoView
        }


    }

    public void initYoutube() {

        MultiValueMap<String, String> parameters = UriComponentsBuilder.fromUriString(url.replace(" ", "")).build().getQueryParams();

        YouTubePlayerView youTubePlayerView = getView().findViewById(R.id.video_player_web);
        youTubePlayerView.setVisibility(View.VISIBLE);
        getLifecycle().addObserver(youTubePlayerView);

        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {

                try {
                    String videoId = "";

                    if(parameters.get("v")!=null){
                        videoId = parameters.get("v").get(0).toString();
                    } else {
                        Uri appLinkData = Uri.parse(url);
                        videoId = appLinkData.getLastPathSegment();
                    }
                    youTubePlayer.loadVideo(videoId, 0);

                    youTubePlayer.addListener(new AbstractYouTubePlayerListener() {
                        @Override
                        public void onError(@NotNull YouTubePlayer youTubePlayer, @NotNull PlayerConstants.PlayerError error) {
                            super.onError(youTubePlayer, error);
                            getContext().alert("Error al cargar el video", new IAlertButton() {
                                @Override
                                public String onText() {
                                    return "Aceptar";
                                }

                                @Override
                                public void onClick() {
                                    getContext().onBackPressed();
                                }
                            });
                        }

                        @Override
                        public void onReady(@NotNull YouTubePlayer youTubePlayer) {
                            super.onReady(youTubePlayer);
                        }

                        @Override
                        public void onStateChange(@NotNull YouTubePlayer youTubePlayer, @NotNull PlayerConstants.PlayerState state) {
                            super.onStateChange(youTubePlayer, state);
                            if (state == state.ENDED){
                                if(execute != null)
                                    execute.execute();
                            }
                        }

                    });

                } catch (Exception ex) {
                    Log.e("Error YT Helper: ", String.valueOf(ex));
                    showPopupErrorLoadVideo();
                }

            }

            @Override
            public void onError(@NonNull YouTubePlayer youTubePlayer, @NonNull PlayerConstants.PlayerError error) {
                super.onError(youTubePlayer, error);
                showPopupErrorLoadVideo();
            }
        });
    }

    public void renderVideo(){
        VideoView video_player_view = getView().findViewById(R.id.video_player_view);
        video_player_view.setVisibility(View.VISIBLE);
        video_player_view.setVideoPath(url);
        video_player_view.start();

        video_player_view.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if(execute != null)
                    execute.execute();
            }

        });

        video_player_view.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                showPopupErrorLoadVideo();
                return false;
            }
        });

    }
    @Override
    public void onResume() {
        super.onResume();
    }

    public void showPopupErrorLoadVideo() {
        getContext().alert("Error al cargar el video", new IAlertButton() {
            @Override
            public String onText() {
                return "Aceptar";
            }

            @Override
            public void onClick() {
                execute = null;
                getContext().onBackPressed();
            }
        });
    }
}

