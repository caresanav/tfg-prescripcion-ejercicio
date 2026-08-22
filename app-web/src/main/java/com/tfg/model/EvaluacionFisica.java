package com.tfg.model;

// Creada por Carmen Esau
// Objeto de datos para la evaluación física realizada por el fisioterapeuta

import com.tfg.model.enums.Nivel;
import com.tfg.model.enums.Equilibrio;

public class EvaluacionFisica {

    // Campo que rellena el fisio (puedes cambiar el nombre si quieres)
    private String comentarioFisioterapeuta;

    // Nuevos campos
    private Integer dolorEscala; // 0-10
    private String limitacionFuncional; // ej: "subir escaleras"
    private String rangoMovimiento; // ej: "flexión rodilla limitada"
    private String observacionesPostura; // ej: "anteversión pélvica"

    private Nivel nivelMovilidad; // "Baja", "Media", "Alta"
    private Equilibrio equilibrio; // "Malo", "Medio", "Bueno"
    private Nivel fuerzaGeneral; // "Baja", "Media", "Alta"
    private String recomendacionesFisio; // texto libre

    public EvaluacionFisica() {
    }

    public String getComentarioFisioterapeuta() {
        return comentarioFisioterapeuta;
    }

    public void setComentarioFisioterapeuta(String comentarioFisioterapeuta) {
        this.comentarioFisioterapeuta = comentarioFisioterapeuta;
    }

    public Integer getDolorEscala() {
        return dolorEscala;
    }

    public void setDolorEscala(Integer dolorEscala) {
        this.dolorEscala = dolorEscala;
    }

    public String getLimitacionFuncional() {
        return limitacionFuncional;
    }

    public void setLimitacionFuncional(String limitacionFuncional) {
        this.limitacionFuncional = limitacionFuncional;
    }

    public String getRangoMovimiento() {
        return rangoMovimiento;
    }

    public void setRangoMovimiento(String rangoMovimiento) {
        this.rangoMovimiento = rangoMovimiento;
    }

    public String getObservacionesPostura() {
        return observacionesPostura;
    }

    public void setObservacionesPostura(String observacionesPostura) {
        this.observacionesPostura = observacionesPostura;
    }

    public Nivel getNivelMovilidad() {
        return nivelMovilidad;
    }

    public void setNivelMovilidad(Nivel nivelMovilidad) {
        this.nivelMovilidad = nivelMovilidad;
    }

    public Equilibrio getEquilibrio() {
        return equilibrio;
    }

    public void setEquilibrio(Equilibrio equilibrio) {
        this.equilibrio = equilibrio;
    }

    public Nivel getFuerzaGeneral() {
        return fuerzaGeneral;
    }

    public void setFuerzaGeneral(Nivel fuerzaGeneral) {
        this.fuerzaGeneral = fuerzaGeneral;
    }

    public String getRecomendacionesFisio() {
        return recomendacionesFisio;
    }

    public void setRecomendacionesFisio(String recomendacionesFisio) {
        this.recomendacionesFisio = recomendacionesFisio;
    }
}
