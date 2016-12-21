package cl.ucn.disc.isof.fivet.domain.model;
import com.durrutia.ebean.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by Victor Araya on 20/12/2016.
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table
public class Examen extends BaseModel {

    /**
     * Nombre del examen
     */
    @Getter
    @NotNull
    @Column
    private String nombre;


    /**
     * Identificador del examen
     */
    @Getter
    @NotNull
    @Column
    private String identificador;

    /**
     * Fecha del examen
     */
    @Getter
    @NotNull
    @Column
    private Date fecha;

    /**
     * Resultado del examen
     */
    @Getter
    @NotNull
    @Column
    private String resultado;

}
