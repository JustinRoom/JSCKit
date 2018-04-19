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
        //请求相机权限、SD卡权限
        checkPermissions(0x100, new MyPermissionChecker.OnCheckListener() {
            @Override
            public void onAllGranted(int requestCode) {
                removePermissionChecker();
                toScannerActivity();
            }

            @Override
            public void onGranted(int requestCode, @NonNull List<String> grantedPermissions) {

            }

            @Override
            public void onDenied(int requestCode, @NonNull List<String> deniedPermissions) {
            
            }

            @Override
            public void onShouldShowSettingTips(List<String> shouldShowPermissions) {
                showPermissionRationaleDialog(shouldShowPermissions);
            }
        }, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA);
    }

    private void toScannerActivity() {
        Intent intent = new Intent(this, ZXingScannerActivity.class);
            Uri uri = Uri.parse("zxinglibrary://" + getPackageName()).buildUpon()
                    .appendPath("scanner")
                    .appendQueryParameter(ZXingFragment.SHOW_FLASH_LIGHT, "true") //是否显示灯光按钮
                    .build();
            intent.setData(uri);
        startActivityForResult(intent, 0x666);
    }
    
    public void showPermissionRationaleDialog(List<String> shouldShowPermissions) {
        new AlertDialog.Builder(this)
                .setTitle("温馨提示")
                .setMessage("当前应用需要以下权限:\n\n" + getAllPermissionDes(shouldShowPermissions))
                .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        //跳转到当前应用的设置界面
                        intent.setData(Uri.parse("package:" + getPackageName()));
                        startActivity(intent);
                    }
                })
                .setNegativeButton("知道了", null)
                .show();
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
```
You also can reference [ZXingScannerActivity](../app/src/main/java/jsc/exam/jsckit/ui/zxing/ZXingScannerActivity.java).
### tool：
+ QRCode encoder:[QRCodeEncoder](../ZXingLibrary/src/main/java/jsc/lib/zxinglibrary/zxing/QRCodeEncoder.java)
+ QRCode decoder:[QRCodeEncoder](../ZXingLibrary/src/main/java/jsc/lib/zxinglibrary/zxing/QRCodeDecoder.java)
