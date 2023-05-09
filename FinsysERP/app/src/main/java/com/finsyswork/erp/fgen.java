package com.finsyswork.erp;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AlertDialog;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class fgen {
    private static final String TAG = "file";
    public static Context Context;
    public  static Boolean GetRequest=false;
    public static String selected_tile = "", xextra1_for_popup="-",_selectedVal="";
    public  static String xpopup_col1 = "",xpopup_col2 = "",xpopup_col3 = "",xpopup_col4 = "",xpopup_col5 = ""
            ,xpopup_col6 = "",xpopup_col7 = "", xcard_view_name="", scanned_qr_Code= "", xextra1 = "";
    public static String frm_request = "", ximage_name="";
    public static final String sp_mymcd = "mymcd";
    public static final String sp_myuname = "myuname";
    public static final String sp_myyear = "myyear";
    public static final String sp_mypass = "mypass";
    public static final String sp_mbr = "mymbr";
    public static String branchcd = "";
    public static String FileServerip = "";
    public static String mypreferencegroup = "mypref", refresh = "N";
    public static String Serverip = "", iconId = "", squery = "", rptLeveltitle = "", muname = "", mcd = "", muid = "", muserpwd = "", cdt1, cdt2, mq5 = "", contactno
            , department, designation, emailid;
    public static String[] rptLevelHeaders;
    public static String finalresult = "", textseprator = "!~!~!", btnid = "", rptheader = "", senderpage = "", mq = "", hf1col1, hf1col2;
    public static String file_Serverinfo = "mytns2", file_bt = "BTaddress";
    public static int expendpos = 0;
    public static View currentview = null;
    public static ArrayList<team> feedListmain, feedListresult;
    public static String qr_scanned_with_external =  "N";
    public static int xjob_status_call_count = 0, toolbar_click_count=0;
    public static String webReportLink = "";
    public static String x41 = "";


    public static void openRptLevel(Context _context, String _IconID, String _ListQuery, String _Title, String[] _Headers) {
        iconId = _IconID;
        squery = _ListQuery;
        rptLeveltitle = _Title;
        rptLevelHeaders = _Headers;
        //_context.startActivity(new Intent(_context, rptlevel5.class));
    }

    public static String getcurrentTime() {
        Date tdate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String cdate = sdf.format(tdate).toString().trim();
        return cdate;

    }

    public static Double make_double(String Val) {
        Double result = 0.00;
        try {
            if (Val.contains(",")) {
                Val = Val.replace(",", "");
            }
            result = Double.valueOf(Val);
        } catch (Exception e) {
            result = 0.00;
        }

        return result;
    }

    public static String Lpad(String Text, Integer length, String padwith) {
        String Result = "";

        StringBuilder sb = new StringBuilder();

        for (int toPrepend = length - Text.length(); toPrepend > 0; toPrepend--) {
            sb.append(padwith);
        }
        sb.append(Text);
        Result = sb.toString();
        return Result;
    }

    public static Double round_off(double d, Integer places) {
        Double result = 0.00;
        double newKB = Math.round(d * 100.0) / 100.0;
        String format = Lpad("", places, "#");
        format = "###." + format;
        DecimalFormat df = new DecimalFormat(format);
        result = Double.valueOf(df.format(d));
        return result;
    }

    public static Integer make_Int(String Val) {
        Integer result = 0;
        try {
            if (Val.contains(",")) {
                Val = Val.replace(",", "");
            }
            result = Integer.valueOf(Val);
        } catch (Exception e) {
            result = 0;
        }
        return result;
    }

    public static Float make_Float(String Val) {
        Float result = Float.valueOf(0);
        try {
            if (Val.contains(",")) {
                Val = Val.replace(",", "");
            }
            result = Float.valueOf(Val);
        } catch (Exception e) {
            result = Float.valueOf(0);
        }
        return result;
    }

    public static String getmac(Context context) {
        String androidID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        return androidID;
    }

    public static String getIMEI() {
		/*TelephonyManager mngr = (TelephonyManager) Context.getSystemService(Context.TELEPHONY_SERVICE);
        String imei = mngr.getDeviceId();*/
        String imei = muname;
        return imei;
    }

    // TODO code written by vipin sir.
    public static String readfile(String fname) {
        // TODO this code is no more to use anywhere

        BufferedReader br = null;
        String response = "";

        try {
            //String fpath = "/sdcard/" + fname + ".txt";
            String fpath = Environment.getExternalStorageDirectory() + File.separator + fname + ".txt";

            File file = new File(fpath);
            if (!file.exists()) {
                if (!fgen.writefile(fname, "")){
                    return "SD card not Readable";
                }
            }

            StringBuffer output = new StringBuffer();

            br = new BufferedReader(new FileReader(fpath));
            String line = "";
            while ((line = br.readLine()) != null) {
                output.append(line);
            }
            response = output.toString();
        } catch (Exception e) {
            BufferedReader input = null;
            File file = null;
            try {
                file = new File(Context.getCacheDir(), fname + ".txt"); // Pass getFilesDir() and "MyFile" to read file

                input = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                String line;
                StringBuffer buffer = new StringBuffer();
                while ((line = input.readLine()) != null) {
                    buffer.append(line);
                }

                Log.d(TAG, buffer.toString());
            } catch (IOException e1) {
                e1.printStackTrace();
                response = "";
            }
        }
        return response;
    }



    //TODO code written by vipin sir.
    public static Boolean writefile(String fname, String fcontent) {
        // TODO this code is no more to use anywhere
        try {
            String fpath = Environment.getExternalStorageDirectory() + File.separator + fname + ".txt";
            File file = new File(fpath);
            // If file does not exists, then create it
            if (file.exists()) file.delete();
            file.createNewFile();
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(fcontent);
            bw.close();

            Log.d("Suceess", "Sucess");
            return true;

        } catch (IOException e) {

            File file;
            FileOutputStream outputStream;
            try {
                // file = File.createTempFile("MyCache", null, getCacheDir());
                file = new File(Context.getCacheDir(), fname + ".txt");
                outputStream = new FileOutputStream(file);
                outputStream.write(fcontent.getBytes());
                outputStream.close();
            } catch (IOException e1) {
                e1.printStackTrace();
                return false;
            }
            return true;
        }
    }

    public static void printReport(Context _context) {
        _context.startActivity(new Intent(_context, frm_PrintReport.class));
    }


    //TODO code written by shivam singh.
    public static Boolean writeFileByShivam(){
        FileOutputStream fos = null;
        try {
            fos = Context.openFileOutput(fgen.file_Serverinfo, android.content.Context.MODE_PRIVATE);
            fos.write(fgen.Serverip.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }finally {
            if(fos != null)
            {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }
        return true;
    }

    //TODO code written by shivam singh.
    public static String readFileByShivam(){
        FileInputStream fis = null;
        String text = "";
        try {
            fis = Context.openFileInput(fgen.file_Serverinfo);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br  = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            text = br.readLine();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(fis != null){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if(text == null)
        {
            return "";
        }
        return text.trim();
    }

    public static void showApiName(Context context, String Message){
        new AlertDialog.Builder(context)
                .setTitle("Save Api")
                .setPositiveButton("ok", null)
                .setMessage(Message)
                .show();
        fgen.toolbar_click_count = 0;
    }
}



