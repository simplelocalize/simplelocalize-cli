[![Build Status](https://travis-ci.org/simplelocalize/simplelocalize-cli.svg?branch=master)](https://travis-ci.org/simplelocalize/simplelocalize-cli)
[![Maintainability](https://api.codeclimate.com/v1/badges/af2f6a7680929a8dba41/maintainability)](https://codeclimate.com/github/simplelocalize/simplelocalize-cli/maintainability)
[![codecov](https://codecov.io/gh/simplelocalize/simplelocalize-cli/branch/master/graph/badge.svg)](https://codecov.io/gh/simplelocalize/simplelocalize-cli)
[![Gitter](https://badges.gitter.im/simplelocalize-io/community.svg)](https://gitter.im/simplelocalize-io/community?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge)

# SimpleLocalize CLI

## ⚡️ What it does?

Application will find all keys which are used by [https://github.com/yahoo/react-intl](https://github.com/yahoo/react-intl). Like:

```jsx
<FormattedMessage id="some_key"/>
```

or

```javascript
intl.formatMessage(defineMessages({
  id: "some_key"
});
```

Keys will be pushed to the [SimpleLocalize cloud](https://app.simplelocalize.io/login), where you will be able to manage translations for multiple languages with ease, and publish them to our CDN.

Consider use of [`react-intl-simplelocalize`](https://github.com/simplelocalize/react-intl-simplelocalize) for the best experience.

## 🛠 Configuration

* Create account here: [https://simplelocalize.io/register](https://simplelocalize.io/register)
* Create example project
* Go to project settings and download configuration properties

**Example** `simplelocalize.yml`

```text
clientId: <YOUR_CLIENT_ID>
clientSecret: <YOUR_SECRET>
projectToken: <PROJECT_TOKEN>
projectType: <SEE_SUPPORT_LIST>
```

## 🚀 Usage

```bash
$ curl -sL https://cdn.simplelocalize.io/cli/simplelocalize | bash
```

_Remember to put_ `simplelocalize.yml` _in same directory where you invoke this command._ CLI will find all i18n keys in current directory and push them to SimpleLocalize cloud.

## 💡 Supported libraries

* [ ] [react-i18next](https://github.com/i18next/react-i18next)
* [x] [yahoo/react-intl](https://github.com/yahoo/react-intl)
* [ ] [ember-intl](https://github.com/ember-intl/ember-intl)
* [ ] [dust-intl](https://github.com/yahoo/dust-intl)
* [ ] [handlebars-intl](https://github.com/yahoo/handlebars-intl)
* [x] Standard Android internationalization
* [x] Standard iOS internationalization
* [x] [Request library support here!](https://github.com/simplelocalize/simplelocalize-cli/issues/new)

## 👩‍⚖️ License

MIT © 

