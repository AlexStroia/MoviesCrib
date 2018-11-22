package co.alexdev.moviescrib_phase2.activities;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import co.alexdev.moviescrib_phase2.R;

/*Main activity where we can see a list with all the movies*/
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
    }
}
