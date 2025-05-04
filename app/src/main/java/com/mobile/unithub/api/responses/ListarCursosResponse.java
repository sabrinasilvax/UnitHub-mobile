package com.mobile.unithub.api.responses;

public class ListarCursosResponse {
    private int cursoId;
    private String nome;
    private String categoria;

    public ListarCursosResponse(int cursoId, String nome, String categoria) {
        this.cursoId = cursoId;
        this.nome = nome;
        this.categoria = categoria;
    }

    public int getCursoId() {
        return cursoId;
    }

    public void setCursoId(int cursoId) {
        this.cursoId = cursoId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}
