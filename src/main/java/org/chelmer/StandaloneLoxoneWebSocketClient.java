package org.chelmer;

import io.netty.buffer.ByteBuf;
import org.apache.commons.cli.*;
import org.chelmer.model.LoxoneConfig;
import org.chelmer.model.control.Control;
import org.chelmer.model.control.controlTypes.DimmerControl;
import org.chelmer.model.control.controlTypes.SwitchControl;
import org.chelmer.response.EventTimerItem;
import org.chelmer.response.WeatherTimerItem;
import org.chelmer.response.change.ComponentChange;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.Consumer;

/**
 * Created by burfo on 21/02/2017.
 */
public class StandaloneLoxoneWebSocketClient {
    // donut: Move Use Mock Data to be a param (default to false)
    private static boolean USE_MOCK_DATA = false;
    public SortedMap<String, Control> controls;
    private LoxoneClient client;

    public StandaloneLoxoneWebSocketClient(String[] args) throws ParseException {
        CommandLine cmd = parseCommandLine(args);

        try {
            int port;
            if (cmd.hasOption("port")) {
                port = Integer.valueOf(cmd.getOptionValue("port"));
            } else {
                port = 80;
            }

            client = new LoxoneClientFactory().createAndConnect(cmd.getOptionValue("host"), port, cmd.getOptionValue("user"), cmd.getOptionValue("password"), USE_MOCK_DATA);
            registerListeners(client);
        } finally {
            if (client != null) {
                client.disconnect();
            }
        }
    }

    public static void main(String[] args) throws ParseException, URISyntaxException, InterruptedException, Exception {
        new StandaloneLoxoneWebSocketClient(args).start();
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
        client.connectAndRegisterForUpdates(USE_MOCK_DATA);
    }

    protected void registerListeners(LoxoneClient client) {
        client.registerConfigListener(
                loxoneConfig -> showInteractiveConsole()
        );
        //client.registerWeatherTimerListener(new PrintFunction<WeatherTimerItem>());
        //client.registerTextMessageListeners(new PrintFunction<String>());
        //client.registerDaytimeTimerChangeListener(new PrintFunction<EventTimerItem>());
        //client.registerBinaryMessageListener(new PrintFunction<ByteBuf>());
        //client.registerComponentChangeListener(new PrintFunction<ComponentChange>());
        client.registerSwitchChangeListener(new PrintFunction<SwitchControl>());
        client.registerDimmerChangeListener(new PrintFunction<DimmerControl>());
    }

    public void showInteractiveConsole() {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                System.out.println();
                System.out.println();
                System.out.println();
                populateComponentMaps();
                printComponentMap(controls);

                try {
                    BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
                    while (true) {
                        String msg = console.readLine();

                        switch (msg) {
                            case "controls":
                                printComponentMap(controls);
                                break;
                            case "bye":
                                client.disconnect();
                                break;
                            default:
                                processCommand(msg);
                                break;
                        }
                        if (msg == null) {
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    client.disconnect();
                }
            }
        };
        new Thread(r).start();

    }

    private void processCommand(String msg) {
        String[] split = msg.split(" ");

        switch (split.length) {
            case 0:
                break;
            case 1: {
                String control = split[0].trim();
                if (control.length() > 0) {
                    printStatus(control);
                }
            }
            break;
            case 2: {
                String control = split[0].trim();
                String command = split[1].trim();
                if (control.length() > 0 && command.length() > 0) {
                    invokeCommand(control, command);
                }
            }

            break;
            default:
                System.out.println("Command not understood (too many words)");
                break;
        }
    }

    private Control getControl(String name) {
        Control control = controls.get(name);

        if (control == null) {
            System.out.println("Error: Cannot find component named: " + name);
        }

        return control;
    }

    private void invokeCommand(String name, String value) {
        if ("raw".equals(name)) {
            this.client.sendCommand(value);
        } else {
            Control control = getControl(name);
            if (control != null) {
                System.out.println("Setting control " + name + " to value: " + value);
                if (control instanceof SwitchControl) {
                    setSwitchControl((SwitchControl) control, value);
                } else if (control instanceof DimmerControl) {
                    setDimmerControl((DimmerControl) control, value);
                }
            }
        }
    }

    private void setDimmerControl(DimmerControl control, String value) {
        // TODO: Just have a set value!!! DUH!!!
        control.invokeCommand(DimmerControl.DimmerValueCommand.SET_VALUE, Integer.valueOf(value));
    }

    private void setSwitchControl(SwitchControl control, String value) {
        // TODO: Just have a set value!!! DUH!!!
        // control.invokeCommand(DimmerControl.DimmerValueCommand.SET_VALUE);

    }

    private void printStatus(String name) {
        Control control = getControl(name);
        if (control != null) {
            System.out.println(control.getValue());
        }
    }

    private <T extends Control> void populateComponentMap(String prefix, Class<T> controlType) {
        Collection<T> controlsOfType = client.getControls(controlType);

        int count = 1;
        for (T control : controlsOfType) {
            String key = prefix + count++;
            controls.put(key, control);
        }
    }

    private void populateComponentMaps() {
        this.controls = new TreeMap<>();
        populateComponentMap("s", SwitchControl.class);
        populateComponentMap("d", DimmerControl.class);
    }

    private <T extends Control> void printComponentMap(Map<String, T> nameToComponentMap) {
        for (Map.Entry<String, T> entry : nameToComponentMap.entrySet()) {
            System.out.println(String.format("%s: %s", entry.getKey(), entry.getValue().getName()));
        }
    }

    private class PrintFunction<T> implements Consumer<T> {
        @Override
        public void accept(T event) {
            System.out.println();
            System.out.println(event);
        }
    }
}
