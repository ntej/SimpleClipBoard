package data;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import www.ntej.com.simpleclipboard.MainActivity;
import www.ntej.com.simpleclipboard.R;

/**
 * Created by navatejareddy on 12/08/16.
 */
public class Transit extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transit);

        Intent intent = getIntent();
        if (intent != null) {
            String action = intent.getAction();
            String type = intent.getType();

            if (Intent.ACTION_SEND.equals(action) && type != null) {
                if ("text/plain".equals(type)) {
                    handleReceivedText(intent); // Handle text being sent
                }
            }
        }
    }

    void handleReceivedText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            i.putExtra("SHAREDTEXT", sharedText);
            startActivity(i);
        }
    }


    @Override
    public void onPause(){
        super.onPause();

        finish();

    }
}
