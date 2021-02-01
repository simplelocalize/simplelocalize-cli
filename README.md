![logo](https://simplelocalize.io/cli/get-started/get-started.png)

![Tests](https://github.com/simplelocalize/simplelocalize-cli/workflows/Run%20Tests/badge.svg)
![Build native executables](https://github.com/simplelocalize/simplelocalize-cli/workflows/Build%20executables/badge.svg?branch=master)
[![Maintainability](https://api.codeclimate.com/v1/badges/af2f6a7680929a8dba41/maintainability)](https://codeclimate.com/github/simplelocalize/simplelocalize-cli/maintainability)
[![codecov](https://codecov.io/gh/simplelocalize/simplelocalize-cli/branch/master/graph/badge.svg)](https://codecov.io/gh/simplelocalize/simplelocalize-cli)

## What it does?

SimpleLocalize CLI to simplifies the process of translation in web apps, mobile apps, and games. It can:
- find translation keys in your local files
- upload existing translation files or transaltion keys
- download translation file in ready to use format for already used i18n library like: i18next, Android, iOS, and many others


## Installation

```shell
curl -s https://get.simplelocalize.io/install | bash
```

## Usage

```properties
simplelocalize-cli [COMMAND] --apiKey <PROJECT_API_KEY> rest of parameters...
```

## Available commands

Rememebr to [get API Key for your SimpleLocalize project](https://simplelocalize.io/docs/cli/get-started/) before your start.

- `simplelocalize-cli extract` - learn more [how to extract translation keys from local files](https://simplelocalize.io/docs/cli/i18n-keys-extraction/)
- `simplelocalize-cli upload` - learn more [how to upload translations or translation keys](https://simplelocalize.io/docs/cli/upload-translations/)
- `simplelocalize-cli download` - learn more [how to download ready to use translation file](https://simplelocalize.io/docs/cli/download-translations/)


## 👩‍⚖️ License

MIT © 

