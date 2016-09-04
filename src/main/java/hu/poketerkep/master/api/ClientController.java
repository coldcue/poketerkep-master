package hu.poketerkep.master.api;


import hu.poketerkep.master.model.ScanLocation;
import hu.poketerkep.master.service.ScanService;
import hu.poketerkep.shared.datasource.PokemonDataSource;
import hu.poketerkep.shared.model.Pokemon;
import hu.poketerkep.shared.validator.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collection;

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

    //@JsonView(ScanLocation.View.OnlyCoordinates.class)
    @GetMapping("nextScanLocations")
    public Collection<ScanLocation> getNextScanLocations(@RequestParam(required = false, defaultValue = "100") int limit) {
        return scanService.getNextScanLocations(limit);
    }

}
