package com.example.demo.service;

import com.example.demo.entity.Vol;
import com.example.demo.repository.VolRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VolService {
    private final VolRepository volRepository;

    public VolService(VolRepository volRepository) {
        this.volRepository = volRepository;
    }

    //  Lecture cacheable des places disponibles
    @Cacheable(value = "placesDisponibles", key = "#volId")
    public Integer getPlacesDisponibles(Long volId) {
        return volRepository.findById(volId)
                .map(Vol::getPlacesDisponibles)
                .orElse(0);
    }

    public List<Vol> getVolsFiltres(String villeDepart, String villeArrivee,
                                    String dateDepart, String dateArrivee,
                                    String tri, String ordre) {
        Sort.Direction direction = "desc".equalsIgnoreCase(ordre) ? Sort.Direction.DESC : Sort.Direction.ASC;
        List<Vol> vols;

        if ("prix".equalsIgnoreCase(tri) || "dateDepart".equalsIgnoreCase(tri) || "dateArrivee".equalsIgnoreCase(tri)) {
            vols = volRepository.findAll(Sort.by(direction, tri));
        } else {
            vols = volRepository.findAll();
            if ("tempsTrajet".equalsIgnoreCase(tri)) {
                Comparator<Vol> comparator = Comparator.comparingLong(Vol::getTempsTrajet);
                if (direction == Sort.Direction.DESC) comparator = comparator.reversed();
                vols = vols.stream().sorted(comparator).collect(Collectors.toList());
            }
        }

        if (villeDepart != null && !villeDepart.isBlank()) {
            vols = vols.stream()
                    .filter(v -> v.getVilleDepart() != null && v.getVilleDepart().equalsIgnoreCase(villeDepart))
                    .collect(Collectors.toList());
        }
        if (villeArrivee != null && !villeArrivee.isBlank()) {
            vols = vols.stream()
                    .filter(v -> v.getVilleArrivee() != null && v.getVilleArrivee().equalsIgnoreCase(villeArrivee))
                    .collect(Collectors.toList());
        }
        if (dateDepart != null && !dateDepart.isBlank()) {
            try {
                LocalDate d = LocalDate.parse(dateDepart.substring(0, 10), DateTimeFormatter.ISO_DATE);
                vols = vols.stream()
                        .filter(v -> v.getDateDepart() != null && v.getDateDepart().toLocalDate().isEqual(d))
                        .collect(Collectors.toList());
            } catch (Exception ignored) {}
        }
        if (dateArrivee != null && !dateArrivee.isBlank()) {
            try {
                LocalDate d = LocalDate.parse(dateArrivee.substring(0, 10), DateTimeFormatter.ISO_DATE);
                vols = vols.stream()
                        .filter(v -> v.getDateArrivee() != null && v.getDateArrivee().toLocalDate().isEqual(d))
                        .collect(Collectors.toList());
            } catch (Exception ignored) {}
        }
        return vols;
    }

    public Vol saveVol(Vol vol) {
        return volRepository.save(vol);
    }
}
