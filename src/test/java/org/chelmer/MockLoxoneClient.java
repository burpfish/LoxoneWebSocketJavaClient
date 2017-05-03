//package org.chelmer;
//
//import io.netty.buffer.ByteBuf;
//import io.netty.channel.ChannelPromise;
//import org.chelmer.clientimpl.LoxoneWebSocketClient;
//import org.chelmer.clientimpl.LoxoneWebSocketClientHandler;
//import org.chelmer.clientimpl.PlaybackUtils;
//import org.chelmer.model.LoxoneConfig;
//import org.chelmer.model.control.Control;
//import org.chelmer.model.control.controlTypes.Command;
//import org.chelmer.model.control.controlTypes.SwitchControl;
//import org.chelmer.response.EventTimerItem;
//import org.chelmer.response.WeatherTimerItem;
//import org.chelmer.response.change.ComponentChange;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.io.File;
//import java.io.IOException;
//import java.net.URL;
//import java.nio.file.Files;
//import java.util.Collection;
//import java.util.SortedMap;
//import java.util.Timer;
//import java.util.TimerTask;
//import java.util.concurrent.ExecutorService;
//import java.util.function.Consumer;
//
//import static org.mockito.Mockito.mock;
//
///**
// * Created by burfo on 08/03/2017.
// */
//public class MockLoxoneClient extends LoxoneWebSocketClient {
//    public static void main(String[] args) {
//        new MockLoxoneClient();
//    }
//
//    public MockLoxoneClient() {
//        super("hostName", 333, "userName", "password");
//    }
//
//    @Override
//    public void connect() {
//        // Start playback
//
//        SortedMap<Integer, Object> files = new PlaybackUtils().getAllFiles();
//
//        for (Object message : files.values()) {
//            if (message instanceof ByteBuf) {
//                handleBinaryMessage((ByteBuf) message);
//            } else if (message instanceof String) {
//                handleTextMessage((String) message);
//            } else {
//                throw new RuntimeException("Unexpected data when playing back");
//            }
//        }
//    }
//
//    @Override
//    protected void requestStatus() {
//
//    }
//
//    @Override
//    protected void registerForUpdates() {
//
//    }
//}
