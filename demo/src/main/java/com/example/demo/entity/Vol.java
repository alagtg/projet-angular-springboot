package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
public class Vol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String villeDepart;

    @NotBlank
    private String villeArrivee;

    @NotNull
    private LocalDateTime dateDepart;

    @NotNull
    private LocalDateTime dateArrivee;

    @PositiveOrZero
    private double prix;

    @Positive(message = "tempsTrajet en minutes > 0")
    private int tempsTrajet;

    @Positive(message = "capacite > 0")
    private int capacite;

    @PositiveOrZero
    private int placesDisponibles;

    @Version
    private Long version;

    // --- Getters & Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getVilleDepart() { return villeDepart; }
    public void setVilleDepart(String villeDepart) { this.villeDepart = villeDepart; }

    public String getVilleArrivee() { return villeArrivee; }
    public void setVilleArrivee(String villeArrivee) { this.villeArrivee = villeArrivee; }

    public LocalDateTime getDateDepart() { return dateDepart; }
    public void setDateDepart(LocalDateTime dateDepart) { this.dateDepart = dateDepart; }

    public LocalDateTime getDateArrivee() { return dateArrivee; }
    public void setDateArrivee(LocalDateTime dateArrivee) { this.dateArrivee = dateArrivee; }

    public double getPrix() { return prix; }
    public void setPrix(double prix) { this.prix = prix; }

    public int getTempsTrajet() { return tempsTrajet; }
    public void setTempsTrajet(int tempsTrajet) { this.tempsTrajet = tempsTrajet; }

    public int getCapacite() { return capacite; }
    public void setCapacite(int capacite) { this.capacite = capacite; }

    public int getPlacesDisponibles() { return placesDisponibles; }
    public void setPlacesDisponibles(int placesDisponibles) { this.placesDisponibles = placesDisponibles; }

    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }
}
