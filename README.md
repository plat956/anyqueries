# ANY-QUERIES.BY

### Project stack
Jakarta EE, MySQL, HTML/CSS/Javascript

### Description
There are 4 roles in the system:
<ul>
  <li><b>Guest</b> - visitors with this access level have only viewing permissions, they can view questions and answers, use search. To become a user, they have opportunities to sign-up or sign-in;</li>
  <li><b>User</b> - visitors with this access level can ask questions, manage them, answer to other people's questions, change account information and so on;</li>
  <li><b>Administrator</b> - visitors with this access level can do everything that <b>User</b> allowed, but the main feature is that they have full access to system data management, such as manage categories and other users;</li>
  <li><b>Moderator</b> - visitors with this access level in addition to <b>User</b> privileges can manage all questions and answers to help administrators keep clean.</li>
</ul>

## ATTENTION
If you wanna test the application together with a telegram bot, please create this one with help of <a href="https://t.me/botfather" target="_blank">@BotFather</a>, then make changes in telegram.properties. Unfortunately, I can't provide access to my created bot and share its credentials because it can be executing on the server at the same time with your project running.
