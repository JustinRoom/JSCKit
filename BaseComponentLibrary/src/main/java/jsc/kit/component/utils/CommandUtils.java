package jsc.kit.component.utils;

import android.support.annotation.IntRange;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * <br>https://github.com/JustinRoom/JSCKit
 *
 * @author jiangshicheng
 */
public final class CommandUtils {
    public static final String TAG = "CommandUtils";
    public static final String COMMAND_PS = "ps";
    public static final String COMMAND_SU = "su";
    public static final String COMMAND_SH = "sh";
    public static final String COMMAND_EXIT = "exit\n";
    public static final String LINE_END = "\n";

    private CommandUtils() {
        throw new AssertionError();
    }

    /**
     * Create a screen capture command with saving directory and photo name.
     * <br>Caller must ensure {@link android.Manifest.permission#WRITE_EXTERNAL_STORAGE} permission if using external storage directory.
     * <br>For example, do it like this:
     * <br>
     * <br>{@code File capDirectory = new File(Environment.getExternalStorageDirectory(), Environment.DIRECTORY_DOWNLOADS);}
     * <br>{@code SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA);}
     * <br>{@code String picName = "cap_" + dateFormat.format(new Date()) + "." + outputFormat.name().toLowerCase();}
     * <br>{@code String cmd = CommandUtils.createScreenCaptureCmd(capDirectory, picName);}
     *
     * @param directory the directory to save screen capture
     * @param photoName photo name, not including path
     * @return the image path
     * @see #createScreenCaptureCmd(String)
     */
    public static String createScreenCaptureCmd(File directory, String photoName) {
        return createScreenCaptureCmd(new File(directory, photoName).getPath());
    }

    /**
     * Create a screen capture command with photo path_name.
     * <br>Caller must ensure {@link android.Manifest.permission#WRITE_EXTERNAL_STORAGE} permission if using external storage directory.
     *
     * @param photoPathName photo's name, including path
     * @return the image path
     */
    public static String createScreenCaptureCmd(String photoPathName) {
        int index = photoPathName.lastIndexOf(File.pathSeparator);
        if (index >= 0) {
            String directory = photoPathName.substring(0, index);
            File dir = new File(directory);
            if (!dir.exists())
                dir.mkdirs();
        }
        return "screencap" + " -p " + photoPathName;
    }

    /**
     * Create a screen record command with saving directory and video name.
     * <br>Caller must ensure {@link android.Manifest.permission#WRITE_EXTERNAL_STORAGE} permission if using external storage directory.
     * <br>For example, do it like this:
     * <br>
     * <br>{@code File videoDirectory = new File(Environment.getExternalStorageDirectory(), Environment.DIRECTORY_MOVIES);}
     * <br>{@code SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA);}
     * <br>{@code String video = dateFormat.format(new Date()) + ".mp4";}
     * <br>{@code String cmd = CommandUtils.createScreenRecordCmd(30, videoDirectory, video);}
     *
     * @param limitTimeSeconds record time, the minimum value is 10 seconds
     * @param directory        the directory to save screen record video
     * @param videoName        video name, not including path
     * @return the image path
     * @see #createScreenRecordCmd(int, String)
     */
    public static String createScreenRecordCmd(@IntRange(from = 10) int limitTimeSeconds, File directory, String videoName) {
        return createScreenRecordCmd(limitTimeSeconds, new File(directory, videoName).getPath());
    }

    /**
     * Create a screen record command with video path_name.
     * <br>Caller must ensure {@link android.Manifest.permission#WRITE_EXTERNAL_STORAGE} permission if using external storage directory.
     *
     * @param limitTimeSeconds record time, the minimum value is 10 seconds
     * @param videoPathName    video's name, including path
     * @return the image path
     */
    public static String createScreenRecordCmd(@IntRange(from = 10) int limitTimeSeconds, String videoPathName) {
        int index = videoPathName.lastIndexOf(File.pathSeparator);
        if (index >= 0) {
            String directory = videoPathName.substring(0, index);
            File dir = new File(directory);
            if (!dir.exists())
                dir.mkdirs();
        }
        return "screenrecord --time-limit " + limitTimeSeconds + " " + videoPathName;
    }

    /**
     * Execute su command.
     *
     * @param command command
     * @see #execSuCommand(String, StringBuilder, StringBuilder)
     */
    public static void execSuCommand(String command) {
        execSuCommand(command, null, null);
    }

    /**
     * Execute su command.
     *
     * @param command command
     * @param successMessageBuilder successful message string builder
     * @param errorMessageBuilder failed message string builder
     */
    public static void execSuCommand(String command, StringBuilder successMessageBuilder, StringBuilder errorMessageBuilder) {
        execCommand(COMMAND_SU, command, successMessageBuilder, errorMessageBuilder);
    }

    /**
     * Execute sh command.
     *
     * @param command command
     * @see #execShCommand(String, StringBuilder, StringBuilder)
     */
    public static void execShCommand(String command) {
        execShCommand(command, null, null);
    }

    /**
     * Execute sh command.
     *
     * @param command command
     * @param successMessageBuilder successful message string builder
     * @param errorMessageBuilder failed message string builder
     */
    public static void execShCommand(String command, StringBuilder successMessageBuilder, StringBuilder errorMessageBuilder) {
        execCommand(COMMAND_SH, command, successMessageBuilder, errorMessageBuilder);
    }

    /**
     * Execute common command.
     *
     * @param command command
     * @param successMessageBuilder successful message string builder
     * @param errorMessageBuilder failed message string builder
     */
    public static void execCommand(String command, StringBuilder successMessageBuilder, StringBuilder errorMessageBuilder) {
        execCommand(null, command, successMessageBuilder, errorMessageBuilder);
    }

    private static void execCommand(String preCommand, String command, StringBuilder successMessageBuilder, StringBuilder errorMessageBuilder) {
        if (TextUtils.isEmpty(preCommand) && TextUtils.isEmpty(command))
            return;

        Process process = null;
        BufferedReader successBufferedReader = null;
        BufferedReader errorBufferedReader = null;
        DataOutputStream outputStream = null;
        try {
            process = Runtime.getRuntime().exec(preCommand == null ? command : preCommand);
            successBufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            errorBufferedReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            outputStream = new DataOutputStream(process.getOutputStream());
            if (TextUtils.isEmpty(preCommand) && !command.equals(COMMAND_SU) && !command.equals(COMMAND_SH)) {
                outputStream.write(command.getBytes());
                if (!command.endsWith(LINE_END))
                    outputStream.writeBytes(LINE_END);
                outputStream.flush();
            }
            outputStream.writeBytes(COMMAND_EXIT);
            outputStream.flush();
            process.waitFor();

            //ensure all streams is read
            String line;
            while ((line = successBufferedReader.readLine()) != null) {
                if (successMessageBuilder != null)
                    successMessageBuilder.append(line).append(LINE_END);
            }
            while ((line = errorBufferedReader.readLine()) != null) {
                if (errorMessageBuilder != null)
                    errorMessageBuilder.append(line).append(LINE_END);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null)
                    outputStream.close();
                if (successBufferedReader != null)
                    successBufferedReader.close();
                if (errorBufferedReader != null)
                    errorBufferedReader.close();
                if (process != null)
                    process.destroy();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Get the user of current application process.
     *
     * @param packageName package name
     * @return current user id
     */
    public static String getCurrentAppUser(String packageName) {
        String user = "";
        Process process = null;
        BufferedReader successBufferedReader = null;
        BufferedReader errorBufferedReader = null;
        DataOutputStream outputStream = null;
        try {
            process = Runtime.getRuntime().exec(COMMAND_PS);
            successBufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            errorBufferedReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            outputStream = new DataOutputStream(process.getOutputStream());
            outputStream.writeBytes(COMMAND_EXIT);
            outputStream.flush();
            process.waitFor();

            String s;
            String currentProcessInfo = null;
            while ((s = successBufferedReader.readLine()) != null) {
                if (s.contains(packageName))
                    currentProcessInfo = s;
            }
            while ((s = errorBufferedReader.readLine()) != null) {
                if (s.contains(packageName))
                    currentProcessInfo = s;
            }
            if (currentProcessInfo != null && currentProcessInfo.trim().length() > 0)
                user = currentProcessInfo.split(" ")[0];
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null)
                    outputStream.close();
                if (successBufferedReader != null)
                    successBufferedReader.close();
                if (errorBufferedReader != null)
                    errorBufferedReader.close();
                if (process != null)
                    process.destroy();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return user;
    }
}