Android Jetpack Workmanager:<br/>
Workmanager is an API that is used to schdeule deferrable and asynchronous tasks that are expectec to run even if app exits  or the  device restarts.Workmanager is considered a good replacement for Android  background  scheduling APIs such as  FirebaseJobDispatcher or Job Scheduler.<br/>
This is the high level  overview what workmanager does under the hood:<br/>
![image](https://user-images.githubusercontent.com/46449085/120057629-6d2d5880-c07f-11eb-985a-2a857cae05e7.png)

************************************************************************************************
Below you can see some of the most useful features of Workmanager:<br/>
1.Work Constraints:Define the optimal conditions for your task to run(Example: run some battery consumable tasks only when your device is connected to a charger).<br/>
2.Robust Scheduling:...<br/>
3.Flexible Retry Policy:...<br/>
4.Work Chaining:...<br/>
5.Built-In Threading Interoperablity:...<br/>
*************************************************************************************************


