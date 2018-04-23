# CustomToast

## Screenshot


## Link
[CustomToast](../../library/src/main/java/jsc/kit/utils/CustomToast.java)

## Attributions
[CustomToast.Builder](../../library/src/main/java/jsc/kit/utils/CustomToast.java)

| 成员变量 | 类型 | 含义 |
|:---|:---|:---|
| text | CharSequence | 文本 |
| backgroundColor | color | Toast背景颜色 |
| textColor | color | 文字颜色 |
| textSize | float | 文字大小，单位为`sp` |
| textGravity | int | 文字对其方式 |
| topMargin | int | Toast相对于statusbar的距离 |
| duration | long | 显示时间，单位为`毫秒`。默认为`3000` |

## Usage
+ At the first, initialize it like this as below: 
```
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CustomToast.getInstance().init(this);
    }
}
```
+ Then, show toast:
```
        CustomToast.getInstance().show("toast");
```
```
        CustomToast.getInstance().show(this, R.string.XXX);
```
```
        CustomToast.getInstance().show(
                new CustomToast.Builder()
                        .text("toast")
                        .backgroundColor(0x33666666)
                        .textColor(Color.WHITE)
                        .textSize(15f)
                        .topMargin(56)
                        .duration(2_000)
        );
```