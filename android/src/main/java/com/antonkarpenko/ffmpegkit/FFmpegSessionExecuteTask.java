package com.antonkarpenko.ffmpegkit;

import androidx.annotation.NonNull;

import com.antonkarpenko.ffmpegkit.FFmpegKitConfig;

import java.util.HashMap;
import java.util.Map;

import io.flutter.plugin.common.MethodChannel;

public class FFmpegSessionExecuteTask implements Runnable {
    private final com.antonkarpenko.ffmpegkit.FFmpegSession ffmpegSession;
    private final FFmpegKitFlutterMethodResultHandler resultHandler;
    private final MethodChannel.Result result;

    public FFmpegSessionExecuteTask(@NonNull final com.antonkarpenko.ffmpegkit.FFmpegSession ffmpegSession, @NonNull final FFmpegKitFlutterMethodResultHandler resultHandler, @NonNull final MethodChannel.Result result) {
        this.ffmpegSession = ffmpegSession;
        this.resultHandler = resultHandler;
        this.result = result;
    }

    @Override
    public void run() {
        FFmpegKitConfig.ffmpegExecute(ffmpegSession);
        resultHandler.successAsync(result, toResultMap(ffmpegSession));
    }

    // The outcome travels with the execute result, so callers never have to look
    // the session up again in the bounded session history (it may be evicted).
    static Map<String, Object> toResultMap(@NonNull final com.antonkarpenko.ffmpegkit.FFmpegSession session) {
        final Map<String, Object> map = new HashMap<>();
        map.put("sessionId", session.getSessionId());
        map.put("state", session.getState().ordinal());
        final ReturnCode returnCode = session.getReturnCode();
        map.put("returnCode", returnCode == null ? null : returnCode.getValue());
        map.put("failStackTrace", session.getFailStackTrace());
        return map;
    }
}
