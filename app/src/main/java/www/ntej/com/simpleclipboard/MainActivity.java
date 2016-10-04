package www.ntej.com.simpleclipboard;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.Set;
import data.*;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText clipboard;
    private ImageButton paste,copy,clear,share;
    private String sharedData;




    String  sysClipBoardText = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        clipboard = (EditText) findViewById(R.id.content);
        paste = (ImageButton) findViewById(R.id.paste);
        copy = (ImageButton) findViewById(R.id.copy);
        clear = (ImageButton) findViewById(R.id.clear);
        share = (ImageButton) findViewById(R.id.share);

        //set on click listener
        paste.setOnClickListener(this);
        copy.setOnClickListener(this);
        clear.setOnClickListener(this);
        share.setOnClickListener(this);

        intentHadler();


    }

    @Override
    protected void onStop() {
        super.onStop();
        //FileHandler.writeToFile(getApplicationContext(),clipboard.getText().toString(),false);
        WriteToFileTask writeToFileTask = new WriteToFileTask();
        writeToFileTask.execute(new String[]{clipboard.getText().toString()});
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        //FileHandler.writeToFile(getApplicationContext(),clipboard.getText().toString(),false);
        WriteToFileTask writeToFileTask = new WriteToFileTask();
        writeToFileTask.execute(new String[]{clipboard.getText().toString()});

    }



    @Override
    public void onClick(View view)
    {
        switch(view.getId()) {


            case R.id.paste:
                /**paste from system clipboard to EditText clipBoard**/

                try {
                    ClipboardManager PasteClipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    //ClipData.Item item = PasteClipboardManager.getPrimaryClip().getItemAt(0);


                    if (!(PasteClipboardManager.hasPrimaryClip())) {
                        Toast.makeText(getApplicationContext(), "No text in system Clipboard", Toast.LENGTH_SHORT).show();
                    } else {

                        ClipData.Item item = PasteClipboardManager.getPrimaryClip().getItemAt(0);
                        sysClipBoardText = item.getText().toString();

                        if (clipboard.getText().toString().equals("")) {
                            clipboard.setText(sysClipBoardText);
                            Toast.makeText(getApplicationContext(), "Pasted from System Clipboard", Toast.LENGTH_SHORT).show();

                        } else {
                            AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                            alert.setTitle("Paste Action");
                            alert.setMessage("Clear ClipBoard and Paste?");
                            alert.setIcon(R.drawable.ic_question_answer_black_24dp);

                            alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    clipboard.setText(sysClipBoardText);

                                }
                            });


                            alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    String temp = clipboard.getText().toString();
                                    clipboard.setText(temp + "\n" + sysClipBoardText);

                                }
                            });

                            alert.show();

                        }
                    }
                }
                catch (Exception e)
                {
                    Log.v("MyActivity",e.getClass().getName());
                   // Toast.makeText(getApplicationContext(), "No text in system Clipboard", Toast.LENGTH_SHORT).show();
                }

                break;


            case R.id.copy:
                /**copy from ClipBoard EditText to clipBoard**/

                    String clipBoardText = clipboard.getText().toString();

                    if (clipBoardText.equals("")) {
                    Toast.makeText(getApplicationContext(), "Nothing to copy to System Clipboard", Toast.LENGTH_SHORT).show();
                } else {

                        ClipboardManager copyClipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("TEXT_LOCAL",clipBoardText);
                        copyClipboardManager.setPrimaryClip(clip);

                        Toast.makeText(getApplicationContext(), "Copied to System Clipboard", Toast.LENGTH_SHORT).show();
                }
                break;


            case R.id.clear:
                /**clear Edit text**/
                if(clipboard.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Nothing to Clear", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                    alert.setTitle("Warning");
                    alert.setMessage("Are you sure want to Clear?");
                    alert.setIcon(R.drawable.ic_warning_black_24dp);
                    alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            clipboard.setText("");
                            Toast.makeText(getApplicationContext(), "Clipboard Cleared", Toast.LENGTH_SHORT).show();
                        }
                    });
                    alert.setNegativeButton("No", null);

                    alert.show();

                }
                break;





            case R.id.share:
                /**show share options**/
                String texttoshare = clipboard.getText().toString();
                if(texttoshare.equals(""))
                {
                    Toast.makeText(getApplicationContext(), "Nothing to Share", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, texttoshare);
                    sendIntent.setType("text/plain");
                    startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to)));
                }

                break;
    }

    }

    @Override
    protected void onNewIntent(Intent intent) {

            Bundle extras = intent.getExtras();


            if (extras != null) {

                Set<String> keys = extras.keySet();

                for (String key : keys) {

                    if (key.equals("SHAREDTEXT")) {
                        sharedData = extras.getString("SHAREDTEXT");
                        intentTextAppendHandler();
                    }
                }

            }

    }

    public void intentHadler()
    {
        if (FileHandler.fileExists(getApplicationContext(), "clipboardData.txt")) {
           // clipboard.setText(FileHandler.readFromFile(getApplicationContext()));
            ReadFromFileTask readFromFileTask = new ReadFromFileTask();
            readFromFileTask.execute();

        }

        Intent intent = getIntent();
        Bundle extras = getIntent().getExtras();


       if((intent.getFlags() & intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY)!=0) {

           // clipboard.setText(FileHandler.readFromFile(getApplicationContext()));
           ReadFromFileTask readFromFileTask = new ReadFromFileTask();
           readFromFileTask.execute();
       }

       else {
            if (extras != null) {

                Set<String> keys = extras.keySet();

                for(String key :keys) {

                    if(key.equals("SHAREDTEXT")) {
                        clipboard.setText(FileHandler.readFromFile(getApplicationContext()));
                        sharedData = extras.getString("SHAREDTEXT");
                        intentTextAppendHandler();
                    }
                }

            }

            else {
                /**When user open app for first time or to avoids exception if file corrupted or lost**/
                if (FileHandler.fileExists(getApplicationContext(), "clipboardData.txt")) {
                    //clipboard.setText(FileHandler.readFromFile(getApplicationContext()));
                    ReadFromFileTask readFromFileTask = new ReadFromFileTask();
                    readFromFileTask.execute();

                } else {
                    clipboard.setText("");
                }
            }
        }
    }


    public void intentTextAppendHandler() {
        if (clipboard.getText().toString().equals("")) {
            clipboard.setText(sharedData);
        } else {
            AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
            alert.setTitle("Share Action");
            alert.setMessage("Clear ClipBoard and Paste?");
            alert.setIcon(R.drawable.ic_question_answer_black_24dp);


            alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    clipboard.setText(sharedData);
                }
            });

            alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    String temp = clipboard.getText().toString();
                    clipboard.setText(temp + "\n" + sharedData);
                }
            });

            alert.show();
        }
    }

    /**separate threads to read and write to file**/

    private  class WriteToFileTask extends AsyncTask<String,Void,Void>
    {
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(String... strings) {

            FileHandler.writeToFile(getApplicationContext(),strings[0],false);

            return null;
        }
    }

     private class ReadFromFileTask extends AsyncTask <Void,Void,String>
    {
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            clipboard.setText(s);

        }

        @Override
        protected String doInBackground(Void... voids) {

          String data =  FileHandler.readFromFile(getApplicationContext());

            return data;
        }
    }

}

