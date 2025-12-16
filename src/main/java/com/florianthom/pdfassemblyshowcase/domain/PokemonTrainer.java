package com.florianthom.pdfassemblyshowcase.domain;

import java.time.LocalDate;
import java.util.List;

public class PokemonTrainer {
    public String trainerId;
    public LocalDate birthdate;
    public List<Pokemon> pokemonteam;

    public PokemonTrainer(String trainerId, LocalDate birthdate, List<Pokemon> pokemonteam){
        this.trainerId = trainerId;
        this.birthdate = birthdate;
        this.pokemonteam = pokemonteam;
    }
}