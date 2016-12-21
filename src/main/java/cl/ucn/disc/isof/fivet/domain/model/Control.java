package cl.ucn.disc.isof.fivet.domain.model;

import com.durrutia.ebean.BaseModel;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by Victor on 20/12/2016.
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table
public class Control extends BaseModel {

    /**
     * Identificador del control
     */
    @Getter
    @Column(nullable = false)
    private String identificador;


    /**
     * Fecha de realización del control
     */
    @Getter
    @Column
    @NonNull
    private Date fecha;

    /**
     * Fecha del proximo control
     */
    @Getter
    @Column
    private Date proximoControl;

    /**
     * Temperatura del paciente al momento del control
     */
    @Getter
    @Column
    private Double temperatura;

    /**
     * Peso del paciente al momento del control
     */
    @Getter
    @Column
    private Double peso;

    /**
     * Altura del paciente al momento del control
     */
    @Getter
    @Column
    private Double altura;

    /**
     * Diagnostico del paciente al momento del control
     */
    @Getter
    @Column
    private String diagnostico;

    /**
     * Paciente del control
     */
    @Getter
    @Setter
    @Column
    @ManyToOne
    private Paciente paciente;


    /**
     * Veterinario encargado del control
     */
    @Getter
    @Column
    @ManyToOne
    private Persona veterinario;
}
