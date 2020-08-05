![logo](https://simplelocalize.io/public/github-banner-cli.png)

![Tests](https://github.com/simplelocalize/simplelocalize-cli/workflows/Run%20Tests/badge.svg)
![Build native executables](https://github.com/simplelocalize/simplelocalize-cli/workflows/Build%20executables/badge.svg?branch=master)
[![Maintainability](https://api.codeclimate.com/v1/badges/af2f6a7680929a8dba41/maintainability)](https://codeclimate.com/github/simplelocalize/simplelocalize-cli/maintainability)
[![codecov](https://codecov.io/gh/simplelocalize/simplelocalize-cli/branch/master/graph/badge.svg)](https://codecov.io/gh/simplelocalize/simplelocalize-cli)

## 💡 What it does?

We've built SimpleLocalize CLI that extracts i18n terms from your project files, and pushes them to the [SimpleLocalize](https://app.simplelocalize.io), where you can to translate it, and publish to the CDN or export in a desired format.

For example to translate frontend application in ReactJS you can use 3rd party library like [yahoo/react-intl](https://github.com/yahoo/react-intl):

Example [yahoo/react-intl](https://github.com/yahoo/react-intl) usage in code:
```jsx
<FormattedMessage id="LOGIN"/>
```

Thanks to SimpleLocalize CLI, key: `LOGIN`  will be found and pushed to the [SimpleLocalize](https://app.simplelocalize.io) automatically.

## 🚀 How to use it

* [Create a new project](https://app.simplelocalize.io/dashboard)
* [Download `simplelocalize.yml` file](https://i.imgur.com/7LFtHeG.png)
* Put `simplelocalize.yml` file in project root directory
* Setup `projectType` property. [See list below](https://github.com/simplelocalize/simplelocalize-cli#-supported-libraries)
* Run in terminal

```bash
$ cd ~/MyProject #same place where the simplelocalize.yml is
$ curl -s https://get.simplelocalize.io | bash
```

Done! CLI will find i18n terms, and push them to [SimpleLocalize](https://app.simplelocalize.io).

**Example** `simplelocalize.yml`

```yaml
uploadToken: <PROJECT_UPLOAD_TOKEN>
projectType: <SEE_BELOW>
```

## 🔌 Supported libraries

| Library | `projectType` value | Is supported? | 
| ------------- |-------------:|:----:|
| [react-intl](https://github.com/yahoo/react-intl)      | yahoo/react-intl | [x]
| [jekyll-multiple-languages-plugin](https://github.com/Anthony-Gaudino/jekyll-multiple-languages-plugin)      | Anthony-Gaudino/jekyll-multiple-languages-plugin      |   [ ] |
| Standard Android internationalization | google/android      |    [x] |
| Standard iOS internationalization | apple/ios      |    [ ] |
| [react-i18next](https://github.com/i18next/react-i18next) | i18next/react-i18next      |    [ ] |
| [ember-intl](https://github.com/ember-intl/ember-intl) | ember-intl/ember-intl      |    [ ] |
| [dust-intl](https://github.com/yahoo/dust-intl) | yahoo/dust-intl      |    [ ] |
| [handlebars-intl](https://github.com/yahoo/handlebars-intl) | yahoo/handlebars-intl      |    [ ] |


## 🤖 Workflow automation

SimpleLocalize can be integrated with any CI/CD service, simply put bash script somewhere in your scripting environment to find and push translation keys:

```bash
$ curl -s https://get.simplelocalize.io | bash
```
The best place to run SimpleLocalize script is **after successful build** because this will not result a wrong internationalisation keys caused by invalid syntax

## ⚙️ Available options
SimpleLocalize CLI can be customized using `simplelocalize.yaml` file. See examples below.

##### Ignoring keys

```yaml
uploadToken: <PROJECT_UPLOAD_TOKEN>
projectType: <PROJECT_TYPE>
ignoredKeys:
    - "HEY"
    - "PLEASE"
    - "DO NOT IGNORE ME"
    - ":("
```

##### Custom search directory
If you would like to search translation keys in some specific path you  can achieve this by adding searchDir and path where the CLI should search keys.

```yaml
uploadToken: <PROJECT_UPLOAD_TOKEN>
projectType: <YOUR_PROJECT_TYPE>
searchDir: /Users/jpomykala/Workspace/MyProject
```
Please pay attention to what you are putting in the `searchDir` property. This may cause high CPU and disc usage due to this will be looking for files to process and try to find translation keys.

##### Configuration profiles
If you would like to use simplelocalize.yml from custom location or with custom name like simplelocalize-dev.yml file. This can be easilly achieved by passing path as first argument.

```bash
$ curl -s https://get.simplelocalize.io | bash -s /tmp/simplelocalize-dev.yml
```

## 🤯 Problem with running CLI

In most cases you should use our regular script which is constantly optimized for everyone like below
```bash
$ curl -s https://get.simplelocalize.io | bash
```
If something is not working properly please [create an issue](https://github.com/simplelocalize/simplelocalize-cli/issues/new) and provide script output. In meantime, you can use one of 2 other options to run CLI.


##### Option 1: I have Java 11+ installed on my system, let me just use app in *.jar file. 
```bash
$ curl -s https://get.simplelocalize.io/run-jar | bash
```

##### Option 2: I want to use *.jar file, but I don't have JDK installed.
```bash
$ curl -s https://get.simplelocalize.io/install-jdk-run-jar | bash
```


## 👩‍⚖️ License

MIT © 

