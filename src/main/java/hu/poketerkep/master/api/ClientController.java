package hu.poketerkep.master.api;


import hu.poketerkep.master.model.ScanLocation;
import hu.poketerkep.master.service.ScanService;
import hu.poketerkep.shared.api.ClientAPIEndpoint;
import hu.poketerkep.shared.datasource.PokemonDataSource;
import hu.poketerkep.shared.geo.Coordinate;
import hu.poketerkep.shared.model.Pokemon;
import hu.poketerkep.shared.validator.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collection;

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
    public ResponseEntity<Coordinate[]> nextScanLocations(@RequestParam int limit) {
        Collection<ScanLocation> nextScanLocations = scanService.getNextScanLocations(limit);

        if (nextScanLocations == null || nextScanLocations.size() == 0) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Coordinate[] coordinates = nextScanLocations.stream()
                .map(ScanLocation::getCoordinate)
                .toArray(Coordinate[]::new);

        return ResponseEntity.ok(coordinates);
    }


}
