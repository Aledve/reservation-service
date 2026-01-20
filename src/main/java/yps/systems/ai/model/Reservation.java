package yps.systems.ai.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

import java.time.LocalDateTime;

@Node("Reservation") 
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonSerialize
public class Reservation {

    @Id
    @GeneratedValue
    private String elementId;

    // Fecha y hora de entrada reservada
    @Property("startTime")
    private LocalDateTime startTime;

    // Fecha y hora de salida reservada
    @Property("endTime")
    private LocalDateTime endTime;

    // Estado: PENDING, CONFIRMED, CANCELLED, COMPLETED
    @Property("status")
    private String status;

    // Costo calculado de la reserva
    @Property("totalPrice")
    private Double totalPrice;

    // ID del vehículo que ocupará el espacio (Vinculación con Vehicle-Service)
    @Property("vehicleId")
    private String vehicleId;

    // ID del espacio/zona reservada (Vinculación con Zone-Service)
    @Property("spotId")
    private String spotId;

    // ID del usuario que hizo la reserva (Vinculación con User-Service)
    @Property("userId")
    private String userId;

}
