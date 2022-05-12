package aiss.model;

public enum Status {
    DRAFT,
    IN_PROGRESS,
    IN_REVISION,
    DONE,
    CANCELLED;

    public static Status parse(String status) {
        if (status.equals("draft")) {
            return DRAFT;
        } else if (status.equals("in_progress") || status.equals("in progress")) {
            return IN_PROGRESS;
        } else if (status.equals("in_revision") || status.equals("in revision")) {
            return IN_REVISION;
        } else if (status.equals("done")) {
            return DONE;
        } else if (status.equals("cancelled")) {
            return CANCELLED;
        } else {
            return null;
        }
    }
}
