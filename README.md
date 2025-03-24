<p align="center">
  <a href="https://simplelocalize.io">
    <img src="static/simplelocalize-git-banner.png" width="100%">
  </a>
</p>
<h3 align="center">Translation Management for software projects</h3>
<p align="center">The easiest way to manage translation files for web and mobile apps.</p>
<p align="center">
  <a href="https://simplelocalize.io/">
    <img src="static/simplelocalize-git-hero.png">
  </a>
</p>

![Tests](https://github.com/simplelocalize/simplelocalize-cli/workflows/Run%20Tests/badge.svg)
[![Maintainability](https://api.codeclimate.com/v1/badges/af2f6a7680929a8dba41/maintainability)](https://codeclimate.com/github/simplelocalize/simplelocalize-cli/maintainability)
[![codecov](https://codecov.io/gh/simplelocalize/simplelocalize-cli/branch/master/graph/badge.svg)](https://codecov.io/gh/simplelocalize/simplelocalize-cli)

SimpleLocalize CLI is a command-line tool that allows you to manage translations in your software project,
and it's a great tool for CI/CD pipelines and localization automation.

- **Upload and download translations.** The main purpose of the command-line tool is to upload and download translation files from [Translation Editor](https://simplelocalize.io/translation-editor/) to your project.
- **[Auto-translate](https://simplelocalize.io/auto-translation/) strings.** Start auto-translation jobs from the command-line and get translated strings in a few minutes.
- **[Hosted translations management](https://simplelocalize.io/translation-hosting/).** Push and pull translations from Translation Hosting to your project using the command-line.
- **[GitHub Actions support](https://github.com/simplelocalize/github-action-cli/).** Use SimpleLocalize CLI in your GitHub Actions workflows.
- **Check project status.** Get project details from the command-line and check if your translations are up-to-date.

## Installation

The installation process is automated by command-line scripts. Both scripts for Windows (PowerShell) and macOS/Linux/Windows downloads a binary file with CLI, copies it to user files and makes it available to run anywhere in the system from a command-line.

```shell
# macOs / Linux / Windows (WSL)
curl -s https://get.simplelocalize.io/2.7/install | bash

# Windows (PowerShell)
. { iwr -useb https://get.simplelocalize.io/2.7/install-windows } | iex;

# npm (Work in progress: https://github.com/simplelocalize/simplelocalize-cli-npm)
npm install @simplelocalize/cli
```

To change or update the CLI version, run the installation script with the desired version number in the URL, e.g.: 
- `https://get.simplelocalize.io/2.0.6/install` installs version 2.0.6 on macOS/Linux/Windows (WSL)
- `https://get.simplelocalize.io/2.0.6/install-windows` installs version 2.0.6 on Windows (PowerShell)

See [releases](https://github.com/simplelocalize/simplelocalize-cli/releases) for the list of available versions.

## Usage

The command-line tool offers several commands to execute. All of them requires Project API Key that is unique for each project. 
You can set `apiKey` in simplelocalize.yml configuration file, pass it as parameter with `--apiKey` or set it by environment variable `SIMPLELOCALIZE_API_KEY`.

```shell
simplelocalize [command] ...parameters
```

Available commands:
- `init` - creates a sample configuration file
- `status` - gets translation project details
- `upload` - uploads translation files or translation keys 
- `download` - downloads translation files
- `auto-translate` - starts [auto-translation](https://simplelocalize.io/auto-translation/) jobs
- `pull` - downloads translation files from [Translation Hosting](https://simplelocalize.io/translation-hosting/)
- `publish` - publishes translations to [Translation Hosting](https://simplelocalize.io/translation-hosting/)
- `purge` - removes all translation, translation keys and languages from the project
- `extract` - finds and extracts translation keys in your project files

## Create configuration file

Command creates a sample configuration file in the current directory.
The configuration file simplifies the usage of the command-line tool
by providing a default configuration for the project and allowing to omit some parameters.

```shell
simplelocalize init
```

## Upload translations

Upload command takes your local files and uploads them to SimpleLocalize. You can specify a path to the file or use placeholders to upload many files at once. 
A good practise is to **upload only source translations** instead of uploading all translations on each run. You can use `{lang}` placeholder to specify language or locale and `{ns}` placeholder to specify namespace,
e.g.: `./src/translations/{lang}/{ns}.json`.

```shell
simplelocalize upload 
  --apiKey <PROJECT_API_KEY>
  --uploadPath <UPLOAD_PATH_PATTERN>
  --uploadFormat <UPLOAD_FORMAT>
```

Upload format is a format of the file(s) with translations. [See available upload formats](https://simplelocalize.io/docs/general/file-formats/)

**Additional parameters:**
- `--replace` allows you to **replace** existing translations with new ones,
- `--dryRun` allows you to **check** what translation files will be uploaded without actually uploading them,
- `--namespace` download translations for a specific namespace, e.g.: `--namespace home`,
- `--customerId` download translations for a specific customer, e.g.: `--customerId ikea`,
- `--uploadOptions` allows you to pass [additional options](https://simplelocalize.io/docs/general/options/) to the upload command, E.g.: `--options TRIM_LEADING_TRAILING_SPACES`. To pass multiple options, use comma as a separator.

> Since version 2.7 you can skip 'upload' part in the parameters names, e.g.: `--path` instead of `--uploadPath`.

Learn more about [upload translations command](https://simplelocalize.io/docs/cli/upload-translations/).

### Example: One file with multiple languages

```bash
.
└── locales
    └── messages.json
```

Command:
```
simplelocalize upload 
  --apiKey <PROJECT_API_KEY>
  --uploadPath ./locales/messages.json
  --uploadFormat multi-language-json
```

### Example: One file per language

In this example we upload only source translations from `./en/messages.json` and uses `--uploadLanguageKey en-GB` to specify language key for the uploaded file for the Translation Editor.
It's a recommended way to upload source translations.

```bash
.
├── ca
│   └── messages.json
├── en
│   └── messages.json
└── es
    └── messages.json
```

Command:
```
simplelocalize upload 
  --apiKey <PROJECT_API_KEY>
  --uploadPath ./en/index.json
  --uploadLanguageKey en-GB
  --uploadFormat single-language-json
```

### Example: One file per language using placeholders

In this example we use `{lang}` placeholder to upload **many files** at once and specify language key for each file. 
It's not a recommended way to upload source translations, as it uploads more files than necessary.

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

### Example: One file per language and namespace

In this example we use `{ns}` placeholder to upload **many files** at once for the English language. We used `--uploadLanguageKey en-GB` to specify language key for the uploaded file for the Translation Editor.  

```bash
.
├── italian
│   ├── common.json
│   └── home.json
├── english
│   ├── common.json
│   └── home.json
└── spanish
    ├── common.json
    └── home.json
```

Command:
```
simplelocalize upload 
  --apiKey <PROJECT_API_KEY>
  --uploadPath /english/{ns}.json
  --uploadLanguageKey en-GB
  --uploadFormat single-language-json
```


## Download translations

Download works similarly to the upload command, but this time it exports translation files from the Translation Editor to your local files.

```shell
simplelocalize download 
  --apiKey <PROJECT_API_KEY>
  --downloadPath <DOWNLOAD_PATH_PATTERN>
  --downloadFormat <DOWNLOAD_FORMAT>
```

Download format is a format of the file(s) with translations. [See available upload formats](https://simplelocalize.io/docs/general/file-formats/)

### Example

Same as before you can use `{lang}` and `{ns}` placeholders to download many files at once and specify language keys that should be downloaded, eg.:

```shell
simplelocalize download 
  --apiKey <PROJECT_API_KEY>
  --downloadPath ./src/{ns}/messages_{lang}.json
  --downloadFormat single-language-json
  --downloadLanguageKey en,de,fr
```

**Additional parameters:**
- `--downloadLanguageKey` allows you to download translation for specified languages. E.g.: `--languageKey en,de,fr`.
- `--downloadCustomerId` allows you to download translations for a specific customer. E.g.: `--customerId ikea`.
- `--downloadNamespace` allows you to download translations for a specific namespace. E.g.: `--namespace home`.
- `--downloadOptions` allows you to pass [additional options](https://simplelocalize.io/docs/general/options/) to the download command. E.g.: `--options WRITE_NESTED`.
- `--downloadSort` allows you to sort translations in the downloaded file. E.g.: `--sort NEWEST_KEYS_FIRST`. Available options: `NEWEST_KEYS_FIRST`, `NEWEST_KEYS_LAST`, `NAMESPACES`, `IMPORT_ORDER`.

> Since version 2.7 you can skip 'download' part in the parameters names, e.g.: `--path` instead of `--downloadPath`.

Learn more about [download translations command](https://simplelocalize.io/docs/cli/download-translations/).

## Auto-translate strings

Auto-translate command starts auto-translation tasks for all languages in the project or for languages specified in `--languageKeys` parameter.
Auto-translation configuration is taken from the last auto-translation job in the project for the given language.

```properties
simplelocalize auto-translate --apiKey <PROJECT_API_KEY>
```

**Additional parameters:**
- `--languageKeys` allows you to specify project language keys to auto-translate, e.g.: `--languageKeys en,de,fr`.

## Translation Hosting: Publish translations

It publishes translation to [Translation Hosting](https://simplelocalize.io/translation-hosting). It behaves exactly the same as publish buttons in the SimpleLocalize (Hosting tab).

Publishes translations from Translation Editor to the `_latest` environment.
```properties
simplelocalize publish --apiKey <PROJECT_API_KEY> --environment _latest
```

Publishes translations from the `_latest` environment to `_production` environment.
```properties
simplelocalize publish --apiKey <PROJECT_API_KEY> --environment _production
```

## Translation Hosting: Pull resources

Downloads all translation hosting files to given directory in `--pullPath` parameter. It overwrites existing files and creates subdirectories if necessary.

Pulls translations from the `_latest` environment.
```properties
simplelocalize pull --apiKey <PROJECT_API_KEY> --pullPath ./hosting/ --environment _latest
```

Pulls translations from the `_production` environment.
```properties
simplelocalize pull --apiKey <PROJECT_API_KEY> --pullPath ./hosting/ --environment _production
```

If you would like to filter files which should be downloaded you can use `--filterRegex` param,
e.g.: `--filterRegex '__index.json'` will download only `__index.json` file.


## Get project details

Command gets project details and prints them to the console.

```properties
simplelocalize status --apiKey <PROJECT_API_KEY>
```

## Purge translations

Command removes all translations, translation keys and languages from [Translation Editor](https://simplelocalize.io/translation-editor).

```properties
simplelocalize purge --apiKey <PROJECT_API_KEY>
```

**Additional parameters:**
- `--force` allows you to skip confirmation prompt.

## Extract translation keys

Extract command finds translation keys and translations from project source code at `<SEARCH_DIRECTORY>` and exports them to `extraction.json` file that uses `simplelocalize-json` file format.

```properties
simplelocalize extract 
  --searchDir <SEARCH_DIRECTOR>
  --projectType <PROJECT_TYPE> 
```

See [available project types](https://simplelocalize.io/docs/cli/i18n-keys-extraction/).


## Configuration file
Use configuration file to simplify your bash command.
Arguments used in command always override properties set in the configuration file.
By default, SimpleLocalize will load configuration from file named `simplelocalize.yml`.
You can load configuration from different location by using a `-c` parameters.

```properties
# Load default simplelocalize.yml file
simplelocalize upload

# Use configuration file at custom location
simplelocalize -c my-configuration.yml upload
```

## Sample configuration file

Filename: `simplelocalize.yml`

```yaml
# Get started with CLI: https://simplelocalize.io/docs/cli/get-started/
# Available formats: https://simplelocalize.io/docs/general/file-formats/
# Available import/export options: https://simplelocalize.io/docs/general/options/
# Support: contact@simplelocalize.io

# Project API Key
apiKey: API_KEY

# Properties used by 'upload' command
uploadPath: ./source-translations/messages_en.json
uploadLanguageKey: en-GB
uploadFormat: single-language-json
uploadOptions:
  # by default, the 'upload' command only adds new keys and fills empty translations, 
  # add this option to overwrite existing translations with values from the uploaded file
  - REPLACE_TRANSLATION_IF_FOUND 

# Properties used by 'download' command
downloadPath: ./output-translations/messages_{lang}.json
downloadLanguageKeys: ['de-DE', 'fr-FR', 'pl-PL']
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
environment: '_production' # or '_latest', or 'my_custom'
```

## Proxy support

SimpleLocalize CLI supports HTTP and HTTPS proxies, and it respects the `http_proxy`, `https_proxy` environment
variables.

Here are some examples of how to set proxy environment variables in Linux and macOS:

```shell
export http_proxy=http://someproxy.com
export http_proxy=http://someproxy.com:8080
export http_proxy=http://user:password@someproxy.com:8080
```

## Support

Please refer to the [official SimpleLocalize documentation](https://simplelocalize.io/docs/cli/get-started/). That should help you troubleshoot common issues. For additional help, you can reach out to us on one of these channels:

- [GitHub](https://github.com/simplelocalize/simplelocalize-cli/issues) (Bug and issue reports)
- [Email](mailto:contact@simplelocalize.io) (General support)
- [Changelog](https://simplelocalize.io/changelog/) (Product updates)
- [YouTube](https://www.youtube.com/channel/UCBpYo2UnHwDGyK175SAKTig) (How-to tutorials)

## License

See [LICENSE](/LICENSE) for more details.
