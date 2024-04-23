package com.example.nurse;

import com.example.nurse.NurseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/nurses")
public class NurseController {

    @Autowired
    private NurseRepository nurseRepository;

    @GetMapping("/")
    public List<Nurse> getAllNurses() {
        List<Nurse> nurses = nurseRepository.findAll();
        return nurses;
    }

    @PostMapping("/")
    public ResponseEntity<String> addNurse(@RequestBody Nurse nurse) {
        nurseRepository.save(nurse);
        return new ResponseEntity<>("New Nurse added successfully", HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Nurse> updateNurse(@PathVariable(value = "id") int nurseId,
                                             @RequestBody Nurse nurseDetails) throws Exception {
        Nurse nurse = nurseRepository.findById(nurseId)
                .orElseThrow(() -> new Exception("Nurse not found with ID: " + nurseId));

        nurse.setName(nurseDetails.getName());
        nurse.setProfile(nurseDetails.getProfile());

        Nurse updatedNurse = nurseRepository.save(nurse);
        return ResponseEntity.ok(updatedNurse);
    }

    @DeleteMapping("/delete/{id}")
    public Map<String, Boolean> deleteNurse(@PathVariable(value = "id") int nurseId) throws Exception {
        Nurse nurse = nurseRepository.findById(nurseId)
                .orElseThrow(() -> new Exception("Nurse not found with ID: " + nurseId));

        nurseRepository.delete(nurse);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response ;

    }


}
