INSERT INTO `role` (`role_name`) VALUES ('USER');
INSERT INTO `role` (`role_name`) VALUES ('EDITOR');
INSERT INTO `role` (`role_name`) VALUES ('ADMIN');

INSERT INTO `users` (`email`, `password`, `enabled`) VALUES ('user1@mail.com', '$2a$10$DsI0IQwDUaVn3PpBOtj3fulHX7b9rFJ2/HNNvH6PX5D3XfOieEwce', true);
INSERT INTO `users` (`email`, `password`, `enabled`) VALUES ('user2@mail.com', '$2a$10$DsI0IQwDUaVn3PpBOtj3fulHX7b9rFJ2/HNNvH6PX5D3XfOieEwce', true);
INSERT INTO `users` (`email`, `password`, `enabled`) VALUES ('user3@mail.com', '$2a$10$DsI0IQwDUaVn3PpBOtj3fulHX7b9rFJ2/HNNvH6PX5D3XfOieEwce', true);

INSERT INTO `users_roles` (`user_id`, `role_id`) VALUES (1, 1);
INSERT INTO `users_roles` (`user_id`, `role_id`) VALUES (2, 2);
INSERT INTO `users_roles` (`user_id`, `role_id`) VALUES (3, 3);
