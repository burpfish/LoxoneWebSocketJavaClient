package org.chelmer.clientimpl;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import org.chelmer.model.LoxoneConfig;
import org.chelmer.model.control.Control;
import org.chelmer.model.control.controlTypes.SwitchControl;
import org.chelmer.response.EventTimerItem;
import org.chelmer.response.WeatherTimerItem;
import org.chelmer.response.change.ComponentChange;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.mockito.Mockito.mock;

/**
 * Created by burfo on 23/02/2017.
 */
public class PlaybackUtils {
    private final Logger LOGGER = LoggerFactory.getLogger(PlaybackUtils.class);

    private final static String DIR = "recordplayback";
    private final Pattern fileNumberPattern = Pattern.compile("(\\d+)(_.*)");

    public PlaybackUtils() {
    }

//    @Test
//    public void playbackTest() {
//        LoxoneWebSocketClientHandler underTest = new LoxoneWebSocketClientHandler(mock(LoxoneWebSocketClient.class), mockHandshaker, "user", "password", eventHandler);
//        underTest.setHandshakeFuture(mock(ChannelPromise.class));
//
//        SortedMap<Integer, Object> files = getAllFiles();
//
//        for (Object message : files.values()) {
//            if (message instanceof ByteBuf) {
//                underTest.handleBinaryMessage((ByteBuf) message);
//            } else if (message instanceof String) {
//                underTest.handleTextMessage((String) message);
//            } else {
//                throw new RuntimeException("Unexpected data when playing back");
//            }
//        }
//
//        // donut - should be a separate test
//        Collection<SwitchControl> switchControls = eventHandler.getConfig().getControls(SwitchControl.class);
//        System.out.println(switchControls);
//    }

    public SortedMap<Integer, Object> getAllFiles() {
        URL url = ClassLoader.getSystemResource(DIR);
        if (url == null) {
            throw new RuntimeException("Cannot find playback file dir: " + DIR);
        }

        SortedMap<Integer, Object> messages = new TreeMap<>();

        try (Stream<Path> paths = Files.walk(Paths.get(url.toURI()))) {
            paths.forEach(filePath -> {
                if (Files.isRegularFile(filePath)) {
                    String fileName = filePath.getFileName().toString();

                    try {
                        Matcher m = fileNumberPattern.matcher(fileName);
                        if (m.find()) {
                            if (fileName.endsWith(Recorder.BINARY_SUFFIX)) {
                                messages.put(Integer.valueOf(m.group(1)), getBinaryFileContent(filePath));
                            } else if (fileName.endsWith(Recorder.TEXT_SUFFIX)) {
                                messages.put(Integer.valueOf(m.group(1)), getTextFileContent(filePath));
                            } else {
                                throw new RuntimeException("Unknown playback file type: " + fileName);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            throw new RuntimeException("Cannot access playback files in dir: " + DIR, e);
        }

        return messages;
    }

    private ByteBuf getBinaryFileContent(Path path) throws IOException {
        return Unpooled.wrappedBuffer(Files.readAllBytes(path));
    }

    private String getTextFileContent(Path path) throws IOException {
        return new String(Files.readAllBytes(path));
    }
}
