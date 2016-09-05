package hu.poketerkep.master.api;


import hu.poketerkep.master.model.ScanLocation;
import hu.poketerkep.master.service.ScanService;
import hu.poketerkep.shared.api.ClientAPIEndpoint;
import hu.poketerkep.shared.datasource.PokemonDataSource;
import hu.poketerkep.shared.geo.Coordinate;
import hu.poketerkep.shared.model.Pokemon;
import hu.poketerkep.shared.validator.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/client")
public class ClientController implements ClientAPIEndpoint {

    private final PokemonDataSource pokemonDataSource;
    private final ScanService scanService;

    @Autowired
    public ClientController(PokemonDataSource pokemonDataSource, ScanService scanService) {
        this.pokemonDataSource = pokemonDataSource;
        this.scanService = scanService;
    }

    @Override
    @PostMapping("/addPokemons")
    public ResponseEntity<Void> addPokemons(@RequestBody Pokemon[] pokemons) {
        try {
            if (pokemons.length == 1) {
                pokemonDataSource.add(pokemons[0]);
            } else {
                pokemonDataSource.addAll(Arrays.asList(pokemons));
            }
        } catch (ValidationException e) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();
    }

    @Override
    @GetMapping("/nextScanLocations")
    public ResponseEntity<Collection<Coordinate>> nextScanLocations(@RequestParam int limit) {
        return ResponseEntity.ok(scanService.getNextScanLocations(limit).stream()
                .map(ScanLocation::getCoordinate)
                .collect(Collectors.toList()));
    }


}
