package dadm.scaffold;

import android.media.AudioManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import dadm.scaffold.counter.GameFragment;
import dadm.scaffold.counter.GameOverFragment;
import dadm.scaffold.counter.MainMenuFragment;
import dadm.scaffold.counter.ShipSelectionFragment;
import dadm.scaffold.sound.SoundManager;

public class ScaffoldActivity extends AppCompatActivity {

    private static final String TAG_FRAGMENT = "content";

    private SoundManager soundManager;

    public int last_id = 0;
    public int ship_id = 0;
    public int dark_ship_id = 0;

    public GameFragment gameFrag;
    public ShipSelectionFragment selectionFrag;
    public MainMenuFragment menuFrag;
    public GameOverFragment gameOverFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scaffold);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new MainMenuFragment(), TAG_FRAGMENT)
                    .commit();
        }
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        soundManager = new SoundManager(getApplicationContext());
    }

    public SoundManager getSoundManager() {
        return soundManager;
    }

    public void startGame(int id) {
        // Navigate the the game fragment, which makes the start automatically

        last_id = id;

        //Set ship ids
        switch (id){
            case 1:
                ship_id = R.drawable.nave1_claro;
                dark_ship_id = R.drawable.nave1_oscuro;
                break;
            case 2:
                ship_id = R.drawable.nave2_claro;
                dark_ship_id = R.drawable.nave2_oscuro;
                break;
            case 3:
                ship_id = R.drawable.nave3_claro;
                dark_ship_id = R.drawable.nave3_oscuro;
                break;
        }

        gameFrag = new GameFragment();
        navigateToFragment(gameFrag);
    }

    public void shipSelection(){
        selectionFrag = new ShipSelectionFragment();
        navigateToFragment(selectionFrag);
    }

    public void mainMenu(){
        menuFrag = new MainMenuFragment();
        navigateToFragment(menuFrag);
    }

    public void gameOver() {
        gameOverFrag = new GameOverFragment();
        navigateToFragment(gameOverFrag);
    }

    private void navigateToFragment(BaseFragment dst) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, dst, TAG_FRAGMENT)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onBackPressed() {
        final BaseFragment fragment = (BaseFragment) getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT);
        if (fragment == null || !fragment.onBackPressed()) {
            super.onBackPressed();
        }
    }

    public void navigateBack() {
        // Do a push on the navigation history
        super.onBackPressed();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            View decorView = getWindow().getDecorView();
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LOW_PROFILE);
            }
            else {
                decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            }
        }
    }
}
