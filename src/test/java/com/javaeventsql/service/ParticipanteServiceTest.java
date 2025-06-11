package com.javaeventsql.service;

import com.javaeventsql.dao.ParticipanteDao;
import com.javaeventsql.table.Participante;
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
class ParticipanteServiceTest {

    @Mock // Cria um mock (objeto falso) do ParticipanteDao
    private ParticipanteDao daoMock;

    @InjectMocks // Cria uma instância de ParticipanteService e injeta os mocks declarados com @Mock
    private ParticipanteService participanteService;

    @Test
    @DisplayName("Deve retornar uma lista de participantes ao chamar listarTodos")
    void deveListarTodosOsParticipantes() {
        // Arrange (Preparação)
        Participante p1 = new Participante(); // Supondo que a classe Participante exista
        p1.setId(1);
        p1.setNome("Ana");
        List<Participante> listaMock = Collections.singletonList(p1);

        // Define o comportamento do mock: quando dao.listarTodos() for chamado, retorne a listaMock
        when(daoMock.listarTodos()).thenReturn(listaMock);

        // Act (Ação)
        List<Participante> resultado = participanteService.listarTodos();

        // Assert (Verificação)
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Ana", resultado.get(0).getNome());

        // Verifica se o método do DAO foi chamado exatamente uma vez
        verify(daoMock, times(1)).listarTodos();
    }

    @Test
    @DisplayName("Deve retornar um participante específico ao buscar por ID")
    void deveBuscarParticipantePorId() {
        int idParaBuscar = 10;
        Participante participanteMock = new Participante();
        participanteMock.setId(idParaBuscar);
        participanteMock.setNome("Carlos");
        participanteMock.setEmail("carlos@email.com");

        when(daoMock.buscarPorId(idParaBuscar)).thenReturn(participanteMock);

        Participante resultado = participanteService.buscarPorId(idParaBuscar);

        assertNotNull(resultado);
        assertEquals(idParaBuscar, resultado.getId());
        assertEquals("Carlos", resultado.getNome());
        verify(daoMock).buscarPorId(idParaBuscar); // Verifica a chamada
    }
    
    @Test
    @DisplayName("Deve retornar nulo quando buscar por um ID que não existe")
    void deveRetornarNuloQuandoIdNaoExiste() {
        int idInexistente = 99;
        when(daoMock.buscarPorId(idInexistente)).thenReturn(null);
        
        Participante resultado = participanteService.buscarPorId(idInexistente);
        
        assertNull(resultado);
        verify(daoMock).buscarPorId(idInexistente);
    }

    @Test
    @DisplayName("Deve chamar o método de inserção do DAO e retornar a mensagem de sucesso")
    void deveInserirNovoParticipante() {
        String mensagemEsperada = "Participante inserido com sucesso.";
        when(daoMock.inserir(anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(mensagemEsperada);

        String resultado = participanteService.inserir("José", "M", "jose@email.com", "999998888", "12345678900", "false", "", "");

        assertEquals(mensagemEsperada, resultado);
        
        verify(daoMock).inserir("José", "M", "jose@email.com", "999998888", "12345678900", "false", "", "");
    }

    @Test
    @DisplayName("Deve chamar o método de exclusão do DAO ao excluir por ID")
    void deveExcluirParticipantePorId() {
        int idParaExcluir = 5;
        String mensagemEsperada = "Excluído com sucesso.";
        when(daoMock.excluirPorId(idParaExcluir)).thenReturn(mensagemEsperada);

        String resultado = participanteService.excluirPorId(idParaExcluir);

        assertEquals(mensagemEsperada, resultado);
        verify(daoMock, times(1)).excluirPorId(idParaExcluir);
    }
}