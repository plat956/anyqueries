/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80027
 Source Host           : localhost:3306
 Source Schema         : any_queries

 Target Server Type    : MySQL
 Target Server Version : 80027
 File Encoding         : 65001

 Date: 19/01/2022 01:15:56
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for answer_attachment
-- ----------------------------
DROP TABLE IF EXISTS `answer_attachment`;
CREATE TABLE `answer_attachment`  (
  `answer_id` int NOT NULL,
  `attachment_id` int NOT NULL,
  PRIMARY KEY (`answer_id`, `attachment_id`) USING BTREE,
  INDEX `fk_comment_has_attachment_attachment1_idx`(`attachment_id`) USING BTREE,
  INDEX `fk_comment_has_attachment_comment1_idx`(`answer_id`) USING BTREE,
  CONSTRAINT `fk_comment_has_attachment_attachment1` FOREIGN KEY (`attachment_id`) REFERENCES `attachments` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `fk_comment_has_attachment_comment1` FOREIGN KEY (`answer_id`) REFERENCES `answers` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of answer_attachment
-- ----------------------------
INSERT INTO `answer_attachment` VALUES (6, 1);
INSERT INTO `answer_attachment` VALUES (8, 2);
INSERT INTO `answer_attachment` VALUES (14, 6);
INSERT INTO `answer_attachment` VALUES (14, 7);
INSERT INTO `answer_attachment` VALUES (14, 8);

-- ----------------------------
-- Table structure for answers
-- ----------------------------
DROP TABLE IF EXISTS `answers`;
CREATE TABLE `answers`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `text` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `creation_date` datetime NOT NULL,
  `editing_date` datetime NULL DEFAULT NULL,
  `solution` tinyint NULL DEFAULT NULL,
  `question_id` int NOT NULL,
  `author_id` int NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `id_UNIQUE`(`id`) USING BTREE,
  INDEX `fk_comment_question1_idx`(`question_id`) USING BTREE,
  INDEX `fk_comment_user1_idx`(`author_id`) USING BTREE,
  CONSTRAINT `fk_comment_question1` FOREIGN KEY (`question_id`) REFERENCES `questions` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `fk_comment_user1` FOREIGN KEY (`author_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 467 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of answers
-- ----------------------------
INSERT INTO `answers` VALUES (1, '<b><i>Меньшов П. О.,</i></b><span>&nbsp;Do you want to generate a resized version of the original png, or just paint a resized version somewhere in your UI?</span>', '2022-01-18 23:50:02', '2022-01-18 23:50:45', 0, 1, 7);
INSERT INTO `answers` VALUES (2, '<b><i>Volski V. I.,</i></b><span>&nbsp;both options suit me :)</span>', '2022-01-18 23:53:58', NULL, 0, 1, 6);
INSERT INTO `answers` VALUES (3, 'If you have an<i><u> java.awt.Image</u></i>, resizing it doesn\'t require any additional libraries. Just do:<b> Image newImage = yourImage.getScaledInstance(newWidth, newHeight, Image.SCALE_DEFAULT);</b>', '2022-01-18 23:54:53', '2022-01-18 23:57:24', 1, 1, 7);
INSERT INTO `answers` VALUES (4, '<p>Did it help you?</p>', '2022-01-18 23:58:48', NULL, 0, 1, 7);
INSERT INTO `answers` VALUES (5, '<b><i>Volski V. I.,</i></b><span>&nbsp;yes, thank you very much. it works great!!!</span>', '2022-01-18 23:59:26', NULL, 0, 1, 6);
INSERT INTO `answers` VALUES (6, '<p>Здравствуйте! Все зависит от курса, я учусь на 1 курсе в БГУИР, прикреплю список тем для наших КП:</p>', '2022-01-19 00:11:00', NULL, 0, 2, 5);
INSERT INTO `answers` VALUES (7, '<b><i>Кипелова А. В.,</i></b><span>&nbsp;благодарю, но мне бы что-то из последних курсов обучения. Может у кого-то есть?</span>', '2022-01-19 00:12:50', NULL, 0, 2, 7);
INSERT INTO `answers` VALUES (8, '<b><i>Volski V. I.,</i></b><span>&nbsp;остался список дипломных, закончил ВУЗ в прошлом году, надеюсь Вам это подойдет:</span>', '2022-01-19 00:15:30', NULL, 1, 2, 6);
INSERT INTO `answers` VALUES (9, '<p>Я вечером заливаю крупу крутым кипятком и даю закипеть (без соли), потом выключаю и оставляю на ночь под крышкой, на следующий день при необходимости добавляю воды и варю на медленном огне минут 30-40, солю только в самом конце, когда крупа готова к употреблению</p>', '2022-01-19 00:26:20', NULL, 1, 3, 4);
INSERT INTO `answers` VALUES (10, '<p>перловка варится.. не варится а томится, не менее 40 минут.\r\nа там. надо контролировать и перемешивать, чтоб не подгорела.</p>', '2022-01-19 00:27:15', NULL, 0, 3, 5);
INSERT INTO `answers` VALUES (11, '<p>Как писали выше 40 мин, больше не стоит</p>', '2022-01-19 00:27:59', NULL, 0, 3, 7);
INSERT INTO `answers` VALUES (12, '<p>Всем спасибо, сварил!</p>', '2022-01-19 00:28:53', NULL, 0, 3, 6);
INSERT INTO `answers` VALUES (13, '<p>не думаю что это возможно....</p>', '2022-01-19 00:35:48', NULL, 0, 4, 6);
INSERT INTO `answers` VALUES (14, '<b><i>Doe J. M.,</i></b><span>&nbsp;подобрал пароль к 1му архиву, содержимое прикрепляю</span>', '2022-01-19 00:36:59', NULL, 0, 4, 4);
INSERT INTO `answers` VALUES (15, '<b><i>Петров Д. А.,</i></b><span>&nbsp;а остальные 2 архива?</span>', '2022-01-19 00:38:40', NULL, 0, 4, 1);
INSERT INTO `answers` VALUES (16, '<b><i>Doe J. M.,</i></b><span>&nbsp;К сожалению пока не удалось подобрать пароль</span>', '2022-01-19 00:39:12', NULL, 0, 4, 4);
INSERT INTO `answers` VALUES (17, '<p>Если, сравнив с окружающими, Вы понимаете, что заслуживаете большего. И знаете, как это большее получить</p>', '2022-01-19 00:45:36', NULL, 0, 5, 1);
INSERT INTO `answers` VALUES (18, '<p>Когда ты уверен, что незаменим и тебя не уволят за наглость).</p>', '2022-01-19 00:46:17', NULL, 0, 5, 4);
INSERT INTO `answers` VALUES (19, '<p>Ну например: Работать больше (усердно), оставаться подольше, что нибудь такое сделать что бы начальник восхитился тобой и повысил :)</p>', '2022-01-19 00:47:19', NULL, 0, 5, 6);
INSERT INTO `answers` VALUES (20, '<p>Охранником</p>', '2022-01-19 00:50:58', NULL, 0, 6, 1);
INSERT INTO `answers` VALUES (21, '<p>философом, вероятно...)</p>', '2022-01-19 00:51:13', NULL, 1, 6, 4);
INSERT INTO `answers` VALUES (22, '<p>Оператором колл-центра...</p>', '2022-01-19 00:51:56', NULL, 0, 6, 6);
INSERT INTO `answers` VALUES (23, '<p>кватэра - [к в а т э р а] - 7 літар, 7 гукаў</p>', '2022-01-19 01:00:58', NULL, 0, 8, 6);
INSERT INTO `answers` VALUES (24, '<p>дзень - [дз\' э н\'] - 5 літар, 3 гукі (дз\' - адзін гук, які на пісьме абазначаецца 2 літарамі: д і з)</p>', '2022-01-19 01:02:00', NULL, 0, 8, 7);
INSERT INTO `answers` VALUES (25, '<p>Дзякуй. А астатнія словы? Дапамажыце пліз, трэба дамашку зрабіць хутка...</p>', '2022-01-19 01:03:04', NULL, 0, 8, 1);
INSERT INTO `answers` VALUES (26, '<b><i>Doe J. M.,</i></b><span>&nbsp;лілея - [л\' і л\' э й\' а] - 5 літар, 6 гукаў</span>', '2022-01-19 01:04:22', NULL, 1, 8, 6);
INSERT INTO `answers` VALUES (27, '<p>акуляры - [а к у л\' а р ы] - 7 літар, 7 гукаў</p>', '2022-01-19 01:05:01', NULL, 0, 8, 6);
INSERT INTO `answers` VALUES (28, '<b><i>Doe J. M.,</i></b><span>&nbsp;што там яшчэ засталося?</span>', '2022-01-19 01:06:05', NULL, 0, 8, 7);
INSERT INTO `answers` VALUES (29, '<b><i>Nosov V. I.,</i></b><span>&nbsp;янота сам зрабіў ужо, \"твар\" засталося</span>', '2022-01-19 01:07:04', NULL, 0, 8, 1);
INSERT INTO `answers` VALUES (30, '<b><i>Doe J. M.,</i></b><span>&nbsp;зусім проста ж: твар - [т в а р] - 4 літары, 4 гукі</span>', '2022-01-19 01:07:57', NULL, 0, 8, 7);
INSERT INTO `answers` VALUES (31, '<p>Дзякуй хлопцы!!! Сын 10 балаў атрымау сення, з мяне ўсім падабайкі)</p>', '2022-01-19 01:10:00', NULL, 0, 8, 1);

-- ----------------------------
-- Table structure for attachments
-- ----------------------------
DROP TABLE IF EXISTS `attachments`;
CREATE TABLE `attachments`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `file` varchar(75) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `id_UNIQUE`(`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 260 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of attachments
-- ----------------------------
INSERT INTO `attachments` VALUES (1, 'Темы курсовых проектов_BqMfmYX8.docx');
INSERT INTO `attachments` VALUES (2, 'list_w3b0Xe3u.png');
INSERT INTO `attachments` VALUES (3, 'img_docs_OkR4b9lS.rar');
INSERT INTO `attachments` VALUES (4, 'documents_jHdOIWBI.zip');
INSERT INTO `attachments` VALUES (5, 'file1_1rnXv7b3.rar');
INSERT INTO `attachments` VALUES (6, 'sky_OEBQP1oz.jpg');
INSERT INTO `attachments` VALUES (7, 'forest_rYsGZPGG.jpg');
INSERT INTO `attachments` VALUES (8, 'nature_oNgrVfGP.jpg');

-- ----------------------------
-- Table structure for categories
-- ----------------------------
DROP TABLE IF EXISTS `categories`;
CREATE TABLE `categories`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(25) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `color` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `id_UNIQUE`(`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 49 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of categories
-- ----------------------------
INSERT INTO `categories` VALUES (1, 'Programming', '#d21414');
INSERT INTO `categories` VALUES (2, 'Кулинария', '#d1c32e');
INSERT INTO `categories` VALUES (3, 'Образование', '#1d0dfd');
INSERT INTO `categories` VALUES (4, 'Музыка', '#1aea7f');
INSERT INTO `categories` VALUES (5, 'Работа', '#2cb5a5');
INSERT INTO `categories` VALUES (6, 'Спорт', '#9580c6');
INSERT INTO `categories` VALUES (7, 'Прочее', '#000000');

-- ----------------------------
-- Table structure for question_attachment
-- ----------------------------
DROP TABLE IF EXISTS `question_attachment`;
CREATE TABLE `question_attachment`  (
  `question_id` int NOT NULL,
  `attachment_id` int NOT NULL,
  PRIMARY KEY (`question_id`, `attachment_id`) USING BTREE,
  INDEX `fk_question_has_attachment_attachment1_idx`(`attachment_id`) USING BTREE,
  INDEX `fk_question_has_attachment_question1_idx`(`question_id`) USING BTREE,
  CONSTRAINT `fk_question_has_attachment_attachment1` FOREIGN KEY (`attachment_id`) REFERENCES `attachments` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `fk_question_has_attachment_question1` FOREIGN KEY (`question_id`) REFERENCES `questions` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of question_attachment
-- ----------------------------
INSERT INTO `question_attachment` VALUES (4, 3);
INSERT INTO `question_attachment` VALUES (4, 4);
INSERT INTO `question_attachment` VALUES (4, 5);

-- ----------------------------
-- Table structure for questions
-- ----------------------------
DROP TABLE IF EXISTS `questions`;
CREATE TABLE `questions`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `text` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `creation_date` datetime NOT NULL,
  `editing_date` datetime NULL DEFAULT NULL,
  `closed` tinyint NULL DEFAULT NULL,
  `category_id` int NOT NULL,
  `author_id` int NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `id_UNIQUE`(`id`) USING BTREE,
  INDEX `fk_question_category1_idx`(`category_id`) USING BTREE,
  INDEX `fk_question_user1_idx`(`author_id`) USING BTREE,
  CONSTRAINT `fk_question_category1` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `fk_question_user1` FOREIGN KEY (`author_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 199 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of questions
-- ----------------------------
INSERT INTO `questions` VALUES (1, 'How can I resize an image using Java?', '<p>I have a PNG image and I want to resize it. How can I do that? Though I have gone through <a href=\"https://stackoverflow.com/questions/244164/how-can-i-resize-an-image-using-java/4528136#4528136\" target=\"_blank\">this</a> I can\'t understand the snippet.</p>', '2022-01-18 23:44:50', '2022-01-18 23:45:54', 1, 1, 6);
INSERT INTO `questions` VALUES (2, 'Темы курсовых работ по программированию', '<p>Добрый вечер. Подскажите пожалуйста примерный список тем курсовых проектов для студентов ИТ специальностей?</p>', '2022-01-19 00:07:21', NULL, 0, 3, 7);
INSERT INTO `questions` VALUES (3, 'Как правильно готовить перловую крупу?', '<p>Cколько варить минут, кто-нибудь знает?</p>', '2022-01-19 00:24:38', NULL, 1, 2, 6);
INSERT INTO `questions` VALUES (4, 'Помогите снять пароль с архива', '<p>Скачал архивы с документами, а они запаролены, можно ли его расшифровать не зная пароля? Архивы в прикреплении</p>', '2022-01-19 00:34:51', '2022-01-19 00:35:11', 0, 7, 1);
INSERT INTO `questions` VALUES (5, 'При каком условии стоит просить повышения?)', '<p>При каком условии стоит просить повышения на работе?)</p>', '2022-01-19 00:44:06', NULL, 0, 5, 5);
INSERT INTO `questions` VALUES (6, 'Работа для выпускника философского факультета', '<p>Кем, кроме преподавателя, предлагают работать выпускнику философского факультета?</p>', '2022-01-19 00:50:21', NULL, 0, 5, 5);
INSERT INTO `questions` VALUES (7, 'Доставка чайного гриба в Саратове.', '<p>Есть ли аэродоставка чайного гриба в Саратове? Очень надо.</p>', '2022-01-19 00:55:30', NULL, 0, 2, 6);
INSERT INTO `questions` VALUES (8, 'Кольки гукаў і літар у словах', '<p>Кватэра, акунь, янот, дзень, лілея,твар, акуляры</p>', '2022-01-19 00:59:31', NULL, 1, 7, 1);

-- ----------------------------
-- Table structure for rating
-- ----------------------------
DROP TABLE IF EXISTS `rating`;
CREATE TABLE `rating`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `grade` int NOT NULL,
  `user_id` int NOT NULL,
  `answer_id` int NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `id_UNIQUE`(`id`) USING BTREE,
  INDEX `fk_rating_user1_idx`(`user_id`) USING BTREE,
  INDEX `fk_rating_answer1_idx`(`answer_id`) USING BTREE,
  CONSTRAINT `fk_rating_answer1` FOREIGN KEY (`answer_id`) REFERENCES `answers` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `fk_rating_user1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 28 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of rating
-- ----------------------------
INSERT INTO `rating` VALUES (1, -1, 7, 2);
INSERT INTO `rating` VALUES (2, 1, 6, 3);
INSERT INTO `rating` VALUES (3, 1, 6, 1);
INSERT INTO `rating` VALUES (4, 1, 5, 1);
INSERT INTO `rating` VALUES (5, 1, 4, 3);
INSERT INTO `rating` VALUES (6, 1, 1, 3);
INSERT INTO `rating` VALUES (7, 1, 7, 8);
INSERT INTO `rating` VALUES (8, 1, 4, 8);
INSERT INTO `rating` VALUES (9, -1, 4, 6);
INSERT INTO `rating` VALUES (10, -1, 6, 10);
INSERT INTO `rating` VALUES (11, 1, 6, 11);
INSERT INTO `rating` VALUES (12, 1, 6, 9);
INSERT INTO `rating` VALUES (13, 1, 1, 14);
INSERT INTO `rating` VALUES (14, 1, 1, 19);
INSERT INTO `rating` VALUES (15, 1, 5, 21);
INSERT INTO `rating` VALUES (16, -1, 5, 20);
INSERT INTO `rating` VALUES (17, -1, 4, 20);
INSERT INTO `rating` VALUES (18, -1, 6, 20);
INSERT INTO `rating` VALUES (19, 1, 6, 21);
INSERT INTO `rating` VALUES (20, 1, 1, 24);
INSERT INTO `rating` VALUES (21, 1, 1, 26);
INSERT INTO `rating` VALUES (22, 1, 1, 23);
INSERT INTO `rating` VALUES (23, 1, 1, 27);
INSERT INTO `rating` VALUES (24, 1, 1, 28);
INSERT INTO `rating` VALUES (25, 1, 1, 30);

-- ----------------------------
-- Table structure for user_hash
-- ----------------------------
DROP TABLE IF EXISTS `user_hash`;
CREATE TABLE `user_hash`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `hash` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `user_id` int NOT NULL,
  `expires` datetime NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `id_UNIQUE`(`id`) USING BTREE,
  UNIQUE INDEX `fk_account_hash_user1_idx`(`user_id`) USING BTREE,
  CONSTRAINT `fk_account_hash_user1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 62 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user_hash
-- ----------------------------

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `first_name` varchar(25) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `last_name` varchar(25) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `middle_name` varchar(25) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `login` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `email` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `avatar` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `status` enum('ACTIVE','INACTIVE','BANNED') CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'INACTIVE',
  `role` enum('ADMIN','USER','MODERATOR') CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'USER',
  `telegram` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `last_login_date` datetime NULL DEFAULT NULL,
  `credential_key` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `id_UNIQUE`(`id`) USING BTREE,
  UNIQUE INDEX `login_UNIQUE`(`login`) USING BTREE,
  UNIQUE INDEX `email_UNIQUE`(`email`) USING BTREE,
  INDEX `fk_user_role1_idx`(`role`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 94 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES (1, 'John', 'Doe', 'Michael', 'admin', '$2a$10$PWd1tJxHFcPcPZuBzP5N1O/Bc.0f00bOf0JlW2.YHSOykRI5IkwKK', 'admin@gmail.com', 'avatar_SSfSVPjJunzFsELEvDyqdhv5u.png', 'ACTIVE', 'ADMIN', NULL, '2022-01-19 00:30:28', '$2a$10$SGH9zIqAi01Fuypq/mABk.vCbrTsS6nz5vjEtsPQJmHz8EXyKOug.');
INSERT INTO `users` VALUES (4, 'Дмитрий', 'Петров', 'Александрович', 'moderator', '$2a$10$/.chP1Gs5wPUbuOhckfekezp/I8NP4JpC1/TR0bP6FHmdxu9zh.5u', 'petrov@gmail.com', 'avatar_5TxWKKPpJJtIhoJ4yT6JeHmP1.png', 'ACTIVE', 'MODERATOR', 'petrov', '2022-01-19 00:17:12', '$2a$10$bbcDWIRqN7XkiSWbELOhx.pYFRykX6a1oLozDtMrCrLW155Htj1G.');
INSERT INTO `users` VALUES (5, 'Алина', 'Кипелова', 'Вячеславовна', 'alina', '$2a$10$QXBooPoAHm6t7LcZf0fdNugDitY7Ni9q8qbTfTswVhzCZOUKQAqwe', 'alina@mail.ru', 'avatar_c0NGmREn2s2glmddxC9P5IlSe.png', 'ACTIVE', 'USER', 'alina99', '2022-01-19 00:08:37', '$2a$10$qP/pmUMAu/V2Ll7UGivSb..D6bkTJnyhwG8KbfQiDSkKzuOW2TCtK');
INSERT INTO `users` VALUES (6, 'Петр', 'Меньшов', 'Олегович', 'wiwoh24794', '$2a$10$ytXt.Wf/yc5n0tbt/.xS6ezoQeBRUPy.a3mQ/7xBzU3xj4LB15CVK', 'wiwoh24794@dkb3.com', 'avatar_c8p750LMrmCeBX722pN6sTvew.png', 'ACTIVE', 'USER', '', '2022-01-19 00:57:50', '$2a$10$J.EHd8vjVqCH56NApD/r8eUYGxLIXHXCbs0mL0O3klF2P/B2ARXy2');
INSERT INTO `users` VALUES (7, 'Viktor', 'Volski', 'Ivanovich', 'viktor1975', '$2a$10$s5DKUSSFw4WNZWj8h6rLFO4VZ/8X6mcpqWdE3.bAdQxcwY2Tpdnau', 'viktor1975@yandex.by', 'avatar_oEiM6a3bGeL8yQHRFFuyVGTkI.png', 'ACTIVE', 'USER', 'viktor1975', '2022-01-19 00:58:08', '$2a$10$.DvFotJJzfcHpr7HYoa38.I89ESO8y0tDJhWWyL7ec7SZl1znSDe6');

SET FOREIGN_KEY_CHECKS = 1;
