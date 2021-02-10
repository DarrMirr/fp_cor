package com.github.darrmirr.fp.hospital;

import java.util.function.Predicate;

/**
 * Interface represent hospital's functions
 *
 */
public interface Hospital {

    static Predicate<Patient> directTo(Doctor doctor) {
        return patient -> patient != null && patient.direction == doctor;
    }

    enum Doctor {
        SURGEON, NURSE, ANOTHER_DOCTOR, THERAPIST
    }
}
