package com.github.darrmirr.fp.hospital;

import java.util.Random;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Interface represent hospital's functions
 *
 */
public interface Hospital {

    static Function<Patient, String> visit(Doctor doctor) {
        return patient -> patient.diagnosis = doctor.diagnose(patient);
    }

    static Predicate<Patient> directTo(Doctor doctor) {
        return patient -> patient != null && patient.direction == doctor;
    }

    enum Doctor {
        SURGEON, NURSE, ANOTHER_DOCTOR, THERAPIST;

        private String diagnose(Patient patient) {
            return Stream
                    .of("x-ray is required", "visit procedure room", "(zzzZ)", "person is healthy")
                    .skip(new Random().nextInt(5))
                    .findAny()
                    .map(diagnosis -> name() + "'s diagnosis is '" + diagnosis + "'")
                    .orElse("doctor office is empty");
        }
    }
}
