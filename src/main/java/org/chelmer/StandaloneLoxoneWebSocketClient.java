package org.chelmer;

import org.apache.commons.cli.*;
import org.chelmer.model.LoxoneConfig;
import org.chelmer.response.ComponentChange;
import org.chelmer.response.EventTimerItem;
import org.chelmer.response.WeatherTimerItem;

import java.net.URISyntaxException;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by burfo on 21/02/2017.
 */
public class StandaloneLoxoneWebSocketClient {
    private LoxoneWebSocketClient client;

    public StandaloneLoxoneWebSocketClient(String[] args) throws ParseException {
        CommandLine cmd = parseCommandLine(args);

        try {
            int port;
            if (cmd.hasOption("port")) {
                port = Integer.valueOf(cmd.getOptionValue("port"));
            } else {
                port = 80;
            }

            client = new LoxoneWebSocketClient(cmd.getOptionValue("host"), port, cmd.getOptionValue("user"), cmd.getOptionValue("password"));
            registerListeners(client);
        } finally {
            if (client != null) {
                client.disconnect();
            }
        }
    }

    public static void main(String[] args) throws ParseException, URISyntaxException, InterruptedException, Exception {
        StandaloneLoxoneWebSocketClient client = new StandaloneLoxoneWebSocketClient(args);
        client.start();
    }

    private CommandLine parseCommandLine(String[] args) throws ParseException {
        Options options = new Options();
        options.addOption(Option.builder("user").hasArg().argName("Loxone user").required().build());
        options.addOption(Option.builder("password").hasArg().argName("Loxone password").required().build());
        options.addOption(Option.builder("port").hasArg().argName("Loxone port (defaults to 80)").build());
        options.addOption(Option.builder("host").hasArg().argName("Loxone host name").required().build());

        CommandLineParser parser = new DefaultParser();
        return parser.parse(options, args);
    }

    protected void start() {
        client.connectAndRegisterForUpdates();
        client.showInteractiveConsole();
    }

    protected void registerListeners(LoxoneWebSocketClient client) {
        client.registerConfigListener(new PrintFunction<LoxoneConfig>());
        client.registerWeatherTimerListener(new PrintFunction<WeatherTimerItem>());
        client.registerTextMessageListeners(new PrintFunction<String>());
        client.registerDaytimeTimerChangeListener(new PrintFunction<EventTimerItem>());
//            client.registerBinaryMessageListener(new PrintFunction<ByteBuf>());
        client.registerComponentChangeListener(new PrintFunction<ComponentChange>());
    }

    private class PrintFunction<T> implements Consumer<T> {
        @Override
        public void accept(T t) {
            System.out.println(t);
        }
    }
}
