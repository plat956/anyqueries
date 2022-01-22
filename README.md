# ANY-QUERIES
The application is an instance of typical "Question-Answer" system. You know such examples as <b>stackoverflow.com</b> or even <b>otvet.mail.ru</b>. Some people who'd like to ask something make question, others can respond. Users can mark any answer as a correct for thier question, attach different files, put likes/dislikes to answers.

There are 4 roles in the system:
<ul>
  <li><b>Guest</b> - visitors with this access level have only viewing permissions, they can view questions and answers, use search. To become a user, they have opportunities to sign-up or sign-in;</li>
  <li><b>User</b> - visitors with this access level can ask questions, manage them, answer to other people's questions, change account information and so on;</li>
  <li><b>Administrator</b> - visitors with this access level can do everything that <b>User</b> allowed, but the main feature is that they have full access to system data management, such as manage categories, other users, all questions and answers;</li>
  <li><b>Moderator</b> - visitors with this access level in addition to <b>User</b> privileges can manage all questions and answers to help administrators keep clean.</li>
</ul>

It's worth noting that each account in the system can have 3 statuses:
<ul>
  <li><b>Banned</b> - these users have been banned by administrators;</li>
  <li><b>Inactive</b> - this status is assigned to user account after the 1st registration step completed, to become an Active account must be activated by following email activation link or using telegram bot;</li>
  <li><b>Active</b> - this status provide user account access to use all system functions.</li>
</ul>

## Project stack
Jakarta EE / MySQL 8 / HTML 5 / CSS 3 / Javascript / JQuery 1.10 / Bootstrap 4

## Database model
[![db model](https://user-images.githubusercontent.com/62507570/149677602-a609bda2-f914-4480-9272-825e3098a238.png)](#)

## Usage
<ul>
  <li>Download sources from this repository;</li>
  <li>Restore database backup from the <b>dump.sql</b> file located in data;</li>
  <li>Unpack <b>uploads.zip</b> insides located in data to uploads folder on your server and change <b>app.upload.dir</b> value in <b>application.properties</b> file;</li>
  <li>Set your values in <b>database.properties</b>;</li>
  <li>If you wanna test the application together with a telegram bot, whose credentials is located in <b>telegram.properties</b>, you should be sure that no one is running any app instances connected to that bot at the same time with you, otherwise you must create a new bot with help of <a href="https://t.me/botfather">@BotFather</a>, then make changes in configuration file or just disable usage of it changing property <b>telegram.bot.alive</b> to <b>false</b>;</li>
  <li>Build and deploy the application;</li>
  <li>To get logged in use accounts credentials from <b>accounts.txt</b> file located in data.</li>
</ul>

## Deployed demo version of the app
http://34.133.230.193:8080/
