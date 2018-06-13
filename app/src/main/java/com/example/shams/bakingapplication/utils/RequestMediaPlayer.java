package com.example.shams.bakingapplication.utils;

import android.content.Context;
import android.net.Uri;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import com.google.android.exoplayer2.ExoPlayerFactory;
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

public final class RequestMediaPlayer {

    public static SimpleExoPlayer mSimpleExoPlayer;
    public static MediaSessionCompat mMediaSessionCompat;
    public static PlaybackStateCompat.Builder mPlayBackState;
    public static BandwidthMeter mBandwidthMeter;
    public static TrackSelector mTrackSelector;
    public static long videoPosition;

    public static void publishMediaPlayer(Uri videoUrl, Context mContext, String TAG, PlayerView playerView) {
        initializeSession(mContext, TAG);
        initializePlayer(videoUrl, mContext, TAG, playerView);
    }

    public static void initializeSession(Context mContext, String TAG) {
        mMediaSessionCompat = new MediaSessionCompat(mContext, TAG);
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

    public static void initializePlayer(Uri uri, Context mContext, String TAG, PlayerView playerView) {
        if (mSimpleExoPlayer == null) {

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

    public static void releasePlayer() {
        if (mSimpleExoPlayer != null) {
            videoPosition = mSimpleExoPlayer.getCurrentPosition();
            mSimpleExoPlayer.stop();
            mSimpleExoPlayer.release();
            mSimpleExoPlayer = null;
        }

        if (mMediaSessionCompat != null) {
            mMediaSessionCompat.setActive(false);
        }

        if (mTrackSelector != null) {
            mTrackSelector = null;
        }
    }
}
