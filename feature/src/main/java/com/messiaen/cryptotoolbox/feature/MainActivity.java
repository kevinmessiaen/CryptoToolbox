package com.messiaen.cryptotoolbox.feature;

import android.os.Build;
import android.os.Bundle;

import com.messiaen.cryptotoolbox.feature.ui.fragments.CoinListingFragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.transition.TransitionInflater;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        supportPostponeEnterTransition();

        Fragment fragment = new CoinListingFragment();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fragment.setSharedElementReturnTransition(TransitionInflater.from(this)
                    .inflateTransition(R.transition.change_bounds));
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_activity_fragment_content, fragment,
                CoinListingFragment.class.getName());
        transaction.commit();
    }
}
