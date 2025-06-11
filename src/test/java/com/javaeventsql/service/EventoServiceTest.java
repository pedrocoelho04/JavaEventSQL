package com.javaeventsql.service;

import com.javaeventsql.dao.EventoDao;
import com.javaeventsql.table.Evento;
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

@ExtendWith(MockitoExtension.class) // Habilita a integração do Mockito com JUnit 5
class EventoServiceTest {

    @Mock // Cria uma instância "falsa" (mock) do EventoDao
    private EventoDao daoMock;

    @InjectMocks // Cria uma instância real do EventoService e injeta o daoMock nela
    private EventoService eventoService;

    @Test
    @DisplayName("Deve retornar uma lista de eventos ao chamar listarTodos")
    void deveListarTodosOsEventos() {
        // Arrange (Preparação)
        Evento evento = new Evento();
        evento.setId(1);
        evento.setTitulo("Java Conference");
        List<Evento> listaMock = Collections.singletonList(evento);

        // Define o que o mock deve fazer: quando dao.listarTodos() for chamado, retorne a listaMock
        when(daoMock.listarTodos()).thenReturn(listaMock);

        // Act (Ação)
        List<Evento> resultado = eventoService.listarTodos();

        // Assert (Verificação)
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Java Conference", resultado.get(0).getTitulo());
        verify(daoMock).listarTodos(); // Verifica se o método do DAO foi chamado
    }

    @Test
    @DisplayName("Deve retornar um evento específico ao buscar por ID")
    void deveBuscarEventoPorId() {
        // Arrange
        int idParaBuscar = 42;
        Evento eventoMock = new Evento();
        eventoMock.setId(idParaBuscar);
        eventoMock.setTitulo("Workshop de Testes");
        eventoMock.setLocal("Online");

        when(daoMock.buscarPorId(idParaBuscar)).thenReturn(eventoMock);

        // Act
        Evento resultado = eventoService.buscarPorId(idParaBuscar);

        // Assert
        assertNotNull(resultado);
        assertEquals(idParaBuscar, resultado.getId());
        assertEquals("Workshop de Testes", resultado.getTitulo());
        verify(daoMock).buscarPorId(idParaBuscar);
    }
    
    @Test
    @DisplayName("Deve retornar nulo quando buscar um ID de evento que não existe")
    void deveRetornarNuloAoBuscarIdInexistente() {
        // Arrange
        int idInexistente = 999;
        when(daoMock.buscarPorId(idInexistente)).thenReturn(null);
        
        // Act
        Evento resultado = eventoService.buscarPorId(idInexistente);
        
        // Assert
        assertNull(resultado);
        verify(daoMock).buscarPorId(idInexistente);
    }

    @Test
    @DisplayName("Deve inserir um novo evento e retornar a mensagem de sucesso")
    void deveInserirNovoEvento() {
        // Arrange
        String mensagemEsperada = "Evento inserido com sucesso.";
        String titulo = "Meetup de IA";
        String local = "Auditório Principal";
        String data = "2025-10-20";
        String detalhes = "Discussão sobre novas tecnologias de IA.";
        
        // anyString() pode ser usado se os parâmetros exatos não importarem para o mock
        when(daoMock.inserir(titulo, local, data, detalhes)).thenReturn(mensagemEsperada);

        // Act
        String resultado = eventoService.inserir(titulo, local, data, detalhes);

        // Assert
        assertEquals(mensagemEsperada, resultado);
        verify(daoMock).inserir(titulo, local, data, detalhes); // Verifica se o DAO foi chamado com os parâmetros corretos
    }

    @Test
    @DisplayName("Deve excluir um evento por ID e retornar a mensagem de sucesso")
    void deveExcluirEventoPorId() {
        // Arrange
        int idParaExcluir = 7;
        String mensagemEsperada = "Evento excluído com sucesso.";
        when(daoMock.excluirPorId(idParaExcluir)).thenReturn(mensagemEsperada);

        // Act
        String resultado = eventoService.excluirPorId(idParaExcluir);

        // Assert
        assertEquals(mensagemEsperada, resultado);
        verify(daoMock, times(1)).excluirPorId(idParaExcluir); // Verifica se o método foi chamado exatamente uma vez
    }
    
    @Test
    @DisplayName("Deve atualizar um evento e retornar a mensagem de sucesso")
    void deveAtualizarUmEvento() {
        // Arrange
        Evento eventoParaAtualizar = new Evento();
        eventoParaAtualizar.setId(10);
        eventoParaAtualizar.setTitulo("Título Atualizado");
        
        String mensagemEsperada = "Evento atualizado com sucesso.";
        when(daoMock.atualizar(eventoParaAtualizar)).thenReturn(mensagemEsperada);
        
        // Act
        String resultado = eventoService.atualizar(eventoParaAtualizar);
        
        // Assert
        assertEquals(mensagemEsperada, resultado);
        verify(daoMock).atualizar(eventoParaAtualizar);
    }
}