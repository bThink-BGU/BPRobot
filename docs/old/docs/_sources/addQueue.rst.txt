Add Queue
==========

If you have additional commands that need to transmit over new channel (from your reasons) you can add new queue.

We show here the way to do that.

The 1-3 steps refer to RobotUtils repository, and related to the definition of the new channel.

The 3-5 steps refer to RobotBProgramRunnerListener class in BPjsProject repository and

MainTest class in RobotActuator repository, and related to the usage in the new channel.

3. Add Queue Name Enum
-----------------------
add enum in QueueNameEnum with the name of your new queue.

This will serve you for a separation between behavior for the different queues in the code.

2. Add Channel
----------------
Simply, add new Channel in CommunicationHandler.

The initialization of the channel will be in the connect function by the ConnectionFactory.

3. Channel Configuration
--------------------------
Add support to your new channel in the following functions in CommunicationHandler:

* connect: connect to RabbitMQ queues.
* purgeQueue: remove all messages from queue
* consumeFromQueue: listen to queue and execute callback when messages from it arrive
* closeConnection: close Send and Receive connection
* send: put message in Send queue

Use the enum you created in step 1.

4. Purge Queue
----------------
Now, after you defined all the behavior of the queue in the different functions this is the time to use them.

In RobotBProgramRunnerListener and MainTest use ICommunication to purge the queue as you define in step 3.

5. Send Messages in the queue
------------------------------
Now everything is prepared to send and receive the messages over the new channel. The implementation in that part is

at your discretion, according to the commands that use in this channel.

You can send the commands over the channel in RobotBProgramRunnerListener and get them in MainTest

and act accordingly. Or alternatively, send the commands over the channel in MainTest and get them

in RobotBProgramRunnerListener.


