package jsc.exam.jsckit.ui.component;

import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import jsc.exam.jsckit.R;
import jsc.exam.jsckit.ui.BaseActivity;
import jsc.kit.component.radarview.RadarEntity;
import jsc.kit.component.radarview.RadarView;

public class RadarViewActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radar_view);
        setTitle(getClass().getSimpleName().replace("Activity", ""));

        RadarView radarView1 = findViewById(R.id.radar_view1);
        RadarView radarView2 = findViewById(R.id.radar_view2);
        String[] labels = new String[]{"中单", "射手", "辅助", "打野", "上单"};
        float[] data = new float[]{0.6f, 0.95f, 0.45f, 0.9f, 0.7f};
        int[] align = new int[]{RadarEntity.ALIGN_RIGHT, RadarEntity.ALIGN_BOTTOM, RadarEntity.ALIGN_BOTTOM, RadarEntity.ALIGN_LEFT, RadarEntity.ALIGN_TOP};
        List<RadarEntity> entities = new ArrayList<>();
        for (int i = 0; i < data.length; i++) {
            RadarEntity entity = new RadarEntity();
            entity.setLabel(labels[i]);
            entity.setValue(data[i]);
            entity.setLabelAlignType(align[i]);
            entities.add(entity);
        }
        radarView1.setRadarEntities(entities);
        radarView2.setRadarEntities(entities);
    }
}
