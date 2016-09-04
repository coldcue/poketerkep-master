package hu.poketerkep.master.service;

import hu.poketerkep.master.dataservice.ScanPolygonDataService;
import hu.poketerkep.master.model.ScanLocation;
import hu.poketerkep.master.model.ScanPolygon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class ScanService {

    private final ScanPolygonDataService scanPolygonDataService;
    private Set<ScanPolygon> scanPolygons;
    private SortedSet<ScanLocation> scanLocations;

    @Autowired
    public ScanService(ScanPolygonDataService scanPolygonDataService) {
        this.scanPolygonDataService = scanPolygonDataService;

        scanPolygons = ConcurrentHashMap.newKeySet();
        scanLocations = Collections.synchronizedSortedSet(new TreeSet<>());

        //Generate scanlocations and add all polygons
        scanPolygonDataService.getAll().forEach(scanPolygons::add);

        //Generate locations
        scanPolygons.forEach(scanPolygon -> scanLocations.addAll(scanPolygon.generateScanLocations()));
    }

    public Collection<ScanLocation> getNextScanLocations(int limit) {
        return scanLocations.stream()
                .limit(limit)
                .peek(scanLocation -> scanLocation.setLastScanned(Instant.now()))
                .collect(Collectors.toList());
    }
}
