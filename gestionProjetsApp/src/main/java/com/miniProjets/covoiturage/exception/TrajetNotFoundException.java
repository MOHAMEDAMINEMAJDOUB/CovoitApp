package com.miniProjets.covoiturage.exception;

public class TrajetNotFoundException extends RuntimeException {
    public TrajetNotFoundException(String message) {
        super(message);
    }

    public TrajetNotFoundException(Long id) {
        super("Trip not found with ID: " + id);
    }
}
