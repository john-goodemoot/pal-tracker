package io.pivotal.pal.tracker;

import io.pivotal.pal.tracker.TimeEntry;
import io.pivotal.pal.tracker.TimeEntryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/time-entries")
public class TimeEntryController {

    private TimeEntryRepository timeEntryRepository;


    public TimeEntryController(TimeEntryRepository repository) {
        this.timeEntryRepository = repository;
    }

    @PostMapping
    public ResponseEntity create(@RequestBody TimeEntry timeEntry) {
    TimeEntry time=timeEntryRepository.create(timeEntry);
    return new ResponseEntity<>(time, HttpStatus.CREATED);


    }

    @PutMapping("{id}")
    public ResponseEntity update(@PathVariable Long id,@RequestBody  TimeEntry timeEntry) {
        TimeEntry newTimeEntry = timeEntryRepository.update(id,timeEntry);
        if(newTimeEntry == null){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(newTimeEntry,HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<List<TimeEntry>> list(){

       return new ResponseEntity<List<TimeEntry>> (timeEntryRepository.list(), HttpStatus.OK);
    }



    @GetMapping("{id}")
    public ResponseEntity<TimeEntry> read(@PathVariable Long id) {
        TimeEntry generatedTimeEntry=timeEntryRepository.find(id);
        if(generatedTimeEntry==null){
            return new ResponseEntity<TimeEntry>(generatedTimeEntry,HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<TimeEntry>(generatedTimeEntry,HttpStatus.OK);

    }
    @DeleteMapping("{id}")
    public ResponseEntity<TimeEntry> delete(@PathVariable Long id) {
        timeEntryRepository.delete(id);
        return new ResponseEntity<TimeEntry>(HttpStatus.NO_CONTENT);
    }
}
