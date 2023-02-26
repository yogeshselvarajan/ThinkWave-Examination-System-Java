-- CREATE THINKWAVE
CREATE SCHEMA THINKWAVE;

-- CREATE USER TABLE
CREATE TABLE THINKWAVE.USER_DETAILS (
                                USER_ID INT PRIMARY KEY,
                                INSTID VARCHAR(5) NOT NULL,
                                NAME VARCHAR(30) NOT NULL,
                                MOBILENUM NUMERIC(10) NOT NULL,
                                EMAIL VARCHAR(319) UNIQUE,
                                ADDRESS VARCHAR(300) NOT NULL,
                                GENDER CHAR(2) NOT NULL
);

-- CREATE USER AUTHENTICATION TABLE
CREATE TABLE THINKWAVE.USER_AUTHENTICATION (
                                               USER_ID INT PRIMARY KEY,
                                               LAST_LOGIN VARCHAR(MAX),
                                               PASSW_HASH VARCHAR(100) NOT NULL,
                                               PASSW_SALT VARCHAR(30) NOT NULL,
                                               ROLE VARCHAR(10)
                                                   FOREIGN KEY (USER_ID) REFERENCES THINKWAVE.USER_DETAILS(USER_ID)
);


CREATE TRIGGER set_last_login
    ON THINKWAVE.USER_AUTHENTICATION
    AFTER UPDATE
    AS
BEGIN
    UPDATE THINKWAVE.USER_AUTHENTICATION
    SET LAST_LOGIN = CONVERT(VARCHAR(30), SWITCHOFFSET(SYSDATETIMEOFFSET(), '+05:30'), 120)
    FROM inserted
    WHERE inserted.USER_ID = THINKWAVE.USER_AUTHENTICATION.USER_ID;
END;

    CREATE TRIGGER set_role
        ON THINKWAVE.USER_DETAILS
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



-- Create a sequence for generating question id
CREATE SEQUENCE THINKWAVE.QuestionIdSequence
    START WITH 1
    INCREMENT BY 1;

-- Create question bank course table
CREATE TABLE THINKWAVE.QB_Course (
                                     CourseID INT PRIMARY KEY,
                                     CourseCode VARCHAR(20),
                                     CourseName VARCHAR(100)
);
-- Create question bank table
CREATE TABLE THINKWAVE.QB_Question (
                                       InstID VARCHAR(5) NOT NULL,
                                       QuestionID  VARCHAR(5) NOT NULL DEFAULT RIGHT(DATEPART(YEAR, GETDATE()), 2) +
                                                                               RIGHT('000' + CONVERT(VARCHAR(3), NEXT VALUE FOR [THINKWAVE].QuestionIdSequence), 3),
                                       CourseID INT NOT NULL,
                                       Semester INT NOT NULL,
                                       Year INT NOT NULL,
                                       ExamTitle VARCHAR(100) NOT NULL,
                                       ExamDate DATE NOT NULL,
                                       FileName VARCHAR(100) NOT NULL,
                                       QuestionPaperPDF VARBINARY(MAX) NOT NULL,
                                       CONSTRAINT FK_Question_Course FOREIGN KEY (CourseID) REFERENCES THINKWAVE.QB_Course (CourseID),
                                       CONSTRAINT PK_QUESTION_BANK PRIMARY KEY (QuestionID)
);

