package dadm.scaffold.counter;

import android.content.Context;
import android.content.DialogInterface;
import android.app.AlertDialog;
import android.graphics.drawable.AnimationDrawable;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import dadm.scaffold.BaseFragment;
import dadm.scaffold.R;
import dadm.scaffold.ScaffoldActivity;
import dadm.scaffold.engine.FramesPerSecondCounter;
import dadm.scaffold.engine.GameEngine;
import dadm.scaffold.engine.GameView;
import dadm.scaffold.engine.ScoreCounter;
import dadm.scaffold.input.JoystickInputController;
import dadm.scaffold.space.GameController;
import dadm.scaffold.space.SpaceShipPlayer;


public class GameFragment extends BaseFragment implements View.OnClickListener {
    private GameEngine theGameEngine;
    private View pauseButton;

    //Pause menu
    private View pause_menu;
    private View pause_image;

    //Finalizer
    private Button finalizer;
    private boolean finalizerEnabled;

    public GameFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_game, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pauseButton = view.findViewById(R.id.btn_play_pause);
        pauseButton.setOnClickListener(this);

        pause_menu = view.findViewById(R.id.menu);
        pause_image = view.findViewById(R.id.menu_image);

        finalizer = view.findViewById(R.id.btn_finalizer);
        finalizerEnabled = false;

        //Pause menu buttons
        for(int i= 1; i< ((ViewGroup)pause_menu).getChildCount(); i++){
            Button child = (Button)((ViewGroup)pause_menu).getChildAt(i);
            child.setOnClickListener(this);
        }

        finalizer.setOnClickListener(this);
        finalizer.setEnabled(false);
        final ViewTreeObserver observer = view.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener(){
            @Override
            public void onGlobalLayout(){
                //Para evitar que sea llamado múltiples veces,
                //se elimina el listener en cuanto es llamado
                observer.removeOnGlobalLayoutListener(this);
                GameView gameView = (GameView) getView().findViewById(R.id.gameView);
                theGameEngine = new GameEngine(getActivity(), gameView);
                theGameEngine.setSoundManager(getScaffoldActivity().getSoundManager());
                theGameEngine.setTheInputController(new JoystickInputController(getView(), getContext()));
                theGameEngine.addGameObject(new SpaceShipPlayer(theGameEngine, ((ScaffoldActivity)getActivity()).ship_id));
                theGameEngine.addGameObject(new FramesPerSecondCounter(theGameEngine));
                theGameEngine.addGameObject(new ScoreCounter(theGameEngine));
                theGameEngine.addGameObject(new GameController(theGameEngine));
                theGameEngine.startGame();
                theGameEngine.setViews((ImageView)getActivity().findViewById(R.id.background), (ImageView)getActivity().findViewById(R.id.background2));
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_play_pause:
                pauseGameAndShowPauseDialog();
                break;
            case R.id.btn_finalizer:
                finalizeGame();
                break;
            case R.id.btn_menu:
                theGameEngine.stopGame();
                ((ScaffoldActivity)getActivity()).mainMenu();
                break;
            case R.id.btn_restart:
                theGameEngine.stopGame();
                ((ScaffoldActivity)getActivity()).startGame(((ScaffoldActivity)getActivity()).last_id);
                break;
            case R.id.btn_continue:
                pause_menu.setVisibility(View.GONE);
                pause_image.setVisibility(View.GONE);
                theGameEngine.resumeGame();
                pauseButton.setAlpha(1);
                finalizer.setAlpha(1);
                finalizer.setEnabled(finalizerEnabled);
                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (theGameEngine.isRunning()){
            pauseGameAndShowPauseDialog();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        theGameEngine.stopGame();
    }

    @Override
    public boolean onBackPressed() {
        if (theGameEngine.isRunning()) {
            pauseGameAndShowPauseDialog();
            return true;
        }
        return false;
    }

    private void pauseGameAndShowPauseDialog() {
        pauseButton.setAlpha(0.4f);
        finalizer.setAlpha(0.4f);
        finalizer.setEnabled(false);
        theGameEngine.pauseGame();

        pause_image.setVisibility(View.VISIBLE);
        pause_menu.setVisibility(View.VISIBLE);
    }

    public void enableFinalizer() {
        finalizerEnabled = true;
        finalizer.setEnabled(true);
        finalizer.setVisibility(View.VISIBLE);
    }

    private void finalizeGame() {
        ImageView explosion = (ImageView) getView().findViewById(R.id.animation);
        TextView win1 = (TextView) getView().findViewById(R.id.youWin1);
        TextView win2 = (TextView) getView().findViewById(R.id.youWin2);

        win1.setVisibility(View.VISIBLE);
        win2.setVisibility(View.VISIBLE);
        explosion.setVisibility(View.VISIBLE);
        explosion.setImageResource(R.drawable.transition_animation);
        AnimationDrawable explosionTransition = (AnimationDrawable) explosion.getDrawable();
        explosionTransition.start();

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                theGameEngine.stopGame();
                ((ScaffoldActivity)getActivity()).gameOver();
            }
        }, 1000);
    }
}
