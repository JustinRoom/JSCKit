# Use Guide

### use ZXing Scanner for several steps like below：
+ 1、add users-permission in your ```AndroidManifest.xml``` file:
```
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
    <uses-permission android:name="android.permission.CAMERA"/>
```
+ 2、please embed [ZXingFragment](../ZXingLibrary/src/main/java/jsc/lib/zxinglibrary/zxing/ui/ZXingFragment.java) into your activity like this:  
```
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, new ZXingFragment())
                .commit();
```
+ 3、check **CAMERA** users-permission before starting ZXing Scanner. For example:    
```
    public void widgetClick(View v) {
        //打开相机权限
        if (checkPermission(0x100, Manifest.permission.CAMERA))
            toScannerActivity();
    }

    private void toScannerActivity() {
        startActivityForResult(new Intent(this, ZXingScannerActivity.class), 0x666);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
            return;

        if (requestCode == 0x666) {
            String result = data.getStringExtra("result");
            tvScanResult.setText("扫描结果：\n" + result);
        }
    }


    public final boolean checkPermission(int requestCode, String permission) {
        if (ActivityCompat.checkSelfPermission(getBaseContext(), permission) == PackageManager.PERMISSION_GRANTED)
            return true;
        ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            toScannerActivity();
    }
```
You also can reference [ZXingScannerActivity](../app/src/main/java/jsc/exam/jsckit/ui/zxing/ZXingScannerActivity.java).
### tool：
+ QRCode encoder:[QRCodeEncoder](../ZXingLibrary/src/main/java/jsc/lib/zxinglibrary/zxing/QRCodeEncoder.java)
+ QRCode decoder:[QRCodeEncoder](../ZXingLibrary/src/main/java/jsc/lib/zxinglibrary/zxing/QRCodeDecoder.java)
