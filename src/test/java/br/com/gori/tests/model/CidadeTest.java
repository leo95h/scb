package br.com.gori.tests.model;

import br.com.gori.scb.dao.impl.CidadeDAOImpl;
import br.com.gori.scb.dao.impl.EstadoDAOImpl;
import br.com.gori.scb.dao.impl.PaisDAOImpl;
import br.com.gori.scb.entidade.Cidade;
import br.com.gori.scb.entidade.Estado;
import br.com.gori.scb.entidade.Pais;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Leonardo
 */
public class CidadeTest {

    private static CidadeDAOImpl cidadeDAO;
    private static EstadoDAOImpl estadoDAO;
    private static Cidade cidade;
    private static Estado estado;

    public CidadeTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        cidadeDAO = new CidadeDAOImpl();
        estadoDAO = new EstadoDAOImpl();
        cidade = cidadeDAO.buscarCidadePorNome("Mandaguari");
        estado = estadoDAO.buscarEstadoPorNome("Paraná");
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void persistIfNotExists() {
        Cidade c = cidadeDAO.buscarCidadePorNome(cidade.getNome());
        if (c == null) {
            Cidade city = new Cidade("Mandaguari", estado);
            c = cidadeDAO.merge(city);
        }
        Assert.assertNotNull(c);
    }

    @Test
    public void findAllCidades() {
        Assert.assertFalse(cidadeDAO.listAll().isEmpty());
    }

    @Test
    public void countCidades() {
        Assert.assertNotEquals(0, cidadeDAO.count());
    }

    @Test
    public void testRecoverCidade() {
        Cidade c = cidadeDAO.listAll().get(0);
        Cidade recovered = cidadeDAO.recover(Cidade.class, c.getId());
        Assert.assertNotNull(recovered);
        Assert.assertEquals(c, recovered);
    }

    @Test
    public void testFindById() {
        Cidade c = cidadeDAO.listAll().get(0);
        Cidade finded = cidadeDAO.findById(c.getId());
        Assert.assertNotNull(finded);
        Assert.assertEquals(c, finded);
    }

    @Test
    public void testBuscarCidadePorNome() {
        Assert.assertNotNull(cidadeDAO.buscarCidadePorNome(cidade.getNome()));
    }
}
