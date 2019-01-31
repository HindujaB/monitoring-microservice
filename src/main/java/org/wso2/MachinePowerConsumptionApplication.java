package org.wso2;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.exporter.HTTPServer;

import java.net.InetSocketAddress;

public class MachinePowerConsumptionApplication extends Application<MachineConfiguration> {
    private static CollectorRegistry registry = new CollectorRegistry();
    private static DevicePowerDataProvider powerDataProvider;

    public static void main(String[] args) throws Exception {
        powerDataProvider = new DevicePowerDataProvider(registry);
        new MachinePowerConsumptionApplication().run(args);
    }

    @Override
    public String getName() {
        return "Machine-power-consumption-microservice";
    }

    @Override
    public void initialize(Bootstrap<MachineConfiguration> bootstrap) {
    }

    @Override
    public void run(MachineConfiguration configuration, Environment environment) throws Exception {
        new HTTPServer(new InetSocketAddress(8082), registry);
        environment.jersey().register(powerDataProvider);
        environment.healthChecks().register("MachineHC", new MachineHealthCheck());
    }
}