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

Upload command takes your local files and uploads them to SimpleLocalize.

```shell
simplelocalize upload 
  --apiKey PROJECT_API_KEY
  --uploadPath INPUT_FILE
  --uploadFormat FILE_FORMAT
```

[GitHub Wiki: Upload translations](/simplelocalize/simplelocalize-cli/wiki/Upload-translations)

## Download translations

Download command takes translation from SimpleLocalize and saves them to your local files.

```shell
simplelocalize download 
  --apiKey PROJECT_API_KEY
  --downloadPath DOWNLOAD_PATH
  --downloadFormat FILE_FORMAT
```

[GitHub Wiki: Download translations](/simplelocalize/simplelocalize-cli/wiki/Download-translations)

## Auto-translate strings

Auto-translate command starts auto-translation tasks for all languages in the project or for languages specified in `--languageKeys` parameter. 

```properties
simplelocalize auto-translate 
  --apiKey PROJECT_API_KEY
  --languageKeys en,fr,de,pl
```

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

[GitHub Wiki: Configuration file](/simplelocalize/simplelocalize-cli/wiki/Configuration-file)

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
