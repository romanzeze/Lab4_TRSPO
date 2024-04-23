    package com.example.patient;

    import com.example.patient.Patient;
    import com.example.patient.PatientRepository;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    import java.util.HashMap;
    import java.util.List;
    import java.util.Map;

    @RestController
    @RequestMapping("/patients")
    public class PatientController {

        @Autowired
        private PatientRepository patientRepository;

        // Get all patients
        @GetMapping("")
        public List<Patient> getAllPatients() {
            return patientRepository.findAll();
        }

        // Get a patient by id
        @GetMapping("/{id}")
        public ResponseEntity<Patient> getPatientById(@PathVariable(value = "id") int patientId)
                throws Exception {
            Patient patient = patientRepository.findById(patientId)
                    .orElseThrow(() -> new Exception("Patient not found for this id :: " + patientId));
            return ResponseEntity.ok().body(patient);
        }

        // Add a new patient
        @PostMapping("")
        public Patient createPatient( @RequestBody Patient patient) {
            return patientRepository.save(patient);
        }

        // Update a patient
        @PutMapping("/{id}")
        public ResponseEntity<Patient> updatePatient(@PathVariable(value = "id") int patientId,
                                                     @RequestBody Patient patientDetails) throws Exception {
            Patient patient = patientRepository.findById(patientId)
                    .orElseThrow(() -> new Exception("Patient not found for this id :: " + patientId));

            patient.setName(patientDetails.getName());
            patient.setDiagnosis(patientDetails.getDiagnosis());
            patient.setPrescription(patientDetails.getStringPrescription());

            final Patient updatedPatient = patientRepository.save(patient);
            return ResponseEntity.ok(updatedPatient);
        }

        // Delete a patient
        @DeleteMapping("/{id}")
        public Map<String, Boolean> deletePatient(@PathVariable(value = "id") int patientId)
                throws Exception {
            Patient patient = patientRepository.findById(patientId)
                    .orElseThrow(() -> new Exception("Patient not found for this id :: " + patientId));

            patientRepository.delete(patient);
            Map<String, Boolean> response = new HashMap<>();
            response.put("deleted", Boolean.TRUE);
            return response;
        }


    }