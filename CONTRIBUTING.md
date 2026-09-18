# Contributing to Keisuki

First off, thank you for considering contributing to **Keisuki**! 🎉 

Keisuki is an open-source project, and contributions of all kinds—bug fixes, new features, UI/UX polish, documentation improvements, and feedback—are highly valued.

---

## 📜 Table of Contents
1. [Code of Conduct](#code-of-conduct)
2. [How Can I Contribute?](#how-can-i-contribute)
   - [Reporting Bugs](#reporting-bugs)
   - [Suggesting Enhancements](#suggesting-enhancements)
   - [Pull Requests](#pull-requests)
3. [Development Setup](#development-setup)
4. [Coding Standards & Conventions](#coding-standards--conventions)
   - [Kotlin & Compose Style](#kotlin--compose-style)
   - [Git Commit Guidelines (Conventional Commits)](#git-commit-guidelines)
5. [License](#license)

---

## 🤝 Code of Conduct

This project and everyone participating in it is governed by the [Keisuki Code of Conduct](CODE_OF_CONDUCT.md). By participating, you are expected to uphold this code. Please report unacceptable behavior following the guidelines in the document.

---

## 🛠️ How Can I Contribute?

### Reporting Bugs
Before creating bug reports, please check existing [Issues](https://github.com/hiashwinsharma/Keisuki/issues) to avoid duplicates.

When creating a bug report, please include:
- A clear and descriptive title.
- Steps to reproduce the issue.
- Expected behavior vs. actual behavior.
- Device information (Device model, Android OS version, app version).
- Logcat snippets or stack traces if applicable.

### Suggesting Enhancements
Feature requests are tracked as GitHub Issues. Provide:
- A concise summary of the proposed feature.
- The use case or problem this feature solves.
- Mockups, screenshots, or design references if relevant.

### Pull Requests
1. **Fork the repository** and clone your fork locally.
2. **Create a topic branch** from `main` (e.g., `feat/interactive-haptics` or `fix/chart-scrubber-bounds`).
3. **Make your changes** cleanly with self-contained commits.
4. **Run unit tests and verification**:
   ```bash
   ./gradlew test
   ./gradlew lintDebug
   ```
5. **Push to your fork** and submit a Pull Request targeting the `main` branch.
6. Clearly describe what the PR accomplishes and reference any related issues (e.g., `Fixes #42`).

---

## 💻 Development Setup

- **IDE**: Android Studio Ladybug (2024.2+) or later.
- **JDK**: Java 17 (set in Android Studio `Settings > Build, Execution, Deployment > Build Tools > Gradle > Gradle JDK`).
- **Target Platform**: Android SDK 35 (Android 15), Min SDK 26 (Android 8.0).

To build the debug APK from the command line:
```bash
./gradlew assembleDebug
```

---

## 🎨 Coding Standards & Conventions

### Kotlin & Compose Style
- Follow official [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html) and [Android Compose API Guidelines](https://github.com/androidx/androidx/blob/androidx-main/compose/docs/compose-api-guidelines.md).
- **Unidirectional Data Flow (UDF)**: ViewModels expose immutable `StateFlow<UiState>` and handle explicit `UiAction` intents.
- **Compose Stability**: Prefer immutable data models and avoid unnecessary recompositions.
- **Design System**: Use `MaterialTheme.colorScheme` and `LocalAppCornerRadius.current` rather than hardcoding colors or dimensions.

### Git Commit Guidelines
We adhere to the [Conventional Commits](https://www.conventionalcommits.org/) specification:

```text
<type>(<optional scope>): <description>

[optional body]

[optional footer(s)]
```

**Allowed Types:**
- `feat`: A new feature
- `fix`: A bug fix
- `refactor`: Code change that neither fixes a bug nor adds a feature
- `style`: Changes that do not affect code logic (formatting, spacing, imports)
- `perf`: A code change that improves performance
- `test`: Adding or updating tests
- `docs`: Documentation updates
- `build` / `ci`: Changes that affect the build system or CI pipelines
- `chore`: Maintenance tasks

*Example:* `feat(history): add animated scrubbing pill to custom canvas chart`

---

## 📄 License

By contributing to Keisuki, you agree that your contributions will be licensed under its [GNU General Public License v3.0 (GPL-3.0)](LICENSE).
