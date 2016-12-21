package cl.ucn.disc.isof.fivet.domain.service.ebean;

import cl.ucn.disc.isof.fivet.domain.model.Control;
import cl.ucn.disc.isof.fivet.domain.model.Examen;
import cl.ucn.disc.isof.fivet.domain.model.Paciente;
import cl.ucn.disc.isof.fivet.domain.model.Persona;
import cl.ucn.disc.isof.fivet.domain.service.BackendService;
import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.EbeanServerFactory;
import com.avaje.ebean.config.EncryptKey;
import com.avaje.ebean.config.EncryptKeyManager;
import com.avaje.ebean.config.ServerConfig;
import com.durrutia.ebean.BaseModel;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class EbeanBackendService implements BackendService {

    /**
     * EBean server
     */
    private final EbeanServer ebeanServer;

    /**
     *
     */
    public EbeanBackendService(final String database) {

        log.debug("Loading EbeanBackend in database: {}", database);

        /**
         * Configuration
         */
        ServerConfig config = new ServerConfig();
        config.setName(database);
        config.setDefaultServer(true);
        config.loadFromProperties();

        // Don't try this at home
        //config.setAutoCommitMode(false);

        // config.addPackage("package.de.la.clase.a.agregar.en.el.modelo");
        config.addClass(BaseModel.class);
        config.addClass(Control.class);
        config.addClass(Examen.class);
        config.addClass(Persona.class);
        config.addClass(Persona.Tipo.class);

        config.addClass(Paciente.class);
        config.addClass(Paciente.Sexo.class);

        // http://ebean-orm.github.io/docs/query/autotune
        config.getAutoTuneConfig().setProfiling(false);
        config.getAutoTuneConfig().setQueryTuning(false);

        config.setEncryptKeyManager(new EncryptKeyManager() {

            @Override
            public void initialise() {
                log.debug("Initializing EncryptKey ..");
            }

            @Override
            public EncryptKey getEncryptKey(final String tableName, final String columnName) {

                log.debug("gettingEncryptKey for {} in {}.", columnName, tableName);

                // Return the encrypt key
                return () -> tableName + columnName;
            }
        });

        this.ebeanServer = EbeanServerFactory.create(config);

        log.debug("EBeanServer ready to go.");

    }


    /**
     * @param rut
     * @return Persona
     */
    @Override
    public Persona getPersona(String rut) {
        return this.ebeanServer.find(Persona.class)
                .where()
                .eq("rut", rut)
                .findUnique();
    }

    /**
     * @param numeroFicha
     * @return Paciente
     */
    @Override
    public Paciente getPaciente(Integer numeroFicha) {
        return this.ebeanServer.find(Paciente.class)
                .where()
                .eq("numero",numeroFicha)
                .findUnique();
    }

    /**
     * Obtiene un control mediante un identificador
     * @param identificador
     * @return Control
     */
    @Override
    public Control getControl(String identificador) {

        return this.ebeanServer.find(Control.class)
                .where()
                .eq("identificador", identificador)
                .findUnique();
    }

    /**
     * Agrega un control al paciente
     * @param control
     * @param numeroFicha
     */
    @Override
    public void addControlPaciente(Control control, Integer numeroFicha) {

        Paciente paciente = this.getPaciente(numeroFicha);
        List<Control> listControles = paciente.getControles();
        listControles.add(control);
        paciente.update();

    }




    /**
     * Inicializa la base de datos
     */
    @Override
    public void initialize() {
        log.info("Initializing Ebean ..");
    }

    /**
     * Cierra la conexion a la BD
     */
    @Override
    public void shutdown() {
        log.debug("Shutting down Ebean ..");

        // TODO: Verificar si es necesario des-registrar el driver
        this.ebeanServer.shutdown(true, false);
    }
}
