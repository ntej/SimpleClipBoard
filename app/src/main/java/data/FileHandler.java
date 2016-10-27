package data;

import android.content.Context;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by navatejareddy on 10/08/16.
 */

public class FileHandler {

    /**Checks file exists or not**/
    public static boolean fileExists(Context context,String fileName) {
        File file = context.getFileStreamPath(fileName);
        if(file==null||!file.exists())
        {
            return false;
        }
        return  true;
    }

    /**Write strings to file**/
    public static void writeToFile( Context context, String data, boolean append) {
        try
        {
            if(append) {
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("clipboardData.txt", Context.MODE_APPEND));
                outputStreamWriter.write(data);
                outputStreamWriter.close();
            }
            else if(!append){
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("clipboardData.txt", Context.MODE_PRIVATE));
                outputStreamWriter.write(data);
                outputStreamWriter.close();
            }


        }
        catch(Exception e)
        {

        }
    }


    /**Reads from file as String**/
    public static String readFromFile(Context context) {
        String data="";
        try
        {
            InputStream inputStream = context.openFileInput("clipboardData.txt");
            if(inputStream!=null)
            {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                String tempString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((tempString=bufferedReader.readLine())!=null)
                {
                    stringBuilder.append(tempString);
                    stringBuilder.append("\n");
                }

                inputStream.close();
                data = stringBuilder.toString();
            }

        }
        catch(Exception e)
        {

        }
        return data;
    }



}
