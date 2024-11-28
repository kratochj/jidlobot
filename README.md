# Slack Bot do send Daily Menu from Jidlovice Telehouse

[![Build Pipeline](https://github.com/kratochj/jidlobot/actions/workflows/docker-publish.yml/badge.svg?branch=main)](https://github.com/kratochj/jidlobot/actions/workflows/docker-publish.yml)

## Table of Contents

- [Introduction](#introduction)
- [Features](#features)
- [Prerequisites](#prerequisites)
- [Setup](#setup)
    - [Clone the Repository](#clone-the-repository)
    - [Configure the Slack App](#configure-the-slack-app)
    - [Set Up Environment Variables](#set-up-environment-variables)
    - [Install Dependencies](#install-dependencies)
- [Configuration](#configuration)
- [Running the Application](#running-the-application)
- [Interacting with the Bot](#interacting-with-the-bot)
- [Health Checks](#health-checks)
- [Contributing](#contributing)

## Introduction

This project is a Slack bot built with Java using Spring Boot and Bolt for Java SDK. The bot connects to Slack via Socket Mode, allowing it to run behind a firewall without exposing a public URL. It listens for mentions and responds with daily menu information or help messages. The bot also includes a health check endpoint implemented with Spring Boot Actuator.

## Features

- **Socket Mode Connection**: Uses Slack's Socket Mode to connect without exposing a public endpoint.
- **Event Handling**: Listens for `app_mention` events to respond to user messages.
- **Message Formatting**: Utilizes Slack's Block Kit for rich message formatting.
- **Health Checks**: Implements a custom health indicator for monitoring bot status via Actuator.
- **Localization**: Formats prices according to Czech locale conventions.
- **Extensibility**: Easily add new commands or modify existing ones.

## Prerequisites

- **Java Development Kit (JDK) 21 or higher**
- **Maven 3.6+**
- **Slack Workspace**: Access to a Slack workspace where you can install and test the bot.
- **Slack App**: A Slack app configured with the necessary permissions.

## Setup

### Clone the Repository

```bash
git clone https://github.com/kratochj/jidlobot.git
cd jidlobot
```

### Configure the Slack App

1. **Create a Slack App**:
    - Go to [Slack API Apps](https://api.slack.com/apps) and create a new app.
    - Choose your workspace and provide an app name.

2. **Enable Socket Mode**:
    - Navigate to **Settings > Socket Mode**.
    - Enable **Socket Mode**.
    - Create an **App-Level Token** with the scope `connections:write`.
    - **Save the App-Level Token (`xapp-` token)**.

3. **Configure OAuth & Permissions**:
    - Go to **Features > OAuth & Permissions**.
    - Under **Scopes**, add the following **Bot Token Scopes**:
        - `app_mentions:read`
        - `chat:write`
    - Install or reinstall the app to your workspace.
    - **Save the Bot User OAuth Token (`xoxb-` token)**.

4. **Enable Event Subscriptions**:
    - Go to **Features > Event Subscriptions**.
    - Enable **Event Subscriptions**.
    - Under **Subscribe to bot events**, add `app_mention`.

### Set Up Environment Variables

Set the following variables with your Slack tokens:

```bash
export SLACK_BOT_TOKEN=xoxb-your-bot-user-oauth-token
export SLACK_APP_LEVEL_TOKEN=xapp-your-app-level-token
```

Alternatively, you can set these in your IDE's run configuration.

### Install Dependencies

Run the following command to download and install project dependencies:

```bash
mvn clean install
```

## Configuration

The application uses `application.yml` for configuration:

```yaml
slack:
  bot-token: ${SLACK_BOT_TOKEN}
  app-level-token: ${SLACK_APP_LEVEL_TOKEN}

management:
  endpoints:
    web:
      exposure:
        include: health,info
```

- **Tokens**: Tokens are injected from environment variables.
- **Actuator Endpoints**: Exposes health and info endpoints.

## Running the Application

Start the Spring Boot application:

```bash
mvn spring-boot:run
```

Or, if you prefer to run the packaged jar:

```bash
java -jar target/your-application.jar
```

## Interacting with the Bot

In your Slack workspace:

1. **Mention the Bot**: In any channel where the bot is present, type `@jidlobot` followed by a command.
2. **Available Commands**:
    - `@jidlobot menu`: The bot responds with the daily menu.
    - `@jidlobot help`: The bot lists all available commands.

**Example**:

```
@jidlobot menu
```

## Health Checks

The application includes a custom health indicator for the Slack bot. Access the health endpoint:

```bash
http://localhost:8080/actuator/health
```

**Sample Response**:

```json
{
  "status": "UP",
  "components": {
    "slackBot": {
      "status": "UP",
      "details": {
        "SlackBot": "Operational"
      }
    }
  }
}
```

- The health check verifies:
    - **Bot Token Validity**: Ensures the bot token is valid using `auth.test` API.
    - **Socket Mode Connectivity**: Checks if the Socket Mode client is connected.

## Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository.
2. Create a new branch for your feature or bugfix.
3. Commit your changes with clear messages.
4. Submit a pull request to the `main` branch.

