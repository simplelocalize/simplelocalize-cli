![logo](https://i.imgur.com/du8JQ2L.png)


[![Build Status](https://travis-ci.org/simplelocalize/simplelocalize-cli.svg?branch=master)](https://travis-ci.org/simplelocalize/simplelocalize-cli)
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

## 👩‍⚖️ License

MIT © 

