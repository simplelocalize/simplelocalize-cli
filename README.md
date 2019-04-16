# SimpleLocalize-CLI 

## ⚡️ What it does?

Application will find all keys which are used by [https://github.com/yahoo/react-intl](https://github.com/yahoo/react-intl).
Like:
```jsx
<FormattedMessage id="some_key"/>
```
or 
```js
intl.formatMessage(defineMessages({
  id: "some_key"
});
```
Keys will be pushed to the [SimpleLocalize cloud](https://app.simplelocalize.io/login), where you will be able to manage translations for multiple languages with ease, and publish them to our CDN.

Consider use of [`react-intl-simplelocalize`](https://github.com/simplelocalize/react-intl-simplelocalize) for the best experience. 

## ⚙️ Installation

```bash
$ curl -sL https://github.com/simplelocalize/simplelocalize-cli/releases/download/0.0.0/simplelocalize | bash
```

## 🛠 Configuration

- Create account here: https://simplelocalize.io/register
- Create example project
- Go to project settings and download configuration properties

**Example `simplelocalize.yml`**
```yml
clientId: <YOUR_CLIENT_ID>
clientSecret: <YOUR_SECRET>
projectToken: <PROJECT_TOKEN>
projectType: <SEE_SUPPORT_LIST>
```

## 🚀 Usage

```bash
$ curl -sL https://cdn.simplelocalize.io/cli/simplelocalize | bash
```
*Remember to put `simplelocalize.yml` in same directory where you invoke this command.*
CLI will find all i18n keys in current directory and push them to SimpleLocalize cloud.

## 💡 Supported libraries

- [ ] [react-i18next](https://github.com/i18next/react-i18next)
- [x] [yahoo/react-intl](https://github.com/yahoo/react-intl)
- [ ] [ember-intl](https://github.com/ember-intl/ember-intl)
- [ ] [dust-intl](https://github.com/yahoo/dust-intl)
- [ ] [handlebars-intl](https://github.com/yahoo/handlebars-intl)
- [x] Standard Android internationalization
- [x] Standard iOS internationalization
- [x] [Request library support here!](https://github.com/simplelocalize/simplelocalize-cli/issues/new)

## 👩‍⚖️ License

MIT © [](https://github.com/)
