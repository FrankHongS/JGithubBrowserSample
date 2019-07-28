package com.frankhon.jgithubbrowsersample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.os.PersistableBundle;

import com.frankhon.jgithubbrowsersample.ui.search.SearchFragment;
import com.frankhon.jgithubbrowsersample.util.NavigationController;

public class MainActivity extends AppCompatActivity {

    private NavigationController navigationController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigationController = new NavigationController(this);

        if (savedInstanceState == null) {
            navigationController.init(new SearchFragment());
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // save Fragment here, do not instantiate another Fragment which is the same as the previous
    }

    public void navigateTo(Fragment fragment) {
        navigationController.navigateTo(fragment);
    }
}
