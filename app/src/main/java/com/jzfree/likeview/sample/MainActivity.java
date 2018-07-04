package com.jzfree.likeview.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jzfree.likeview.LikeView;

public class MainActivity extends AppCompatActivity {
    TextView textViewLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final LikeView likeView = findViewById(R.id.likeview);

        textViewLog = findViewById(R.id.log);

        Button change = findViewById(R.id.button_change);
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isLike = likeView.isLike();
                boolean result = likeView.setLike(!isLike);
                addLog("change likeview: " + result + " status: " + likeView.isLike());
            }
        });
    }

    private void addLog(String log) {
        textViewLog.append("\n" + log);
    }
}
