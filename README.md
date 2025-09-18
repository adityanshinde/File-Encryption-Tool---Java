
# File Encryption Tool

A modern Java Swing application for secure file encryption and decryption using AES.

---

## Table of Contents
- [Features](#features)
- [Requirements](#requirements)
- [Project Structure](#project-structure)
- [Setup & Build](#setup--build)
- [Running the Application](#running-the-application)
- [Running Unit Tests](#running-unit-tests)
- [Usage Guide](#usage-guide)
- [Security Notes](#security-notes)
- [Screenshots](#screenshots)
- [License](#license)

---

## Features
- Modern, visually appealing light-themed UI
- Encrypt and decrypt files with a user-provided passphrase (min 16 chars)
- Secure key management (no hardcoded keys)
- Input validation for file and passphrase
- Progress bar and status messages for long operations
- Logging of encryption/decryption events and errors
- File overwrite protection with user confirmation
- Unit tests for encryption/decryption logic

## Requirements
- Java 8 or higher
- Maven (for building and running tests)

## Project Structure
```
FileEncryptionTool/
├── pom.xml
├── README.md
├── src/
│   ├── encryption/
│   │   ├── EncryptionHandler.java
│   │   └── EncryptionHandlerTest.java
│   └── ui/
│       └── EncryptionToolUI.java
```

## Setup & Build
1. **Clone or download** this repository.
2. Open a terminal in the project root directory.
3. Build the project:
	```
	mvn clean compile
	```

## Running the Application
You can run the UI directly using Maven:
```sh
mvn exec:java -Dexec.mainClass="ui.EncryptionToolUI"
```
Or, compile and run manually:
```sh
javac -d out src/encryption/EncryptionHandler.java src/ui/EncryptionToolUI.java
java -cp out ui.EncryptionToolUI
```

## Running Unit Tests
To run the unit tests:
```sh
mvn test
```

## Usage Guide
1. **Launch the application.**
2. Click **Upload File** and select the file you want to encrypt or decrypt.
3. Enter a secure passphrase (at least 16 characters).
4. Click **Encrypt** or **Decrypt** as needed.
5. If the output file already exists, you will be prompted to confirm overwriting.
6. Status and progress will be shown at the bottom of the window.
7. The encrypted file is saved in the same directory as the original, prefixed with `encrypted_`. Decrypted files are prefixed with `decrypted_`.

## Security Notes
- The passphrase is never stored or logged.
- Use a strong, unique passphrase for each file.
- Encrypted files are saved with the prefix `encrypted_`, and decrypted files with `decrypted_`.
- The tool uses AES encryption with a 16-byte key derived from your passphrase.

## Screenshots

<img width="584" height="464" alt="image" src="https://github.com/user-attachments/assets/731ed23c-e068-45b9-9c5a-0805153c3d5e" />


## License
MIT License
