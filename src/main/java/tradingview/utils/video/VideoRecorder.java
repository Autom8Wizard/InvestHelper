package tradingview.utils.video;
import com.codeborne.selenide.WebDriverRunner;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import tradingview.utils.props.GlobalProperty;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class VideoRecorder {


    private static final Logger logger = LogManager.getLogger(VideoRecorder.class);


    private Thread recordingThread;
    private final WebDriver driver;
    private final String testName;
    private final List<File> screenshotList = new ArrayList<>();
    private volatile boolean recording = true;


    public VideoRecorder(String testName, WebDriver driver) {
        this.testName = testName;
        this.driver = driver;
    }


    public VideoRecorder(String testName) {
        this(testName, WebDriverRunner.getAndCheckWebDriver());
    }


    /**
     * Capture screenshots with the defined interval while the 'recording' field is true
     */
    private void captureScreenshots() {
        while (this.recording) {
            this.screenshotList.add(((TakesScreenshot) this.driver).getScreenshotAs(OutputType.FILE));
            try {
                TimeUnit.MILLISECONDS.sleep(GlobalProperty.getVideoScreenshotCaptureStepTimeout());
            } catch (InterruptedException e) {
                this.recordingThread.interrupt();
                Thread.currentThread().interrupt();
            }
        }
    }


    /**
     * Start screenshots capturing.
     * Create and start a thread which will call captureScreenshots()
     */
    public void startRecording() {
        logger.trace("Screenshot capturing for the video started for the test: " + this.testName);

        this.recordingThread = new Thread(this::captureScreenshots);
        this.recordingThread.start();
    }


    /**
     * Stop screenshots capturing.
     * Set the 'recording' field to false, this change will stop the captureScreenshots() method
     * and interrupt the recording thread
     *
     * @param saveResults Write captured screenshots to the filesystem or flush them
     */
    private void stopRecording(boolean saveResults) {
        this.recording = false;

        if (saveResults) {
            Exception e = null;
            for (int i = 0; i < this.screenshotList.size(); i++) {
                File screenshot = this.screenshotList.get(i);
                String fullFilePath = GlobalProperty.getVideoDirectoryPath() +
                        this.testName +
                        File.separator +
                        this.testName +
                        "-" +
                        String.format("%05d", i) +
                        GlobalProperty.getScreenshotFileExtension();
                try {
                    FileUtils.copyFile(screenshot, new File(fullFilePath));
                } catch (IOException ioException) {
                    e = ioException;
                    logger.warn(ioException);
                    break;
                }
            }

            if (e == null) {
                logger.trace("All the captured screenshots for the test '" + this.testName + "' have been saved to the " +
                        "directory: " + GlobalProperty.getVideoDirectoryPath() + this.testName);
            } else {
                logger.trace("Not all the captured screenshots for the test '" + this.testName + "' have been saved." +
                        "\nError occurred: " + e.getMessage());
            }
        } else {
            logger.trace("No screenshots saving is required for the test '" + this.testName + "'." +
                    "\nAll captured screenshots will be flushed.");
        }

        // we won't need those screenshots, so let's delete them
        this.screenshotList.clear();

        // interrupt screenshots capturing thread
        this.recordingThread.interrupt();
    }


    /**
     * Convert captured screenshots to a video file
     */
    private void convertScreenshotsToVideo() {
        logger.trace("Screenshot conversion to a video started for the test: " + this.testName);

        String ffmpegCmd = "ffmpeg -loglevel quiet -framerate 2 -i " +
                GlobalProperty.getVideoDirectoryPath() +
                this.testName +
                File.separator +
                this.testName +
                "-%05d" +
                GlobalProperty.getScreenshotFileExtension() +
                " -vf scale=1920:1080:flags=lanczos -c:v libx264 -crf 16 -b:v 8000k -pix_fmt yuv420p -movflags +faststart " +
                GlobalProperty.getVideoDirectoryPath() +
                this.testName +
                GlobalProperty.getVideoFileExtension();
        logger.info("Executing ffmpeg command from cmd: " + ffmpegCmd);
        try {
            // execute the command in a separate process, and wait for the command execution
            Process process = Runtime.getRuntime().exec(ffmpegCmd);
            if (process.waitFor(GlobalProperty.getVideoConversionTimeout(), TimeUnit.SECONDS)) {
                if (process.exitValue() == 0) {
                    logger.info("Captured screenshots were converted to a video file successfully for the test: " + this.testName);
                } else {
                    logger.warn("Screenshot conversion to a video has failed for the test: " + this.testName + "." +
                            "\nFfmpeg command has failed with the exit code: " + process.exitValue());
                }
            } else {
                logger.warn("Screenshot conversion to a video has failed for the test: " + this.testName + "." +
                        "Ffmpeg command did not finish in time. Given timeout reached: " + GlobalProperty.getVideoConversionTimeout());
            }
        } catch (IOException | InterruptedException e) {
            logger.warn(e);
        }
    }


    /**
     * Stop screenshots capturing and flush the captured screenshots
     */
    public void stopRecordingAndFlushFiles() {
        this.stopRecording(false);
    }


    /**
     * Stop screenshots capturing and convert the screenshots to a video file
     */
    public void stopRecordingAndSaveVideo() {
        this.stopRecording(true);
        this.convertScreenshotsToVideo();

        if (!GlobalProperty.isKeepVideoScreenshots()) {
            try {
                FileUtils.deleteDirectory(new File(GlobalProperty.getVideoDirectoryPath() + this.testName));
            } catch (IOException e) {
                logger.warn(e);
            }
        }
    }


}