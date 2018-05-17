package jsc.exam.jsckit.ui.component;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import jsc.exam.jsckit.R;
import jsc.exam.jsckit.ui.ABaseActivity;
import jsc.kit.turntable.GiftEntity;
import jsc.kit.turntable.TurntableView;

public class TurntableViewActivity extends ABaseActivity {

    private TurntableView turntableView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turntable_view);
        setTitle(getClass().getSimpleName().replace("Activity", ""));

        turntableView = findViewById(R.id.turntable_view);
//        turntableView.setLabelTextSize(14.0f);
        turntableView.setGifts(getGifts());
        turntableView.setOnTurnListener(new TurntableView.OnTurnListener() {
            @Override
            public void onTurnEnd(int index, GiftEntity entity) {
                Toast.makeText(TurntableViewActivity.this, entity.getLabel(), Toast.LENGTH_SHORT).show();
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
                int turnCount = 12 * (5 + random.nextInt(5)) + random.nextInt(12);
                turntableView.turntableByCount(turnCount);
                break;
        }
    }

    //礼品种类最好是能被360整除，比如3、6、9、12、18等等
    private List<GiftEntity> getGifts() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.butterfly_96px);
        List<GiftEntity> gifts = new ArrayList<>();
        int[] colors = {
                Color.GREEN, Color.YELLOW, Color.RED,
                Color.GREEN, Color.YELLOW, Color.RED,
                Color.GREEN, Color.YELLOW, Color.RED,
                Color.GREEN, Color.YELLOW, Color.RED
        };
        String[] labels = {
                "谢谢惠顾", "蝎子", "¥10000",
                "再来一次", "佐伊", "¥10万",
                "美女一吻", "德玛西亚", "¥120万",
                "可乐一瓶", "提莫", "¥500万",
        };
        for (int i = 0; i < colors.length; i++) {
            GiftEntity entity = new GiftEntity();
            entity.setLabel(labels[i]);
            entity.setBackgroundColor(colors[i]);
            if (colors[i] == Color.RED)
                entity.setLabelTextColor(Color.WHITE);
            entity.setBitmap(bitmap);
            gifts.add(entity);
        }
        return gifts;
    }
}
