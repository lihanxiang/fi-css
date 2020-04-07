package com.lee.ficss.constant;

public enum StatusCode {

    // 2xx Success
    SUCCESS(200, "success"),
    CREATE_CONFERENCE_SUCCESS(201, "Create conference success"),
    CREATE_AGENDA_SUCCESS(202, "Create agenda success"),
    CREATE_EVENT_SUCCESS(203, "Create event success"),
    CREATE_SESSION_SUCCESS(204, "Create session success"),
    SUBMIT_PAPER_SUCCESS(205, "Submit paper success"),
    CREATE_TOPIC_SUCCESS(206, "Create topic success"),
    CONFERENCE_FOUND(207, "Conference found"),
    AGENDA_FOUND(208, "Agenda found"),
    EVENT_FOUND(209, "Event found"),
    SESSION_FOUND(210, "Session found"),
    SUBMISSION_FOUND(211, "Submission found"),
    TOPIC_FOUND(212, "Topic found"),
    CANDIDATE_FOUND(213, "Candidate found"),


    // 3xx Found
    CONFERENCE_ALREADY_EXIST(300, "Same conference already exists"),
    AGENDA_ALREADY_EXIST(301, "Same agenda already exists in this conference"),
    EVENT_ALREADY_EXIST(302, "Same event already exists at this time"),
    TOPIC_ALREADY_EXIST(303, "Same topic already exists in this conference"),


    // 4xx Not Found
    CONFERENCE_NOT_FOUND(400, "There is no conference found"),
    AGENDA_NOT_FOUND(401, "There is no agenda found"),
    EVENT_NOT_FOUND(402, "There is no event found"),
    SESSION_NOT_FOUND(403, "There is no session found"),
    SUBMISSION_NOT_FOUND(404, "There is no submission found"),
    TOPIC_NOT_FOUND(405, "There is no topic found"),
    CANDIDATE_NOT_FOUND(406, "There is no candidate found"),
    PAPER_NOT_FOUND(407, "No paper found in this session"),
    NO_AVAILABLE_PAPER(408, "No paper available for this session"),

    // 5xx Warning/Error
    INTERNAL_SERVER_ERROR(502, "Internal server error"),
    SUBMISSION_PROCESS_INTERRUPTED(500, "Submission process interrupted, please try again. " +
            "If it doesn't work, contact with the administrator"),
    CREATE_AGENDA_PROCESS_FAILED(501, "Create agenda failed, please try again. " +
            "If it doesn't work, contact with the administrator"),

    ONLY_ONE_SUBMISSION_IN_EACH_CONFERENCE(502, "For every conference, each candidate " +
            "can only have one submission"),
    FILE_ALREADY_EXIST(5, "File is already exist in the database"),


    ;



    private int code;
    private String message;

    StatusCode(int code, String message){
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
