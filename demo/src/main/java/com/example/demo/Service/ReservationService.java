package com.example.demo.service;

import com.example.demo.entity.AuditReservation;
import com.example.demo.entity.Reservation;
import com.example.demo.entity.Vol;
import com.example.demo.repository.AuditReservationRepository;
import com.example.demo.repository.ReservationRepository;
import com.example.demo.repository.VolRepository;
import org.springframework.cache.CacheManager;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class ReservationService {

    // Exceptions métier internes
    public static class VolNotFoundException extends RuntimeException {
        public VolNotFoundException(String m) { super(m); }
    }
    public static class PlacesInsuffisantesException extends RuntimeException {
        public PlacesInsuffisantesException(String m) { super(m); }
    }
    public static class ConcurrencyException extends RuntimeException {
        public ConcurrencyException(String m) { super(m); }
    }

    private final ReservationRepository reservationRepository;
    private final VolRepository volRepository;
    private final AuditReservationRepository auditReservationRepository;
    private final CacheManager cacheManager;
    private final TransactionTemplate tx;

    public ReservationService(ReservationRepository reservationRepository,
                              VolRepository volRepository,
                              AuditReservationRepository auditReservationRepository,
                              CacheManager cacheManager,
                              PlatformTransactionManager transactionManager) {
        this.reservationRepository = reservationRepository;
        this.volRepository = volRepository;
        this.auditReservationRepository = auditReservationRepository;
        this.cacheManager = cacheManager;
        this.tx = new TransactionTemplate(transactionManager);
    }

    //  Méthode utilitaire centralisée pour audit
    private void saveAudit(Reservation reservation, Vol vol, int placesAvant, String statut, String message) {
        AuditReservation audit = new AuditReservation();
        audit.setTimestamp(LocalDateTime.now());
        audit.setEmailPassager(reservation.getEmail());
        audit.setPlacesDemandees(reservation.getNombrePlaces());
        audit.setVol(vol);
        audit.setPlacesDisponiblesAvant(placesAvant);
        audit.setStatut(statut);
        audit.setMessageErreur(message);
        auditReservationRepository.save(audit);
    }

    //  Gestion transactionnelle + audit
    public Reservation reserverVol(Reservation reservation) {
        if (reservation.getVol() == null || reservation.getVol().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "vol.id manquant dans la réservation");
        }
        final Long volId = reservation.getVol().getId();
        final AtomicBoolean success = new AtomicBoolean(false);

        try {
            Reservation result = tx.execute(status -> {
                Vol vol = volRepository.findById(volId)
                        .orElseThrow(() -> new VolNotFoundException("Vol introuvable"));

                reservation.setVol(vol);

                int placesAvant = vol.getPlacesDisponibles();
                if (placesAvant < reservation.getNombrePlaces()) {
                    // 🚨 Pas de saveAudit ici → sinon rollback efface l’audit
                    throw new PlacesInsuffisantesException("Pas assez de places disponibles !");
                }

                // --- Décrément
                vol.setPlacesDisponibles(placesAvant - reservation.getNombrePlaces());

                // (optionnel) simuler lenteur pour tester les conflits
                try { Thread.sleep(3000); } catch (InterruptedException ignored) {}

                volRepository.saveAndFlush(vol);

                reservation.setDateReservation(LocalDateTime.now());
                Reservation savedRes = reservationRepository.saveAndFlush(reservation);

                success.set(true);

                // audit SUCCESS
                saveAudit(reservation, vol, placesAvant, "SUCCESS", null);
                return savedRes;
            });

            //  Invalidation cache après succès
            if (success.get() && cacheManager != null && cacheManager.getCache("placesDisponibles") != null) {
                cacheManager.getCache("placesDisponibles").evict(volId);
            }

            return result;

        } catch (PlacesInsuffisantesException e) {
            //  Audit FAILED en dehors de la transaction
            Vol vol = volRepository.findById(volId).orElse(null);
            int placesAvant = (vol != null ? vol.getPlacesDisponibles() : 0);
            saveAudit(reservation, vol, placesAvant, "FAILED", "Pas assez de places disponibles !");
            throw e;

        } catch (OptimisticLockingFailureException e) {
            // audit FAILED → conflit concurrent
            Vol vol = volRepository.findById(volId).orElse(null);
            int placesAvant = (vol != null ? vol.getPlacesDisponibles() : 0);
            saveAudit(reservation, vol, placesAvant, "FAILED", "Conflit concurrent (optimistic lock)");
            throw new ConcurrencyException("Conflit concurrent, réessayez");

        } catch (DataAccessException e) {
            // audit FAILED → SQLITE_BUSY
            String msg = String.valueOf(e.getMessage()).toLowerCase();
            if (msg.contains("sqlite_busy") || msg.contains("database is locked")) {
                Vol vol = volRepository.findById(volId).orElse(null);
                int placesAvant = (vol != null ? vol.getPlacesDisponibles() : 0);
                saveAudit(reservation, vol, placesAvant, "FAILED", "Conflit concurrent (database locked)");
                throw new ConcurrencyException("Conflit concurrent, réessayez");
            }
            throw e;
        }
    }
}
