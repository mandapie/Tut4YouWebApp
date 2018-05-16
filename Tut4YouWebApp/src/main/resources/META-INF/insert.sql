INSERT INTO Groups VALUES ('tut4youapp.student','authenticated user only')
INSERT INTO Groups VALUES ('tut4youapp.tutor','tutor of selected courses')
INSERT INTO Groups VALUES ('tut4youapp.moderator','polices tutors and students')

INSERT INTO Subject(subjectName) VALUES ('Arts')
INSERT INTO Subject(subjectName) VALUES ('Math')
INSERT INTO Subject(subjectName) VALUES ('Biology')
INSERT INTO Subject(subjectName) VALUES ('Chemistry')
INSERT INTO Subject(subjectName) VALUES ('Computer Science')

INSERT INTO Course(subjectName,courseName) VALUES ('Arts','Artists in Their Own Words')
INSERT INTO Course(subjectName,courseName) VALUES ('Arts','Introduction to the Visual Arts')
INSERT INTO Course(subjectName,courseName) VALUES ('Math','Mathematical Ideas')
INSERT INTO Course(subjectName,courseName) VALUES ('Math','Algebra')
INSERT INTO Course(subjectName,courseName) VALUES ('Biology','Introduction to Human Disease')
INSERT INTO Course(subjectName,courseName) VALUES ('Biology','Introduction to Marine Biology')
INSERT INTO Course(subjectName,courseName) VALUES ('Biology','General Biology I')
INSERT INTO Course(subjectName,courseName) VALUES ('Biology','General Biology II')
INSERT INTO Course(subjectName,courseName) VALUES ('Chemistry','General Chemistry I')
INSERT INTO Course(subjectName,courseName) VALUES ('Computer Science','Computer Security')
INSERT INTO Course(subjectName,courseName) VALUES ('Computer Science','Introduction to Software Engineering')
INSERT INTO Course(subjectName,courseName) VALUES ('Computer Science','Computer Logic Design I')
INSERT INTO Course(subjectName,courseName) VALUES ('Computer Science','Computer Logic Design II')

INSERT INTO Question(id,description,title,student_email,courseName) VALUES (1,'Solve the following using the quadratic formula: x^2 + 6x = 14','Need help understanding how to use the quadratic formula','bobby@gmail.com','Algebra')


INSERT INTO Users(email,user_type,firstName,lastName,phoneNumber,username,university,dateJoinedAsTutor,hourlyRate,doNotDisturb,defaultZip,securityQuestion,securityAnswer,password,overallRating) VALUES ('amanda@gmail.com','Tutor','Amanda','Pan','7141234567','apan','CSULB','2018-05-09',15.00,0,'92704','What was your childhood nickname?','amanda','CD639F5ECF3514CCC33B9985B55A118727A21782C04A714AB6A7634BE91DC4B6E8BFF6456B546E382EF00057550952F019993BBE6B8D84FE09C2B961149BFA2A',2)
INSERT INTO Users(email,user_type,firstName,lastName,phoneNumber,username,university,dateJoinedAsTutor,hourlyRate,doNotDisturb,defaultZip,securityQuestion,securityAnswer,password) VALUES ('keith@gmail.com','Tutor','Keith','Tran','7149876543','kTran','CSULB','2017-09-01',12.00,0,'92703','What was your childhood nickname?','keith','0C0BAAA4ECC4C21265E4E41B966C54B806315CDDFF946F6C8D86C352DD8E6F6B8D158F97C6DC310A82202FFC3785996A631E66D02E0CC8FC52623B74A0D646E0')
INSERT INTO Users(email,user_type,firstName,lastName,phoneNumber,username,university,dateJoinedAsTutor,hourlyRate,doNotDisturb,defaultZip,securityQuestion,securityAnswer,password) VALUES ('syed@gmail.com','Tutor','Syed','Haider','5621123400','sHaider','CSULB','2017-09-01',12.50,0,'90801','What was your childhood nickname?','syed','E82C6184E06670CE275027FE9177D95C85E81AA04160FA6095924F514FEE43C3150F14D65EDC4D4ECD105D5E69C9A5B3A5CC7ECCA10C5EE9B80A25863FEA1E08')
INSERT INTO Users(email,user_type,firstName,lastName,phoneNumber,username,university,dateJoinedAsTutor,hourlyRate,doNotDisturb,defaultZip,securityQuestion,securityAnswer,password) VALUES ('andrew@gmail.com','Tutor','Andrew','Kaichi','7143337789','aKaichi','CSULB','2017-09-01',13.00,0,'90804','What was your childhood nickname?','andrew','6E99F65920D0B34E678A1FFBA8AA834568A1EF9EFB4D94AD0FB34B42FF2C49C39C228BF5FA619A647831BC0745C49D9FF2171A1513B8591B37FA83B6FC9E2827')
INSERT INTO Users(email,user_type,firstName,lastName,phoneNumber,username,university,dateJoinedAsTutor,hourlyRate,doNotDisturb,defaultZip,securityQuestion,securityAnswer,password) VALUES ('brenda@gmail.com','Tutor','Brenda','Hoffler','9495673890','bHoff','UCI','2018-01-01',15.50,1,'91206','In what city or town did your parents meet?','long beach','2BF2B4C1A129E7E193446EFA878EC1413E16EE01C55336C628CD1FE8BCB9DCD77425B439413684DCBA91E862E0A1FC57A8DEE54DA96E4642132CBC8468E5D7C4')
INSERT INTO Users(email,user_type,firstName,lastName,phoneNumber,username,university,dateJoinedAsTutor,hourlyRate,doNotDisturb,defaultZip,securityQuestion,securityAnswer,password) VALUES ('carmen@gmail.com','Tutor','Carmen','Dangos','5622574254','cDang','UCR','2018-02-01',12.00,0,'90721','In what city or town did your parents meet?','long beach','290295170CF0721418A3FD7C77081CF3D5312DC9D5AD7D2A7C123E177BB1CF051B4D20491EC3CA6202E42088B556E6314D924FFB41C1D886AD62815EE1E5723C')
INSERT INTO Users(email,user_type,firstName,lastName,phoneNumber,username,university,dateJoinedAsTutor,hourlyRate,doNotDisturb,defaultZip,securityQuestion,securityAnswer,password) VALUES ('william@gmail.com','Tutor','William','Hanie','7145636659','wHanie','UCLA','2018-03-01',14.00,1,'90001','What was the last name of your third grade teacher?','monge','8D92CD360EC9CBDA00DBEC6E0DE42A9A2A11AEF9975E495700088C1126721F9D8F06C27E5EE6F5874A548B9230C971820F0DD492E3BFE6362916F2093848FF11')
INSERT INTO Users(email,user_type,firstName,lastName,phoneNumber,username,university,dateJoinedAsTutor,hourlyRate,doNotDisturb,defaultZip,securityQuestion,securityAnswer,password) VALUES ('david@gmail.com','Tutor','David','Spencer','5626548965','dSpencer','CSUF','2018-01-15',15.00,0,'90720','What was your childhood nickname?','david','A917D01789B58DFD3A702C715496269886F5D363D7445F42EE7B963E9DE2A1DA7DFBF0B88248CA648E69927353C0A76AACCD1D9B2EF1E32A7FE18CA3710F8929')
INSERT INTO Users(email,user_type,firstName,lastName,phoneNumber,username,university,securityQuestion,securityAnswer,password) VALUES ('emma@gmail.com','Student','Emma','Danvers','7144522257','eDanvers','CSULB','What was your childhood nickname?','emma','FA1F5406758E1B5BEF4BB775D8DA544D4CD4CEEAB66C4B7522BEC42632E3DB1AB13C5E91415E68A9569C53FA64D7678BEEE7075050BBBA30F2579F9572909922')
INSERT INTO Users(email,user_type,firstName,lastName,phoneNumber,username,university,securityQuestion,securityAnswer,password) VALUES ('grace@gmail.com','Student','Grace','Nguyen','7149856125','gNguyen','CSULB','What was the last name of your third grade teacher?','monge','FDC5DB249104F8E05FA6A4CF985F91CBF7CBBA29CB5956B414D829796D3B96C2F920BCF3648D3A75B7706AB3456B2C9C5A142ED384378066C49B09C9ADEA9026')
INSERT INTO Users(email,user_type,firstName,lastName,phoneNumber,username,university,securityQuestion,securityAnswer,password) VALUES ('lily@gmail.com','Student','Lily','Sanders','5623357412','lSanders','UCI','What was the last name of your third grade teacher?','monge','80D8F8A4C006563DE0BDB695205B4D27CFE6BC16E8D42A46F6E4997BC1CA3951A3F76478E711143E6A60057CD8931CA7D74283271AB5C2D0594FBD36C7008937')
INSERT INTO Users(email,user_type,firstName,lastName,phoneNumber,username,university,securityQuestion,securityAnswer,password) VALUES ('daniel@gmail.com','Student','Daniel','Louie','7146585255','dLouie','UCI','In what city or town did your parents meet?','long beach','F68A41E098CF7ECB8924645BFE335941BEB068E7BEBAF0BBA26549C0693560DF08CACE69DEBBC59F6D2E30A328570DC331C1EC2F998A43CD0340B08065D4318A')
INSERT INTO Users(email,user_type,firstName,lastName,phoneNumber,username,university,securityQuestion,securityAnswer,password) VALUES ('evan@gmail.com','Student','Evan','Chen','9495633222','eChen','UCSB','What was your childhood nickname?','evan','B30314C311F4ADF562B16BD400113EA39C6E08934600E2C5707195B5CCE66740402C18CB3F1A534C7C576C1F1E2F8A4914EBCD7894C99D23AA5A03C2F69EBA59')
INSERT INTO Users(email,user_type,firstName,lastName,phoneNumber,username,university,securityQuestion,securityAnswer,password) VALUES ('john@gmail.com','Student','John','Smith','9496662541','jSmith','UCLA','In what city or town did your parents meet?','long beach','B7FCC6E612145267D2FFEA04BE754A34128C1ED8133A09BFBBABD6AFE6327688AA71D47343DD36E719F35F30FA79AEC540E91B81C214FDDFE0BEDD53370DF46D')
INSERT INTO Users(email,user_type,firstName,lastName,phoneNumber,username,university,securityQuestion,securityAnswer,password) VALUES ('caleb@gmail.com','Student','Caleb','Young','7143341556','cYoung','CSUF','What was your childhood nickname?','caleb','B7F05F9993451875B68DE9DD0D190B43AD2B01C8392A8FC7672F9BBD98CC24739059755983A1519E06E76AD95928484526FE4B83E90287D03BE9023D6BEE047A')
INSERT INTO Users(email,user_type,firstName,lastName,password,phoneNumber,username,university,securityQuestion,securityAnswer) VALUES ('samtesting1@gmail.com','Student','Sam','Chen','B30314C311F4ADF562B16BD400113EA39C6E08934600E2C5707195B5CCE66740402C18CB3F1A534C7C576C1F1E2F8A4914EBCD7894C99D23AA5A03C2F69EBA59','6266579513','sChen','USC','What was your childhood nickname?','sam')
INSERT INTO Users(email,user_type,firstName,lastName,password,phoneNumber,username,university,hourlyRate,defaultZip,ZipCode_ID,securityQuestion,securityAnswer) VALUES ('briantesting1@gmail.com','Tutor','Brian','Chen','B30314C311F4ADF562B16BD400113EA39C6E08934600E2C5707195B5CCE66740402C18CB3F1A534C7C576C1F1E2F8A4914EBCD7894C99D23AA5A03C2F69EBA59','7146579513','bChen','CSULB',15.00,'92704',1,'What was your childhood nickname?','brian')

INSERT INTO Groups_users(email,groupName) VALUES ('amanda@gmail.com','tut4youapp.moderator')
INSERT INTO Groups_users(email,groupName) VALUES ('andrew@gmail.com','tut4youapp.moderator')
INSERT INTO Groups_users(email,groupName) VALUES ('keith@gmail.com','tut4youapp.moderator')
INSERT INTO Groups_users(email,groupName) VALUES ('syed@gmail.com','tut4youapp.moderator')

INSERT INTO Groups_users(email,groupName) VALUES ('amanda@gmail.com','tut4youapp.tutor')
INSERT INTO Groups_users(email,groupName) VALUES ('andrew@gmail.com','tut4youapp.tutor')
INSERT INTO Groups_users(email,groupName) VALUES ('keith@gmail.com','tut4youapp.tutor')
INSERT INTO Groups_users(email,groupName) VALUES ('syed@gmail.com','tut4youapp.tutor')
INSERT INTO Groups_users(email,groupName) VALUES ('brenda@gmail.com','tut4youapp.tutor')
INSERT INTO Groups_users(email,groupName) VALUES ('carmen@gmail.com','tut4youapp.tutor')
INSERT INTO Groups_users(email,groupName) VALUES ('william@gmail.com','tut4youapp.tutor')
INSERT INTO Groups_users(email,groupName) VALUES ('david@gmail.com','tut4youapp.tutor')
INSERT INTO Groups_users(email,groupName) VALUES ('briantesting1@gmail.com','tut4youapp.tutor')


INSERT INTO Groups_users(email,groupName) VALUES ('amanda@gmail.com','tut4youapp.student')
INSERT INTO Groups_users(email,groupName) VALUES ('andrew@gmail.com','tut4youapp.student')
INSERT INTO Groups_users(email,groupName) VALUES ('keith@gmail.com','tut4youapp.student')
INSERT INTO Groups_users(email,groupName) VALUES ('syed@gmail.com','tut4youapp.student')
INSERT INTO Groups_users(email,groupName) VALUES ('brenda@gmail.com','tut4youapp.student')
INSERT INTO Groups_users(email,groupName) VALUES ('carmen@gmail.com','tut4youapp.student')
INSERT INTO Groups_users(email,groupName) VALUES ('william@gmail.com','tut4youapp.student')
INSERT INTO Groups_users(email,groupName) VALUES ('david@gmail.com','tut4youapp.student')
INSERT INTO Groups_users(email,groupName) VALUES ('emma@gmail.com','tut4youapp.student')
INSERT INTO Groups_users(email,groupName) VALUES ('grace@gmail.com','tut4youapp.student')
INSERT INTO Groups_users(email,groupName) VALUES ('lily@gmail.com','tut4youapp.student')
INSERT INTO Groups_users(email,groupName) VALUES ('daniel@gmail.com','tut4youapp.student')
INSERT INTO Groups_users(email,groupName) VALUES ('evan@gmail.com','tut4youapp.student')

INSERT INTO Groups_users(email,groupName) VALUES ('john@gmail.com','tut4youapp.student')
INSERT INTO Groups_users(email,groupName) VALUES ('caleb@gmail.com','tut4youapp.student')
INSERT INTO Groups_users(email,groupName) VALUES ('samtesting1@gmail.com','tut4youapp.student')
INSERT INTO Groups_users(email,groupName) VALUES ('briantesting1@gmail.com','tut4youapp.student')

INSERT INTO Courses_tutors(email,courseName) VALUES ('amanda@gmail.com','General Biology I')
INSERT INTO Courses_tutors(email,courseName) VALUES ('briantesting1@gmail.com','General Biology I')

INSERT INTO Courses_tutors(email,courseName) VALUES ('amanda@gmail.com','General Biology II')
INSERT INTO Courses_tutors(email,courseName) VALUES ('amanda@gmail.com','Introduction to Human Disease')
INSERT INTO Courses_tutors(email,courseName) VALUES ('andrew@gmail.com','General Biology I')
INSERT INTO Courses_tutors(email,courseName) VALUES ('andrew@gmail.com','Introduction to Marine Biology')
INSERT INTO Courses_tutors(email,courseName) VALUES ('andrew@gmail.com','Mathematical Ideas')
INSERT INTO Courses_tutors(email,courseName) VALUES ('keith@gmail.com','Mathematical Ideas')
INSERT INTO Courses_tutors(email,courseName) VALUES ('keith@gmail.com','Artists in Their Own Words')
INSERT INTO Courses_tutors(email,courseName) VALUES ('syed@gmail.com','Mathematical Ideas')
INSERT INTO Courses_tutors(email,courseName) VALUES ('syed@gmail.com','Introduction to the Visual Arts')
INSERT INTO Courses_tutors(email,courseName) VALUES ('brenda@gmail.com','Introduction to the Visual Arts')
INSERT INTO Courses_tutors(email,courseName) VALUES ('amanda@gmail.com','Computer Security')
INSERT INTO Courses_tutors(email,courseName) VALUES ('andrew@gmail.com','Computer Security')
INSERT INTO Courses_tutors(email,courseName) VALUES ('keith@gmail.com','Computer Security')
INSERT INTO Courses_tutors(email,courseName) VALUES ('syed@gmail.com','Computer Security')

INSERT INTO Request(student_email,tutor_email,course_courseName,description,status) VALUES ('evan@gmail.com','amanda@gmail.com','General Biology I','Photosynthesis process',1)
INSERT INTO Request(student_email,tutor_email,course_courseName,description,status,dayOfWeek,lengthOfSession) VALUES ('evan@gmail.com','amanda@gmail.com','General Biology I','Photosynthesis process',1,'Monday',2)
INSERT INTO Request(student_email,tutor_email,course_courseName,description,status,dayOfWeek,lengthOfSession) VALUES ('briantesting1@gmail.com','amanda@gmail.com','Biology','Photosynthesis process',1,'Tuesday',2)
INSERT INTO Request(student_email,tutor_email,course_courseName,description,status,dayOfWeek,lengthOfSession) VALUES ('daniel@gmail.com','amanda@gmail.com','Chemistry','Photosynthesis process',1,'Thursday',2)
INSERT INTO Request(student_email,tutor_email,course_courseName,description,status) VALUES ('samtesting1@gmail.com','briantesting1@gmail.com','General Biology I','Photosynthesis process',4)
INSERT INTO Request(student_email,tutor_email,course_courseName,description,status) VALUES ('samtesting1@gmail.com','briantesting1@gmail.com','General Biology II','Photosynthesis process',1)
INSERT INTO Request(student_email,tutor_email,course_courseName,description,status) VALUES ('evan@gmail.com','amanda@gmail.com','General Biology II','Photosynthesis process',1)
INSERT INTO Request(student_email,tutor_email,course_courseName,description,status) VALUES ('daniel@gmail.com','amanda@gmail.com','General Biology II','Photosynthesis process',1)
INSERT INTO Request(student_email,tutor_email,course_courseName,description,status) VALUES ('amanda@gmail.com','andrew@gmail.com','General Biology II','Photosynthesis process',1)
INSERT INTO Request(student_email,course_courseName,description,status) VALUES ('amanda@gmail.com','General Biology I','Photosynthesis process',0)
INSERT INTO Request(student_email,course_courseName,description,status) VALUES ('amanda@gmail.com','General Biology I','Kreb cycle',0)
INSERT INTO Request(student_email,course_courseName,description,status) VALUES ('keith@gmail.com','General Biology I','Kreb cycle',0)
INSERT INTO Request(student_email,course_courseName,description,status) VALUES ('daniel@gmail.com','General Biology I','Kreb cycle',0)
INSERT INTO Request(student_email,tutor_email,course_courseName,description,status) VALUES ('daniel@gmail.com','andrew@gmail.com','Computer Security','Hacking',1)
INSERT INTO Request(student_email,tutor_email,course_courseName,description,status) VALUES ('evan@gmail.com','amanda@gmail.com','General Biology I','Photosynthesis process',1)
INSERT INTO Request(student_email,tutor_email,course_courseName,description,status) VALUES ('daniel@gmail.com','amanda@gmail.com','General Biology I','Photosynthesis process',1)

INSERT INTO Request(student_email,tutor_email,course_courseName,description,status) VALUES ('evan@gmail.com','andrew@gmail.com','General Biology I','Photosynthesis process',4)
INSERT INTO Request(student_email,tutor_email,course_courseName,description,status) VALUES ('evan@gmail.com','keith@gmail.com','General Biology I','Photosynthesis process',4)
INSERT INTO Request(student_email,tutor_email,course_courseName,description,status) VALUES ('amanda@gmail.com','keith@gmail.com','General Biology I','Photosynthesis process',4)
INSERT INTO Request(student_email,tutor_email,course_courseName,description,status) VALUES ('keith@gmail.com','amanda@gmail.com','General Biology I','Photosynthesis process',4)
INSERT INTO Availability(DAYOFWEEK, ENDTIME, STARTTIME, TUTOR_EMAIL) VALUES ('Monday', '03:59:00', '02:00:00', 'amanda@gmail.com')
INSERT INTO Availability(DAYOFWEEK, ENDTIME, STARTTIME, TUTOR_EMAIL) VALUES ('Monday', '04:59:00', '04:00:00', 'amanda@gmail.com')
INSERT INTO Availability(DAYOFWEEK, ENDTIME, STARTTIME, TUTOR_EMAIL) VALUES ('Monday', '06:59:00', '05:00:00', 'amanda@gmail.com')
INSERT INTO Availability(DAYOFWEEK, ENDTIME, STARTTIME, TUTOR_EMAIL) VALUES ('Tuesday', '04:59:00', '04:00:00', 'amanda@gmail.com')
INSERT INTO Availability(DAYOFWEEK, ENDTIME, STARTTIME, TUTOR_EMAIL) VALUES ('Tuesday', '06:59:00', '05:00:00', 'amanda@gmail.com')
INSERT INTO Availability(DAYOFWEEK, ENDTIME, STARTTIME, TUTOR_EMAIL) VALUES ('Thursday', '23:59:00', '12:00:00', 'amanda@gmail.com')

INSERT INTO Availability(DAYOFWEEK, ENDTIME, STARTTIME, TUTOR_EMAIL) VALUES ('Wednesday', '23:59:00', '00:00:00', 'amanda@gmail.com')
INSERT INTO Availability(DAYOFWEEK, ENDTIME, STARTTIME, TUTOR_EMAIL) VALUES ('Wednesday', '23:00:00', '00:00:00', 'andrew@gmail.com')
INSERT INTO Availability(DAYOFWEEK, ENDTIME, STARTTIME, TUTOR_EMAIL) VALUES ('Wednesday', '11:00:00', '02:00:00', 'andrew@gmail.com')
INSERT INTO Availability(DAYOFWEEK, ENDTIME, STARTTIME, TUTOR_EMAIL) VALUES ('Friday', '23:00:00', '02:00:00', 'amanda@gmail.com')
INSERT INTO Availability(DAYOFWEEK, ENDTIME, STARTTIME, TUTOR_EMAIL) VALUES ('Saturday', '23:00:00', '02:00:00', 'amanda@gmail.com')
INSERT INTO Availability(DAYOFWEEK, ENDTIME, STARTTIME, TUTOR_EMAIL) VALUES ('Sunday', '23:00:00', '02:00:00', 'amanda@gmail.com')

INSERT INTO Availability(DAYOFWEEK, ENDTIME, STARTTIME, TUTOR_EMAIL) VALUES ('Monday', '01:00:00', '23:59:00', 'andrew@gmail.com')
INSERT INTO Availability(DAYOFWEEK, ENDTIME, STARTTIME, TUTOR_EMAIL) VALUES ('Sunday', '23:00:00', '02:00:00', 'keith@gmail.com')
INSERT INTO Availability(DAYOFWEEK, ENDTIME, STARTTIME, TUTOR_EMAIL) VALUES ('Sunday', '23:00:00', '02:00:00', 'syed@gmail.com')

INSERT INTO Requests_tutors(id,email) VALUES (1,'amanda@gmail.com')
INSERT INTO Requests_tutors(id,email) VALUES (2,'amanda@gmail.com')
INSERT INTO Requests_tutors(id,email) VALUES (3,'andrew@gmail.com')

INSERT INTO Rating(student_email,description,ratingValue,tutor_email) VALUES ('daniel@gmail.com','He was a good tutor.', 1,'andrew@gmail.com')
INSERT INTO Rating(student_email,description,ratingValue,tutor_email) VALUES ('evan@gmail.com','I liked him.',1,'andrew@gmail.com')
INSERT INTO Rating(student_email,description,ratingValue,tutor_email) VALUES ('daniel@gmail.com','She did well.',5,'amanda@gmail.com')
INSERT INTO Rating(student_email,description,ratingValue,tutor_email) VALUES ('evan@gmail.com','She did well.',5,'amanda@gmail.com')
INSERT INTO Rating(student_email,description,ratingValue,tutor_email) VALUES ('keith@gmail.com','She did well.',5,'amanda@gmail.com')
INSERT INTO Rating(student_email,description,ratingValue,tutor_email) VALUES ('keith@gmail.com','She was efficient.',5,'amanda@gmail.com')
INSERT INTO Rating(student_email,description,ratingValue,tutor_email) VALUES ('evan@gmail.com','She did not quite understand my question.',4,'amanda@gmail.com')
INSERT INTO Rating(student_email,description,ratingValue,tutor_email) VALUES ('syed@gmail.com','She did okay.',3,'amanda@gmail.com')
INSERT INTO Rating(student_email,description,ratingValue,tutor_email) VALUES ('evan@gmail.com','She did okay.',3,'amanda@gmail.com')
INSERT INTO Rating(student_email,description,ratingValue,tutor_email) VALUES ('evan@gmail.com','Her explanation was meh.',2,'amanda@gmail.com')
INSERT INTO Rating(student_email,description,ratingValue,tutor_email) VALUES ('evan@gmail.com','She did well.',4,'amanda@gmail.com')
INSERT INTO Rating(student_email,description,ratingValue,tutor_email) VALUES ('evan@gmail.com','She is terrible at explaining.',1,'amanda@gmail.com')
INSERT INTO Rating(student_email,description,ratingValue,tutor_email) VALUES ('evan@gmail.com','She did well.',5,'amanda@gmail.com')
INSERT INTO Rating(student_email,description,ratingValue,tutor_email) VALUES ('evan@gmail.com','She did bad.',2,'amanda@gmail.com')
INSERT INTO Rating(student_email,description,ratingValue,tutor_email) VALUES ('evan@gmail.com','She did okay.',3,'amanda@gmail.com')

INSERT INTO ModeratorApplication(applicationStatus,reason,resumeFilePath,user_email) VALUES (0,'to help people','fsdafdsafsfasdf','carmen@gmail.com')
INSERT INTO Availability(DAYOFWEEK, ENDTIME, STARTTIME, TUTOR_EMAIL) VALUES ('Monday', '06:59:00', '05:00:00', 'carmen@gmail.com')
INSERT INTO Courses_tutors(email,courseName) VALUES ('carmen@gmail.com','Computer Security')
--INSERT INTO Rating(student_email,description,ratingValue,tutor_email) VALUES ('daniel@gmail.com','He was a good tutor.', 1,'carmen@gmail.com')

INSERT INTO Complaint(title,details,user_email,reporteduser_email) VALUES ('Tutor did not answer my question', 'Tutor failed to answer my questions and was not prepared','evan@gmail.com','carmen@gmail.com')
INSERT INTO Complaint(title,details,user_email,reporteduser_email) VALUES ('FAIL', 'Tutor failed to answer my questions and was not prepared','keith@gmail.com','carmen@gmail.com')