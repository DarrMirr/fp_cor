package com.github.darrmirr.fp.hospital;

/**
 * Hospital's patient model
 */
public class Patient {
    public Hospital.Doctor direction;
    public String conclusion;

    public Patient(Hospital.Doctor direction) {
        this.direction = direction;
    }
}
