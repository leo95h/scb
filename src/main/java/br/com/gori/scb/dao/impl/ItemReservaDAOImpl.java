package br.com.gori.scb.dao.impl;

import br.com.gori.scb.connection.EntityManagerProducer;
import br.com.gori.scb.dao.AbstractDAO;
import br.com.gori.scb.dao.inter.ItemReservaDAO;
import br.com.gori.scb.entidade.ItemReserva;
import br.com.gori.scb.entidade.Pessoa;
import br.com.gori.scb.entidade.Reserva;
import java.util.Date;
import java.util.List;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;

/**
 *
 * @author Leonardo
 */
public class ItemReservaDAOImpl extends AbstractDAO<ItemReserva> implements ItemReservaDAO {

    public ItemReservaDAOImpl() {
        super(ItemReserva.class);
    }

    @Override
    public List<ItemReserva> listarItemReservaPorTitulo(String titulo) {
        Query q = EntityManagerProducer.getEntityManager().createNamedQuery("ItemReserva.findByTitulo", ItemReserva.class);
        q.setParameter("titulo", titulo);
        return q.getResultList();
    }

    @Override
    public ItemReserva buscarItemReservaPorTitulo(String titulo) {
        Query q = EntityManagerProducer.getEntityManager().createNamedQuery("ItemReserva.findByTitulo", ItemReserva.class);
        q.setParameter("titulo", titulo);
        List<ItemReserva> itens = q.getResultList();
        if (itens.isEmpty()) {
            return null;
        }
        if (itens.size() >= 1) {
            return itens.get(0);
        } else {
            throw new NonUniqueResultException();
        }
    }

    public List<ItemReserva> recuperaItensReservasAbertas(Reserva reserva) {
        String sql = "select b.* from reserva a, itemreserva b "
                + "where a.id = b.reserva_id "
                + "and (b.efetivado is null or b.efetivado = 'false') "
                + "and a.id = :parte";
        Query q = EntityManagerProducer.getEntityManager().createNativeQuery(sql, ItemReserva.class);
        q.setParameter("parte", reserva.getId());
        return q.getResultList();
    }

    public List<ItemReserva> reservasAbertasPessoa(Pessoa pessoa) {
        String sql = "select b.* from reserva a, itemreserva b "
                + "where a.id = b.reserva_id "
                + "and (b.efetivado = 'false' or b.efetivado is null) "
                + "and b.previsao = current_date "
                + "and a.pessoa_id = :parte ";
        Query q = EntityManagerProducer.getEntityManager().createNativeQuery(sql, ItemReserva.class);
        q.setParameter("parte", pessoa.getId());
        return q.getResultList();
    }

    public List<ItemReserva> filtrarReservas(String publicacao, String pessoa, Date dataInicial, Date dataFinal, Long idPessoa) {
        String sql = "select b.* from reserva a, itemreserva b, publicacao c, pessoa d "
                + "where b.reserva_id = a.id "
                + "and b.publicacao_id = c.id "
                + "and a.pessoa_id = d.id ";
        if (!"".equals(publicacao)) {
            sql = sql + "and lower(c.titulo) like :publicacao ";
        }
        if (!"".equals(pessoa)) {
            sql = sql + "and lower(d.nome) like :pessoa ";
        }
        if (dataInicial != null) {
            sql = sql + "and b.previsao >= :inicial ";
        }
        if (dataFinal != null) {
            sql = sql + "and b.previsao <= :final ";
        }
        if (idPessoa != null) {
            sql = sql + "and d.id = :idPessoa ";
        }
        Query q = EntityManagerProducer.getEntityManager().createNativeQuery(sql, ItemReserva.class);
        if (!"".equals(publicacao)) {
            q.setParameter("publicacao", "%" + publicacao.toLowerCase() + "%");
        }
        if (!"".equals(pessoa)) {
            q.setParameter("pessoa", "%" + pessoa.toLowerCase() + "%");
        }
        if (dataInicial != null) {
            q.setParameter("inicial", dataInicial);
        }

        if (dataFinal != null) {
            q.setParameter("final", dataFinal);
        }
        if (idPessoa != null) {
            q.setParameter("idPessoa", idPessoa);
        }
        return q.getResultList();
    }

    public List<ItemReserva> recuperarReservasAbertaPessoa(Pessoa pessoa) {
        String sql = "select b.* from reserva a, itemreserva b "
                + "where a.id = b.reserva_id "
                + "and (b.efetivado is null or b.efetivado = 'false') "
                + "and a.pessoa_id = :parte";
        Query q = EntityManagerProducer.getEntityManager().createNativeQuery(sql, ItemReserva.class);
        q.setParameter("parte", pessoa.getId());
        return q.getResultList();
    }

}
