package hu.poketerkep.master.service;

import hu.poketerkep.master.config.RedisConfig;
import hu.poketerkep.shared.datasource.PokemonDataSource;
import hu.poketerkep.shared.model.Pokemon;
import hu.poketerkep.shared.model.RandomPokemonGenerator;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {RedisConfig.class, DataManagerService.class, PokemonDataSource.class})
public class DataManagerServiceTest {

    @Autowired
    DataManagerService dataManagerService;

    @Autowired
    PokemonDataSource pokemonDataSource;

    @Test
    public void deleteOldPokemons() throws Exception {
        // Add test data
        Pokemon newPokemon = RandomPokemonGenerator.generate();
        newPokemon.setEncounterId("TEST_NEW_POKEMON");
        newPokemon.setDisappearTime(Instant.now().plus(5, ChronoUnit.MINUTES).toEpochMilli());

        Pokemon oldPokemon = RandomPokemonGenerator.generate();
        oldPokemon.setEncounterId("TEST_OLD_POKEMON");
        oldPokemon.setDisappearTime(Instant.now().minus(1, ChronoUnit.SECONDS).toEpochMilli());

        pokemonDataSource.addAll(Arrays.asList(newPokemon, oldPokemon));

        //Delete old pokemons
        dataManagerService.deleteOldPokemons();

        //Check results
        HashSet<Pokemon> pokemons = pokemonDataSource.getAll();

        Assert.assertTrue(isInDatabase(newPokemon, pokemons));
        Assert.assertFalse(isInDatabase(oldPokemon, pokemons));
    }

    private boolean isInDatabase(Pokemon pokemon, Collection<Pokemon> pokemons) {
        for (Pokemon p : pokemons) {
            if (pokemon.getEncounterId().equals(p.getEncounterId())) return true;
        }
        return false;
    }

}