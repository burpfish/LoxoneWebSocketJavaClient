package org.chelmer.clientimpl;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import org.chelmer.exceptions.MockDataPlaybackException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Plays back previously recorded files to simulate events from Loxone.
 */
public class DummyEventHandler implements LoxoneEventHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(DummyEventHandler.class);

    private static final String MOCK_DIR = "mock";
    private static final Pattern fileNumberPattern = Pattern.compile("(\\d+)(_.*)");
    private static final int DELAY_BETWEEN_MESSAGES = 1000;

    public DummyEventHandler(LoxoneWebSocketClient client, URI uri, String userName, String password) {
        TimerTask tt = new TimerTask() {
            public void run() {
                SortedMap<Integer, Object> files = getMockFileContent();

                for (Object message : files.values()) {
                    if (message instanceof ByteBuf) {
                        client.handleBinaryMessage((ByteBuf) message);
                    } else if (message instanceof String) {
                        client.handleTextMessage((String) message);
                    } else {
                        throw new MockDataPlaybackException("Unexpected data playing back mock file: " + message.getClass().getName());
                    }
                }
            }
        };

        new Timer().schedule(tt, DELAY_BETWEEN_MESSAGES);
    }


    @Override
    public void disconnect() {
    }

    @Override
    public void sendTextMessage(String s) {
    }

    @Override
    public void authComplete() {

    }

    public SortedMap<Integer, Object> getMockFileContent() {
        final File jarFile = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath());

        try {
            if (jarFile.isFile()) {
                return getMockFileContentFromJar(jarFile);
            } else {
                return getMockFileContentFromFiles();
            }
        }
        catch (IOException e) {
            throw new MockDataPlaybackException("Error accessing file: " + jarFile, e);
        }
    }

    private SortedMap<Integer,Object> getMockFileContentFromJar(File jarFile) throws IOException {
        SortedMap<Integer, Object> messages = new TreeMap<>();

            final JarFile jar = new JarFile(jarFile);
            final Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar

            while (entries.hasMoreElements()) {

                final String fileName = entries.nextElement().getName();
                if (fileName.startsWith(MOCK_DIR + "/")) {
                    InputStream is = null;
                    try {
                        is = getClass().getResourceAsStream("/"+fileName);
                            Matcher m = fileNumberPattern.matcher(fileName);
                            if (m.find()) {
                                if (fileName.endsWith(".bin")) {
                                    messages.put(Integer.valueOf(m.group(1)), getBinaryContentFromZip(is));
                                } else if (fileName.endsWith(".txt")) {
                                    messages.put(Integer.valueOf(m.group(1)), getTextContentFromZip(is));
                                } else {
                                    throw new RuntimeException("Unknown playback file type: " + fileName);
                                }
                            }
                    } catch (IOException e) {
                        throw new MockDataPlaybackException("Error reading file: " + fileName, e);
                    } finally {
                        if (is != null) {
                            try {
                                is.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }

            return messages;
        }

    public SortedMap<Integer, Object> getMockFileContentFromFiles() {
        SortedMap<Integer, Object> messages = new TreeMap<>();

        try {
            List<File> files = getRawFiles();

            files.forEach(file -> {
                    String fileName = file.getName().toString();

                    try {
                        Matcher m = fileNumberPattern.matcher(fileName);
                        if (m.find()) {
                            if (fileName.endsWith(".bin")) {
                                messages.put(Integer.valueOf(m.group(1)), getBinaryFileContent(file));
                            } else if (fileName.endsWith(".txt")) {
                                messages.put(Integer.valueOf(m.group(1)), getTextFileContent(file));
                            } else {
                                throw new RuntimeException("Unknown playback file type: " + fileName);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            });
        } catch (IOException e) {
            throw new RuntimeException("Cannot access playback files in dir: " + MOCK_DIR, e);
        }

        return messages;
    }

    private ByteBuf getBinaryFileContent(File file) throws IOException {
        return Unpooled.wrappedBuffer(Files.readAllBytes(file.toPath()));
    }

    private String getTextFileContent(File file) throws IOException {
        return new String(Files.readAllBytes(file.toPath()));
    }

    private List<File> getRawFiles() throws IOException {
        List<File> files = new ArrayList<>();
            final URL url = DummyEventHandler.class.getResource("/" + MOCK_DIR);
            if (url != null) {
                try {
                    final File dir = new File(url.toURI());
                    for (File file : dir.listFiles()) {
                        files.add(file);
                    }
                } catch (URISyntaxException ex) {
                    throw new MockDataPlaybackException("Error parsing mock input file: " + url, ex);
                }
            }

        return files;
    }

    private ByteBuf getBinaryContentFromZip(InputStream is) throws IOException {
        DataInputStream dis  = new DataInputStream(is);

        try{
            byte[] data = new byte[(int) is.available()];
            dis.readFully(data);
            return Unpooled.wrappedBuffer(data);
        } finally {
           dis.close();
        }
    }

    private String getTextContentFromZip(InputStream is) throws IOException {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        String content = s.hasNext() ? s.next() : "";
        return content;
    }

}
