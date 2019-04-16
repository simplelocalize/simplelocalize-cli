---
description: The best way to use SimpleLocalize with ReactJS
---

# ReactJS

[![NPM](https://camo.githubusercontent.com/18839ad1c65ef1e1c25cfb3843b5631e84d07ec6/68747470733a2f2f696d672e736869656c64732e696f2f6e706d2f762f72656163742d696e746c2d73696d706c656c6f63616c697a652e737667)](https://www.npmjs.com/package/react-intl-simplelocalize) [![JavaScript Style Guide](https://camo.githubusercontent.com/58fbab8bb63d069c1e4fb3fa37c2899c38ffcd18/68747470733a2f2f696d672e736869656c64732e696f2f62616467652f636f64655f7374796c652d7374616e646172642d627269676874677265656e2e737667)](https://standardjs.com/)

### Install

```bash
npm install --save react-intl-simplelocalize react-intl
```

or

```bash
yarn add react-intl-simplelocalize react-intl
```

### Configuration

* Setup account here: [https://simplelocalize.io](https://simplelocalize.io/)
* Create new FormatJS project.
* Copy project hash from settings tabs.
* Wrap your applications same as you do with redux `<Provider/>`.

```jsx
import SimpleLocalize from 'react-intl-simplelocalize'

const app = document.getElementById("root");

render(
  <SimpleLocalize projectToken="<PROJECT_TOKEN>">
      <App/>
  </SimpleLocalize>, app);
```

### Usage

Start using `<FormattedMessage />`components from `yahoo/react-intl`.

```jsx
import React from "react";
import {FormattedMessage} from "react-intl";

class Home extends React.Component {

  render() {
    return (<FormattedMessage id="hello" />);
  }

}

export default Footer;
```

That's all, all translations will be now loaded and injected automatically.

### Adding new i18n keys

#### Discovering new keys automatically

Consider use of [`simplelocalize-cli`](https://github.com/simplelocalize/simplelocalize-cli) for the best experience. This application will find and push all new i18n keys in seconds!

#### Adding new keys manually

You can always manually add new keys using [SimpleLocalize cloud](https://simplelocalize.io/).

### License

MIT ©

