package edu.upc.dsa.models;

public class ChatIndividual {
    String participantes;
    String name;
    String comentario;

    public ChatIndividual(){}

    public ChatIndividual(String participante1, String participante2, String name, String comentario) {
        this.participantes = participante1 +","+participante2;
        this.name = name;
        this.comentario = comentario;
    }

    public String getParticipantes() {
        return participantes;
    }

    public void setParticipantes(String participantes) {
        this.participantes = participantes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
}
