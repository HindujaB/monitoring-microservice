package org.wso2;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Counter;
import io.prometheus.client.SimpleCollector;
import org.joda.time.DateTime;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Random;

@Path("/device-power-consumption")
@Produces(MediaType.APPLICATION_JSON)
public class DevicePowerDataProvider {

    private final CollectorRegistry registry;
    private double lastUpdateTime = System.currentTimeMillis();
    private StringBuilder stringBuilder;
    private SimpleCollector collector;
    private Random random = new Random();
    private String[] deviceIDArray = new String[]{"server001", "server002", "monitor001", "server003"};
    private String[] roomIDArray = new String[]{"F3Room2", "F2Room2", "F3Room4", "F2Room1"};

    DevicePowerDataProvider(CollectorRegistry registry) {
        this.registry = registry;
        registerMetrics();
    }

    private void registerMetrics() {
        SimpleCollector.Builder builder = Counter.build()
                .name("total_device_power_consumption_WATTS")
                .help("Total Power consumption of each devices in Watts")
                .labelNames("deviceID", "roomID");
        collector = builder.register(registry);
    }


    @GET
    public String powerConsumption() {
        stringBuilder = new StringBuilder();
        checkAndRefreshMetrics();
        stringBuilder.append("{\n\t\"Date\" : \"")
                .append(new DateTime().toString("dd/MM/yyyy"))
                .append("\",\n")
                .append("\t\"Power Consumption\" : [\n");
        for (int i = 0; i <= 3; i++) {
            setJSONDataAndUpdateMetrics(i, getPowerConsumption());
        }
        stringBuilder.append("\t]\n}");
        return stringBuilder.toString();
    }

    private void setJSONDataAndUpdateMetrics(int i, int powerConsumption) {
        stringBuilder.append(" \t{\n\t\t\"deviceID\" : \"")
                .append(deviceIDArray[i])
                .append("\",\n")
                .append("\t\t\"roomID\": \"")
                .append(roomIDArray[i])
                .append("\",\n")
                .append("\t\t\"current-power-consumption\": ")
                .append(powerConsumption);
        if(i == 3) {
            stringBuilder.append("\n \t}");
        } else {
            stringBuilder.append("\n \t},");
        }
        stringBuilder.append("\n");
        ((Counter) collector).labels(deviceIDArray[i], roomIDArray[i]).inc(powerConsumption);
    }

    private int getPowerConsumption() {
        return random.nextInt(15) + 5;
    }

    private void checkAndRefreshMetrics() {
        double currentTime = System.currentTimeMillis();
        if(currentTime - lastUpdateTime >= 1000 * 60 * 60 * 24) {
            registry.unregister(collector);
            registerMetrics();
            lastUpdateTime = currentTime;
        }
    }
}