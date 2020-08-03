![logo](https://i.imgur.com/du8JQ2L.png)

![Tests](https://github.com/simplelocalize/simplelocalize-cli/workflows/Run%20Tests/badge.svg)
![Build native executables](https://github.com/simplelocalize/simplelocalize-cli/workflows/Build%20executables/badge.svg?branch=master)
[![Maintainability](https://api.codeclimate.com/v1/badges/af2f6a7680929a8dba41/maintainability)](https://codeclimate.com/github/simplelocalize/simplelocalize-cli/maintainability)
[![codecov](https://codecov.io/gh/simplelocalize/simplelocalize-cli/branch/master/graph/badge.svg)](https://codecov.io/gh/simplelocalize/simplelocalize-cli)




## 💡 What it does?

CLI will find i18n keys in you source code which are used by your library and push them to the cloud, where you will be able to translate keys and publish to the CDN and download in desired format.

For example to translate frontend application in ReactJS you can use [yahoo/react-intl](https://github.com/yahoo/react-intl) which looks like follows:

```jsx
<FormattedMessage id="LOGIN"/>
```

Thanks to SimpleLocalize command line tool, key: `LOGIN`  will be pushed to the [SimpleLocalize cloud](https://app.simplelocalize.io), where you will be able to translate this key into multiple languages, and publish it to the CDN or download in desired format.

## 🚀 Usage

* [Create a new project](https://app.simplelocalize.io/dashboard)
* Go to project settings and download configuration file
* Put `simplelocalize.yml` file in project root directory
* Setup `projectType` property. [Show available projectType values](https://simplelocalize.gitbook.io/simplelocalize/supported-libraries)
* Run in terminal

```bash
$ cd ~/MyProject #same place where the simplelocalize.yml is
$ curl -s https://get.simplelocalize.io | bash
```


**Example** `simplelocalize.yml`

```yaml
uploadToken: <PROJECT_UPLOAD_TOKEN>
projectType: <SEE_SUPPORT_LIST>
```
[✅ SEE SUPPORTED LIBRARIES LIST](https://simplelocalize.gitbook.io/simplelocalize/supported-libraries)

## 🔌 Supported libraries

* [x] [Request library support here!](https://github.com/simplelocalize/simplelocalize-cli/issues/new)
* [x] [yahoo/react-intl](https://github.com/yahoo/react-intl)
* [ ] Jekyll: [Anthony-Gaudino/jekyll-multiple-languages-plugin](https://github.com/Anthony-Gaudino/jekyll-multiple-languages-plugin)
* [x] Standard Android internationalization
* [ ] Standard iOS internationalization
* [ ] [react-i18next](https://github.com/i18next/react-i18next)
* [ ] [ember-intl](https://github.com/ember-intl/ember-intl)
* [ ] [dust-intl](https://github.com/yahoo/dust-intl)
* [ ] [handlebars-intl](https://github.com/yahoo/handlebars-intl)

## 👩‍⚖️ License

MIT © 

