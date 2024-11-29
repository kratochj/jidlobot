# Jidlobot
[![Docker](https://github.com/kratochj/jidlobot/actions/workflows/build.yml/badge.svg)](https://github.com/kratochj/jidlobot/actions/workflows/build.yml)  
[![CodeQL](https://github.com/kratochj/jidlobot/actions/workflows/github-code-scanning/codeql/badge.svg)](https://github.com/kratochj/jidlobot/actions/workflows/github-code-scanning/codeql)

**Jidlobot** is a Slack bot designed to fetch and display the daily menu from **Jídlovice**. Built with **Spring Boot**, it integrates seamlessly with Slack using the **Bolt framework**.

---

## 🌟 Features

- **Fetch Daily Menu**: Automatically retrieves the daily menu, including soups, main dishes, and special dishes.
- **Slack Integration**: Responds to mentions and commands in Slack via Socket Mode.
- **Cache Management**: Optimizes HTTP requests by caching menu data for a configurable duration.

---

## ⚙️ Setup

### Prerequisites

- **Java** 21
- **Maven** 3.x
- **Docker** (optional, for containerization)
- A Slack workspace with a configured bot user

### Obtaining Slack Keys

To use Jidlobot, you will need two Slack keys:
1. **Bot User OAuth Token (`SLACK_BOT_TOKEN`)**
2. **App-Level Token (`SLACK_APP_LEVEL_TOKEN`)**

Follow these steps to obtain them:

1. **Create a Slack App**:
    - Visit the [Slack API Apps Page](https://api.slack.com/apps) and click **Create an App**.
    - Select **From scratch** and give your app a name (e.g., `Jidlobot`), then choose the appropriate Slack workspace.

2. **Enable Bot Token Scopes**:
    - In the **OAuth & Permissions** section of your app, scroll to **Bot Token Scopes**.
    - Add the following scopes:
        - `app_mentions:read`
        - `chat:write`
        - `channels:read`
        - `groups:read`

3. **Install the App to Your Workspace**:
    - Once scopes are configured, install the app to your workspace from the **OAuth & Permissions** section.
    - Copy the **Bot User OAuth Token** (starts with `xoxb-`) and use it as `SLACK_BOT_TOKEN`.

4. **Generate an App-Level Token**:
    - Go to the **Socket Mode** section and enable Socket Mode.
    - Click **Generate Token and Scopes**, then add the `connections:write` scope.
    - Copy the **App-Level Token** (starts with `xapp-`) and use it as `SLACK_APP_LEVEL_TOKEN`.

---

### Installation

1. **Clone the repository**:
   ```bash
   git clone https://github.com/<your-username>/jidlobot.git
   cd jidlobot
   ```

2. **Build the project**:
   ```bash
   mvn install
   ```

3. **Configure Slack Tokens**:  
   Create a Kubernetes `Secret` with your Slack bot credentials:

   ```yaml
   apiVersion: v1
   kind: Secret
   metadata:
     name: slack-secrets
     namespace: fun
   type: Opaque
   data:
     SLACK_BOT_TOKEN: <base64-encoded-value>
     SLACK_APP_LEVEL_TOKEN: <base64-encoded-value>
   ```

4. **Run the application**:
   ```bash
   mvn spring-boot:run
   ```

5. **Deploy to Kubernetes** (optional):  
   Use the `jidlobot-deployment.yaml` file provided in the repository. Adjust the image name and tag as necessary.

   The pre-built Docker image for `jidlobot` is available on [GitHub Container Registry (GHCR)](https://github.com/kratochj/jidlobot/pkgs/container/jidlobot).

---

## 🔧 Configuration

Customize the application using the `application.yaml` file:

```yaml
slack:
  bot-token: ${SLACK_BOT_TOKEN}
  app-token: ${SLACK_APP_LEVEL_TOKEN}

menu:
  url: "https://www.jidlovice.cz/telehouse/"
  cache-enabled: true
  cache-for-seconds: 3600
```

---

## 🛠️ Usage

- Mention the bot in any channel with commands like:
  ```text
  @jidlobot menu
  ```
  to receive the current menu.

- Use:
  ```text
  @jidlobot help
  ```
  to view help instructions.

---

## 💻 Development

### Running Tests
Run the test suite using Maven:
```bash
mvn test
```

### Code Coverage
Generate a code coverage report:
```bash
mvn jacoco:report
```

### Building a Docker Image
Build and push Docker images using the provided GitHub Actions workflow. Images are automatically pushed to **GitHub Container Registry (GHCR)**.

To pull the pre-built Docker image, use:
```bash
docker pull ghcr.io/kratochj/jidlobot:latest
```

---

## 📜 License

This project is licensed under the **MIT License**. See the [LICENSE](LICENSE) file for details.

---

## 🤝 Contributors

Contributions are welcome! Please read the [CONTRIBUTING.md](CONTRIBUTING.md) file for the code of conduct and details on submitting pull requests.
