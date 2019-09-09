package com.digitalgalaxy.voice.comparison.demo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.digitalgalaxy.voice.comparison.Callback;
import com.digitalgalaxy.voice.comparison.Format;
import com.digitalgalaxy.voice.comparison.VoiceComparisonSDK;
import com.obsez.android.lib.filechooser.ChooserDialog;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final Map<Integer, Integer> views = new HashMap<Integer, Integer>() {
        {
            put(10, R.id.edit_wav1);
            put(11, R.id.edit_wav2);
            put(12, R.id.edit_text);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button_wav1).setTag(10);
        findViewById(R.id.button_wav2).setTag(11);
        findViewById(R.id.button_wav1).setOnClickListener(this);
        findViewById(R.id.button_wav2).setOnClickListener(this);
        findViewById(R.id.button_run).setOnClickListener(this);
    }

    protected void browse(final int id) {
        new ChooserDialog().with(this)
                .withResources(R.string.title_choose, R.string.title_choose, R.string.dialog_cancel)
                .withChosenListener(new ChooserDialog.Result() {
                    @Override
                    public void onChoosePath(String path, File pathFile) {
                        int editResId = views.get(id);
                        EditText editText = findViewById(editResId);
                        editText.setText(path);
                    }
                })
                .build()
                .show();
    }

    @Override
    public void onClick(View v) {
        App app = (App) getApplication();
        if (!app.isSetUp()) {
            Toast.makeText(this, R.string.setup_warning, Toast.LENGTH_LONG).show();
            return;
        }

        if (v.getTag() != null && v.getTag() instanceof Integer) {
            Integer id = (Integer) v.getTag();
            browse(id);
        }

        if (v.getId() == R.id.button_run) {
            String wav1 = ((EditText)findViewById(R.id.edit_wav1)).getText().toString();
            String wav2 = ((EditText)findViewById(R.id.edit_wav2)).getText().toString();
            String text = ((EditText)findViewById(R.id.edit_text)).getText().toString();
            setResults("");
            new ExecuteTask().execute(wav1, wav2, text);
        }
    }

    private void setResults(String result) {
        EditText textVew = findViewById(R.id.text);
        textVew.setText(result);
    }

    private void addResults(String result) {
        EditText textVew = findViewById(R.id.text);
        textVew.setText(textVew.getText().toString() + result);
    }

    private void setProgressVisibility(boolean visible) {
        if (visible) {
            findViewById(R.id.progressbar).setVisibility(View.VISIBLE);
            findViewById(R.id.button_run).setVisibility(View.INVISIBLE);
        } else {
            findViewById(R.id.progressbar).setVisibility(View.INVISIBLE);
            findViewById(R.id.button_run).setVisibility(View.VISIBLE);
        }
    }

    class ExecuteTask extends AsyncTask<String, Integer, Void> {
        @Override
        protected void onPreExecute() {
            setProgressVisibility(true);
        }

        @Override
        protected Void doInBackground(String... strings) {
            String wav1 = strings[0];
            String wav2 = strings[1];
            String text = strings[2];
Callback callback = new Callback() {
    @Override
    public void onResult(final int stage, final String result) {
        super.onResult(stage, result);
        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                addResults(String.format("Stage: %d\n%s\n\n", stage, result));
            }
        });
    }
};
try {
    VoiceComparisonSDK.compare(callback,MainActivity.this, Format.JSON, wav1, wav2, text);
} catch (IOException e) {

}
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            setProgressVisibility(false);
        }
    }
}
