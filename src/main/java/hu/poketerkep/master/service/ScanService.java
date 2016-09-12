package hu.poketerkep.master.service;

import hu.poketerkep.master.dataservice.ScanPolygonDataService;
import hu.poketerkep.master.model.ScanLocation;
import hu.poketerkep.master.model.ScanPolygon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;
import java.util.logging.Logger;

@Service
public class ScanService {
    private final Logger log = Logger.getLogger(ScanService.class.getName());
    private final ScanPolygonDataService scanPolygonDataService;
    private final Queue<ScanLocation> scanLocationQueue = new ConcurrentLinkedQueue<>();

    private final Semaphore refreshScanPolygonsPermit = new Semaphore(1);
    private final Semaphore refreshQueuePermit = new Semaphore(1);

    private Set<ScanPolygon> scanPolygons;
    private Set<ScanLocation> scanLocations;

    @Autowired
    public ScanService(ScanPolygonDataService scanPolygonDataService) {
        this.scanPolygonDataService = scanPolygonDataService;

        scanPolygons = ConcurrentHashMap.newKeySet();
        scanLocations = ConcurrentHashMap.newKeySet();
    }

    /**
     * Refresh scanLocations every 8 hours
     */
    @Scheduled(fixedDelay = 8 * 60 * 60 * 1000)
    @Async
    public void refreshScanPolygons() {
        if (refreshScanPolygonsPermit.tryAcquire()) {
            scanPolygons.clear();

            //Generate scan locations and add all polygons
            scanPolygonDataService.getAll().forEach(scanPolygons::add);

            scanLocations.clear();

            //Generate locations
            scanPolygons.forEach(scanPolygon -> scanLocations.addAll(scanPolygon.generateScanLocations()));

            //Release permit
            refreshScanPolygonsPermit.release();

            log.info("Scan polygons refreshed with " + scanPolygons.size() + " polygons and " + scanLocations.size() + " scan locations");
        } else {
            try {
                refreshScanPolygonsPermit.acquire();
                refreshScanPolygonsPermit.release();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Refresh queue every 30 sec
     */
    @Scheduled(fixedDelay = 30 * 1000)
    @Async
    public void refreshQueue() {
        if (refreshQueuePermit.tryAcquire()) {
            Instant now = Instant.now();
            ArrayList<ScanLocation> list = new ArrayList<>(scanLocations.size());
            for (ScanLocation scanLocation : scanLocations) {
                scanLocation.setCompareNow(now);
                list.add(scanLocation);
            }

            try {
                Collections.sort(list);
            } catch (IllegalArgumentException e) {
                log.severe("ScanLocation has an error: " + e.getMessage());
            }

            scanLocationQueue.clear();
            scanLocationQueue.addAll(list);

            log.info("ScanLocation queue refreshed!");
            refreshQueuePermit.release();
        } else {
            try {
                refreshQueuePermit.acquire();
                refreshQueuePermit.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Get next scan locations
     *
     * @param limit the limit
     * @return
     */
    public Collection<ScanLocation> getNextScanLocations(int limit) {
        if (scanLocationQueue.size() == 0) {
            refreshQueue();
        }

        Collection<ScanLocation> result = new ArrayList<>();
        Instant now = Instant.now();

        for (int i = 0; i < limit; i++) {
            ScanLocation poll = null;
            synchronized (scanLocationQueue) {
                poll = scanLocationQueue.poll();
            }
            if (poll == null) break;
            result.add(poll);
            poll.setLastScanned(now);
        }

        log.info("Next scan location - scan polygons: " + scanPolygons.size() + " scan locations: " + scanLocations.size() + " scan locations queue:" + scanLocationQueue.size());

        return result;
    }
}
