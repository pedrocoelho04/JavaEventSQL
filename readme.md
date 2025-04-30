# Sistema de Gerenciamento de Eventos

Este sistema permite gerenciar **participantes** e **eventos** de forma simples, utilizando Java com Swing e SQLite.

## 🎯 Funcionalidades

- Cadastro de participantes
- Consulta e exclusão de participantes
- Cadastro de eventos
- Consulta e exclusão de eventos

---

## 🖥️ Telas do Sistema

### `HomeFrame`
Tela principal do sistema com acesso às funcionalidades:
- Cadastrar participante
- Consultar participantes
- Consultar eventos

---

### `CadastroFrame`
Formulário para cadastro de participantes. Campos:
- Nome
- Sexo (F ou M)
- Email
- Celular
- Palestrante (S ou N)

---

### `ConsultaFrame`
Tela para visualizar e consultar os participantes cadastrados.
- Permite buscar por nome, sexo e email.
- Possui botão para **excluir participante selecionado**.

---

### `CadastroEventoFrame`
Formulário para cadastro de eventos. Campos:
- Título
- Local
- DataEvento
- Detalhes

---

### `ConsultaEventosFrame`
Tela para visualizar e consultar eventos cadastrados.
- Lista todos os eventos registrados.
- Possui botão para **excluir evento selecionado**.

---

## 🗃️ Banco de Dados

- Banco: SQLite
- Tabelas:
  - `participante` (id, nome, sexo, email, celular, ePalestrante)
  - `evento` (id, titulo, local)

---
## 📺 Apresentação
- [Slide de apresentação](https://www.canva.com/design/DAGmJuozI_E/Q9yAGJSLwwTNtMrLV7lahg/view?utm_content=DAGmJuozI_E&utm_campaign=designshare&utm_medium=link2&utm_source=uniquelinks&utlId=hbab1ada0e1)
---
## 🧑‍💻 Contribuidores

- [@Dspofu](https://github.com/Dspofu)
- [@SammyKunimatsu](https://github.com/SammyKunimatsu)
- [@PedroCoelho04](https://github.com/pedrocoelho04)
