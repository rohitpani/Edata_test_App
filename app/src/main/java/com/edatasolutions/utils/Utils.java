package com.edatasolutions.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.edatasolutions.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static boolean licenseValidation(String str) {
        String regex = "^(([A-Z]{1}[0-9]{7})$)";

        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    public static boolean dobdateValidate(String date) {
        boolean result = false;
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
        try {
            Date parseddate = sdf.parse(date);
            Calendar c2 = Calendar.getInstance();
            c2.add(Calendar.YEAR, -13);

            Calendar c3 = Calendar.getInstance();
            c3.add(Calendar.YEAR, -100);
            Date dateObj2 = new Date(System.currentTimeMillis());
            assert parseddate != null;
            if (parseddate.before(c2.getTime()) && parseddate.after(c3.getTime())) {
                result = true;
            }

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }

    public static boolean isWeekend(String date) {
        boolean result = false;
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");

        try {
            Date parseddate = sdf.parse(date);

            Calendar cal = Calendar.getInstance();
            cal.setTime(parseddate);

            int day = cal.get(Calendar.DAY_OF_WEEK);
            return day == Calendar.SATURDAY || day == Calendar.SUNDAY;

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return result;
    }

    public static ProgressDialog pDialog = null;

    public static void hideKeyboardFromFragment(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isAcceptingText()) {

            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(),
                    0
            );
        }
    }


//    public static void makeTextViewResizable(final TextView tv, final int maxLine, final String expandText, final boolean viewMore) {
//
//        if (tv.getTag() == null) {
//            tv.setTag(tv.getText());
//        }
//        ViewTreeObserver vto = tv.getViewTreeObserver();
//        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//
//            @SuppressWarnings("deprecation")
//            @Override
//            public void onGlobalLayout() {
//
//                ViewTreeObserver obs = tv.getViewTreeObserver();
//                obs.removeGlobalOnLayoutListener(this);
//                if (maxLine == 0) {
//                    int lineEndIndex = tv.getLayout().getLineEnd(0);
//                    String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
//                    tv.setText(text);
//                    tv.setMovementMethod(LinkMovementMethod.getInstance());
//                    tv.setText(addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText, viewMore), TextView.BufferType.SPANNABLE);
//                } else if (maxLine > 0 && tv.getLineCount() >= maxLine) {
//                    int lineEndIndex = tv.getLayout().getLineEnd(maxLine - 1);
//                    String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
//                    tv.setText(text);
//                    tv.setMovementMethod(LinkMovementMethod.getInstance());
//                    tv.setText(
//                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText,
//                                    viewMore), TextView.BufferType.SPANNABLE);
//                } else {
//                    int lineEndIndex = tv.getLayout().getLineEnd(tv.getLayout().getLineCount() - 1);
//                    String text = tv.getText().subSequence(0, lineEndIndex) + " " + expandText;
//                    tv.setText(text);
//                    tv.setMovementMethod(LinkMovementMethod.getInstance());
//                    tv.setText(
//                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, lineEndIndex, expandText,
//                                    viewMore), TextView.BufferType.SPANNABLE);
//                }
//            }
//        });
//
//    }

//    private static SpannableStringBuilder addClickablePartTextViewResizable(final Spanned strSpanned, final TextView tv,
//                                                                            final int maxLine, final String spanableText, final boolean viewMore) {
//        String str = strSpanned.toString();
//        SpannableStringBuilder ssb = new SpannableStringBuilder(strSpanned);
//
//        if (str.contains(spanableText)) {
//
//
//            ssb.setSpan(new MySpannable(false) {
//                @Override
//                public void onClick(View widget) {
//                    if (viewMore) {
//                        tv.setLayoutParams(tv.getLayoutParams());
//                        tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
//                        tv.invalidate();
//                        makeTextViewResizable(tv, -1, "See Less", false);
//                    } else {
//                        tv.setLayoutParams(tv.getLayoutParams());
//                        tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
//                        tv.invalidate();
//                        makeTextViewResizable(tv, 3, ".. See More", true);
//                    }
//                }
//            }, str.indexOf(spanableText), str.indexOf(spanableText) + spanableText.length(), 0);
//
//        }
//        return ssb;
//
//    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void showAlertMessage(Context mContext, String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        alertDialog.setTitle("Alert");
        alertDialog.setMessage(message);
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialog.show();
    }

    //--- added by Dalsukh --- //
    public static String getFilePath(Context context, Uri uri) {
        String selection = null;
        String[] selectionArgs = null;
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        if (Build.VERSION.SDK_INT >= 19 && DocumentsContract.isDocumentUri(context.getApplicationContext(), uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            } else if (isDownloadsDocument(uri)) {
                try {
                    final String id = DocumentsContract.getDocumentId(uri);
                    uri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                    return getDataColumn(context, uri, null, null);
                } catch (Exception e) {
                    e.printStackTrace();
                    List<String> segments = uri.getPathSegments();
                    if (segments.size() > 1) {
                        String rawPath = segments.get(1);
                        if (!rawPath.startsWith("/")) {
                            return rawPath.substring(rawPath.indexOf("/"));
                        } else {
                            return rawPath;
                        }
                    }
                }
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("image".equals(type)) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                } else if ("document".equals(type)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        uri = MediaStore.Downloads.EXTERNAL_CONTENT_URI;
                    }
                }
                selection = "_id=?";
                selectionArgs = new String[]{
                        split[1]
                };
                return getDataColumn(context, uri, selection,
                        selectionArgs);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            if (isGooglePhotosUri(uri)) {
                return uri.getLastPathSegment();
            }
            String[] projection = {
                    MediaStore.Images.Media.DATA
            };
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver()
                        .query(uri, projection, selection, selectionArgs, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null)
                    cursor.close();
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public static String getDataColumn(Context context, Uri uri,
                                       String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection,
                    selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return "noPath";
    }


    public static androidx.appcompat.app.AlertDialog.Builder showPopUpMessage(Context context, String message) {
        androidx.appcompat.app.AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new androidx.appcompat.app.AlertDialog.Builder(context, android.R.style.ThemeOverlay_Material_Dialog_Alert);
        } else {
            builder = new androidx.appcompat.app.AlertDialog.Builder(context);
        }
        builder.setTitle(R.string.app_name)
                .setMessage(message);
        builder.setPositiveButton("OK", null);
        return builder;
    }

    public static void showProgress(Context context) {
        try {
            if (Utils.pDialog != null && Utils.pDialog.isShowing())
                Utils.pDialog.dismiss();

            if (Utils.pDialog == null) {
                Utils.pDialog = new ProgressDialog(context);
                Utils.pDialog.setMessage("Loading");
                Utils.pDialog.setCancelable(true);
                Utils.pDialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void dismissProgress() {
        if (Utils.pDialog != null && Utils.pDialog.isShowing())
            Utils.pDialog.dismiss();
        Utils.pDialog = null;
    }

    public static String getCurrDateInDDMMYYYY() {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        return df.format(c);
    }

    public static String getDateInFormat(String inDateFormat, String outDateFormat, String last_sync) {
        String outputDateStr = "";
        @SuppressLint("SimpleDateFormat") DateFormat inputFormat = new SimpleDateFormat(inDateFormat);
        @SuppressLint("SimpleDateFormat") DateFormat outputFormat = new SimpleDateFormat(outDateFormat);

        try {
            assert last_sync != null;
            Date date = inputFormat.parse(last_sync);
            assert date != null;
            outputDateStr = outputFormat.format(date);
        } catch (Exception ex) {
            Log.e("error", "error");
        }
        return outputDateStr;
    }

}
