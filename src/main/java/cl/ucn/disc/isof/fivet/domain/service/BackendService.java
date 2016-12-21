package cl.ucn.disc.isof.fivet.domain.service;

import cl.ucn.disc.isof.fivet.domain.model.Control;
import cl.ucn.disc.isof.fivet.domain.model.Paciente;
import cl.ucn.disc.isof.fivet.domain.model.Persona;

/**
 * Interface que representa las operaciones de acceso al backend.
 */
public interface BackendService {

    /**
     * Obtiene una persona desde el backend dado su rut.
     *
     * @param rut
     * @return the Persona
     */
    Persona getPersona(final String rut);

    /**
     * Obtiene un paciente desde el backend dado su numero de ficha.
     *
     * @param numeroFicha
     * @return el paciente
     */
    Paciente getPaciente(Integer numeroFicha);

    /**
     * Obtiene un control mediante le identificador de este
     */

    Control getControl(String identificador);

    /**
     * Agrega un control al paciente
     */
    void addControlPaciente(final Control control, final Integer numeroFicha);
    /**
     * Inicializa el backend.
     */
    void initialize();

    /**
     * Cierra la conexion al backend.
     */
    void shutdown();

}
