package com.miniProjets.covoiturage.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TrajetDTO {

    @NotBlank(message = "Departure city is required")
    @Size(min = 2, max = 100, message = "Departure city must be between 2 and 100 characters")
    private String villeDepart;

    @NotBlank(message = "Arrival city is required")
    @Size(min = 2, max = 100, message = "Arrival city must be between 2 and 100 characters")
    private String villeArrivee;

    @NotNull(message = "Date and time are required")
    @FutureOrPresent(message = "Trip date cannot be in the past")
    private LocalDateTime dateHeure;

    @NotNull(message = "Number of available seats is required")
    @Min(value = 1, message = "At least 1 seat must be available")
    @Max(value = 50, message = "Maximum 50 seats allowed")
    private Integer placesDisponibles;

    @NotNull(message = "Price per seat is required")
    @DecimalMin(value = "0.0", message = "Price cannot be negative")
    @Digits(integer = 10, fraction = 2, message = "Price must be a valid amount")
    private Float prixParPlace;

    private Long voitureId;
}
