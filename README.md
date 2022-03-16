

![Tests](https://github.com/simplelocalize/simplelocalize-cli/workflows/Run%20Tests/badge.svg)
[![Maintainability](https://api.codeclimate.com/v1/badges/af2f6a7680929a8dba41/maintainability)](https://codeclimate.com/github/simplelocalize/simplelocalize-cli/maintainability)
[![codecov](https://codecov.io/gh/simplelocalize/simplelocalize-cli/branch/master/graph/badge.svg)](https://codecov.io/gh/simplelocalize/simplelocalize-cli)

## What it does?

SimpleLocalize CLI to simplifies the process of translation in web apps, mobile apps, and games. It can:
- find translation keys in your local files
- upload existing translation files or translation keys
- download translation file in ready to use format for already used i18n library like: i18next, Android, iOS, and many others


## Installation

### Version 2.0.X (recommended)
```shell
curl -s https://get.simplelocalize.io/2.0/install | bash
```

### Version 1.1.2
```shell
curl -s https://get.simplelocalize.io/install | bash
```

## Usage

```properties
simplelocalize -c config.yml [COMMAND] --apiKey <PROJECT_API_KEY> --uploadaPath <UPLOAD_PATH> other arguments...
```

## Example configuration file
Use configuration file in order to simplify your bash command. Arguments used in command always overrides properties set in configuration file.

Filename: `simplelocalize.yml`

```yaml
# Project API Key
apiKey: API_KEY

# Properties used by 'upload' command
uploadPath: ./src/{lang}/{ns}.json
uploadFormat: single-language-json
uploadOptions:
  - INCLUDE_NAMESPACE

# Properties used by 'download' command
downloadPath: ./src/{ns}/messages_{lang}.json
downloadFormat: single-language-json
downloadOptions:
  - WRITE_NESTED

# Properties used by 'extract' command
searchDir: ./src
projectType: yahoo/react-intl
ignoreKeys:
  - 'WELCOME'
  - 'ABOUT-US'
```

### Example: One file with translations

```bash
.
└── locales
    └── messages.json
```

Configuration:
```
uploadPath: /locales/messages.json
uploadFormat: multi-language-json
```


### Example: Single file with multiple language directories

```bash
.
├── ca
│   └── index.json
├── en
│   └── index.json
└── es
    └── index.json
```

Configuration:
```
uploadPath: /{lang}/index.json
uploadFormat: single-language-json
```

### Example: Multiple files with multiple language directories

```bash
.
├── ca
│   ├── common.json
│   └── home.json
├── en
│   ├── common.json
│   └── home.json
└── es
    ├── common.json
    └── home.json
```

Configuration:
```
uploadPath: /{lang}/{ns}.json
uploadFormat: single-language-json
```

## Upload translations command

```properties
simplelocalize upload --apiKey <PROJECT_API_KEY>
```


## Download translations command

```properties
simplelocalize download --apiKey <PROJECT_API_KEY>
```


## Sync translations command
Sync command combines upload and download commend execution.
```properties
simplelocalize sync --apiKey <PROJECT_API_KEY>
```


## Extract translation keys command

```properties
simplelocalize extract --apiKey <PROJECT_API_KEY>
```

## Custom configuration file
By default, SimpleLocalize will load configuration from file named `simplelocalize.yml`. You can load configuration from different location using `-c` parameters.

```properties
simplelocalize -c my-configuration.yml upload
```

## Commands documentation

Please remember to [get API Key for your SimpleLocalize project](https://simplelocalize.io/docs/cli/get-started/) before your start.

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

