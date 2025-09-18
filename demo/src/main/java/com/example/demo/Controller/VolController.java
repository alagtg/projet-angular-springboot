package com.example.demo.controller;

import com.example.demo.entity.Vol;
import com.example.demo.service.VolService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vols")
@CrossOrigin(origins = "http://localhost:4200")
public class VolController {

    private final VolService volService;

    public VolController(VolService volService) {
        this.volService = volService;
    }

    // Recherche de vols avec filtres et tri
    @GetMapping
    public List<Vol> rechercherVols(
            @RequestParam(required = false) String villeDepart,
            @RequestParam(required = false) String villeArrivee,
            @RequestParam(required = false) String dateDepart,
            @RequestParam(required = false) String dateArrivee,
            @RequestParam(defaultValue = "prix") String tri,
            @RequestParam(defaultValue = "asc") String ordre
    ) {
        return volService.getVolsFiltres(villeDepart, villeArrivee, dateDepart, dateArrivee, tri, ordre);
    }

    // Créer un vol
    @PostMapping
    public Vol createVol(@Valid @RequestBody Vol vol) {
        vol.setPlacesDisponibles(vol.getCapacite()); // au départ toutes les places sont dispo
        return volService.saveVol(vol);
    }
}
