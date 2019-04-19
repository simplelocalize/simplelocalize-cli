import React, {Component} from "react";
import {Col, Grid} from "react-bootstrap";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {FormattedMessage} from "react-intl";
import Header from "../components/Header";
import SocialLogin from "../containers/SocialLogin";

class LoginPage extends Component {

  render() {
    return (
      <div>
        {data ? <FormattedMessage id="SAVE"/> : <FormattedMessage id="CREATE"/>}
      </div>
    );
  }
}

export default LoginPage;

