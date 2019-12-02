package dadm.scaffold.input;

import android.content.Context;
import android.media.Image;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import dadm.scaffold.R;
import dadm.scaffold.ScaffoldActivity;
import dadm.scaffold.engine.Vector;

public class JoystickInputController extends InputController {

    private float startingPositionX;
    private float startingPositionY;

    private final double maxDistance;
    private boolean enabled = true;
    private boolean pause = false;

    private Handler handler;

    ImageView fireButton;

    ImageView bigCircleImg;
    int bigCircleResource;

    ImageView smallCircleImg;
    int smallCircleResource;

    public JoystickInputController(View view, Context context) {
        view.findViewById(R.id.joystick_main).setOnTouchListener(new JoystickTouchListener());
        view.findViewById(R.id.joystick_touch).setOnTouchListener(new FireButtonTouchListener());

        double pixelFactor = view.getHeight() / 400d;
        maxDistance = 50*pixelFactor;

        fireButton = (ImageView) view.findViewById(R.id.firebutton);

        bigCircleImg = (ImageView) view.findViewById(R.id.bigCircle);
        bigCircleResource = context.getResources().getIdentifier("@drawable/circulo_grande_transparente", "drawable", context.getPackageName());

        smallCircleImg = (ImageView) view.findViewById(R.id.smallCircle);
        smallCircleResource = context.getResources().getIdentifier("@drawable/circulo_pequeno", "drawable", context.getPackageName());
    }

    private class JoystickTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if(!pause) {
                int action = event.getActionMasked();
                if (action == MotionEvent.ACTION_DOWN) {
                    startingPositionX = event.getX(0);
                    startingPositionY = event.getY(0);

                    bigCircleImg.setImageResource(bigCircleResource);
                    bigCircleImg.setX(startingPositionX - bigCircleImg.getWidth() / 2);
                    bigCircleImg.setY(startingPositionY - bigCircleImg.getHeight() / 2);
                } else if (action == MotionEvent.ACTION_UP) {
                    horizontalFactor = 0;
                    verticalFactor = 0;

                    bigCircleImg.setImageResource(android.R.color.transparent);
                    smallCircleImg.setImageResource(android.R.color.transparent);
                } else if (action == MotionEvent.ACTION_MOVE) {
                    // Get the proportion to the max
                    horizontalFactor = (event.getX(0) - startingPositionX) / maxDistance;
                    verticalFactor = (event.getY(0) - startingPositionY) / maxDistance;

                    Vector vector = new Vector(horizontalFactor, verticalFactor);

                    vector.clamp(1);

                    horizontalFactor = vector.x;
                    verticalFactor = vector.y;

                    smallCircleImg.setImageResource(smallCircleResource);
                    smallCircleImg.setX((float) (horizontalFactor * maxDistance) + startingPositionX - smallCircleImg.getWidth() / 2);
                    smallCircleImg.setY((float) (verticalFactor * maxDistance) + startingPositionY - smallCircleImg.getHeight() / 2);
                }
            }
            return true;
        }

    }

    private class FireButtonTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if(enabled && !pause) {
                int action = event.getActionMasked();
                if (action == MotionEvent.ACTION_DOWN) {
                    fireButton.setImageAlpha(100);
                } else if (action == MotionEvent.ACTION_UP) {
                    isFiringAlt = true;
                    enabled = false;

                    handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            fireButton.setImageAlpha(255);
                            enabled = true;
                        }
                    }, 5000);
                }
            }
            return true;
        }
    }

    @Override
    public void onPause(){
        pause = true;
        fireButton.setImageAlpha(100);
    }

    @Override
    public void onResume(){
        if(enabled) fireButton.setImageAlpha(255);
        pause = false;
    }
}

