package org.chelmer.clientimpl;

import io.netty.buffer.ByteBuf;
import org.apache.commons.cli.ParseException;
import org.chelmer.LoxoneWebSocketClient;
import org.chelmer.StandaloneLoxoneWebSocketClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.util.function.Function;

/**
 * Created by burfo on 23/02/2017.
 */
public class Recorder extends StandaloneLoxoneWebSocketClient {
    public static final String TEXT_SUFFIX = ".txt";
    public static final String BINARY_SUFFIX = ".bin";
    private static final String filename = "lox_client_test";
    private final Logger LOGGER = LoggerFactory.getLogger(LoxoneWebSocketClient.class);
    private final File dir;

    private int MSG_COUNT = 0;

    public Recorder(String[] args) throws ParseException, IOException {
        super(args);
        dir = Files.createTempDirectory("loxrecorder").toFile();
        LOGGER.info("Saving files to: " + dir);
        start();
    }

    public static void main(String[] args) throws Exception {
        new Recorder(args);
    }

    protected void registerListeners(LoxoneWebSocketClient client) {
        client.registerTextMessageListeners(new Function<String, Boolean>() {
            @Override
            public Boolean apply(String message) {
                textMessageIncoming(message);
                return false;
            }
        });
        client.registerBinaryMessageListener(new Function<ByteBuf, Boolean>() {
            @Override
            public Boolean apply(ByteBuf byteBuf) {
                binaryMessageIncoming(byteBuf);
                return false;
            }
        });
    }

    private File getOutputFile(String suffix) {
        return new File(dir, MSG_COUNT++ + "_" + filename + suffix);
    }

    public void textMessageIncoming(String message) {
        File file = getOutputFile(TEXT_SUFFIX);
        try (Writer writer = new FileWriter(file)) {
            LOGGER.info("Saving text file: " + file);
            writer.write(message);
        } catch (IOException e) {
            LOGGER.error("Error saving text file", e);
        }

    }

    public void binaryMessageIncoming(ByteBuf bytes) {
        File file = getOutputFile(BINARY_SUFFIX);
        LOGGER.info("Saving binary file: " + file);

        try (FileOutputStream outputFile = new FileOutputStream(file, true)) {
            FileChannel outChannel = outputFile.getChannel();
            bytes.getBytes(0, outChannel, 0, bytes.capacity());
        } catch (IOException e) {
            LOGGER.error("Error saving binary file", e);
        }
    }
}
