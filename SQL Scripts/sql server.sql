create table THINKWAVE.QB_Course
(
    CourseID   int        not null
        primary key,
    CourseCode varchar(7) not null
        unique,
    CourseName varchar(100)
);

create table THINKWAVE.QB_Question
(
    InstID           varchar(5)                                                                    not null,
    QuestionID       varchar(5) default right(datepart(year, getdate()), 2) + right(
                '000' + CONVERT([varchar](3), NEXT VALUE FOR [THINKWAVE].[QuestionIdSequence]), 3) not null
        constraint PK_QUESTION_BANK
            primary key,
    CourseID         int                                                                           not null
        constraint FK_Question_Course
            references THINKWAVE.QB_Course,
    Semester         int                                                                           not null,
    Year             varchar(3)                                                                    not null,
    ExamTitle        varchar(100)                                                                  not null,
    ExamDate         varchar(max)                                                                  not null,
    FileName         varchar(100)                                                                  not null,
    QuestionPaperPDF varbinary(max)                                                                not null
);

create table THINKWAVE.USER_DETAILS
(
    USER_ID   int          not null
        primary key,
    INSTID    varchar(5)   not null,
    NAME      varchar(30)  not null,
    MOBILENUM varchar(10)  not null,
    EMAIL     varchar(319)
        unique,
    ADDRESS   varchar(300) not null,
    GENDER    char(2)      not null
);

create table THINKWAVE.USER_AUTHENTICATION
(
    USER_ID    int          not null
        primary key
        references THINKWAVE.USER_DETAILS,
    LAST_LOGIN varchar(max),
    PASSW_HASH varchar(100) not null,
    PASSW_SALT varchar(30)  not null,
    ROLE       varchar(10),
    INST_ID    varchar(5)   not null
);

CREATE TRIGGER set_role
        ON THINKWAVE.USER_AUTHENTICATION
        AFTER INSERT
        AS
    BEGIN
        UPDATE ua
        SET ua.ROLE =
                CASE
                    WHEN i.USER_ID BETWEEN 1 AND 1000 THEN 'Student'
                    WHEN i.USER_ID BETWEEN 10001 AND 20000 THEN 'Faculty'
                    WHEN i.USER_ID BETWEEN 20001 AND 30000 THEN 'Admin'
                    END
        FROM THINKWAVE.USER_AUTHENTICATION ua
                 JOIN inserted i ON ua.USER_ID = i.USER_ID;
    END;

