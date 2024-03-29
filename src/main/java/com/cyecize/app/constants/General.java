package com.cyecize.app.constants;

import java.time.format.DateTimeFormatter;

public class General {

    public static final String APPLICATION_JSON = "application/json";

    public static final String HIBERNATE_HINT_ENTITY_GRAPH = "javax.persistence.fetchgraph";

    public static final String VALID_EMAIL_PATTERN = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    public static final String VALID_PHONE_PATTERN = "^(\\+?[0-9]{9,12})$|^.{0}$";

    public static final String ROLE_USER = "ROLE_USER";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_GOD = "ROLE_GOD";

    public static final int MAX_FILE_SIZE_BYTES = 15_000_000;

    public static final int MAX_VARCHAR = 255;
    public static final int MAX_NAME = 50;

    public static final int MAX_ALLOWED_TAGS = 5;
    public static final int MIN_PROD_QUANTITY = 0;
    public static final int MAX_PROD_QUANTITY = 50;
    public static final int MAX_ALLOWED_BULK_UPDATE_ITEMS = 100;

    public static final String QUERY_PARAM_LANG = "lang";

    public static final DateTimeFormatter DEFAULT_DATE_TIME_FORMAT = DateTimeFormatter.ofPattern(
            "dd MMM, yyyy - HH:mm"
    );

    public static final int MIN_PASS_LENGTH = 6;
}
