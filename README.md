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

## ⚙️ Install

```bash
//work in progress
```
Download latest version:
[https://github.com/simplelocalize/simplelocalize-cli/releases](https://github.com/simplelocalize/simplelocalize-cli/releases)

## 🛠 First configuration

- Create account here: https://simplelocalize.io/register
- Create example project
- Go to project settings and download configuration properties
- Setup project source path in `search.dir` 
- Run `./simplelocalize.sh` and you are done! 💃

```properties
# simplelocalize.properties example
search.dir=/Users/{YOUR_NAME}/Workspace/MyProject/src
client.id=<YOUR_CLIENT_ID>
secret=<YOUR_SECRET>
project.write.token=<YOUR_PROJECT_WRITE_TOKEN>
```

## 🚀 Usage

```bash
java -jar simplelocalize-1.0.0.jar
```
Application will find all i18n keys and push them to SimpleLocalize cloud.

## 💡 Further work

- Add easier option to install (bundle with Java)
- Support more project types

## 👩‍⚖️ License

MIT © [](https://github.com/)
