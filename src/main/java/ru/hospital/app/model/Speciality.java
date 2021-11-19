package ru.hospital.app.model;

public enum Speciality {
    ORTHOPEDIST("Ортопед"),
    PEDIATRICIAN("Педиатр"),
    ALLERGIST("Аллерголог"),
    THERAPIST("Терапевт"),
    OTOLARYNGOLOGIST("Отоларинголог");

    private final String speciality;

    Speciality(String speciality) {
        this.speciality = speciality;
    }

    public String getSpeciality() {
        return speciality;
    }
}
