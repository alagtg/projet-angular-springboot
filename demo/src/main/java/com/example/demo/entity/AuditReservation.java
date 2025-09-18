package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class AuditReservation {

    @Id
    @GeneratedValue
    private UUID id;

    private LocalDateTime timestamp;
    private String emailPassager;
    private int placesDemandees;
    private int placesDisponiblesAvant;
    private String statut;          // SUCCESS ou FAILED
    private String messageErreur;   // si échec, message explicatif

    @ManyToOne
    @JoinColumn(name = "vol_id")
    private Vol vol;

    // --- Getters & Setters ---
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public String getEmailPassager() { return emailPassager; }
    public void setEmailPassager(String emailPassager) { this.emailPassager = emailPassager; }

    public int getPlacesDemandees() { return placesDemandees; }
    public void setPlacesDemandees(int placesDemandees) { this.placesDemandees = placesDemandees; }

    public int getPlacesDisponiblesAvant() { return placesDisponiblesAvant; }
    public void setPlacesDisponiblesAvant(int placesDisponiblesAvant) { this.placesDisponiblesAvant = placesDisponiblesAvant; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    public String getMessageErreur() { return messageErreur; }
    public void setMessageErreur(String messageErreur) { this.messageErreur = messageErreur; }

    public Vol getVol() { return vol; }
    public void setVol(Vol vol) { this.vol = vol; }
}
