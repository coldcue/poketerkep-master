package hu.poketerkep.master.api;


import hu.poketerkep.master.model.ScanLocation;
import hu.poketerkep.master.service.ScanService;
import hu.poketerkep.shared.datasource.PokemonDataSource;
import hu.poketerkep.shared.geo.Coordinate;
import hu.poketerkep.shared.model.Pokemon;
import hu.poketerkep.shared.validator.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@RestController
public class ClientController {

    private final PokemonDataSource pokemonDataSource;
    private final ScanService scanService;

    @Autowired
    public ClientController(PokemonDataSource pokemonDataSource, ScanService scanService) {
        this.pokemonDataSource = pokemonDataSource;
        this.scanService = scanService;
    }

    @PostMapping("addPokemons")
    public void addPokemons(@RequestBody Pokemon[] pokemons) throws ValidationException {
        pokemonDataSource.addAll(Arrays.asList(pokemons));
    }

    @GetMapping("nextScanLocations")
    public Collection<Coordinate> getNextScanLocations(@RequestParam int limit) {
        return scanService.getNextScanLocations(limit).stream()
                .map(ScanLocation::getCoordinate)
                .collect(Collectors.toList());
    }

}
