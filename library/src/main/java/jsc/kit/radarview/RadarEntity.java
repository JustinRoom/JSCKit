package jsc.kit.radarview;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class RadarEntity implements Parcelable{
    public final static int ALIGN_LEFT = 0;
    public final static int ALIGN_RIGHT = 1;
    public final static int ALIGN_TOP = 2;
    public final static int ALIGN_BOTTOM = 3;
    @IntDef({ALIGN_LEFT, ALIGN_RIGHT, ALIGN_TOP, ALIGN_BOTTOM})
    @Retention(RetentionPolicy.SOURCE)
    public @interface LabelAlignType {
    }

    /**标签*/
    private String label;
    /**标签字体颜色， 默认为：0xFF333333*/
    private int labelColor;
    /**标签字体大小， 默认为：12sp*/
    private float labelTextSize;
    /**标签相对于顶点的位置*/
    private int labelAlignType;
    /**有效值百分比*/
    private float value;

    public RadarEntity() {
        label = "";
        labelColor = 0xFF333333;
        labelTextSize = 12f;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getLabelColor() {
        return labelColor;
    }

    public void setLabelColor(@ColorInt int labelColor) {
        this.labelColor = labelColor;
    }

    public float getLabelTextSize() {
        return labelTextSize;
    }

    public float getValue() {
        return value;
    }

    public void setValue(@FloatRange(from = 0, to = 1.0f) float value) {
        this.value = value;
    }

    /**
     * The text size of label. The unit is <code>sp</code>.
     *
     * @param labelTextSize
     */
    public void setLabelTextSize(float labelTextSize) {
        this.labelTextSize = labelTextSize;
    }

    public int getLabelAlignType() {
        return labelAlignType;
    }

    public void setLabelAlignType(@LabelAlignType int labelAlignType) {
        this.labelAlignType = labelAlignType;
    }

    protected RadarEntity(Parcel in) {
        label = in.readString();
        labelColor = in.readInt();
        labelTextSize = in.readFloat();
        labelAlignType = in.readInt();
        value = in.readFloat();
    }

    public static final Creator<RadarEntity> CREATOR = new Creator<RadarEntity>() {
        @Override
        public RadarEntity createFromParcel(Parcel in) {
            return new RadarEntity(in);
        }

        @Override
        public RadarEntity[] newArray(int size) {
            return new RadarEntity[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(label);
        dest.writeInt(labelColor);
        dest.writeFloat(labelTextSize);
        dest.writeInt(labelAlignType);
        dest.writeFloat(value);
    }
}
