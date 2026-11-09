package co.edu.uptc.persistencia;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import co.edu.uptc.modelo.enums.AccionAuditoria;

/**
 * Verifica que la auditoría compuesta mantenga el TXT obligatorio de RF18 y
 * el JSON adicional sin duplicar la lectura principal.
 */
public class AuditoriaCompuestaTest {

    @Rule
    public TemporaryFolder carpetaTemporal = new TemporaryFolder();

    @Test
    public void testRegistraEnTxtYJson() throws Exception {
        Path base = carpetaTemporal.getRoot().toPath().resolve("data");
        AuditoriaTxtRepository txt = new AuditoriaTxtRepository(
                base.resolve("auditoria_admin.txt").toString());
        AuditoriaRepository json = new AuditoriaRepository(
                base.resolve("auditoria.json").toString());
        AuditoriaCompuesta compuesta = new AuditoriaCompuesta(txt, json);

        compuesta.registrar(AccionAuditoria.CREATE, "9780000002001", "admin",
                "Registro de prueba");

        assertEquals(1, Files.readAllLines(base.resolve("auditoria_admin.txt")).size());
        assertEquals(1, json.listar().size());
        assertEquals(1, compuesta.listar().size());
        assertTrue(Files.size(base.resolve("auditoria.json")) > 0);
    }

    @Test
    public void testUnDestinoNuloNoImpideElOtro() throws Exception {
        Path base = carpetaTemporal.getRoot().toPath().resolve("data");
        AuditoriaTxtRepository txt = new AuditoriaTxtRepository(
                base.resolve("auditoria_admin.txt").toString());
        AuditoriaCompuesta compuesta = new AuditoriaCompuesta(txt, null);

        compuesta.registrar(AccionAuditoria.UPDATE, "9780000002002", "admin",
                "Actualización de prueba");

        assertEquals(1, compuesta.listar().size());
    }
}
