package com.javaeventsql.service;

import com.javaeventsql.dao.EventoParticipanteDao;
import com.javaeventsql.table.EventoParticipante;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventoParticipanteServiceTest {

    @Mock // Cria uma instância "falsa" (mock) do EventoParticipanteDao
    private EventoParticipanteDao daoMock;

    @InjectMocks // Cria uma instância do EventoParticipanteService e injeta o mock acima
    private EventoParticipanteService eventoParticipanteService;

    @Test
    @DisplayName("Deve listar todas as inscrições de participantes em eventos")
    void deveListarTodasAsInscricoes() {
        // Arrange (Preparação)
        EventoParticipante inscricao = new EventoParticipante();
        inscricao.setId(1);
        inscricao.setEventId(100);
        inscricao.setParticipanteId(200);
        List<EventoParticipante> listaMock = Collections.singletonList(inscricao);

        // Define o comportamento do mock
        when(daoMock.listarTodos()).thenReturn(listaMock);

        // Act (Ação)
        List<EventoParticipante> resultado = eventoParticipanteService.listarTodos();

        // Assert (Verificação)
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(100, resultado.get(0).getEventId());
        verify(daoMock).listarTodos(); // Verifica se o método do DAO foi chamado
    }

    @Test
    @DisplayName("Deve buscar uma inscrição pelo seu ID")
    void deveBuscarInscricaoPorId() {
        int idBusca = 50;
        EventoParticipante inscricaoMock = new EventoParticipante();
        inscricaoMock.setId(idBusca);
        inscricaoMock.setEventId(10);
        inscricaoMock.setParticipanteId(20);

        when(daoMock.buscarPorId(idBusca)).thenReturn(inscricaoMock);

        EventoParticipante resultado = eventoParticipanteService.buscarPorId(idBusca);

        assertNotNull(resultado);
        assertEquals(idBusca, resultado.getId());
        assertEquals(10, resultado.getEventId());
        verify(daoMock).buscarPorId(idBusca);
    }

    @Test
    @DisplayName("Deve inserir uma nova inscrição (relação evento-participante)")
    void deveInserirNovaInscricao() {
        Integer idEvento = 15;
        Integer idParticipante = 25;
        String mensagemEsperada = "Inscrição realizada com sucesso.";

        when(daoMock.inserir(idEvento, idParticipante)).thenReturn(mensagemEsperada);

        String resultado = eventoParticipanteService.inserir(idEvento, idParticipante);

        assertEquals(mensagemEsperada, resultado);
        verify(daoMock).inserir(idEvento, idParticipante);
    }

    @Test
    @DisplayName("Deve buscar uma inscrição pelo ID do participante")
    void deveBuscarInscricaoPorIdParticipante() {
        Integer idParticipanteBusca = 77;
        EventoParticipante inscricaoMock = new EventoParticipante();
        inscricaoMock.setId(5);
        inscricaoMock.setEventId(12);
        inscricaoMock.setParticipanteId(idParticipanteBusca);

        when(daoMock.buscarPorIdParticipante(idParticipanteBusca)).thenReturn(inscricaoMock);

        EventoParticipante resultado = eventoParticipanteService.buscarPorIdParticipante(idParticipanteBusca);

        assertNotNull(resultado);
        assertEquals(idParticipanteBusca, resultado.getParticipanteId());
        verify(daoMock).buscarPorIdParticipante(idParticipanteBusca);
    }
    
    @Test
    @DisplayName("Deve retornar nulo ao buscar por um ID de inscrição inexistente")
    void deveRetornarNuloParaIdInexistente() {
        int idInexistente = 999;
        when(daoMock.buscarPorId(idInexistente)).thenReturn(null);
        
        EventoParticipante resultado = eventoParticipanteService.buscarPorId(idInexistente);
        
        assertNull(resultado);
        verify(daoMock).buscarPorId(idInexistente);
    }
}