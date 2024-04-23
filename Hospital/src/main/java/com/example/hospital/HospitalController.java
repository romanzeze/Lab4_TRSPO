package com.example.hospital;

import com.example.hospital.entries.Doctor;
import com.example.hospital.entries.Nurse;
import com.example.hospital.entries.Patient;
import com.example.hospital.repos.DoctorRepository;
import com.example.hospital.repos.NurseRepository;
import com.example.hospital.repos.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/hospital")
public class HospitalController {
    @Autowired
    private  NurseRepository nurseRepository;
    @Autowired
    private  DoctorRepository doctorRepository;
    @Autowired
    private  PatientRepository patientRepository;

    public HospitalController(NurseRepository nurseRepository, DoctorRepository doctorRepository,
                              PatientRepository patientRepository) {
        this.nurseRepository = nurseRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
    }

    @PostMapping("/nurse-cure")
    public ResponseEntity<String> nurseCure(@RequestParam int nurseId, @RequestParam int patientId) {
        Optional<Nurse> nurseOptional = nurseRepository.findById(nurseId);
        Optional<Patient> patientOptional = patientRepository.findById(patientId);

        if (nurseOptional.isEmpty() || patientOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid nurse or patient ID");
        }

        Nurse nurse = nurseOptional.get();
        Patient patient = patientOptional.get();

        if (!Objects.equals(patient.getStringPrescription(), "Operation")) {
            patient.curePatient();
            patientRepository.save(patient);
        }
        else
        {
            return ResponseEntity.ok("Nurse is not capable of operating a patient");
        }

        return ResponseEntity.ok("Nurse " + nurse.getName() + " cured patient " + patient.getName());
    }

    @PostMapping("/doctor-cure")
    public ResponseEntity<String> doctorCure(@RequestParam int doctorId, @RequestParam int patientId) {
        Optional<Doctor> doctorOptional = doctorRepository.findById(doctorId);
        Optional<Patient> patientOptional = patientRepository.findById(patientId);

        if (doctorOptional.isEmpty() || patientOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid doctor or patient ID");
        }

        Doctor doctor = doctorOptional.get();
        Patient patient = patientOptional.get();

        patient.curePatient();
        patientRepository.save(patient);

        return ResponseEntity.ok("Doctor " + doctor.getName() + " cured patient " + patient.getName());
    }
}
