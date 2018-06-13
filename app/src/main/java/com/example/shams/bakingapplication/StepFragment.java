package com.example.shams.bakingapplication;

import android.content.Context;
import android.net.ProxyInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.shams.bakingapplication.model.Recipes;
import com.example.shams.bakingapplication.model.Steps;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.ParsingLoadable;
import com.google.android.exoplayer2.util.Util;

import java.security.PrivilegedAction;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class StepFragment extends Fragment {


    private String TAG = StepFragment.class.getSimpleName();

    private int mCurrentStepId;

    private ArrayList<Recipes> recipesArrayList;
    private Recipes currentRecipe;

    private Steps currentStep ;
    private String description;
    private String videoUrl;
    private String thumbnailUrl;

    private Context mContext;

    @BindView(R.id.tv_step_description_text_view_fragment_id)
    TextView descriptionTextView ;

    @BindView(R.id.player_view_step_fragment_id)
    PlayerView playerView;

    public StepFragment() {
        // Required empty public constructor
    }

    public static StepFragment newInstance(int mCurrentStepId,int stepsCellsNumber,ArrayList<Recipes> recipesArrayList) {
        StepFragment fragment = new StepFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.KEY_CURRENT_STEP_ID,mCurrentStepId);
        args.putInt(Constants.KEY_STEPS_SIZE_NUMBER,stepsCellsNumber);
        args.putParcelableArrayList(Constants.KEY_RECIPE_PARCELABLE_ARRAY_LIST,recipesArrayList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            recipesArrayList = getArguments().getParcelableArrayList(Constants.KEY_RECIPE_PARCELABLE_ARRAY_LIST);
            mCurrentStepId = getArguments().getInt(Constants.KEY_CURRENT_STEP_ID);
            currentRecipe = recipesArrayList.get(0);

            currentStep  = currentRecipe.getSteps().get(mCurrentStepId);
            description = currentStep.getDescription();
            videoUrl = currentStep.getVideoURL();
            thumbnailUrl = currentStep.getThumbnailURL();
        }

        if (savedInstanceState != null){
            RequestMediaPlayer.videoPosition = savedInstanceState.getLong(Constants.KEY_VIDEO_POSITION_KEY);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step, container, false);

        ButterKnife.bind(this,view);

        if (savedInstanceState == null){
            RequestMediaPlayer.publishMediaPlayer(Uri.parse(videoUrl),mContext,TAG,playerView);
        }

        descriptionTextView.setText(description);

        return view;
    }



    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(Constants.KEY_VIDEO_POSITION_KEY, RequestMediaPlayer.videoPosition);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (RequestMediaPlayer.mSimpleExoPlayer != null) RequestMediaPlayer.videoPosition = RequestMediaPlayer.mSimpleExoPlayer.getCurrentPosition();

        RequestMediaPlayer.releasePlayer();
    }
    @Override
    public void onStart() {
        super.onStart();
        if ((Util.SDK_INT <= 23 || RequestMediaPlayer.mSimpleExoPlayer == null)) {
            RequestMediaPlayer.publishMediaPlayer(Uri.parse(videoUrl),mContext,TAG, playerView);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23 ||  RequestMediaPlayer.mSimpleExoPlayer == null)) {
            RequestMediaPlayer.publishMediaPlayer(Uri.parse(videoUrl),getContext(),TAG, playerView);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (RequestMediaPlayer.mSimpleExoPlayer != null) RequestMediaPlayer.videoPosition = RequestMediaPlayer.mSimpleExoPlayer.getCurrentPosition();
        RequestMediaPlayer.releasePlayer();
    }




}
