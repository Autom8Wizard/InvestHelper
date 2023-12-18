package testlisteners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import tradingview.utils.DataTransferStub;
import tradingview.utils.video.VideoRecorder;

public class VideoRecordingAnalyzer implements TestWatcher {


    private static final Logger logger = LogManager.getLogger(VideoRecordingAnalyzer.class);


    @Override
    public void testSuccessful(ExtensionContext context) {
        // we don't need a video on success
        handleTestOutcome(false);
        logger.info("Test '{}' passed.", context.getTestMethod().get().getName());
    }


    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        // we need a video on failure
        handleTestOutcome(true);
        logger.info("Test '{}' failed.", context.getTestMethod().get().getName());
    }


    @Override
    public void testAborted(ExtensionContext context, Throwable cause) {
        // we don't need a video on aborted (because if is most likely caused by the hook's failure)
        handleTestOutcome(false);
        logger.info("Test '{}' aborted.", context.getTestMethod().get().getName());
    }


    private void handleTestOutcome(boolean saveVideo) {
        VideoRecorder videoRecorder = (VideoRecorder) DataTransferStub.getObject();
        if (videoRecorder != null) {
            if (saveVideo) {
                videoRecorder.stopRecordingAndSaveVideo();
            } else {
                videoRecorder.stopRecordingAndFlushFiles();
            }
        }
    }

}