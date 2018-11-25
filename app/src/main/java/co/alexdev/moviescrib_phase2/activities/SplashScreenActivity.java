package co.alexdev.moviescrib_phase2.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import co.alexdev.moviescrib_phase2.R;

public class SplashScreenActivity extends AppCompatActivity {

    private ImageView iv_popcorn_splash;
    private ImageView iv_splash_logo;
    private ImageView iv_glasses_big;

    private Animation alpha;
    private Animation fromBottom;
    private Animation fromTop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        iv_splash_logo = findViewById(R.id.ic_splash_logo);
        iv_popcorn_splash = findViewById(R.id.iv_popcorn_splash);
        iv_glasses_big = findViewById(R.id.iv_glasses_big);

        setAnimations();
    }

    private void setAnimations() {
        setBottonAnimation();
        setTopAnimation();
        setAlphaAnimation();
    }

    private void setBottonAnimation() {
        fromBottom = AnimationUtils.loadAnimation(this, R.anim.frombottom);
        iv_popcorn_splash.setAnimation(fromBottom);
    }

    private void setTopAnimation() {
        fromTop = AnimationUtils.loadAnimation(this, R.anim.fromtop);
        iv_glasses_big.setAnimation(fromTop);
    }

    private void setAlphaAnimation() {
        alpha = AnimationUtils.loadAnimation(this, R.anim.fade);
        iv_splash_logo.setAnimation(alpha);
        alpha.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent intent = new Intent(SplashScreenActivity.this, BaseActivity.class);
                startActivity(intent);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
