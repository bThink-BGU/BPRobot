Appendices
===========

- 	The robot is slower than BPjs, and it may be overflowed with commands and this can cause lost of commands.
	make sure you don't send to many commands together.

- 	When adding a new functionality to CommandHandler in RobotActuator - you might want to test it,
	use the TestBoard object for these tests if needed.
	This will allow you to test methods without connecting the robot.

-   You can debug the program when you replace the code that execute when the build command getting in the RobotActuator(in the build function in the CommandHandler) by 'MockBoards'- in this way the system will not connected to the actual external robots. example to using the 'MockBoards' left as comment as you can see in the code.

- 	When you add new command you can use the nicknames that you define in the build command and subscribe them, as parameters in the json of the command.