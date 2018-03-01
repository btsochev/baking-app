package com.nanodegree.boyan.bakingapp.ui;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.nanodegree.boyan.bakingapp.R;
import com.nanodegree.boyan.bakingapp.data.Recipe;
import com.nanodegree.boyan.bakingapp.data.Step;
import com.nanodegree.boyan.bakingapp.utilities.StringHelpers;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class StepDetailFragment extends Fragment {
    @BindView(R.id.player_container)
    RelativeLayout playContainer;

    @BindView(R.id.player)
    SimpleExoPlayerView playerView;

    @BindView(R.id.no_media_iv)
    ImageView noMediaIv;

    @BindView(R.id.step_desc_tv)
    TextView stepDescription;

    private static final String TAG = StepDetailFragment.class.getSimpleName();

    private Recipe mRecipe;
    private Step mStep;

    private SimpleExoPlayer mExoPlayer;
    private MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;
    private BandwidthMeter mBandwidthMeter;
    private TrackSelector mTrackSelector;

    private long mPlayerPosition;

    public StepDetailFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRecipe = Parcels.unwrap(getArguments().getParcelable("recipe"));
        int stepPosition = getArguments().getInt("step_position", 0);
        mStep = mRecipe.getSteps().get(stepPosition);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_step_detail, container, false);
        ButterKnife.bind(this, rootView);

        if (savedInstanceState == null) {
            createMediaPlayer();
        }
        if (!getResources().getBoolean(R.bool.isTablet))
            resizeMediaPlayer();

        return rootView;
    }

    private void resizeMediaPlayer() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int height = displayMetrics.heightPixels;
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
            playerView.setLayoutParams(layoutParams);
        } else {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            playerView.setLayoutParams(layoutParams);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23 || mExoPlayer == null)) {
            createMediaPlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mExoPlayer != null) mPlayerPosition = mExoPlayer.getCurrentPosition();
        releasePlayer();
    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            mPlayerPosition = mExoPlayer.getCurrentPosition();
        }

        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }

        if (mMediaSession != null) {
            mMediaSession.setActive(false);
        }

        if (mTrackSelector != null) {
            mTrackSelector = null;
        }
    }

    public void createMediaPlayer() {

        if (!StringHelpers.isNullOrEmpty(mStep.getVideoURL())) {
            noMediaIv.setVisibility(View.GONE);

            initializeMediaSession();
            initializePlayer(Uri.parse(mStep.getVideoURL()));

        } else {
            noMediaIv.setVisibility(View.VISIBLE);

            if (!StringHelpers.isNullOrEmpty(mStep.getThumbnailURL())) {
                Picasso.with(getContext())
                        .load(mStep.getThumbnailURL())
                        .placeholder(R.drawable.default_recipe_image)
                        .error(R.drawable.default_recipe_image)
                        .into(noMediaIv);
            } else {
                noMediaIv.setImageResource(R.drawable.default_recipe_image);
            }
        }
    }

    private void initializePlayer(Uri mediaUri) {

        if (mExoPlayer == null) {
            TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(mBandwidthMeter);
            mTrackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), mTrackSelector);
            playerView.setPlayer(mExoPlayer);

            mBandwidthMeter = new DefaultBandwidthMeter();

            String userAgent = Util.getUserAgent(getContext(), TAG);
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getContext(), userAgent);
            MediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(mediaUri);
            mExoPlayer.prepare(mediaSource);

            mExoPlayer.setPlayWhenReady(true);
            mExoPlayer.seekTo(mPlayerPosition);
        }
    }

    private void initializeMediaSession() {

        mMediaSession = new MediaSessionCompat(getContext(), TAG);

        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mMediaSession.setMediaButtonReceiver(null);

        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mMediaSession.setPlaybackState(mStateBuilder.build());
        mMediaSession.setCallback(new MediaSessionCallback());
        mMediaSession.setActive(true);
    }

    private class MediaSessionCallback extends MediaSessionCompat.Callback {

        @Override
        public void onPlay() {
            super.onPlay();
            mExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            super.onPause();
            mExoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            super.onSkipToPrevious();
            mExoPlayer.seekTo(0);
        }
    }
}
