package jsc.exam.jsckit.entity;

import org.json.JSONException;
import org.json.JSONObject;

public class VersionEntity {
    private OutputType outputType;
    private ApkInfo apkInfo;
    private String path;

    public OutputType getOutputType() {
        return outputType;
    }

    public void setOutputType(OutputType outputType) {
        this.outputType = outputType;
    }

    public ApkInfo getApkInfo() {
        return apkInfo;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setApkInfo(ApkInfo apkInfo) {
        this.apkInfo = apkInfo;
    }

    public static VersionEntity fromJson(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            VersionEntity entity = new VersionEntity();

            JSONObject outputTypeObject = jsonObject.getJSONObject("outputType");
            OutputType outputType = new OutputType();
            outputType.setType(outputTypeObject.optString("type"));
            entity.setOutputType(outputType);

            JSONObject apkInfoObject = jsonObject.getJSONObject("apkInfo");
            ApkInfo apkInfo = new ApkInfo();
            apkInfo.setType(apkInfoObject.optString("type"));
            apkInfo.setVersionCode(apkInfoObject.optInt("versionCode"));
            apkInfo.setVersionName(apkInfoObject.optString("versionName"));
            apkInfo.setEnabled(apkInfoObject.optBoolean("enabled"));
            apkInfo.setOutputFile(apkInfoObject.optString("outputFile"));
            apkInfo.setFullName(apkInfoObject.optString("fullName"));
            apkInfo.setBaseName(apkInfoObject.getString("baseName"));
            entity.setApkInfo(apkInfo);

            entity.setPath(jsonObject.optString("path"));

            return entity;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String toJson() {
        JSONObject jsonObject = new JSONObject();
        try {
            JSONObject outputTypeObject = new JSONObject();
            outputTypeObject.put("type", outputType.getType());
            jsonObject.put("outputType", outputTypeObject);

            JSONObject apkInfoObject = new JSONObject();
            apkInfoObject.put("type", apkInfo.getType());
            apkInfoObject.put("versionCode", apkInfo.getVersionCode());
            apkInfoObject.put("versionName", apkInfo.getVersionName());
            apkInfoObject.put("enabled", apkInfo.isEnabled());
            apkInfoObject.put("outputFile", apkInfo.getOutputFile());
            apkInfoObject.put("fullName", apkInfo.getFullName());
            apkInfoObject.put("baseName", apkInfo.getBaseName());
            jsonObject.put("apkInfo", apkInfoObject);

            jsonObject.put("path", getPath());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
}
