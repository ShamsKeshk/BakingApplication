package com.example.shams.bakingapplication;

import android.content.Context;
import android.net.ProxyInfo;
import android.net.Uri;
import android.os.Bundle;
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
import com.google.android.exoplayer2.util.Util;

import java.security.PrivilegedAction;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class StepFragment extends Fragment {


    private String TAG = StepFragment.class.getSimpleName();

    private int mCurrentStepId;
    private int mStepsCellsNumber;

    private ArrayList<Recipes> recipesArrayList;
    private Recipes currentRecipe;

    private Steps currentStep ;
    private String description;
    private String videoUrl;
    private String thumbnailUrl;


    //Exo Player Part
    private SimpleExoPlayer mSimpleExoPlayer;
    private MediaSessionCompat mMediaSessionCompat;
    private PlaybackStateCompat.Builder mPlayBackState;
    private BandwidthMeter mBandwidthMeter;
    private TrackSelector mTrackSelector;
    private long videoPosition;

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
        args.putInt(Constants.KEY_STEP_CELLS_NUMBER,stepsCellsNumber);
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
            mStepsCellsNumber = getArguments().getInt(Constants.KEY_STEP_CELLS_NUMBER);
            currentRecipe = recipesArrayList.get(0);

            currentStep  = currentRecipe.getSteps().get(mCurrentStepId);
            description = currentStep.getDescription();
            videoUrl = currentStep.getVideoURL();
            thumbnailUrl = currentStep.getThumbnailURL();

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step, container, false);

        ButterKnife.bind(this,view);

        if (savedInstanceState == null){
            publishMediaPlayer();
        }

        descriptionTextView.setText(description);

        return view;
    }

    public void publishMediaPlayer(){
        initializeSession();
        initializePlayer(Uri.parse(videoUrl));
    }

    public void initializeSession(){
        mMediaSessionCompat = new MediaSessionCompat(mContext,TAG);
        mMediaSessionCompat.setFlags(MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS
                | MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS);
        mMediaSessionCompat.setMediaButtonReceiver(null);

        mPlayBackState = new PlaybackStateCompat.Builder()
                .setActions(PlaybackStateCompat.ACTION_PLAY |
                        PlaybackStateCompat.ACTION_PAUSE |
                        PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                        PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mMediaSessionCompat.setPlaybackState(mPlayBackState.build());

        mMediaSessionCompat.setCallback(new MediaSessionCompat.Callback() {
            @Override
            public void onPlay() {
                super.onPlay();
                mSimpleExoPlayer.setPlayWhenReady(true);
            }

            @Override
            public void onPause() {
                super.onPause();
                mSimpleExoPlayer.setPlayWhenReady(true);
            }

            @Override
            public void onSkipToPrevious() {
                super.onSkipToPrevious();
                mSimpleExoPlayer.seekTo(0);
            }
        });

        mMediaSessionCompat.setActive(true);
    }

    public void initializePlayer(Uri uri){
        if (mSimpleExoPlayer == null){

            TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(mBandwidthMeter);
            mTrackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);


            mSimpleExoPlayer = ExoPlayerFactory.newSimpleInstance(mContext, mTrackSelector);

            playerView.setPlayer(mSimpleExoPlayer);

            mBandwidthMeter = new DefaultBandwidthMeter();

            String userAgent = Util.getUserAgent(mContext, TAG);
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(mContext, userAgent);
            MediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
            mSimpleExoPlayer.prepare(mediaSource);

            mSimpleExoPlayer.setPlayWhenReady(true);
            mSimpleExoPlayer.seekTo(videoPosition);
        }
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
        if (mSimpleExoPlayer != null) videoPosition = mSimpleExoPlayer.getCurrentPosition();
        releasePlayer();
    }
    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23 || mSimpleExoPlayer == null)) {
            publishMediaPlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void releasePlayer() {
        if (mSimpleExoPlayer != null) {
            videoPosition = mSimpleExoPlayer.getCurrentPosition();
        }

        if (mSimpleExoPlayer != null) {
            mSimpleExoPlayer.stop();
            mSimpleExoPlayer.release();
            mSimpleExoPlayer = null;
        }

        if (mMediaSessionCompat != null) {
            mMediaSessionCompat.setActive(false);
        }

        if (mTrackSelector !=  null) {
            mTrackSelector = null;
        }
    }


}
