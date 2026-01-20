package yps.systems.ai.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;
import yps.systems.ai.model.Reservation;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface IReservationRepository extends Neo4jRepository<Reservation, String> {

    // Buscar reservas de un usuario específico
    List<Reservation> findAllByUserId(String userId);

    // Buscar reservas de un vehículo específico
    List<Reservation> findAllByVehicleId(String vehicleId);

    // Consulta compleja: Verificar si existe conflicto de horarios para un espacio
    // Retorna true si hay solapamiento (OVERLAP)
    @Query("MATCH (r:Reservation) " +
           "WHERE r.spotId = $spotId " +
           "AND r.status <> 'CANCELLED' " +
           "AND NOT (r.endTime <= $newStartTime OR r.startTime >= $newEndTime) " +
           "RETURN count(r) > 0")
    Boolean checkSpotAvailability(String spotId, LocalDateTime newStartTime, LocalDateTime newEndTime);

    // Obtener reservas activas en un rango de fechas (para reportes)
    @Query("MATCH (r:Reservation) " +
           "WHERE r.startTime >= $start AND r.endTime <= $end " +
           "RETURN r")
    List<Reservation> findReservationsInInterval(LocalDateTime start, LocalDateTime end);
}
