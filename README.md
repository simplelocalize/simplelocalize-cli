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
$ curl -s -L "https://github.com/simplelocalize/simplelocalize-cli/releases/download/0.0.0/simplelocalize" | bash
```

## 🛠 Configuration

- Create account here: https://simplelocalize.io/register
- Create example project
- Go to project settings and download configuration properties

**Example `simplelocalize.properties`**
```properties
search.dir=/Users/{YOUR_NAME}/Workspace/MyProject/src
client.id=<YOUR_CLIENT_ID>
secret=<YOUR_SECRET>
project.token=<PROJECT_TOKEN>
project.type=<PROJECT_TYPE>
```

## 🚀 Usage

```bash
$ simplelocalize-cli
```
Application will find all i18n keys and push them to SimpleLocalize cloud.

## 💡 Further work

- Support more project types

## 👩‍⚖️ License

MIT © [](https://github.com/)
