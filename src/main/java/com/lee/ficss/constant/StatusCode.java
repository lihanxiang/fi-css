package com.lee.ficss.constant;

public enum StatusCode {

    // 1xx
    USER_NOT_LOGIN(100, "Your authentication is unavailable now, please login again"),

    // 2xx
    SUCCESS(200, "success"),

    // 3xx
    PAPER_SUBMITTED_SUCCESSFULLY(300, "Paper submitted successfully"),
    SUBMISSION_PROCESS_INTERRUPTED(301, "Submission process interrupted, please try again. " +
            "If it doesn't work, contact with the administrator"),
    SUBMISSION_NOT_FOUND(302, "There is no submission for now"),

    // 4xx
    CREATE_AGENDA_SUCCESSFULLY(400, "Create agenda successfully"),
    AGENDA_NOT_FOUND(401, "There is no agenda for now"),
    CREATE_AGENDA_PROCESS_FAILED(301, "Create agenda failed, please try again. " +
            "If it doesn't work, contact with the administrator"),




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
