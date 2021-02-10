package com.github.darrmirr.fp;

import com.github.darrmirr.fp.hospital.Hospital;
import com.github.darrmirr.fp.hospital.Patient;
import com.github.darrmirr.fp.utils.Chain;

import static com.github.darrmirr.fp.hospital.Hospital.Doctor.*;

/**
 * Example 3:
 *
 *     Chain java.util.function.Function
 *     Example represent case of patient visit hospital
 *
 */
public interface MainHospital {

    static void main(String[] args) {
        var newPatient = new Patient(THERAPIST);

        var hospital = Chain
                .chain((Patient patient) ->
                        patient.conclusion = THERAPIST + " - person is healthy")
                .responsibility(Hospital.directTo(THERAPIST)::test)
                .chain(patient ->
                        patient.conclusion = ANOTHER_DOCTOR + " - (zzzZ)")
                .responsibility(Hospital.directTo(ANOTHER_DOCTOR)::test)
                .chain(patient ->
                        patient.conclusion = NURSE + " - procedure room is busy")
                .responsibility(Hospital.directTo(NURSE)::test)
                .chain(patient ->
                        patient.conclusion = SURGEON + " - x-ray is required")
                .responsibility(Hospital.directTo(SURGEON)::test);

        System.out.println("Hospital visit result : " + hospital.apply(newPatient));
    }
}
