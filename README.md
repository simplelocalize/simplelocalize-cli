![logo](https://simplelocalize.io/simplelocalize-cli-github.png)

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

🥸 Tip: Run script above to update CLI to the newest version. 

## Usage

```properties
simplelocalize [COMMAND] --apiKey <PROJECT_API_KEY> rest of parameters...
```

## Available commands

Rememebr to [get API Key for your SimpleLocalize project](https://simplelocalize.io/docs/cli/get-started/) before your start.

- `simplelocalize extract` - learn more [how to extract translation keys from local files](https://simplelocalize.io/docs/cli/i18n-keys-extraction/)
- `simplelocalize upload` - learn more [how to upload translations or translation keys](https://simplelocalize.io/docs/cli/upload-translations/)
- `simplelocalize download` - learn more [how to download ready to use translation file](https://simplelocalize.io/docs/cli/download-translations/)

## Integrations 

- [Android localization guide](https://simplelocalize.io/docs/integrations/android/)
- [iOS localization guide](https://simplelocalize.io/docs/integrations/ios-macos/)
- [macOS localization guide](https://simplelocalize.io/docs/integrations/ios-macos/)
- [JVM apps integration guide](https://simplelocalize.io/docs/file-formats/java-properties/)
- [FormatJS integration guide](https://simplelocalize.io/docs/integrations/format-js/)
- [FormatJS CLI messsages import guide](https://simplelocalize.io/docs/integrations/format-js-cli/)
- [i18next HTTP backend integration guide](https://simplelocalize.io/docs/integrations/i18next/)

## Documentation 

Head to [docs.simplelocalize.io](https://simplelocalize.io/docs/cli/get-started/) to learn the SimpleLocalize basics

## License

Check LICENSE.md file

