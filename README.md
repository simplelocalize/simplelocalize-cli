

![Tests](https://github.com/simplelocalize/simplelocalize-cli/workflows/Run%20Tests/badge.svg)
[![Maintainability](https://api.codeclimate.com/v1/badges/af2f6a7680929a8dba41/maintainability)](https://codeclimate.com/github/simplelocalize/simplelocalize-cli/maintainability)
[![codecov](https://codecov.io/gh/simplelocalize/simplelocalize-cli/branch/master/graph/badge.svg)](https://codecov.io/gh/simplelocalize/simplelocalize-cli)

## What it does?

SimpleLocalize CLI to simplifies the process of translation in web apps, mobile apps, and games. It can:
- find translation keys in your local files
- upload existing translation files or translation keys
- download translation file in ready to use format for already used i18n library like: i18next, Android, iOS, and many others


## Installation

Installation process is automated by command-line scripts. Both scripts for Windows (PowerShell) and macOS/Linux/Windows downloads a binary file with CLI, copies it to user files and makes it available to run anywhere in the system from a command-line.

```shell
# macOs / Linux / Windows (WSL)
curl -s https://get.simplelocalize.io/2.0/install | bash

# Windows (PowerShell)
. { iwr -useb https://get.simplelocalize.io/2.0/install-windows } | iex;
```

Use the same command to update SimpleLocalize CLI to the newest available version. 

You can also put the exact CLI version in URL to make sure the CLI won't change overtime, eg.: `https://get.simplelocalize.io/2.0.6/install` or `https://get.simplelocalize.io/2.0.6/install-windows`.

## Usage

SimpleLocalize CLI offers a serveral commands to invoke, `upload`, `download`, `sync` and `extract`. All of them requrires `--apiKey=KEY` parameter. You can also create a `simplelocalize.yml` file with configuration.

```shell
simplelocalize [command] ...parameters
```

### Upload translations

Command uploads translation files from given `<UPLOAD_PATH>` to SimpleLocalize.

```shell
simplelocalize upload 
  --apiKey <PROJECT_API_KEY>
  --uploadPath <UPLOAD_PATH>
  --uploadFormat <UPLOAD_FORMAT>
  --uploadOptions <UPLOAD_OPTIONS>
```

`--uploadOptions` parameter is optional. Use `REPLACE_TRANSLATION_IF_FOUND` option to update existing translations.

Learn more about [upload translations command](https://simplelocalize.io/docs/cli/upload-translations/).

### Download translations

Command downloads translation files from SimpleLocalize to given `<DOWNLOAD_PATH>`.

```shell
simplelocalize download 
  --apiKey <PROJECT_API_KEY>
  --downloadPath <DOWNLOAD_PATH>
  --downloadFormat <DOWNLOAD_FORMAT>
  --downloadOptions <DOWNLOAD_OPTIONS>
```

`--downloadOptions` parameter is optional.

Learn more about [download translations command](https://simplelocalize.io/docs/cli/download-translations/).

### Sync translations

Sync command combines upload and download command executions.

```properties
simplelocalize sync 
  --apiKey <PROJECT_API_KEY>
  --downloadPath <DOWNLOAD_PATH>
  --downloadFormat <DOWNLOAD_FORMAT>
  --downloadOptions <DOWNLOAD_OPTIONS>
  --uploadPath <UPLOAD_PATH>
  --uploadFormat <UPLOAD_FORMAT>
  --uploadOptions <UPLOAD_OPTIONS>
```

`--downloadOptions` and `--uploadOptions` parameters are optional.

## Extract translation keys

Extract command finds and upload translation keys from project source code at `<SEARCH_DIRECTORY>` to SimpleLocalize.

```properties
simplelocalize extract 
  --apiKey <PROJECT_API_KEY>
  --searchDir <SEARCH_DIRECTOR>
  --projectType <PROJECT_TYPE> 
```

See [available project types](https://simplelocalize.io/docs/cli/i18n-keys-extraction/).


## Configuration file
Use configuration file in order to simplify your bash command. Arguments used in command always overrides properties set in configuration file. By default, SimpleLocalize will load configuration from file named `simplelocalize.yml`. You can load configuration from different location by using a `-c` parameters.

```properties
# Load default simplelocalize.yml file
simplelocalize upload

# Use configuration file at custom location
simplelocalize -c my-configuration.yml upload
```

### Sample configuration file

Filename: `simplelocalize.yml`

```yaml
# Get started with CLI: https://simplelocalize.io/docs/cli/get-started/
# Available formats: https://simplelocalize.io/docs/general/file-formats/
# Available import/export options: https://simplelocalize.io/docs/general/options/
# Support: contact@simplelocalize.io

# Project API Key
apiKey: API_KEY

# Properties used by 'upload' command
uploadPath: ./src/{lang}/{ns}.json
uploadFormat: single-language-json
uploadOptions:
  # by default, the 'upload' command only adds new keys and fills empty translations, 
  # add this option to overwrite existing translations with values from the uploaded file
  - REPLACE_TRANSLATION_IF_FOUND 

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

CLI command:
```
simplelocalize upload 
  --apiKey <PROJECT_API_KEY>
  --uploadPath /locales/messages.json
  --uploadFormat multi-language-json
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

CLI command:
```
simplelocalize upload 
  --apiKey <PROJECT_API_KEY>
  --uploadPath /{lang}/index.json
  --uploadFormat single-language-json
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

CLI command:
```
simplelocalize upload 
  --apiKey <PROJECT_API_KEY>
  --uploadPath /{lang}/{ns}.json
  --uploadFormat single-language-json
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

Head to [simplelocalize.io/docs](https://simplelocalize.io/docs/cli/get-started/) to learn the SimpleLocalize basics

## How to build project, contribute or add features?

[Please see CONTRIBUTING.md file](https://github.com/simplelocalize/simplelocalize-cli/blob/bd71926809085048bbe76ec1fea205c70f885acb/CONTRIBUTING.md)

## License

Check LICENSE.md file

