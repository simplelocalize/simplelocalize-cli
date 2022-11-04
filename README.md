

![Tests](https://github.com/simplelocalize/simplelocalize-cli/workflows/Run%20Tests/badge.svg)
[![Maintainability](https://api.codeclimate.com/v1/badges/af2f6a7680929a8dba41/maintainability)](https://codeclimate.com/github/simplelocalize/simplelocalize-cli/maintainability)
[![codecov](https://codecov.io/gh/simplelocalize/simplelocalize-cli/branch/master/graph/badge.svg)](https://codecov.io/gh/simplelocalize/simplelocalize-cli)

## What it does?

SimpleLocalize CLI offers:
- finding and extracting translation keys in your project files
- uploading translation files or translation keys
- downloading translation files
- getting translation project status
- publishing translations to the CDN.

## Installation

The installation process is automated by command-line scripts. Both scripts for Windows (PowerShell) and macOS/Linux/Windows downloads a binary file with CLI, copies it to user files and makes it available to run anywhere in the system from a command-line.

```shell
# macOs / Linux / Windows (WSL)
curl -s https://get.simplelocalize.io/2.1/install | bash

# Windows (PowerShell)
. { iwr -useb https://get.simplelocalize.io/2.1/install-windows } | iex;
```

To change or update the CLI version, run the installation script with the desired version number in the URL. You can also put the exact CLI version in URL to 
explicitly point the version you want to use, e.g.: `https://get.simplelocalize.io/2.0.6/install` or `https://get.simplelocalize.io/2.0.6/install-windows`. See [releases](https://github.com/simplelocalize/simplelocalize-cli/releases) for the list of available versions.



## Usage

SimpleLocalize CLI offers a several commands to invoke, All of them requires `--apiKey=KEY` parameter that is unique for each project. 

```shell
simplelocalize [command] ...parameters
```

Commands:
- `status` - gets translation project details
- `upload` - uploads translation files or translation keys 
- `download` - downloads translation files
- `sync` - uploads translation files and downloads translation files
- `pull` - downloads translation files from [Translation Hosting](https://simplelocalize.io/translation-hosting)
- `publish` - publishes translations to [Translation Hosting](https://simplelocalize.io/translation-hosting)
- `extract` - finds and extracts translation keys in your project files

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

### Extract translation keys

Extract command finds and upload translation keys from project source code at `<SEARCH_DIRECTORY>` to SimpleLocalize.

```properties
simplelocalize extract 
  --apiKey <PROJECT_API_KEY>
  --searchDir <SEARCH_DIRECTOR>
  --projectType <PROJECT_TYPE> 
```

See [available project types](https://simplelocalize.io/docs/cli/i18n-keys-extraction/).


## Usage examples

Below you can find some examples of using SimpleLocalize CLI.

### Example: One file with translations

```bash
.
└── locales
    └── messages.json
```

Command:
```
simplelocalize upload 
  --apiKey <PROJECT_API_KEY>
  --uploadPath /locales/messages.json
  --uploadFormat multi-language-json
```


### Example: Single file in multiple language directories

```bash
.
├── ca
│   └── index.json
├── en
│   └── index.json
└── es
    └── index.json
```

Command:
```
simplelocalize upload 
  --apiKey <PROJECT_API_KEY>
  --uploadPath /{lang}/index.json
  --uploadFormat single-language-json
```

### Example: Multiple files in multiple language directories

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

Command:
```
simplelocalize upload 
  --apiKey <PROJECT_API_KEY>
  --uploadPath /{lang}/{ns}.json
  --uploadFormat single-language-json
```

### Pull resources from Translation Hosting

Downloads all translation hosting files to given directory in `--pullPath` parameter. It overwrites existing files and creates subdirectories if necessary. Available environment variables: `latest`, `production`.

```
simplelocalize pull 
  --apiKey <PROJECT_API_KEY>
  --pullPath ./hosting/
  --environment latest
```

### Publish resources to Translation Hosting

It publishes translation to Translation Hosting. It behaves exactly the same as publish buttons in the SimpleLocalize (Hosting tab).

- `--environment latest` gets translations from Translation Editor and publishes them to Translation Hosting to `latest` environment.
- `--environment production` gets translations from Translation Hosting (`latest`) and publishes them to Translation Hosting (`production`).

```
simplelocalize publish 
  --apiKey <PROJECT_API_KEY>
  --environment latest
```

### Getting project details

Command gets project details and prints them to the console.

```
simplelocalize publish 
  --apiKey <PROJECT_API_KEY>
```


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

# Properties used by 'pull' and 'publish' command    
pullPath: ./src/hosting/ 
environment: 'production' # or 'latest' 

```

## Documentation 

Visit [simplelocalize.io/docs/cli/get-started/](https://simplelocalize.io/docs/cli/get-started/) to get more information about SimpleLocalize CLI.

## License

Check LICENSE.md file

