package hu.poketerkep.master.service;

import hu.poketerkep.shared.datasource.PokemonDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.logging.Logger;

@Service
public class DataManagerService {
    private final Logger log = Logger.getLogger(DataManagerService.class.getName());
    private final PokemonDataSource pokemonDataSource;

    @Autowired
    public DataManagerService(PokemonDataSource pokemonDataSource) {
        this.pokemonDataSource = pokemonDataSource;
    }

    @Scheduled(fixedRate = 5000)
    public void deleteOldPokemons() {
        long now = Instant.now().toEpochMilli();
        long count = pokemonDataSource.getAll().stream()
                .filter(pokemon -> pokemon.getDisappearTime() < now)
                .peek(pokemonDataSource::remove)
                .count();

        log.info("Deleted " + count + " old pokemons");
    }
}
