package hu.poketerkep.master.service;

import hu.poketerkep.master.dataservice.ScanPolygonDataService;
import hu.poketerkep.master.model.ScanLocation;
import hu.poketerkep.master.model.ScanPolygon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class ScanService {

    private final ScanPolygonDataService scanPolygonDataService;
    private Set<ScanPolygon> scanPolygons;
    private Set<ScanLocation> scanLocations;

    @Autowired
    public ScanService(ScanPolygonDataService scanPolygonDataService) {
        this.scanPolygonDataService = scanPolygonDataService;

        scanPolygons = ConcurrentHashMap.newKeySet();
        scanLocations = new HashSet<>();

        //Generate scanlocations and add all polygons
        scanPolygonDataService.getAll().forEach(scanPolygons::add);

        //Generate locations
        scanPolygons.forEach(scanPolygon -> scanLocations.addAll(scanPolygon.generateScanLocations()));
    }

    public Collection<ScanLocation> getNextScanLocations(int limit) {
        return scanLocations.stream()
                .sorted(ScanLocation::compareTo)
                .limit(limit)
                .peek(scanLocation -> scanLocation.setLastScanned(Instant.now()))
                .collect(Collectors.toList());
    }
}
