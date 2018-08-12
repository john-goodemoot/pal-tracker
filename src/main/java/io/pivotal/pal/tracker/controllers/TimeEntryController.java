package io.pivotal.pal.tracker.controllers;

import io.pivotal.pal.tracker.model.TimeEntry;
import io.pivotal.pal.tracker.repositories.TimeEntryRepository;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.boot.actuate.metrics.GaugeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/time-entries")
public class TimeEntryController {

    private TimeEntryRepository timeEntryRepository;
    private CounterService counter = null;
    private GaugeService gauge = null;

    public TimeEntryController(TimeEntryRepository repository,
                               CounterService counter,
                               GaugeService gauge
    ) {
        this.timeEntryRepository = repository;
        this.counter = counter;
        this.gauge = gauge;
    }

    @PostMapping
    public ResponseEntity create(@RequestBody TimeEntry timeEntry) {
        TimeEntry time=timeEntryRepository.create(timeEntry);
        counter.increment("TimeEntry.created");
        gauge.submit("timeEntries.count", timeEntryRepository.list().size());
        return new ResponseEntity<>(time, HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity update(@PathVariable Long id,@RequestBody  TimeEntry timeEntry) {
        TimeEntry newTimeEntry = timeEntryRepository.update(id,timeEntry);
        if(newTimeEntry == null){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        counter.increment("TimeEntry.updated");
        return new ResponseEntity(newTimeEntry,HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<List<TimeEntry>> list(){
        counter.increment("TimeEntry.listed");
        return new ResponseEntity<List<TimeEntry>> (timeEntryRepository.list(), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<TimeEntry> read(@PathVariable Long id) {
        TimeEntry generatedTimeEntry=timeEntryRepository.find(id);
        if(generatedTimeEntry==null){
            return new ResponseEntity<TimeEntry>(generatedTimeEntry,HttpStatus.NOT_FOUND);
        }
        counter.increment("TimeEntry.read");
        return new ResponseEntity<TimeEntry>(generatedTimeEntry,HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<TimeEntry> delete(@PathVariable Long id) {
        timeEntryRepository.delete(id);
        counter.increment("TimeEntry.deleted");
        gauge.submit("timeEntries.count", timeEntryRepository.list().size());
        return new ResponseEntity<TimeEntry>(HttpStatus.NO_CONTENT);
    }
}
