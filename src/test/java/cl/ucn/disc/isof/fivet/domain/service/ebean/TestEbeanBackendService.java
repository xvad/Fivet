package cl.ucn.disc.isof.fivet.domain.service.ebean;

import cl.ucn.disc.isof.fivet.domain.model.Control;
import cl.ucn.disc.isof.fivet.domain.model.Paciente;
import cl.ucn.disc.isof.fivet.domain.model.Persona;
import cl.ucn.disc.isof.fivet.domain.service.BackendService;
import com.google.common.base.Stopwatch;
import lombok.extern.slf4j.Slf4j;
import org.junit.*;
import org.junit.rules.Timeout;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Clase de testing del {@link BackendService}.
 */
@Slf4j
@FixMethodOrder(MethodSorters.DEFAULT)
public class TestEbeanBackendService {

    /**
     * Todos los test deben terminar antes de 60 segundos.
     */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(60);

    /**
     * Configuracion de la base de datos:  h2, hsql, sqlite
     * WARN: hsql no soporta ENCRYPT
     */
    private static final String DB = "h2";

    /**
     * Backend
     */
    private BackendService backendService;

    /**
     * Cronometro
     */
    private Stopwatch stopWatch;

    /**
     * Antes de cada test
     */
    @Before
    public void beforeTest() {

        stopWatch = Stopwatch.createStarted();
        log.debug("Initializing Test Suite with database: {}", DB);

        backendService = new EbeanBackendService(DB);
        backendService.initialize();
    }

    /**
     * Despues del test
     */
    @After
    public void afterTest() {

        log.debug("Test Suite done. Shutting down the database ..");
        backendService.shutdown();

        log.debug("Test finished in {}", stopWatch.toString());
    }

    /**
     * Test de la persona
     */
    @Test
    public void testPersona() {

        final String rut = "1-1";
        final String nombre = "Este es mi nombre";

        // Insert into backend
        {
            final Persona persona = Persona.builder()
                    .nombre(nombre)
                    .rut(rut)
                    .password("durrutia123")
                    .tipo(Persona.Tipo.CLIENTE)
                    .build();

            persona.insert();

            log.debug("Persona to insert: {}", persona);
            Assert.assertNotNull("Objeto sin id", persona.getId());
        }

        // Get from backend v1
        {
            final Persona persona = backendService.getPersona(rut);
            log.debug("Persona founded: {}", persona);
            Assert.assertNotNull("Can't find Persona", persona);
            Assert.assertNotNull("Objeto sin id", persona.getId());
            Assert.assertEquals("Nombre distintos!", nombre, persona.getNombre());
            Assert.assertNotNull("Pacientes null", persona.getPacientes());
            Assert.assertTrue("Pacientes != 0", persona.getPacientes().size() == 0);

            // Update nombre
            persona.setNombre(nombre);
            persona.update();
        }

        // Get from backend v2
        {
            final Persona persona = backendService.getPersona(rut);
            log.debug("Persona founded: {}", persona);
            Assert.assertNotNull("Can't find Persona", persona);
            Assert.assertEquals("Nombres distintos!", nombre, persona.getNombre());
        }

    }

    /**
     * Test del Paciente
     */
    @Test
    public void testPaciente(){
        final Integer numero = 1;
        final String nombre = "Bibidi";
        final String nombre2 = "Babidi";
        final String color = "cafe";
        final Date fechaNacimiento =  new java.util.Date();

        // Insert into backend
        {
            final Paciente paciente = Paciente.builder()
                    .numero(numero)
                    .nombre(nombre)
                    .fechaNacimiento(fechaNacimiento)
                    .color(color)
                    .controles(new ArrayList<Control>())
                    .sexo(Paciente.Sexo.HEMBRA)
                    .build();

            paciente.insert();


            log.debug("{Paciente to insert: {}", paciente);
            Assert.assertNotNull("Objeto sin id", paciente.getId());
        }

        // Get from backend v1
        {
            final Paciente paciente = backendService.getPaciente(numero);
            log.debug("Paciente found: {}", paciente);
            Assert.assertNotNull("Can't find Paciente", paciente);
            Assert.assertNotNull("Objeto sin id", paciente.getId());
            Assert.assertEquals("Numero distintos!", numero, paciente.getNumero());
            Assert.assertEquals("Nombre distintos!", nombre, paciente.getNombre());
            Assert.assertEquals("Fecha de nacimiento distintas!", fechaNacimiento,
                    paciente.getFechaNacimiento());
            Assert.assertEquals("Color distintos!", color, paciente.getColor());
            Assert.assertEquals("Sexo distintos!", Paciente.Sexo.HEMBRA, paciente.getSexo());

            // Update nombre
            paciente.setNombre(nombre2);
            paciente.update();

        }
        {
            final Paciente paciente = backendService.getPaciente(numero);
            log.debug("Paciente: {}", paciente);
            Assert.assertNotNull("Can't find Paciente", paciente);
        }


    }

    /**
     * Test del control
     */
    @Test
    public void testAgregarControlPaciente(){
        final String nombre = "VAD";
        final String rut = "18.361.842-3";
        final Persona.Tipo tipo = Persona.Tipo.VETERINARIO;
        final String password = "1234";
        final java.util.Date fecha = new java.util.Date();

        final Persona veterinario = Persona.builder()
                .rut(rut)
                .nombre(nombre)
                .password(password)
                .tipo(tipo)
                .build();

        veterinario.insert();

        final Integer numero = 1;
        final String id = "DSDSA";


        final Paciente paciente = Paciente.builder()
                .numero(numero)
                .build();

            paciente.insert();
            log.debug("Paciente to insert: {}", paciente);
            Assert.assertNotNull("Objeto sin id", paciente.getId());

        final Control control = Control.builder()
                .identificador(id)
                .fecha(fecha)
                .veterinario(veterinario)
                .build();

        control.insert();
        log.debug("Control to insert: {}", control);
        Assert.assertNotNull("Objeto sin id", control.getId());

        this.backendService.addControlPaciente(control,numero);


        // Get from backend v1
        {
            List<Control> controles = backendService.getPaciente(numero).getControles();
            Assert.assertNotNull("Can't find controles", controles);
        }

    }

}
