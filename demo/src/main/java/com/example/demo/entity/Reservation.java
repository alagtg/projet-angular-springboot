package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "nom obligatoire")
    private String nom;

    @Email
    @NotBlank(message = "email obligatoire")
    private String email;

    @Positive(message = "nombrePlaces doit être > 0")
    private int nombrePlaces;

    private LocalDateTime dateReservation;

    @ManyToOne
    @JoinColumn(name = "vol_id")
    @NotNull(message = "vol obligatoire")
    private Vol vol;

    // --- Getters & Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public int getNombrePlaces() { return nombrePlaces; }
    public void setNombrePlaces(int nombrePlaces) { this.nombrePlaces = nombrePlaces; }

    public LocalDateTime getDateReservation() { return dateReservation; }
    public void setDateReservation(LocalDateTime dateReservation) { this.dateReservation = dateReservation; }

    public Vol getVol() { return vol; }
    public void setVol(Vol vol) { this.vol = vol; }
}
