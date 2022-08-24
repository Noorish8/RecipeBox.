package com.brainque.cookry;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;

import com.brainque.cookry.databinding.ActivityCommonBinding;
import com.brainque.fragment.CategoryFragment;
import com.brainque.fragment.LatestFragment;
import com.brainque.fragment.MostViewFragment;

public class CommonActivity extends AppCompatActivity {

    ActivityCommonBinding binding;
    private FragmentManager fragmentManager;

    String fragment_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCommonBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        fragmentManager = getSupportFragmentManager();

        if (getIntent() != null) {
            fragment_name = getIntent().getStringExtra("fragment_name");
        }


        if (fragment_name.equals("most_view")) {
            MostViewFragment mostViewFragment = new MostViewFragment();
            loadFrag(mostViewFragment, getString(R.string.menu_most), fragmentManager);
        } else if (fragment_name.equals("categories")) {
            CategoryFragment categoryFragment = new CategoryFragment();
            loadFrag(categoryFragment, getString(R.string.menu_category), fragmentManager);
        } else if (fragment_name.equals("latest")) {
            LatestFragment latestFragment = new LatestFragment();
            loadFrag(latestFragment, getString(R.string.menu_latest), fragmentManager);
        }
    }

    public void loadFrag(Fragment f1, String name, FragmentManager fm) {
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }

        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment1, f1, name);
        ft.commitAllowingStateLoss();
        binding.txtTitle.setText(name);
    }

    public void img_back(View v) {
        onBackPressed();
    }
}