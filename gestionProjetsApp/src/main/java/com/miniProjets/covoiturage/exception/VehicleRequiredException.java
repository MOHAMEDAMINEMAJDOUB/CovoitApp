package com.miniProjets.covoiturage.exception;

public class VehicleRequiredException extends RuntimeException {
    public VehicleRequiredException(String message) {
        super(message);
    }

    public VehicleRequiredException() {
        super("You must register a vehicle before creating trips");
    }
}
