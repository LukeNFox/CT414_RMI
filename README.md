###### `To Run Project`

1. Change file location in client.policy and server.policy to match your own file structure

2. Run *javac ./\*/\*.java* from root directory

3. Run *java -Djava.security.policy=server.policy engine.ExamEngine*

4. Run in seperate terminal *java -Djava.security.policy=client.policy client.Client*

If succeeded the first terminal should print out "Trying to login"