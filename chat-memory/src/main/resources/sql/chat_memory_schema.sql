CREATE TABLE IF NOT EXISTS `SPRING_AI_CHAT_MEMORY` (
                                                       `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
                                                       `conversation_id` VARCHAR(255) NOT NULL,
    `type` VARCHAR(10) NOT NULL,
    `content` TEXT NOT NULL,
    `timestamp`       TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_conversation (conversation_id)
    ) ;
