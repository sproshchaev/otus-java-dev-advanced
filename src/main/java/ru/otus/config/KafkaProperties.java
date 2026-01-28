package ru.otus.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "kafka")
public class KafkaProperties {

    private String bootstrapServers;
    private Topics topics = new Topics();

    public static class Topics {
        private String queue1Messages;
        private String queue2Messages;

        // Getters and setters
        public String getQueue1Messages() {
            return queue1Messages;
        }

        public void setQueue1Messages(String queue1Messages) {
            this.queue1Messages = queue1Messages;
        }

        public String getQueue2Messages() {
            return queue2Messages;
        }

        public void setQueue2Messages(String queue2Messages) {
            this.queue2Messages = queue2Messages;
        }
    }

    // Getters and setters
    public String getBootstrapServers() {
        return bootstrapServers;
    }

    public void setBootstrapServers(String bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
    }

    public Topics getTopics() {
        return topics;
    }

    public void setTopics(Topics topics) {
        this.topics = topics;
    }
}