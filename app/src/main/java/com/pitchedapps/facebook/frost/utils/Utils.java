package com.pitchedapps.facebook.frost.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.customtabs.CustomTabsSession;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.util.Pair;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.pitchedapps.facebook.frost.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

public class Utils {

    private static int statusBarHeight = 60, navBarHeight = 120;

    /**
     * Take screenshot of the activity including the action bar
     *
     * @param activity
     * @return The screenshot of the activity including the action bar
     */
    public static Bitmap takeScreenshot(Activity activity) {
        ViewGroup decor = (ViewGroup) activity.getWindow().getDecorView();
        ViewGroup decorChild = (ViewGroup) decor.getChildAt(0);
        decorChild.setDrawingCacheEnabled(true);
        decorChild.buildDrawingCache();
        Bitmap drawingCache = decorChild.getDrawingCache(true);
        Bitmap bitmap = Bitmap.createBitmap(drawingCache);
        decorChild.setDrawingCacheEnabled(false);
        return bitmap;
    }

    /**
     * Print hash key
     */
    public static void printHashKey(Context context) {
        try {
            String TAG = "com.pitchedapps.facebook.glass";
            PackageInfo info = context.getPackageManager().getPackageInfo(TAG, PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String keyHash = Base64.encodeToString(md.digest(), Base64.DEFAULT);
                Log.d(TAG, "keyHash: " + keyHash);
            }
        } catch (NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    /**
     * Update language
     *
     * @param code The language code. Like: en, cz, iw, ...
     */
    public static void updateLanguage(Context context, String code) {
        Locale locale = new Locale(code);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }

    /**
     * Build alert dialog with properties and data
     *
     * @param pairs
     * @return {@link AlertDialog}
     */
    public static AlertDialog buildProfileResultDialog(Activity activity, Pair<String, String>... pairs) {
        StringBuilder stringBuilder = new StringBuilder();

        for (Pair<String, String> pair : pairs) {
            stringBuilder.append(String.format("<h3>%s</h3>", pair.first));
            stringBuilder.append(String.valueOf(pair.second));
            stringBuilder.append("<br><br>");
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(Html.fromHtml(stringBuilder.toString())).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        return builder.create();
    }

    public static String toHtml(Object object) {
        StringBuilder stringBuilder = new StringBuilder(256);
        try {
            for (Field field : object.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                if (Modifier.isStatic(field.getModifiers())) {
                    continue;
                }
                Object val = field.get(object);
                stringBuilder.append("<b>");
                stringBuilder.append(field.getName().substring(1, field.getName().length()));
                stringBuilder.append(": ");
                stringBuilder.append("</b>");
                stringBuilder.append(val);
                stringBuilder.append("<br>");
            }
        } catch (Exception e) {
            // Do nothing
        }
        return stringBuilder.toString();
    }

    public static byte[] getSamleVideo(Context context, String assetFile) {
        try {
            InputStream inputStream = context.getAssets().open(assetFile);
            return getBytes(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static byte[] getBytes(InputStream input) throws IOException {
        byte[] buffer = new byte[8192];
        int bytesRead;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
        return output.toByteArray();
    }

    public static void t(Activity a, Object o) {
        Log.e("FBFrost", o.toString());
        Toast.makeText(a, o.toString(),
                Toast.LENGTH_LONG).show();
    }

    public static void e(Object o) {
        Log.e("FBFrost", o.toString());
    }

    public static void d(Object o) {
        Log.d("FBFrost", o.toString());
    }

    public static String getAppVersion(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            // this should never happen
            return "Unknown";
        }
    }

    public static void saveStatusBarHeight(int i) {
        statusBarHeight = i;
    }

    public static int getStatusBarHeight() {
        return statusBarHeight;
    }

    public static void saveNavBarHeight(int i) {
        navBarHeight = i;
    }

    public static int getNavBarHeight() {
        return navBarHeight;
    }

    public static void showSimpleSnackbar(Context context, View location, String text) {
        try {
            Snackbar snackbar = Snackbar.make(location, text,
                    Snackbar.LENGTH_LONG);

            ViewGroup viewgroup = (ViewGroup) snackbar.getView();
            viewgroup.setBackgroundColor(ContextCompat.getColor(context, R.color.facebook_blue));
            viewgroup.setPadding(viewgroup.getPaddingLeft(), viewgroup.getPaddingTop(),
                    viewgroup.getPaddingRight(), viewgroup.getPaddingBottom() + navBarHeight); //Add padding to match top of navbar
            snackbar.show();
        } catch (Exception e) {
            Toast.makeText(context, text,
                    Toast.LENGTH_SHORT).show();
            e("Snackbar error: " + e);
        }
    }

    public static void openLink(Context context, String link) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @SuppressWarnings("ResourceAsColor")
    public static void openLinkInChromeCustomTab(Context context, String link) {
        final CustomTabsClient[] mClient = new CustomTabsClient[1];
        final CustomTabsSession[] mCustomTabsSession = new CustomTabsSession[1];
        CustomTabsServiceConnection mCustomTabsServiceConnection;
        CustomTabsIntent customTabsIntent;

        mCustomTabsServiceConnection = new CustomTabsServiceConnection() {
            @Override
            public void onCustomTabsServiceConnected(ComponentName componentName, CustomTabsClient customTabsClient) {
                mClient[0] = customTabsClient;
                mClient[0].warmup(0L);
                mCustomTabsSession[0] = mClient[0].newSession(null);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mClient[0] = null;
            }
        };

        CustomTabsClient.bindCustomTabsService(context, "com.android.chrome", mCustomTabsServiceConnection);
        customTabsIntent = new CustomTabsIntent.Builder(mCustomTabsSession[0])
                .setToolbarColor(ContextCompat.getColor(context, R.color.facebook_blue))
                .setShowTitle(true)
                .build();

        customTabsIntent.launchUrl((Activity) context, Uri.parse(link));
    }

    public static void sendEmailWithDeviceInfo(Context context) {
        StringBuilder emailBuilder = new StringBuilder();
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + context.getResources().getString(R.string.email_id)));
        intent.putExtra(Intent.EXTRA_SUBJECT, context.getResources().getString(R.string.email_subject));
        emailBuilder.append("Write stuff here\n \n \nOS Version: ").append(System.getProperty("os.version")).append("(").append(Build.VERSION.INCREMENTAL).append(")");
        emailBuilder.append("\nOS API Level: ").append(Build.VERSION.SDK_INT);
        emailBuilder.append("\nDevice: ").append(Build.DEVICE);
        emailBuilder.append("\nManufacturer: ").append(Build.MANUFACTURER);
        emailBuilder.append("\nModel (and Product): ").append(Build.MODEL).append(" (").append(Build.PRODUCT).append(")");
        PackageInfo appInfo = null;
        try {
            appInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        assert appInfo != null;
        emailBuilder.append("\nApp Version Name: ").append(appInfo.versionName);
        emailBuilder.append("\nApp Version Code: ").append(appInfo.versionCode);
        intent.putExtra(Intent.EXTRA_TEXT, emailBuilder.toString());
        context.startActivity(Intent.createChooser(intent, (context.getResources().getString(R.string.send_title))));
    }

    public static void sendEmailFromFrost(Context context, String email) {
        StringBuilder emailBuilder = new StringBuilder();
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + email));
        intent.putExtra(Intent.EXTRA_SUBJECT, context.getResources().getString(R.string.subject_here));
        emailBuilder.append("\n \n \nSent via Frost for Facebook");
        intent.putExtra(Intent.EXTRA_TEXT, emailBuilder.toString());
        context.startActivity(Intent.createChooser(intent, (context.getResources().getString(R.string.send_title))));
    }

    public static String componentFromHttp(String s, String http) {
        return componentFromHttp(s, http, true);
    }

    public static String componentFromHttp(String s, String http, boolean log) {
        if (!http.contains(s)) return null;
        String s2 = http.substring(http.indexOf(s));
        s2 = s2.split(">")[1];
        s2 = s2.split("<")[0];
        if (log) d(s + " " + s2);
        if (s2.equals("null")) return null;
        return s2;
    }

    //Screen size
    public static Point getScreenSize(Context c) {
        WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    public static double getScreenDiagonal(Context c) {
        Point p = getScreenSize(c);
        return Math.sqrt(p.x * p.x + p.y * p.y);
    }

    public static Point getLocation(View v) {
        int[] posXY = new int[2];
        v.getLocationInWindow(posXY);
        return new Point(posXY[0] + v.getWidth()/2, posXY[1] + v.getHeight()/2);
    }
}
