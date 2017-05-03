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
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
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
public class PlaybackTest {
    private final static String DIR = "recordplayback";
    private final Logger LOGGER = LoggerFactory.getLogger(PlaybackTest.class);
    private final Pattern fileNumberPattern = Pattern.compile("(\\d+)(_.*)");
    TestLoxoneEventHandler eventHandler = new TestLoxoneEventHandler();
    private WebSocketClientHandshaker mockHandshaker = mock(WebSocketClientHandshaker.class);
    private PlaybackUtils utils = new PlaybackUtils();

    @Test
    public void playbackTest() throws URISyntaxException, MalformedURLException {
        LoxoneWebSocketClient client = mock(LoxoneWebSocketClient.class);
        LoxoneWebSocketClientHandler underTest = new LoxoneWebSocketClientHandler(client, new URL("http://192.168.11.0:8080").toURI(), "user", "password");//, eventHandler);
        underTest.setHandshakeFuture(mock(ChannelPromise.class));

        SortedMap<Integer, Object> files = utils.getAllFiles();

        for (Object message : files.values()) {
            if (message instanceof ByteBuf) {
                underTest.handleBinaryMessage((ByteBuf) message);
            } else if (message instanceof String) {
                underTest.handleTextMessage((String) message);
            } else {
                throw new RuntimeException("Unexpected data when playing back");
            }
        }

        // donut - should be a separate test
        Collection<? extends Control> switchControls = eventHandler.getConfig().getControls(SwitchControl.class);
        System.out.println(switchControls);
    }

    private static class TestLoxoneEventHandler {// implements LoxoneEventHandler {
        LoxoneConfig config;

        public LoxoneConfig getConfig() {
            return config;
        }

//        @Override
        public void configIncoming(LoxoneConfig config) {
            this.config = config;
        }

//        @Override
        public void weatherTimerChangeIncoming(WeatherTimerItem value) {
        }

//        @Override
        public void daytimeTimerChangeIncoming(EventTimerItem values) {
        }

//        @Override
        public void textMessageIncoming(String value) {
        }

//        @Override
        public void binaryMessageIncoming(ByteBuf bytes) {
        }

//        @Override
        public void componentChangeIncoming(ComponentChange change) {
            System.out.println();
            System.out.println(change.getMessage());
            System.out.println(String.format("    %s", change.getComponent().getClass().getName()));
        }
    }
}
