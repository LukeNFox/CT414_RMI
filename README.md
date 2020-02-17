#Java RMI Exam Center

####To Run Project:
- Change file location in client.policy and server.policy to match your own file structure

- Run **_javac ./\*/\*.java_** from root directory

- Run **_rmiregistry 20345_**

- Run in new terminal **_java -Djava.security.policy=server.policy engine.ExamEngine_**

- Run in seperate terminal **_java -Djava.security.policy=client.policy client.Client_**

If succeeded you should be welcomed to the exam center and will be asked to login.

---

####Sample Login Details:
Student Id: 1 

Password: mypass
