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

    @Override
    public boolean shouldShowRequestPermissionRationale(@NonNull String permission) {
        new AlertDialog.Builder(this)
                .setTitle("温馨提示")
                .setMessage("当前应用需要【" + getPermissionDeniedTips(permission) + "】权限。")
                .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("知道了", null)
                .show();
        return super.shouldShowRequestPermissionRationale(permission);
    }

    private String getPermissionDeniedTips(String permission) {
        String tip;
        if (Manifest.permission.ACCESS_NETWORK_STATE.equals(permission))
            tip = "查看网络状态";
        else if (Manifest.permission.CALL_PHONE.equals(permission))
            tip = "拨号";
        else
            tip = "未知";
        return tip;
    }
```
You also can reference [ZXingScannerActivity](../app/src/main/java/jsc/exam/jsckit/ui/zxing/ZXingScannerActivity.java).
### tool：
+ QRCode encoder:[QRCodeEncoder](../ZXingLibrary/src/main/java/jsc/lib/zxinglibrary/zxing/QRCodeEncoder.java)
+ QRCode decoder:[QRCodeEncoder](../ZXingLibrary/src/main/java/jsc/lib/zxinglibrary/zxing/QRCodeDecoder.java)