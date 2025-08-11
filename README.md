<p align="center">
  <a href="https://simplelocalize.io/">
    <img src="static/logo-square-white-rounded.png" width="96" height="96">
  </a>
</p>
<h3 align="center">Translation Management for software projects</h3>
<p align="center">The easiest way to manage translation files for web and mobile apps.</p>

[![codecov](https://codecov.io/gh/simplelocalize/simplelocalize-cli/branch/master/graph/badge.svg)](https://codecov.io/gh/simplelocalize/simplelocalize-cli)

SimpleLocalize CLI is a command-line tool that allows you to manage translations in your software project,
and it's a great tool for CI/CD pipelines and localization automation.

- [Synchronize files](https://simplelocalize.io/auto-translation/) - keep in sync all your translation files
- [Auto-translate](https://simplelocalize.io/auto-translation/) - auto-translate texts in seconds
- [Host translations](https://simplelocalize.io/translation-hosting/) - manage your hosted translations
- [GitHub Actions support](https://github.com/simplelocalize/github-action-cli/) - seemles integration with GitHub Actions

## Installation

The installation process is automated by command-line scripts. Both scripts for Windows (PowerShell) and macOS/Linux/Windows downloads a binary file with CLI, copies it to user files and makes it available to run anywhere in the system from a command-line. The CLI supports macOS (both Apple Silicon and Intel), Linux (x86 and ARM64), and Windows.

```bash
# macOS / Linux / Windows (WSL) 
curl -s https://get.simplelocalize.io/2.10/install | bash

# Windows (PowerShell)
. { iwr -useb https://get.simplelocalize.io/2.10/install-windows } | iex;

# npm - https://github.com/simplelocalize/simplelocalize-cli-npm
npm install @simplelocalize/cli
```

[GitHub Wiki: Installation](https://github.com/simplelocalize/simplelocalize-cli/wiki)

## Usage

The command-line tool offers several commands to execute. All of them requires Project API Key that is unique for each project. 
You can set `apiKey` via [configuration file](/simplelocalize-cli/wiki/Configuration-file), pass it as parameter with `--apiKey` or by environment variable `SIMPLELOCALIZE_API_KEY`.

```bash
simplelocalize [command] ...parameters
```

[GitHub Wiki: Usage](https://github.com/simplelocalize/simplelocalize-cli/wiki)

## Upload translations

Upload command takes your local files and uploads them to SimpleLocalize.

```bash
simplelocalize upload 
  --apiKey PROJECT_API_KEY
  --uploadPath INPUT_FILE
  --uploadFormat FILE_FORMAT
```

[GitHub Wiki: Upload translations](/simplelocalize/simplelocalize-cli/wiki/Upload-translations)

## Download translations

Download command takes translations from SimpleLocalize and saves them to your local files.

```bash
simplelocalize download 
  --apiKey PROJECT_API_KEY
  --downloadPath DOWNLOAD_PATH
  --downloadFormat FILE_FORMAT
```

[GitHub Wiki: Download translations](/simplelocalize/simplelocalize-cli/wiki/Download-translations)

## Auto-translate strings

Auto-translate command starts auto-translation tasks for project languages and waits for the finish. 

```bash
simplelocalize auto-translate --apiKey PROJECT_API_KEY
```

[GitHub Wiki: Auto-translate strings](/simplelocalize/simplelocalize-cli/wiki/Auto%E2%80%90translate-strings)

## Translation Hosting

There are two commands that operates on [translation hosting](https://simplelocalize.io/translation-hosting/) resources which is `publish` and `pull`.

### Publish translations

Publish translations between translation editor and hosting or between hosting environments.

```bash
simplelocalize publish
  --apiKey PROJECT_API_KEY
  --environment _latest
```

[GitHub Wiki: Pull resources](/simplelocalize/simplelocalize-cli/wiki/Translation-Hosting)

### Pull resources

Pull command downloads files from translation hosting.

```bash
simplelocalize pull
  --apiKey PROJECT_API_KEY
  --pullPath ./hosting/
  --environment _latest
```

[GitHub Wiki: Pull resources](/simplelocalize/simplelocalize-cli/wiki/Translation-Hosting)

## Additional commands

### Initalize configuration file

Command creates a sample [configuration file](/simplelocalize/simplelocalize-cli/wiki/Configuration-file) in the current directory.

```bash
simplelocalize init
```

[GitHub Wiki: Additional commands](/simplelocalize/simplelocalize-cli/wiki/Additional-commands)

### Get project details

Command gets project details and prints them.

```bash
simplelocalize status --apiKey PROJECT_API_KEY
```

[GitHub Wiki: Additional commands](/simplelocalize/simplelocalize-cli/wiki/Additional-commands)

### Purge translations

Command removes all translations, translation keys and languages.

```bash
simplelocalize purge --apiKey PROJECT_API_KEY
```

[GitHub Wiki: Additional commands](/simplelocalize/simplelocalize-cli/wiki/Additional-commands)

### Extract translation keys

Extract command finds translation keys and translations from the source code.

```bash
simplelocalize extract --searchDir SEARCH_DIRECTORY --projectType PROJECT_TYPE 
```

[GitHub Wiki: Additional commands](/simplelocalize/simplelocalize-cli/wiki/Additional-commands)

## Configuration file

Create configuration file to to simplify the bash commands. Arguments used in command always override properties set in the configuration file.

```bash
# It load simplelocalize.yml file by default
simplelocalize upload
```

[GitHub Wiki: Configuration file](/simplelocalize/simplelocalize-cli/wiki/Configuration-file)

## Proxy support

SimpleLocalize CLI supports HTTP and HTTPS proxies, and it respects the `http_proxy`, `https_proxy` environment
variables.

Here are some examples of how to set proxy environment variables in Linux and macOS:

```bash
export http_proxy=http://someproxy.com
export http_proxy=http://someproxy.com:8080
export http_proxy=http://user:password@someproxy.com:8080
```

[GitHub Wiki: Proxy support](/simplelocalize/simplelocalize-cli/wiki/Proxy-Support)

## Support

Please refer to the [official SimpleLocalize documentation](https://simplelocalize.io/docs/cli/get-started/). That should help you troubleshoot common issues. For additional help, you can reach out to us on one of these channels:

- [GitHub](https://github.com/simplelocalize/simplelocalize-cli/issues) (Bug and issue reports)
- [Email](mailto:contact@simplelocalize.io) (General support)
- [Changelog](https://simplelocalize.io/changelog/) (Product updates)
- [YouTube](https://www.youtube.com/channel/UCBpYo2UnHwDGyK175SAKTig) (How-to tutorials)

## License

See [LICENSE](/LICENSE) for more details.
