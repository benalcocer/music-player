package com.example.fxutil;

import com.example.util.BufferedImageTranscoder;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.logging.Logger;
import javafx.scene.image.Image;
import org.apache.batik.transcoder.TranscoderInput;
import javafx.embed.swing.SwingFXUtils;
import org.apache.batik.transcoder.image.ImageTranscoder;

public class ImageUtils {
    public static Image createSVGImage(URL url) {
        // TODO: Make SVG Image creating more robust and higher quality.
        BufferedImageTranscoder transcoder = new BufferedImageTranscoder();
        transcoder.addTranscodingHint(ImageTranscoder.KEY_WIDTH, (float) 40.0);
        transcoder.addTranscodingHint(ImageTranscoder.KEY_HEIGHT, (float) 40.0);

        File file = new File(url.getFile());
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            TranscoderInput input = new TranscoderInput(fileInputStream);
            transcoder.transcode(input, null);
            return SwingFXUtils.toFXImage(transcoder.getBufferedImage(), null);
        } catch (Exception e) {
            Logger.getGlobal().warning(e.getMessage());
        }
        return null;
    }
}
