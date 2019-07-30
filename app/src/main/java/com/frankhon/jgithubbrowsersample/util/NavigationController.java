package com.frankhon.jgithubbrowsersample.util;

import android.util.Pair;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.frankhon.jgithubbrowsersample.R;

/**
 * Created by Frank Hon on 2019-07-27 01:40.
 * E-mail: frank_hon@foxmail.com
 */
public class NavigationController {

    private FragmentManager fragmentManager;

    public NavigationController(AppCompatActivity activity) {
        this.fragmentManager = activity.getSupportFragmentManager();
    }

    public void init(Fragment fragment) {
        fragmentManager.beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit();
    }

    public void navigateTo(Fragment target) {
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, target)
                .addToBackStack(null)
                .commit();
    }

    public void navigateTo(Fragment target, Pair<View,String> sharedElement) {
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, target)
                .addSharedElement(sharedElement.first,sharedElement.second)
                .addToBackStack(null)
                .commit();
    }
}
