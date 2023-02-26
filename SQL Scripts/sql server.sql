-- CREATE THINKWAVE
CREATE SCHEMA THINKWAVE;

CREATE TABLE THINKWAVE.QB_Course (
                                     CourseID INT NOT NULL PRIMARY KEY,
                                     CourseCode VARCHAR(7) NOT NULL UNIQUE,
                                     CourseName VARCHAR(100)
);

INSERT INTO THINKWAVE.QB_Course (CourseID, CourseCode, CourseName)
VALUES (1, '19HS101', 'Technical English'),
       (2, '19MA101', 'Mathematics - I'),
       (3, '19PH101', 'Engineering Physics - I'),
       (4, '19CY101', 'Engineering Chemistry - I'),
       (5, '19GE101', 'Engineering Graphics'),
       (6, '19HS201', 'Communication Skills'),
       (7, '19MA201', 'Mathematics - II'),
       (8, '19PH201', 'Engineering Physics - II'),
       (9, '19CY201', 'Engineering Chemistry - II'),
       (10, '19CS201', 'Computer Programming');

-- Create a sequence for generating question id
CREATE SEQUENCE THINKWAVE.QuestionIdSequence
    START WITH 1
    INCREMENT BY 1;

CREATE TABLE THINKWAVE.QB_Question (
                                       InstID VARCHAR(5) NOT NULL,
                                       QuestionID VARCHAR(5) DEFAULT RIGHT(DATEPART(YEAR, GETDATE()), 2) + RIGHT('000' + CONVERT(VARCHAR(3), NEXT VALUE FOR [THINKWAVE].[QuestionIdSequence]), 3) NOT NULL CONSTRAINT PK_QUESTION_BANK PRIMARY KEY,
                                       CourseID INT NOT NULL CONSTRAINT FK_Question_Course REFERENCES THINKWAVE.QB_Course,
                                       Semester INT NOT NULL,
                                       Year VARCHAR(3) NOT NULL,
                                       ExamTitle VARCHAR(100) NOT NULL,
                                       ExamDate VARCHAR(MAX) NOT NULL,
                                       FileName VARCHAR(100) NOT NULL,
                                       QuestionPaperPDF VARBINARY(MAX) NOT NULL
);

CREATE TABLE THINKWAVE.USER_DETAILS (
                                        USER_ID INT NOT NULL PRIMARY KEY,
                                        INSTID VARCHAR(5) NOT NULL,
                                        NAME VARCHAR(30) NOT NULL,
                                        MOBILENUM NUMERIC(10) NOT NULL,
                                        EMAIL VARCHAR(319) UNIQUE,
                                        ADDRESS VARCHAR(300) NOT NULL,
                                        GENDER CHAR(2) NOT NULL
);

CREATE TABLE THINKWAVE.USER_AUTHENTICATION (
                                               USER_ID INT NOT NULL PRIMARY KEY REFERENCES THINKWAVE.USER_DETAILS,
                                               LAST_LOGIN VARCHAR(MAX),
                                               PASSW_HASH VARCHAR(100) NOT NULL,
                                               PASSW_SALT VARCHAR(30) NOT NULL,
                                               ROLE VARCHAR(10)
);

CREATE TRIGGER set_role ON THINKWAVE.USER_AUTHENTICATION AFTER INSERT AS
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