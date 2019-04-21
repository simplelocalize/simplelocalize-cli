[![Build Status](https://travis-ci.org/simplelocalize/simplelocalize-cli.svg?branch=master)](https://travis-ci.org/simplelocalize/simplelocalize-cli)
[![Maintainability](https://api.codeclimate.com/v1/badges/af2f6a7680929a8dba41/maintainability)](https://codeclimate.com/github/simplelocalize/simplelocalize-cli/maintainability)
[![codecov](https://codecov.io/gh/simplelocalize/simplelocalize-cli/branch/master/graph/badge.svg)](https://codecov.io/gh/simplelocalize/simplelocalize-cli)
[![Gitter](https://badges.gitter.im/simplelocalize-io/community.svg)](https://gitter.im/simplelocalize-io/community?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge)

# SimpleLocalize CLI


## ⚡️ What it does?

CLI will find i18n keys in you source code which are used by your library and push them to the cloud, where you will be able to translate keys and publish to the CDN and download in desired format.

For example to translate frontend application in ReactJS you can use [yahoo/react-intl](https://github.com/yahoo/react-intl) which looks like follows:

```jsx
<FormattedMessage id="some_key"/>
```

Thanks to SimpleLocalize-CLI, key: LOGIN  will be pushed to the SimpleLocalize cloud, where you will be able to translate this key to multiple languages, and publish it to the CDN or download in desired format.

## 🚀 Usage

* [Create a new project](https://app.simplelocalize.io/dashboard)
* Go to project settings and download configuration file
* Put `simplelocalize.yml` file in project root directory
* Setup `projectType` property. [Show available projectType values](https://simplelocalize.gitbook.io/simplelocalize/supported-libraries)
* Run in terminal

```bash
$ cd ~/MyProject #same place where the simplelocalize.yml is
$ curl -sL https://cdn.simplelocalize.io/cli/simplelocalize | bash
```


**Example** `simplelocalize.yml`

```yaml
clientId: <YOUR_CLIENT_ID>
clientSecret: <YOUR_SECRET>
projectToken: <PROJECT_TOKEN>
projectType: <SEE_SUPPORT_LIST>
```
[✅ SEE SUPPORTED LIBRARIES LIST](https://simplelocalize.gitbook.io/simplelocalize/supported-libraries)

## 💡 Supported libraries

* [x] [Request library support here!](https://github.com/simplelocalize/simplelocalize-cli/issues/new)
* [x] [yahoo/react-intl](https://github.com/yahoo/react-intl)
* [ ] Standard Android internationalization
* [ ] Standard iOS internationalization
* [ ] [react-i18next](https://github.com/i18next/react-i18next)
* [ ] [ember-intl](https://github.com/ember-intl/ember-intl)
* [ ] [dust-intl](https://github.com/yahoo/dust-intl)
* [ ] [handlebars-intl](https://github.com/yahoo/handlebars-intl)

## 👩‍⚖️ License

MIT © 

