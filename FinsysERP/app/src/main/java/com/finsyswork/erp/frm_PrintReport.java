package com.finsyswork.erp;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.print.PrintAttributes;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.IOException;

public class frm_PrintReport extends AppCompatActivity {

    WebView myWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_print_report);
        //loadPrintReport();
        myWebView = (WebView) findViewById(R.id.webView);
        startWebView(myWebView, fgen.webReportLink);
        //new DownloadFile().execute("http://maven.apache.org/maven-1.x/maven.pdf", "maven.pdf");
        loadPrintReport();
    }

    void loadPrintReport() {
        try {
            myWebView.getSettings().setLoadsImagesAutomatically(true);
            myWebView.getSettings().setJavaScriptEnabled(true);
            myWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            myWebView.setInitialScale(1);
            myWebView.getSettings().setUseWideViewPort(true);
            myWebView.getSettings().setUseWideViewPort(true);
            myWebView.loadUrl(fgen.webReportLink);
        } catch (Exception ex) {
        }

        if (!fgen.mcd.equals("SACL")) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse(fgen.webReportLink), "application/pdf");
            try {
               myWebView.getContext().startActivity(intent);
               finish();
            } catch (ActivityNotFoundException e) {
                //user does not have a pdf viewer installed
            }
        }
    }

    private PrintAttributes getDefaultPrintAttrs() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return null;

        PrintAttributes.MediaSize customSize = new
                PrintAttributes.MediaSize("COIL", "COIL", 5800,40000);
        customSize.asPortrait();

        return new PrintAttributes.Builder()
                .setMediaSize(customSize)
                .setResolution(new PrintAttributes.Resolution("RESOLUTION_ID", "RESOLUTION_ID", 600, 600))
                .setMinMargins(PrintAttributes.Margins.NO_MARGINS)
                .build();

    }

    private void startWebView(WebView webView, String url) {
        webView.setWebViewClient(new WebViewClient() {
            ProgressDialog progressDialog;

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            public void onLoadResource(WebView view, String url) {

                if (progressDialog == null) {
                    progressDialog = new ProgressDialog(frm_PrintReport.this);
                    progressDialog.setMessage("Please wait, loading preview...");
                    progressDialog.show();
                }
            }
            public void onPageFinished(WebView view, String url) {
                try {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                        progressDialog.hide();
                    }

                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }

        });


        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
       // webView.loadUrl("http://maven.apache.org/maven-1.x/maven.pdf");

    }

    public void view(View v)
    {
        File pdfFile = new File(Environment.getRootDirectory() + "/testthreepdf/" + "maven.pdf");  // -> filename = maven.pdf
        Uri path = Uri.fromFile(pdfFile);
        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
        pdfIntent.setDataAndType(path, "application/pdf");
        pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        try{
            startActivity(pdfIntent);
        }catch(ActivityNotFoundException e){
            Toast.makeText(frm_PrintReport.this, "No Application available to view PDF", Toast.LENGTH_SHORT).show();
        }
    }
}


class DownloadFile extends AsyncTask<String, Void, Void> {

    @Override
    protected Void doInBackground(String... strings) {
        String fileUrl = strings[0];   // -> http://maven.apache.org/maven-1.x/maven.pdf
        String fileName = strings[1];  // -> maven.pdf
        String extStorageDirectory = Environment.getRootDirectory().toString();
        File folder = new File(extStorageDirectory, "testthreepdf");
        folder.mkdir();

        File pdfFile = new File(folder, fileName);

        try{
            pdfFile.createNewFile();
        }catch (IOException e){
            e.printStackTrace();
        }
        FileDownloader.downloadFile(fileUrl, pdfFile);
        return null;
    }
}

