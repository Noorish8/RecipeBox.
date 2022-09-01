package com.brainque.cookry;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.brainque.cookry.databinding.ActivityMainBinding;
import com.brainque.fragment.FavouriteFragment;
import com.brainque.fragment.HomeFragment;
import com.brainque.fragment.SettingFragment;
import com.brainque.item.ItemAbout;
import com.brainque.util.API;
import com.brainque.util.Constant;
import com.brainque.util.JsonUtils;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.ixidev.gdpr.GDPRChecker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

public class ActivityMain extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    //   NavigationView navigationView;
    Toolbar toolbar;
    private FragmentManager fragmentManager;
    ArrayList<ItemAbout> mListItem;
    JsonUtils jsonUtils;
    LinearLayout adLayout;
    MyApplication MyApp;
    private ProgressBar progressBar;
    private LinearLayout lyt_not_found;
    TextView header_tag;
    int versionCode;
    LinearLayout logout, profile;

    ImageView dot_fav, dot_home, dot_user, dot_setting, img_home, img_setting, img_favourite, img_user;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        MyApp = MyApplication.getAppInstance();
        JsonUtils.setStatusBarGradiant(ActivityMain.this);
        versionCode = BuildConfig.VERSION_CODE;


        dot_home = binding.drawerLayout.findViewById(R.id.img_dot_home);
        img_home = binding.drawerLayout.findViewById(R.id.img_home);

        dot_fav = binding.drawerLayout.findViewById(R.id.img_dot_favourite);
        img_favourite = binding.drawerLayout.findViewById(R.id.img_favourite);

        dot_setting = binding.drawerLayout.findViewById(R.id.img_dot_setting);
        img_setting = binding.drawerLayout.findViewById(R.id.img_setting);

        img_user = binding.drawerLayout.findViewById(R.id.img_user);
        dot_user = binding.drawerLayout.findViewById(R.id.img_dot_user);


        visible_gone();

        dot_home.setVisibility(View.VISIBLE);
        img_home.setColorFilter(getResources().getColor(R.color.light_green));

        toolbar.post(new Runnable() {
            @Override
            public void run() {
                Drawable d = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_drawer, null);
                toolbar.setNavigationIcon(d);
            }
        });
        jsonUtils = new JsonUtils(this);
        jsonUtils.forceRTLIfSupported(getWindow());

        mDrawerLayout = findViewById(R.id.drawer_layout);


        //    navigationView = findViewById(R.id.nav_view);

        //   View headerView = binding.navigationView.getHeaderView(0);
        //home_latter = (ImageView) headerView.findViewById(R.id.home_latter);
        //   View hView = navigationView.inflateHeaderView(R.layout.nav_header);
        //   View hView = navigationView.inflateHeaderView(R.layout.side_bar);
        // header_tag = hView.findViewById(R.id.header_tag);

        binding.drawerLayout.findViewById(R.id.ll_bottom_profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profile_screen();
            }
        });

        binding.drawerLayout.findViewById(R.id.ll_setting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SettingFragment settingFragment = new SettingFragment();
                loadFrag(settingFragment, getString(R.string.menu_setting), fragmentManager);
                mDrawerLayout.closeDrawers();

                visible_gone();
                dot_setting.setVisibility(View.VISIBLE);
                img_setting.setColorFilter(getResources().getColor(R.color.light_green));
            }
        });

        binding.drawerLayout.findViewById(R.id.ll_home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeFragment homeFragment = new HomeFragment();
                loadFrag(homeFragment, getString(R.string.menu_home), fragmentManager);
                mDrawerLayout.closeDrawers();

                visible_gone();
                dot_home.setVisibility(View.VISIBLE);
                img_home.setColorFilter(getResources().getColor(R.color.light_green));

            }
        });

        binding.drawerLayout.findViewById(R.id.ll_favourite).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FavouriteFragment favoriteFragment = new FavouriteFragment();
                loadFrag(favoriteFragment, getString(R.string.menu_favorite), fragmentManager);
                mDrawerLayout.closeDrawers();

                visible_gone();
                dot_fav.setVisibility(View.VISIBLE);
                img_favourite.setColorFilter(getResources().getColor(R.color.light_green));
            }
        });

        binding.navigationView.findViewById(R.id.ll_home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeFragment homeFragment = new HomeFragment();
                loadFrag(homeFragment, getString(R.string.menu_home), fragmentManager);
                mDrawerLayout.closeDrawers();
            }
        });

        binding.navigationView.findViewById(R.id.ll_latest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(getApplicationContext(), CommonActivity.class).putExtra("fragment_name", "latest");
                startActivity(intent2);
                mDrawerLayout.closeDrawers();

            }
        });
        binding.navigationView.findViewById(R.id.ll_categories).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(getApplicationContext(), CommonActivity.class).putExtra("fragment_name", "categories");
                startActivity(intent2);
//                MostViewFragment mostViewFragment = new MostViewFragment();
//                loadFrag(mostViewFragment, getString(R.string.menu_most), fragmentManager);
                mDrawerLayout.closeDrawers();
            }
        });

        binding.navigationView.findViewById(R.id.ll_most_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(getApplicationContext(), CommonActivity.class).putExtra("fragment_name", "most_view");
                startActivity(intent2);
//                MostViewFragment mostViewFragment = new MostViewFragment();
//                loadFrag(mostViewFragment, getString(R.string.menu_most), fragmentManager);
                mDrawerLayout.closeDrawers();
            }
        });

        binding.navigationView.findViewById(R.id.ll_saved).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FavouriteFragment favoriteFragment = new FavouriteFragment();
                loadFrag(favoriteFragment, getString(R.string.menu_favorite), fragmentManager);
                mDrawerLayout.closeDrawers();
            }
        });

        binding.navigationView.findViewById(R.id.ll_profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profile_screen();
            }
        });

        binding.navigationView.findViewById(R.id.img_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.closeDrawers();
            }
        });

        binding.navigationView.findViewById(R.id.ll_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        binding.navigationView.findViewById(R.id.ll_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (MyApp.getUserType()) {
                    case "Normal":
                        Logout();
                        break;
                    case "Google":
                        logoutG();
                        break;
                    case "Facebook":
                        LoginManager.getInstance().logOut();
                        MyApp.saveIsLogin(false);
                        MyApp.setUserId("");
                        Intent intent2 = new Intent(getApplicationContext(), SignInActivity.class);
                        intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent2);
                        finish();
                        break;
                }

            }
        });


        binding.drawerLayout.findViewById(R.id.bottom_navigation_bar);

        adLayout = findViewById(R.id.adview);
        lyt_not_found = findViewById(R.id.lyt_not_found);
        progressBar = findViewById(R.id.progressBar);

        mListItem = new ArrayList<>();

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(), //Insert your own package name.
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException ignored) {
        }

        JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API());
        jsObj.addProperty("method_name", "get_app_details");
        if (JsonUtils.isNetworkAvailable(ActivityMain.this)) {
            new MyTaskDev(API.toBase64(jsObj.toString())).execute(Constant.API_URL);
        } else {
            showToast(getString(R.string.network_msg));
        }

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        mDrawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        if (binding.navigationView != null) {
            setupDrawerContent(binding.navigationView);
        }
        fragmentManager = getSupportFragmentManager();

    }

    private void setupDrawerContent(final NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        int id = menuItem.getItemId();
                        switch (id) {
                            /*case R.id.nav_home:
                                HomeFragment homeFragment = new HomeFragment();
                                loadFrag(homeFragment, getString(R.string.menu_home), fragmentManager);
                                mDrawerLayout.closeDrawers();
                                break;
                            case R.id.nav_latest:
                                LatestFragment latestFragment = new LatestFragment();
                                loadFrag(latestFragment, getString(R.string.menu_latest), fragmentManager);
                                mDrawerLayout.closeDrawers();
                                break;
                            case R.id.nav_cat:
                                CategoryFragment categoryFragment = new CategoryFragment();
                                loadFrag(categoryFragment, getString(R.string.menu_category), fragmentManager);
                                mDrawerLayout.closeDrawers();
                                break;
                            case R.id.nav_most:
                                MostViewFragment mostViewFragment = new MostViewFragment();
                                loadFrag(mostViewFragment, getString(R.string.menu_most), fragmentManager);
                                mDrawerLayout.closeDrawers();
                                break;
                            case R.id.nav_fav:
                                FavoriteFragment favoriteFragment = new FavoriteFragment();
                                loadFrag(favoriteFragment, getString(R.string.menu_favorite), fragmentManager);
                                mDrawerLayout.closeDrawers();
                                break;*/

                            case R.id.nav_setting:
                                SettingFragment settingFragment = new SettingFragment();
                                loadFrag(settingFragment, getString(R.string.menu_setting), fragmentManager);
                                mDrawerLayout.closeDrawers();
                                break;
                           /* case R.id.menu_go_login:
                                Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                                return true;

                            case R.id.menu_go_logout:
                                switch (MyApp.getUserType()) {
                                    case "Normal":
                                        Logout();
                                        break;
                                    case "Google":
                                        logoutG();
                                        break;
                                    case "Facebook":
                                        LoginManager.getInstance().logOut();
                                        MyApp.saveIsLogin(false);
                                        MyApp.setUserId("");
                                        Intent intent2 = new Intent(getApplicationContext(), SignInActivity.class);
                                        intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent2);
                                        finish();
                                        break;
                                }
                                mDrawerLayout.closeDrawers();
                                return true;*/
                        }
                        return true;
                    }
                });


    }

    public void highLightNavigation(int position) {
        binding.navigationView.getMenu().getItem(position).setChecked(true);
    }

    public void loadFrag(Fragment f1, String name, FragmentManager fm) {
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment1, f1, name);
        ft.commitAllowingStateLoss();
        setToolbarTitle(name);
    }

    public void setToolbarTitle(String Title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(Title);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class MyTaskDev extends AsyncTask<String, Void, String> {

        String base64;

        private MyTaskDev(String base64) {
            this.base64 = base64;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress(true);
        }

        @Override
        protected String doInBackground(String... params) {
            return JsonUtils.getJSONString(params[0], base64);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            showProgress(false);
            if (null == result || result.length() == 0) {
                lyt_not_found.setVisibility(View.VISIBLE);
            } else {
                try {
                    JSONObject mainJson = new JSONObject(result);
                    JSONArray jsonArray = mainJson.getJSONArray(Constant.ARRAY_NAME);
                    JSONObject objJson;

                    for (int i = 0; i < jsonArray.length(); i++) {
                        objJson = jsonArray.getJSONObject(i);
                        if (objJson.has("status")) {
                            showToast(getString(R.string.no_data));
                        } else {
                            ItemAbout itemAbout = new ItemAbout();

                            itemAbout.setappDevelop(objJson.getString(Constant.APP_DEVELOP));
                            itemAbout.setAppTagLine(objJson.getString(Constant.APP_TAGLINE));
                            itemAbout.setappBannerId(objJson.getString(Constant.ADS_BANNER_ID));
                            itemAbout.setappFullId(objJson.getString(Constant.ADS_FULL_ID));
                            itemAbout.setappBannerOn(objJson.getString(Constant.ADS_BANNER_ON_OFF));
                            itemAbout.setappFullOn(objJson.getString(Constant.ADS_FULL_ON_OFF));
                            itemAbout.setappFullPub(objJson.getString(Constant.ADS_PUB_ID));
                            itemAbout.setappFullAdsClick(objJson.getString(Constant.ADS_CLICK));
                            itemAbout.setAppNativeId(objJson.getString(Constant.NATIVE_AD_ID));
                            itemAbout.setAppNativeType(objJson.getString(Constant.NATIVE_TYPE));
                            itemAbout.setAppNativeOnOff(objJson.getString(Constant.NATIVE_AD_ON_OFF));
                            itemAbout.setAppBannerType(objJson.getString(Constant.BANNER_TYPE));
                            itemAbout.setAppFullType(objJson.getString(Constant.FULL_TYPE));
                            Constant.appUpdateVersion = objJson.getInt("app_new_version");
                            Constant.appUpdateUrl = objJson.getString("app_redirect_url");
                            Constant.appUpdateDesc = objJson.getString("app_update_desc");
                            Constant.isAppUpdate = objJson.getBoolean("app_update_status");
                            Constant.isAppUpdateCancel = objJson.getBoolean("cancel_update_status");
                            Constant.SAVE_NATIVE_CLICK_OTHER = objJson.getString("native_other_position");
                            Constant.SAVE_NATIVE_CLICK_CAT = objJson.getString("native_cat_position");
                            mListItem.add(itemAbout);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                setResult();
            }
        }
    }

    private void setResult() {

        if (mListItem.size() != 0) {

            ItemAbout itemAbout = mListItem.get(0);
            Constant.SAVE_ADS_BANNER_ID = itemAbout.getappBannerId();
            Constant.SAVE_ADS_FULL_ID = itemAbout.getappFullId();
            Constant.SAVE_ADS_BANNER_ON_OFF = itemAbout.getappBannerOn();
            Constant.SAVE_ADS_FULL_ON_OFF = itemAbout.getappFullOn();
            Constant.SAVE_ADS_PUB_ID = itemAbout.getappFullPub();
            Constant.SAVE_ADS_CLICK = itemAbout.getappFullAdsClick();
            Constant.SAVE_TAG_LINE = itemAbout.getAppTagLine();
            //       header_tag.setText(Constant.SAVE_TAG_LINE);
            Constant.SAVE_ADS_NATIVE_ON_OFF = itemAbout.getAppNativeOnOff();
            Constant.SAVE_NATIVE_ID = itemAbout.getAppNativeId();
            Constant.SAVE_BANNER_TYPE = itemAbout.getAppBannerType();
            Constant.SAVE_FULL_TYPE = itemAbout.getAppFullType();
            Constant.SAVE_NATIVE_TYPE = itemAbout.getAppNativeType();

        }
        if (Constant.SAVE_BANNER_TYPE.equals("admob")) {
            checkForConsent();
        } else {
            JsonUtils.showNonPersonalizedAdsFB(adLayout, ActivityMain.this);
        }

        HomeFragment homeFragment = new HomeFragment();
        loadFrag(homeFragment, getString(R.string.menu_home), fragmentManager);

        if (Constant.appUpdateVersion > versionCode && Constant.isAppUpdate) {
            newUpdateDialog();
        }
    }

    private void showProgress(boolean show) {
        if (show) {
            progressBar.setVisibility(View.VISIBLE);
            lyt_not_found.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    private void newUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityMain.this);
        builder.setTitle(getString(R.string.app_update_title));
        builder.setCancelable(false);
        builder.setMessage(Constant.appUpdateDesc);
        builder.setPositiveButton(getString(R.string.app_update_btn), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(Constant.appUpdateUrl)));
            }
        });
        if (Constant.isAppUpdateCancel) {
            builder.setNegativeButton(getString(R.string.app_cancel_btn), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                }
            });
        }
        builder.setIcon(R.mipmap.ic_launcher_background);
        builder.show();
    }


    public void showToast(String msg) {
        Toast.makeText(ActivityMain.this, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else if (fragmentManager.getBackStackEntryCount() != 0) {
            String tag = fragmentManager.getFragments().get(fragmentManager.getBackStackEntryCount() - 1).getTag();
            setToolbarTitle(tag);
            super.onBackPressed();
        } else {
            AlertDialog.Builder alert = new AlertDialog.Builder(ActivityMain.this);
            alert.setTitle(getString(R.string.app_name));
            alert.setIcon(R.drawable.ic_info_icon);
            alert.setMessage(getString(R.string.exit_msg));
            alert.setPositiveButton(getString(R.string.exit_yes),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            finish();
                        }
                    });
            alert.setNegativeButton(getString(R.string.exit_no), new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                }
            });
            alert.show();
        }
    }

    public void checkForConsent() {
        new GDPRChecker()
                .withContext(ActivityMain.this)
                .withPrivacyUrl(getString(R.string.gdpr_privacy_link))
                .withPublisherIds(Constant.SAVE_ADS_PUB_ID)
                //.withTestMode("FA0F55855A8169A47EB9D713413B9FE9")
                .check();
        JsonUtils.ShowBannerAds(ActivityMain.this, adLayout);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
        return true;
    }

    public void logoutG() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(ActivityMain.this, gso);

        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        MyApp.saveIsLogin(false);
                        MyApp.setUserId("");
                        Intent intent = new Intent(ActivityMain.this, SignInActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                });
    }

    private void Logout() {

        final PrettyDialog dialog = new PrettyDialog(ActivityMain.this);
        dialog.setTitle(getString(R.string.dialog_logout))
                .setTitleColor(R.color.dark_green)
                .setMessage(getString(R.string.logout_msg))
                .setMessageColor(R.color.light_green)
                .setAnimationEnabled(false)
                .setIcon(R.drawable.ic_info_icon, R.color.white, new PrettyDialogCallback() {
                    @Override
                    public void onClick() {
                        dialog.dismiss();
                    }
                })
                .addButton(getString(R.string.dialog_ok), R.color.green_abt, R.color.dark_green, new PrettyDialogCallback() {
                    @Override
                    public void onClick() {
                        dialog.dismiss();
                        MyApp.saveIsLogin(false);
                        MyApp.setUserId("");
                        Intent intent_login = new Intent(ActivityMain.this, SignInActivity.class);
                        intent_login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent_login);
                        finish();
                    }
                })
                .addButton(getString(R.string.dialog_no), R.color.light_green, R.color.green_light_btn, new PrettyDialogCallback() {
                    @Override
                    public void onClick() {
                        dialog.dismiss();
                    }
                });
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (MyApp.getIsLogin()) {

            binding.navigationView.findViewById(R.id.ll_logout).setVisibility(View.VISIBLE);
            binding.navigationView.findViewById(R.id.ll_login).setVisibility(View.GONE);

            //         navigationView.getMenu().findItem(R.id.menu_go_login).setVisible(false);
            //         navigationView.getMenu().findItem(R.id.menu_go_logout).setVisible(true);
        } else {
            binding.navigationView.findViewById(R.id.ll_logout).setVisibility(View.GONE);
            binding.navigationView.findViewById(R.id.ll_login).setVisibility(View.VISIBLE);
            //     navigationView.getMenu().findItem(R.id.menu_go_login).setVisible(true);
            //       navigationView.getMenu().findItem(R.id.menu_go_logout).setVisible(false);
        }
    }


    private void profile_screen() {
        mDrawerLayout.closeDrawers();
        if (MyApp.getIsLogin()) {
            Intent intent_edit = new Intent(ActivityMain.this, ProfileEditActivity.class);
            startActivity(intent_edit);

            visible_gone();
            dot_user.setVisibility(View.VISIBLE);
            img_user.setColorFilter(getResources().getColor(R.color.light_green));

        } else {
            final PrettyDialog dialog = new PrettyDialog(ActivityMain.this);
            dialog.setTitle(getString(R.string.dialog_warning))
                    .setTitleColor(R.color.dialog_text)
                    .setMessage(getString(R.string.login_require))
                    .setMessageColor(R.color.dialog_text)
                    .setAnimationEnabled(false)
                    .setIcon(R.drawable.ic_logout_d, R.color.white, new PrettyDialogCallback() {
                        @Override
                        public void onClick() {
                            dialog.dismiss();
                        }
                    })
                    .addButton(getString(R.string.dialog_ok), R.color.dialog_white_text, R.color.dark_green, new PrettyDialogCallback() {
                        @Override
                        public void onClick() {
                            dialog.dismiss();
                            Intent intent_login = new Intent(ActivityMain.this, SignInActivity.class);
                            intent_login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent_login);
                        }
                    })
                    .addButton(getString(R.string.dialog_no), R.color.dark_green, R.color.green_light_btn, new PrettyDialogCallback() {
                        @Override
                        public void onClick() {
                            dialog.dismiss();
                        }
                    });
            dialog.setCancelable(false);
            dialog.show();
        }
    }


    private void visible_gone() {
        dot_fav.setVisibility(View.GONE);
        dot_home.setVisibility(View.GONE);
        dot_user.setVisibility(View.GONE);
        dot_setting.setVisibility(View.GONE);

        img_home.setColorFilter(getResources().getColor(R.color.dark_bottom_color));
        img_favourite.setColorFilter(getResources().getColor(R.color.dark_bottom_color));

        img_setting.setColorFilter(getResources().getColor(R.color.dark_bottom_color));
        img_user.setColorFilter(getResources().getColor(R.color.dark_bottom_color));
    }
}