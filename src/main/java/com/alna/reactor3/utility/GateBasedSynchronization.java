package com.alna.reactor3.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class GateBasedSynchronization {

    private static Logger log = LoggerFactory.getLogger(GateBasedSynchronization.class);

    // HashSet of gate names that have been opened.
    private HashSet<String> openGateNames;

    public GateBasedSynchronization() {
        this.openGateNames = new HashSet<>();
    }

    // Block until a specific gate name has been opened.
    public synchronized void waitForGate(String gateName ) {
        try {
            // See if this gate has been opened
            while(!openGateNames.contains(gateName)) {

                // wait...and then check again
                wait();
            }
        }
        catch (InterruptedException e) {
            log.warn(String.format( "InterruptedException while waiting for gate '%1$s'", gateName), e);
        }
    }

    // Block until one of several gates is opened
    public synchronized void waitForAny(String ... gateNames) {

        // Turn the incoming gate names into a Set
        Set<String> searchGateNames = new HashSet<>(Arrays.asList(gateNames));

        try {
            // See if any of the named gates have been opened
            while(searchGateNames.stream().noneMatch(nextGate -> openGateNames.contains(nextGate) )) {

                // wait...and then check again
                wait();
            }
        }
        catch (InterruptedException e) {
            String gateNameList = generateGateNameList(searchGateNames);
            log.warn(String.format( "InterruptedException while waiting for gate(s) '%1$s'", gateNameList), e);
        }
    }

    public synchronized void waitForAny(Duration duration, String... gateNames ) {
        // Turn the incoming gate names into a Set
        Set<String> searchGateNames = new HashSet<>(Arrays.asList(gateNames));

        Instant exitTime = Instant.now().plus(duration);

        try {
            // See if any of the named gates have been opened
            while(searchGateNames.stream().noneMatch(nextGate -> openGateNames.contains(nextGate) )) {

                // If we have exceeded the wait time...exit.
                if(Instant.now().isAfter(exitTime)) {
                    break;
                }

                // wait...and then check again
                this.wait(duration.toMillis());
            }
        }
        catch (InterruptedException e) {
            String gateNameList = generateGateNameList(searchGateNames);
            log.warn(String.format( "InterruptedException while waiting for gate(s) '%1$s'", gateNameList), e);
        }
    }

    // Block until all of the named gates are opened
    public synchronized void waitForAll(String ... gateNames) {

        // Turn the incoming gate names into a Set
        Set<String> searchGateNames = new HashSet<>(Arrays.asList(gateNames));

        try {
            // See if any of the named gates have been opened
            while(!openGateNames.containsAll(searchGateNames)) {

                // wait...and then check again
                wait();
            }
        }
        catch (InterruptedException e) {
            String gateNameList = generateGateNameList(searchGateNames);
            log.warn(String.format( "InterruptedException while waiting for gate(s) '%1$s'", gateNameList), e);
        }
    }

    private String generateGateNameList(Set<String> searchGateNames) {
        StringBuilder stringBuilder = new StringBuilder();
        searchGateNames.forEach(nextGateName -> {
            if (stringBuilder.length() > 0) {
                stringBuilder.append(", ");
            }
            stringBuilder.append(nextGateName);
        });
        return stringBuilder.toString();
    }

    public synchronized void openGate(String gateName) {
        openGateNames.add(gateName);
        notifyAll();
    }

    public synchronized boolean isGateOpen( String gateName ) {
        return openGateNames.contains(gateName);
    }

    public synchronized void resetAll() {
        openGateNames.clear();
    }

    public static void waitMultiple(String[] gateNames, GateBasedSynchronization ... gates) {

        for(GateBasedSynchronization nextGate : gates ) {
            nextGate.waitForAny(gateNames);
        }

    }
}
