create table C##THINKWAVE.USER_TABLE
(
    ID             VARCHAR2(5)   not null
        primary key,
    NAME           VARCHAR2(30)  not null,
    MBL            NUMBER(10)    not null
        constraint CHECK_MOBILE_USER
            check ("MBL" >= 1111111111 AND "MBL" <= 9999999999),
    EMAIL          VARCHAR2(319) not null
        unique,
    ADDRESS        VARCHAR2(300) not null,
    GENDER         CHAR          not null,
    CREATED_ON     DATE          not null,
    EMAIL_VERIFIED CHAR          not null,
    PASSW_HASH     VARCHAR2(100) not null,
    INSTID         VARCHAR2(5)   not null,
    PASSW_SALT     VARCHAR2(30)  not null
);

create index C##THINKWAVE.USER_TABLE_IDX_USER_ID_USER_PASSWORD
    on C##THINKWAVE.USER_TABLE (ID, PASSW);

create table C##THINKWAVE.INSTITUTION_TABLE
(
    INSTITUTION_ADMIN_ID             VARCHAR2(5)   not null
        primary key,
    INSTITUTION_EMAIL_ADDRESS        VARCHAR2(319) not null
        unique,
    INSTITUTION_PASSWORD             VARCHAR2(100) not null,
    INSTITUTION_ADMIN_CREATED_ON     DATE          not null,
    INSTITUTION_ADMIN_EMAIL_VERIFIED CHAR          not null,
    INSTID                           VARCHAR2(5)   not null
);

create table C##THINKWAVE.INSTRUCTOR_TABLE
(
    INSTRUCTOR_ID             VARCHAR2(5)   not null
        primary key,
    INSTRUCTOR_PASSWORD       VARCHAR2(100) not null,
    INSTRUCTOR_NAME           VARCHAR2(30)  not null,
    INSTRUCTOR_MBL            NUMBER(10)    not null
        constraint CHECK_MOBILE_INSTRUCTOR
            check (INSTRUCTOR_MBL between 1111111111 and 9999999999),
    INSTRUCTOR_EMAIL          VARCHAR2(319) not null
        unique,
    INSTRUCTOR_CREATED_ON     DATE          not null,
    INSTRUCTOR_EMAIL_VERIFIED CHAR          not null,
    INSTITUTE_ID              VARCHAR2(5)   not null
        constraint INSTRUCTOR_TABLE_INSTITUTION_TABLE_INSTITUTION_ADMIN_ID_FK
            references C##THINKWAVE.INSTITUTION_TABLE
                on delete cascade
);

create table C##THINKWAVE.EXAM_TABLE
(
    EXAM_ID             NUMBER       not null
        primary key,
    INSTRUCTOR_ID       VARCHAR2(5)  not null
        constraint EXAM_TABLE_INSTRUCTOR_TABLE_INSTRUCTOR_ID_FK
            references C##THINKWAVE.INSTRUCTOR_TABLE,
    EXAM_TITLE          VARCHAR2(50) not null,
    EXAM_DATETIME       DATE         not null,
    EXAM_DURATION       NUMBER       not null,
    TOTAL_QUESTION      NUMBER       not null,
    MARKS_PER_RIGHT_ANS VARCHAR2(2)  not null,
    MARKS_PER_WRONG_ANS VARCHAR2(2)  not null,
    EXAM_CREATED_ON     DATE         not null,
    EXAM_CODE           VARCHAR2(7)  not null
);

create table C##THINKWAVE.QUESTION_TABLE
(
    Q_ID     NUMBER        not null
        primary key,
    QUESTION VARCHAR2(300) not null
        unique,
    EXAM_ID  NUMBER        not null
        constraint QUESTION_TABLE_EXAM_TABLE_EXAM_ID_FK
            references C##THINKWAVE.EXAM_TABLE
);

create table C##THINKWAVE.OPTION_TABLE
(
    OPTION_ID     NUMBER       not null
        primary key,
    QUESTION_ID   NUMBER       not null
        unique
        constraint OPTIONINFO_QUESTIONINFO_Q_ID_FK
            references C##THINKWAVE.QUESTION_TABLE,
    OPTION_NUMBER NUMBER       not null,
    OPTION_TITLE  VARCHAR2(50) not null
);

create table C##THINKWAVE.USER_EXAM_QUESTION_ANSWER
(
    USER_EXAM_QUES_ANS_ID NUMBER      not null
        primary key,
    USER_ID               VARCHAR2(5) not null
        unique
        constraint USER_ID_FK
            references C##THINKWAVE.USER_TABLE,
    EXAM_ID               NUMBER      not null
        unique
        constraint USER_EXAM_QUESTION_ANSWER_EXAM_TABLE_EXAM_ID_FK
            references C##THINKWAVE.EXAM_TABLE,
    QUESTION_ID           NUMBER      not null,
    USER_ANSWER_OPTION    CHAR        not null,
    MARKS                 VARCHAR2(5) not null
);

create table C##THINKWAVE.USER_EXAM_ENROLL_TABLE
(
    USER_EXAM_ENROLL_ID NUMBER      not null
        primary key,
    USER_ID             VARCHAR2(5) not null
        constraint USER_EXAM_ENROLL_TABLE_USER_TABLE_USER_ID_FK
            references C##THINKWAVE.USER_TABLE
                on delete cascade,
    EXAM_ID             NUMBER      not null
        constraint USER_EXAM_ENROLL_TABLE_EXAM_TABLE_EXAM_ID_FK
            references C##THINKWAVE.EXAM_TABLE,
    ENROLL_STATUS       CHAR        not null
);


