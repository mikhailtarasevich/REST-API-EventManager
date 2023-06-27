package com.mikhail.tarasevich.eventmanager.util;

import java.util.Arrays;

public enum Status {

    PENDING(0), REJECTED(1), ACCEPTED(2);

    private final int id;

    Status(int id) {
        this.id = id;
    }

    public static Status getById(int id) {

        return Arrays.stream(Status.values())
                .filter(gender -> gender.id == id)
                .findFirst().orElse(null);
    }

    public int getId() {

        return id;
    }

}
