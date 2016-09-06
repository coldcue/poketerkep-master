package hu.poketerkep.master.service;

import hu.poketerkep.master.dataservice.ScanPolygonDataService;
import hu.poketerkep.master.model.ScanLocation;
import hu.poketerkep.master.model.ScanPolygon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.logging.Logger;

@Service
public class ScanService {
    private final Logger log = Logger.getLogger(ScanService.class.getName());
    private final ScanPolygonDataService scanPolygonDataService;
    private Set<ScanPolygon> scanPolygons;
    private Set<ScanLocation> scanLocations;
    private Queue<ScanLocation> scanLocationQueue = new PriorityBlockingQueue<>();

    @Autowired
    public ScanService(ScanPolygonDataService scanPolygonDataService) {
        this.scanPolygonDataService = scanPolygonDataService;

        scanPolygons = ConcurrentHashMap.newKeySet();
        scanLocations = ConcurrentHashMap.newKeySet();
    }

    /**
     * Refresh scanLocations every 15 min
     */
    @Scheduled(fixedDelay = 15 * 60 * 1000)
    public synchronized void refreshScanPolygons() {
        scanPolygons.clear();

        //Generate scan locations and add all polygons
        scanPolygonDataService.getAll().forEach(scanPolygons::add);

        scanLocations.clear();

        //Generate locations
        scanPolygons.forEach(scanPolygon -> scanLocations.addAll(scanPolygon.generateScanLocations()));

        log.info("Scan polygons refreshed with " + scanPolygons.size() + " polygons and " + scanLocations.size() + " scan locations");
    }

    /**
     * Refresh queue
     */
    @Scheduled(fixedDelay = 1000)
    public synchronized void refreshQueue() {
        scanLocationQueue.clear();

        Instant now = Instant.now();
        for (ScanLocation scanLocation : scanLocations) {
            scanLocation.setCompareNow(now);
            scanLocationQueue.offer(scanLocation);
        }

    }

    public Collection<ScanLocation> getNextScanLocations(int limit) {
        SortedSet<ScanLocation> result = new TreeSet<>();
        Instant now = Instant.now();

        for (int i = 0; i < limit; i++) {
            ScanLocation poll = scanLocationQueue.poll();
            if (poll == null) break;
            result.add(poll);
            poll.setLastScanned(now);
        }

        return result;
    }
}
