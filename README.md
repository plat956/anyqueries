# ANY-QUERIES.BY
The application is an instance of typical "Question-Answer" system. You know such examples as <b>stackoverflow.com</b> or even <b>otvet.mail.ru</b>. Some people who'd like to ask something make question, others can respond. Users can mark any answer as a correct for thier question, attach different files, put likes/dislikes to answers.

There are 4 roles in the system:
<ul>
  <li><b>Guest</b> - visitors with this access level have only viewing permissions, they can view questions and answers, use search. To become a user, they have opportunities to sign-up or sign-in;</li>
  <li><b>User</b> - visitors with this access level can ask questions, manage them, answer to other people's questions, change account information and so on;</li>
  <li><b>Administrator</b> - visitors with this access level can do everything that <b>User</b> allowed, but the main feature is that they have full access to system data management, such as manage categories and other users;</li>
  <li><b>Moderator</b> - visitors with this access level in addition to <b>User</b> privileges can manage all questions and answers to help administrators keep clean.</li>
</ul>

## Project stack
Jakarta EE, MySQL, HTML/CSS/Javascript

## Database model
[![db model](https://user-images.githubusercontent.com/62507570/149012896-a94aa649-69ea-4c56-a93d-07d03b6f98a5.png)](#)

## Usage
<ul>
  <li>Download sources from this repository;</li>
  <li>Restore database backup from the <b>dump.sql</b> file located in root;</li>
  <li>Unpack <b>uploads.zip</b> insides located in root to uploads folder on your server and change <b>app.upload.dir</b> value in <b>application.properties</b> file;</li>
  <li>Set your values in <b>database.properties</b>;</li>
  <li>If you wanna test the application together with a telegram bot, please create this one with help of <a href="https://t.me/botfather">@BotFather</a>, then make changes in <b>telegram.properties</b>. Unfortunately, I can't provide access to my created bot and share its credentials because it can be executing on my server at the same time with your project running. If you don't want to create and use bot, please switch <b>telegram.bot.alive</b> value in <b>telegram.properties</b> to <b>false</b>;</li>
  <li>Build and deploy the application.</li>
</ul>

## Demo
<a href="">link will be here</a>
