package com.brainque.cookry;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.brainque.fragment.SettingFragment;
import com.brainque.item.ItemAbout;
import com.brainque.util.API;
import com.brainque.util.Constant;
import com.brainque.util.JsonUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;


public class AboutUsActivity extends AppCompatActivity {

    TextView txtAppName, txtVersion, txtCompany, txtEmail, txtWebsite, txtContact;
    TextView imgAppLogo;
    View img_back;
    ArrayList<ItemAbout> mListItem;
    ScrollView mScrollView;
    ProgressBar mProgressBar;
    WebView webView;
    Toolbar toolbar;
    JsonUtils jsonUtils;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        JsonUtils.setStatusBarGradiant(AboutUsActivity.this);
//        toolbar = findViewById(R.id.toolbar);
//        toolbar.setTitle(getString(R.string.menu_about));
        //  setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        jsonUtils = new JsonUtils(this);
        jsonUtils.forceRTLIfSupported(getWindow());

        txtAppName = findViewById(R.id.txt_cook);
        txtVersion = findViewById(R.id.txt_virsion);
        txtCompany = findViewById(R.id.txt_company);
        txtEmail = findViewById(R.id.txt_email);
        txtWebsite = findViewById(R.id.txt_website);
        txtContact = findViewById(R.id.txt_contact);
        imgAppLogo = findViewById(R.id.txt_about);
        webView = findViewById(R.id.webView);
        img_back = findViewById(R.id.img_back);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
//                Intent intent = new Intent(AboutUsActivity.this, SettingFragment.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//                //finish();
            }
        });

        mScrollView = findViewById(R.id.scrollView);
        mProgressBar = findViewById(R.id.progressBar);

        mListItem = new ArrayList<>();

        JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API());
        jsObj.addProperty("method_name", "get_app_details");
        if (JsonUtils.isNetworkAvailable(AboutUsActivity.this)) {
            //new MyTaskAbout(API.toBase64(jsObj.toString())).execute(Constant.API_URL);
        }

    }

    @SuppressLint("StaticFieldLeak")
    private class MyTaskAbout extends AsyncTask<String, Void, String> {

        String base64;

        private MyTaskAbout(String base64) {
            this.base64 = base64;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            mProgressBar.setVisibility(View.VISIBLE);
            mScrollView.setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(String... params) {
            return JsonUtils.getJSONString(params[0], base64);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            mProgressBar.setVisibility(View.GONE);
            mScrollView.setVisibility(View.VISIBLE);
            if (null == result || result.length() == 0) {
                showToast(getString(R.string.no_data));
            } else {
                try {
                    JSONObject mainJson = new JSONObject(result);
                    JSONArray jsonArray = mainJson.getJSONArray(Constant.ARRAY_NAME);
                    JSONObject objJson;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        objJson = jsonArray.getJSONObject(i);
                        if (objJson.has("status")) {
                            final PrettyDialog dialog = new PrettyDialog(AboutUsActivity.this);
                            dialog.setTitle(getString(R.string.dialog_error))
                                    .setTitleColor(R.color.dialog_text)
                                    .setMessage(getString(R.string.restart_msg))
                                    .setMessageColor(R.color.dialog_text)
                                    .setAnimationEnabled(false)
                                    .setIcon(R.drawable.pdlg_icon_close, R.color.dialog_color, new PrettyDialogCallback() {
                                        @Override
                                        public void onClick() {
                                            dialog.dismiss();
                                            finish();
                                        }
                                    })
                                    .addButton(getString(R.string.dialog_ok), R.color.dialog_white_text, R.color.dialog_color, new PrettyDialogCallback() {
                                        @Override
                                        public void onClick() {
                                            dialog.dismiss();
                                            finish();
                                        }
                                    });
                            dialog.setCancelable(false);
                            dialog.show();
                        } else {
                            ItemAbout itemAbout = new ItemAbout();
                            itemAbout.setAppName(objJson.getString(Constant.APP_NAME));
                            itemAbout.setAppLogo(objJson.getString(Constant.APP_IMAGE));
                            itemAbout.setAppVersion(objJson.getString(Constant.APP_VERSION));
                            itemAbout.setAppAuthor(objJson.getString(Constant.APP_AUTHOR));
                            itemAbout.setAppEmail(objJson.getString(Constant.APP_EMAIL));
                            itemAbout.setAppWebsite(objJson.getString(Constant.APP_WEBSITE));
                            itemAbout.setAppContact(objJson.getString(Constant.APP_CONTACT));
                            itemAbout.setAppDescription(objJson.getString(Constant.APP_DESC));
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
            txtAppName.setText(itemAbout.getAppName());
            txtVersion.setText(itemAbout.getAppVersion());
            txtCompany.setText(itemAbout.getAppAuthor());
            txtEmail.setText(itemAbout.getAppEmail());
            txtWebsite.setText(itemAbout.getAppWebsite());
            txtContact.setText(itemAbout.getAppContact());
            // Picasso.get().load(Constant.IMAGE_PATH_URL + itemAbout.getAppLogo()).into(imgAppLogo);

            String mimeType = "text/html;charset=UTF-8";
            String encoding = "utf-8";
            String htmlText = itemAbout.getAppDescription();

            String text = "<html><head>"
                    + "<style type=\"text/css\">@font-face {font-family: MyFont;src: url(\"file:///android_asset/myfonts/Montserrat-SemiBold.ttf\")}body{font-family: MyFont;color: #8b8b8b;text-align:justify;line-height:1.6}"
                    + "</style></head>"
                    + "<body>"
                    + htmlText
                    + "</body></html>";

            webView.loadDataWithBaseURL(null, text, mimeType, encoding, null);
        }
    }


    public void showToast(String msg) {
        Toast.makeText(AboutUsActivity.this, msg, Toast.LENGTH_LONG).show();
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem menuItem) {
//        if (menuItem.getItemId() == android.R.id.home) {
//            onBackPressed();
//        } else {
//            return super.onOptionsItemSelected(menuItem);
//        }
//        return true;
//    }

//    public void onBackPressed() {
//        onBackPressed();
//    }
}
