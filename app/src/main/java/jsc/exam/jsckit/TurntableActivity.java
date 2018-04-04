package jsc.exam.jsckit;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import jsc.kit.turntable.GiftEntity;
import jsc.kit.turntable.TurntableView;

public class TurntableActivity extends AppCompatActivity {

    private TurntableView turntableView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turntable);
        turntableView = findViewById(R.id.turntable_view);
        turntableView.setLabelTextSize(14.0f);
        turntableView.setGifts(getGifts());
        turntableView.setOnTurnListener(new TurntableView.OnTurnListener() {
            @Override
            public void onTurnEnd(int index, GiftEntity entity) {
                Toast.makeText(TurntableActivity.this, entity.getLabel(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void widgetClick(View v) {
        switch (v.getId()) {
            case R.id.btn_change_type:
                turntableView.reset();
                if (turntableView.getRotationType() == TurntableView.ROTATION_TYPE_CHASSIS)
                    turntableView.setRotationType(TurntableView.ROTATION_TYPE_COMPASS);
                else
                    turntableView.setRotationType(TurntableView.ROTATION_TYPE_CHASSIS);
                break;
            case R.id.btn_turn:
                Random random = new Random();
//                int angle = 2000 + random.nextInt(2000);
//                turntableView.turntableByAngle(angle);
                int turnCount = 12 * (3 + random.nextInt(5)) + random.nextInt(12);
                turntableView.turntableByCount(turnCount);
                break;
        }
    }

    private List<GiftEntity> getGifts() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.butterfly_96px);
        List<GiftEntity> gifts = new ArrayList<>();
        int[] colors = {
                Color.GREEN, Color.YELLOW, Color.RED,
                Color.GREEN, Color.YELLOW, Color.RED,
                Color.GREEN, Color.YELLOW, Color.RED,
                Color.GREEN, Color.YELLOW, Color.RED
        };
        for (int i = 0; i < colors.length; i++) {
            GiftEntity entity = new GiftEntity();
            entity.setLabel("礼品" + (i + 1));
            entity.setBackgroundColor(colors[i]);
            if (colors[i] == Color.RED)
                entity.setLabelTextColor(Color.WHITE);
            entity.setBitmap(bitmap);
            gifts.add(entity);
        }
        return gifts;
    }
}
